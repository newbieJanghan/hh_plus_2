package org.example.course_register.course_register.exception;

public class RegisterFailureException extends RuntimeException {
  public RegisterFailureException() {
    super("수강 인원 초과로 신청에 실패했습니다.");
  }
}
