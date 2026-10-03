package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.HotelDto;
import com.onlinebookingsystem.airbnbsystem.exceptions.ResourceNotFoundException;
import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new Hotel with name : {}" , hotelDto.getName() );
       Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
       hotel.setActive(false);
      hotel =  hotelRepository.save(hotel);
     log.info("Created a new Hotel with Id : {}", hotel.getId());
      return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
     log.info("Getting the hotel with Id :{}",id);
        Hotel hotel = hotelRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Hotel with hotel id :"+ id + "Not found"));

        return modelMapper.map(hotel,HotelDto.class);
    }
}
