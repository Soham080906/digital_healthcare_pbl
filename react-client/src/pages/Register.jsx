import React from 'react';
import { Link } from 'react-router-dom';

export default function Register() {
    return (
        <div className="register-page">
            <div className="login-container">
        <div className="login-card">
            <div className="login-header">
                <h2>Create Account</h2>
                <p>Join ClinicPlus for better healthcare management</p>
            </div>
            
            <form id="registerForm" className="login-form">
                <div className="form-group">
                    <label htmlFor="name">Full Name</label>
                    <input />
                </div>
                
                <div className="form-group">
                    <label htmlFor="email">Email Address</label>
                    <input />
                </div>
                
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input />
                </div>
                
                <div className="form-group">
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <input />
                    <div className="forgot-password-link">
                        <a href="forgot-password.html">Forgot Password?</a>
                    </div>
                </div>
                
                <div className="form-group">
                    <label htmlFor="role">Role</label>
                    <select id="role" name="role" required>
                        <option value="">Select your role</option>
                        <option value="patient">Patient</option>
                        <option value="doctor">Doctor</option>
                    </select>
                </div>
                
                
                <div id="doctorFields" className="doctor-fields" style={{"display":"none"}}>
                    <div className="form-group">
                        <label htmlFor="specialization">Specialization</label>
                        <select id="specialization" name="specialization">
                            <option value="">Select specialization</option>
                            <option value="Cardiologist">Cardiologist</option>
                            <option value="Pediatrician">Pediatrician</option>
                            <option value="Dermatologist">Dermatologist</option>
                            <option value="Orthopedic Surgeon">Orthopedic Surgeon</option>
                            <option value="Neurologist">Neurologist</option>
                            <option value="Psychiatrist">Psychiatrist</option>
                            <option value="General Physician">General Physician</option>
                            <option value="Dentist">Dentist</option>
                            <option value="Ophthalmologist">Ophthalmologist</option>
                            <option value="Gynecologist">Gynecologist</option>
                        </select>
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="licenseNumber">Medical License Number</label>
                        <input />
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="experience">Years of Experience</label>
                        <input />
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="education">Education</label>
                        <input />
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="phone">Phone Number</label>
                        <input />
                    </div>
                    
                    <div className="form-group">
                        <label htmlFor="clinic">Select Clinic (Optional)</label>
                        <select id="clinic" name="clinic">
                            <option value="">Choose a clinic (optional)</option>
                        </select>
                    </div>
                </div>
                
                <button type="submit" className="login-btn">
                    <i className="fas fa-user-plus"></i>
                    Create Account
                </button>
            </form>
            
            <div className="login-footer">
                <p>Already have an account? <a href="login.html">Sign in here</a></p>
                <a href="index.html" className="back-home">
                    <i className="fas fa-arrow-left"></i>
                    Back to Home
                </a>
            </div>
        </div>
    </div>

    
    
    

    <style dangerouslySetInnerHTML={{__html: `
        .doctor-fields {
            border-top: 1px solid #e5e7eb;
            padding-top: 1.5rem;
            margin-top: 1.5rem;
        }

        .doctor-fields .form-group {
            margin-bottom: 1rem;
        }

        .toast {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        @keyframes slideIn {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }
    `}} />
        </div>
    );
}
