package com.onlinebookingsystem.airbnbsystem.repository;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    void deleteByDateAfterAndRoom(LocalDate date, Room room);
}
