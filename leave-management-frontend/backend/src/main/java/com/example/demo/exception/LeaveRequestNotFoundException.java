package com.example.demo.exception;

public class LeaveRequestNotFoundException extends RuntimeException {
    public LeaveRequestNotFoundException(Long id) { super("Leave request not found: " + id); }
}

