package com.busify.project.report.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.busify.project.trip.entity.Trip;
import com.busify.project.trip.repository.TripRepository;
import com.busify.project.booking.entity.Bookings;
import com.busify.project.booking.repository.BookingRepository;
import com.busify.project.bus_operator.repository.BusOperatorRepository;
import com.busify.project.report.entity.ReportEntity;
import com.busify.project.report.repository.ReportRepository;
import com.busify.project.ticket.entity.Tickets;
import com.busify.project.ticket.enums.TicketStatus;
import com.busify.project.ticket.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

        private final TripRepository tripRepository;
        private final BookingRepository bookingRepository;
        private final ReportRepository reportRepository;
        private final BusOperatorRepository busOperatorRepository;
        private final TicketRepository ticketRepository;

        // run at the start of every month
        @Scheduled(cron = "0 0 0 1 * ?")
        // run at 12:55 every day for testing
        // @Scheduled(cron = "0 20 13 * * ?")
        public void generateMonthlyReport() {
                System.out.println("Run cron job at " + Instant.now());
                final List<Long> operatorIds = busOperatorRepository.findAllIds();
                System.out.println("Found operators: " + operatorIds);
                for (Long operatorId : operatorIds) {
                        List<Trip> trips = tripRepository.findTripArrivedByOperatorId(operatorId).stream().filter(t -> {
                                Instant date = t.getEstimatedArrivalTime();
                                Instant now = Instant.now();
                                return date.atZone(java.time.ZoneId.systemDefault()).getMonth() == now
                                                .atZone(java.time.ZoneId.systemDefault()).getMonth()
                                                && date.atZone(java.time.ZoneId.systemDefault()).getYear() == now
                                                                .atZone(java.time.ZoneId.systemDefault()).getYear();
                        }).toList();
                        Long totalTrips = (long) trips.size();

                        BigDecimal totalRevenue = BigDecimal.ZERO;

                        Long totalTickets = 0L;
                        Long canceledTickets = 0L;

                        for (Trip trip : trips) {
                                List<Bookings> bookings = bookingRepository.findByTripId(trip.getId());
                                for (Bookings booking : bookings) {
                                        List<Tickets> tickets = ticketRepository.findById(booking.getId()).stream()
                                                        .toList();
                                        totalTickets += tickets.size();
                                        canceledTickets += tickets.stream()
                                                        .filter(t -> t.getStatus().equals(TicketStatus.cancelled))
                                                        .count();
                                        totalRevenue = totalRevenue.add(tickets.stream()
                                                        .filter(t -> t.getStatus().equals(TicketStatus.used))
                                                        .map(Tickets::getPrice)
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                                }
                        }

                        double cancelTicketRatio = totalTickets == 0 ? 0.0 : (double) canceledTickets / totalTickets;

                        ReportEntity report = new ReportEntity();
                        report.setId(UUID.randomUUID().toString());
                        report.setTitle("Monthly Report");
                        report.setReportDate(Instant.now());
                        report.setOperatorId(operatorId);
                        report.setData(Map.of(
                                        "totalTrips", totalTrips,
                                        "cancelTicketRatio", cancelTicketRatio,
                                        "totalRevenue", totalRevenue));
                        reportRepository.save(report);
                }
        }

        // run at the start of every year
        @Scheduled(cron = "0 0 0 1 1 ?")
        public void generateYearlyReport() {
                final List<Long> operatorIds = busOperatorRepository.findAllIds();
                for (Long operatorId : operatorIds) {
                        List<ReportEntity> monthlyReports = getReportsByYear(
                                        Instant.now().atZone(java.time.ZoneId.systemDefault()).getYear() - 1,
                                        operatorId)
                                        .stream()
                                        .filter(r -> r.getOperatorId().equals(operatorId))
                                        .toList();
                        final ReportEntity report = new ReportEntity();
                        report.setId(UUID.randomUUID().toString());
                        report.setTitle("Yearly Report");
                        report.setReportDate(Instant.now());
                        report.setOperatorId(operatorId);
                        report.setData(Map.of(
                                        "totalTrips",
                                        monthlyReports.stream().mapToLong(r -> (Long) r.getData().get("totalTrips"))
                                                        .sum(),
                                        "cancelTicketRatio",
                                        monthlyReports.stream()
                                                        .mapToDouble(r -> (Double) r.getData().get("cancelTicketRatio"))
                                                        .average()
                                                        .orElse(0.0),
                                        "totalRevenue",
                                        monthlyReports.stream().map(r -> (BigDecimal) r.getData().get("totalRevenue"))
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add)));
                        reportRepository.save(report);
                }
        }

        public List<ReportEntity> getReportsByYear(int year, Long operatorId) {
                final List<ReportEntity> reports = reportRepository.findByOperatorId(operatorId);
                final List<ReportEntity> filteredReports = reports.stream()
                                .filter(report -> report.getTitle().equals("Monthly Report"))
                                .filter(report -> {
                                        Instant date = report.getReportDate();
                                        int reportYear = date.atZone(java.time.ZoneId.systemDefault()).getYear();
                                        return reportYear == year;
                                })
                                .toList();
                return filteredReports;
        }

        public List<ReportEntity> getReportsByYearRange(int startYear, int endYear, Long operatorId) {
                final List<ReportEntity> reports = reportRepository.findByOperatorId(operatorId);
                return reports.stream()
                                .filter(report -> report.getTitle().equals("Yearly Report"))
                                .filter(report -> {
                                        Instant date = report.getReportDate();
                                        int reportYear = date.atZone(java.time.ZoneId.systemDefault()).getYear();
                                        return reportYear >= startYear && reportYear <= endYear;
                                })
                                .toList();
        }
}
