package model;

import first_strategy.*;
import second_strategy.*;

// Chooses a strategy for the current situation. The only class where the choice is written with if.
public class StrategySelector {
    public static final int RUSH_QUEUE = 10;     // more waiting orders than this is a rush
    public static final int BUSY_QUEUE = 5;      // from this many waiting orders grouping pays off

    private final BatterySensor sensor;

    public StrategySelector(BatterySensor sensor) {
        this.sensor = sensor;
    }

    public RobotStrategy robotStrategyFor(Situation situation) {
        if (situation.isNight()) {
            return new LongestIdleRobot();                 // quiet time: share the work
        }
        if (situation.queueSize() > RUSH_QUEUE) {
            return new NearestRobot();      // rush: be fast
        }
        return new MostChargedRobot(sensor);                                    // normal day: be safe
    }

    public OrderStrategy orderStrategyFor(Situation situation) {
        if (situation.queueSize() > RUSH_QUEUE) {
            return new SmallestFirst();          // clear the backlog fast
        }
        if (situation.queueSize() >= BUSY_QUEUE) {
            return new SameShelfTogether();     // fewer walks to the shelf
        }
        return new ArrivalOrder();                                                   // quiet queue: be fair
    }
}
