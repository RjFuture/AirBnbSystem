package com.onlinebookingsystem.airbnbsystem.dto;

import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//todo :- have to break in RoomRequestDto and RoomResponseDto
@Data
public class RoomDto {

    private Long id;

    private String type;
    private BigDecimal basePrice;

    private List<String> photos = new ArrayList<>();

    private List<String> amenities = new ArrayList<>();

    private Integer totalCount;

    private Integer capacity;

}
