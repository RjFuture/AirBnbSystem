package com.onlinebookingsystem.airbnbsystem.service;

import com.onlinebookingsystem.airbnbsystem.models.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking,String suceesUrl, String failureUrl);

}
