package com.Naveen.activity_service.service;


import com.Naveen.activity_service.entity.Activity;
import com.Naveen.activity_service.exception.ActivityNotFoundException;
import com.Naveen.activity_service.repository.ActivityRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceClassTest {

    @InjectMocks
    private ActivityServiceClass activityServiceClass;

    @Mock
    private ActivityRepository activityRepository;

    @Test
    public void findAllActivity_Success() {
        Activity activity1 = new Activity(1, "spin-task", "1", 1);
        Activity activity2 = new Activity(2, "rolling", "2", 1);
        Activity activity3 = new Activity(3, "push-ups", "1", 1);
        List<Activity> activities = Arrays.asList(activity1, activity2, activity3);

        when(activityRepository.findAll()).thenReturn(activities);

        List<Activity> response = activityServiceClass.findAllActivity();
        assertNotNull(response);
        assertEquals(3, response.size());
        assertEquals(activity1, response.get(0));
        assertEquals(activity2, response.get(1));
        assertEquals(activity3, response.get(2));
    }

    @Test
    public void findActivityById_Success() {
        Activity activity = new Activity(1, "spin-task", "1", 1);
        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        Activity response = activityServiceClass.findActivityById(1);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void findActivityById_NotFound() {
        when(activityRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ActivityNotFoundException.class, () -> {
            activityServiceClass.findActivityById(1);
        });

        assertEquals("Activity with id : 1 not found", exception.getMessage());
    }

    @Test
    public void saveActivity_Success() {
        Activity activity = new Activity(0, "push-ups", "2", 1);
        when(activityRepository.save(activity)).thenReturn(activity);

        Activity response = activityServiceClass.saveActivity(activity);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void deleteActivity_Success() {
        Activity activity = new Activity(1, "rolling", "1", 1);
        when(activityRepository.findById(1)).thenReturn(Optional.of(activity));

        activityServiceClass.deleteActivity(1);
        verify(activityRepository).deleteById(1);
    }

    @Test
    public void deleteActivity_NotFound() {
        when(activityRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ActivityNotFoundException.class, () -> {
            activityServiceClass.deleteActivity(1);
        });

        assertEquals("Activity with id : 1 not found", exception.getMessage());
    }

    @Test
    public void findByUserId_Success() {
        int userId = 1;
        Activity activity1 = new Activity(1, "spin-task", "1", userId);
        List<Activity> activities = List.of(activity1);
        when(activityRepository.findByUserId(userId)).thenReturn(activities);

        List<Activity> response = activityServiceClass.findByUserId(userId);
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(activity1, response.get(0));
    }

    @Test
    public void updateActivity_Success() {
        Activity activity = new Activity(1, "updated push-ups", "2", 1);
        when(activityRepository.save(activity)).thenReturn(activity);

        Activity response = activityServiceClass.updateActivity(activity);
        assertNotNull(response);
        assertEquals(activity, response);
    }

    @Test
    public void deleteActivityByUserId_Success() {
        int userId = 1;
        doNothing().when(activityRepository).deleteActivityByUserId(userId);

        activityServiceClass.deleteActivityByUserId(userId);
        verify(activityRepository).deleteActivityByUserId(userId);
    }
}
