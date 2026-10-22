import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** One section per robot strategy, each strategy used directly. */
class RobotStrategiesTest {
    private final Point target = new Point(0, 0);
    private final Robot near = new Robot("NEAR", new Point(1, 0), 10);
    private final Robot far = new Robot("FAR", new Point(9, 0), 50);

    // ----- NearestRobot -----
    @Test void nearestRobot_choosesSmallestDistance() {
        assertEquals("NEAR", new NearestRobot().choose(List.of(far, near), target).orElseThrow().id());
    }

    @Test void nearestRobot_dependsOnTheTargetNotOnTheListOrder() {
        Point nearFar = new Point(10, 0);
        assertEquals("FAR", new NearestRobot().choose(List.of(near, far), nearFar).orElseThrow().id());
    }

    // ----- MostChargedRobot -----
    @Test void mostChargedRobot_choosesHighestBattery() {
        BatterySensor sensor = new FakeBatterySensor(Map.of("NEAR", 40, "FAR", 80));
        assertEquals("FAR", new MostChargedRobot(sensor).choose(List.of(near, far), target).orElseThrow().id());
    }

    @Test void mostChargedRobot_ignoresRobotsBelow20ButAcceptsExactly20() {
        BatterySensor sensor = new FakeBatterySensor(Map.of("NEAR", 19, "FAR", 20));    // plain numbers on purpose
        assertEquals("FAR", new MostChargedRobot(sensor).choose(List.of(near, far), target).orElseThrow().id());
        assertTrue(new MostChargedRobot(new FakeBatterySensor(Map.of("NEAR", 19, "FAR", 19)))
                .choose(List.of(near, far), target).isEmpty());
    }

    @Test void mostChargedRobot_asksTheSensorNotTheRobot() {
        BatterySensor first = new FakeBatterySensor(Map.of("NEAR", 90, "FAR", 30));
        BatterySensor second = new FakeBatterySensor(Map.of("NEAR", 30, "FAR", 90));
        assertEquals("NEAR", new MostChargedRobot(first).choose(List.of(near, far), target).orElseThrow().id());
        assertEquals("FAR", new MostChargedRobot(second).choose(List.of(near, far), target).orElseThrow().id());
    }

    // ----- LongestIdleRobot -----
    @Test void longestIdleRobot_choosesTheOneThatWaitedMost() {
        assertEquals("FAR", new LongestIdleRobot().choose(List.of(near, far), target).orElseThrow().id());
    }

    @Test void longestIdleRobot_ignoresDistance() {
        assertEquals("FAR", new LongestIdleRobot().choose(List.of(near, far), new Point(9, 0)).orElseThrow().id());
    }

    // ----- names -----
    @Test void everyStrategyHasAReadableName() {
        assertEquals("Nearest robot", new NearestRobot().name());
        assertEquals("Most charged robot", new MostChargedRobot(new FakeBatterySensor(Map.of())).name());
        assertEquals("Longest idle robot", new LongestIdleRobot().name());
    }
}
