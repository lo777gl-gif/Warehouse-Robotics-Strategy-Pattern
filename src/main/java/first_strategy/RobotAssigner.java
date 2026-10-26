package first_strategy;

import model.Point;
import model.Robot;

import java.util.List;
import java.util.Optional;

// Context of family 1: holds a robot strategy and uses it. It does not know which one it holds. 
public class RobotAssigner {
    private RobotStrategy strategy;

    public RobotAssigner(RobotStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RobotStrategy strategy) {
        this.strategy = strategy;
    }

    public RobotStrategy getStrategy() {
        return strategy;
    }

    public Optional<Robot> assign(List<Robot> freeRobots, Point target) {
        return strategy.choose(freeRobots, target);
    }
}
