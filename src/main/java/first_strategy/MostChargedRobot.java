package first_strategy;

import model.BatterySensor;
import model.Point;
import model.Robot;

import java.util.List;
import java.util.Optional;

public class MostChargedRobot implements RobotStrategy {
    public static final int MIN_BATTERY = 15;     // below this a robot is too weak for a job

    private final BatterySensor sensor;           // outside data, only seen through our own interface

    public MostChargedRobot(BatterySensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public Optional<Robot> choose(List<Robot> freeRobots, Point target) {
        if (freeRobots == null || freeRobots.isEmpty()) {
            return Optional.empty();
        }

        Robot bestRobot = null;
        int maxBattery = MIN_BATTERY;

        for (Robot robot : freeRobots) {
            int currentBattery = sensor.batteryLevel(robot.id());

            if (currentBattery >= MIN_BATTERY) {
                if (bestRobot == null || currentBattery > maxBattery) {
                    maxBattery = currentBattery;
                    bestRobot = robot;
                }
            }
        }

        return Optional.ofNullable(bestRobot);
    }

    @Override
    public String name() {
        return "Most charged robot";
    }
}
