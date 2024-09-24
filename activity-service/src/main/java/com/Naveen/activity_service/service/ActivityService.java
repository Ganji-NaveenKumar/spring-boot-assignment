package com.Naveen.activity_service.service;

import com.Naveen.activity_service.entity.Activity;

import java.util.List;

public interface ActivityService {

    public List<Activity> findAllActivity();

    public Activity findActivityById(int ActivityId);

    public Activity saveActivity(Activity activity);

    public void  deleteActivity(int ActivityId);

    public List<Activity> findByUserId(int userId);

    public Activity updateActivity(Activity activity);

    public void deleteActivityByUserId(int userId);
}
