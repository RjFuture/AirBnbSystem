package com.onlinebookingsystem.airbnbsystem.dto;

import com.onlinebookingsystem.airbnbsystem.models.HotelContactInfo;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// todo : -  While further we proceed we break it into HotelRequestDto and Hotel ResponseDto
@Data
public class HotelDto {


    private Long id;
    private String name;
    private String city;
    private List<String> photos = new ArrayList<>();
    private List<String> amenities = new ArrayList<>();
    private HotelContactInfo hotelContactInfo;
    private Boolean active;
}
