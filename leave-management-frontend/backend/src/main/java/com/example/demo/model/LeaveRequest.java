    package com.example.demo.model;

    import jakarta.persistence.*;
    import java.time.LocalDate;

    @Entity
    @Table(name = "leave_requests")
    public class LeaveRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Many requests can belong to one employee
        @ManyToOne
        @JoinColumn(name = "employee_id", nullable = false)
        private Employee employee;

        @Column(nullable = false)
        private LocalDate startDate;

        @Column(nullable = false)
        private LocalDate endDate;

        @Enumerated(EnumType.STRING) // Store enum as string
        @Column(nullable = false)
        private LeaveStatus status = LeaveStatus.PENDING; // Default PENDING

        @Column(length = 255)
        private String reason;

        // Constructors
        public LeaveRequest() {}

        public LeaveRequest(Employee employee, LocalDate startDate, LocalDate endDate, String reason) {
            this.employee = employee;
            this.startDate = startDate;
            this.endDate = endDate;
            this.reason = reason;
            this.status = LeaveStatus.PENDING;
        }

        // Getters & Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Employee getEmployee() { return employee; }
        public void setEmployee(Employee employee) { this.employee = employee; }

        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

        public LeaveStatus getStatus() { return status; }
        public void setStatus(LeaveStatus status) { this.status = status; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
