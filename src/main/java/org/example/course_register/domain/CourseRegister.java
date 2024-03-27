package org.example.course_register.domain;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registeration.CourseRegistrationService;
import org.example.course_register.api.course_registeration.exceptions.AlreadyRegisteredException;
import org.example.course_register.api.course_registeration.exceptions.LimitationOverFailureException;
import org.example.course_register.database.course.Course;
import org.example.course_register.database.course.CourseReader;
import org.example.course_register.database.course_registration.*;
import org.example.course_register.database.course_registration.model.CourseRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseRegister implements CourseRegistrationService {

  private static final Logger logger = LoggerFactory.getLogger(CourseRegister.class);
  private final CourseReader courseReader;
  private final CourseRegistrationWriter courseRegistrationWriter;
  private final CourseRegistrationReader courseRegistrationReader;

  private final int REGISTRATION_LIMIT = 30;

  @Autowired
  public CourseRegister(
      CourseReader courseReader,
      CourseRegistrationWriter courseRegistrationWriter,
      CourseRegistrationReader courseRegistrationReader) {
    this.courseReader = courseReader;
    this.courseRegistrationWriter = courseRegistrationWriter;
    this.courseRegistrationReader = courseRegistrationReader;
  }

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public CourseRegistration register(long courseId, long userId) throws BadRequestException {
    try {
      validateRegister(courseId, userId);

      int registerCount = courseRegistrationReader.getCurrentCount();
      System.out.println("userId: " + userId);
      System.out.println("registerCount: " + registerCount);
      if (registerCount >= REGISTRATION_LIMIT) {
        throw new LimitationOverFailureException();
      }

      CourseRegistration courseRegistration = CourseRegistration.builder().userId(userId).build();
      return courseRegistrationWriter.save(courseRegistration);
    } catch (Exception e) {
      logger.error("Failed to register user: {}", userId, e);
      throw e;
    }
  }

  public CourseRegistration checkRegistrationExist(long courseId, long userId)
      throws EntityNotFoundException {
    CourseRegistration result = this.courseRegistrationReader.query(courseId, userId);
    if (result == null) {
      throw new EntityNotFoundException();
    }

    return result;
  }

  private void validateRegister(long courseId, long userId) throws BadRequestException {
    Course course = courseReader.getById(courseId);
    if (course == null) {
      throw new BadRequestException("courseId가 올바르지 않습니다.");
    }

    CourseRegistration exist = courseRegistrationReader.query(courseId, userId);
    if (exist != null) {
      throw new AlreadyRegisteredException();
    }
  }
}
