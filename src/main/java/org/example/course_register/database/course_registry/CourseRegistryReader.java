package org.example.course_register.database.course_registry;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Component;

@Component
public class CourseRegistryReader {
  private final CourseRegistryRepository repository;

  @Autowired
  public CourseRegistryReader(CourseRegistryRepository repository) {
    this.repository = repository;
  }

  public CourseRegistry getByUserId(long userId) {
    return repository.findByUserId(userId).orElse(null);
  }

  public int countAll() {
    return (int) repository.count();
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "10000")})
  public int getCurrentCountWithLock() {
    return (int) repository.count();
  }
}
