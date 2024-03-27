package org.example.course_register.api.courses;

import java.util.List;
import org.example.course_register.database.course.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CoursesController {
  private final CoursesService coursesService;

  @Autowired
  public CoursesController(CoursesService coursesService) {
    this.coursesService = coursesService;
  }

  @GetMapping
  public List<Course> getCourses() {
    return coursesService.getCourses();
  }

  @GetMapping("/{id}")
  public Course getCourse(@PathVariable Long id) {
    return coursesService.getCourse(id);
  }
}
