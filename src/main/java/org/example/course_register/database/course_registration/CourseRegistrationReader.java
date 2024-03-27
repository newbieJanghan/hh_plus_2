package org.example.course_register.database.course_registration;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.List;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistrationReader {
  private final CourseRegistrationRepository repository;

  @Autowired
  public CourseRegistrationReader(CourseRegistrationRepository repository) {
    this.repository = repository;
  }

  public CourseRegistration getById(long id) {
    return repository.findById(id).orElse(null);
  }

  public CourseRegistration query(long courseId, long userId) {
    return repository.findByCourseIdAndUserId(courseId, userId).orElse(null);
  }

  public List<CourseRegistration> getByUserId(long userId) {
    return repository.findByUserId(userId);
  }

  public int countAll() {
    return (int) repository.count();
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000")})
  public int getCurrentCount() {
    return (int) repository.count();
  }
}
