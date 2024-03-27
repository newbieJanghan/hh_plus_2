package org.example.course_register.database.course_registration_event;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.course_register.database.course.Course;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "course_registration_event")
public class CourseRegistrationEvent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String name;

  @Column(nullable = false, name = "start_at")
  private LocalDateTime startAt;

  @Column(nullable = false, name = "end_at")
  private LocalDateTime endAt;

  @CreationTimestamp
  @Column(nullable = false, name = "created_at")
  private LocalDateTime createdAt;

  @CreationTimestamp
  @Column(nullable = false, name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  public CourseRegistrationEvent(
      Course course, String name, LocalDateTime startAt, LocalDateTime endAt) {
    this.course = course;
    this.name = name;
    this.startAt = startAt;
    this.endAt = endAt;
  }
}
