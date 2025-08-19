import React, { useState, useEffect, useCallback } from 'react';
import { FaBalanceScale } from 'react-icons/fa';

const LeaveBalance = () => {
  const [leaveBalance, setLeaveBalance] = useState(0);
  const [employeeId, setEmployeeId] = useState('');
  const [employees, setEmployees] = useState([]);

  const fetchEmployees = useCallback(async () => {
    const response = await fetch('http://localhost:8080/employees');
    if (response.ok) setEmployees(await response.json());
  }, []);

  const fetchLeaveBalance = useCallback(async () => {
    if (employeeId) {
      const response = await fetch(`http://localhost:8080/employees/${employeeId}/leave-balance`);
      if (response.ok) setLeaveBalance(await response.json());
    }
  }, [employeeId]);

  useEffect(() => { fetchEmployees(); }, [fetchEmployees]);
  useEffect(() => { fetchLeaveBalance(); }, [employeeId, fetchLeaveBalance]);

  return (
    <div className="card shadow p-4">
      <h2 className="mb-4 text-primary"><FaBalanceScale /> Leave Balance</h2>
      <div className="mb-3">
        <label className="form-label">Select Employee</label>
        <select className="form-select" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)}>
          <option value="">Select Employee</option>
          {employees.map((employee) => (
            <option key={employee.id} value={employee.id}>{employee.name}</option>
          ))}
        </select>
      </div>
      <h5 className="text-success">Remaining Leave: {leaveBalance}</h5>
    </div>
  );
};

export default LeaveBalance;
