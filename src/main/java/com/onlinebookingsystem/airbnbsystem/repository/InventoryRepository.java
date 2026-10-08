package com.onlinebookingsystem.airbnbsystem.repository;

import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import com.onlinebookingsystem.airbnbsystem.models.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    void deleteByRoom( Room room);



    @Query("""
select  Distinct i.hotel from Inventory i 
where i.city = :city AND i.date between :startDate AND :endDate
AND (i.totalCount - i.bookedCount)>=:roomsCount
AND i.closed = false 
group by i.hotel, i.room
having Count(*)= :dateCount
""")
    Page<Hotel> findHotelsWithAvailableInventory(@Param("city") String city
    , @Param("startDate") LocalDate startDate
    , @Param("endDate") LocalDate endDate
    , @Param("roomsCount") Integer roomsCount
    , @Param("dateCount") Long dateCount, Pageable pageable
     );
}
