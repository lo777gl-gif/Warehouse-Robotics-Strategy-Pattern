import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SmallestFirst implements OrderStrategy {
    @Override
    public List<Order> arrange(List<Order> orders) {
        List<Order> result = new ArrayList<>(orders);
        result.sort(Comparator.comparingInt(Order::quantity).thenComparingInt(Order::arrivalMinute));
        return result;
    }

    @Override
    public String name() { return "Smallest first"; }
}
