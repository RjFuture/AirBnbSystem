package com.onlinebookingsystem.airbnbsystem.controller;


import com.onlinebookingsystem.airbnbsystem.advice.ResponseResultBody;
import com.onlinebookingsystem.airbnbsystem.dto.BookingDto;
import com.onlinebookingsystem.airbnbsystem.dto.BookingRequestDto;
import com.onlinebookingsystem.airbnbsystem.dto.GuestDto;
import com.onlinebookingsystem.airbnbsystem.service.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Bookings")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@ResponseResultBody
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initialiseBooking(@RequestBody BookingRequestDto bookingRequestDto)
    {
        return new ResponseEntity<>(bookingService.initialiseBooking(bookingRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList)
    {
        return ResponseEntity.ok(bookingService.addGuests(bookingId,guestDtoList));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String,String>> initiatePayment(@PathVariable Long bookingId)
    {
        String sessionUrl = bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("SessionUrl",sessionUrl));
    }
    

}
