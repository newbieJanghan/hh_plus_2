package org.example.course_register.api.course_registeration;

import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.example.course_register.api.course_registeration.exceptions.AlreadyRegisteredException;
import org.example.course_register.api.course_registeration.exceptions.LimitationOverFailureException;
import org.example.course_register.database.course_registration.model.CourseRegistration;

public interface CourseRegistrationService {
  CourseRegistration register(long courseId, long userId)
      throws BadRequestException, AlreadyRegisteredException, LimitationOverFailureException;

  CourseRegistration checkRegistrationExist(long courseId, long userId)
      throws EntityNotFoundException;
}
