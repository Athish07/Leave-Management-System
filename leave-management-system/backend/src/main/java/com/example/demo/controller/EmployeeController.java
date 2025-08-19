package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.example.demo.web.dto.CreateEmployeeRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) { this.employeeService = employeeService; }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody CreateEmployeeRequest req) {
        Employee e = new Employee(req.name(), req.email(), req.department(), req.joiningDate());
        Employee saved = employeeService.addEmployee(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Employee> getAllEmployees() { return employeeService.getAllEmployees(); }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable Long id) { return employeeService.getEmployeeById(id); }

    @GetMapping("/{id}/leave-balance")
    public int getLeaveBalance(@PathVariable Long id) { return employeeService.getEmployeeById(id).getLeaveBalance(); }
}
