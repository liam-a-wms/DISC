package disc.data;

import disc.util.WaypointException;

/**
 * Simple data structure for storing Position data, including name, x coord, y
 * coord, z coord, heading, roll, and pitch. The String version of an
 * instruction is as follows: <br>
 * <br>
 * name: x, y, z, heading, roll, pitch<br>
 * <br>
 * The minimum amount of data that must be specified for a position to be built
 * from a string is the x and y. However, it's string form must still begin with
 * a ':'<br>
 * <br>
 * This data structure is extensible.
 * 
 * @author Liam Williams
 * @version 0.3.1
 */
public class Position extends Waypoint {

    /**
     * Constructs a {@link Position} from a String matching the {@link Waypoint}
     * format.
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
     * Constructor for building a {@link Position}, without needing a name to be
     * populated.
     */
    public Position(double x, double y, double z, double heading, double roll,
            double pitch) {
        super("", x, y, z, heading, roll, pitch);
    }

    /**
     * Constructor for creating a {@link Position} with only an x and a y.
     */
    public Position(double x, double y) {
        super(x, y);
        this.name = "";
    }

    /**
     * Constructor for creating a {@link Position} with only an x, y, and a
     * heading.
     */
    public Position(double x, double y, double heading) {
        super(x, y, heading);
        this.name = "";
    }

    /**
     * @return the Name associated with this {@link Position}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the Name to set for this {@link Position}
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the X associated with this {@link Position}
     */
    @Override
    public double getX() {
        return x;
    }

    /**
     * @param x
     *            the X to set for this {@link Position}
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the Y associated with this {@link Position}
     */
    @Override
    public double getY() {
        return y;
    }

    /**
     * @param y
     *            The Y to set for this {@link Position}
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the Z associated with this {@link Position}
     */
    @Override
    public double getZ() {
        return z;
    }

    /**
     * @param z
     *            the Z to set for this {@link Position}
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * @return the Heading associated with this {@link Position}
     */
    @Override
    public double getHeading() {
        return heading;
    }

    /**
     * @param heading
     *            the Heading to set for this {@link Position}
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * @return the Roll associated with this {@link Position}
     */
    @Override
    public double getRoll() {
        return roll;
    }

    /**
     * @param roll
     *            the Roll to set for this {@link Position}
     */
    public void setRoll(double roll) {
        this.roll = roll;
    }

    /**
     * @return the Pitch associated with this {@link Position}
     */
    @Override
    public double getPitch() {
        return pitch;
    }

    /**
     * @param pitch
     *            the Pitch to set for this {@link Position}
     */
    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    /**
     * Rudimentary comparison to compare this {@link Position} to a
     * {@link Waypoint}.
     * 
     * @param w
     *            the Waypoint to compare against
     * @param tolerance
     *            the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean compareToWaypoint(Waypoint w, double tolerance) {
        return (compareOrthogonal(w, tolerance)
                && compareRotation(w, tolerance));
    }

    /**
     * Comparison to check for an exact match between this {@link Position} and
     * a {@link Waypoint}.
     * 
     * @param w
     *            the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean compareToWaypoint(Waypoint w) {
        // Compares with a tolerance of 0.0000001 just incase of a floating
        // point error
        return compareToWaypoint(w, 1e-7);
    }

    /**
     * Rudimentary comparison to compare this {@link Position} to a
     * {@link Waypoint}, only checking x, y, and z.
     * 
     * @param w
     *            the Waypoint to compare against
     * @param tolerance
     *            the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean compareOrthogonal(Waypoint w, double tolerance) {
        return (((this.x - w.x <= tolerance) 
        		        || (this.x - w.x >= -tolerance))
                && ((this.y - w.y <= tolerance) 
                		|| (this.y - w.y >= -tolerance))
                && ((this.z - w.z <= tolerance) 
                		|| (this.z - w.z >= -tolerance)));
    }

    /**
     * Comparison to check for an exact match between this {@link Position} and
     * a {@link Waypoint}, only checking x, y, and z.
     * 
     * @param w
     *            the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean compareOrthogonal(Waypoint w) {
        // Compares with a tolerance of 0.0000001 just incase of a floating
        // point error
        return compareOrthogonal(w, 1e-7);
    }

    /**
     * Rudimentary comparison to compare this {@link Position} to a
     * {@link Waypoint}, only checking heading, pitch, and roll.
     * 
     * @param w
     *            the Waypoint to compare against
     * @param tolerance
     *            the tolerance for "how close" it can be
     * @return true if the values are within a tolerable difference
     */
    public boolean compareRotation(Waypoint w, double tolerance) {
    	 return (((this.heading - w.heading <= tolerance) 
 		                || (this.heading - w.heading >= -tolerance))
                 && ((this.pitch - w.pitch <= tolerance) 
         		        || (this.pitch - w.pitch >= -tolerance))
                 && ((this.roll - w.roll <= tolerance) 
         		        || (this.roll - w.roll >= -tolerance)));
    }

    /**
     * Comparison to check for an exact match between this {@link Position} and
     * a {@link Waypoint}, only checking heading, roll, and pitch.
     * 
     * @param w
     *            the Waypoint to compare against
     * @return true if the Waypoint matches
     */
    public boolean compareRotation(Waypoint w) {
        // Compares with a tolerance of 0.0000001 just incase of a floating
        // point error
        return compareRotation(w, 1e-7);
    }

}
