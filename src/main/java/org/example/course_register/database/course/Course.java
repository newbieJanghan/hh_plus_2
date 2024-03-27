package org.example.course_register.database.course;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.example.course_register.database.course_registration_event.CourseRegistrationEvent;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false, columnDefinition = "VARCHAR(255)")
  private String name;

  @Column(columnDefinition = "TEXT DEFAULT NULL")
  private String description;

  @OneToMany(mappedBy = "course")
  private List<CourseRegistration> courseRegistries;

  @OneToMany(mappedBy = "course")
  private List<CourseRegistrationEvent> courseRegistrationEvents;

  //  @Column(nullable = false, name = "start_at")
  //  private LocalDateTime startAt;
  //
  //  @Column(nullable = false, name = "end_at")
  //  private LocalDateTime endAt;

  @CreationTimestamp
  @Column(nullable = false, name = "created_at")
  private LocalDateTime createdAt;

  @CreationTimestamp
  @Column(nullable = false, name = "updated_at")
  private LocalDateTime updatedAt;

  @Builder
  public Course(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
