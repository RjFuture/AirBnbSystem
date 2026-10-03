package com.onlinebookingsystem.airbnbsystem.controller;


import com.onlinebookingsystem.airbnbsystem.dto.HotelDto;
import com.onlinebookingsystem.airbnbsystem.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@Slf4j
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto>  createNewHotel(@RequestBody HotelDto hotelDto)
    {
        log.info("Attemping to create a new Hotel with name ,{}",hotelDto.getName());
        HotelDto hotel = hotelService.createNewHotel(hotelDto);
        return  new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long hotelId)
    {
        HotelDto hotelDto = hotelService.getHotelById(hotelId);
        return  ResponseEntity.ok(hotelDto);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long hotelId, @RequestBody HotelDto hotelDto)
    {
       HotelDto hotel =  hotelService.updateHotelById(hotelId,hotelDto);

       return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId)
    {
       hotelService.deleteHotelById(hotelId);
       return  ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotel(@PathVariable("hotelId") Long Id)
    {
        hotelService.activateHotel(Id);
        return ResponseEntity.noContent().build();
    }
}
