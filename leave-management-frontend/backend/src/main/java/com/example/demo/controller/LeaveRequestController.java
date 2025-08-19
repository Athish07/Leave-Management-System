package com.example.demo.controller;

import com.example.demo.model.LeaveRequest;
import com.example.demo.service.LeaveRequestService;
import com.example.demo.web.dto.ApplyLeaveRequest;
import com.example.demo.web.dto.LeaveResponse;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveService;
    public LeaveRequestController(LeaveRequestService leaveService) { this.leaveService = leaveService; }

    @PostMapping("/apply/{employeeId}")
    public ResponseEntity<LeaveResponse> apply(@PathVariable Long employeeId, @Valid @RequestBody ApplyLeaveRequest req) {
        LeaveRequest lr = leaveService.applyLeave(employeeId, req.startDate(), req.endDate(), req.reason());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(lr));
    }

    @PutMapping("/{leaveId}/approve")
    public LeaveResponse approve(@PathVariable Long leaveId) {
        return toResponse(leaveService.approve(leaveId));
    }

    @PutMapping("/{leaveId}/reject")
    public LeaveResponse reject(@PathVariable Long leaveId) {
        return toResponse(leaveService.reject(leaveId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveRequest> byEmployee(@PathVariable Long employeeId) {
        return leaveService.getLeavesByEmployee(employeeId);
    }

    @GetMapping
    public List<LeaveRequest> all() {
        return leaveService.getAllLeaves();
    }

    private LeaveResponse toResponse(LeaveRequest lr) {
        return new LeaveResponse(
                lr.getId(),
                lr.getEmployee().getId(),
                lr.getStartDate(),
                lr.getEndDate(),
                lr.getStatus(),
                lr.getReason()
        );
    }
}
