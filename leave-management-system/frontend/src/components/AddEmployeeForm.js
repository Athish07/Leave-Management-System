import React, { useState } from 'react';
import { FaUserPlus } from 'react-icons/fa';

const AddEmployeeForm = ({ onAdd }) => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [department, setDepartment] = useState('');
  const [joiningDate, setJoiningDate] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    const employee = { name, email, department, joiningDate };

    try {
      const response = await fetch('http://localhost:8080/employees', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(employee),
      });

      if (response.ok) {
        const newEmployee = await response.json();
        setName(''); setEmail(''); setDepartment(''); setJoiningDate('');
        if (onAdd) onAdd(newEmployee);
      } else {
        console.error('Failed to add employee');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

 return (
    <form onSubmit={handleSubmit} className="add-employee-form">
      <h2>Add Employee</h2>
      <label>
        Name:
        <input type="text" value={name} onChange={(e) => setName(e.target.value)} required />
      </label>
      <label>
        Email:
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
      </label>
      <label>
        Department:
        <input type="text" value={department} onChange={(e) => setDepartment(e.target.value)} required />
      </label>
      <label>
        Joining Date:
        <input type="date" value={joiningDate} onChange={(e) => setJoiningDate(e.target.value)} required />
      </label>
      <button type="submit">Add Employee</button>
    </form>
  );
};

export default AddEmployeeForm;
