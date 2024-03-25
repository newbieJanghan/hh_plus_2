package org.example.course_register.database.course_registry;

import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistryWriter {
  public CourseRegistry write(CourseRegistry courseRegistry) {
    // write to database
    return courseRegistry;
  }
}
