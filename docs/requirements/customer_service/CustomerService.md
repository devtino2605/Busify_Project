# Customer Service Features - User Stories

---

## 1. Search for Customer's Ticket Information (API - done/UI)

**User Story:**  
As a customer service staff, I want to search for customer's ticket information by ticket code, email, or phone number, so that I can quickly support customers in checking their booking status.

**Acceptance Criteria:**
- Can input ticket code, email, or phone number to search.
- If the input information is invalid or incomplete, display clear error messages.
- If the ticket information is found, the system displays detailed information: ticket code, customer name, route, time, ticket status, etc.
- If no information is found, display the message "No matching ticket information found."
- System response time does not exceed 3 seconds.
- Allow multiple searches with different information.

**Priority:** Medium  
**Story Points:** 3  
### new api:
- GET - api/tickets - get all tickets
- GET - api/tickets/search - search ticket
  - param:
    - ticketCode - string
    - name - string
    - phone - string
---

## 2. Search for Trip Information (UI)

**User Story:**  
As a customer service staff, I want to search for a list of available trips based on specific criteria (departure location, destination, date, time, etc.) so that I can assist customers in finding suitable trips quickly.

**Acceptance Criteria:**
- Can access the trip search feature from the system interface.
- The interface provides a form with input fields: departure location, destination, departure date, departure time.
- When valid information is provided, the system allows sending the search request.
- The backend verifies the data and returns a list of suitable trips.
- If no suitable trips are found, display the message "No matching trip found."
- System response time does not exceed 5 seconds.
- Results are displayed in a table, possibly including information: trip code, departure date, time, departure point, destination, seat status.

**Priority:** Medium  
**Story Points:** 5  
**UI Design:** Attached business flow diagram (your provided image)

---

## 3. Handle Customer Complaints (API - done/UI)

**User Story:**  
As a customer service staff I want to view and process customer complaints so that I can provide appropriate resolutions such as support, refunds, explanations, or rejections.

**Acceptance Criteria:**
- Can access the complaints list function in the system.
- The system displays a list of unresolved complaints.
- Can select any complaint to view details.
- The system displays full complaint details: ticket code, complaint content, current status, customer information, response history (if any).
- can input the handling result (support, refund, explanation, rejection, etc.) and update the status.
- The system saves the handling result and updates the complaint status.
- After successful saving, the system displays the message "Complaint processed successfully."

**Priority:** High  
**Story Points:** 8  
**UI Design:** Attached business flow diagram (your provided image)

### new api:
- GET - api/complaints - get all complaints

---

## 4. Update Customer's Ticket Information (API/UI)

**User Story:**  
As a customer service staff I want to modify the details of a customer's booking (such as name, phone number, email, etc.) so that I can correct inaccurate information for customers after they have completed their booking.

**Acceptance Criteria:**
- Can access the ticket management function from the system interface.
- The system displays the list of booked tickets.
- Can select any ticket to view detailed information.
- The system displays full ticket information: ticket code, customer name, phone number, email, departure date, time, route, ticket status, etc.
- Can edit allowed fields: full name, email, phone number.
- Sends update requests, the system saves the new information to the database.
- After successful saving, display the message "Ticket information updated successfully."

**Priority:** Medium  
**Story Points:** 5  
**UI Design:** Attached business flow diagram (your provided image)

### new api
- PATCH - api/bookings/{bookingCode} - update booking
- GET - api/bookings/all - get all booking


---

## 5. Filter Customer Reviews (API/UI)

**User Story:**  
As a customer service staff I want to filter the customer reviews list by different criteria (such as star ratings, date, processing status), so that I can easily manage and review relevant feedback.

**Acceptance Criteria:**
- Can access the customer review list in the system interface.
- The system displays all collected customer reviews.
- Can select filtering criteria such as:
  - Star ratings (from 1 to 5)
  - Review creation date
- When submits a filter request, the system performs data filtering based on the criteria.
- Filter results are accurate and complete according to the criteria.
- The system displays the filtered review list clearly and understandably.

**Priority:** Medium  
**Story Points:** 3  
**UI Design:** Attached business flow diagram (your provided image)

### new api:
- GET - api/reviews/filter - filter reviews
---

## 6. Search Customer Reviews (API/UI)

**User Story:**  
As a customer service staff I want to search for customer reviews by keywords (such as customer name, content, ticket code, etc.) so that I can quickly find specific feedback for review or processing.

**Acceptance Criteria:**
- Can access the customer review list in the system interface.
- The system displays all collected customer reviews.
- Can input search keywords based on the following criteria:
  - Customer name
  - Review content
  - Ticket code
- When performs a search, the system queries and filters data based on the keywords.
- The results returned are accurate and match the search keywords.
- The system displays the matching review list clearly and understandably.

**Priority:** Medium  
**Story Points:** 3  
**UI Design:** Attached business flow diagram (your provided image)

### new api:
- GET - api/reviews - get all reviews
- GET - api/reviews/search - search reviews

---
