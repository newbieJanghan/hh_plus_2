package org.example.course_register.api.course_registerations.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCourseRegistrationRequestDto {
  @NotNull
  @Min(1)
  public long courseId;

  @NotNull
  @Min(1)
  public long userId;
}
