import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SameShelfTogether implements OrderStrategy {
    @Override
    public List<Order> arrange(List<Order> orders) {
        List<Order> byArrival = new ArrayList<>(orders);
        byArrival.sort(Comparator.comparingInt(Order::arrivalMinute));

        Map<String, List<Order>> groups = new LinkedHashMap<>();       // groups appear in order of first arrival
        for (Order order : byArrival)
            groups.computeIfAbsent(order.sku(), sku -> new ArrayList<>()).add(order);

        List<Order> result = new ArrayList<>();
        for (List<Order> group : groups.values()) result.addAll(group);
        return result;
    }

    @Override
    public String name() { return "Same shelf together"; }
}
