package org.example.course_register.database.course_registration;

import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistrationWriter {
  private final CourseRegistrationRepository repository;

  @Autowired
  public CourseRegistrationWriter(CourseRegistrationRepository repository) {
    this.repository = repository;
  }

  public CourseRegistration save(CourseRegistration CourseRegistration) {
    return repository.save(CourseRegistration);
  }
}
