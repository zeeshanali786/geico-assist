package com.geico.claim.roadsideassistance.service;

import com.geico.claim.roadsideassistance.seed.TestHelper;
import com.geico.claim.roadsideassistance.service.exception.AssistantNotAvailableException;
import com.geico.claim.roadsideassistance.service.exception.InvalidRequestException;
import com.geico.claim.roadsideassistance.model.Assistant;
import com.geico.claim.roadsideassistance.model.Customer;
import com.geico.claim.roadsideassistance.model.Geolocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class RoadsideAssistanceServiceImplTest {

    private RoadsideAssistanceServiceImpl roadsideAssistanceService;

    @Mock
    private AssistantManager assistantManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roadsideAssistanceService = new RoadsideAssistanceServiceImpl(assistantManager);
    }

    @Test
    void updateAssistantLocation_shouldThrowInvalidRequestException_whenAssistantOrLocationIsNull() {
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.updateAssistantLocation(null, new Geolocation(0.0, 0.0)));
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.updateAssistantLocation(TestHelper.CreateAssistant("abc"), null));
    }

    @Test
    void findNearestAssistants_shouldThrowInvalidRequestException_whenGeolocationIsNullOrLimitIsZero() {
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.findNearestAssistants(null, 10));
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.findNearestAssistants(new Geolocation(0.0, 0.0), 0));
    }

    @Test
    void reserveAssistant_shouldThrowInvalidRequestException_whenCustomerOrLocationIsNull() {
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.reserveAssistant(null, new Geolocation(0.0, 0.0)));
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.reserveAssistant(new Customer("ABC"), null));
    }

    @Test
    void reserveAssistant_shouldThrowAssistantNotAvailableException_whenAssistantNotAvailable() {
        Customer customer = new Customer("Best Customer");
        Geolocation customerLocation = new Geolocation(0.0, 0.0);
        when(assistantManager.reserveAssistant(any(Customer.class), any(Geolocation.class))).thenReturn(Optional.empty());

        assertTrue(roadsideAssistanceService.reserveAssistant(customer, customerLocation).isEmpty());
    }

    @Test
    void releaseAssistant_shouldThrowInvalidRequestException_whenCustomerOrAssistantIsNull() {
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.releaseAssistant(null, TestHelper.CreateAssistant("abc")));
        assertThrows(InvalidRequestException.class,
                () -> roadsideAssistanceService.releaseAssistant(new Customer("Best Customer"), null));
    }
}
