package org.example.course_register.course_register.service;

import org.example.course_register.course_register.domian.CourseRegistry;
import org.springframework.stereotype.Service;

@Service
public class CourseRegisterService implements CourseRegister {
  public CourseRegistry register(long userId) {
    return new CourseRegistry(userId);
  }

  public CourseRegistry checkRegistry(long userId) {
    return new CourseRegistry(userId);
  }
}
