package com.onlinebookingsystem.airbnbsystem.controller;


import com.onlinebookingsystem.airbnbsystem.advice.ResponseResultBody;
import com.onlinebookingsystem.airbnbsystem.dto.HotelDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelInfoDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelPriceDto;
import com.onlinebookingsystem.airbnbsystem.dto.HotelSearchRequest;
import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.service.HotelService;
import com.onlinebookingsystem.airbnbsystem.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@ResponseResultBody
@RequestMapping("/hotels")
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest)
    {
       Page<HotelPriceDto> page= inventoryService.searchHotels(hotelSearchRequest);
       return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId)
    {
        return  ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
