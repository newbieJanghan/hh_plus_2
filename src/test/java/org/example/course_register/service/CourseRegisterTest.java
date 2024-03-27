package org.example.course_register.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registeration.exceptions.AlreadyRegisteredException;
import org.example.course_register.api.course_registeration.exceptions.LimitationOverFailureException;
import org.example.course_register.database.course.Course;
import org.example.course_register.database.course.CourseReader;
import org.example.course_register.database.course_registration.CourseRegistrationReader;
import org.example.course_register.database.course_registration.CourseRegistrationWriter;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.example.course_register.domain.CourseRegister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class CourseRegisterTest {
  private @Mock CourseRegistrationWriter courseRegistrationWriter;
  private @Mock CourseRegistrationReader courseRegistrationReader;
  private @Mock CourseReader courseReader;

  private CourseRegister register;

  @BeforeEach
  public void setUp() {
    initMocks(this);
    register = new CourseRegister(courseReader, courseRegistrationWriter, courseRegistrationReader);
  }

  @Test
  @DisplayName("특강 수강 신청 성공")
  public void registerSuccess() {
    long courseId = 1;
    long userId = 1;

    try {
      // given
      CourseRegistration mockRegistration = mockCourseRegistration(courseId, userId);

      when(courseReader.getById(courseId)).thenReturn(mockCourse());
      when(courseRegistrationReader.countAll()).thenReturn(0);
      when(courseRegistrationWriter.save(mockRegistration)).thenReturn(mockRegistration);

      // when
      CourseRegistration result = register.register(courseId, userId);

      // then
      assertEquals(courseId, result.getCourseId());
      assertEquals(userId, result.getUserId());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 수강 신청 대상 강좌 없음")
  public void registerFail_CauseOfNoCourse() {
    long courseId = 1;
    long userId = 1;

    // given
    when(courseReader.getById(courseId)).thenReturn(null);

    // when && then
    assertThrows(BadRequestException.class, () -> register.register(courseId, userId));
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 수강 인원 제한")
  public void registerFail_CauseOfRegisterLimitation() {
    long courseId = 1;
    long userId = 1;

    // given
    when(courseReader.getById(courseId)).thenReturn(mockCourse());
    when(courseRegistrationReader.getCurrentCount()).thenReturn(30);

    // when
    try {
      assertThrows(LimitationOverFailureException.class, () -> register.register(courseId, userId));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 이미 수강 신청한 경우")
  public void registerFail_CauseOfAlreadyRegistered() {
    long courseId = 1;
    long userId = 1;

    // given
    CourseRegistration exist = mockCourseRegistration(courseId, userId);
    when(courseReader.getById(courseId)).thenReturn(mockCourse());
    when(courseRegistrationReader.query(courseId, userId)).thenReturn(exist);

    // when && then
    assertThrows(AlreadyRegisteredException.class, () -> register.register(courseId, userId));
  }

  @Test
  @DisplayName("수강 신청 조회 - 성공")
  public void checkSuccessRegistration() {
    long courseId = 1;
    long userId = 1;

    // given
    when(courseRegistrationReader.query(courseId, userId))
        .thenReturn(mockCourseRegistration(courseId, userId));

    // when
    CourseRegistration result = register.checkRegistrationExist(courseId, userId);

    // then
    assertEquals(1, result.getUserId());
  }

  @Test
  @DisplayName("수강 신청 조회 - 수강 신청 내역 없음")
  public void checkNoRegistration() {
    long courseId = 1;
    long userId = 1;

    // given
    when(courseRegistrationReader.query(courseId, userId)).thenReturn(null);

    // when & then
    assertThrows(
        EntityNotFoundException.class, () -> register.checkRegistrationExist(courseId, userId));
  }

  private Course mockCourse() {
    return Course.builder().build();
  }

  private CourseRegistration mockCourseRegistration(long courseId, long userId) {
    return CourseRegistration.builder().courseId(courseId).userId(userId).build();
  }
}
