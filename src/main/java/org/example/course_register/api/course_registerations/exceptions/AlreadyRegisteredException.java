package org.example.course_register.api.course_registerations.exceptions;

public class AlreadyRegisteredException extends RuntimeException {
  public AlreadyRegisteredException() {
    super("이미 수강 중인 특강입니다.");
  }
}
