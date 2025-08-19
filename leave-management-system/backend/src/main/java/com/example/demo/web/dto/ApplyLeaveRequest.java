// com/example/demo/web/dto/ApplyLeaveRequest.java
package com.example.demo.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ApplyLeaveRequest(
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate,
        @Size(max = 255) String reason
) {}
