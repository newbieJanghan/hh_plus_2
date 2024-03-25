package org.example.course_register.database.course_registry;

import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistryReader {
  public CourseRegistry read(long userId) {
    // read from database
    return new CourseRegistry(userId);
  }

  public int readCount() {
    // read from database
    return 0;
  }
}
