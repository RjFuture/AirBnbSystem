package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import com.onlinebookingsystem.airbnbsystem.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;

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
    public void deleteFutureInventories(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByDateAfterAndRoom(today.minusDays(1),room);
    }
}
