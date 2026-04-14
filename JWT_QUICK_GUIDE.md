# JWT Authentication - Quick Integration Guide

## Quick Start

### For Login/Registration Pages
Simply use the existing login.html and register.html - they already handle JWT token storage and user info.

### For Protected Pages
1. Add script imports:
```html
<script src="script.js"></script>
<script src="api-client.js"></script>
```

2. Check authentication on page load:
```javascript
// For pages that require login
window.apiClient.requireAuth();

// For pages that require specific role
window.apiClient.requireRole('doctor');
```

3. Make API calls:
```javascript
// Instead of fetch():
const result = await window.apiClient.apiPost('/appointments', data);
const result = await window.apiClient.apiGet('/doctors/profile');

// Error handling is automatic - no need to handle 401/403
```

### For Navigation Elements
The script.js automatically handles showing/hiding login/logout buttons based on authentication state.

## API Client Methods

### Authentication Checks
```javascript
// Check if user is logged in
if (window.apiClient.isAuthenticated()) { ... }

// Check user role
if (window.apiClient.hasRole('doctor')) { ... }

// Check if user has any role
if (window.apiClient.hasAnyRole(['doctor', 'admin'])) { ... }

// Get current user info
const user = window.apiClient.getCurrentUser();
console.log(user.name, user.role, user.email);

// Logout
window.apiClient.logout();

// Require authentication (redirect if not logged in)
window.apiClient.requireAuth();

// Require specific role (redirect with error if no role)
window.apiClient.requireRole('patient');
```

### API Methods
```javascript
// GET - No body
const data = await window.apiClient.apiGet('/doctors/profile');

// POST - With body
const result = await window.apiClient.apiPost('/appointments', {
    clinic: 'clinic-id',
    doctor: 'doctor-id',
    slot: '2024-05-15T10:00:00'
});

// PUT - Update resource
const updated = await window.apiClient.apiPut('/appointments/1', {
    status: 'cancelled'
});

// PATCH - Partial update
const patched = await window.apiClient.apiPatch('/doctors/profile', {
    phone: '1234567890'
});

// DELETE - Delete resource
const result = await window.apiClient.apiDelete('/appointments/1');
```

## Common Scenarios

### Scenario 1: Book Appointment (Patient Only)
```javascript
// Check authentication
window.apiClient.requireRole('patient');

// Make booking
const appointment = await window.apiClient.apiPost('/appointments', {
    clinic: clinicId,
    doctor: doctorId,
    slot: dateTime,
    notes: 'Any notes'
});

// Error is handled automatically - if 401, user is redirected to login
// If 403, user sees "permission denied" message
```

### Scenario 2: Get Doctor Profile
```javascript
// This endpoint requires DOCTOR role
window.apiClient.requireRole('doctor');

const profile = await window.apiClient.apiGet('/doctors/profile');
if (profile) {
    // Update UI with profile
    document.getElementById('doctorName').textContent = profile.name;
}
```

### Scenario 3: Show Role-Based UI
```javascript
// Show elements based on role
if (window.apiClient.hasRole('doctor')) {
    document.getElementById('doctorSection').style.display = 'block';
} else {
    document.getElementById('patientSection').style.display = 'block';
}

// Or check any role
if (window.apiClient.hasAnyRole(['doctor', 'admin'])) {
    document.getElementById('adminPanel').style.display = 'block';
}
```

### Scenario 4: Handle Logout
```javascript
// Simple logout
document.getElementById('logoutBtn').addEventListener('click', (e) => {
    e.preventDefault();
    window.apiClient.logout(); // Clears tokens and redirects
});
```

## Error Handling - Automatic!

You don't need to handle errors in your code - api-client does it automatically:

| Status | Auto Action | User Sees |
|--------|------------|-----------|
| 200 | Return data | Success |
| 401 | Clear tokens + redirect to login | "Session expired" message |
| 403 | Show error | "Permission denied" message |
| 404 | Show error | "Resource not found" message |
| 5xx | Show error | "Server error" message |

If you need custom error handling, modify `handleResponseError()` in api-client.js.

## LocalStorage Structure

The following data is stored when user logs in:
```javascript
localStorage = {
    token: "eyJhbGci...", // JWT token - REQUIRED for protected endpoints
    userRole: "patient",   // Role for authorization checks
    userId: "123",         // User ID for API calls
    userName: "John Doe",  // Display name
    userEmail: "john@...", // Email for password reset, etc
    doctorProfile: "{...}", // If user is doctor
    patientProfile: "{...}"  // If user is patient
}
```

When user logs out, all these are cleared.

## Testing Protected Endpoints

### Test 1: Call Protected Endpoint with Valid Token
```javascript
const result = await window.apiClient.apiGet('/patients/dashboard');
// Should return data
```

### Test 2: Call Protected Endpoint with Wrong Role
```javascript
// Login as patient, try to access doctor endpoint
const result = await window.apiClient.apiGet('/doctors/profile');
// Should show "Permission denied" and NOT redirect
```

### Test 3: Call Protected Endpoint After Logout
```javascript
// Logout (clears token)
window.apiClient.logout();

// Try to access protected endpoint
const result = await window.apiClient.apiGet('/patients/dashboard');
// Should redirect to login
```

## Production Deployment

Before deploying to production:

1. Update API base URL in api-client.js:
```javascript
const API_BASE_URL = 'https://your-production-api.com/api';
```

2. Test all authentication flows in production environment

3. Verify CORS settings if frontend and backend are on different domains

4. Test JWT token expiration and refresh (if implemented in backend)

## Troubleshooting

### "Cannot access protected endpoints"
- Check that token is stored in localStorage
- Check that token is included in request headers (should be automatic)
- Check backend is returning 401 for expired tokens

### "Login page not working"
- Verify /api/auth/login endpoint returns: `{ token: "...", user: {...} }`
- Check that response includes userRole field

### "Logout not working"
- Check that redirect to login page is not blocked
- Verify localStorage is being cleared (check DevTools > Application > Storage)

### "Role-based access not working"
- Verify backend returns correct userRole in login response
- Check that role values match exactly (case-sensitive)

### "CORS errors"
- Backend must have CORS enabled for frontend domain
- Check 'Access-Control-Allow-Origin' header in response

## Need More Help?

- See JWT_IMPLEMENTATION_SUMMARY.md for detailed overview
- Check api-client.js comments for method documentation
- Review login.html for example implementation
- Check browser console (F12) for error messages
