package com.Naveen.user_service.service;


import com.Naveen.user_service.entity.Activity;
import com.Naveen.user_service.entity.User;
import com.Naveen.user_service.exception.UserNotFoundException;
import com.Naveen.user_service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


public class ServiceClassTest {
    @InjectMocks
    private UserServiceClass userServiceClass;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsers_Success() {
        User user1 = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User user2 = new User(2, "Swarupa", "Ganji", "ganjiswarupa@gmail.com");
        List<User> userList = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userServiceClass.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userList, result);
    }

    @Test
    public void getUserById_Success() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userServiceClass.getUserById(1); // Call the method under test

        assertNotNull(result);
        assertEquals(user, result); // Verify that the returned user matches the expected user
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
    public void updateUser_NotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.updateUser(1, new User());
        });

        assertEquals("user with id : 1 not found", exception.getMessage());
    }

    @Test
    public void deleteUserActivities_Success() {
        // Arrange: Create a user and set up repository behavior
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), isNull(), eq(String.class)))
                .thenReturn(ResponseEntity.ok("Deleted"));

        // Act: Call the method under test
        String response = userServiceClass.deleteUserActivities(1, 1);
        // Assert: Check that the response is as expected
        assertEquals("Deleted", response);

        // Optional: Verify that the repository's delete method was called (if applicable)
        // verify(userRepository).deleteById(1);
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
    public void saveUser_InvalidInput() {
        User invalidUser = null; // Or create a user with invalid fields
        doThrow(new IllegalArgumentException("Invalid user data")).when(userRepository).save(invalidUser);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userServiceClass.saveUser(invalidUser);
        });

        assertEquals("Invalid user data", exception.getMessage());
    }
    @Test
    public void deleteUser_NonExistentId() {
        int nonExistentUserId = 999;
        doThrow(new UserNotFoundException("User with id ; " + nonExistentUserId + " not found"))
                .when(userRepository).findById(nonExistentUserId);

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.deleteUser(nonExistentUserId);
        });

        assertEquals("User with id ; 999 not found", exception.getMessage());
    }
    @Test
    public void saveUserActivity_InvalidData() {
        Activity invalidActivity = new Activity(0, "", "", 1); // Invalid data
        when(userRepository.findById(1)).thenReturn(Optional.of(new User()));

        doThrow(new IllegalArgumentException("Invalid activity data"))
                .when(restTemplate).postForEntity(anyString(), any(), eq(Activity.class));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userServiceClass.saveUserActivity(invalidActivity, 1);
        });

        assertEquals("Invalid activity data", exception.getMessage());
    }
    @Test
    public void getUserActivities_RestTemplateException() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(),
                eq(new ParameterizedTypeReference<List<Activity>>() {})))
                .thenThrow(new RuntimeException("RestTemplate Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userServiceClass.getUserActivities(1);
        });

        assertEquals("RestTemplate Error", exception.getMessage());
    }
    @Test
    public void updateUserActivity_RestTemplateException() {
        User user = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        Activity activity = new Activity(1, "Updated Activity", "2", 1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(), eq(Activity.class)))
                .thenThrow(new RuntimeException("RestTemplate Error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userServiceClass.updateUserActivity(1, 1, activity);
        });

        assertEquals("RestTemplate Error", exception.getMessage());
    }
    @Test
    public void saveUser_MissingFields() {
        User user = new User(0, null, null, null); // Invalid user
        doThrow(new IllegalArgumentException("User fields cannot be null")).when(userRepository).save(user);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userServiceClass.saveUser(user);
        });

        assertEquals("User fields cannot be null", exception.getMessage());
    }

    @Test
    public void deleteUserActivities_UserNotFoundForId() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.deleteUserActivities(1, 1);
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
    public void deleteUserActivities_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userServiceClass.deleteUserActivities(1, 1);
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

    @Test
    public void updateUser_Success() {
        User existingUser = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User updatedInfo = new User(1, "Naveen", "UpdatedLastName", "ganjinaveen@gmail.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedInfo);

        User updatedUser = userServiceClass.updateUser(1, updatedInfo);
        assertNotNull(updatedUser);
        assertEquals("UpdatedLastName", updatedUser.getLastName());
    }

    @Test
    public void updateUser_EmailOnly() {
        User existingUser = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User updatedInfo = new User(1, null, null, "newemail@gmail.com"); // Only email provided

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userServiceClass.updateUser(1, updatedInfo);

        assertNotNull(updatedUser);
        assertEquals("newemail@gmail.com", updatedUser.getEmail()); // Email should be updated
        assertEquals("Naveen", updatedUser.getFirstName()); // First name should remain unchanged
    }

        @Test
    public void updateUser_FirstNameOnly() {
        User existingUser = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User updatedInfo = new User(1, "UpdatedName", null, null);
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));

        User updatedUser = userServiceClass.updateUser(1, updatedInfo);
        assertNotNull(updatedUser);
        assertEquals("UpdatedName", updatedUser.getFirstName());
        assertEquals("Ganji", updatedUser.getLastName()); // Last name should remain unchanged
    }

    @Test
    public void updateUser_LastNameOnly() {
        User existingUser = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User updatedInfo = new User(1, null, "UpdatedLastName", null);
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));

        User updatedUser = userServiceClass.updateUser(1, updatedInfo);
        assertNotNull(updatedUser);
        assertEquals("UpdatedLastName", updatedUser.getLastName());
        assertEquals("ganjinaveen@gmail.com", updatedUser.getEmail()); // Email should remain unchanged
    }

    @Test
    public void updateUser_NothingToUpdate() {
        User existingUser = new User(1, "Naveen", "Ganji", "ganjinaveen@gmail.com");
        User updatedInfo = new User(1, null, null, null); // No fields to update
        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));

        User updatedUser = userServiceClass.updateUser(1, updatedInfo);
        assertNotNull(updatedUser);
        assertEquals("Naveen", updatedUser.getFirstName());
        assertEquals("Ganji", updatedUser.getLastName());
        assertEquals("ganjinaveen@gmail.com", updatedUser.getEmail());
    }
}
