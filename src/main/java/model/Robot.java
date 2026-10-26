package model;

// A free robot: where it stands and how long it has been doing nothing.
public record Robot(String id, Point position, int idleMinutes) { }
