package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.BookingDto;
import com.onlinebookingsystem.airbnbsystem.dto.BookingRequestDto;
import org.springframework.http.ResponseEntity;


public interface BookingService {

    BookingDto initialiseBooking( BookingRequestDto bookingRequestDto);
}
