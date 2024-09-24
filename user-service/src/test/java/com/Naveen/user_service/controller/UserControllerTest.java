package com.Naveen.user_service.controller;

import com.Naveen.user_service.entity.Activity;
import com.Naveen.user_service.entity.User;
import com.Naveen.user_service.exception.UserNotFoundException;
import com.Naveen.user_service.service.UserService;
import com.Naveen.user_service.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsers_Success() {
        User user1 = new User(1, "Naveen", "Ganji", "naveenganji@gmail.com");
        User user2 = new User(2, "Bhavya", "Ganji", "bhavyaganji@gmail.com");
        List<User> userList = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(userList);

        List<User> response = userController.getUsers();
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(user1, response.get(0));
        assertEquals(user2, response.get(1));
    }
    @Test
    public void getUsers_NoUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        List<User> response = userController.getUsers();
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    @Test
    public void getUserById_Success() {
        User user = new User(1, "Naveen", "Ganji", "naveenganji@gmail.com");
        when(userService.getUserById(1)).thenReturn(user);

        User response = userController.getUserById(1);
        assertNotNull(response);
        assertEquals(user, response);
    }

    @Test
    public void getUserById_NotFound() {
        when(userService.getUserById(anyInt())).thenReturn(null);

        User response = userController.getUserById(999);
        assertNull(response);
    }

    @Test
    public void saveUser_Success() {
        User user = new User(0, "NewUser", "LastName", "newuser@example.com");
        User savedUser = new User(1, "NewUser", "LastName", "newuser@example.com");
        when(userService.saveUser(user)).thenReturn(savedUser);

        User response = userController.saveUser(user);
        assertNotNull(response);
        assertEquals(savedUser, response);
    }

    @Test
    public void saveUser_InvalidInput() {
        User invalidUser = new User(0, "", "", ""); // Invalid data
        doThrow(new IllegalArgumentException("Invalid user data")).when(userService).saveUser(invalidUser);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.saveUser(invalidUser);
        });
        assertEquals("Invalid user data", exception.getMessage());
    }

    @Test
    public void updateUser_Success() {
        User user = new User(1, "UpdatedUser", "LastName", "updateduser@example.com");
        when(userService.saveUser(user)).thenReturn(user);

        User response = userController.updateUser(user);
        assertNotNull(response);
        assertEquals(user, response);
    }

    @Test
    public void updateUser_InvalidId() {
        User user = new User(999, "NonExistentUser", "LastName", "nonexistent@example.com");
        when(userService.saveUser(user)).thenReturn(null); // simulate user not found

        User response = userController.updateUser(user);
        assertNull(response);
    }

    @Test
    public void deleteUser_Success() {
        int userId = 1;
        String response = userController.deleteUser(userId);
        assertEquals(Constant.DELETE_MSG + userId, response);
        verify(userService).deleteUser(userId);
    }

    @Test
    public void deleteUser_NonExistentId() {
        int nonExistentUserId = 999;
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(nonExistentUserId);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.deleteUser(nonExistentUserId);
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void getUserActivities_Success() {
        Activity activity1 = new Activity(1, "push-up", "2.00", 1);
        Activity activity2 = new Activity(2, "spin", "1.45", 1);
        List<Activity> activities = Arrays.asList(activity1, activity2);
        when(userService.getUserActivities(1)).thenReturn(activities);

        List<Activity> response = userController.getUserActivities(1);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(activity1, response.get(0));
        assertEquals(activity2, response.get(1));
    }

    @Test
    public void getUserActivities_NoActivities() {
        when(userService.getUserActivities(1)).thenReturn(Collections.emptyList());

        List<Activity> response = userController.getUserActivities(1);
        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    public void saveUserActivity_Success() {
        Activity activity = new Activity(1, "push-up", "2.00", 1);
        when(userService.saveUserActivity(activity, 1)).thenReturn(activity);

        Activity response = userController.saveUserActivity(1, activity);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void saveUserActivity_InvalidData() {
        Activity invalidActivity = new Activity(0, "", "", 1); // invalid data
        doThrow(new IllegalArgumentException("Invalid activity data")).when(userService).saveUserActivity(invalidActivity, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.saveUserActivity(1, invalidActivity);
        });
        assertEquals("Invalid activity data", exception.getMessage());
    }

    @Test
    public void getUserActivity_Success() {
        Activity activity = new Activity(1, "push-up", "2.00", 1);
        when(userService.getUserActivity(1, 1)).thenReturn(activity);

        Activity response = userController.getUserActivity(1, 1);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void getUserActivity_NonExistentActivity() {
        when(userService.getUserActivity(1, 999)).thenReturn(null);

        Activity response = userController.getUserActivity(1, 999);
        assertNull(response);
    }

    @Test
    public void updateUserActivity_Success() {
        Activity activity = new Activity(1, "push-up", "2.00", 1);
        when(userService.updateUserActivity(1, 1, activity)).thenReturn(activity);

        Activity response = userController.updateUserActivity(1, 1, activity);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void updateUserActivity_NonExistentActivity() {
        Activity activity = new Activity(999, "NonExistentActivity", "2.00", 1);
        when(userService.updateUserActivity(1, 999, activity)).thenThrow(new UserNotFoundException("Activity not found"));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userController.updateUserActivity(1, 999, activity);
        });
        assertEquals("Activity not found", exception.getMessage());
    }

    @Test
    public void deleteUserActivities_Success() {
        int userId = 1;
        int activityId = 1;
        String expectedMessage = Constant.DELETE_MSG + activityId;
        when(userService.deleteUserActivities(userId, activityId)).thenReturn(expectedMessage);

        String response = userController.deleteUserActivities(userId, activityId);

        assertEquals(expectedMessage, response);
        verify(userService).deleteUserActivities(userId, activityId);
    }

    @Test
    public void patchUser_Success() {
        User user = new User(1, "PatchedUser", "LastName", "patcheduser@example.com");
        when(userService.updateUser(1, user)).thenReturn(user);

        User response = userController.patchUser(1, user);
        assertNotNull(response);
        assertEquals(user, response);
    }

    @Test
    public void patchUser_NonExistentUser() {
        User user = new User(999, "NonExistentUser", "LastName", "nonexistent@example.com");
        when(userService.updateUser(anyInt(), any(User.class))).thenThrow(new UserNotFoundException("User not found"));

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.patchUser(999, user);
        });
        assertEquals("User not found", exception.getMessage());
    }
    @Test
    public void deleteUserActivities_NonExistentActivity() {
        int userId = 1;
        int nonExistentActivityId = 999;
        doThrow(new UserNotFoundException("Activity not found")).when(userService).deleteUserActivities(userId, nonExistentActivityId);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userController.deleteUserActivities(userId, nonExistentActivityId);
        });
        assertEquals("Activity not found", exception.getMessage());
    }
    @Test
    public void updateUser_NonExistentUser() {
        User user = new User(999, "NonExistentUser", "LastName", "nonexistent@example.com");
        when(userService.saveUser(user)).thenReturn(null); // simulate user not found

        User response = userController.updateUser(user);
        assertNull(response);
    }

}
