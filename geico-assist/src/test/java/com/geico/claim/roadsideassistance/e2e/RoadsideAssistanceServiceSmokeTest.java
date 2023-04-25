package com.geico.claim.roadsideassistance.e2e;

import java.util.Optional;
import java.util.SortedSet;

import com.geico.claim.RoadsideAssistanceApplication;
import com.geico.claim.roadsideassistance.model.Assistant;
import com.geico.claim.roadsideassistance.model.Customer;
import com.geico.claim.roadsideassistance.model.Geolocation;
import com.geico.claim.roadsideassistance.seed.TestHelper;
import com.geico.claim.roadsideassistance.service.AssistantManager;
import com.geico.claim.roadsideassistance.service.RoadsideAssistanceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {RoadsideAssistanceApplication.class})
public class RoadsideAssistanceServiceSmokeTest {
    private final RoadsideAssistanceService roadsideAssistanceService;

    private  AssistantManager assistantManager;

    @Autowired
    public RoadsideAssistanceServiceSmokeTest(RoadsideAssistanceService roadsideAssistanceService, AssistantManager assistantManager) {
        this.roadsideAssistanceService = roadsideAssistanceService;
        this.assistantManager = assistantManager;
    }
    private Customer customer;

    @BeforeEach
    void setUp() throws Exception {
        TestHelper.createSevenAssistants(roadsideAssistanceService);

        customer = TestHelper.CreateCustomer("John Doe");
    }

    @AfterEach
    void cleanup() throws Exception {
        //release all assistants
        while(true )
        {
            if(!roadsideAssistanceService.reserveAssistant(customer
                    , new Geolocation(42.986656,-96.258133)).isPresent() ){
                break;
            }
        }
    }

    /**
     * This functional/smoke test will test the basic follow of following business requirement
     . Update location of a service provider
     • Return the 5 nearest service trucks ordered by ascending distance which are available to be reserved
     • Reserve a service provider for a customer
     . Return the 5 nearest service trucks excluding the one which are either reserved or not available
     • Release a service provider from a customer
     . Verify again the nearest service trucks availability
     */
    @Test
    void testFindNearestAssistantsSelectionBasedOnTheirDistanceAndAvailability() {
        SortedSet<Assistant> nearestAssistants= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(46.986656,-93.258133),5);
        assertEquals(5,nearestAssistants.size());
        assertEquals("Zakir",nearestAssistants.first().getName());
        assertEquals("All Seasons Towing",nearestAssistants.last().getName());

        Optional<Assistant> assistantReserved  = roadsideAssistanceService.reserveAssistant(customer
                , new Geolocation(46.986656,-96.258133));
        assertEquals("Zakir",assistantReserved.get().getName());

        SortedSet<Assistant> nearestAssistantsWhenZakirIsReserved= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(46.986656,-96.258133),5);
        assertEquals(5,nearestAssistantsWhenZakirIsReserved.size());
        assertEquals("Paul",nearestAssistantsWhenZakirIsReserved.first().getName());
        assertEquals("Better Times Roadside Service LLC",nearestAssistantsWhenZakirIsReserved.last().getName());

        roadsideAssistanceService.releaseAssistant(customer,assistantReserved.get());

        SortedSet<Assistant> nearestAssistantsWhenZakirIsReleased= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(46.986656,-96.258133),5);
        assertEquals(5,nearestAssistantsWhenZakirIsReleased.size());
        assertEquals("Zakir",nearestAssistantsWhenZakirIsReleased.first().getName());
        assertEquals("All Seasons Towing",nearestAssistantsWhenZakirIsReleased.last().getName());
    }


    @Test
    void testFindNearestAssistantsWithDifferentLimitAndReservedReleaseScenarios() {
        //try giving a limit of 7. it should return only 6 record as 7th is not available for work
        SortedSet<Assistant> nearestAssistants= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(42.986656,-93.258133),7);
        assertEquals(6,nearestAssistants.size());
        assertEquals("All Seasons Towing",nearestAssistants.first().getName());
        assertEquals("Zee",nearestAssistants.last().getName());

        //try giving a limit of 10, should still return 6
        nearestAssistants= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(42.986656,-93.258133),10);
        assertEquals(6,nearestAssistants.size());
        assertEquals("All Seasons Towing",nearestAssistants.first().getName());
        assertEquals("Zee",nearestAssistants.last().getName());

        //try giving a limit of 2, should still return 2
        nearestAssistants= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(42.986656,-93.258133),2);
        assertEquals(2,nearestAssistants.size());
        assertEquals("All Seasons Towing",nearestAssistants.first().getName());
        assertEquals("Preferred Vendor",nearestAssistants.last().getName());

        //should reserve All Seasons Towing
        Optional<Assistant> assistantReserved  = roadsideAssistanceService.reserveAssistant(customer
                , new Geolocation(42.986656,-96.258133));
        assertEquals("All Seasons Towing",assistantReserved.get().getName());

        //should reserve Preferred Vendor
        assistantReserved  = roadsideAssistanceService.reserveAssistant(customer
                , new Geolocation(42.986656,-96.258133));
        assertEquals("Preferred Vendor",assistantReserved.get().getName());

        //since two assistants are reserved and one is not on work so total it should return is total-3 -> 7-3 = 4
        nearestAssistants= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(42.986656,-93.258133),5);
        assertEquals(4,nearestAssistants.size());
        assertEquals("Better Times Roadside Service LLC",nearestAssistants.first().getName());
        assertEquals("Zee",nearestAssistants.last().getName());
    }

    @Test
    void testNegativeScenarios() {
        //try to reserve more than available assistant in system
        while(true )
        {
            if(!roadsideAssistanceService.reserveAssistant(customer
                    , new Geolocation(42.986656,-96.258133)).isPresent() ){
                break;
            }
        }

        Optional<Assistant> assistantReserved  =  roadsideAssistanceService.reserveAssistant(customer
                , new Geolocation(42.986656,-96.258133));
        assertEquals(false,assistantReserved.isPresent());

        // all reserved, should not return any assistant
        SortedSet<Assistant> nearestAssistants= roadsideAssistanceService.findNearestAssistants(
                new Geolocation(42.986656,-93.258133),5);
        assertEquals(0,nearestAssistants.size());
    }

    @Test
    void testConcurrency() {
        //if time permits
    }
}