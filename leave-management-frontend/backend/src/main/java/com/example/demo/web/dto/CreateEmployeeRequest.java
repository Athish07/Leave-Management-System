package com.example.demo.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateEmployeeRequest(
        @NotBlank String name,
        @Email @NotBlank String email,
        @NotBlank String department,
        @NotNull LocalDate joiningDate
) {}

