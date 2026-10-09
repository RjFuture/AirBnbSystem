package com.onlinebookingsystem.airbnbsystem.Strategy;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;


    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal oldPrice = wrapped.calculatePrice(inventory);
       double occupancyFraction =  (double)inventory.getBookedCount()/ inventory.getTotalCount();
       if(occupancyFraction>0.8)
       {
         return oldPrice.multiply(BigDecimal.valueOf(1.25));
       }
       return oldPrice;
    }
}
