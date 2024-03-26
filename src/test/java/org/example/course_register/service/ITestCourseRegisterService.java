package org.example.course_register.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.*;
import org.example.course_register.course_register.service.CourseRegisterService;
import org.example.course_register.database.course_registry.CourseRegistryReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ITestCourseRegisterService {
  @Autowired private CourseRegisterService service;
  @Autowired private CourseRegistryReader reader;

  @Test
  public void requestFailure_WhenStudentLimitationOver_withPessimisticLock()
      throws InterruptedException {
    int limitation = 30;
    int threadCount = limitation + 1;

    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    for (int i = 1; i <= threadCount; i++) {
      long userId = i;
      executor.submit(() -> service.register(userId));
    }
    executor.shutdown();
    executor.awaitTermination(30, TimeUnit.SECONDS);

    int count = reader.countAll();
    assertEquals(limitation, count);
  }
}
