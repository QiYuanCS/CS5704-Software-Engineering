package river;

import java.awt.*;

public class GameObject {

    private Location location;
    private final String label;
    private final Color color;
    private final boolean isDriver;

    public GameObject(String label, Location location, Color color, boolean isDriver) {
        this.label = label;
        this.location = location;
        this.color = color;
        this.isDriver = isDriver;
    }

    public Location getLocation() {
        return location;
    }

    public String getLabel() {
        return label;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Color getColor() {
        return color;
    }
}
