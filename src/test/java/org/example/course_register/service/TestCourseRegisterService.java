package org.example.course_register.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.example.course_register.course_register.exception.AlreadyRegisteredException;
import org.example.course_register.course_register.service.CourseRegister;
import org.example.course_register.course_register.service.CourseRegisterService;
import org.example.course_register.database.course_registry.CourseRegistryReader;
import org.example.course_register.database.course_registry.CourseRegistryWriter;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.example.course_register.domain.course_registry.CourseRegistryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class TestCourseRegisterService {
  private @Mock CourseRegistryWriter courseRegistryWriter;
  private @Mock CourseRegistryReader courseRegistryReader;
  private CourseRegister courseRegister;

  @BeforeEach
  public void setUp() {
    initMocks(this);
    courseRegister = new CourseRegisterService(courseRegistryWriter, courseRegistryReader);
  }

  @Test
  public void registerSuccess() {
    try {
      // given
      CourseRegistry successCourseRegistry = new CourseRegistry(1);
      successCourseRegistry.setCompleted();
      when(courseRegistryWriter.write(successCourseRegistry)).thenReturn(successCourseRegistry);

      // when
      CourseRegistry courseRegistry = courseRegister.register(1);

      // then
      assertEquals(1, courseRegistry.getUserId());
      assertNotNull(courseRegistry.getRegisterTime());
      assertTrue(courseRegistry.getIsCompleted());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void registerPending_CauseOfRegisterLimitation() {
    // given
    when(courseRegistryReader.readCount()).thenReturn(30);

    // when
    try {
      CourseRegistry courseRegistry = courseRegister.register(1);
      assertEquals(1, courseRegistry.getUserId());
      assertNotNull(courseRegistry.getRegisterTime());
      assertEquals(CourseRegistryStatus.PENDING, courseRegistry.getStatus());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void registerFail_CauseOfAlreadyRegistered() {
    // given
    when(courseRegistryReader.read(1)).thenReturn(new CourseRegistry(1));

    // when && then
    assertThrows(
        AlreadyRegisteredException.class,
        () -> {
          courseRegister.register(1);
        });
  }
}
