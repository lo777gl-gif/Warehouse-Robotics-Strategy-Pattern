package first_strategy;

import model.Point;
import model.Robot;

import java.util.List;
import java.util.Optional;

public class NearestRobot implements RobotStrategy {
    @Override
    public Optional<Robot> choose(List<Robot> freeRobots, Point target) {
        if (freeRobots == null || freeRobots.isEmpty() || target == null) {
            return Optional.empty();
        }

        Robot closestRobot = null;
        double minDistance = Double.MAX_VALUE;

        for (Robot robot : freeRobots) {
            if (robot != null && robot.position() != null) {
                double currentDistance = robot.position().distanceTo(target);

                if (closestRobot == null || currentDistance < minDistance) {
                    minDistance = currentDistance;
                    closestRobot = robot;
                }
            }
        }

        return Optional.ofNullable(closestRobot);
    }

    @Override
    public String name() { return "Nearest robot"; }
}
