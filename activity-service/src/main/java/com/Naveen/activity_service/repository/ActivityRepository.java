package com.Naveen.activity_service.repository;

import com.Naveen.activity_service.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Integer> {
    List<Activity> findByUserId(int userId);
    void deleteActivityByUserId(int userId);
}
