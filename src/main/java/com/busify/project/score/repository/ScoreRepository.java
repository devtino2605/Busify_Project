package com.busify.project.score.repository;

import com.busify.project.score.entity.Score;
import com.busify.project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByUser(User user);
}
