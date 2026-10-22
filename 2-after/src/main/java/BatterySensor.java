/** Our own interface for outside data: battery level of a robot, 0-100. Real hardware sits behind it. */
public interface BatterySensor {
    int batteryLevel(String robotId);
}
