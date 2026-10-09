package com.onlinebookingsystem.airbnbsystem.service;


import com.onlinebookingsystem.airbnbsystem.Strategy.PricingService;
import com.onlinebookingsystem.airbnbsystem.dto.HotelPriceDto;
import com.onlinebookingsystem.airbnbsystem.models.Hotel;
import com.onlinebookingsystem.airbnbsystem.models.HotelMinPrice;
import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import com.onlinebookingsystem.airbnbsystem.repository.HotelMinPriceRepository;
import com.onlinebookingsystem.airbnbsystem.repository.HotelRepository;
import com.onlinebookingsystem.airbnbsystem.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PricingUpdateService {

    //Scheduler to update the inventory and HotelsMinPrice Tables every Hour
    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0 0 * * * *")
    public void updatePrices()
    {
        int page =0;
        int batchSize = 100;
        
        while(true)
        {
            Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page,batchSize));
            if(hotelPage.isEmpty()) break;
            
            // update HotelPrices 
            hotelPage.getContent().forEach(this::updateHotelPrices);
            
            page++;
        }
    }

    private void updateHotelPrices(Hotel hotel) {

        log.info("Updating Hotel Prices for Hotel with Id : {}", hotel.getId());
        
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);
        
       List<Inventory> inventoryList =  inventoryRepository.findByHotelAndDateBetween(hotel,startDate,endDate);
       updateInventoryPrices(inventoryList);
       
       updateHotelMinPrice(hotel,inventoryList,startDate,endDate);
    }

    private void updateHotelMinPrice(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {

        Map<LocalDate, BigDecimal> dailyMinimumPrice = inventoryList.stream()
                .collect(Collectors.groupingBy(Inventory::getDate,Collectors.
                        mapping(Inventory::getPrice,Collectors.minBy(Comparator.naturalOrder()))))
                .entrySet().stream().
                collect(Collectors.toMap(Map.Entry::getKey, f -> f.getValue().orElse(BigDecimal.ZERO)));


        //prepare HotelPrice entities in build
        List<HotelMinPrice> hotelPrices = new ArrayList<>();

        dailyMinimumPrice.forEach(((date, price) -> {
            HotelMinPrice hotelPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date)
                    .orElse(new HotelMinPrice(hotel,date));
            hotelPrice.setPrice(price);
            hotelPrices.add(hotelPrice);
        }));

        hotelMinPriceRepository.saveAll(hotelPrices);

    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {

        inventoryList.forEach(inventory -> {
            BigDecimal dynamicPrice = pricingService.calculateDynamicPricing(inventory);
            inventory.setPrice(dynamicPrice);
        });
        inventoryRepository.saveAll(inventoryList);
    }



}
