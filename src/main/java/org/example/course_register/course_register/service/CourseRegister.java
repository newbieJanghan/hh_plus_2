package org.example.course_register.course_register.service;

import org.example.course_register.domain.course_registry.CourseRegistry;

public interface CourseRegister {
  CourseRegistry register(long userId) throws Exception;

  CourseRegistry checkRegistry(long userId) throws Exception;
}
