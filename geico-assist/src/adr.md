ADR: Choosing Doubly Linked List for Internal State Management of Assistants

Decision: I have decided to use Doubly Linked List for internal state management of assistants in Roadside Assistance service.

Reasons:

1.  O(1) Time Complexity for Add/Write: We require an optimized O(1) time complexity for adding and writing new assistants as if  we  have a large number of assistants in our system. By using a Doubly Linked List, we can achieve this optimized time complexity for adding and writing new assistants.

2.  Limited Use of SortedSet: Although a SortedSet can be used for maintaining the order of assistants based on their geolocation, I do not see much value in using it in this particular scenario. The normal use-case is that the `findNearestAssistants` method will be called with different geolocations, because all customer will have mostly different locations and sort/order(nearestAssistant) for one customer will be different than the other, so the order/sort maintained in the SortedSet won't be reused. Therefore, every time the `findNearestAssistants` method is called, it will require one complete iteration of all assistants. Using a SortedSet would add time complexity of O(logN) for every write/add operation, which we need in initial population and in `updateAssistantLocation` and `releaseAssistant` methods because we need to remove and add elements to maintain the order of the binary balanced tree.

3.  Optimized Time Complexity for UpdateAssistant and ReleaseAssistant Operations: To optimized time complexity for updateAssitant and releaseAssistant operations I would  prefer Doubly Linked List, we can achieve O(1) time complexity for these operations, making them more efficient.

Consequences:

1.  Loss of Sorting: By using a Doubly Linked List, we will lose the sorting based on geolocation of assistants. However, we can achieve this ordering at the time of querying the nearest assistants based on a given geolocation by iterating over all assistants and sorting them based on their distance from the given geolocation.

3.  Limited Reusability: The Doubly Linked List is only useful for maintaining the internal state of assistants in our Roadside Assistance service. It may not be useful in other parts of the system thats why I keep SortedSet for findNearestAssistants Method