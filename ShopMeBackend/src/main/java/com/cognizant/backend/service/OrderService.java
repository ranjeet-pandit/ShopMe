package com.cognizant.backend.service;

import com.cognizant.backend.payload.CheckoutItemDto;
import com.cognizant.backend.payload.ProductDto;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;

public interface OrderService {

    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException;
}
