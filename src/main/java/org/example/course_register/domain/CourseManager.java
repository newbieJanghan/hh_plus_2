package org.example.course_register.domain;

import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import org.example.course_register.api.courses.CoursesService;
import org.example.course_register.api.courses.dto.SearchCourseParamsRequestDto;
import org.example.course_register.database.course.Course;
import org.springframework.stereotype.Service;

@Service
public class CourseManager implements CoursesService {
  public CourseManager() {}

  public Course getCourse(long courseId) {
    throw new EntityNotFoundException("강의가 존재하지 않습니다.");
  }

  public List<Course> getCourses() {
    return Collections.emptyList();
  }

  public List<Course> getCourses(SearchCourseParamsRequestDto params) {
    return Collections.emptyList();
  }
}
