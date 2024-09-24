package com.Naveen.user_service.controller;


import com.Naveen.user_service.entity.Activity;
import com.Naveen.user_service.entity.User;
import com.Naveen.user_service.service.UserService;
import com.Naveen.user_service.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(Constant.USERS)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(Constant.EMPTY)
    public List<User> getUsers(){
        return userService.getAllUsers();
    }
    @GetMapping(Constant.USERS_ID)
    public  User getUserById(@PathVariable("userId") int userId){
        return  userService.getUserById(userId);
    }

    @PostMapping(Constant.EMPTY)
    public User saveUser(@RequestBody User user){
        user.setId(0);
       return userService.saveUser(user);
    }

    @PutMapping(Constant.EMPTY)
    public User updateUser(@RequestBody User user){
        return  userService.saveUser(user);
    }

    @PatchMapping(Constant.USERS_ID)
    public User patchUser(@PathVariable("userId") int userId,@RequestBody User user){
        return userService.updateUser(userId,user);
    }

    @DeleteMapping(Constant.USERS_ID)
    public String deleteUser(@PathVariable("userId") int userId){
        userService.deleteUser(userId);
        return Constant.DELETE_MSG+userId;
    }

    @GetMapping(Constant.USER_ID_ACTIVITIES)
    public List<Activity> getUserActivities(@PathVariable("userId") int userId){
        return  userService.getUserActivities(userId);
    }

    @PostMapping(Constant.USER_ID_ACTIVITIES)
    public  Activity saveUserActivity(@PathVariable("userId") int userId,@RequestBody Activity activity){
        return userService.saveUserActivity(activity,userId);
    }

    @GetMapping(Constant.USER_ID_ACTIVITIES_ACTIVITY_ID)
    public Activity getUserActivity(@PathVariable("userId") int userId,@PathVariable("activityId") int activityId){
        return userService.getUserActivity(userId,activityId);
    }



    @PutMapping(Constant.USER_ID_ACTIVITIES_ACTIVITY_ID)
    public Activity updateUserActivity(@PathVariable("userId") int userId,@PathVariable("activityId") int activityId,@RequestBody Activity activity){
        return userService.updateUserActivity(userId,activityId,activity);
    }
    @DeleteMapping(Constant.USER_ID_ACTIVITIES_ACTIVITY_ID)
    public String deleteUserActivities(@PathVariable("userId") int userId,@PathVariable("activityId") int activityId){
        return  userService.deleteUserActivities(userId,activityId);
    }
}
