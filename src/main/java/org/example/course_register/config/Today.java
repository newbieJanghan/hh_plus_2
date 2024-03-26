package org.example.course_register.config;

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Today {
  private static Clock _clock = Clock.systemDefaultZone();

  @Autowired
  public Today(Clock clock) {
    Today._clock = clock;
  }

  public static LocalDateTime getDateTime() {
    return LocalDateTime.now(_clock);
  }
}
