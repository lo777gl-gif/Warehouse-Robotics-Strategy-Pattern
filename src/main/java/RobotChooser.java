import java.util.List;
import java.util.Optional;

// BEFORE refactoring: one class knows every way to choose a robot. A new way means editing this class. 
public class RobotChooser {
    private final BatterySensor sensor;

    public RobotChooser(BatterySensor sensor) { this.sensor = sensor; }

    public Optional<Robot> choose(String mode, List<Robot> robots, Point target) {
        switch (mode) {
            case "NEAREST": {
                Robot best = null;
                for (Robot r : robots) {
                    if (best == null || r.position().distanceTo(target) < best.position().distanceTo(target)) best = r;
                }
                return Optional.ofNullable(best);
            }
            case "MOST_CHARGED": {
                Robot best = null;
                for (Robot r : robots) {
                    if (sensor.batteryLevel(r.id()) < 20) continue;        // too weak for a job
                    if (best == null || sensor.batteryLevel(r.id()) > sensor.batteryLevel(best.id())) best = r;
                }
                return Optional.ofNullable(best);
            }
            case "LONGEST_IDLE": {
                Robot best = null;
                for (Robot r : robots) {
                    if (best == null || r.idleMinutes() > best.idleMinutes()) best = r;
                }
                return Optional.ofNullable(best);
            }
            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}
