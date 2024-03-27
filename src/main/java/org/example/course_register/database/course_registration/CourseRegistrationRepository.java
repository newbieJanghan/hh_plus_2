package org.example.course_register.database.course_registration;

import java.util.List;
import java.util.Optional;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {

  @Query("SELECT c FROM CourseRegistration c WHERE c.courseId = ?1 AND c.userId = ?2")
  Optional<CourseRegistration> findByCourseIdAndUserId(long courseId, long userId);

  @Query("SELECT c FROM CourseRegistration c WHERE c.userId = ?1")
  List<CourseRegistration> findByUserId(long userId);
}
