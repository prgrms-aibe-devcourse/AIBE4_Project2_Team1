package websocket.repository;

import websocket.entity.lesson.Booking;
import websocket.type.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // 1. 나의 예약 현황 조회
    List<Booking> findAllByStudentIdOrderByScheduleStartTimeDesc(Long studentId);

    // 교사의 예약목록을 조회
    @Query("SELECT b FROM Booking b WHERE b.lesson.user.id = :teacherId")
    List<Booking> findAllByTeacherId(@Param("teacherId") Long teacherId);
  
    // 2. 중복 예약 방지
    boolean existsByScheduleIdAndStatusNot(Long scheduleId, BookingStatus status);

    // 미리 수강한 수업 예약 조회
    @Query("SELECT b FROM Booking b WHERE b.student.id = :id AND b.schedule.endTime < CURRENT_TIMESTAMP ORDER BY b.schedule.endTime")
    List<Booking> findAllBefore(@Param("id") Long id);

    // 아직 수강하지 않은 수업 예약 조회
    @Query("SELECT b FROM Booking b WHERE b.student.id = :id AND b.schedule.startTime > CURRENT_TIMESTAMP ORDER BY b.schedule.startTime")
    List<Booking> findAllAfter(@Param("id") Long id);
}
