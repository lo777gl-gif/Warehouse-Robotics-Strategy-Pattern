import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import first_strategy.LongestIdleRobot;
import first_strategy.MostChargedRobot;
import first_strategy.NearestRobot;
import model.Situation;
import model.StrategySelector;
import org.junit.jupiter.api.Test;
import second_strategy.ArrivalOrder;
import second_strategy.SameShelfTogether;
import second_strategy.SmallestFirst;

// Every selection rule, including the boundary values. 
class StrategySelectorTest {
    private final StrategySelector selector = new StrategySelector(new FakeBatterySensor(Map.of()));

    // ----- robot rules -----
    @Test void robotRule_nightUsesLongestIdle() {
        assertInstanceOf(LongestIdleRobot.class, selector.robotStrategyFor(new Situation(23, 3)));
        assertInstanceOf(LongestIdleRobot.class, selector.robotStrategyFor(new Situation(2, 3)));
    }

    @Test void robotRule_nightBeatsRush() {
        assertInstanceOf(LongestIdleRobot.class, selector.robotStrategyFor(new Situation(3, 50)));
    }

    @Test void robotRule_nightBoundaries() {
        assertInstanceOf(LongestIdleRobot.class, selector.robotStrategyFor(new Situation(5, 3)));   // last night hour
        assertInstanceOf(MostChargedRobot.class, selector.robotStrategyFor(new Situation(6, 3)));   // first day hour
        assertInstanceOf(MostChargedRobot.class, selector.robotStrategyFor(new Situation(21, 3)));  // last day hour
        assertInstanceOf(LongestIdleRobot.class, selector.robotStrategyFor(new Situation(22, 3)));  // first night hour
    }

    @Test void robotRule_rushDuringTheDayUsesNearest() {
        assertInstanceOf(NearestRobot.class, selector.robotStrategyFor(new Situation(14, 11)));
    }

    @Test void robotRule_rushBoundary() {
        assertInstanceOf(MostChargedRobot.class, selector.robotStrategyFor(new Situation(14, 10)));  // 10 is not a rush
        assertInstanceOf(NearestRobot.class, selector.robotStrategyFor(new Situation(14, 11)));
    }

    @Test void robotRule_normalDayUsesMostCharged() {
        assertInstanceOf(MostChargedRobot.class, selector.robotStrategyFor(new Situation(14, 0)));
    }

    // ----- order rules -----
    @Test void orderRule_rushUsesSmallestFirst() {
        assertInstanceOf(SmallestFirst.class, selector.orderStrategyFor(new Situation(14, 11)));
    }

    @Test void orderRule_busyQueueUsesSameShelfTogether() {
        assertInstanceOf(SameShelfTogether.class, selector.orderStrategyFor(new Situation(14, 7)));
    }

    @Test void orderRule_quietQueueUsesArrivalOrder() {
        assertInstanceOf(ArrivalOrder.class, selector.orderStrategyFor(new Situation(14, 0)));
        assertInstanceOf(ArrivalOrder.class, selector.orderStrategyFor(new Situation(14, 4)));
    }

    @Test void orderRule_boundaries() {
        assertInstanceOf(ArrivalOrder.class, selector.orderStrategyFor(new Situation(14, 4)));
        assertInstanceOf(SameShelfTogether.class, selector.orderStrategyFor(new Situation(14, 5)));
        assertInstanceOf(SameShelfTogether.class, selector.orderStrategyFor(new Situation(14, 10)));
        assertInstanceOf(SmallestFirst.class, selector.orderStrategyFor(new Situation(14, 11)));
    }

    @Test void orderRule_timeOfDayDoesNotMatter() {
        assertInstanceOf(SmallestFirst.class, selector.orderStrategyFor(new Situation(3, 11)));
    }
}
