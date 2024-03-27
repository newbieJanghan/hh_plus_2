package org.example.course_register.api.courses;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.example.course_register.api.courses.dto.SearchCourseParamsRequestDto;
import org.example.course_register.database.course.Course;

public interface CoursesService {
  List<Course> getCourses();

  List<Course> getCourses(SearchCourseParamsRequestDto params);

  Course getCourse(long courseId) throws EntityNotFoundException;
}
