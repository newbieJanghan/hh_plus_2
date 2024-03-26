package org.example.course_register.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import jakarta.persistence.EntityNotFoundException;
import org.example.course_register.course_register.exception.AlreadyRegisteredException;
import org.example.course_register.course_register.exception.LimitationOverFailureException;
import org.example.course_register.course_register.service.CourseRegister;
import org.example.course_register.course_register.service.CourseRegisterService;
import org.example.course_register.database.course_registry.CourseRegistryReader;
import org.example.course_register.database.course_registry.CourseRegistryWriter;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  @DisplayName("특강 수강 신청 성공")
  public void registerSuccess() {
    long userId = 1;

    try {
      // given
      CourseRegistry successCourseRegistry = CourseRegistry.builder().userId(userId).build();
      when(courseRegistryReader.countAll()).thenReturn(0);
      when(courseRegistryWriter.save(successCourseRegistry)).thenReturn(successCourseRegistry);

      // when
      CourseRegistry courseRegistry = courseRegister.register(userId);

      // then
      assertEquals(userId, courseRegistry.getUserId());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 수강 인원 제한")
  public void registerPending_CauseOfRegisterLimitation() {
    long userId = 1;

    // given
    when(courseRegistryReader.countAll()).thenReturn(30);

    // when
    try {
      assertThrows(LimitationOverFailureException.class, () -> courseRegister.register(userId));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 이미 수강 신청한 경우")
  public void registerFail_CauseOfAlreadyRegistered() {
    long userId = 1;

    // given
    CourseRegistry exist = CourseRegistry.builder().userId(userId).build();
    when(courseRegistryReader.getByUserId(userId)).thenReturn(exist);

    // when && then
    assertThrows(AlreadyRegisteredException.class, () -> courseRegister.register(userId));
  }

  @Test
  @DisplayName("특강 수강 신청 확인 - 수강 신청 성공")
  public void checkSuccessRegistration() {
    long userId = 1;

    // given
    CourseRegistry courseRegistry = CourseRegistry.builder().userId(userId).build();
    when(courseRegistryReader.getByUserId(userId)).thenReturn(courseRegistry);

    // when
    CourseRegistry result = courseRegister.checkRegistrationStatus(userId);

    // then
    assertEquals(1, result.getUserId());
  }

  @Test
  @DisplayName("특강 수강 신청 확인 - 수강 신청 내역 없음")
  public void checkNoRegistration() {
    long userId = 1;

    // given
    when(courseRegistryReader.getByUserId(userId)).thenReturn(null);

    // when & then
    assertThrows(
        EntityNotFoundException.class, () -> courseRegister.checkRegistrationStatus(userId));
  }
}
