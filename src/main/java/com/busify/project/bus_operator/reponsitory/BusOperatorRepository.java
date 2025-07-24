package com.busify.project.bus_operator.reponsitory;

import com.busify.project.bus_operator.entity.BusOperator;
import com.busify.project.trip.dto.response.TopOperatorRatingDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusOperatorRepository extends JpaRepository<BusOperator,Long> {
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
