# User Stories

## User Story #1: Search for a Trip
- **User Story**: As a customer, I want to search for trips based on departure point, destination, and departure date, so that I can find a trip that suits my needs.
- **Acceptance Criteria**:
  - The user can enter or select the departure point.
  - The user can enter or select the destination.
  - The user can select the departure date.
  - The system displays a list of trips that match the search criteria.
  - Each result in the list must display basic information: departure time, estimated arrival time, bus operator name, and ticket price.
  - If no trips are found, the system displays a clear message such as: "No trips found matching your request. Please try again with different criteria."
- **Priority**: High
- **Story Points**: 2
- **UI Design**: [Figma Link or Attached Image]

## User Story #2: View Trip Details
- **User Story**: As a customer, I want to view detailed information about a specific trip, so that I can decide whether to book a ticket for that trip.
- **Acceptance Criteria**:
  - After selecting a trip from the search list, the user is redirected to the details page.
  - The details page displays full information: bus operator name, vehicle type, route, pick-up/drop-off points, bus operator policies, trip images (if available), and reviews/ratings from passengers who have taken this trip.
  - The seat layout of the bus is displayed with seat statuses: available, booked, or selected.
  - If the trip has no images, the system displays a default image or a message "No images available."
  - If the trip has no reviews, the system displays a message "No reviews available for this trip."
- **Priority**: High
- **Story Points**: 2
- **UI Design**: [Figma Link or Attached Image]

## User Story #3: Book a Ticket
- **User Story**: As a customer, I want to select seats and proceed with booking a ticket, so that I can reserve a spot for my trip.
- **Acceptance Criteria**:
  - The user can select one or more available seats from the seat layout.
  - The user must enter passenger information (full name, phone number, email).
  - After selecting seats and entering information, the system displays a summary page with booking details: trip, selected seats, total price, and passenger information.
  - The user must confirm the summary details before proceeding to payment.
  - The user can choose a payment method, including:
    - Immediate payment (e.g., credit card, e-wallet).
    - Post-paid payment, which requires a deposit (e.g., 20% of the total ticket price or 30% for not login casse) and notifies the user of the full payment deadline (e.g., within 24 hours).
  - The system clearly displays deposit information (amount, payment deadline), cancellation policies if full payment is not made (e.g., forfeiture of deposit), and the final payment method.
  - After the deposit is successfully paid (or immediate payment is made), the system sends a booking confirmation notification via email or SMS, including payment status and deadline (if post-paid).
  - The booked ticket is saved in the user's "Booking History" section with an appropriate status (e.g., "Deposited" or "Fully Paid").
- **Priority**: High
- **Story Points**: 3
- **UI Design**: [Figma Link or Attached Image]

## User Story #4: View Booking History
- **User Story**: As a logged-in customer, I want to view all the tickets I have booked, so that I can easily manage my trips.
- **Acceptance Criteria**:
  - The user can access the "Booking History" or "My Tickets" section.
  - The system displays a list of booked tickets, including upcoming and past trips, with summary information: route, departure date, status (e.g., confirmed, canceled, completed).
  - The list can be sorted or filtered by status (upcoming, past, canceled) for easier management.
  - The user can click on a ticket to view its detailed information.
- **Priority**: Medium
- **Story Points**: 2
- **UI Design**: [Figma Link or Attached Image]

## User Story #5: Cancel/Change a Ticket
- **User Story**: As a customer, I want to cancel or change a booked ticket (if permitted), so that I can adjust my travel plans in case of unexpected events.
- **Acceptance Criteria**:
  - When viewing details of an upcoming ticket, the user sees a "Cancel Ticket" or "Change Ticket" button if the ticket meets the cancellation/change conditions per the bus operator's policy.
  - The system clearly displays the cancellation/change policy (e.g., cancellation fee, allowed time frame).
  - Before submitting a cancellation or change request, the system displays a confirmation dialog for the user to agree.
  - For the "Change Ticket" feature, the user can select a different trip from the same operator or route (if permitted), and the system processes the ticket change, including calculating any price difference (if applicable).
  - After the request is processed, the ticket status is updated, and the user receives a notification via email or SMS.
- **Priority**: Medium
- **Story Points**: 3
- **UI Design**: [Figma Link or Attached Image]

## User Story #6: Rate a Trip
- **User Story**: As a customer, I want to rate and write a review about a completed trip, so that I can share my experience and help the bus operator improve their service.
- **Acceptance Criteria**:
  - After the trip is completed, the user sees a "Rate" option for the ticket in their booking history.
  - The user can rate the trip (e.g., from 1 to 5 stars).
  - The user can write a detailed comment about their experience.
  - After submission, the rating is saved and may undergo a review process before being publicly displayed on the trip details page. (If no review process is applied, the rating is displayed immediately.)
- **Priority**: Low
- **Story Points**: 2
- **UI Design**: [Figma Link or Attached Image]

## User Story #7: Manage Personal Information
- **User Story**: As a customer, I want to add and update my personal information, so that my information remains accurate and the booking process is faster next time.
- **Acceptance Criteria**:
  - The user can access the account management page.
  - The user can modify information such as: full name, phone number, email, password.
  - When the user saves changes, the new information is updated immediately across the entire system.
- **Priority**: Medium
- **Story Points**: 1
- **UI Design**: [Figma Link or Attached Image]

## User Story #8: Submit a Complaint
- **User Story**: As a customer, I want to submit a complaint or feedback about an issue, so that I can receive support and help the service provider address the problem.
- **Acceptance Criteria**:
  - The user can find the "Submit Complaint/Support" section.
  - The complaint form allows the user to select a topic (e.g., service quality, payment issues), enter detailed content, and attach images as evidence (optional).
  - After submission, the system confirms receipt and provides a tracking code for the user to check the complaint status.
- **Priority**: Medium
- **Story Points**: 2
- **UI Design**: [Figma Link or Attached Image]

## User Story #9: Chat with Customer Support
- **User Story**: As a customer, I want to chat directly with customer support staff, so that I can get quick answers to my questions.
- **Acceptance Criteria**:
  - The user can open a chat window from a button or icon on the website.
  - The system displays the status of customer support staff (online/offline).
  - The user can send and receive text messages in real-time.
  - The chat history is saved, and the user can review previous conversations in their account.
- **Priority**: Medium
- **Story Points**: 2
- **UI Design**: [Figma Link or Attached Image]
