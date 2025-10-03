package com.busify.project.bus_operator.repository;

import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.dto.response.MonthlyBusOperatorReportDTO;
import com.busify.project.bus_operator.dto.response.MonthlyTotalRevenueDTO;
import com.busify.project.bus_operator.dto.response.WeeklyBusOperatorReportDTO;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.bus_operator.enums.OperatorStatus;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusOperatorRepository extends JpaRepository<BusOperator, Long> {
    @Query(value = "SELECT " +
            "bo.operator_id AS id, " +
            "bo.name AS name, " +
            "bo.email AS email, " +
            "bo.hotline AS hotline, " +
            "COALESCE(AVG(r.rating), 0) AS averageRating, " +
            "COUNT(r.review_id) AS totalReviews " +
            "FROM " +
            "bus_operators bo " +
            "LEFT JOIN " +
            "buses b ON bo.operator_id = b.operator_id " +
            "LEFT JOIN " +
            "trips t ON b.id = t.bus_id " +
            "LEFT JOIN " +
            "reviews r ON t.trip_id = r.trip_id " +
            "GROUP BY " +
            "bo.operator_id, bo.name, bo.hotline, bo.email " +
            "ORDER BY " +
            "averageRating DESC, totalReviews DESC", nativeQuery = true)
    List<BusOperatorRatingResponse> findAllOperatorsWithRatings(Pageable pageable);

    @Query("""
            SELECT bo.id as operatorId, bo.name as operatorName, AVG(r.rating) as averageRating
            FROM Review r
            JOIN r.trip t
            JOIN t.bus b
            JOIN b.operator bo
            GROUP BY bo.id, bo.name
            ORDER BY AVG(r.rating) DESC
            """)
    List<TopOperatorRatingDTO> findTopRatedOperatorId(Pageable pageable);

    @Query("SELECT bo FROM BusOperator bo ")
    List<BusOperator> getAllBusOperators();

    @Query(value = """
            SELECT bo FROM BusOperator bo
            WHERE (:keyword IS NULL OR :keyword = ''
                   OR LOWER(bo.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(bo.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                   OR LOWER(bo.hotline) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:status IS NULL OR bo.status = :status)
              AND (:ownerName IS NULL OR :ownerName = ''
                   OR LOWER(bo.owner.fullName) LIKE LOWER(CONCAT('%', :ownerName, '%')))
            """)
    Page<BusOperator> findBusOperatorsForManagement(
            @Param("keyword") String keyword,
            @Param("status") OperatorStatus status,
            @Param("ownerName") String ownerName,
            Pageable pageable);

    List<BusOperator> findByStatus(OperatorStatus status);

    @Query(value = """
            SELECT
                bo.operator_id AS operatorId,
                CAST(COUNT(DISTINCT t.trip_id) AS SIGNED) AS totalTrips,
                CAST(COALESCE(SUM(CASE WHEN p.status = 'completed' THEN p.amount ELSE 0 END), 0) AS DECIMAL(10,2)) AS totalRevenue,
                CAST(COUNT(DISTINCT CASE WHEN b.status IN ('confirmed', 'completed') THEN b.id END) AS SIGNED) AS totalPassengers,
                COUNT(DISTINCT bus.id) AS totalBuses
            FROM bus_operators bo
            LEFT JOIN buses bus ON bo.operator_id = bus.operator_id
            LEFT JOIN trips t ON bus.id = t.bus_id
                AND t.departure_time >= DATE_SUB(NOW(), INTERVAL 7 DAY)
                AND t.departure_time <= NOW()
            LEFT JOIN bookings b ON t.trip_id = b.trip_id
                AND b.created_at >= DATE_SUB(NOW(), INTERVAL 7 DAY)
                AND b.created_at <= NOW()
            LEFT JOIN payments p ON b.id = p.booking_id
            WHERE bo.operator_id = :operatorId
            GROUP BY bo.operator_id
            """, nativeQuery = true)
    WeeklyBusOperatorReportDTO findWeeklyReportByOperatorId(@Param("operatorId") Long operatorId);

    @Query(value = """
            SELECT
                bo.operator_id AS operatorId,
                bo.name AS operatorName,
                bo.email AS operatorEmail,
                CAST(:month AS SIGNED) AS month,
                CAST(:year AS SIGNED) AS year,
                CAST(COUNT(DISTINCT t.trip_id) AS SIGNED) AS totalTrips,
                CAST(COALESCE(SUM(CASE WHEN p.status = 'completed' THEN p.amount ELSE 0 END), 0) AS DECIMAL(10,2)) AS totalRevenue,
                CAST(COUNT(DISTINCT CASE WHEN b.status IN ('confirmed', 'completed') THEN b.id END) AS SIGNED) AS totalPassengers,
                COUNT(DISTINCT bus.id) AS totalBuses,
                CURDATE() AS reportGeneratedDate,
                FALSE AS sentToAdmin
            FROM bus_operators bo
            LEFT JOIN buses bus ON bo.operator_id = bus.operator_id
            LEFT JOIN trips t ON bus.id = t.bus_id
                AND MONTH(t.departure_time) = :month
                AND YEAR(t.departure_time) = :year
            LEFT JOIN bookings b ON t.trip_id = b.trip_id
                AND MONTH(b.created_at) = :month
                AND YEAR(b.created_at) = :year
            LEFT JOIN payments p ON b.id = p.booking_id
            WHERE bo.operator_id = :operatorId
            GROUP BY bo.operator_id, bo.name, bo.email
            """, nativeQuery = true)
    MonthlyBusOperatorReportDTO findMonthlyReportByOperatorId(
            @Param("operatorId") Long operatorId,
            @Param("month") int month,
            @Param("year") int year);

    // Lấy tất cả báo cáo hàng tháng cho admin
    @Query(value = """
            SELECT
                bo.operator_id AS operatorId,
                bo.name AS operatorName,
                bo.email AS operatorEmail,
                CAST(:month AS SIGNED) AS month,
                CAST(:year AS SIGNED) AS year,
                CAST(COUNT(DISTINCT t.trip_id) AS SIGNED) AS totalTrips,
                CAST(COALESCE(SUM(CASE WHEN p.status = 'completed' THEN p.amount ELSE 0 END), 0) AS DECIMAL(10,2)) AS totalRevenue,
                CAST(COUNT(DISTINCT CASE WHEN b.status IN ('confirmed', 'completed') THEN b.id END) AS SIGNED) AS totalPassengers,
                COUNT(DISTINCT bus.id) AS totalBuses,
                CURDATE() AS reportGeneratedDate,
                TRUE AS sentToAdmin
            FROM bus_operators bo
            LEFT JOIN buses bus ON bo.operator_id = bus.operator_id
            LEFT JOIN trips t ON bus.id = t.bus_id
                AND MONTH(t.departure_time) = :month
                AND YEAR(t.departure_time) = :year
            LEFT JOIN bookings b ON t.trip_id = b.trip_id
                AND MONTH(b.created_at) = :month
                AND YEAR(b.created_at) = :year
            LEFT JOIN payments p ON b.id = p.booking_id
            WHERE bo.status = 'active'
            GROUP BY bo.operator_id, bo.name, bo.email
            ORDER BY totalRevenue DESC
            """, nativeQuery = true)
    List<MonthlyBusOperatorReportDTO> findAllMonthlyReports(
            @Param("month") int month,
            @Param("year") int year);

    // Lấy tổng doanh thu của tất cả bus operators theo từng tháng trong năm
    @Query(value = """
            SELECT
                CAST(month_data.month AS SIGNED) AS month,
                CAST(:year AS SIGNED) AS year,
                CAST(COALESCE(SUM(CASE WHEN p.status = 'completed' THEN p.amount ELSE 0 END), 0) AS DECIMAL(15,2)) AS totalRevenue,
                CAST(COUNT(DISTINCT t.trip_id) AS SIGNED) AS totalTrips,
                CAST(COUNT(DISTINCT CASE WHEN b.status IN ('confirmed', 'completed') THEN b.id END) AS SIGNED) AS totalPassengers,
                CAST(COUNT(DISTINCT bo.operator_id) AS SIGNED) AS totalActiveOperators
            FROM (
                SELECT 1 AS month UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6
                UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12
            ) month_data
            LEFT JOIN bookings b ON MONTH(b.created_at) = month_data.month AND YEAR(b.created_at) = :year
            LEFT JOIN trips t ON b.trip_id = t.trip_id
            LEFT JOIN buses bus ON t.bus_id = bus.id
            LEFT JOIN bus_operators bo ON bus.operator_id = bo.operator_id AND bo.status = 'active'
            LEFT JOIN payments p ON b.id = p.booking_id
            GROUP BY month_data.month
            ORDER BY month_data.month ASC
            """, nativeQuery = true)
    List<MonthlyTotalRevenueDTO> findMonthlyTotalRevenueByYear(@Param("year") int year);

    @Query(value = """
            SELECT
                bo.operator_id AS operatorId,
                bo.name AS operatorName,
                bo.email AS operatorEmail,
                CAST(:year AS SIGNED) AS year,
                CAST(COUNT(DISTINCT t.trip_id) AS SIGNED) AS totalTrips,
                CAST(COALESCE(SUM(CASE WHEN p.status = 'completed' THEN p.amount ELSE 0 END), 0) AS DECIMAL(10,2)) AS totalRevenue,
                CAST(COUNT(DISTINCT CASE WHEN b.status IN ('confirmed', 'completed') THEN b.id END) AS SIGNED) AS totalPassengers,
                COUNT(DISTINCT bus.id) AS totalBuses,
                CURDATE() AS reportGeneratedDate,
                TRUE AS sentToAdmin
            FROM bus_operators bo
            LEFT JOIN buses bus ON bo.operator_id = bus.operator_id
            LEFT JOIN trips t ON bus.id = t.bus_id
                AND YEAR(t.departure_time) = :year
            LEFT JOIN bookings b ON t.trip_id = b.trip_id
                AND YEAR(b.created_at) = :year
            LEFT JOIN payments p ON b.id = p.booking_id
            WHERE bo.status = 'active'
            GROUP BY bo.operator_id, bo.name, bo.email
            ORDER BY totalRevenue DESC
            """, nativeQuery = true)
    List<MonthlyBusOperatorReportDTO> findAllYearlyReports(
            @Param("year") int year);

    @Query("""
            SELECT bo FROM BusOperator bo
            LEFT JOIN bo.owner o
            LEFT JOIN Employee e ON bo.id = e.operator.id
            WHERE bo.owner.id = :userId OR e.id = :userId
            """)
    BusOperator findBusOperatorByUserId(@Param("userId") Long userId);

    @Query("SELECT bo FROM BusOperator bo WHERE bo.email = :email AND bo.isDeleted = :isDeleted")
    Optional<BusOperator> findByEmailAndIsDeleted(@Param("email") String email, @Param("isDeleted") boolean isDeleted);

    @Query("SELECT bo FROM BusOperator bo WHERE LOWER(bo.name) = LOWER(:name) AND bo.isDeleted = false")
    Optional<BusOperator> findByName(@Param("name") String name);

    default Optional<BusOperator> findByEmailAndIsDeletedFalse(String email) {
        return findByEmailAndIsDeleted(email, false);
    }

    @Query("""
                SELECT bo.id
                FROM BusOperator bo
                WHERE bo.owner.id = :userId
            """)
    Optional<Long> findOperatorIdByUserId(@Param("userId") Long userId);

    @Query("SELECT bo.id FROM BusOperator bo")
    List<Long> findAllIds();
}
