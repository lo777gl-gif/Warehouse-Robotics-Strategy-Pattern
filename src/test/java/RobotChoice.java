import java.util.List;
import java.util.Optional;

// Test helper for the BEFORE version: the only file that differs between the two commits. 
class RobotChoice {
    static Optional<Robot> pick(String mode, List<Robot> robots, Point target, BatterySensor sensor) {
        return new RobotChooser(sensor).choose(mode, robots, target);
    }
}
