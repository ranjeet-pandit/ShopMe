package com.cognizant.backend.controller;

import com.cognizant.backend.payload.CheckoutItemDto;
import com.cognizant.backend.payload.ProductDto;
import com.cognizant.backend.payload.StripeResponse;
import com.cognizant.backend.service.OrderService;
import com.stripe.exception.StripeException;
import com.stripe.model.Order;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(methods = RequestMethod.POST)
public class StripePaymentController {

    @Autowired
    public OrderService orderService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList)
    throws StripeException {
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId() , session.getUrl());
        return  new ResponseEntity<>(stripeResponse , HttpStatus.OK);
    }



}
