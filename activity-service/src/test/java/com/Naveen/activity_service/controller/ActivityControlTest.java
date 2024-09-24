package com.Naveen.activity_service.controller;


import com.Naveen.activity_service.entity.Activity;
import com.Naveen.activity_service.service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActivityControlTest {
    @InjectMocks
    private ActivityControl activityControl;

    @Mock
    private ActivityService activityService;
    @Test
    public void findAllActivity_Success() {
        Activity activity1 = new Activity(1, "Activity 1", "1", 1);
        Activity activity2 = new Activity(2, "Activity 2", "2", 1);
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityService.findAllActivity()).thenReturn(activities);

        List<Activity> response = activityControl.findAllActivity();
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(activity1, response.get(0));
        assertEquals(activity2, response.get(1));
    }

    @Test
    public void findById_Success() {
        Activity activity = new Activity(1, "Activity 1", "1", 1);
        when(activityService.findActivityById(1)).thenReturn(activity);

        Activity response = activityControl.findById(1);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void saveActivity_Success() {
        Activity activity = new Activity(0, "New Activity", "1", 1);
        when(activityService.saveActivity(activity)).thenReturn(activity);

        Activity response = activityControl.saveActivity(activity);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void deleteActivity_Success() {
        int activityId = 1;
        doNothing().when(activityService).deleteActivity(activityId);

        String response = activityControl.deleteActivity(activityId);
        assertEquals("Activity with id: 1 deleted", response);
    }

    @Test
    public void findByUserId_Success() {
        int userId = 1;
        Activity activity1 = new Activity(1, "Activity 1", "1", userId);
        List<Activity> activities = List.of(activity1);
        when(activityService.findByUserId(userId)).thenReturn(activities);

        List<Activity> response = activityControl.findByUserId(userId);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(activity1, response.get(0));
    }

    @Test
    public void updateActivity_Success() {
        Activity activity = new Activity(1, "Updated Activity", "2", 1);
        when(activityService.updateActivity(activity)).thenReturn(activity);

        Activity response = activityControl.updateActivity(activity);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void deleteActivityByUserId_Success() {
        int userId = 1;
        doNothing().when(activityService).deleteActivityByUserId(userId);

        String response = activityControl.deleteActivityByUserId(userId);
        assertEquals("Activities with userId : 1 Deleted", response);
    }
}
