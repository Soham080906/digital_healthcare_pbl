import React from 'react';
import { Link } from 'react-router-dom';

export default function ForgotPassword() {
    return (
        <div className="forgotpassword-page">
            <div className="login-container">
        <div className="login-card">
            <div className="login-header">
                <h2>Forgot Password</h2>
                <p>Enter your email to receive a password reset OTP</p>
            </div>
            
            
            <form id="emailForm" className="login-form" style={{"display":"block"}}>
                <div className="form-group">
                    <label htmlFor="email">Email Address</label>
                    <input />
                </div>
                
                <button type="submit" className="login-btn">
                    <i className="fas fa-paper-plane"></i>
                    Send OTP
                </button>
            </form>

            
            <form id="otpForm" className="login-form" style={{"display":"none"}}>
                <div className="form-group">
                    <label htmlFor="otp">Enter OTP</label>
                    <input />
                    <small>OTP sent to <span id="userEmail"></span></small>
                </div>
                
                <div className="form-group">
                    <label htmlFor="newPassword">New Password</label>
                    <input />
                </div>
                
                <div className="form-group">
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <input />
                </div>
                
                <button type="submit" className="login-btn">
                    <i className="fas fa-key"></i>
                    Reset Password
                </button>
            </form>
            
            <div className="login-footer">
                <p>Remember your password? <a href="login.html">Login here</a></p>
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

        .form-group small {
            color: #6b7280;
            font-size: 0.875rem;
            margin-top: 0.25rem;
            display: block;
        }
    `}} />
        </div>
    );
}
