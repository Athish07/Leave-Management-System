package com.example.demo.exception;

public class LeaveBalanceExceededException extends RuntimeException {
    public LeaveBalanceExceededException(int needed, int available) {
        super("Insufficient leave balance. Needed: " + needed + ", Available: " + available);
    }
}


