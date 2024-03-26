package org.example.course_register.database.course_registry;

import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistryWriter {
  private final CourseRegistryRepository repository;

  @Autowired
  public CourseRegistryWriter(CourseRegistryRepository repository) {
    this.repository = repository;
  }

  public CourseRegistry save(CourseRegistry courseRegistry) {
    return repository.save(courseRegistry);
  }
}
