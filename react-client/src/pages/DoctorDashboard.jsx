import React from 'react';
import { Link } from 'react-router-dom';

export default function DoctorDashboard() {
    return (
        <div className="doctordashboard-page">
            <nav className="navbar">
        <div className="nav-container">
            <div className="nav-logo">
                <i className="fas fa-heartbeat"></i>
                <span>ClinicPlus</span>
            </div>
            <ul className="nav-menu">
                <li><a href="index.html" className="nav-link">Home</a></li>
                <li><a href="#patients" className="nav-link">Patients</a></li>
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
                <h1 id="welcomeMessage">Welcome, Dr. Unknown</h1>
                <p id="dashboardSubtitle">Manage your practice and patient appointments</p>
            </div>
        </div>

        <div className="dashboard-content">
            <div className="container">
                
                <div className="dashboard-grid">
                    <div className="dashboard-card">
                        <div className="card-header">
                            <i className="fas fa-calendar-check"></i>
                            <h3>Patient Appointments</h3>
                        </div>
                        <div className="card-content">
                            <div className="stat-number" id="appointmentCount">0</div>
                            <div className="stat-label">Scheduled</div>
                        </div>
                    </div>

                    <div className="dashboard-card">
                        <div className="card-header">
                            <i className="fas fa-user-injured"></i>
                            <h3>Total Patients</h3>
                        </div>
                        <div className="card-content">
                            <div className="stat-number" id="patientCount">0</div>
                            <div className="stat-label">Registered</div>
                        </div>
                    </div>

                    <div className="dashboard-card">
                        <div className="card-header">
                            <i className="fas fa-hospital"></i>
                            <h3>My Clinic</h3>
                        </div>
                        <div className="card-content">
                            <div className="stat-label" id="clinicName">Not assigned</div>
                        </div>
                    </div>
                </div>

                
                <section id="doctorProfileSection" className="dashboard-section">
                    <h2>My Profile</h2>
                    <div className="doctor-profile-card">
                        <div className="profile-header">
                            <div className="profile-avatar">
                                <i className="fas fa-user-md"></i>
                            </div>
                            <div className="profile-info">
                                <h3 id="doctorName">Dr. Unknown</h3>
                                <p id="doctorSpecialization">Specialization</p>
                                <p id="doctorClinic">Clinic</p>
                            </div>
                        </div>
                        <div className="profile-details">
                            <div className="detail-item">
                                <span className="label">License:</span>
                                <span id="doctorLicense">Not provided</span>
                            </div>
                            <div className="detail-item">
                                <span className="label">Experience:</span>
                                <span id="doctorExperience">0 years</span>
                            </div>
                            <div className="detail-item">
                                <span className="label">Education:</span>
                                <span id="doctorEducation">Not provided</span>
                            </div>
                            <div className="detail-item">
                                <span className="label">Phone:</span>
                                <span id="doctorPhone">Not provided</span>
                            </div>
                        </div>
                        <button className="btn btn-secondary" id="editProfileBtn">
                            <i className="fas fa-edit"></i>
                            Edit Profile
                        </button>
                    </div>
                </section>

                
                <div id="editProfileModal" className="modal">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h3>Edit Profile</h3>
                            <span className="close" id="closeEditModal">&times;</span>
                        </div>
                        <form id="editProfileForm">
                            <div className="form-group">
                                <label htmlFor="editName">Full Name</label>
                                <input />
                            </div>
                            <div className="form-group">
                                <label htmlFor="editSpecialization">Specialization</label>
                                <select id="editSpecialization" name="specialization" required>
                                    <option value="">Select specialization</option>
                                </select>
                            </div>
                            <div className="form-group">
                                <label htmlFor="editLicense">Medical License Number</label>
                                <input />
                            </div>
                            <div className="form-group">
                                <label htmlFor="editExperience">Years of Experience</label>
                                <input />
                            </div>
                            <div className="form-group">
                                <label htmlFor="editEducation">Education</label>
                                <input />
                            </div>
                            <div className="form-group">
                                <label htmlFor="editPhone">Phone Number</label>
                                <input />
                            </div>
                            <div className="form-group">
                                <label htmlFor="editClinic">Select Clinic</label>
                                <select id="editClinic" name="clinic">
                                    <option value="">Choose a clinic</option>
                                </select>
                            </div>
                            <div className="modal-actions">
                                <button type="button" className="btn btn-secondary" id="cancelEdit">Cancel</button>
                                <button type="submit" className="btn btn-primary">Save Changes</button>
                            </div>
                        </form>
                    </div>
                </div>

                
                <section className="dashboard-section">
                    <div className="section-header">
                        <div>
                            <h2>Patient Appointments</h2>
                            <p className="section-description">View and manage appointments booked by patients with you</p>
                        </div>
                        <div className="section-actions">
                            <div className="filter-controls">
                                <label htmlFor="appointmentFilter">Filter:</label>
                                <select id="appointmentFilter" className="filter-select">
                                    <option value="all">All Appointments</option>
                                    <option value="active">Active Only</option>
                                    <option value="cancelled">Cancelled Only</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div className="appointments-list" id="doctorAppointmentsList">
                        
                    </div>
                </section>

                
                <section id="clinics" className="dashboard-section">
                    <h2>Available Clinics</h2>
                    <div className="clinics-grid" id="clinicsGrid">
                        
                    </div>
                </section>
            </div>
        </div>
    </div>
        </div>
    );
}
