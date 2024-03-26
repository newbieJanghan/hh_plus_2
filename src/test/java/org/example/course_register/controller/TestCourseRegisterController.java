package org.example.course_register.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.course_register.course_register.controller.CourseRegisterController;
import org.example.course_register.course_register.exception.AlreadyRegisteredException;
import org.example.course_register.course_register.exception.LimitationOverFailureException;
import org.example.course_register.course_register.service.CourseRegister;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CourseRegisterController.class)
public class TestCourseRegisterController {
  @Autowired private MockMvc mockMvc;
  @MockBean private CourseRegister courseRegisterService;

  // Request DTO level 에서 throw 한 에러와
  // Service level 에서 throw 한 에러를 통일해야 하는가?
  @Test
  @DisplayName("특강 수강 신청 실패 - 요청에 userId가 없을 때")
  @WithMockUser
  public void register_fail_no_userId() {
    try {
      mockMvc
          .perform(post("/register").with(csrf()).contentType("application/json").content("{}"))
          .andExpect(status().isBadRequest());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 성공")
  @WithMockUser
  public void register_success() {
    long userId = 1;
    CourseRegistry courseRegistry = CourseRegistry.builder().userId(userId).build();

    try {
      when(courseRegisterService.register(userId)).thenReturn(courseRegistry);

      mockMvc
          .perform(
              post("/register")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"userId\":1}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.userId").value(1));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 인원 제한")
  @WithMockUser
  public void register_fail_over_limit() {
    long userId = 1;

    try {
      when(courseRegisterService.register(userId)).thenThrow(new LimitationOverFailureException());

      mockMvc
          .perform(
              post("/register")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"userId\":1}"))
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
    long userId = 1;

    try {
      when(courseRegisterService.register(userId)).thenThrow(new AlreadyRegisteredException());

      mockMvc
          .perform(
              post("/register")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"userId\":1}"))
          .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
          .andExpect(jsonPath("$.code").value("500"))
          .andExpect(jsonPath("$.message").value("이미 수강 중인 특강입니다."));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강 신청 실패 - 올바르지 않은 유저 아이디")
  @WithMockUser
  public void register_fail_no_user() {
    long userId = 1;

    try {
      when(courseRegisterService.register(userId)).thenThrow(BadRequestException.class);

      mockMvc
          .perform(
              post("/register")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"userId\":1}"))
          .andExpect(jsonPath("$.code").value("400"))
          .andExpect(jsonPath("$.message").value("userId가 올바르지 않습니다."));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("수강 신청 결과 조회 - 성공")
  @WithMockUser
  public void getSuccessCourseRegistry() {
    long userId = 1;
    CourseRegistry courseRegistry = CourseRegistry.builder().userId(userId).build();

    try {
      when(courseRegisterService.checkRegistrationStatus(userId)).thenReturn(courseRegistry);

      mockMvc
          .perform(get("/register/{userId}", userId).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.userId").value(1));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("수강 신청 결과 조회 - 실패")
  @WithMockUser
  public void getFailCourseRegistry() {
    long userId = 1;

    try {
      when(courseRegisterService.checkRegistrationStatus(userId))
          .thenThrow(new EntityNotFoundException("수강 신청 내역이 없습니다."));

      mockMvc
          .perform(get("/register/{userId}", userId).with(csrf()))
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
  public void getFailCourseRegistry_CauseOfUserNotFound() {
    long userId = 1;

    try {
      when(courseRegisterService.checkRegistrationStatus(userId))
          .thenThrow(new BadRequestException("userId가 올바르지 않습니다."));

      mockMvc
          .perform(get("/register/{userId}", userId).with(csrf()))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.code").value("400"))
          .andExpect(jsonPath("$.message").value("userId가 올바르지 않습니다."));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
