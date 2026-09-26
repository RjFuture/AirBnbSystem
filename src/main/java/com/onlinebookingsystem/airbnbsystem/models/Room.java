package com.onlinebookingsystem.airbnbsystem.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// it is mainly telling Room type :- Delux , Suite, Normal like that
@Entity
@Getter
@Setter
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Many belogs to this Room and one to hotel so Many room of same Type one Hotel
    @JoinColumn(name = "hotel_id",nullable = false)
    private Hotel hotel;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal basePrice;

    @ElementCollection
    @CollectionTable(name = "room_photos",joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "photo_url")
    private List<String> photos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="room_amenities" ,joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer capacity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
