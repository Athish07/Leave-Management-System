import React, { useState, useEffect } from 'react';
import { FaUsers } from 'react-icons/fa';

const EmployeeList = () => {
  const [employees, setEmployees] = useState([]);

  useEffect(() => {
    const fetchEmployees = async () => {
      try {
        const response = await fetch('http://localhost:8080/employees');
        if (response.ok) {
          const data = await response.json();
          setEmployees(data);
        } else {
          console.error('Failed to fetch employees');
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    fetchEmployees();
  }, []);

  return (
    <div className="employee-list">
      <h2><FaUsers /> Employee List</h2>
      <ul>
        {employees.map((employee) => (
          <li key={employee.id}>
            <strong>{employee.name}</strong>
            <br /><small>{employee.email}</small>
            <br />Dept: {employee.department} | Joined: {employee.joiningDate}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default EmployeeList;
