import React, { useState, useEffect } from 'react';
import { FaCalendarAlt } from 'react-icons/fa';

const LeaveRequestForm = () => {
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [reason, setReason] = useState('');
  const [employeeId, setEmployeeId] = useState('');
  const [employees, setEmployees] = useState([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await fetch('http://localhost:8080/employees');
        if (response.ok) {
          const data = await response.json();
          setEmployees(data);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };
    fetchEmployees();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const leaveRequest = { startDate, endDate, reason };

    try {
      const response = await fetch(`http://localhost:8080/leaves/apply/${employeeId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(leaveRequest),
      });

      if (response.ok) {
        setStartDate(''); setEndDate(''); setReason(''); setEmployeeId('');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="add-employee-form">
      <h2>Apply for Leave</h2>
      <label>Employee</label>
      <select className="add-employee-form select" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)} required>
        <option value="">Select Employee</option>
        {employees.map((employee) => (
          <option key={employee.id} value={employee.id}>{employee.name}</option>
        ))}
      </select>
      <label>Start Date</label>
      <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} required />
      <label>End Date</label>
      <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} required />
      <label>Reason</label>
      <textarea value={reason} onChange={(e) => setReason(e.target.value)} required />
      <button type="submit">Submit</button>
    </form>
  );
};

export default LeaveRequestForm;
