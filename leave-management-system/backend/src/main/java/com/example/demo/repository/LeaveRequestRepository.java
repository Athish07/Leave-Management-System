    package com.example.demo.repository;

    import com.example.demo.model.LeaveRequest;
    import com.example.demo.model.LeaveStatus;
    import org.springframework.data.jpa.repository.*;
    import org.springframework.data.repository.query.Param;

    import java.time.LocalDate;
    import java.util.List;

    public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

        List<LeaveRequest> findByEmployeeId(Long employeeId);

        @Query("""
            select (count(l) > 0) from LeaveRequest l
            where l.employee.id = :empId
                and l.status = :status
                and l.startDate <= :endDate
                and l.endDate   >= :startDate
            """)
        boolean hasOverlap(
                @Param("empId") Long employeeId,
                @Param("status") LeaveStatus status,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate
        );
    }
