package com.onlinebookingsystem.airbnbsystem.Strategy;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal oldPrice = wrapped.calculatePrice(inventory);
        boolean isInventoryDayHoliday = true; //call an api
        if(isInventoryDayHoliday)
        {
            return oldPrice.multiply(BigDecimal.valueOf(1.25));
        }
        return oldPrice;
    }
}
