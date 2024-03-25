package org.example.course_register.domain.course_registry;

import java.time.LocalDateTime;
import lombok.Getter;
import org.example.course_register.config.Today;

@Getter
public class CourseRegistry {
  private final long userId;
  private final LocalDateTime registerTime;
  private CourseRegistryStatus status;

  public CourseRegistry(long userId) {
    this.userId = userId;
    this.registerTime = Today.getDateTime();
    this.status = CourseRegistryStatus.PENDING;
  }

  public boolean getIsCompleted() {
    return this.status == CourseRegistryStatus.REGISTERED;
  }

  public void setCompleted() {
    this.status = CourseRegistryStatus.REGISTERED;
  }
}
