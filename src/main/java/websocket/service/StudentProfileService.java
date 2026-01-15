package websocket.service;

import websocket.dto.view.StudentDTO;
import websocket.entity.Payment;
import websocket.entity.lesson.Booking;
import websocket.entity.user.StudentProfile;
import websocket.repository.BookingRepository;
import websocket.repository.PaymentRepository;
import websocket.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websocket.repository.UserRepository;

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
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Value("${file.path-student}")
    private String studentPath;

    // 학생 프로필 수정
    public void updateStudentProfile(StudentDTO dto) {
        StudentProfile profile = getProfileByStudentId(dto.getId());

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + dto.getImage().getOriginalFilename();
        System.out.println("이미지 이름: " + imageFileName);

        Path directoryPath = Paths.get(studentPath);
        Path imageFilePath = directoryPath.resolve(imageFileName);

        try {
            if (Files.notExists(directoryPath)) Files.createDirectories(directoryPath);
            Files.write(imageFilePath, dto.getImage().getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        profile.setImageUrl(imageFileName);
        profile.setRegionCode(dto.getRegionCode());
        profile.setBio(dto.getIntroduce());
    }

//    // 프로필 사진 등록 -> 이미지 경로 출력
//    public String setStudentImage(StudentDTO dto) {
//        StudentProfile profile = getProfileByStudentId(dto.getId());
//
//        UUID uuid = UUID.randomUUID();
//        String imageFileName = uuid + "_" + dto.getId();
//        System.out.println("이미지 이름: " + imageFileName);
//
//        Path imageFilePath = Paths.get(studentPath, imageFileName);
//
//        try {
//            Files.write(imageFilePath, dto.getImage().getBytes());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        profile.setImageUrl(imageFileName);
//        return imageFileName;
//    }
//
//    // 학생 한줄 소개 등록
//    public void setStudentIntroduce(StudentDTO dto) {
//        StudentProfile profile = getProfileByStudentId(dto.getId());
//        profile.setBio(dto.getIntroduce());
//    }

    // 학생 id로 학생 정보 불러오기 => 없다면 빈(empty) 프로필 생성
    public StudentProfile getProfileByStudentId(Long id) {
        return studentProfileRepository.findByUser_Id(id)
                .orElseGet(() -> studentProfileRepository.save(new StudentProfile(
                        userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."))
                )));
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
