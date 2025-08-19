package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false) // unique email
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private LocalDate joiningDate;

    @Column(nullable = false)
    private int leaveBalance = 20; // Default leave balance (e.g., 20 days)

    // Constructors
    public Employee() {}

    public Employee(String name, String email, String department, LocalDate joiningDate) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.joiningDate = joiningDate;
        this.leaveBalance = 20;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public int getLeaveBalance() { return leaveBalance; }
    public void setLeaveBalance(int leaveBalance) { this.leaveBalance = leaveBalance; }

}
