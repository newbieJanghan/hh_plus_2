package org.example.course_register.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityNotFoundException;
import org.example.course_register.course_register.controller.CourseRegisterController;
import org.example.course_register.course_register.exception.AlreadyRegisteredException;
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

  @Test
  @DisplayName("특강 수강신청 실패 - 요청에 userId가 없을 때")
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
  @DisplayName("특강 수강신청 성공")
  @WithMockUser
  public void register_success() {
    long userId = 1;
    CourseRegistry courseRegistry = new CourseRegistry(userId);
    courseRegistry.setCompleted();

    try {
      when(courseRegisterService.register(userId)).thenReturn(courseRegistry);

      mockMvc
          .perform(
              post("/register")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"userId\":1}"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.userId").value(1))
          .andExpect(jsonPath("$.isCompleted").value(true));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 수강신청 실패 - 이미 수강신청한 경우")
  @WithMockUser
  public void register_fail_already_registered() {
    long userId = 1;
    CourseRegistry courseRegistry = new CourseRegistry(userId);
    courseRegistry.setCompleted();

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
  @DisplayName("특강 수강신청 실패 - 사용자가 없을 때")
  @WithMockUser
  public void register_fail_no_user() {
    long userId = 1;
    CourseRegistry courseRegistry = new CourseRegistry(userId);
    courseRegistry.setCompleted();

    try {
      when(courseRegisterService.register(userId)).thenThrow(EntityNotFoundException.class);

      mockMvc
          .perform(
              post("/register")
                  .with(csrf())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content("{\"userId\":1}"))
          .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
          .andExpect(jsonPath("$.code").value("404"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("성공한 수강신청 조회")
  @WithMockUser
  public void getSuccessCourseRegistry() {
    long userId = 1;
    CourseRegistry courseRegistry = new CourseRegistry(userId);
    courseRegistry.setCompleted();

    try {
      when(courseRegisterService.checkRegistry(userId)).thenReturn(courseRegistry);

      mockMvc
          .perform(get("/register/{userId}", userId).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.userId").value(1))
          .andExpect(jsonPath("$.isCompleted").value(true));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("실패한 수강 신청 조회")
  @WithMockUser
  public void getFailCourseRegistry() {
    long userId = 1;
    CourseRegistry courseRegistry = new CourseRegistry(userId);

    try {
      when(courseRegisterService.checkRegistry(userId)).thenReturn(courseRegistry);

      mockMvc
          .perform(get("/register/{userId}", userId).with(csrf()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.userId").value(1))
          .andExpect(jsonPath("$.isCompleted").value(false));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("수강신청 조회 실패 - 사용자가 없을 때")
  @WithMockUser
  public void getFailCourseRegistry_CauseOfUserNotFound() {
    long userId = 1;

    try {
      when(courseRegisterService.checkRegistry(userId)).thenThrow(EntityNotFoundException.class);

      mockMvc
          .perform(get("/register/{userId}", userId).with(csrf()))
          .andExpect(status().isNotFound());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
