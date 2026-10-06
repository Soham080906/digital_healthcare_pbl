import React from 'react';
import { Link } from 'react-router-dom';

export default function Clinics() {
    return (
        <div className="clinics-page">
            <nav className="navbar">
        <div className="nav-container">
            <div className="nav-logo">
                <i className="fas fa-heartbeat"></i>
                <span>ClinicPlus</span>
            </div>
            <ul className="nav-menu">
                <li><a href="index.html" className="nav-link">Home</a></li>
                <li><a href="doctors.html" className="nav-link">Doctors</a></li>
                <li><a href="appointments.html" className="nav-link">Book Appointment</a></li>
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

    
    <div className="clinics-page">
        <div className="container">
            <div className="clinics-header">
                <h1>Our Medical Clinics</h1>
                <p>Discover our network of modern healthcare facilities</p>
            </div>

            
            <div className="search-section">
                <div className="search-controls">
                    <div className="search-group">
                        <label htmlFor="searchClinic">Search Clinics</label>
                        <input />
                    </div>
                    <div className="search-group">
                        <label htmlFor="serviceFilter">Services</label>
                        <select id="serviceFilter">
                            <option value="">All Services</option>
                            <option value="cardiology">Cardiology</option>
                            <option value="pediatrics">Pediatrics</option>
                            <option value="dermatology">Dermatology</option>
                            <option value="orthopedics">Orthopedics</option>
                            <option value="general">General Medicine</option>
                        </select>
                    </div>
                </div>
            </div>

            
            <div className="clinics-grid" id="clinicsGrid">
                
            </div>

            
            <div id="loadingState" className="loading-state">
                <i className="fas fa-spinner fa-spin"></i>
                <p>Loading clinics...</p>
            </div>

            
            <div id="noResultsState" className="no-results-state" style={{"display":"none"}}>
                <i className="fas fa-search"></i>
                <h3>No clinics found</h3>
                <p>Try adjusting your search criteria</p>
            </div>
        </div>
    </div>

    
    
    

    <style dangerouslySetInnerHTML={{__html: `
        .clinics-page {
            padding-top: 100px;
            min-height: 100vh;
            background: #f8fafc;
        }

        .clinics-header {
            text-align: center;
            margin-bottom: 3rem;
        }

        .clinics-header h1 {
            font-size: 2.5rem;
            color: #1e293b;
            margin-bottom: 1rem;
        }

        .clinics-header p {
            color: #64748b;
            font-size: 1.1rem;
        }

        .search-section {
            background: white;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            margin-bottom: 2rem;
        }

        .search-controls {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1.5rem;
        }

        .search-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #374151;
            font-weight: 500;
        }

        .search-group input,
        .search-group select {
            width: 100%;
            padding: 0.75rem;
            border: 2px solid #e5e7eb;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease;
        }

        .search-group input:focus,
        .search-group select:focus {
            outline: none;
            border-color: #2563eb;
        }

        .clinics-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
            gap: 2rem;
            margin-bottom: 2rem;
        }

        .clinic-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .clinic-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
        }

        .clinic-header {
            display: flex;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .clinic-icon {
            width: 60px;
            height: 60px;
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.5rem;
            margin-right: 1rem;
        }

        .clinic-title h3 {
            margin: 0 0 0.5rem 0;
            color: #1e293b;
            font-weight: 600;
        }

        .clinic-title .location {
            margin: 0;
            color: #64748b;
            font-size: 0.9rem;
        }

        .clinic-info {
            margin-bottom: 1.5rem;
        }

        .info-item {
            display: flex;
            align-items: center;
            margin-bottom: 0.5rem;
            color: #64748b;
        }

        .info-item i {
            width: 20px;
            margin-right: 0.5rem;
            color: #2563eb;
        }

        .clinic-services,
        .clinic-specializations {
            margin-bottom: 1.5rem;
        }

        .clinic-services h4,
        .clinic-specializations h4 {
            margin: 0 0 0.75rem 0;
            color: #1e293b;
            font-weight: 600;
            font-size: 1rem;
        }

        .services-list,
        .specializations-list {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
        }

        .service-tag,
        .spec-tag {
            background: #eff6ff;
            color: #2563eb;
            padding: 0.25rem 0.75rem;
            border-radius: 15px;
            font-size: 0.8rem;
            font-weight: 500;
        }

        .spec-tag {
            background: #f0fdf4;
            color: #059669;
        }

        .clinic-actions {
            display: flex;
            gap: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #e5e7eb;
        }

        .btn-sm {
            padding: 0.5rem 1rem;
            font-size: 0.875rem;
        }

        .loading-state,
        .no-results-state {
            text-align: center;
            padding: 3rem;
            color: #64748b;
        }

        .loading-state i,
        .no-results-state i {
            font-size: 3rem;
            margin-bottom: 1rem;
            color: #2563eb;
        }

        .no-results-state h3 {
            margin: 1rem 0 0.5rem 0;
            color: #1e293b;
        }

        @media (max-width: 768px) {
            .search-controls {
                grid-template-columns: 1fr;
            }

            .clinics-grid {
                grid-template-columns: 1fr;
            }

            .clinic-actions {
                flex-direction: column;
            }
        }
    `}} />
        </div>
    );
}
