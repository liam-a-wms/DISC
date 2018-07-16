package disc.data;

import disc.util.WaypointException;

/**
 * Simple data structure for storing Position data, including name, x coord, y
 * coord, z coord, heading, roll, and pitch. The String version of an
 * instruction is as follows: <br>
 * <br>
 * name: x, y, z, heading, roll, pitch<br>
 * <br>
 * The minimum amount of data that must be specified for a position to be
 * built from a string is the x, y, and z. However, it's string form must still
 * begin with a ':'<br>
 * <br>
 * This data structure is extensible.
 * 
 * @author Liam Williams
 * @version 0.1.0
 */
public class Position extends Waypoint {

    /**
     * Constructs a {@link Position} from a String matching the {@link Waypoint} format.
     * 
     * @param arg0
     *            The String to construct the Position from
     * @throws WaypointException
     *             If the data minimum is not met.
     */
    public Position(String arg0) throws WaypointException {
        super(arg0);
    }
    
    /**
     * Constructor for cloning, or building a {@link Position} with everything.
     */
    public Position(String name, double x, double y, double z, double heading,
            double roll, double pitch) {
        super(name, x, y, z, heading, roll, pitch);
    }
    
    /**
     * Constructor for creating a {@link Position} with only an x and a y.
     */
    public Position(double x, double y) {
        super(x, y);
        this.name = "";
    }
    
    /**
     * Constructor for creating a {@link Position} with only an x, y, and a heading.
     */
    public Position(double x, double y, double heading) {
        super(x, y, heading);
        this.name = "";
    }
    
    /**
     * @return the Name associated with this {@link Position}
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the Name to set for this {@link Position}
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the X associated with this {@link Position}
     */
    public double getX() {
        return x;
    }
    
    /**
     * @param x the X to set for this {@link Position}
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the Y associated with this {@link Position}
     */
    public double getY() {
        return y;
    }
    
    /**
     * @param y The Y to set for this {@link Position}
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the Z associated with this {@link Position}
     */
    public double getZ() {
        return z;
    }
    
    /**
     * @param z the Z to set for this {@link Position}
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return the Heading associated with this {@link Position}
     */
    public double getHeading() {
        return heading;
    }
    
    /**
     * @param heading the Heading to set for this {@link Position}
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * @return the Roll associated with this {@link Position}
     */
    public double getRoll() {
        return roll;
    }
    
    /**
     * @param roll the Roll to set for this {@link Position}
     */
    public void setRoll(double roll) {
        this.roll = roll;
    }

    /**
     * @return the Pitch associated with this {@link Position}
     */
    public double getPitch() {
        return pitch;
    }
    
    /**
     * @param pitch the Pitch to set for this {@link Position}
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }
    
    /**
     * Rudimentary comparison to compare this {@link Position} to a {@link Waypoint}.
     * 
     * @param w the Waypoint to compare against
     * @param tolerance the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean compareToWaypoint(Waypoint w, double tolerance) {
        double[] a = new double[6];
        double[] b = new double[6];
        boolean[] c = new boolean[6];
        
        a[0] = this.x;
        a[1] = this.y;
        a[2] = this.z;
        a[3] = this.heading;
        a[4] = this.roll;
        a[5] = this.pitch;
        
        b[0] = w.x;
        b[1] = w.y;
        b[2] = w.z;
        b[3] = w.heading;
        b[4] = w.roll;
        b[5] = w.pitch;
        
        for(int i = 0; i < 6; i++) {
            //If both are at 0 (the default for both classes), say true
            if(a[i] == 0 || b[i] == 0) c[i] = true;
            else c[i] = (Math.abs(a[i] - b[i]) <= tolerance);
        }
        
        return (c[0] && c[1] && c[2] && c[3] && c[4] && c[5]);
    }
    
    /**
     * Comparison to check for an exact match between this {@link Position} and a {@link Waypoint}
     * 
     * @param w the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean compareToWaypoint(Waypoint w) {
        //Compares with a tolerance of 0.0000001 just incase of a floating point error
        return compareToWaypoint(w, 1e-7);
    }

}
