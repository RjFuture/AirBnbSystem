package com.onlinebookingsystem.airbnbsystem.controller;


import com.onlinebookingsystem.airbnbsystem.advice.ResponseResultBody;
import com.onlinebookingsystem.airbnbsystem.dto.RoomDto;
import com.onlinebookingsystem.airbnbsystem.repository.RoomRepository;
import com.onlinebookingsystem.airbnbsystem.service.RoomService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Room Admin")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@ResponseResultBody
public class RoomAdminController {

   private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto)
    {
       RoomDto room = roomService.createNewRoom(hotelId,roomDto);
       return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId)
    {
        return  ResponseEntity.ok(roomService.getAllRoomsInHotel(hotelId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId)
    {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long hotelId, @PathVariable Long roomId)
    {
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}
