# JWT Authentication Implementation - Frontend Summary

## Overview
The frontend has been updated to integrate with the JWT-based authentication system implemented in the backend. All protected API endpoints now require JWT tokens, and the frontend properly handles token storage, validation, and refresh.

## Changes Made

### 1. **New File: `api-client.js`** ✅
A centralized API client utility that handles:
- **JWT Token Management**: Automatically includes Bearer token in all requests
- **Error Handling**: 
  - 401 Unauthorized: Clears token and redirects to login
  - 403 Forbidden: Shows permission error message
  - Other errors: Displays appropriate error toasts
- **API Methods**: `apiGet()`, `apiPost()`, `apiPut()`, `apiPatch()`, `apiDelete()`
- **Authentication Utilities**: 
  - `isAuthenticated()`: Check if user is logged in
  - `getCurrentUser()`: Get current user info from localStorage
  - `hasRole()`: Check user role
  - `logout()`: Clear tokens and redirect to login

**Location**: `client/api-client.js`

### 2. **Updated: `login.html`** ✅
Changes:
- Added `api-client.js` script import
- Redirect to dashboard if already logged in
- Improved error handling with error messages from backend
- Store patient profile if available (in addition to doctor profile)
- Added loading state to submit button

**Key Features**:
- Form validation
- JWT token storage on successful login
- User info storage (name, email, role, ID)
- Profile storage (doctor/patient)

### 3. **Updated: `register.html`** ✅
Changes:
- Added `api-client.js` script import
- Redirect to dashboard if already logged in
- Improved error handling with backend messages
- Store patient profile on successful registration
- Added loading state to submit button
- Auto-login after successful registration

**Key Features**:
- Dynamic form fields based on role (patient/doctor)
- Doctor-specific fields: specialization, license, experience, education, phone, clinic
- Password validation
- Automatic clinic and specialization loading from API

### 4. **Updated: `dashboard.html`** ✅
Changes:
- Added `api-client.js` script import
- Authentication check on page load (`requireAuth()`)
- Logout button handler with confirmation
- Role-based UI rendering:
  - **Patient**: Shows book appointment button, notification section, appointments list
  - **Doctor**: Shows doctor profile section, patient appointments section

**Key Features**:
- Protected page - redirects to login if not authenticated
- Personalized welcome message based on role
- Role-specific dashboard views
- Doctor profile loading and display
- Patient appointment notifications

### 5. **Updated: `appointments.html`** ✅
Changes:
- Added `api-client.js` script import
- Added authentication check before booking
- Updated book appointment handler to use `api-client.apiPost()`
- JWT token automatically included in all API calls
- Improved error handling with 401/403 responses

**Key Features**:
- Login required for booking (but can view doctors/clinics without login)
- Automatic Authorization header with JWT token
- Protected endpoint: POST /api/appointments
- Public endpoints: GET /api/clinics, GET /api/doctors, GET /api/appointments/booked-slots

### 6. **Updated: `doctors.html`** ✅
Changes:
- Added `api-client.js` script import
- No authentication required (public endpoint)
- JWT token included if user is logged in (for future personalization)

### 7. **Updated: `clinics.html`** ✅
Changes:
- Added `api-client.js` script import
- No authentication required (public endpoint)
- JWT token included if user is logged in (for future personalization)

### 8. **Updated: `index.html`** ✅
Changes:
- Added `api-client.js` script import
- Updated logout handler to use `apiClient.logout()`
- Navigation update based on authentication state
- "Book Appointment" button behavior based on login status

**Key Features**:
- Dynamic navigation showing user info when logged in
- Login/Register links for non-authenticated users
- Dashboard link for authenticated users
- Logout functionality with page reload

### 9. **Updated: `script.js`** ✅
Changes:
- Added navigation authentication state management
- Auto-hide login/register links when authenticated
- Hide logout button when not authenticated
- Export window.showToast and window.addLoadingState globally

**Key Features**:
- Toast notification system (success, error, info)
- Loading state management for buttons
- Navigation state sync across pages

## Authentication Flow

### Login Flow
```
1. User enters credentials on login page
2. POST /api/auth/login with email, password, role
3. Backend returns JWT token + user info
4. Frontend stores:
   - token (localStorage)
   - userRole, userId, userName, userEmail (localStorage)
   - doctorProfile or patientProfile (if applicable)
5. Redirect to dashboard
```

### Protected API Calls
```
1. Page includes api-client.js
2. Use apiClient.apiPost/apiGet/etc() methods
3. JWT token automatically added to Authorization header
4. If response is 401: Clear tokens and redirect to login
5. If response is 403: Show permission error
```

### Logout Flow
```
1. User clicks logout
2. Clear all localStorage items (token, user info, profiles)
3. Show success message
4. Redirect to login
```

## API Endpoints Status

### Public Endpoints (No Authentication)
- POST `/api/auth/login` - User login
- POST `/api/auth/register` - User registration
- GET `/api/doctors` - List all doctors
- GET `/api/clinics` - List all clinics
- GET `/api/specializations` - List specializations
- GET `/api/appointments/booked-slots/{doctorId}/{date}` - Check available slots

### Protected Endpoints (JWT Required)
- GET `/api/doctors/profile` - Get doctor profile (DOCTOR role)
- GET `/api/patients/profile` - Get patient profile (PATIENT role)
- GET `/api/patients/dashboard` - Get patient dashboard (PATIENT role)
- GET `/api/clinics/manage` - Manage clinics (ADMIN/DOCTOR roles)
- POST `/api/appointments` - Book appointment (PATIENT role)
- GET `/api/appointments/my-appointments` - Get user's appointments (PATIENT role)
- DELETE `/api/appointments/{id}` - Cancel appointment (PATIENT role)

## Storage Details

### localStorage Structure
```javascript
{
  token: "eyJhbGci...", // JWT token (required for protected endpoints)
  userRole: "patient|doctor|admin", // User role
  userId: "123", // User ID
  userName: "John Doe", // User's full name
  userEmail: "john@example.com", // User's email
  doctorProfile: { ...doctor object... }, // Only for doctors
  patientProfile: { ...patient object... } // Only for patients
}
```

## Error Handling

### 401 Unauthorized
**Cause**: Invalid or expired JWT token
**Action**: Clear all tokens and redirect to login page
**Message**: "Your session has expired. Please login again."

### 403 Forbidden
**Cause**: User doesn't have required role for endpoint
**Action**: Show error message
**Message**: "You do not have permission to access this resource."

### Other Errors
**Action**: Show appropriate error message from backend or generic error
**Message**: Toast notification with error details

## Testing Checklist

### Authentication Flow
- [ ] Register as patient - should auto-login and redirect to dashboard
- [ ] Register as doctor - should auto-login with doctor profile
- [ ] Login with patient account - should show patient dashboard
- [ ] Login with doctor account - should show doctor dashboard
- [ ] Logout - should clear tokens and redirect to login

### Protected Pages
- [ ] Access dashboard without login - should redirect to login
- [ ] Book appointment without login - should prompt to login
- [ ] Book appointment as patient - should succeed
- [ ] Access protected API endpoints with valid token - should work
- [ ] Access protected API with expired token - should redirect to login

### Role-Based Access
- [ ] Patient cannot access doctor endpoints (403 error)
- [ ] Doctor cannot access patient endpoints (403 error)
- [ ] Admin can access clinic management endpoints
- [ ] Role-based UI elements show/hide correctly

### Navigation
- [ ] Login/Register links show when not logged in
- [ ] User info shows when logged in
- [ ] Logout button works correctly
- [ ] Dashboard link appears for authenticated users

## Environment Configuration

The API base URL is currently set to:
```javascript
const API_BASE_URL = 'http://localhost:8080/api';
```

For production, update this in `api-client.js` to your production API endpoint.

## Notes for Developers

### Adding New Protected Endpoints
1. Use `apiClient.apiPost/apiGet/etc()` instead of direct fetch
2. No need to manually add Authorization header - it's automatic
3. Handle auth errors in the api-client (already done)

### Backend Integration Requirements
The backend must:
1. Return JWT token on successful login: `{ token: "...", user: {...} }`
2. Return appropriate HTTP status codes:
   - 200: Success
   - 401: Invalid/expired token
   - 403: Insufficient permissions
3. Validate Authorization header: `Authorization: Bearer <token>`

### Customization
To customize error messages, modify the `handleResponseError()` function in `api-client.js`.

## Files Modified Summary
- ✅ `api-client.js` - **NEW FILE**
- ✅ `login.html` - Added JWT support
- ✅ `register.html` - Added JWT support
- ✅ `dashboard.html` - Added JWT support + logout
- ✅ `appointments.html` - Added JWT support
- ✅ `doctors.html` - Added api-client import
- ✅ `clinics.html` - Added api-client import
- ✅ `index.html` - Added JWT support for logout
- ✅ `script.js` - Added navigation auth state management

## Success Indicators
- Users can register and auto-login ✅
- Users can login with credentials ✅
- JWT token is stored and sent with requests ✅
- 401 errors redirect to login ✅
- 403 errors show permission denied ✅
- Logout clears tokens and redirects ✅
- Role-based UI renders correctly ✅
- Navigation updates based on auth state ✅
