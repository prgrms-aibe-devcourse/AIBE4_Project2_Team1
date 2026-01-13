package kr.java.pr1mary.controller.view;

import kr.java.pr1mary.dto.view.TeacherDTO;
import kr.java.pr1mary.entity.lesson.Lesson;
import kr.java.pr1mary.entity.user.Review;
import kr.java.pr1mary.entity.user.TeacherProfile;
import kr.java.pr1mary.service.TeacherProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/profile/teacher")
@RequiredArgsConstructor
public class TeacherProfileController {
    private final TeacherProfileService profileService;

    // 프로필 페이지
    @GetMapping
    public String teacherProfilePage(@RequestParam("id") Long id, Model model) {
        List<Lesson> lessons = profileService.getAllLessons(id);
        long profit = 0L;
        for (Lesson lesson: lessons) profit += lesson.getPrice();

        model.addAttribute("profile", profileService.getProfileByTeacherId(id));
        model.addAttribute("bookings", profileService.getAllBookings(id));
        model.addAttribute("lessons", lessons);
        model.addAttribute("profit", profit);
        model.addAttribute("average", profileService.getAverageRating(id));
        return "pages/profiles/teacher-profile";
    }

    // 프로필 공개 페이지
    @GetMapping("/public")
    public String teacherPublicProfilePage(@RequestParam("id") Long id, Model model) {
        List<Lesson> lessons = profileService.getAllLessons(id);
        long profit = 0L;
        for (Lesson lesson: lessons) profit += lesson.getPrice();

        model.addAttribute("profile", profileService.getProfileByTeacherId(id));
        model.addAttribute("bookings", profileService.getAllBookings(id));
        model.addAttribute("reviews", profileService.getReviews(id));
        model.addAttribute("lessons", lessons);
        model.addAttribute("profit", profit);
        model.addAttribute("average", profileService.getAverageRating(id));
        return "pages/profiles/teacher-profile-public";
    }

    // 프로필 편집 페이지
    @GetMapping("/update")
    public String updateTeacherProfilePage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("profile", profileService.getProfileByTeacherId(id));
        return "pages/profiles/teacher-profile-update";
    }

    // 프로필 수정
    @PostMapping("/update")
    public String updateTeacherProfileImage(@RequestBody TeacherDTO dto) {
        profileService.setTeacherIntroduce(dto);
        profileService.setTeacherImage(dto);
        profileService.saveSubject(dto);
        return "redirect:/profile/teacher?id=%d".formatted(dto.getId());
    }

//    // 프로필 이미지 수정
//    @PostMapping("/update/image")
//    public String updateTeacherProfileImage(@RequestBody TeacherDTO dto) {
//        profileService.setTeacherImage(dto);
//        return "redirect:/profile/teacher/update";
//    }
//
//    // 프로필 한줄 소개 수정
//    @PostMapping("/update/introduce")
//    public String updateTeacherProfileIntroduce(@RequestBody TeacherDTO dto) {
//        profileService.setTeacherIntroduce(dto);
//        return "redirect:/profile/teacher/update";
//    }
//
//    // 프로필 담당 과목 수정
//    @PostMapping("/update/subject")
//    public String updateTeacherProfileSubject(@RequestBody TeacherDTO dto) {
//        profileService.saveSubject(dto);
//        return "redirect:/profile/teacher/update";
//    }
}
