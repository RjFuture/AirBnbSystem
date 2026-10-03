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

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the Hotel with Id ,{}", id);
        Hotel hotel = hotelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id :"+id));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        return modelMapper.map(hotel,HotelDto.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        boolean exists = hotelRepository.existsById(id);
        if(!exists) throw new ResourceNotFoundException("Hotel not found with Id: "+id);

        hotelRepository.deleteById(id);
        //todo : delete the future inventories for this hotel

    }
}
