package com.Naveen.activity_service.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private  int id;

    @Column(name="activity_description")
    private String activity;

    @Column(name="duration_hours")
    private String duration;

    @Column(name = "user_id")
    private int userId;
}
