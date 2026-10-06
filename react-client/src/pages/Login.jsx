import React from 'react';
import { Link } from 'react-router-dom';

export default function Login() {
    return (
        <div className="login-page">
            <div className="login-container">
        <div className="login-card">
            <div className="login-header">
                <h2>Welcome Back</h2>
                <p>Sign in to your ClinicPlus account</p>
            </div>
            
            <form id="loginForm" className="login-form">
                <div className="form-group">
                    <label htmlFor="email">Email Address</label>
                    <input />
                </div>
                
                <div className="form-group">
                    <label htmlFor="password">Password</label>
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
                
                <button type="submit" className="login-btn">
                    <i className="fas fa-sign-in-alt"></i>
                    Sign In
                </button>
            </form>
            
            <div className="login-footer">
                <p>Don't have an account? <a href="register.html">Register here</a></p>
                <a href="index.html" className="back-home">
                    <i className="fas fa-arrow-left"></i>
                    Back to Home
                </a>
            </div>
        </div>
    </div>

    
    
    

    <style dangerouslySetInnerHTML={{__html: `
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
