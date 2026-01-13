package kr.java.pr1mary.service;

import kr.java.pr1mary.dto.view.StudentDTO;
import kr.java.pr1mary.entity.Payment;
import kr.java.pr1mary.entity.lesson.Booking;
import kr.java.pr1mary.entity.lesson.Subject;
import kr.java.pr1mary.entity.user.StudentProfile;
import kr.java.pr1mary.repository.*;
import kr.java.pr1mary.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentProfileService {
    private final StudentProfileRepository studentProfileRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Value("${file.path-student}")
    private String studentPath;

    // 프로필 사진 등록 -> 이미지 경로 출력
    public String setStudentImage(StudentDTO dto) {
        StudentProfile profile = getProfileByStudentId(dto.getId());

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + dto.getId();
        System.out.println("이미지 이름: " + imageFileName);

        Path imageFilePath = Paths.get(studentPath, imageFileName);

        try {
            Files.write(imageFilePath, dto.getImage().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        profile.setImageUrl(imageFileName);
        return imageFileName;
    }

    // 학생 한줄 소개 등록
    public void setStudentIntroduce(StudentDTO dto) {
        StudentProfile profile = getProfileByStudentId(dto.getId());
        profile.setBio(dto.getIntroduce());
    }

    // 학생 id로 학생 정보 불러오기
    public StudentProfile getProfileByStudentId(Long id) {
        return studentProfileRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없음"));
    }

    // 학생 id로 예약 정보 불러오기
    public List<Booking> getBookingByStudentId(Long id) {
        return bookingRepository.findAllByStudentIdOrderByScheduleStartTimeDesc(id);
    }

    // 학생이 결제한 내역을 최신순으로 조회
    public List<Payment> getPaymentByStudentId(Long id) {
        return paymentRepository.findAllByBooking_Student_IdOrderByCreatedAtDesc(id);
    }
}
