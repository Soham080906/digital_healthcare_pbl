import React from 'react';
import { Link } from 'react-router-dom';

export default function Appointments() {
    return (
        <div className="appointments-page">
            <nav className="navbar">
        <div className="nav-container">
            <div className="nav-logo">
                <i className="fas fa-heartbeat"></i>
                <span>ClinicPlus</span>
            </div>
            <ul className="nav-menu">
                <li><a href="index.html" className="nav-link">Home</a></li>
                <li><a href="dashboard.html" className="nav-link">Dashboard</a></li>
                <li><a href="login.html" className="nav-link btn-login" id="loginLink">Login</a></li>
                <li><a href="register.html" className="nav-link btn-register" id="registerLink">Register</a></li>
            </ul>
            <div className="hamburger">
                <span className="bar"></span>
                <span className="bar"></span>
                <span className="bar"></span>
            </div>
        </div>
    </nav>

    
    <div className="appointment-page">
        <div className="container">
            <div className="appointment-header">
                <h1>Book Your Appointment</h1>
                <p>Choose your preferred clinic, doctor, and time slot</p>
            </div>

            <div className="appointment-content">
                <div className="booking-section">
                    <div className="booking-form-large">
                        <h2>Appointment Details</h2>
                        
                        <div className="form-grid">
                            <div className="form-group">
                                <label htmlFor="clinic">Select Clinic</label>
                                <select id="clinic" name="clinic" required>
                                    <option value="">Choose a clinic</option>
                                </select>
                            </div>
                            
                            <div className="form-group">
                                <label htmlFor="doctor">Select Doctor</label>
                                <select id="doctor" name="doctor" required>
                                    <option value="">Choose a doctor</option>
                                </select>
                            </div>
                            
                            <div className="form-group">
                                <label htmlFor="date">Appointment Date</label>
                                <input />
                            </div>
                            
                            <div className="form-group">
                                <label htmlFor="time">Appointment Time</label>
                                <input />
                                <div className="time-selection-info">
                                    <i className="fas fa-info-circle"></i>
                                    <span>Select a date and doctor to view available time slots</span>
                                </div>
                            </div>
                        </div>
                        
                        
                        <div className="time-slots-section">
                            <div className="slots-header">
                                <h3>Available Time Slots</h3>
                                <button type="button" className="btn btn-secondary btn-sm" id="refreshSlotsBtn" onclick="refreshTimeSlots()">
                                    <i className="fas fa-sync-alt"></i> Refresh
                                </button>
                            </div>
                            <div className="time-slots-grid" id="timeSlotsGrid">
                                
                            </div>
                        </div>
                        
                        <div className="form-group">
                            <label htmlFor="notes">Additional Notes (Optional)</label>
                            <textarea id="notes" name="notes" rows="4" placeholder="Any special requirements or notes for your appointment..."></textarea>
                        </div>
                        
                        <button type="button" className="btn btn-primary btn-large" id="bookAppointment">
                            <i className="fas fa-calendar-plus"></i>
                            Book Appointment
                        </button>
                    </div>
                </div>

                <div className="info-section">
                    <div className="info-card">
                        <div className="info-icon">
                            <i className="fas fa-clock"></i>
                        </div>
                        <h3>Appointment Duration</h3>
                        <p>Standard appointments are 30 minutes.</p>
                    </div>

                    <div className="info-card">
                        <div className="info-icon">
                            <i className="fas fa-calendar-check"></i>
                        </div>
                        <h3>Booking Policy</h3>
                        <p>Appointments can be booked in advance. 24-hour cancellation.</p>
                    </div>

                    <div className="info-card">
                        <div className="info-icon">
                            <i className="fas fa-phone"></i>
                        </div>
                        <h3>Need Help?</h3>
                        <p>Contact our support team at +91 8686658586 for assistance with booking.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    
    
    

    <style dangerouslySetInnerHTML={{__html: `
        .appointment-page {
            padding-top: 100px;
            min-height: 100vh;
            background: #f8fafc;
        }

        .appointment-header {
            text-align: center;
            margin-bottom: 3rem;
        }

        .appointment-header h1 {
            font-size: 2.5rem;
            color: #1e293b;
            margin-bottom: 1rem;
        }

        .appointment-header p {
            color: #64748b;
            font-size: 1.1rem;
        }

        .appointment-content {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 3rem;
            margin-bottom: 3rem;
        }

        .booking-form-large {
            background: white;
            padding: 2.5rem;
            border-radius: 20px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
        }

        .booking-form-large h2 {
            margin-bottom: 2rem;
            color: #1e293b;
            font-weight: 600;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1.5rem;
            margin-bottom: 1.5rem;
        }

        .form-group textarea {
            width: 100%;
            padding: 1rem;
            border: 2px solid #e5e7eb;
            border-radius: 10px;
            font-size: 1rem;
            font-family: inherit;
            resize: vertical;
            transition: border-color 0.3s ease;
        }

        .form-group textarea:focus {
            outline: none;
            border-color: #2563eb;
        }

        .btn-large {
            width: 100%;
            padding: 1.2rem;
            font-size: 1.1rem;
        }

        .info-section {
            display: flex;
            flex-direction: column;
            gap: 1.5rem;
        }

        .info-card {
            background: white;
            padding: 1.5rem;
            border-radius: 15px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        .info-icon {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 1rem;
            color: white;
            font-size: 1.5rem;
        }

        .info-card h3 {
            margin-bottom: 0.5rem;
            color: #1e293b;
            font-weight: 600;
        }

        .info-card p {
            color: #64748b;
            line-height: 1.6;
        }

        .time-slots-section {
            margin: 1.5rem 0;
            padding: 1.5rem;
            background: #f8fafc;
            border-radius: 15px;
            border: 2px solid #e2e8f0;
        }

        .time-slots-section h3 {
            margin-bottom: 1rem;
            color: #1e293b;
            font-weight: 600;
            font-size: 1.1rem;
        }

        .slots-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .slots-header h3 {
            margin: 0;
        }

        .time-slots-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
            gap: 1rem;
        }

        .time-slot {
            background: white;
            padding: 0.75rem;
            border-radius: 8px;
            text-align: center;
            border: 2px solid #e2e8f0;
            transition: all 0.3s ease;
            cursor: pointer;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }

        .time-slot.available:hover {
            border-color: #2563eb;
            background: #eff6ff;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(37, 99, 235, 0.15);
        }

        .time-slot.selected {
            border-color: #2563eb;
            background: #dbeafe;
            box-shadow: 0 4px 12px rgba(37, 99, 235, 0.2);
            transform: translateY(-1px);
        }

        .time-slot.booked {
            cursor: not-allowed;
            opacity: 0.6;
            background: #f1f5f9;
            border-color: #e5e7eb;
            position: relative;
        }

        .time-slot.booked::after {
            content: "✕";
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-size: 1.5rem;
            color: #dc2626;
            font-weight: bold;
            background: white;
            border-radius: 50%;
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .time-selection-info {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.75rem;
            background: #f8fafc;
            border: 2px solid #e2e8f0;
            border-radius: 8px;
            color: #64748b;
            font-size: 0.875rem;
        }

        .time-selection-info i {
            color: #3b82f6;
        }

        .no-slots-message {
            text-align: center;
            padding: 2rem;
            color: #64748b;
            background: #f8fafc;
            border-radius: 8px;
            border: 2px dashed #cbd5e1;
        }

        .no-slots-message i {
            font-size: 2rem;
            color: #dc2626;
            margin-bottom: 1rem;
            display: block;
        }

        .no-slots-message p {
            margin: 0;
            font-size: 1rem;
            line-height: 1.5;
        }

        .time-slot .time {
            display: block;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 0.25rem;
        }

        .time-slot .status {
            font-size: 0.8rem;
            padding: 0.25rem 0.5rem;
            border-radius: 15px;
            font-weight: 500;
        }

        .status.available {
            background: #dcfce7;
            color: #166534;
        }

        .status.booked {
            background: #fee2e2;
            color: #dc2626;
        }

        @media (max-width: 768px) {
            .appointment-content {
                grid-template-columns: 1fr;
            }

            .form-grid {
                grid-template-columns: 1fr;
            }

            .time-slots-grid {
                grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
            }
        }
    `}} />
        </div>
    );
}
