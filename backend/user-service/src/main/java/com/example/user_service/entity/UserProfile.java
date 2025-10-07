package com.example.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profile")
@Builder
@Data
public class UserProfile {

    @Id @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    private String address;
    private String gender;
    private LocalDate dob;
    private String phone_number;
    private String full_name;

}
