import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Describes WHAT robot choice must do. It only talks to RobotChoice.pick(...),
 * so the same file runs on the "before" commit (switch) and on the "after" commit (Strategy).
 */
class RobotChoiceBehaviourTest {
    private final Point target = new Point(0, 0);
    private final BatterySensor sensor = new FakeBatterySensor(Map.of("R1", 90, "R2", 50, "R3", 10));
    private final List<Robot> robots = List.of(
            new Robot("R1", new Point(10, 0), 5),    // far, full battery, just worked
            new Robot("R2", new Point(3, 4), 30),    // distance 5
            new Robot("R3", new Point(1, 1), 60));   // very close, battery 10, idle longest

    private Optional<Robot> pick(String mode, List<Robot> free) {
        return RobotChoice.pick(mode, free, target, sensor);
    }

    @Test void nearestModeChoosesTheClosestRobot() {
        assertEquals("R3", pick("NEAREST", robots).orElseThrow().id());
    }

    @Test void mostChargedModeChoosesTheFullestBatteryAndSkipsWeakOnes() {
        assertEquals("R1", pick("MOST_CHARGED", robots).orElseThrow().id());
    }

    @Test void mostChargedModeGivesNothingWhenEveryBatteryIsBelow20() {
        List<Robot> weak = List.of(robots.get(2));          // only R3, battery 10
        assertTrue(pick("MOST_CHARGED", weak).isEmpty());
    }

    @Test void longestIdleModeChoosesTheRobotThatWaitedMost() {
        assertEquals("R3", pick("LONGEST_IDLE", robots).orElseThrow().id());
    }

    @Test void whenTwoRobotsAreEqualTheFirstOneWins() {
        List<Robot> twins = List.of(new Robot("A", new Point(2, 0), 1), new Robot("B", new Point(0, 2), 1));
        assertEquals("A", pick("NEAREST", twins).orElseThrow().id());
    }

    @Test void noFreeRobotsGivesNothingInEveryMode() {
        for (String mode : List.of("NEAREST", "MOST_CHARGED", "LONGEST_IDLE"))
            assertTrue(pick(mode, List.of()).isEmpty(), mode);
    }

    @Test void unknownModeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> pick("FASTEST", robots));
    }
}
