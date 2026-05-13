/**
 * API Client with JWT Authentication Support
 * Handles automatic token inclusion, error handling, and 401/403 responses
 */

const API_BASE_URL = 'https://digital-healthcare-pbl.onrender.com/api';

/**
 * Get JWT token from localStorage
 */
function getToken() {
    return localStorage.getItem('token');
}

/**
 * Get authorization header with JWT token
 */
function getAuthHeader() {
    const token = getToken();
    if (token) {
        return {
            'Authorization': `Bearer ${token}`
        };
    }
    return {};
}

/**
 * Handle API response errors
 * 401: Redirect to login
 * 403: Show permission error
 */
function handleResponseError(status, errorData) {
    if (status === 401) {
        // Token expired or invalid
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userId');
        localStorage.removeItem('userName');
        localStorage.removeItem('userEmail');
        localStorage.removeItem('doctorProfile');
        localStorage.removeItem('patientProfile');
        
        window.showToast('Your session has expired. Please login again.', 'error');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1500);
        return;
    }
    
    if (status === 403) {
        window.showToast('You do not have permission to access this resource.', 'error');
        return;
    }
    
    if (status === 404) {
        window.showToast('Resource not found.', 'error');
        return;
    }
    
    if (status >= 500) {
        window.showToast('Server error. Please try again later.', 'error');
        return;
    }
    
    // Try to show error message from backend
    if (errorData && errorData.message) {
        window.showToast(errorData.message, 'error');
    } else if (errorData && errorData.error) {
        window.showToast(errorData.error, 'error');
    } else {
        window.showToast('An error occurred. Please try again.', 'error');
    }
}

/**
 * Perform an API request with JWT authentication
 * 
 * @param {string} endpoint - API endpoint (e.g., '/appointments')
 * @param {object} options - Fetch options (method, body, etc.)
 * @returns {Promise} Response data or null if error
 */
async function apiRequest(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    
    // Prepare request options
    const requestOptions = {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...getAuthHeader(),
            ...options.headers
        }
    };
    
    try {
        const response = await fetch(url, requestOptions);
        
        // Handle response
        let data = null;
        try {
            data = await response.json();
        } catch (e) {
            // Response is not JSON
            data = { message: response.statusText };
        }
        
        if (!response.ok) {
            handleResponseError(response.status, data);
            return null;
        }
        
        return data;
    } catch (error) {
        console.error('API Request Error:', error);
        window.showToast('Network error. Please check your connection.', 'error');
        return null;
    }
}

/**
 * GET request
 */
async function apiGet(endpoint) {
    return apiRequest(endpoint, {
        method: 'GET'
    });
}

/**
 * POST request
 */
async function apiPost(endpoint, data) {
    return apiRequest(endpoint, {
        method: 'POST',
        body: JSON.stringify(data)
    });
}

/**
 * PUT request
 */
async function apiPut(endpoint, data) {
    return apiRequest(endpoint, {
        method: 'PUT',
        body: JSON.stringify(data)
    });
}

/**
 * PATCH request
 */
async function apiPatch(endpoint, data) {
    return apiRequest(endpoint, {
        method: 'PATCH',
        body: JSON.stringify(data)
    });
}

/**
 * DELETE request
 */
async function apiDelete(endpoint) {
    return apiRequest(endpoint, {
        method: 'DELETE'
    });
}

/**
 * Check if user is authenticated
 */
function isAuthenticated() {
    return !!getToken();
}

/**
 * Get current user info from localStorage
 */
function getCurrentUser() {
    if (!isAuthenticated()) {
        return null;
    }
    
    return {
        id: localStorage.getItem('userId'),
        name: localStorage.getItem('userName'),
        email: localStorage.getItem('userEmail'),
        role: localStorage.getItem('userRole')
    };
}

/**
 * Check if current user has a specific role
 */
function hasRole(requiredRole) {
    const user = getCurrentUser();
    if (!user) return false;
    return user.role?.toLowerCase() === requiredRole.toLowerCase();
}

/**
 * Check if current user has any of the specified roles
 */
function hasAnyRole(requiredRoles) {
    const user = getCurrentUser();
    if (!user) return false;
    return requiredRoles.some(role => user.role?.toLowerCase() === role.toLowerCase());
}

/**
 * Logout user
 */
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('doctorProfile');
    localStorage.removeItem('patientProfile');
    
    window.showToast('Logged out successfully', 'success');
    setTimeout(() => {
        window.location.href = 'login.html';
    }, 1500);
}

/**
 * Redirect to login if not authenticated
 */
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'login.html';
    }
}

/**
 * Redirect to login if user doesn't have required role
 */
function requireRole(requiredRole) {
    if (!hasRole(requiredRole)) {
        window.showToast('You do not have permission to access this page.', 'error');
        setTimeout(() => {
            window.location.href = 'dashboard.html';
        }, 1500);
    }
}

// Export functions globally
window.apiClient = {
    apiRequest,
    apiGet,
    apiPost,
    apiPut,
    apiPatch,
    apiDelete,
    isAuthenticated,
    getCurrentUser,
    hasRole,
    hasAnyRole,
    logout,
    requireAuth,
    requireRole,
    getToken,
    getAuthHeader
};
