package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.HotelDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelPriceDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelSearchRequest;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
