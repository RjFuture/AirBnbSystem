package com.onlinebookingsystem.airbnbsystem.repository;

import com.onlinebookingsystem.airbnbsystem.dto.HotelPriceDto;
import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.models.HotelMinPrice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {



    @Query("""
select  new com.onlinebookingsystem.airbnbsystem.dto.HotelPriceDto(i.hotel,avg(i.price)) from HotelMinPrice i 
where i.hotel.city = :city AND i.date between :startDate AND :endDate
And i.hotel.active=true
group by i.hotel
having count(i.hotel)= :dateCount
""")
    Page<HotelPriceDto> findHotelsWithAvailableInventory(@Param("city") String city
            , @Param("startDate") LocalDate startDate
            , @Param("endDate") LocalDate endDate
            , @Param("roomsCount") Integer roomsCount
            , @Param("dateCount") Long dateCount, Pageable pageable
    );

    Optional<HotelMinPrice> findByHotelAndDate(Hotel hotel, LocalDate date);
}
