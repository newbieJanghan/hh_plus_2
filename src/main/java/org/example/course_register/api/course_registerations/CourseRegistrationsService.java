package org.example.course_register.api.course_registerations;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registerations.exceptions.AlreadyRegisteredException;
import org.example.course_register.api.course_registerations.exceptions.LimitationOverFailureException;
import org.example.course_register.database.course_registration.model.CourseRegistration;

public interface CourseRegistrationsService {
  CourseRegistration register(long courseId, long userId)
      throws BadRequestException, AlreadyRegisteredException, LimitationOverFailureException;

  CourseRegistration checkExistence(long courseId, long userId) throws EntityNotFoundException;
}
