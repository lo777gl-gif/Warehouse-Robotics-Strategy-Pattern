import static org.junit.jupiter.api.Assertions.*;

import model.BatterySensor;
import model.SimulatedBatterySensor;
import org.junit.jupiter.api.Test;

class SimulatedBatterySensorTest {
    private final BatterySensor sensor = new SimulatedBatterySensor();

    @Test void giveTheSameLevelForTheSameRobotEveryTime() {
        assertEquals(sensor.batteryLevel("R1"), sensor.batteryLevel("R1"));
    }

    @Test void levelIsAlwaysBetween0And100() {
        for (String id : new String[] {"R1", "R2", "R3", "R4", "robot-with-a-long-name", ""})
            assertTrue(sensor.batteryLevel(id) >= 0 && sensor.batteryLevel(id) <= 100, id);
    }
}
