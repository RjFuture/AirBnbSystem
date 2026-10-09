package com.onlinebookingsystem.airbnbsystem.Strategy;

import com.onlinebookingsystem.airbnbsystem.models.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;



public class BasePricingStrategy implements PricingStrategy{
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
