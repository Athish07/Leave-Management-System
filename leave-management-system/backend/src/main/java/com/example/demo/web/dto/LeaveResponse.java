package com.example.demo.web.dto;

import com.example.demo.model.LeaveStatus;
import java.time.LocalDate;

public record LeaveResponse(
        Long id,
        Long employeeId,
        LocalDate startDate,
        LocalDate endDate,
        LeaveStatus status,
        String reason
) {}

