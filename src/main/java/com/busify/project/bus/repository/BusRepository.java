package com.busify.project.bus.repository;

import com.busify.project.bus.entity.Bus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.busify.project.bus_operator.entity.BusOperator;


@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
    List<Bus> findByOperator(BusOperator operator);
}