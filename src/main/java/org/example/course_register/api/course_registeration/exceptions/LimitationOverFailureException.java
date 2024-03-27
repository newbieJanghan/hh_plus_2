package org.example.course_register.api.course_registeration.exceptions;

public class LimitationOverFailureException extends RuntimeException {
  public LimitationOverFailureException() {
    super("수강 신청이 마감되었습니다.");
  }
}
