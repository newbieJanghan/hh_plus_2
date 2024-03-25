package org.example.course_register.course_register.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CourseRegisterCreateRequestDto {
  @NotNull
  @Min(1)
  public long userId;
}
