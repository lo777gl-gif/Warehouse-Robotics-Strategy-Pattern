/** What is happening in the warehouse right now. */
public record Situation(int hour, int queueSize) {
    public boolean isNight() { return hour < 6 || hour >= 22; }
}
