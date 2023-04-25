package com.geico.claim.roadsideassistance.service;

import com.geico.claim.roadsideassistance.service.exception.InvalidRequestException;
import com.geico.claim.roadsideassistance.model.Assistant;
import com.geico.claim.roadsideassistance.model.Customer;
import com.geico.claim.roadsideassistance.model.Geolocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.SortedSet;

/**
 *This class implement the RoadsideAssistanceService interface and provide the behaviour of
 * common Roadside assistance functionality
 * This class will only surface and bubble up the exceptions, since this class does not know what to do with errors
 * and is expecting the client(Rest API, or any such client) will know better, and should
 * handle the custom exceptions like InvalidRequestException and AssistantNotAvailableException,
 * and create error payload and notify the upstreams and also manage the logging and instrumentation of those
 * errors and normal flow for business and application metrics,alerts etc.
 */
@Service
public class RoadsideAssistanceServiceImpl implements RoadsideAssistanceService {
    private final AssistantManager assistantManager;

    @Autowired
    public RoadsideAssistanceServiceImpl(AssistantManager assistantManager) {
        this.assistantManager = assistantManager;
    }

    @Override
    public void updateAssistantLocation(Assistant assistant, Geolocation assistantLocation) {
        if (assistant == null || assistantLocation == null) {
            throw new InvalidRequestException("Invalid update request. Assistant and assistant location must not be null.");
        }
        this.assistantManager.updateAssistantLocation(assistant, assistantLocation);
    }

    @Override
    public SortedSet<Assistant> findNearestAssistants(Geolocation geolocation, int limit) {
        if (geolocation == null || limit <= 0) {
            throw new InvalidRequestException("Invalid find request. Geolocation must not be null and limit must be greater than zero.");
        }
        return this.assistantManager.findNearestAssistants(geolocation, limit);
    }

    @Override
    public Optional<Assistant> reserveAssistant(Customer customer, Geolocation customerLocation) {
        if (customer == null || customerLocation == null) {
            throw new InvalidRequestException("Invalid reserve request. Customer and customer location must not be null.");
        }
       return this.assistantManager.reserveAssistant(customer, customerLocation);

    }

    @Override
    public void releaseAssistant(Customer customer, Assistant assistant) {
        if (customer == null || assistant == null) {
            throw new InvalidRequestException("Invalid release request. Customer and assistant must not be null.");
        }
         this.assistantManager.releaseAssistant(customer, assistant);
    }
}
