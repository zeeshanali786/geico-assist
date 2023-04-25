package com.geico.claim.roadsideassistance.seed;

import com.geico.claim.roadsideassistance.model.Assistant;
import com.geico.claim.roadsideassistance.model.Customer;
import com.geico.claim.roadsideassistance.model.Geolocation;
import com.geico.claim.roadsideassistance.service.RoadsideAssistanceService;

public class TestHelper {

    static  public Assistant CreateAssistant(String name){
       return new Assistant(name,CreateDefaultGeolocation());
    }
    static public Geolocation CreateDefaultGeolocation(){
       return new Geolocation(0,0);
    }

    static public Customer CreateCustomer(String name){
        return new Customer(name);
    }
    static public void createSevenAssistants(RoadsideAssistanceService assistService){
        assistService.updateAssistantLocation(
                CreateAssistant("Better Times Roadside Service LLC"),
                new Geolocation(40.986656,-93.258133));

        assistService.updateAssistantLocation(
                CreateAssistant("All Seasons Towing"),
                new Geolocation(42.986656,-93.258133));

        assistService.updateAssistantLocation(
                CreateAssistant("Preferred Vendor"),
                new Geolocation(43.986656,-93.258133));

        assistService.updateAssistantLocation(
                CreateAssistant("Zakir"),
                new Geolocation(46.986656,-93.258133));

        assistService.updateAssistantLocation(
                CreateAssistant("Paul"),
                new Geolocation(47.986656,-93.258133));

        assistService.updateAssistantLocation(
                CreateAssistant("Zee"),
                new Geolocation(48.986656,-98.258133));

        Assistant vendorNotOnWork  = CreateAssistant("Vendor not on Work");
        vendorNotOnWork.setOutOfWorkingHours(true);

        assistService.updateAssistantLocation(
                vendorNotOnWork, new Geolocation(49.986656,-99.258133));
    }
}
