package org.example.course_register.api.course_registerations;

import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registerations.dto.CreateRequestDto;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.example.course_register.domain.CourseRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrations")
public class CourseRegistrationsController {
  private final CourseRegistrationsService courseRegisterService;

  @Autowired
  CourseRegistrationsController(CourseRegistrationsService courseRegisterService) {
    this.courseRegisterService = courseRegisterService;
  }

  public CourseRegistrationsController(CourseRegister courseRegisterService) {
    this.courseRegisterService = courseRegisterService;
  }

  @GetMapping("/{courseId}/{userId}")
  public CourseRegistration checkExistence(@PathVariable long courseId, @PathVariable long userId) {
    return courseRegisterService.checkExistence(courseId, userId);
  }

  @PostMapping("")
  @ResponseStatus(HttpStatus.CREATED)
  public CourseRegistration register(@RequestBody @Valid CreateRequestDto requestDto)
      throws BadRequestException {
    return courseRegisterService.register(requestDto.getCourseId(), requestDto.getUserId());
  }
}
