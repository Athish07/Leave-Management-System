package com.example.demo.service;

import com.example.demo.exception.*;
import com.example.demo.model.*;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public LeaveRequest applyLeave(Long employeeId, LocalDate start, LocalDate end, String reason) {
        if (start == null || end == null) throw new InvalidLeaveOperationException("Start and end dates are required");
        if (end.isBefore(start)) throw new InvalidLeaveOperationException("End date cannot be before start date");

        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

        // Optional: prevent applying in the past (business choice)
        // if (start.isBefore(LocalDate.now())) throw new InvalidLeaveOperationException("Cannot apply retroactively");

        int days = daysInclusive(start, end);

        // (Do NOT deduct now; deduct on approval.) Check balance feasibility at apply-time only if you want early feedback:
        if (emp.getLeaveBalance() <= 0) {
            throw new LeaveBalanceExceededException(days, emp.getLeaveBalance());
        }

        // Overlap check against APPROVED leaves (you may also include PENDING if policy requires)
        boolean overlaps = leaveRequestRepository.hasOverlap(emp.getId(), LeaveStatus.APPROVED, start, end);
        if (overlaps) throw new InvalidLeaveOperationException("Requested dates overlap with already approved leave");

        LeaveRequest lr = new LeaveRequest(emp, start, end, reason);
        lr.setStatus(LeaveStatus.PENDING);
        return leaveRequestRepository.save(lr);
    }

    @Transactional
    public LeaveRequest approve(Long leaveId) {
        LeaveRequest lr = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new LeaveRequestNotFoundException(leaveId));

        if (lr.getStatus() == LeaveStatus.APPROVED)
            throw new InvalidLeaveOperationException("Leave already approved");
        if (lr.getStatus() == LeaveStatus.REJECTED)
            throw new InvalidLeaveOperationException("Cannot approve a rejected leave");

        // Re-validate overlap and balance at approval time
        boolean overlaps = leaveRequestRepository.hasOverlap(lr.getEmployee().getId(),
                LeaveStatus.APPROVED, lr.getStartDate(), lr.getEndDate());
        if (overlaps) throw new InvalidLeaveOperationException("Dates now overlap with another approved leave");

        int days = daysInclusive(lr.getStartDate(), lr.getEndDate());
        Employee emp = lr.getEmployee();
        if (emp.getLeaveBalance() < days)
            throw new LeaveBalanceExceededException(days, emp.getLeaveBalance());

        emp.setLeaveBalance(emp.getLeaveBalance() - days);
        lr.setStatus(LeaveStatus.APPROVED);

        // save both (same transaction)
        employeeRepository.save(emp);
        return leaveRequestRepository.save(lr);
    }

    @Transactional
    public LeaveRequest reject(Long leaveId) {
        LeaveRequest lr = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new LeaveRequestNotFoundException(leaveId));

        if (lr.getStatus() == LeaveStatus.REJECTED)
            throw new InvalidLeaveOperationException("Leave already rejected");
        if (lr.getStatus() == LeaveStatus.APPROVED)
            throw new InvalidLeaveOperationException("Cannot reject an already approved leave");

        lr.setStatus(LeaveStatus.REJECTED);
        return leaveRequestRepository.save(lr);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequest> getLeavesByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestRepository.findAll();
    }

    private int daysInclusive(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        if (days <= 0) throw new InvalidLeaveOperationException("Invalid date range");
        return Math.toIntExact(days);
    }
}
