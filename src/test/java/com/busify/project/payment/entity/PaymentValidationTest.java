package com.busify.project.payment.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.cargo.entity.CargoBooking;
import com.busify.project.payment.enums.PaymentMethod;
import com.busify.project.payment.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Payment entity validation
 * 
 * Tests the validation logic ensuring:
 * - Payment must have exactly one target (booking XOR cargoBooking)
 * - Payment cannot have both targets
 * - Payment cannot have no targets
 * 
 * @author Busify Team
 * @version 1.0
 * @since 2025-11-10
 */
class PaymentValidationTest {

    /**
     * Test Case 1: Valid payment with booking only
     * Expected: No exception thrown
     */
    @Test
    void testValidPayment_WithBookingOnly_ShouldPass() {
        // Arrange
        Payment payment = new Payment();
        payment.setBooking(new Bookings()); // Has booking
        payment.setCargoBooking(null); // No cargo
        payment.setPaymentMethod(PaymentMethod.VNPAY);
        payment.setAmount(BigDecimal.valueOf(100000));
        payment.setStatus(PaymentStatus.pending);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            payment.validateAndUpdate();
        });
    }

    /**
     * Test Case 2: Valid payment with cargo only
     * Expected: No exception thrown
     */
    @Test
    void testValidPayment_WithCargoOnly_ShouldPass() {
        // Arrange
        Payment payment = new Payment();
        payment.setBooking(null); // No booking
        payment.setCargoBooking(new CargoBooking()); // Has cargo
        payment.setPaymentMethod(PaymentMethod.PAYPAL);
        payment.setAmount(BigDecimal.valueOf(50000));
        payment.setStatus(PaymentStatus.pending);

        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> {
            payment.validateAndUpdate();
        });
    }

    /**
     * Test Case 3: Invalid payment with both booking and cargo
     * Expected: IllegalStateException thrown
     */
    @Test
    void testInvalidPayment_WithBothTargets_ShouldThrowException() {
        // Arrange
        Payment payment = new Payment();
        payment.setBooking(new Bookings()); // Has booking
        payment.setCargoBooking(new CargoBooking()); // Has cargo - INVALID!
        payment.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        payment.setAmount(BigDecimal.valueOf(200000));
        payment.setStatus(PaymentStatus.pending);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            payment.validateAndUpdate();
        });

        // Verify error message
        assertTrue(exception.getMessage().contains("cannot be associated with both"));
        assertTrue(exception.getMessage().contains("Booking and CargoBooking"));
    }

    /**
     * Test Case 4: Invalid payment with no targets
     * Expected: IllegalStateException thrown
     */
    @Test
    void testInvalidPayment_WithNoTargets_ShouldThrowException() {
        // Arrange
        Payment payment = new Payment();
        payment.setBooking(null); // No booking
        payment.setCargoBooking(null); // No cargo - INVALID!
        payment.setPaymentMethod(PaymentMethod.VNPAY);
        payment.setAmount(BigDecimal.valueOf(150000));
        payment.setStatus(PaymentStatus.pending);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            payment.validateAndUpdate();
        });

        // Verify error message
        assertTrue(exception.getMessage().contains("must be associated with either"));
        assertTrue(exception.getMessage().contains("Both cannot be null"));
    }

    /**
     * Test Case 5: Helper method isCargo() works correctly
     */
    @Test
    void testHelperMethod_IsCargo_ShouldReturnCorrectly() {
        // Test with cargo
        Payment cargoPayment = new Payment();
        cargoPayment.setCargoBooking(new CargoBooking());
        assertTrue(cargoPayment.isCargo());
        assertFalse(cargoPayment.isBooking());

        // Test with booking
        Payment bookingPayment = new Payment();
        bookingPayment.setBooking(new Bookings());
        assertFalse(bookingPayment.isCargo());
        assertTrue(bookingPayment.isBooking());
    }

    /**
     * Test Case 6: Helper method getPaymentType() works correctly
     */
    @Test
    void testHelperMethod_GetPaymentType_ShouldReturnCorrectType() {
        // Test booking payment type
        Payment bookingPayment = new Payment();
        bookingPayment.setBooking(new Bookings());
        assertEquals("TICKET", bookingPayment.getPaymentType());

        // Test cargo payment type
        Payment cargoPayment = new Payment();
        cargoPayment.setCargoBooking(new CargoBooking());
        assertEquals("CARGO", cargoPayment.getPaymentType());

        // Test unknown type (should not happen if validation works)
        Payment invalidPayment = new Payment();
        assertEquals("UNKNOWN", invalidPayment.getPaymentType());
    }

    /**
     * Test Case 7: Validation should work on @PrePersist
     * This simulates what happens when saving to database
     */
    @Test
    void testValidation_OnPrePersist_ShouldValidate() {
        // Create invalid payment (no targets)
        Payment payment = new Payment();
        payment.setPaymentMethod(PaymentMethod.VNPAY);
        payment.setAmount(BigDecimal.valueOf(100000));
        payment.setStatus(PaymentStatus.pending);

        // Manually call @PrePersist callback
        assertThrows(IllegalStateException.class, () -> {
            payment.validateAndUpdate();
        });
    }
}
