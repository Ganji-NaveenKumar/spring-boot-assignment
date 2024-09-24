package com.Naveen.user_service.service;

import com.Naveen.user_service.entity.Activity;
import com.Naveen.user_service.entity.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUsers();

    public User getUserById(int userId);

    public User saveUser(User user);

    public  void deleteUser(int userId);

    public User updateUser(int userId, User user);

    public List<Activity> getUserActivities(int UserId);

    public Activity saveUserActivity(Activity activity,int userId);

    public  String deleteUserActivities(int userId,int ActivityId);

    public  Activity updateUserActivity(int userId,int ActivityId,Activity activity);

   public Activity getUserActivity(int userId, int activityId);
}
