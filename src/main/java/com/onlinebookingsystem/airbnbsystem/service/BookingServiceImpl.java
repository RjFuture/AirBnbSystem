package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.BookingDto;
import com.onlinebookingsystem.airbnbsystem.dto.BookingRequestDto;
import com.onlinebookingsystem.airbnbsystem.dto.GuestDto;
import com.onlinebookingsystem.airbnbsystem.exceptions.ResourceNotFoundException;
import com.onlinebookingsystem.airbnbsystem.exceptions.UnAuthorisedException;
import com.onlinebookingsystem.airbnbsystem.models.*;
import com.onlinebookingsystem.airbnbsystem.models.enums.BookingStatus;
import com.onlinebookingsystem.airbnbsystem.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private final GuestRepository guestRepository;

    private  final CheckoutService checkoutService;

    @Value("${frontend.url}")
    private String frontendUrl;

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

       long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate()
               ,bookingRequestDto.getCheckOutDate())+1;

       if(inventoryList.size()!=daysCount)
       {
           throw new IllegalStateException("Room is not available anymore");
       }

       //Reserve the room/update the booked count of inventories

        for(Inventory inventory : inventoryList)
        {
            inventory.setReservedCount( inventory.getBookedCount() + bookingRequestDto.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        //create the booking


        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequestDto.getRoomsCount())
                .amount(BigDecimal.valueOf(100))
                .build();

       Booking savedBooking =  bookingRepository.save(booking);

       return modelMapper.map(savedBooking,BookingDto.class);



    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {

        log.info("Adding guests for booking with id : {} ", bookingId);

        Booking booking = bookingRepository.findById(bookingId).
                orElseThrow(() -> new ResourceNotFoundException("Booking not found with this Id : " + bookingId));

        User user = getCurrentUser();

        if(!user.getId().equals(booking.getUser().getId()))
        {
            throw new UnAuthorisedException("Booking does not belong to this user with Id :" +user.getId());
        }

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has already expired");
        }


        if (booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not under reserved state,cannot add guests");
        }

        for (GuestDto guestDto : guestDtoList) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(getCurrentUser());
           guest = guestRepository.save(guest);
           booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);
    }

    @Override
    public String initiatePayments(Long bookingId) {
        Booking booking =bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found for the id :"+bookingId)
        );

        User user = getCurrentUser();

        if(!user.getId().equals(booking.getUser().getId()))
        {
            throw new UnAuthorisedException("Booking does not belong to this user with Id :" +user.getId());
        }

        if(hasBookingExpired(booking))
        {
            throw new IllegalStateException("Booking has already expired");
        }
        // todo :- have to create checkoutService

       String sessionUrl =  checkoutService.getCheckoutSession(booking,frontendUrl+"/payments/success",frontendUrl+"/payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);
        return  sessionUrl;
    }

    private boolean hasBookingExpired(Booking booking)
        {
            return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
        }

        private User getCurrentUser()
        {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }



}
