package com.busify.project.bookings;
// import com.busify.project.booking.entity.Bookings;
// import com.busify.project.booking.enums.BookingStatus;
// import com.busify.project.trip.entity.Trip;
// import com.busify.project.user.entity.User;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.DisplayName;
// import java.math.BigDecimal;
// import java.time.Instant;
// import static org.junit.jupiter.api.Assertions.*;

public class BookingsTest {

    // private Bookings booking;
    // private User customer;
    // private Trip trip;
    // private User agent;

    // @BeforeEach
    // void setUp() {
    //     customer = new User();
    //     customer.setId(1L);

    //     trip = new Trip();
    //     trip.setId(1L);

    //     agent = new User();
    //     agent.setId(2L);

    //     booking = new Bookings();
    // }

    // @Test
    // @DisplayName("Should create booking with all required fields")
    // void shouldCreateBookingWithRequiredFields() {
    //     booking.setCustomer(customer);
    //     booking.setTrip(trip);
    //     booking.setBookingCode("BK123456");
    //     booking.setSeatNumber("A1");
    //     booking.setTotalAmount(new BigDecimal("50.00"));
    //     booking.setStatus(BookingStatus.pending);

    //     assertNotNull(booking);
    //     assertEquals(customer, booking.getCustomer());
    //     assertEquals(trip, booking.getTrip());
    //     assertEquals("BK123456", booking.getBookingCode());
    //     assertEquals("A1", booking.getSeatNumber());
    //     assertEquals(new BigDecimal("50.00"), booking.getTotalAmount());
    //     assertEquals(BookingStatus.pending, booking.getStatus());
    // }

    // @Test
    // @DisplayName("Should set guest information for non-registered users")
    // void shouldSetGuestInformation() {
    //     booking.setGuestFullName("John Doe");
    //     booking.setGuestEmail("john@example.com");
    //     booking.setGuestPhone("+1234567890");
    //     booking.setGuestAddress("123 Main St");

    //     assertEquals("John Doe", booking.getGuestFullName());
    //     assertEquals("john@example.com", booking.getGuestEmail());
    //     assertEquals("+1234567890", booking.getGuestPhone());
    //     assertEquals("123 Main St", booking.getGuestAddress());
    // }

    // @Test
    // @DisplayName("Should automatically set createdAt timestamp")
    // void shouldSetCreatedAtTimestamp() {
    //     Instant before = Instant.now();
    //     Bookings newBooking = new Bookings();
    //     Instant after = Instant.now();

    //     assertNotNull(newBooking.getCreatedAt());
    //     assertTrue(newBooking.getCreatedAt().isAfter(before.minusSeconds(1)));
    //     assertTrue(newBooking.getCreatedAt().isBefore(after.plusSeconds(1)));
    // }


    // @Test
    // @DisplayName("Should set agent who accepted booking")
    // void shouldSetAgentAcceptBooking() {
    //     booking.setAgentAcceptBooking(agent);

    //     assertEquals(agent, booking.getAgentAcceptBooking());
    // }

    // @Test
    // @DisplayName("Should handle booking status changes")
    // void shouldHandleBookingStatusChanges() {
    //     booking.setStatus(BookingStatus.pending);
    //     assertEquals(BookingStatus.pending, booking.getStatus());

    //     booking.setStatus(BookingStatus.confirmed);
    //     assertEquals(BookingStatus.confirmed, booking.getStatus());

    //     booking.setStatus(BookingStatus.canceled_by_user);
    //     assertEquals(BookingStatus.canceled_by_user, booking.getStatus());

    //     booking.setStatus(BookingStatus.canceled_by_operator);
    //     assertEquals(BookingStatus.canceled_by_operator, booking.getStatus());
    // }

    // @Test
    // @DisplayName("Should validate total amount is positive")
    // void shouldValidateTotalAmount() {
    //     BigDecimal positiveAmount = new BigDecimal("100.50");
    //     booking.setTotalAmount(positiveAmount);

    //     assertEquals(positiveAmount, booking.getTotalAmount());
    //     assertTrue(booking.getTotalAmount().compareTo(BigDecimal.ZERO) > 0);
    // }

    // @Test
    // @DisplayName("Should generate unique booking code")
    // void shouldHaveUniqueBookingCode() {
    //     String bookingCode = "BK" + System.currentTimeMillis();
    //     booking.setBookingCode(bookingCode);

    //     assertNotNull(booking.getBookingCode());
    //     assertEquals(bookingCode, booking.getBookingCode());
    // }
}
