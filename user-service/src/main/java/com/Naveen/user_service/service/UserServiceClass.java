package com.Naveen.user_service.service;

import com.Naveen.user_service.entity.Activity;
import com.Naveen.user_service.entity.User;
import com.Naveen.user_service.exception.UserNotFoundException;
import com.Naveen.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


import com.Naveen.user_service.utils.Constant;

@Service
public class UserServiceClass implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("User with id : "+userId+ " not found"));
        return user;
    }

    @Override
    public User saveUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("User with id ; "+userId+" not found"));
        ResponseEntity<String> response=restTemplate.exchange(Constant.URL+"/user/"+userId,HttpMethod.DELETE,null,String.class);
        userRepository.deleteById(userId);
    }

    @Override
    public User updateUser(int userId, User user) {
        User updatedUser=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("user with id : "+userId+" not found"));
        if(user.getEmail()!=null){
            updatedUser.setEmail(user.getEmail());
        }
        if(user.getFirstName()!=null){
            updatedUser.setFirstName(user.getFirstName());
        }
        if(user.getLastName()!=null){
            updatedUser.setLastName(user.getLastName());
        }
        return  updatedUser;
    }

    @Override
    public List<Activity> getUserActivities(int userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id : "+userId+ " not found"));

        ResponseEntity<List<Activity>> userActivities=restTemplate.exchange(Constant.URL+"/user/"+userId,
                HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Activity>>() {});


        return userActivities.getBody();
    }

    @Override
    public Activity saveUserActivity(Activity activity, int userId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id : "+userId+ " not found"));
        activity.setUserId(userId);
        ResponseEntity<Activity> response=restTemplate.postForEntity(Constant.URL,activity, Activity.class);
        return response.getBody();
    }

    @Override
    public String deleteUserActivities(int userId,int activityId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id : "+userId+ " not found"));
        ResponseEntity<String> response=restTemplate.exchange(Constant.URL+"/"+activityId,HttpMethod.DELETE,null, String.class);
        return response.getBody();
    }

    @Override
    public Activity updateUserActivity(int userId,int ActivityId,Activity activity) {
        User user=userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id : "+userId+ " not found"));
        ResponseEntity<Activity> response=restTemplate.exchange(Constant.URL,HttpMethod.PUT,new HttpEntity<>(activity),Activity.class);
        return response.getBody();
    }

    @Override
    public Activity getUserActivity(int userId, int activityId) {
        User user=userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException("user with id : "+userId+ " not found"));
        ResponseEntity<Activity> response=restTemplate.exchange(Constant.URL+"/"+activityId,HttpMethod.GET,null, Activity.class);
        return response.getBody();
    }
}
