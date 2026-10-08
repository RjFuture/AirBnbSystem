package com.onlinebookingsystem.airbnbsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class HotelInfoDto {

    private HotelDto hotelDto;

    private List<RoomDto> rooms;
}
