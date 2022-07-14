package com.cognizant.backend.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CheckoutItemDto {

    private String name;
    private long id;
    private double price;
    private  int quantity;

}
