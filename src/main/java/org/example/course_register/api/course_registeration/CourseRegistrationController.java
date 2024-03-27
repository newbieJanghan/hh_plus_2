package org.example.course_register.api.course_registeration;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registeration.dto.CreateRequestDto;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.example.course_register.domain.CourseRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
public class CourseRegistrationController {
  private final CourseRegistrationService courseRegisterService;

  @Autowired
  CourseRegistrationController(CourseRegistrationService courseRegisterService) {
    this.courseRegisterService = courseRegisterService;
  }

  public CourseRegistrationController(CourseRegister courseRegisterService) {
    this.courseRegisterService = courseRegisterService;
  }

  @GetMapping("/{courseId}/{userId}")
  public CourseRegistration checkRegistrationExist(
      @PathVariable long courseId, @PathVariable long userId) {
    return courseRegisterService.checkRegistrationExist(courseId, userId);
  }

  @PostMapping("")
  public CourseRegistration register(@RequestBody @Valid CreateRequestDto requestDto)
      throws BadRequestException {
    return courseRegisterService.register(requestDto.getCourseId(), requestDto.getUserId());
  }
}
