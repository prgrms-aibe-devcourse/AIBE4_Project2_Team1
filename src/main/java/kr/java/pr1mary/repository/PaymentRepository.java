package kr.java.pr1mary.repository;

import kr.java.pr1mary.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 학생이 결제한 내역을 최신순으로 조회
    List<Payment> findAllByBooking_Student_IdOrderByCreatedAtDesc(Long studentId);
}
