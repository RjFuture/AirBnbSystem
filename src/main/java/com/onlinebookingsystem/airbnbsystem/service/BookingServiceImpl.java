package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.BookingDto;
import com.onlinebookingsystem.airbnbsystem.dto.BookingRequestDto;
import com.onlinebookingsystem.airbnbsystem.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;

    @Override
    public BookingDto initialiseBooking(BookingRequestDto bookingRequestDto) {

    }
}
