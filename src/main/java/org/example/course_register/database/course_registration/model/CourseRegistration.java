package org.example.course_register.database.course_registration.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.course_register.database.course.Course;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "course_registration")
public class CourseRegistration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "user_id")
  private Long userId;

  @Column(nullable = false, name = "course_id")
  private Long courseId;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @CreationTimestamp
  @Column(nullable = false, name = "created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false, name = "updated_at")
  private LocalDateTime updatedAt;

  @Enumerated(EnumType.STRING)
  @Column(
      nullable = false,
      columnDefinition = "ENUM('PENDING', 'REGISTERED', 'FAILED', 'CANCELED') DEFAULT 'PENDING'")
  private CourseRegistrationStatus status;

  @Builder
  public CourseRegistration(long courseId, long userId) {
    this.courseId = courseId;
    this.userId = userId;
    this.status = CourseRegistrationStatus.PENDING;
  }
}
