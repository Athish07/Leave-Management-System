import React, { useState, useEffect, useCallback } from 'react';
import { FaCheckCircle, FaTimesCircle, FaClipboardList } from 'react-icons/fa';

const LeaveRequestList = () => {
  const [leaveRequests, setLeaveRequests] = useState([]);
  const [employeeId, setEmployeeId] = useState('');
  const [employees, setEmployees] = useState([]);

  const fetchEmployees = useCallback(async () => {
    const response = await fetch('http://localhost:8080/employees');
    if (response.ok) setEmployees(await response.json());
  }, []);

  const fetchLeaveRequests = useCallback(async () => {
    if (employeeId) {
      const response = await fetch(`http://localhost:8080/leaves/employee/${employeeId}`);
      if (response.ok) setLeaveRequests(await response.json());
    }
  }, [employeeId]);

  useEffect(() => { fetchEmployees(); }, [fetchEmployees]);
  useEffect(() => { fetchLeaveRequests(); }, [fetchLeaveRequests]);

  const handleApprove = async (id) => {
    await fetch(`http://localhost:8080/leaves/${id}/approve`, { method: 'PUT' });
    fetchLeaveRequests();
  };

  const handleReject = async (id) => {
    await fetch(`http://localhost:8080/leaves/${id}/reject`, { method: 'PUT' });
    fetchLeaveRequests();
  };

  return (
    <div className="card shadow p-4">
      <h2 className="mb-4 text-primary"><FaClipboardList /> Leave Requests</h2>
      <div className="mb-3">
        <label className="form-label">Select Employee</label>
        <select className="form-select" value={employeeId} onChange={(e) => setEmployeeId(e.target.value)}>
          <option value="">Select Employee</option>
          {employees.map((employee) => (
            <option key={employee.id} value={employee.id}>{employee.name}</option>
          ))}
        </select>
      </div>
      <ul className="list-group">
        {leaveRequests.map((leave) => (
          <li key={leave.id} className="list-group-item">
            <p><strong>Start:</strong> {leave.startDate} | <strong>End:</strong> {leave.endDate}</p>
            <p><strong>Reason:</strong> {leave.reason}</p>
            <p>
              <strong>Status:</strong>{" "}
              <span className={`badge ${leave.status === 'APPROVED' ? 'bg-success' :
                leave.status === 'REJECTED' ? 'bg-danger' : 'bg-warning text-dark'}`}>
                {leave.status}
              </span>
            </p>
            <div>
              <button className="btn btn-sm btn-success me-2" onClick={() => handleApprove(leave.id)}>
                <FaCheckCircle /> Approve
              </button>
              <button className="btn btn-sm btn-danger" onClick={() => handleReject(leave.id)}>
                <FaTimesCircle /> Reject
              </button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default LeaveRequestList;
