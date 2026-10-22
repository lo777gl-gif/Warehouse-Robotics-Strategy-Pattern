import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Test helper for the AFTER version: the only file that differs between the two commits. */
class RobotChoice {
    static Optional<Robot> pick(String mode, List<Robot> robots, Point target, BatterySensor sensor) {
        Map<String, RobotStrategy> byMode = Map.of(
                "NEAREST", new NearestRobot(),
                "MOST_CHARGED", new MostChargedRobot(sensor),
                "LONGEST_IDLE", new LongestIdleRobot());
        RobotStrategy strategy = byMode.get(mode);
        if (strategy == null) throw new IllegalArgumentException("Unknown mode: " + mode);
        return new RobotAssigner(strategy).assign(robots, target);
    }
}
