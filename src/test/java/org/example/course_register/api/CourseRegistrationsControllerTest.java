package org.example.course_register.api;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registerations.CourseRegistrationsController;
import org.example.course_register.api.course_registerations.CourseRegistrationsService;
import org.example.course_register.api.course_registerations.exceptions.AlreadyRegisteredException;
import org.example.course_register.api.course_registerations.exceptions.LimitationOverFailureException;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseRegistrationsController.class)
public class CourseRegistrationsControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private CourseRegistrationsService courseRegisterService;

  @Test
  @DisplayName("특강 수강 신청 실패 - 요청에 userId가 없을 때")
  @WithMockUser
  public void register_fail_no_userId() {
    try {
      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType("application/json")
                  .content("{\"courseId\":1}"))
          .andExpect(status().isBadRequest());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 요청에 courseId가 없을 때")
  @WithMockUser
  public void register_fail_no_courseId() {
    try {
      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType("application/json")
                  .content("{\"userId\":1}"))
          .andExpect(status().isBadRequest());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 성공")
  @WithMockUser
  public void register_success() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.register(courseId, userId))
          .thenReturn(mockRegistration(courseId, userId));

      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"courseId\":1,\"userId\":1}"))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.userId").value(1));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 인원 제한")
  @WithMockUser
  public void register_fail_over_limit() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.register(courseId, userId))
          .thenThrow(new LimitationOverFailureException());

      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"courseId\":1, \"userId\":1}"))
          .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
          .andExpect(jsonPath("$.code").value("500"))
          .andExpect(jsonPath("$.message").value("수강 신청이 마감되었습니다."));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 이미 수강 신청한 경우")
  @WithMockUser
  public void register_fail_already_registered() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.register(courseId, userId))
          .thenThrow(new AlreadyRegisteredException());

      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"courseId\":1, \"userId\":1}"))
          .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
          .andExpect(jsonPath("$.code").value("500"))
          .andExpect(jsonPath("$.message").value("이미 수강 중인 특강입니다."));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 올바르지 않은 강의 아이디")
  @WithMockUser
  public void register_fail_no_course() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.register(courseId, userId))
          .thenThrow(new BadRequestException("courseId가 올바르지 않습니다."));

      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"courseId\":1, \"userId\":1}"))
          .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
          .andExpect(jsonPath("$.code").value("400"))
          .andExpect(jsonPath("$.message").value("courseId가 올바르지 않습니다."));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 올바르지 않은 유저 아이디")
  @WithMockUser
  public void register_fail_no_user() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.register(courseId, userId))
          .thenThrow(new BadRequestException("userId가 올바르지 않습니다."));

      mockMvc
          .perform(
              post("/registrations")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"courseId\":1, \"userId\":1}"))
          .andExpect(jsonPath("$.code").value("400"))
          .andExpect(jsonPath("$.message").value("userId가 올바르지 않습니다."));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("수강 신청 결과 조회 - 성공")
  @WithMockUser
  public void getSuccessCourseRegistration() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.checkExistence(courseId, userId))
          .thenReturn(mockRegistration(courseId, userId));

      mockMvc
          .perform(get("/registration/{courseId}/{userId}", courseId, userId).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.userId").value(1));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("수강 신청 결과 조회 - 실패")
  @WithMockUser
  public void getFailCourseRegistration() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.checkExistence(courseId, userId))
          .thenThrow(new EntityNotFoundException("수강 신청 내역이 없습니다."));

      mockMvc
          .perform(get("/registration/{coourseId}/{userId}", courseId, userId).with(csrf()))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.code").value("404"))
          .andExpect(jsonPath("$.message").value("수강 신청 내역이 없습니다."));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("수강 신청 조회 실패 - 사용자가 없을 때")
  @WithMockUser
  public void getFailCourseRegistration_CauseOfUserNotFound() {
    long courseId = 1;
    long userId = 1;

    try {
      when(courseRegisterService.checkExistence(courseId, userId))
          .thenThrow(new BadRequestException("userId가 올바르지 않습니다."));

      mockMvc
          .perform(get("/registration/{courseId}/{userId}", userId).with(csrf()))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.code").value("400"))
          .andExpect(jsonPath("$.message").value("userId가 올바르지 않습니다."));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private CourseRegistration mockRegistration(long courseId, long userId) {
    return CourseRegistration.builder().courseId(courseId).userId(userId).build();
  }
}
