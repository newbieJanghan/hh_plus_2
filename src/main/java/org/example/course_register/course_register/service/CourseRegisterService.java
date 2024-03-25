package org.example.course_register.course_register.service;

import org.example.course_register.course_register.exception.AlreadyRegisteredException;
import org.example.course_register.database.course_registry.CourseRegistryReader;
import org.example.course_register.database.course_registry.CourseRegistryWriter;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseRegisterService implements CourseRegister {

  private CourseRegistryWriter courseRegistryWriter;
  private CourseRegistryReader courseRegistryReader;

  @Autowired
  public CourseRegisterService(
      CourseRegistryWriter courseRegistryWriter, CourseRegistryReader courseRegistryReader) {
    this.courseRegistryWriter = courseRegistryWriter;
    this.courseRegistryReader = courseRegistryReader;
  }

  @Transactional
  public CourseRegistry register(long userId) {
    CourseRegistry exist = courseRegistryReader.read(userId);
    if (exist != null) {
      throw new AlreadyRegisteredException();
    }

    try {
      CourseRegistry courseRegistry = new CourseRegistry(userId);

      int registerCount = courseRegistryReader.readCount();
      if (registerCount < 30) {
        courseRegistry.setCompleted();
      }

      return courseRegistryWriter.write(courseRegistry);
    } catch (Exception e) {
      // TODO database rollback
      throw e;
    }
  }

  public CourseRegistry checkRegistry(long userId) {
    return new CourseRegistry(userId);
  }
}
