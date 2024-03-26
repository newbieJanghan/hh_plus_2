package org.example.course_register.domain.course_registry;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "course_registry")
public class CourseRegistry {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "user_id")
  private long userId;

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
  private CourseRegistryStatus status;

  @Builder
  public CourseRegistry(long userId) {
    this.userId = userId;
    this.status = CourseRegistryStatus.PENDING;
  }
}
