package org.example.course_register.course_register.domian;

import java.time.LocalDateTime;
import org.example.course_register.config.Today;

public class CourseRegistry {
  public long userId;
  public LocalDateTime registerTime;
  public boolean isCompleted;

  public CourseRegistry(long userId) {
    this.userId = userId;
    this.registerTime = Today.getDateTime();
  }

  public void setCompleted(boolean isCompleted) {
    this.isCompleted = isCompleted;
  }
}
