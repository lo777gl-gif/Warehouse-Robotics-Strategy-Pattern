import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** The same check is run on EVERY strategy of a family. A new strategy only has to be added to the list. */
class AllStrategiesTest {
    private static final BatterySensor SENSOR = new FakeBatterySensor(Map.of("R1", 90, "R2", 50, "R3", 70));
    private static final Point TARGET = new Point(0, 0);
    private static final List<Robot> ROBOTS = List.of(
            new Robot("R1", new Point(10, 0), 5), new Robot("R2", new Point(3, 4), 30), new Robot("R3", new Point(1, 1), 60));
    private static final List<Order> ORDERS = List.of(
            new Order("O1", "BOX", 30, 3), new Order("O2", "PALLET", 5, 1),
            new Order("O3", "BOX", 12, 2), new Order("O4", "TOOLS", 8, 4));

    static Stream<RobotStrategy> robotStrategies() {
        return Stream.of(new NearestRobot(), new MostChargedRobot(SENSOR), new LongestIdleRobot());
    }

    static Stream<OrderStrategy> orderStrategies() {
        return Stream.of(new ArrivalOrder(), new SmallestFirst(), new SameShelfTogether());
    }

    @ParameterizedTest(name = "{0}") @MethodSource("robotStrategies")
    void robotStrategy_alwaysChoosesOneOfTheFreeRobots(RobotStrategy strategy) {
        Robot chosen = strategy.choose(ROBOTS, TARGET).orElseThrow();
        assertTrue(ROBOTS.contains(chosen));
    }

    @ParameterizedTest(name = "{0}") @MethodSource("robotStrategies")
    void robotStrategy_givesNothingWhenNoRobotIsFree(RobotStrategy strategy) {
        assertTrue(strategy.choose(List.of(), TARGET).isEmpty());
    }

    @ParameterizedTest(name = "{0}") @MethodSource("orderStrategies")
    void orderStrategy_keepsEveryOrderExactlyOnce(OrderStrategy strategy) {
        List<Order> result = strategy.arrange(ORDERS);
        assertEquals(ORDERS.size(), result.size());
        assertTrue(result.containsAll(ORDERS));
    }

    @ParameterizedTest(name = "{0}") @MethodSource("orderStrategies")
    void orderStrategy_doesNotChangeTheGivenList(OrderStrategy strategy) {
        List<Order> copy = new ArrayList<>(ORDERS);
        strategy.arrange(copy);
        assertEquals(ORDERS, copy);
    }
}
