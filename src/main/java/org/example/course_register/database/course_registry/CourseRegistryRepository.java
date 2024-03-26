package org.example.course_register.database.course_registry;

import java.util.Optional;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRegistryRepository extends JpaRepository<CourseRegistry, Long> {

  @Query("SELECT c FROM CourseRegistry c WHERE c.userId = ?1")
  Optional<CourseRegistry> findByUserId(long userId);
}
