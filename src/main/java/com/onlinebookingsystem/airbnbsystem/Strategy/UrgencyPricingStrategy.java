package com.onlinebookingsystem.airbnbsystem.Strategy;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal oldOne = wrapped.calculatePrice(inventory);
        LocalDate today = LocalDate.now();
        if( !inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7)))
        {
            return oldOne.multiply(BigDecimal.valueOf(1.15));
        }
        return oldOne;
    }
}
