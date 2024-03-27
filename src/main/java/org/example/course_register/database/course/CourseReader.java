package org.example.course_register.database.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CourseReader {
  private final CourseRepository repository;

  @Autowired
  public CourseReader(CourseRepository repository) {
    this.repository = repository;
  }

  @Nullable
  public Course getById(long id) {
    return repository.findById(id).orElse(null);
  }
}
