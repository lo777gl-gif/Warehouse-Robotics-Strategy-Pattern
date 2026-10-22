import java.util.List;

// BEFORE: tiny demo of the switch-based chooser. 
public class Main {
    public static void main(String[] args) {
        RobotChooser chooser = new RobotChooser(new SimulatedBatterySensor());
        List<Robot> robots = List.of(
                new Robot("R1", new Point(10, 0), 5),
                new Robot("R2", new Point(3, 4), 30),
                new Robot("R3", new Point(1, 1), 60));
        for (String mode : List.of("NEAREST", "MOST_CHARGED", "LONGEST_IDLE"))
            System.out.println(mode + " -> " + chooser.choose(mode, robots, new Point(0, 0)).map(Robot::id).orElse("nobody"));
    }
}
