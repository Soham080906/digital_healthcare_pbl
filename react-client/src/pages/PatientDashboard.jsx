import React from 'react';
import { Link } from 'react-router-dom';

export default function PatientDashboard() {
    return (
        <div className="patientdashboard-page">
            <nav className="navbar">
        <div className="nav-container">
            <div className="nav-logo">
                <i className="fas fa-heartbeat"></i>
                <span>ClinicPlus</span>
            </div>
            <ul className="nav-menu">
                <li><a href="index.html" className="nav-link">Home</a></li>
                <li><a href="#appointments" className="nav-link">Appointments</a></li>
                <li><a href="#doctors" className="nav-link">Doctors</a></li>
                <li><a href="#clinics" className="nav-link">Clinics</a></li>
                <li><a href="#" className="nav-link" id="logoutBtn">Logout</a></li>
            </ul>
            <div className="hamburger">
                <span className="bar"></span>
                <span className="bar"></span>
                <span className="bar"></span>
            </div>
        </div>
    </nav>

    
    <div className="dashboard">
        <div className="dashboard-header">
            <div className="container">
                <h1 id="welcomeMessage">Welcome to Your Dashboard</h1>
                <p id="dashboardSubtitle">Manage your healthcare appointments and information</p>
                <a href="appointments.html" className="btn btn-primary" id="bookAppointmentBtn" style={{"display":"inline-block","marginTop":"1rem"}}>
                    <i className="fas fa-calendar-check"></i> Book Appointment
                </a>
                
                
                <div id="notificationSection" className="notification-section" style={{"display":"block","marginTop":"1rem"}}>
                    <div className="notification-header">
                        <button className="notification-toggle" id="notificationToggle" style={{"background":"none","border":"none","fontSize":"24px","cursor":"pointer","position":"relative","color":"#3b82f6","padding":"0.5rem"}}>
                            <i className="fas fa-bell"></i>
                            <span className="notification-badge" id="notificationBadge" style={{"position":"absolute","top":"0","right":"0","background":"#ef4444","color":"white","borderRadius":"50%","width":"20px","height":"20px","display":"flex","alignItems":"center","justifyContent":"center","fontSize":"12px","fontWeight":"bold"}}>0</span>
                        </button>
                    </div>
                    <div className="notification-panel" id="notificationPanel" style={{"display":"none","position":"absolute","top":"60px","right":"0","background":"white","border":"1px solid #e5e7eb","borderRadius":"8px","boxShadow":"0 4px 6px rgba(0, 0, 0, 0.1)","zIndex":"1000","minWidth":"300px"}}>
                        <div className="notification-content" style={{"padding":"1rem"}}>
                            <h4 style={{"margin":"0 0 1rem 0","fontSize":"16px","fontWeight":"600"}}>Upcoming Appointments</h4>
                            <div id="upcomingNotifications" style={{"maxHeight":"300px","overflowY":"auto"}}>
                                
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div className="dashboard-content">
            <div className="container">
                
                <div className="dashboard-grid">
                    <div className="dashboard-card">
                        <div className="card-header">
                            <i className="fas fa-calendar-check"></i>
                            <h3>My Appointments</h3>
                        </div>
                        <div className="card-content">
                            <div className="stat-number" id="appointmentCount">0</div>
                            <div className="stat-label">Upcoming</div>
                        </div>
                    </div>

                    <div className="dashboard-card">
                        <div className="card-header">
                            <i className="fas fa-user-md"></i>
                            <h3>Available Doctors</h3>
                        </div>
                        <div className="card-content">
                            <div className="stat-number" id="doctorCount">0</div>
                            <div className="stat-label">Specialists</div>
                        </div>
                    </div>

                    <div className="dashboard-card">
                        <div className="card-header">
                            <i className="fas fa-hospital"></i>
                            <h3>Nearby Clinics</h3>
                        </div>
                        <div className="card-content">
                            <div className="stat-number" id="clinicCount">0</div>
                            <div className="stat-label">Locations</div>
                        </div>
                    </div>
                </div>

                
                <section className="dashboard-section">
                    <h2>My Appointments</h2>
                    <div className="appointments-list" id="appointmentsList">
                        
                    </div>
                </section>

                
                <section id="doctors" className="dashboard-section">
                    <h2>Available Doctors</h2>
                    <div className="doctors-grid" id="doctorsGrid">
                        
                    </div>
                </section>

                
                <section id="clinics" className="dashboard-section">
                    <h2>Nearby Clinics</h2>
                    <div className="clinics-grid" id="clinicsGrid">
                        
                    </div>
                </section>
            </div>
        </div>
    </div>
        </div>
    );
}
