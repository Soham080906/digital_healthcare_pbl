import React from 'react';
import { Link } from 'react-router-dom';

export default function Index() {
    return (
        <div className="index-page">
            <nav className="navbar">
        <div className="nav-container">
            <div className="nav-logo">
                <i className="fas fa-heartbeat"></i>
                <span>ClinicPlus</span>
            </div>
            <ul className="nav-menu">
                <li><a href="#home" className="nav-link">Home</a></li>
                <li><a href="#services" className="nav-link">Services</a></li>
                <li><a href="doctors.html" className="nav-link">Doctors</a></li>
                <li><a href="clinics.html" className="nav-link">Clinics</a></li>
                <li id="authSection">
                    
                </li>
            </ul>
            <div className="hamburger">
                <span className="bar"></span>
                <span className="bar"></span>
                <span className="bar"></span>
            </div>
        </div>
    </nav>

    
    <section id="home" className="hero">
        <div className="hero-container">
            <div className="hero-content">
                <h1 className="hero-title">
                    Modern Healthcare
                    <span className="highlight">Management</span>
                </h1>
                <p className="hero-description">
                    Streamline your clinic operations with our comprehensive healthcare management system. 
                    Book appointments, manage patients, and coordinate care seamlessly.
                </p>
                <div className="hero-buttons">
                    <a href="#" className="btn btn-primary" id="bookAppointmentBtn">
                        <i className="fas fa-calendar-check"></i>
                        Book Appointment
                    </a>
                </div>
                
                <div className="user-status" id="userStatus">
                
                </div>
            </div>
            <div className="hero-image">
                <div className="hero-card">
                    <div className="card-icon">
                        <i className="fas fa-user-md"></i>
                    </div>
                    <h3>Expert Doctors</h3>
                    <p>Specialized healthcare professionals</p>
                </div>
                <div className="hero-card">
                    <div className="card-icon">
                        <i className="fas fa-hospital"></i>
                    </div>
                    <h3>Modern Clinics</h3>
                    <p>State-of-the-art medical facilities</p>
                </div>
                <div className="hero-card">
                    <div className="card-icon">
                        <i className="fas fa-clock"></i>
                    </div>
                    <h3>24/7 Support</h3>
                    <p>Round-the-clock patient care</p>
                </div>
            </div>
        </div>
    </section>

    
    <section id="services" className="services">
        <div className="container">
            <div className="section-header">
                <h2>Our Services</h2>
                <p>Comprehensive healthcare solutions for modern clinics</p>
            </div>
            <div className="services-grid">
                <div className="service-card">
                    <div className="service-icon">
                        <i className="fas fa-calendar-alt"></i>
                    </div>
                    <h3>Appointment Booking</h3>
                    <p>Easy online appointment scheduling with real-time availability</p>
                </div>
                <div className="service-card">
                    <div className="service-icon">
                        <i className="fas fa-user-friends"></i>
                    </div>
                    <h3>Patient Management</h3>
                    <p>Comprehensive patient records and history tracking</p>
                </div>
                <div className="service-card">
                    <div className="service-icon">
                        <i className="fas fa-stethoscope"></i>
                    </div>
                    <h3>Doctor Directory</h3>
                    <p>Find specialists and book consultations easily</p>
                </div>
                <div className="service-card">
                    <div className="service-icon">
                        <i className="fas fa-clinic-medical"></i>
                    </div>
                    <h3>Clinic Management</h3>
                    <p>Streamlined clinic operations and administration</p>
                </div>
            </div>
        </div>
    </section>

    
    <section className="stats">
        <div className="container">
            <div className="stats-grid">
                <div className="stat-item">
                    <div className="stat-number">5+</div>
                    <div className="stat-label">Happy Patients</div>
                </div>
                <div className="stat-item">
                    <div className="stat-number">10+</div>
                    <div className="stat-label">Expert Doctors</div>
                </div>
                <div className="stat-item">
                    <div className="stat-number">4+</div>
                    <div className="stat-label">Modern Clinics</div>
                </div>
                <div className="stat-item">
                    <div className="stat-number">24/7</div>
                    <div className="stat-label">Support Available</div>
                </div>
            </div>
        </div>
    </section>

    
    <footer className="footer">
        <div className="container">
            <div className="footer-content">
                <div className="footer-section">
                    <h3>ClinicPlus</h3>
                    <p>Modern healthcare management system for clinics and patients.</p>
                    <div className="social-links">
                       
                    </div>
                </div>
                <div className="footer-section">
                    <h4>Quick Links</h4>
                    <ul>
                        <li><a href="#home">Home</a></li>
                        <li><a href="#services">Services</a></li>
                        <li><a href="doctors.html">Doctors</a></li>
                        <li><a href="clinics.html">Clinics</a></li>
                    </ul>
                </div>
                <div className="footer-section">
                    <h4>Services</h4>
                    <ul>
                        <li><a href="#">Appointment Booking</a></li>
                        <li><a href="#">Patient Management</a></li>
                        <li><a href="#">Doctor Directory</a></li>
                        <li><a href="#">Clinic Management</a></li>
                    </ul>
                </div>
                <div className="footer-section">
                    <h4>Contact</h4>
                    <p><i className="fas fa-phone"></i> +91 8686658586</p>
                    <p><i className="fas fa-envelope"></i> info@clinicplus.com</p>
                </div>
            </div>
            
        </div>
    </footer>
        </div>
    );
}
