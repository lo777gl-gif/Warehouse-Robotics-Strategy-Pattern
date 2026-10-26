package first_strategy;

import model.Point;
import model.Robot;

import java.util.List;
import java.util.Optional;

// One way to choose which free robot takes a job. 
public interface RobotStrategy {
    Optional<Robot> choose(List<Robot> freeRobots, Point target);

    String name();
}
