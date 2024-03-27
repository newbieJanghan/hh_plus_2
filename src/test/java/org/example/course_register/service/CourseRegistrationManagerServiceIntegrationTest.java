package org.example.course_register.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.*;
import org.example.course_register.database.course_registration.CourseRegistrationReader;
import org.example.course_register.domain.CourseRegistrationManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class CourseRegistrationManagerServiceIntegrationTest {
  @Autowired private CourseRegistrationManager service;
  @Autowired private CourseRegistrationReader reader;

  @Test
  public void requestFailure_WhenStudentLimitationOver_withPessimisticLock()
      throws InterruptedException {
    int limitation = 30;
    int threadCount = limitation + 1;
    long courseId = 1;

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    for (int i = 1; i <= threadCount; i++) {
      long userId = i;
      executor.submit(() -> service.register(courseId, userId));
    }
    executor.shutdown();
    executor.awaitTermination(30, TimeUnit.SECONDS);

    int count = reader.countAll();
    assertEquals(limitation, count);
  }
}
