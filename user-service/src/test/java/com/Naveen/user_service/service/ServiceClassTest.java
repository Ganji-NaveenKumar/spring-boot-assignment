package com.Naveen.user_service.service;


import com.Naveen.user_service.entity.Activity;
import com.Naveen.user_service.entity.User;
import com.Naveen.user_service.exception.UserNotFoundException;
import com.Naveen.user_service.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceClassTest {
    @InjectMocks
    private UserServiceClass userServiceClass;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @Test
    public void fetchAllUsers(){
        User user1=new User(1,"Naveen","ganji","ganjinaveen@gmail.com");
        User user2=new User(2,"Swarupa","ganji","ganjiswarupa@gmail.com");
        List<User> userList= Arrays.asList(user1,user2);

        when(userRepository.findAll()).thenReturn(userList);
        List<User> result=userRepository.findAll();
        assertEquals(userList,result);
    }

    @Test
    public void fetchUserById_Success(){
        User user1=new User(1,"Naveen","ganji","ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user1));

        Optional<User> userResult=userRepository.findById(1);

        assertNotNull(userResult);
        assertEquals(user1,userResult.get());
    }
    @Test
    public void fetchUserById_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.getUserById(1);
        });

        assertEquals("User with id : 1 not found", exception.getMessage());
    }
    @Test
    public void saveUser_Success() {
        User user = new User(0, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userServiceClass.saveUser(user);
        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    @Test
    public void deleteUser_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), isNull(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Deleted"));

        userServiceClass.deleteUser(1);
        verify(userRepository).deleteById(1);
    }

    @Test
    public void deleteUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.deleteUser(1);
        });

        assertEquals("User with id ; 1 not found", exception.getMessage());
    }

    @Test
    public void updateUser_Success() {
        User existingUser = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User updatedInfo = new User(1, "Naveen", "UpdatedLastName", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));

        User updatedUser = userServiceClass.updateUser(1, updatedInfo);
        assertNotNull(updatedUser);
        assertEquals("UpdatedLastName", updatedUser.getLastName());
    }

    @Test
    public void updateUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.updateUser(1, new User());
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }

    @Test
    public void getUserActivities_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        List<Activity> activities = Arrays.asList(new Activity(1, "Activity1", "2", 1));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
                eq(new ParameterizedTypeReference<List<Activity>>() {})))
                .thenReturn(ResponseEntity.ok(activities));

        List<Activity> response = userServiceClass.getUserActivities(1);
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    public void getUserActivities_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.getUserActivities(1);
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }

    @Test
    public void saveUserActivity_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        Activity activity = new Activity(0, "New Activity", "1", 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.postForEntity(anyString(), any(), eq(Activity.class)))
                .thenReturn(ResponseEntity.ok(activity));

        Activity savedActivity = userServiceClass.saveUserActivity(activity, 1);
        assertNotNull(savedActivity);
        assertEquals(activity, savedActivity);
    }

    @Test
    public void saveUserActivity_UserNotFound() {
        Activity activity = new Activity(0, "New Activity", "1", 1);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.saveUserActivity(activity, 1);
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }

    @Test
    public void deleteUserActivities_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), isNull(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Deleted"));

        String response = userServiceClass.deleteUserActivities(1, 1);
        assertEquals("Deleted", response);
    }

    @Test
    public void deleteUserActivities_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.deleteUserActivities(1, 1);
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }

    @Test
    public void updateUserActivity_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        Activity activity = new Activity(1, "Updated Activity", "2", 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Activity.class)))
                .thenReturn(ResponseEntity.ok(activity));

        Activity updatedActivity = userServiceClass.updateUserActivity(1, 1, activity);
        assertNotNull(updatedActivity);
        assertEquals(activity, updatedActivity);
    }

    @Test
    public void updateUserActivity_UserNotFound() {
        Activity activity = new Activity(1, "Updated Activity", "2", 1);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.updateUserActivity(1, 1, activity);
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }

    @Test
    public void getUserActivity_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        Activity activity = new Activity(1, "Existing Activity", "2", 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), eq(Activity.class)))
                .thenReturn(ResponseEntity.ok(activity));

        Activity response = userServiceClass.getUserActivity(1, 1);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void getUserActivity_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.getUserActivity(1, 1);
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }
}
