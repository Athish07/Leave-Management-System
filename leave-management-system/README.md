Mini Leave Management System (LMS) – MVP

A pragmatic Leave Management System for a startup (~50 employees). HR can add employees, employees can apply for leave, HR can approve/reject, and the system tracks leave balances.

Live Frontend: https://leave-management-system-indol.vercel.app/

Backend: Spring Boot (local by default)

 Problem → Why this exists

When teams grow, managing leave over spreadsheets and emails becomes messy: double bookings, no single source of truth, and zero visibility into balances.
This MVP solves it by providing:

A central place for employee profiles :

A simple workflow to apply / approve / reject leaves

Accurate balances with basic business rules

What’s in this MVP :

Employee management: Name, Email (unique), Department, Joining Date

Leave requests: start/end dates, reason, status (PENDING/APPROVED/REJECTED)

Approvals: HR approves or rejects

Balances: deducted on approval, validated on apply

Validation: strong input validation via jakarta.validation + DTOs

Edge cases: overlapping leaves, invalid dates, missing employee, etc.

Clean architecture: Controllers → Services → Repositories (JPA/Hibernate) → MySQL

Tech Stack :

Backend: Java 17, Spring Boot, Spring Web, Spring Data JPA (Hibernate), MySQL

Frontend: React, Axios, Bootstrap/Material-friendly

Testing: Postman (collection-ready workflows)

Build/Run: Maven, npm

Deployment: Vercel (frontend)

High-Level Architecture:

React (UI)  ──Axios──▶  Spring Boot API  ──JPA/Hibernate──▶  MySQL
   ▲                        │
   └────────────── JSON ◀───┘
JPA/Hibernate generates SQL and uses JDBC (mysql-connector-j) to talk to MySQL.
spring.jpa.show-sql=true prints SQL; MySQL8Dialect ensures MySQL-specific SQL.



Domain & Data Model :

Entities :

Employee

id (PK), name, email (unique), department, joiningDate, leaveBalance (default 20)

LeaveRequest

id (PK), employee (ManyToOne), startDate, endDate, status (enum), reason

Enum

LeaveStatus: PENDING, APPROVED, REJECTED
Stored as STRING via @Enumerated(EnumType.STRING) for readability and safety.

Relationship

LeaveRequest Many-to-One Employee via @ManyToOne + @JoinColumn(name="employee_id")
(Owning side = LeaveRequest → creates employee_id FK column.)

 Business Rules & Edge Cases (Handled) :

Validation at the boundary (DTOs + @Valid)

@NotBlank, @Email, @NotNull, size limits on reason, etc.

Bad requests return structured 400 with field errors.

Leave dates

startDate <= endDate

Must not be before employee’s joining date

No negative or zero duration (unless policy allows single-day leave, which we do)

Overlap prevention

No new PENDING/APPROVED leave that overlaps existing ones for same employee

Repository method:

@Query("""
  select (count(l) > 0) from LeaveRequest l
  where l.employee.id = :empId
    and l.status = :status
    and l.startDate <= :endDate
    and l.endDate   >= :startDate
""")
boolean hasOverlap(...);


Balance checks

On apply (or on approve), ensure requested days do not exceed remaining balance

On approve, deduct balance atomically; reject if insufficient

State transitions

Only PENDING requests can be approved/rejected

Idempotency/validation if trying to re-approve or re-reject

Uniqueness

email is unique; provides a clean 409/400 style response

Not found

Non-existent employee/leave → 404 Not Found

Notes:

Holidays/weekends calendar not included (future enhancement).

“Business days vs calendar days” currently calendar days for simplicity.

Why DTOs? And how we use them :

DTO (Data Transfer Object) decouples API contracts from entities:

Prevents over-posting (clients can’t set internal fields like leaveBalance or status).

Evolves API without breaking DB schema.

First place to enforce validation (@Valid) before business logic runs.

Examples used:

CreateEmployeeRequest (input)

EmployeeResponseDTO (output)

ApplyLeaveRequest (input)

LeaveResponseDTO (output)

UpdateLeaveStatusRequest (input with status)

Flow:
Controller receives JSON → maps to DTO → @Valid triggers bean validation → Service maps DTO → Entity and applies rules → Repository persists → service maps back to Response DTO.

Validation & Error Handling :

@Valid on controller methods applies Jakarta validation annotations in DTOs

MethodArgumentNotValidException → handled by @ControllerAdvice → returns 400 with field error list

Custom exceptions (e.g., ResourceNotFoundException, BusinessRuleViolationException, OverlapException, InsufficientBalanceException) map to:

404 Not Found (resource missing)

409 Conflict (overlap, duplicates)

422 Unprocessable Entity (business rule like insufficient balance)

Sample error response:
{
  "timestamp": "2025-08-18T12:34:56Z",
  "status": 409,
  "error": "Conflict",
  "message": "Leave dates overlap with an existing APPROVED/PENDING leave.",
  "path": "/api/employees/1/leaves"
}

Conclusion:

The Leave Management System (LMS) demonstrates the end-to-end development of a mini enterprise-grade application using Spring Boot (backend) and React (frontend). It follows clean coding practices, leverages DTOs with validation annotations (@Valid, @NotNull, @Email, etc.), and ensures error handling with descriptive responses.

By combining RESTful APIs, frontend integration, and deployment on Vercel, this project showcases how a real-world system can be designed, implemented, and deployed in a professional manner. With scalability considerations, validation mechanisms, and extensible architecture, this LMS can be further enhanced with additional features like role-based authentication, leave approval workflows, and reporting dashboards.

This project highlights practical problem-solving, adherence to best practices, and production-ready deployment skills, making it a solid foundation for further enterprise enhancements.
