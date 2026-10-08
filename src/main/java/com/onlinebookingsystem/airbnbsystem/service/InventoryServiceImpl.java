package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.HotelDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelSearchRequest;
import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import com.onlinebookingsystem.airbnbsystem.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAYear(Room room) {

        LocalDate today = LocalDate.now();
       LocalDate endDate = today.plusYears(1);

       for(;!today.isAfter(endDate);today = today.plusDays(1))
       {
           Inventory inventory = Inventory.builder()
                   .date(today).hotel(room.getHotel()).bookedCount(0)
                   .totalCount(room.getTotalCount()).room(room)
                   .price(room.getBasePrice()).surgeFactor(BigDecimal.ONE)
                   .city(room.getHotel().getCity()).closed(false)
                   .build();

           inventoryRepository.save(inventory);
       }

    }

    @Override
    public void deleteInventories(Room room) {
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest) {

        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(),hotelSearchRequest.getSize());

        Long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate())+1;

       Page<Hotel> hotelPage =  inventoryRepository.findHotelsWithAvailableInventory(hotelSearchRequest.getCity()
        ,hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate(),hotelSearchRequest.getRoomsCount(),
                dateCount,pageable);

      return hotelPage.map(hotel -> modelMapper.map(hotel,HotelDto.class));

    }
}
