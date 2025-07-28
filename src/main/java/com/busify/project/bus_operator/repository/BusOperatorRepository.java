package com.busify.project.bus_operator.repository;

import com.busify.project.bus_operator.dto.response.BusOperatorRatingResponse;
import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;

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

}
