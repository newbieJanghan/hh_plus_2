package org.example.course_register.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.example.course_register.api.courses.CoursesController;
import org.example.course_register.api.courses.CoursesService;
import org.example.course_register.database.course.Course;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CoursesController.class)
public class CoursesControllerTest {
  @Autowired private MockMvc mockMvc;
  @MockBean private CoursesService specialCourseService;

  @Test
  @DisplayName("특강 목록 조회 - 빈 배열 반환")
  @WithMockUser
  public void getCourses_WithEmptyList() {
    try {
      when(specialCourseService.getCourses()).thenReturn(Collections.emptyList());
      mockMvc.perform(get("/courses")).andExpect(status().isOk()).andExpect(content().json("[]"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("특강 목록 조회 - 쿼리에 맞는 목록 반환")
  @WithMockUser
  public void getCourses_WithQueryParams() {
    try {
      when(specialCourseService.getCourses()).thenReturn(mockCourseList());
      mockMvc
          .perform(get("/courses?limit=10&offset=0"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$[0].name").value("test1"))
          .andExpect(jsonPath("$[1].name").value("test2"));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private List<Course> mockCourseList() {
    List<Course> courseList = new ArrayList<>();
    courseList.add(mockCourse("test1"));
    courseList.add(mockCourse("test2"));

    return courseList;
  }

  private Course mockCourse(String name) {
    return Course.builder().name(name).build();
  }
}
