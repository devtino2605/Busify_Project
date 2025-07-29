### User Story #1: Ticket Confirmation

**User Story:**  
_As a ticket holder, I want to confirm my ticket so that I can ensure my booking is valid and secure my seat or access._

**Acceptance Criteria:**

- The user can enter a ticket code, scan a QR code, or input phone number to confirm  
- The system checks the validity of the ticket (exists and has not been confirmed previously)  
- If the ticket is invalid or already confirmed, show a clear error message  
- If the ticket is valid, display ticket details (event name, time, location) and confirmation status  
- Send a confirmation email to the user (if the ticket is linked to an email)  
- Log the confirmation time and the confirming user (if logged in) 

**Priority:** High

**Story Points:** 5

**UI Design:** _[Figma link] / [Attached image]_
-----
### User Story #2: Bus Staff Views Trip Schedule

**User Story:**  
_As a bus company staff member, I want to view the detailed schedule of each trip so that I can monitor its progress, support coordination, and ensure everything runs smoothly._

**Acceptance Criteria:**

- Staff can search and select a bus by license plate, trip code, or route
- Display general trip information:
  - Route (start point → end point)
  - Departure time and estimated arrival time
  - List of stops with estimated arrival/departure times
  - Current trip status (Waiting / In progress / Completed)
- If the trip is ongoing, display current bus location (via GPS if available)
- Show driver information, phone number, and number of booked passengers
- Allow filtering by date, route, and status to quickly find trips
- View-only access to schedule if not a manager

**Priority:** Medium

**Story Points:** 4

**UI Design:** _[Figma link] / [Attached image]_
----
### User Story #3: View Passenger List

**User Story:**  
_As a bus company staff member, I want to view the passenger list for each trip so that I can verify bookings, assist with check-in, and manage passenger information._

**Acceptance Criteria:**

- Staff can select a trip by date, route, or trip code to view the passenger list
- Display passenger details including:
  - Full name
  - Phone number
  - Ticket code / booking code
  - Seat number
  - Payment status (Paid / Unpaid)
  - Check-in status (Boarded / Not boarded)
- Search passengers by name, phone number, or ticket code
- Export or print list (PDF, Excel)
- Manually update check-in status if needed (e.g., passenger without a phone)
- Filter passenger list by status (Checked-in / Not checked-in / Canceled)

**Priority:** Medium

**Story Points:** 5

**UI Design:** _[Figma link] / [Attached image]_
----
### User Story #4: Report Trip Incident

**User Story:**  
_As a bus company staff member, I want to report any incidents during the trip so that the company can respond quickly and take appropriate actions._

**Acceptance Criteria:**

- Staff can select an ongoing or completed trip to create an incident report
- Each report includes:
  - Trip code / route
  - Time of incident
  - Detailed description (e.g., vehicle breakdown, disruptive passenger, traffic jam, accident, bad weather…)
  - Supporting images (optional)
  - Severity level (Low / Medium / High)
- Ability to attach current GPS location (if trip is active)
- Report is sent to management and displayed in the incident tracking system
- After submission, show incident status: Pending, Received, In Progress, Resolved
- Staff can update the report with additional developments if the issue persists

**Priority:** Medium

**Story Points:** 3

**UI Design:** _[Figma link] / [Attached image]_
