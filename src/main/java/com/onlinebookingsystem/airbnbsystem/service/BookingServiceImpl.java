package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.BookingDto;
import com.onlinebookingsystem.airbnbsystem.dto.BookingRequestDto;
import com.onlinebookingsystem.airbnbsystem.exceptions.ResourceNotFoundException;
import com.onlinebookingsystem.airbnbsystem.models.*;
import com.onlinebookingsystem.airbnbsystem.models.enums.BookingStatus;
import com.onlinebookingsystem.airbnbsystem.repository.BookingRepository;
import com.onlinebookingsystem.airbnbsystem.repository.HotelRepository;
import com.onlinebookingsystem.airbnbsystem.repository.InventoryRepository;
import com.onlinebookingsystem.airbnbsystem.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequestDto bookingRequestDto) {


        log.info("Intialising booking for hotel : {} ,room : {} , date {} -- {} ", bookingRequestDto.getHotelId()
                ,bookingRequestDto.getRoomId(),bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId()).orElseThrow(
                () -> new ResourceNotFoundException("Hotel not found with id : "+bookingRequestDto.getHotelId())
        );

        Room room = roomRepository.findById(bookingRequestDto.getRoomId()).orElseThrow(
                () -> new ResourceNotFoundException("Room not found with id : "+bookingRequestDto.getRoomId())
        );

        if (!room.getHotel().getId().equals(hotel.getId())) {
            throw new IllegalStateException("Room does not belong to this hotel");
        }

       List<Inventory> inventoryList= inventoryRepository.findAndLockAvailableInventory(room.getId(),bookingRequestDto.getCheckInDate()
                ,bookingRequestDto.getCheckOutDate(),bookingRequestDto.getRoomsCount());

       long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(),bookingRequestDto.getCheckOutDate());

       if(inventoryList.size()!=daysCount)
       {
           throw new IllegalStateException("Room is not available anymore");
       }

       //Reserve the room/update the booked count of inventories

        for(Inventory inventory : inventoryList)
        {
            inventory.setBookedCount( inventory.getBookedCount() + bookingRequestDto.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        //create the booking

        User user = new User();
        user.setId(1L); // todo : remove dummy user

        //todo : amount dynamic pricing

        Booking booking = Booking.builder().
                bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkOutDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .user(user)
                .roomsCount(bookingRequestDto.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

       Booking savedBooking =  bookingRepository.save(booking);

       return modelMapper.map(savedBooking,BookingDto.class);



    }
}
