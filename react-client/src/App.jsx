import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './styles.css';
import Appointments from './pages/Appointments';
import Clinics from './pages/Clinics';
import Dashboard from './pages/Dashboard';
import DoctorDashboard from './pages/DoctorDashboard';
import Doctors from './pages/Doctors';
import ForgotPassword from './pages/ForgotPassword';
import Index from './pages/Index';
import Login from './pages/Login';
import PatientDashboard from './pages/PatientDashboard';
import Register from './pages/Register';

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Index />} />
                <Route path="/appointments" element={<Appointments />} />
                <Route path="/clinics" element={<Clinics />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/doctor-dashboard" element={<DoctorDashboard />} />
                <Route path="/doctors" element={<Doctors />} />
                <Route path="/forgot-password" element={<ForgotPassword />} />
                
                <Route path="/login" element={<Login />} />
                <Route path="/patient-dashboard" element={<PatientDashboard />} />
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    );
}
