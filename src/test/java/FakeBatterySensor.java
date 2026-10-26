import model.BatterySensor;

import java.util.Map;

// Test double: battery levels are fixed by the test, so results never change. 
public class FakeBatterySensor implements BatterySensor {
    private final Map<String, Integer> levels;

    public FakeBatterySensor(Map<String, Integer> levels) { this.levels = levels; }

    @Override
    public int batteryLevel(String robotId) { return levels.getOrDefault(robotId, 0); }
}
