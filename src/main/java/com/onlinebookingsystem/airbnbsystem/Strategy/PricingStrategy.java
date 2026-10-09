package com.onlinebookingsystem.airbnbsystem.Strategy;


import com.onlinebookingsystem.airbnbsystem.models.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
