package com.onlinebookingsystem.airbnbsystem.Strategy;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



@RequiredArgsConstructor
public class SurgePricingStrategy implements PricingStrategy{

   private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal oldOne =  wrapped.calculatePrice(inventory);
        return oldOne.multiply(inventory.getSurgeFactor());
    }
}
