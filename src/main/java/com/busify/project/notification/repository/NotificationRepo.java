package com.busify.project.notification.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.busify.project.notification.entity.NotificationData;

@Repository
public interface NotificationRepo extends MongoRepository<NotificationData, String> {

    public List<NotificationData> findAllBySub(String sub);
}
