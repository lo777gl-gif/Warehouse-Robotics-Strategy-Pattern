import java.util.List;

/** Context of family 2: holds an order strategy and uses it. It does not know which one it holds. */
public class OrderPlanner {
    private OrderStrategy strategy;

    public OrderPlanner(OrderStrategy strategy) { this.strategy = strategy; }

    public void setStrategy(OrderStrategy strategy) { this.strategy = strategy; }

    public OrderStrategy getStrategy() { return strategy; }

    public List<Order> plan(List<Order> waitingOrders) {
        return strategy.arrange(waitingOrders);
    }
}
