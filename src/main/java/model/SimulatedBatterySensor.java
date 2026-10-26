package model;

// Stand-in for the real sensor: gives the same made-up level for the same robot id every time.
public class SimulatedBatterySensor implements BatterySensor {
    @Override
    public int batteryLevel(String robotId) {
        return Math.abs(robotId.hashCode() * 31) % 101;
    }
}
