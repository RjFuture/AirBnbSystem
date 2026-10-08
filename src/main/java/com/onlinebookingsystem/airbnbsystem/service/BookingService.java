package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.BookingDto;
import com.onlinebookingsystem.airbnbsystem.dto.BookingRequestDto;
import com.onlinebookingsystem.airbnbsystem.dto.GuestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface BookingService {

    BookingDto initialiseBooking( BookingRequestDto bookingRequestDto);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
