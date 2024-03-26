package org.example.course_register.course_register.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.course_register.course_register.exception.AlreadyRegisteredException;
import org.example.course_register.course_register.exception.LimitationOverFailureException;
import org.example.course_register.database.course_registry.CourseRegistryReader;
import org.example.course_register.database.course_registry.CourseRegistryWriter;
import org.example.course_register.domain.course_registry.CourseRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseRegisterService implements CourseRegister {

  private static final Logger logger = LoggerFactory.getLogger(CourseRegisterService.class);
  private final CourseRegistryWriter courseRegistryWriter;
  private final CourseRegistryReader courseRegistryReader;
  private final int REGISTRATION_LIMIT = 30;

  @Autowired
  public CourseRegisterService(
      CourseRegistryWriter courseRegistryWriter, CourseRegistryReader courseRegistryReader) {
    this.courseRegistryWriter = courseRegistryWriter;
    this.courseRegistryReader = courseRegistryReader;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public CourseRegistry register(long userId) {
    try {
      CourseRegistry exist = courseRegistryReader.getByUserId(userId);
      if (exist != null) {
        throw new AlreadyRegisteredException();
      }

      int registerCount = courseRegistryReader.getCurrentCountWithLock();
      //      System.out.println("userId: " + userId);
      //      System.out.println("registerCount: " + registerCount);
      if (registerCount >= REGISTRATION_LIMIT) {
        throw new LimitationOverFailureException();
      }

      CourseRegistry courseRegistry = CourseRegistry.builder().userId(userId).build();
      return courseRegistryWriter.save(courseRegistry);
    } catch (Exception e) {
      logger.error("Failed to register user: {}", userId, e);
      throw e;
    }
  }

  public CourseRegistry checkRegistrationStatus(long userId) throws EntityNotFoundException {
    CourseRegistry result = this.courseRegistryReader.getByUserId(userId);
    if (result == null) {
      throw new EntityNotFoundException();
    }

    return result;
  }
}
