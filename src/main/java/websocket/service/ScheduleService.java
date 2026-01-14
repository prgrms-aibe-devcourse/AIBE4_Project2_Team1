package websocket.service;

import jakarta.persistence.EntityNotFoundException;
import websocket.dto.api.request.ScheduleRequest;
import websocket.dto.api.response.ScheduleResponse;
import websocket.dto.api.response.TeacherScheduleResponse;
import websocket.dto.view.ScheduleDto;
import websocket.entity.lesson.Booking;
import websocket.entity.lesson.Lesson;
import websocket.entity.lesson.Schedule;
import websocket.entity.user.User;
import websocket.repository.BookingRepository;
import websocket.repository.LessonRepository;
import websocket.repository.ScheduleRepository;
import websocket.repository.UserRepository;
import websocket.type.BookingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;

    // ==========================================
    //  [학생 기능] 수강 신청 팝업 데이터 조회 (기존 코드)
    // ==========================================
    public ScheduleDto getSchedule(Long scheduleId) {
        // 1. 스케줄 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스케줄입니다."));

        // 2. 엔티티 그래프 검색
        String teacherName = schedule.getUser().getName();

        // 3. Lesson 정보 가져오기
        if(schedule.getLesson() == null){
            throw new IllegalStateException("스케줄에 연결된 수업 정보가 없습니다.");
        }

        String subject = schedule.getLesson().getTitle(); // 수업 제목
        Long price = schedule.getLesson().getPrice();

        return new ScheduleDto(
                schedule.getId(),
                schedule.getUser().getId(),
                teacherName,
                subject,
                price,
                schedule.getStartTime()
        );
    }

    // ==========================================================
    //  2. [선생님용] 내 시간표 조회
    // ==========================================================
    public List<TeacherScheduleResponse> getTeacherSchedules(Long teacherId) {

        // 1. 선생님의 모든 스케줄을 가져옵니다.
        List<Schedule> schedules = scheduleRepository.findAllByUserIdOrderByStartTimeDesc(teacherId);

        return schedules.stream().map(schedule -> {
            // 2. 해당 스케줄에 연결된 예약(Booking)이 있는지 확인합니다.
            Optional<Booking> bookingOpt = bookingRepository.findAll().stream()
                    .filter(b -> b.getSchedule().getId().equals(schedule.getId()))
                    .filter(b -> b.getStatus() != BookingStatus.CANCELLED_BY_STUDENT)
                    .findFirst();

            // 3. DTO 변환을 위한 변수 준비
            String studentName = null;
            String status = "AVAILABLE"; // 기본값: 예약 가능
            String statusKor = "예약 가능";

            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                // 예약이 존재하면 학생 정보와 상태를 덮어씁니다.
                studentName = booking.getStudent().getName();
                status = booking.getStatus().name();

                // 한글 상태명 변환 (간단 로직)
                if(booking.getStatus() == BookingStatus.CONFIRMED) statusKor = "예약 확정";
                else if(booking.getStatus() == BookingStatus.PENDING) statusKor = "승인 대기";
            }

            // 4. 선생님용 DTO 생성 및 반환
            return TeacherScheduleResponse.builder()
                    .scheduleId(schedule.getId())
                    .bookingId(bookingOpt.map(Booking::getId).orElse(null)) // 예약 없으면 null
                    .lessonTitle(schedule.getLesson().getTitle())
                    .studentName(studentName)
                    .startTime(schedule.getStartTime())
                    .endTime(schedule.getEndTime())
                    .status(status)
                    .statusKor(statusKor)
                    .build();

        }).collect(Collectors.toList());
    }

    // 일정 생성
    public ScheduleResponse createSchedule(ScheduleRequest request, Long teacherId) {
        User user = userRepository.findById(teacherId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new EntityNotFoundException("과외를 찾을 수 없습니다."));

        Schedule schedule = new Schedule(request, lesson, user);
        Schedule saved = scheduleRepository.save(schedule);

        return ScheduleResponse.from(saved);
    }
}
