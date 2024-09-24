package com.Naveen.activity_service.service;


import com.Naveen.activity_service.entity.Activity;
import com.Naveen.activity_service.exception.ActivityNotFoundException;
import com.Naveen.activity_service.repository.ActivityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityServiceClass implements ActivityService{

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    @Transactional
    public List<Activity> findAllActivity() {
        return activityRepository.findAll();
    }

    @Override
    @Transactional
    public Activity findActivityById(int activityId) {
        Activity activity=activityRepository.findById(activityId)
                .orElseThrow(()->new ActivityNotFoundException("Activity with id : "+activityId+" not found"));
        return  activity;
    }

    @Override
    @Transactional
    public Activity saveActivity(Activity activity) {
        activityRepository.save(activity);
        return activity;
    }

    @Override
    @Transactional
    public void deleteActivity(int activityId) {
        Activity activity=activityRepository.findById(activityId)
                .orElseThrow(()->new ActivityNotFoundException("Activity with id : "+activityId+" not found"));
        activityRepository.deleteById(activityId);
    }

    @Override
    @Transactional
    public List<Activity> findByUserId(int userId) {
         return activityRepository.findByUserId(userId);
    }

    @Override
    public Activity updateActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    @Transactional
    public void deleteActivityByUserId(int userId) {
        activityRepository.deleteActivityByUserId(userId);
    }


}
