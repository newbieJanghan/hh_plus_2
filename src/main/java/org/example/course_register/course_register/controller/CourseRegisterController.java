package org.example.course_register.course_register.controller;

import jakarta.validation.Valid;
import org.example.course_register.course_register.service.CourseRegister;
import org.example.course_register.course_register.service.CourseRegisterService;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class CourseRegisterController {
  private final CourseRegister courseRegisterService;

  @Autowired
  CourseRegisterController(CourseRegister courseRegisterService) {
    this.courseRegisterService = courseRegisterService;
  }

  public CourseRegisterController(CourseRegisterService courseRegisterService) {
    this.courseRegisterService = courseRegisterService;
  }

  @GetMapping("/{userId}")
  public CourseRegistry checkRegistrationStatus(@PathVariable long userId) throws Exception {
    return courseRegisterService.checkRegistrationStatus(userId);
  }

  @PostMapping("")
  public CourseRegistry register(@RequestBody @Valid CourseRegisterCreateRequestDto requestDto)
      throws Exception {
    return courseRegisterService.register(requestDto.getUserId());
  }
}
