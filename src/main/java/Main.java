import first_strategy.*;
import model.*;
import second_strategy.*;

import java.util.List;

// Demo. Run it, nothing to type. 
public class Main {
    public static void main(String[] args) {

        BatterySensor sensor = new SimulatedBatterySensor();
        Point target = new Point(0, 0);

        List<Robot> robots = List.of(
                new Robot("R1", new Point(10, 0), 90),     // far away, waited longest
                new Robot("R2", new Point(1, 1), 30),      // closest to the job
                new Robot("R3", new Point(6, 8), 5),       // far away, strongest battery
                new Robot("R4", new Point(3, 4), 2));      // battery too weak for a job
        List<Order> orders = List.of(
                new Order("O1", "BOX", 30, 1), new Order("O2", "PALLET", 5, 2),
                new Order("O3", "BOX", 12, 3), new Order("O4", "TOOLS", 8, 4),
                new Order("O5", "PALLET", 25, 5));

        System.out.println("1. Same input, every strategy of each family");
        System.out.println("Robots (id, distance to the job, battery, idle minutes):");

        for (Robot r : robots) {
            System.out.printf("  %s  %.1f m  battery %d  idle %d%n", r.id(), r.position().distanceTo(target), sensor.batteryLevel(r.id()), r.idleMinutes());
        }

        for (RobotStrategy s : List.of(new NearestRobot(), new MostChargedRobot(sensor), new LongestIdleRobot())) {
            System.out.println("  " + s.name() + " -> " + new RobotAssigner(s).assign(robots, target).map(Robot::id).orElse("nobody"));
        }

        System.out.println("\nOrders: " + describe(orders));
        for (OrderStrategy s : List.of(new ArrivalOrder(), new SmallestFirst(), new SameShelfTogether())) {
            System.out.println("  " + s.name() + " -> " + describe(new OrderPlanner(s).plan(orders)));
        }


        System.out.println("\n2. Manual choice and automatic choice (Selector)");

        StrategySelector selector = new StrategySelector(sensor);
        RobotAssigner assigner = new RobotAssigner(new NearestRobot());              // chosen by hand
        OrderPlanner planner = new OrderPlanner(new ArrivalOrder());

        System.out.println("By hand: " + assigner.getStrategy().name() + " / " + planner.getStrategy().name());
        for (Situation situation : List.of(new Situation(14, 3), new Situation(14, 7), new Situation(14, 15), new Situation(23, 3))) {
            assigner.setStrategy(selector.robotStrategyFor(situation));
            planner.setStrategy(selector.orderStrategyFor(situation));
            System.out.println("Hour " + situation.hour() + ", queue " + situation.queueSize() + " -> "
                    + assigner.getStrategy().name() + " / " + planner.getStrategy().name());
        }


        System.out.println("\n3. Change the strategy while the program runs");
        RobotAssigner live = new RobotAssigner(new NearestRobot());
        System.out.println(live.getStrategy().name() + " -> " + live.assign(robots, target).map(Robot::id).orElse("nobody"));
        live.setStrategy(new LongestIdleRobot());
        System.out.println(live.getStrategy().name() + " -> " + live.assign(robots, target).map(Robot::id).orElse("nobody"));
        live.setStrategy(new MostChargedRobot(sensor));
        System.out.println(live.getStrategy().name() + " -> " + live.assign(robots, target).map(Robot::id).orElse("nobody"));
    }

    private static String describe(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        for (Order o : orders) {
            sb.append(o.id()).append("(").append(o.sku()).append(" x").append(o.quantity()).append(") ");
        }
        return sb.toString().trim();
    }
}
