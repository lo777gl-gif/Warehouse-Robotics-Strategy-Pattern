import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class LongestIdleRobot implements RobotStrategy {
    @Override
    public Optional<Robot> choose(List<Robot> freeRobots, Point target) {
        return freeRobots.stream().max(Comparator.comparingInt(Robot::idleMinutes));
    }

    @Override
    public String name() { return "Longest idle robot"; }
}
