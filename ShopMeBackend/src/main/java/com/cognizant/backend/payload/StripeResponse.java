package com.cognizant.backend.payload;

import com.stripe.model.checkout.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StripeResponse {

    private String sessionId;
    private String sessionUrl;
}
