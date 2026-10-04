package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.dto.RoomDto;
import com.onlinebookingsystem.airbnbsystem.exceptions.ResourceNotFoundException;
import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import com.onlinebookingsystem.airbnbsystem.repository.HotelRepository;
import com.onlinebookingsystem.airbnbsystem.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId,RoomDto roomDto) {
        log.info("Creating a new Room in Hotel with Id : {}", hotelId);
      Hotel hotel =   hotelRepository.findById(hotelId).
              orElseThrow(() -> new ResourceNotFoundException(("Hotel with hotel id :"+ hotelId + " Not found")));

      Room room= modelMapper.map(roomDto,Room.class);
      room.setHotel(hotel);
      room = roomRepository.save(room);

        if(hotel.getActive())
        {
            inventoryService.initializeRoomForAYear(room);
        }

      return modelMapper.map(room,RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all rooms int hotel with Id : {}",hotelId);
        Hotel hotel =   hotelRepository.findById(hotelId).
                orElseThrow(() -> new ResourceNotFoundException(("Hotel with hotel id :"+ hotelId + " Not found")));
        List<Room> rooms = hotel.getRooms();
        return rooms.stream().map(room -> modelMapper.map(room,RoomDto.class)).toList();
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting the room with Id : {}",roomId);
        Room room =   roomRepository.findById(roomId).
                orElseThrow(() -> new ResourceNotFoundException("Room not found with Id : "+roomId));

        return modelMapper.map(room,RoomDto.class);

    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Getting the room with Id : {}",roomId);
        Room room =   roomRepository.findById(roomId).
                orElseThrow(() -> new ResourceNotFoundException("Room not found with Id : "+roomId));


        inventoryService.deleteFutureInventories(room);
        roomRepository.deleteById(roomId);

    }
}
