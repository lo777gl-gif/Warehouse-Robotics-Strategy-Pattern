import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import model.Order;
import org.junit.jupiter.api.Test;
import second_strategy.ArrivalOrder;
import second_strategy.SameShelfTogether;
import second_strategy.SmallestFirst;

// One section per order strategy. The orders arrive in a mixed sequence on purpose. 
class OrderStrategiesTest {
    private final Order o1 = new Order("O1", "BOX", 30, 1);
    private final Order o2 = new Order("O2", "PALLET", 5, 2);
    private final Order o3 = new Order("O3", "BOX", 12, 3);
    private final Order o4 = new Order("O4", "TOOLS", 8, 4);
    private final Order o5 = new Order("O5", "PALLET", 25, 5);
    private final List<Order> shuffled = List.of(o4, o1, o5, o3, o2);

    private List<String> ids(List<Order> orders) { return orders.stream().map(Order::id).toList(); }

    // ----- second_strategy.ArrivalOrder -----
    @Test void arrivalOrder_sortsByArrivalTime() {
        assertEquals(List.of("O1", "O2", "O3", "O4", "O5"), ids(new ArrivalOrder().arrange(shuffled)));
    }

    // ----- second_strategy.SmallestFirst -----
    @Test void smallestFirst_sortsByQuantity() {
        assertEquals(List.of("O2", "O4", "O3", "O5", "O1"), ids(new SmallestFirst().arrange(shuffled)));
    }

    @Test void smallestFirst_equalQuantitiesKeepArrivalSequence() {
        Order late = new Order("LATE", "BOX", 5, 9);
        assertEquals(List.of("O2", "LATE"), ids(new SmallestFirst().arrange(List.of(late, o2))));
    }

    // ----- second_strategy.SameShelfTogether -----
    @Test void sameShelfTogether_putsOrdersOfOneSkuNextToEachOther() {
        // BOX appears first (O1), then PALLET (O2), then TOOLS (O4); inside a group the arrival sequence stays
        assertEquals(List.of("O1", "O3", "O2", "O5", "O4"), ids(new SameShelfTogether().arrange(shuffled)));
    }

    // ----- shared -----
    @Test void emptyQueueGivesEmptyResultInEveryStrategy() {
        assertTrue(new ArrivalOrder().arrange(List.of()).isEmpty());
        assertTrue(new SmallestFirst().arrange(List.of()).isEmpty());
        assertTrue(new SameShelfTogether().arrange(List.of()).isEmpty());
    }

    @Test void everyStrategyHasAReadableName() {
        assertEquals("Arrival order", new ArrivalOrder().name());
        assertEquals("Smallest first", new SmallestFirst().name());
        assertEquals("Same shelf together", new SameShelfTogether().name());
    }
}
