package com.geico.claim.roadsideassistance.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Assistant  {

    private String name;
    // Assistant current location
    private Geolocation location;
    //customer it is reserved for or null if it is not available for reservation
    private Customer reservedFor;
    //Not available to be reserved for any customer because he/she is not on work(Vacations, out of working schedule etc)
    //require some readOnly/AutoCalculated abstraction, Out of Scope for now
    private boolean outOfWorkingHours;
    private String cellNumber;

    //For Future consideration, out of scope for now
    //Todo: Ratings
    //Todo: Vendor Company
    //Todo:Address
    //Todo:Contract

    private Integer rating ;

    public Assistant(String name, Geolocation location) {
        this.name = name;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assistant assistant = (Assistant) o;
        return Objects.equals(name, assistant.name) && Objects.equals(location, assistant.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location);
    }
}
