package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.HotelDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelInfoDto;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void  deleteHotelById(Long id);

    void activateHotel(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
