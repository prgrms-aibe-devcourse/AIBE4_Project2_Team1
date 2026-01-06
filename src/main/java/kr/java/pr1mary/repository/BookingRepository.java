package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.lesson.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
