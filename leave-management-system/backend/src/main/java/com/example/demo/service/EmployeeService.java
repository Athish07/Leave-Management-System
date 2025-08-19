package com.example.demo.service;

import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) { this.employeeRepository = employeeRepository; }

    @Transactional
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public void adjustLeaveBalance(Long employeeId, int delta) {
        Employee e = getEmployeeById(employeeId);
        int newBal = e.getLeaveBalance() + delta;
        if (newBal < 0) throw new IllegalArgumentException("Negative balance not allowed");
        e.setLeaveBalance(newBal);
        employeeRepository.save(e);
    }
}
