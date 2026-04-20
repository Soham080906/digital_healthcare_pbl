# Digital Healthcare PBL - Full Project README

This README is a single-file technical reference for the full project (Spring Boot backend + static frontend), including features, architecture, API map, and function-by-function implementation notes.

## 1) Project Overview

Digital Healthcare is a role-based appointment booking platform:

- Patients can register/login, browse clinics/doctors, book slots, and cancel appointments.
- Doctors can view appointments, approve/reject, and mark as completed.
- JWT authentication is used for protected endpoints.
- PostgreSQL stores users, doctors, clinics, and appointments.

## 2) Tech Stack

- Backend: Spring Boot, Spring Security, Spring Data JPA, JWT (jjwt), PostgreSQL
- Frontend: HTML, CSS, vanilla JavaScript (no framework)
- Auth: JWT Bearer token

## 3) Runtime Configuration

Backend config file: `backend/digital_healthcare/src/main/resources/application.properties`

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.jpa.hibernate.ddl-auto=update`
- `jwt.secret`
- `jwt.expiration`

## 4) Backend Architecture

Backend base package: `com.pbl.digital_healthcare`

- `controller/`: REST endpoints
- `service/`: business logic
- `repository/`: DB access
- `models/`: entities/enums
- `security/`: JWT utilities + auth filter
- `config/`: security, beans, exception handler, data init

---

## 5) Backend API Endpoints

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Clinics
- `GET /api/clinics`
- `GET /api/clinics/manage` (DOCTOR/ADMIN)

### Doctors
- `GET /api/doctors`
- `GET /api/doctors/profile` (DOCTOR)
- `GET /api/doctors/user/{userId}` (DOCTOR)
- `PUT /api/doctors/{userId}` (DOCTOR)

### Patients
- `GET /api/patients/profile` (PATIENT)
- `GET /api/patients/dashboard` (PATIENT)

### Appointments
- `POST /api/appointments` (PATIENT)
- `GET /api/appointments/booked-slots/{doctorId}/{date}`
- `GET /api/appointments/my-appointments` (PATIENT)
- `DELETE /api/appointments/{appointmentId}` (PATIENT)
- `GET /api/appointments/doctor` (DOCTOR)
- `GET /api/appointments/doctor/{doctorId}` (DOCTOR)
- `GET /api/appointments/doctor/user/{userId}` (DOCTOR)
- `POST /api/appointments/{appointmentId}/status` (DOCTOR/ADMIN)
- `DELETE /api/appointments/{appointmentId}/cancel` (PATIENT/DOCTOR/ADMIN)

### Utility / Debug
- `GET /api/specializations`
- `GET /api/debug/users`
- `GET /api/debug/test`
- `GET /api/test`
- `GET /api/test/appointments/doctor/{doctorId}`

---

## 6) Backend Function-by-Function Implementation

### `DigitalHealthcareApplication.java`
- `main(String[] args)`  
  Starts the Spring Boot application.

### `config/AppConfig.java`
- `passwordEncoder()`  
  Registers `BCryptPasswordEncoder` bean for hashing passwords.

### `config/SecurityConfig.java`
- `authenticationProvider()`  
  Creates `DaoAuthenticationProvider` and attaches `CustomUserDetailsService` + `PasswordEncoder`.
- `authenticationManager(AuthenticationConfiguration config)`  
  Exposes framework `AuthenticationManager`.
- `securityFilterChain(HttpSecurity http)`  
  Configures stateless JWT security, CORS, route authorization, and inserts JWT filter.
- `corsConfigurationSource()`  
  Sets CORS origin/method/header policy and registers it for all routes.

### `config/GlobalExceptionHandler.java`
- `handleRuntimeException(RuntimeException e)`  
  Returns `400 Bad Request` with `MessageResponse`.
- `handleGenericException(Exception e)`  
  Returns `500 Internal Server Error` with generic message payload.

### `config/DataInitializer.java`
- `run(String... args)`  
  Startup initializer: fixes appointment status DB constraint and seeds clinics/doctors if DB is empty.
- `ensureAppointmentStatusConstraint()`  
  Drops/recreates `appointments_status_check` with case-insensitive valid statuses (`pending`, `confirmed`, `completed`, `cancelled`).
- `initializeClinics()`  
  Inserts default clinic rows.
- `initializeDoctors()`  
  Inserts sample doctor users and linked doctor profiles (with clinic relation).

### `security/JwtUtil.java`
- `getSigningKey()`  
  Builds HMAC signing key from configured secret.
- `extractUsername(String token)`  
  Extracts JWT subject (email).
- `extractExpiration(String token)`  
  Returns token expiration date.
- `extractClaim(String token, Function<Claims, T> claimsResolver)`  
  Generic claim extractor.
- `extractAllClaims(String token)`  
  Parses and validates signed token, returns payload claims.
- `isTokenExpired(String token)`  
  Checks expiration.
- `generateToken(UserDetails userDetails)`  
  Creates token with username subject.
- `generateToken(String username, String role)`  
  Creates token including `role` claim.
- `createToken(Map<String, Object> claims, String subject)`  
  Internal builder for token content + issue/expiry timestamps.
- `validateToken(String token, UserDetails userDetails)`  
  Validates token subject against user and checks expiry.
- `validateToken(String token)`  
  Validates structural correctness and expiry.
- `extractRole(String token)`  
  Reads `role` claim from JWT.

### `security/JwtAuthenticationFilter.java`
- `doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)`  
  Extracts bearer token, resolves username, validates token, and sets authenticated principal in Spring Security context.

### `service/CustomUserDetailsService.java`
- `loadUserByUsername(String email)`  
  Loads user by email and maps to Spring Security `UserDetails` with `ROLE_*` authority.

### `service/AuthService.java`
- `register(RegisterRequest request)`  
  Validates unique email, creates user, and if role is doctor creates associated doctor profile linked to selected clinic.
- `login(LoginRequest request)`  
  Validates credentials, builds `UserResponse`, and returns JWT in `AuthResponse`.

### `service/DoctorService.java`
- `getAllDoctors()`  
  Returns all doctor entities from repository.

### `service/AppointmentService.java`
- `bookAppointment(AppointmentRequest request, String userEmail)`  
  Validates request, resolves patient/doctor/clinic, parses slot in multiple date-time formats, rejects past/duplicate slots, saves appointment with `PENDING`.
- `getBookedSlots(Long doctorId, String date)`  
  Loads same-day appointments and returns booked time strings (`HH:mm`) for `CONFIRMED` + `PENDING`.
- `getUserAppointments(String userEmail)`  
  Finds patient by email and returns mapped appointment DTO list.
- `cancelAppointment(Long appointmentId, String userEmail)`  
  Validates ownership and future-time condition, then sets status to `CANCELLED`.
- `convertToResponse(Appointment appointment)`  
  Maps entity graph into nested `AppointmentResponse` DTO.
- `getDoctorAppointments(Long doctorId, String status)`  
  Returns doctor appointments with optional status filtering (`all`, `active`, specific status).
- `getDoctorAppointmentsByUserId(Long userId, String status)`  
  Resolves doctor via user id and applies same status filtering.
- `getDoctorAppointmentsByEmail(String userEmail, String status)`  
  Resolves doctor via authenticated email and returns filtered list.
- `updateAppointmentStatus(Long appointmentId, String status)`  
  Converts status string to enum, updates appointment status, and stamps `updatedAt`.

### `controller/AuthController.java`
- `register(RegisterRequest request)`  
  Pass-through endpoint to `AuthService.register`.
- `login(LoginRequest request)`  
  Pass-through endpoint to `AuthService.login`.

### `controller/ClinicController.java`
- `getAllClinics()`  
  Returns all clinics mapped to `ClinicResponse`.
- `getClinicManagementAccess()`  
  Protected endpoint that returns authenticated user context for management access.
- `convertToResponse(Clinic clinic)`  
  Maps clinic entity to API DTO.

### `controller/DoctorController.java`
- `getAllDoctors()`  
  Returns doctor catalog for public listing.
- `getDoctorProfile()`  
  Returns profile for authenticated doctor.
- `getDoctorByUserId(Long userId)`  
  Returns doctor profile by linked user id.
- `updateDoctorProfile(Long userId, DoctorUpdateRequest updateRequest)`  
  Applies partial updates to user and doctor fields, then saves both.
- `convertToResponse(Doctor doctor)`  
  Converts doctor entity (including clinic + user nested objects) to `DoctorResponse`.

### `controller/PatientController.java`
- `getPatientProfile()`  
  Returns authenticated patient identity details.
- `getPatientDashboard()`  
  Returns simple dashboard metadata for patient role.

### `controller/SpecializationController.java`
- `getSpecializations()`  
  Returns predefined specialization list.

### `controller/AppointmentController.java`
- `bookAppointment(AppointmentRequest request)`  
  Gets authenticated email, delegates booking, and wraps errors in API response.
- `getBookedSlots(Long doctorId, String date)`  
  Returns reserved slots for UI calendar/time picker.
- `getUserAppointments()`  
  Returns current patient’s appointments.
- `cancelAppointment(Long appointmentId)`  
  Cancels an appointment for authenticated patient.
- `getDoctorAppointments(String status)`  
  Returns appointments for authenticated doctor with optional status filter.
- `getDoctorAppointmentsById(Long doctorId, String status)`  
  Returns appointments for explicit doctor id.
- `getDoctorAppointmentsByUserId(Long userId, String status)`  
  Returns doctor appointments resolved from user id.
- `updateAppointmentStatus(Long appointmentId, Map<String, String> statusUpdate)`  
  Normalizes incoming status, supports alias `complete -> completed`, validates allowed set, and updates status.
- `cancelAppointmentByRole(Long appointmentId)`  
  Cancel endpoint accessible by patient/doctor/admin roles.

### `controller/DebugController.java`
- `getAllUsers()`  
  Returns all users and prints debug info to logs.
- `testConnection()`  
  Verifies DB access and returns user count or error text.

### `controller/TestController.java`
- `test()`  
  Health text endpoint.
- `getAppointmentsForDoctor(Long doctorId)`  
  Debug dump of appointments by doctor id with slot/status lines.

---

## 7) Core Backend Data Models

- `User`: name, email, password, role
- `Doctor`: user relation, specialization, license, experience, education, phone, clinic
- `Clinic`: name, location, contact, hours, services
- `Appointment`: patient, doctor, clinic, slot (`LocalDateTime`), status (`PENDING/CONFIRMED/COMPLETED/CANCELLED`), notes, updatedAt

---

## 8) Frontend Architecture

Frontend root: `frontend/digital_healthcare_pbl/client`

- Public pages: `index.html`, `doctors.html`, `clinics.html`
- Auth pages: `login.html`, `register.html`, `forgot-password.html`
- Patient pages: `patient-dashboard.html`, `appointments.html`
- Doctor pages: `doctor-dashboard.html`, `dashboard.html` (combined/legacy dashboard logic)
- Shared JS: `script.js`, `api-client.js`

---

## 9) Frontend Function-by-Function Implementation

> Note: Most page scripts are inline inside each HTML file.

### `client/api-client.js`
- `getToken()` - Reads JWT from `localStorage`.
- `getAuthHeader()` - Returns bearer header object when token exists.
- `handleResponseError(status, errorData)` - Centralized error behavior for 401/403/404/5xx and backend message extraction.
- `apiRequest(endpoint, options)` - Generic authenticated fetch wrapper with JSON parse and error handling.
- `apiGet(endpoint)` - GET helper.
- `apiPost(endpoint, data)` - POST helper.
- `apiPut(endpoint, data)` - PUT helper.
- `apiPatch(endpoint, data)` - PATCH helper.
- `apiDelete(endpoint)` - DELETE helper.
- `isAuthenticated()` - Token presence check.
- `getCurrentUser()` - Returns current user object from storage.
- `hasRole(requiredRole)` - Role check utility.
- `hasAnyRole(requiredRoles)` - Multi-role check utility.
- `logout()` - Clears auth/profile storage and redirects to login.
- `requireAuth()` - Redirect guard when not authenticated.
- `requireRole(requiredRole)` - Role-based page access guard.

### `client/script.js`
- `addLoadingState(button)` - Disables button + shows loading text and returns restore callback.
- `showToast(message, type)` - Creates/removes temporary toast notifications.

### `client/index.html`
- `updateLoginStatus()` - Adjusts nav/buttons based on login state and role.
- `showToast(message, type)` - Local toast helper for page-level feedback.

### `client/login.html` (inline handlers)
- Form submit handler authenticates via `/api/auth/login`, stores token/user info, redirects by role.
- Logout/redirect helpers managed through shared scripts.

### `client/register.html`
- `loadClinics()` - Loads clinic options for doctor registration.
- `loadSpecializations()` - Loads specialization options from backend.
- `showToast(message, type)` - Displays registration feedback.

### `client/forgot-password.html`
- `showToast(message, type)` - UI feedback helper for reset flow placeholder.

### `client/doctors.html`
- `loadDoctors()` - Fetches doctors and initializes listing.
- `populateFilters()` - Builds specialization/clinic filter options.
- `displayDoctors(doctors)` - Renders doctor cards with profile details.
- `hideLoading()` - Hides loading skeleton/spinner state.
- `filterDoctors()` - Applies client-side filter criteria to doctor list.

### `client/clinics.html`
- `loadClinics()` - Fetches clinics and doctor data, computes doctor counts by clinic.
- `getClinicIdFromObj(obj)` - Helper to normalize clinic id extraction.
- `getClinicIdFromDoctor(doctor)` - Helper to derive clinic id from doctor object shape.
- `displayClinics(clinics)` - Renders clinics grid/cards.
- `hideLoading()` - Removes loading state.
- `filterClinics()` - Filters clinic cards by search criteria.
- `viewClinicDetails(clinicId)` - Opens clinic detail modal/panel for selected clinic.

### `client/appointments.html` (patient booking page)
- `checkAuthForBooking()` - Ensures user is authenticated and role is PATIENT.
- `redirectToLogin()` - Redirects to login with return URL.
- `setMinDate()` - Sets booking date minimum to tomorrow and enforces future-only selection.
- `loadBookingData()` - Loads clinics/doctors and initializes selectors (with mock fallback).
- `getMockClinics()` - Returns fallback clinic mock data.
- `getMockDoctors()` - Returns fallback doctor mock data.
- `loadMockData()` - Loads full mock state if API fetch fails.
- `populateClinicSelect(clinics)` - Fills clinic dropdown and wires clinic->doctor filtering.
- `populateDoctorSelect(doctors)` - Fills doctor dropdown and triggers slot fetch.
- `loadAvailableSlots()` - Calls backend booked-slots API and computes available/booked slot objects.
- `generateAllTimeSlots()` - Creates day slot set at 30-minute intervals.
- `generateMockAvailability()` - Generates randomized availability fallback.
- `displayTimeSlots(slots)` - Renders slot cards with available/booked UI state.
- `selectTimeSlot(time)` - Marks selected slot and writes hidden `time` input.
- `generateTimeSlots()` - Legacy fallback slot generator.
- Booking button click handler validates fields/date and posts appointment payload (`clinic`, `doctor`, `slot`, `notes`).
- `checkSlotsRefresh()` - Refreshes slots if cancellation flag is set in storage.
- `forceRefreshSlots()` - Periodic slot refresh when page is visible.
- `addRefreshButton()` - Creates manual refresh action.
- `refreshTimeSlots()` - Manual refresh with spinner/feedback state.

### `client/patient-dashboard.html`
- `loadDashboardData()` - Loads patient appointments, doctors, clinics, and notifications.
- `loadAppointmentsList(appointments)` - Renders patient appointment cards with status actions.
- `populateDoctorsGrid(doctors)` - Renders doctor summary cards.
- `populateClinicsGrid(clinics)` - Renders clinic summary cards.
- `loadNotifications()` - Shows upcoming appointment notifications.
- `cancelAppointment(e)` - Calls appointment cancel endpoint and refreshes state.
- Notification toggle handlers manage bell panel visibility.

### `client/doctor-dashboard.html`
- `fetchDoctorProfileAndLoad()` - Fetches and caches doctor profile for UI.
- `loadDoctorProfile()` - Reads profile from storage and renders doctor section.
- `loadDashboardData()` - Loads doctor appointments and clinics, with mock fallback.
- `generateMockDoctorAppointments()` - Returns demo appointments.
- `displayDoctorPatientAppointments(appointments)` - Renders appointment cards and action buttons by status.
- `addAppointmentActionListeners()` - Binds approve/reject/complete button handlers.
- `approveAppointment(e)` - Sets appointment status to `confirmed`.
- `rejectAppointment(e)` - Sets appointment status to `cancelled`.
- `completeAppointment(e)` - Sets appointment status to `completed` and handles backend error messages.
- `getStatusConfig(status)` - Maps status to label/icon for UI badge.
- `populateClinicsGrid(clinics)` - Renders clinics list on doctor page.
- `populateEditForm()` - Loads doctor profile/specialization/clinic values into edit modal.
- Edit profile submit handler updates doctor profile via API.

### `client/dashboard.html` (combined dashboard script)
- `loadUserRole()` - Detects role and toggles role-specific sections.
- `fetchDoctorProfileAndLoad()` - Fetches doctor profile for dashboard role flow.
- `loadDoctorProfile()` - Renders doctor profile details.
- `loadFallbackDoctorProfile()` - Provides fallback profile values.
- `loadDashboardData()` - Loads role-appropriate summary data.
- `loadDoctorPatientAppointments(statusFilter)` - Fetches doctor appointments with filter.
- `generateMockDoctorAppointments()` - Mock fallback records.
- `displayDoctorPatientAppointments(appointments)` - Renders doctor appointment list/actions.
- `applyAppointmentFilter()` - Applies active/cancelled/all filter on cards.
- `getStatusConfig(status)` - Badge/icon config helper.
- `populateDoctorsGrid(doctors)` - Renders doctor cards.
- `populateClinicsGrid(clinics)` - Renders clinic cards.
- `loadAppointmentsList(appointments)` - Renders patient-side appointment cards.
- `showToast(message, type)` - Local page toast helper.
- `populateEditForm()` - Pre-fills doctor edit modal.
- `loadSpecializations()` - Fetches specialization options.
- `loadClinicsForEdit()` - Fetches clinic options for doctor edit form.
- `cancelAppointment(event)` - Cancels appointment and refreshes list.
- `updateAppointmentStatus(event)` - Generic status update caller for doctor actions.
- `viewPatientDetails(event)` - Opens patient details modal/panel.
- `rescheduleAppointment(event)` - Placeholder reschedule action.
- `sendReminder(email, date, time)` - Reminder action placeholder.
- `downloadPatientReport(patientName, appointmentId)` - Report-download action placeholder.
- `loadNotifications()` - Loads appointments and builds upcoming notification dataset.
- `getUpcomingAppointments(appointments)` - Filters to next-window upcoming items.
- `displayNotifications(upcomingAppointments)` - Renders notification panel list.
- `getTimeUntil(appointmentTime)` - Relative time formatter for alerts.
- `toggleNotificationPanel()` - Opens/closes notification pane.

---

## 10) Current Business Rules Implemented

- Appointment slot must be in the future.
- Duplicate doctor-slot bookings are blocked.
- Booked slot API treats `PENDING` and `CONFIRMED` as occupied.
- Status workflow supports: `pending`, `confirmed`, `completed`, `cancelled`.
- Status update endpoint accepts alias `complete` and normalizes to `completed`.
- Database status check constraint is auto-corrected on startup by `DataInitializer`.

---

## 11) Local Run Instructions

### Backend
1. Create PostgreSQL DB: `digital_healthcare_pbl`
2. Configure `application.properties` DB/JWT values
3. Run backend:
   - `mvn spring-boot:run` (or use IDE run)
4. API starts at `http://localhost:8080`

### Frontend
1. Open `frontend/digital_healthcare_pbl/client/index.html` in browser (or serve with any static server).
2. Ensure backend is running at `http://localhost:8080`.

---

## 12) Deployment Notes

- Deploy backend and frontend separately.
- Replace hardcoded API base URL in frontend (`api-client.js`) with deployed backend URL.
- Move DB credentials and JWT secret to environment variables.
- Keep CORS restricted to actual frontend domains in production.

