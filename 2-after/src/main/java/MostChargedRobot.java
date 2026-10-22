import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MostChargedRobot implements RobotStrategy {
    public static final int MIN_BATTERY = 20;     // below this a robot is too weak for a job

    private final BatterySensor sensor;           // outside data, only seen through our own interface

    public MostChargedRobot(BatterySensor sensor) { this.sensor = sensor; }

    @Override
    public Optional<Robot> choose(List<Robot> freeRobots, Point target) {
        return freeRobots.stream()
                .filter(r -> sensor.batteryLevel(r.id()) >= MIN_BATTERY)
                .max(Comparator.comparingInt(r -> sensor.batteryLevel(r.id())));
    }

    @Override
    public String name() { return "Most charged robot"; }
}
