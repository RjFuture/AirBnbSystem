package com.onlinebookingsystem.airbnbsystem.dto;

import com.onlinebookingsystem.airbnbsystem.models.User;
import com.onlinebookingsystem.airbnbsystem.models.enums.Gender;
import jakarta.persistence.*;

public class GuestDto {

    private Long id;
    private User user;

    private String name;

    private Gender gender;

    private Integer age;
}
