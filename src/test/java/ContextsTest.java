import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import first_strategy.LongestIdleRobot;
import first_strategy.NearestRobot;
import first_strategy.RobotAssigner;
import first_strategy.RobotStrategy;
import model.Order;
import model.Point;
import model.Robot;
import org.junit.jupiter.api.Test;
import second_strategy.ArrivalOrder;
import second_strategy.OrderPlanner;
import second_strategy.OrderStrategy;
import second_strategy.SmallestFirst;

// The contexts must only hand the work to their strategy. Stub strategies prove it. 
class ContextsTest {
    private final Point target = new Point(0, 0);
    private final Robot a = new Robot("A", new Point(1, 1), 1);
    private final Robot b = new Robot("B", new Point(2, 2), 2);

    private RobotStrategy alwaysPicks(Robot robot) {
        return new RobotStrategy() {
            @Override public Optional<Robot> choose(List<Robot> free, Point t) { return Optional.of(robot); }
            @Override public String name() { return "stub " + robot.id(); }
        };
    }

    private OrderStrategy reversing() {
        return new OrderStrategy() {
            @Override public List<Order> arrange(List<Order> orders) {
                List<Order> result = new ArrayList<>(orders);
                Collections.reverse(result);
                return result;
            }
            @Override public String name() { return "reverse"; }
        };
    }

    @Test void robotAssigner_usesTheStrategyFromTheConstructor() {
        assertEquals("A", new RobotAssigner(alwaysPicks(a)).assign(List.of(a, b), target).orElseThrow().id());
    }

    @Test void robotAssigner_changesBehaviourWhenTheStrategyIsReplaced() {
        RobotAssigner assigner = new RobotAssigner(alwaysPicks(a));
        assertEquals("A", assigner.assign(List.of(a, b), target).orElseThrow().id());
        assigner.setStrategy(alwaysPicks(b));
        assertEquals("B", assigner.assign(List.of(a, b), target).orElseThrow().id());
        assertEquals("stub B", assigner.getStrategy().name());
    }

    @Test void robotAssigner_worksWithRealStrategiesToo() {
        RobotAssigner assigner = new RobotAssigner(new NearestRobot());
        assertEquals("A", assigner.assign(List.of(b, a), target).orElseThrow().id());
        assigner.setStrategy(new LongestIdleRobot());
        assertEquals("B", assigner.assign(List.of(b, a), target).orElseThrow().id());
    }

    @Test void orderPlanner_usesTheStrategyFromTheConstructor() {
        List<Order> orders = List.of(new Order("1", "X", 1, 1), new Order("2", "X", 1, 2));
        assertEquals(List.of("2", "1"), new OrderPlanner(reversing()).plan(orders).stream().map(Order::id).toList());
    }

    @Test void orderPlanner_changesBehaviourWhenTheStrategyIsReplaced() {
        List<Order> orders = List.of(new Order("1", "X", 9, 1), new Order("2", "X", 1, 2));
        OrderPlanner planner = new OrderPlanner(new ArrivalOrder());
        assertEquals("1", planner.plan(orders).get(0).id());
        planner.setStrategy(new SmallestFirst());
        assertEquals("2", planner.plan(orders).get(0).id());
        assertEquals("Smallest first", planner.getStrategy().name());
    }
}
