package websocket.service;

import websocket.dto.api.request.BookingCreateRequest;
import websocket.dto.api.response.BookingHistoryResponse;
import websocket.dto.api.response.ScheduleSlotResponse;
import websocket.entity.lesson.Booking;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Schedule;
import websocket.entity.user.User;
import websocket.exception.InvalidDateException;
import websocket.repository.BookingRepository;
import websocket.repository.ScheduleRepository;
import websocket.repository.UserRepository;
import websocket.type.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    // Booking-History-01 [학생] 예약 현황 조회
    // Class History 탭과 Timetable 탭에서 공통으로 사용
    public List<BookingHistoryResponse> getMyBookings(Long studentId){
        // 학생이 실제로 존재하는지 먼저 검증
        if(!userRepository.existsById(studentId)){
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        // Repository에서 Entity 조회
        List<Booking> bookings = bookingRepository.findAllByStudentIdOrderByScheduleStartTimeDesc(studentId);
        // 예약 내역이 없으면 예외 발생
        if(bookings.isEmpty()){
            throw new IllegalArgumentException("예약 내역이 존재하지 않습니다.");
        }
        // Entity -> DTO 변환
        return bookings.stream()
                .map(BookingHistoryResponse::from)
                .collect(Collectors.toList());
    }

    // Class-Booking-01 [학생] 선생님별 예약 가능한 수업 일정 검색
    // 날짜를 받으면 그 날의 00:00 ~ 23:59 사이 스케줄을 조회
    public List<ScheduleSlotResponse> getAvailableSchedules(Long teacherId, LocalDate date){
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        // 선생님 존재 여부 확인
        userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 선생님입니다."));

        // 예약 안된(is Booked = false) 스케줄만 가져옴
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndStartTimeBetweenOrderByStartTimeAsc(
                teacherId, startOfDay, endOfDay
        );

        return schedules.stream()
                .map(ScheduleSlotResponse::from)
                .collect(Collectors.toList());
    }

    // Class-Booking-02 [학생] 수업 예약 요청
    // System-Logic-01 중복 예약 방지 로직
    // Transactional(readOnly = false) 필요 - 데이터 저장, 수정 발생
    @Transactional
    public Long registerBooking(Long studentId, BookingCreateRequest request){
        // 학생 조회
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        // 스케줄 조회
        Schedule schedule = scheduleRepository.findByIdWithLock(request.scheduleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));

        // System-Logic-02 시간 유효성 검사
        validateBookingTime(schedule);

        // 수업 정보에 관한 정보
        Lesson lesson = schedule.getLesson();
        if(lesson == null){
            throw new IllegalStateException("스케줄에 연결된 수업정보가 없습니다.");
        }

        // System-Logic-01 중복 예약 검증(1차: 스케줄 자체 상태 확인)
        if(schedule.getIsBooked()){
            throw new IllegalStateException("이미 예약된 시간입니다."); // OverLapException 대체
        }

        // System-Logic-01 중복 예약 검증(2차:Booking 테이블 확인)
        boolean isDuplicate = bookingRepository.existsByScheduleIdAndStatusNot(
                schedule.getId(), BookingStatus.CANCELLED_BY_STUDENT
        );
        if(isDuplicate){
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }

        // 예약 생성 및 저장
        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setLesson(lesson);
        booking.setSchedule(schedule);
        booking.setStatus(BookingStatus.PENDING);
        booking.setRequestMessage(request.requestMessage());

        bookingRepository.save(booking);

        // 스케줄 상태 변경(예약됨 처리) -> Dirty Checking
        schedule.setIsBooked(true);
        return booking.getId();
    }

    // Class-Booking-03 수업 예약 취소
    @Transactional
    public void cancelBooking(Long studentId, Long bookingId){
        // 예약 조회
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        // 권한 검증(자신의 예약이 맞는지 확인)
        if(!booking.getStudent().getId().equals(studentId)){
            throw new SecurityException("본인의 예약만 취소할 수 있습니다.");
        }

        // 취소 가능 기간 검증 - 3일 전까지만 가능함
        Schedule schedule = booking.getSchedule();
        LocalDateTime classStartTime = schedule.getStartTime(); // 수업 시작 시간
        // * 데드라인 계산 : 수업 시작 시간 - 3일
        LocalDateTime cancelDeadLine = classStartTime.minusDays(3);

        if(LocalDateTime.now().isAfter(cancelDeadLine)){
            throw new IllegalStateException("수업 시작 3일 전까지만 취소할 수 있습니다.");
        }

        // 상태 변경(취소)
        booking.setStatus(BookingStatus.CANCELLED_BY_STUDENT);
        // 스케줄 원상 복구(다시 예약할 수 있게 함)
        booking.getSchedule().setIsBooked(false);
    }

    // Booking-History-01 선생님의 예약 가능 스케줄 조회
    @Transactional(readOnly = true)
    public List<ScheduleSlotResponse> getAvailableSchedules(Long teacherId){

        // DB에서 선생님 ID로 검색 + 예약 안된 것(isBooked=false)만 가져오기
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndIsBookedFalseOrderByStartTimeAsc(teacherId);

        // DTO로 변환
        return schedules.stream()
                .map(ScheduleSlotResponse::from)
                .collect(Collectors.toList());
    }

    private void validateBookingTime(Schedule schedule){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = schedule.getStartTime();
        LocalDateTime endTime = schedule.getEndTime();

        // 과거 날짜 검증
        if(startTime.isBefore(now)){
            throw new InvalidDateException("이미 지난 시간의 수업은 신청할 수 없습니다.");
        }

        // 시간 역전/오류 검증
        if(endTime.isBefore(startTime)||endTime.equals(startTime)){
            throw new InvalidDateException("잘못된 수업 시간입니다. (종료 시간이 시작 시간보다 빠르거나 같음)");
        }
    }

    // Class-Booking-04 선생님의 예약 요청 수락
    @Transactional
    public void acceptBooking(Long teacherId, Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        // 권한 검증 : 이 예약이 해당 선생님의 수업에 대한 예약인지 확인
        // Booking -> Schedule -> User(Teacher)로 접근
        if(!booking.getSchedule().getUser().getId().equals(teacherId)){
            throw new SecurityException("해당 예약을 수락할 권한이 없습니다.");
        }

        // 상태 검증 : 대기(PENDING) 상태일 때만 수락 가능
        if(booking.getStatus() != BookingStatus.PENDING){
            throw new IllegalStateException("대기 상태의 예약만 수락할 수 있습니다.");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
    }

    // Class-Booking-05 선생님의 예약 요청 거절
    @Transactional
    public void rejectBooking(Long teacherId, Long bookingId){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));

        if(!booking.getSchedule().getUser().getId().equals(teacherId)){
            throw new SecurityException("해당 예약을 거절할 권한이 없습니다.");
        }

        if(booking.getStatus() != BookingStatus.PENDING){
            throw new IllegalArgumentException("대기 상태의 예약만 거절할 수 있습니다.");
        }

        // 상태 변경
        booking.setStatus(BookingStatus.REJECTED);

        // 스케줄 원상 복구
        booking.getSchedule().setIsBooked(false);
    }

    // Class-Booking-06 선생님의 수강 확정된 예약 취소
    @Transactional
    public void cancelBookingByTeacher(Long teacherId, Long bookingId, String cancelReason){
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약입니다."));
        // 권한 검증 : 해당 스케줄의 선생님인지 확인
        if(!booking.getSchedule().getUser().getId().equals(teacherId)){
            throw new SecurityException("본인의 수업 예약만 취소할 수 있습니다.");
        }

        // 상태 검증
        BookingStatus status = booking.getStatus();
        if (status != BookingStatus.PENDING && status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("이미 처리되었거나 취소된 예약은 다시 취소할 수 없습니다.");
        }

        // 상태 변경
        booking.setStatus(BookingStatus.CANCELLED_BY_TEACHER);
        booking.setCancelReason(cancelReason);

        // 스케줄 복구
        booking.getSchedule().setIsBooked(false);
    }

    // 이미 수강한 수업 예약 조회하기
    public List<Booking> getAllBeforeNow(Long id) {
        return bookingRepository.findAllBefore(id);
    }

    // 아직 수강하지 않은 수업 예약 조회
    public List<Booking> getAllAfterNow(Long id) {
        return bookingRepository.findAllAfter(id);
    }
}