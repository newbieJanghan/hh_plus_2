package org.example.course_register.course_register.exception;

public class AlreadyRegisteredException extends RuntimeException {
  public AlreadyRegisteredException() {
    super("이미 수강 중인 특강입니다.");
  }
}
