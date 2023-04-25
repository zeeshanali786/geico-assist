package com.geico.claim.roadsideassistance.service;

import com.geico.claim.roadsideassistance.model.Assistant;
import com.geico.claim.roadsideassistance.model.Customer;
import com.geico.claim.roadsideassistance.model.Geolocation;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This class will encapsulate the state management of shared assistants.
 * Singleton pattern is implement to make sure even by mistake, client or services classes won't be
 * able to create multiple instances of  managed state of assistants
 * This Class is thread safe and designed for concurrent  access over assistants state
 */
//@Component
public class AssistantManager {
    //It should be added somewhere else like adr etc, but I put it here also as reviewer might not find adr.
    // I am choosing doubleLinkedList DS here for internal all assistants state management.
    // Reason for that is,so that we have optimized O(1) time complexity for write/Add
    // with SortedSet it would have taken O(LogN) for every write/add which we need in initial population and
    // also in updateAssistantLocation and releaseAssistant Methods because we required to remove and add elements so that
    // it maintain and balanced the order of binary balanced tree.
    private final ConcurrentLinkedDeque<Assistant> assistants = new ConcurrentLinkedDeque<>();

    private static volatile AssistantManager instance;

    private AssistantManager() {
        // private constructor to prevent instantiation of multiple instances.
        // That will make sure we have only single instance of assistants storage and state
    }

    public static AssistantManager getInstance() {
        if (instance == null) {
            //the synchronized block is used to  double-checked locking  to make sure only
            //single instance of the class is created even in a multi-threaded environment.
            synchronized (AssistantManager.class) {
                if (instance == null) {
                    instance = new AssistantManager();
                }
            }
        }
        return instance;
    }

    public void updateAssistantLocation(Assistant assistant, Geolocation assistantLocation) {
        assistants.remove(assistant);
        assistant.setLocation(assistantLocation);
        assistants.add(assistant);
    }

    public SortedSet<Assistant> findNearestAssistants(Geolocation geolocation, int limit) {
        // I personally don't see much value of SortedSet<Assistant> here, the normal which I am assuming use-case is,
        // this method will be called with different geolocation(different customer, different location right?)
        // so whatever the order/sort we have maintained, won't be reused and every time findNearestAssistants method is called,
        // it is going to require, one complete iteration of all assistants,
        // and then adding them one ny one in SortedSet, and time complexity of this is going to be NlogN. and if we have used any other
        // sequential data structure, and apply sort on it and then picked top n elements would have ended up with same NlogN
        // It would have added more value of SortedSet usage if we have a multiple reads use-case from a dataset. Like a stream of data coming
        // and the same data, based on some order is required to be read all the time. Here in our case, order require to  be changed
        // as different clients will always have different geolocation
        if (assistants.isEmpty()) {
            return new TreeSet<>();
        }

        SortedSet<Assistant> nearestAssistants = new TreeSet<>
                (Comparator.comparingDouble(a -> a.getLocation().distanceTo(geolocation)));

        // this iterator will help to filter and balance/sort the assistants tree based on customer location,
        assistants.stream()
                .filter(a -> a.getReservedFor() == null && !a.isOutOfWorkingHours())
                .forEach(a -> nearestAssistants.add(a));

        SortedSet<Assistant> nearestTopNAssistants = new TreeSet<>
                (Comparator.comparingDouble(a -> a.getLocation().distanceTo(geolocation)));
        // Now when all the assistants are balanced/sorted, we can pick the top N
        Iterator<Assistant> iterator = nearestAssistants.iterator();
        while (nearestTopNAssistants.size() < limit && iterator.hasNext()) {
            nearestTopNAssistants.add(iterator.next());
        }

        return nearestTopNAssistants;
    }

    public Optional<Assistant> reserveAssistant(Customer customer, Geolocation customerLocation) {
        SortedSet<Assistant> nearestAssistants = findNearestAssistants(customerLocation, 1);
        if (nearestAssistants.isEmpty()) {
            return Optional.empty();
        }
        Assistant assistant = nearestAssistants.first();
        assistants.remove(assistant);
        assistant.setReservedFor(customer);
        assistants.add(assistant);
        return Optional.of(assistant);
    }

    public void releaseAssistant(Customer customer, Assistant assistant) {
        assistants.remove(assistant);
        assistant.setReservedFor(null);
        assistants.add(assistant);
    }
}