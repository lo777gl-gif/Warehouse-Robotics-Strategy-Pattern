import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class NearestRobot implements RobotStrategy {
    @Override
    public Optional<Robot> choose(List<Robot> freeRobots, Point target) {
        return freeRobots.stream().min(Comparator.comparingDouble(r -> r.position().distanceTo(target)));
    }

    @Override
    public String name() { return "Nearest robot"; }
}
