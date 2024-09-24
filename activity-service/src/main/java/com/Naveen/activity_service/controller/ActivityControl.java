package com.Naveen.activity_service.controller;


import com.Naveen.activity_service.entity.Activity;
import com.Naveen.activity_service.service.ActivityService;
import com.Naveen.activity_service.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.ACTIVITIES)
public class ActivityControl {

    @Autowired
    private ActivityService activityService;

    @GetMapping(Constant.EMPTY)
    public List<Activity> findAllActivity(){
        return activityService.findAllActivity();
    }

    @GetMapping(Constant.ACTIVITY_ID)
    public Activity findById(@PathVariable("activityId") int activityId){
        return activityService.findActivityById(activityId);
    }

    @PostMapping(Constant.EMPTY)
    public Activity saveActivity(@RequestBody Activity activity){
        return activityService.saveActivity(activity);
    }

    @DeleteMapping(Constant.ACTIVITY_ID)
    public  String deleteActivity(@PathVariable("activityId") int activityId){
        activityService.deleteActivity(activityId);
        return  "Activity with id: "+activityId+" deleted";
    }

    @GetMapping(Constant.USER_USER_ID)
    public List<Activity> findByUserId(@PathVariable("userId") int userId){
        return activityService.findByUserId(userId);
    }

    @PutMapping(Constant.EMPTY)
    public Activity updateActivity(@RequestBody Activity activity){
        return activityService.updateActivity(activity);
    }


    @DeleteMapping(Constant.USER_USER_ID)
    public String deleteActivityByUserId(@PathVariable("userId") int userId){
        activityService.deleteActivityByUserId(userId);
        return Constant.DELETE_MSG+userId;
    }
}
