import './App.css';
import React, { Suspense, lazy } from 'react';
import { BrowserRouter as Router, Route, Link, Routes } from 'react-router-dom';

const AddEmployeeForm = lazy(() => import('./components/AddEmployeeForm'));
const EmployeeList = lazy(() => import('./components/EmployeeList'));
const LeaveRequestForm = lazy(() => import('./components/LeaveRequestForm'));
const LeaveRequestList = lazy(() => import('./components/LeaveRequestList'));
const LeaveBalance = lazy(() => import('./components/LeaveBalance'));

function App() {
  return (
    <Router>
      <div className="App">
        {/* Navbar */}
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
          <div className="container-fluid">
            <h1>LeaveMS</h1>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
              <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
              <ul className="navbar-nav ms-auto">
                <li className="nav-item"><Link className="nav-link" to="/">Add Employee</Link></li>
                <li className="nav-item"><Link className="nav-link" to="/employees">Employee List</Link></li>
                <li className="nav-item"><Link className="nav-link" to="/leave">Apply Leave</Link></li>
                <li className="nav-item"><Link className="nav-link" to="/leave-requests">Leave Requests</Link></li>
                <li className="nav-item"><Link className="nav-link" to="/leave-balance">Leave Balance</Link></li>
              </ul>
            </div>
          </div>
        </nav>

        {/* Main Content */}
        <div className="container my-4">
          <Suspense fallback={<div className="text-center my-5">Loading...</div>}>
            <Routes>
              <Route path="/" element={<AddEmployeeForm />} />
              <Route path="/employees" element={<EmployeeList />} />
              <Route path="/leave" element={<LeaveRequestForm />} />
              <Route path="/leave-requests" element={<LeaveRequestList />} />
              <Route path="/leave-balance" element={<LeaveBalance />} />
            </Routes>
          </Suspense>
        </div>
      </div>
    </Router>
  );
}

export default App;
