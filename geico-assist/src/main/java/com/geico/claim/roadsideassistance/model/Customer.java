package com.geico.claim.roadsideassistance.model;

import lombok.Data;

@Data
public class Customer {

    private String name;
    private String PolicyNumber;
    //For Future extension or if time permits
    //Todo: Vehicles
    //Todo: Benefits
    //Todo:Address
    //Todo:Contact/Address

    public Customer(String name){
        this.name = name;
    }
}
