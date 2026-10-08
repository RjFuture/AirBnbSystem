package com.onlinebookingsystem.airbnbsystem.repository;

import com.onlinebookingsystem.airbnbsystem.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking,Long> {
}
