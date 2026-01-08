package kr.java.pr1mary.service;

import kr.java.pr1mary.dto.api.request.BookingCreateRequest;
import kr.java.pr1mary.dto.api.response.BookingHistoryResponse;
import kr.java.pr1mary.dto.api.response.ScheduleSlotResponse;
import kr.java.pr1mary.entity.lesson.Booking;
import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.lesson.Schedule;
import kr.java.pr1mary.entity.user.User;
import kr.java.pr1mary.repository.BookingRepository;
import kr.java.pr1mary.repository.LessonRepository;
import kr.java.pr1mary.repository.ScheduleRepository;
import kr.java.pr1mary.repository.UserRepository;
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
        // Repository에서 Entity 조회
        List<Booking> bookings = bookingRepository.findAllByStudentIdOrderByScheduleStartTimeDesc(studentId);
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

        // 예약 안된(is Booked = false) 스케줄만 가져옴
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndStartTimeBetweenAndIsBookedFalseOrderByStartTimeAsc(
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
    public Long registerBooking(BookingCreateRequest request){
        // 학생 조회
        User student = userRepository.findById(request.studentId())
                .orElseThrow(() ->new IllegalArgumentException("존재하지 않는 학생입니다."));

        // 스케줄 조회
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));

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
                schedule.getId(), "CANCELLED"
        );
        if(isDuplicate){
            throw new IllegalStateException("이미 예약된 시간입니다.");
        }

        // 예약 생성 및 저장
        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setLesson(lesson);
        booking.setSchedule(schedule);
        booking.setStatus("PENDING");
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

        // 상태 변경(취소)
        booking.setStatus("CANCELLED");

        // 스케줄 원상 복구(다시 예약할 수 있게 함)
        booking.getSchedule().setIsBooked(false);
    }

    // 선생님의 예약 가능 스케줄 조회
    @Transactional(readOnly = true)
    public List<ScheduleSlotResponse> getAvailableSchedules(Long teacherId){

        // DB에서 선생님 ID로 검색 + 예약 안된 것(isBooked=false)만 가져오기
        List<Schedule> schedules = scheduleRepository.findAllByUserIdAndIsBookedFalseOrderByStartTimeAsc(teacherId);

        // DTO로 변환
        return schedules.stream()
                .map(ScheduleSlotResponse::from)
                .collect(Collectors.toList());
    }
}
