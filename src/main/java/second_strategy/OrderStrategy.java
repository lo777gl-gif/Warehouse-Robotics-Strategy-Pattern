package second_strategy;

import model.Order;

import java.util.List;

// One way to decide in which sequence waiting orders are handled. 
public interface OrderStrategy {
    // Returns a new list with the same orders in the new sequence. The given list stays unchanged. 
    List<Order> arrange(List<Order> orders);

    String name();
}
