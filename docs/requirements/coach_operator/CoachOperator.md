### **User Stories for Coach Operator**

### 🧩 **1. Login & Authentication**

- **User Story:**
  As a **Coach Operator**,
  I want to **log into the system securely**,
  So that **I can access the management dashboard and perform operational tasks**.

- **Acceptance Criteria:**

  - [ ] The login interface must have "Email" and "Password" fields.
  - [ ] The system must validate email format when user enters it.
  - [ ] When login is successful, the system must redirect the coach operator to the main dashboard.
  - [ ] When login fails (incorrect email or password), the system must display a generic error message: "Email or password is incorrect."
  - [ ] There must be a "Forgot password?" link that leads to the password reset request page.

- **Priority Level:** High
- **Story Points:** 2

---

### 🧩 **2. Company Profile & Contract Management**

- **User Story:**
  As a **Coach Operator**,
  I want to **manage my company profile and contract**,
  So that **my information is always accurate and the relationship with the platform is transparent**.

- **Acceptance Criteria:**

  - [ ] In the admin panel, there must be a "Coach Operator Info" or "Company Profile" section.
  - [ ] I can view and edit information: Company name, Address, Hotline, Contact email, Brief description.
  - [ ] The "Save changes" button should only be enabled when there are information changes.
  - [ ] I can view current contract status (e.g., Active, Pending approval, Terminated).
  - [ ] There must be a function to request contract termination, which requires a confirmation step (e.g., modal pop-up) before sending.

- **Priority Level:** High
- **Story Points:** 3

---

### 🧩 **3. Driver Management**

- **User Story:**
  As a **Coach Operator**,
  I want to **comprehensively manage my driver list (add, view, edit, change status)**,
  So that **I can maintain accurate driver records and assign them to trips**.

- **Acceptance Criteria:**

  - [ ] There is a "Driver Management" page displaying all drivers with columns: Full name, Phone number, Status (Active/Inactive).
  - [ ] There is an "Add new driver" button that opens a form for entering: Full name, email, phone number, driver's license number.
  - [ ] The system must check that phone number and email are not duplicated with other drivers in the same coach company.
  - [ ] I can click on a driver to view/edit their detailed information.
  - [ ] I can change driver status from "Active" to "Inactive" and vice versa. "Inactive" drivers will not appear in the selection list when creating new trips.

- **Priority Level:** High
- **Story Points:** 5 (For complete CRUD functionality - Create, Read, Update, Delete)

---

### 🧩 **4. Vehicle Management**

- **User Story:**
  As a **Coach Operator**,
  I want to **manage my vehicle fleet efficiently**,
  So that **vehicle readiness status is accurate for trip scheduling**.

- **Acceptance Criteria:**

  - [ ] The "Vehicle Management" page displays vehicle list with columns: License plate, Vehicle type, Total seats, Status.
  - [ ] There is an "Add new vehicle" button that opens a form for entering: License plate, Manufacturer, Model, Total seats, Seat layout selection, Amenities (wifi, drinks, etc.).
  - [ ] License plate must be unique system-wide and validated.
  - [ ] I can edit information of an existing vehicle.
  - [ ] I can change vehicle status: "Active", "Under maintenance", "Inactive". Vehicles not in "Active" status cannot be selected for creating trips.
  - [ ] There is a "Delete vehicle" function, but only allows deletion of vehicles that have never performed any trips.

- **Priority Level:** High
- **Story Points:** 5

---

### 🧩 **5. Route Management**

- **User Story:**
  As a **Coach Operator**,
  I want to **create and manage my service routes**,
  So that **I can establish itineraries and stops for the trips I provide**.

- **Acceptance Criteria:**

  - [ ] The "Route Management" page allows me to view all created routes.
  - [ ] When creating a new route, I must select Departure point and Destination from available location list.
  - [ ] I can add stops (pickup/drop-off stations) to the route in correct order.
  - [ ] I must enter default travel time (in minutes) and default ticket price for the route.
  - [ ] I can edit (add/remove stops, change price, time) or delete a route.
  - [ ] The system does not allow deleting routes if they are being used by upcoming trips.

- **Priority Level:** High
- **Story Points:** 8 (More complex due to managing ordered stops and related logic)

---

### 🧩 **6. Trip Schedule Management**

- **User Story:**
  As a **Coach Operator**,
  I want to **create and manage trip schedules**,
  So that **customers can search and book tickets for these trips**.

- **Acceptance Criteria:**

  - [ ] On the "Trip Management" page, I can view trips with filters (date, route, status).
  - [ ] The "Create new trip" button opens a form requiring: select Route, select Vehicle, select Driver, enter Departure date/time, ticket price (defaults from route but can be overridden).
  - [ ] The "select Vehicle" and "select Driver" lists only show active vehicles/drivers that are not conflicted at that time.
  - [ ] Expected arrival time is automatically calculated by the system.
  - [ ] I can update trip information (e.g., change vehicle/driver for emergency reasons) before departure time.
  - [ ] I can "Cancel trip". When canceling, the system must have a mechanism to notify passengers who have booked tickets.

- **Priority Level:** High
- **Story Points:** 8

---

### 🧩 **7. Ticket Management**

- **User Story:**
  As a **Coach Operator**,
  I want to **view detailed information about tickets and bookings**,
  So that **I can track sales, manage passenger lists and seat status**.

- **Acceptance Criteria:**

  - [ ] From the trip list, I can click on a trip to view details.
  - [ ] The trip detail page displays list of passengers who booked tickets (Name, Phone, seat number, payment status).
  - [ ] There must be a visual seat map of the vehicle, clearly showing seats: Available, Booked, On hold.
  - [ ] I can export the passenger list of a trip to file (PDF/Excel) to give to the driver.

- **Priority Level:** Medium
- **Story Points:** 5 (Visual seat map increases complexity)

---

### 🧩 **8. Business Analytics Tracking**

- **User Story:**
  As a **Coach Operator**,
  I want to **view reports and statistics about business performance**,
  So that **I can track revenue and make strategic decisions**.

- **Acceptance Criteria:**

  - [ ] There is a "Reports & Analytics" page (Dashboard).
  - [ ] Display overview metrics: Total revenue, Total tickets sold, Average occupancy rate.
  - [ ] I can filter reports by time period (Today, Last 7 days, This month, Custom).
  - [ ] There must be charts showing revenue over time (e.g., line chart).
  - [ ] There must be a performance statistics table for each route (which route has highest/lowest revenue).
  - [ ] There is a function to export report data to CSV file.

- **Priority Level:** Medium
- **Story Points:** 8 (Requires complex data processing in backend and chart display in frontend)

---

### 🧩 **9. Staff Management & Authorization**

- **User Story:**
  As a **Coach Operator Manager** (owner),
  I want to **create and manage accounts for my staff (e.g., ticket sales staff, operations staff)**,
  So that **I can assign work and control their access to system functions securely**.

- **Acceptance Criteria:**

  - [ ] In the admin panel, there must be a "Staff Management" section.
  - [ ] I can view list of all current staff with information: Name, Email, Role, Status.
  - [ ] When creating new staff account, I must enter basic information (Full name, Email, Temporary password) and **select role** from a predefined list.
  - [ ] The system must have at least the following roles:
    - `Manager`: Has all permissions like the owner.
    - `Operations Staff`: Can create/manage trips, view ticket lists, but cannot view revenue reports.
    - `Ticket Sales / Customer Service Staff`: Only has access to functions related to booking and processing tickets.
  - [ ] I can change role or deactivate an employee's account when they leave.
  - [ ] Deactivated staff cannot log into the system.
  - [ ] The system must strictly enforce access restrictions. For example: accounts with `Ticket Sales Staff` role will not see the "Reports & Analytics" menu item.

- **Priority Level:** High
- **Story Points:** 8 (This is a high-complexity feature as it involves not just simple CRUD but also building and implementing a Role-Based Access Control (RBAC) system across the entire application).
