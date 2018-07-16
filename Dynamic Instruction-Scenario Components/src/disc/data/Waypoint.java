package disc.data;

import disc.util.WaypointException;

/**
 * Simple data structure for storing Waypoint data, including name, x coord, y
 * coord, z coord, heading, roll, and pitch. The String version of an
 * instruction is as follows: <br>
 * <br>
 * name: x, y, z, heading, roll, pitch<br>
 * <br>
 * The minimum amount of data that must be specified for a Waypoint to be
 * built from a string is the name, x, y, and z.<br>
 * <br>
 * This data structure is extensible.
 * 
 * @author Liam Williams
 * @version 0.1.5
 */
public class Waypoint {

    protected String name;
    protected double x;
    protected double y;
    protected double z;
    protected double heading;
    protected double roll;
    protected double pitch;

    /**
     * Constructs a {@link Waypoint} from a String Waypoint.
     * 
     * @param arg0
     *            The String to construct the Waypoint from
     * @throws WaypointException
     *             If the data minimum is not met.
     */
    public Waypoint(String arg0) throws WaypointException {
        int o = arg0.indexOf(':');
        if(o != -1) {
            this.name = arg0.substring(0, o);
            int i = arg0.indexOf(',');
            if(i != -1) {
                x = Double.valueOf(arg0.substring(o + 1, i));
                o = i;
                i = arg0.indexOf(',', o + 1);
            } else throw new WaypointException(
                    "Invalid Waypoint: does not define data.");
            if(i != -1) {
                y = Double.valueOf(arg0.substring(o + 1, i));
                o = i;
                i = arg0.indexOf(',', o + 1);
            } else throw new WaypointException(
                    "Invalid Waypoint: does not define the minimum amount of data.");
            if(i != -1) {
                z = Double.valueOf(arg0.substring(o + 1, i));
                o = i;
                i = arg0.indexOf(',', o + 1);
            } else {
                z = Double.valueOf(arg0.substring(o + 1));
                heading = 0;
                roll = 0;
                pitch = 0;
                o = -1;
            }
            if(i != -1 && o != -1) {
                heading = Double.valueOf(arg0.substring(o + 1, i));
                o = i;
                i = arg0.indexOf(',', o + 1);
            } else if(o != -1) {
                heading = Double.valueOf(arg0.substring(o + 1));
                roll = 0;
                pitch = 0;
                o = -1;
            }
            if(i != -1 && o != -1) {
                roll = Double.valueOf(arg0.substring(o + 1, i));
                o = i;
                i = arg0.indexOf(',', o + 1);
            } else if(o != -1) {
                roll = Double.valueOf(arg0.substring(o + 1));
                pitch = 0;
                o = -1;
            }
            if(i != -1 && o != -1)
                pitch = Double.valueOf(arg0.substring(o + 1, i));
            else if(o != -1) pitch = Double.valueOf(arg0.substring(o + 1));
        } else throw new WaypointException(
                "Invalid Waypoint: does not define name.");
    }

    /**
     * Constructor for cloning a {@link Waypoint}
     */
    public Waypoint(String name, double x, double y, double z, double heading,
            double roll, double pitch) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
        this.roll = roll;
        this.pitch = pitch;
    }

    /**
     * Constructor for creating a {@link Waypoint} with only an x and y, with a
     * hashCode()-generated name.
     */
    public Waypoint(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.heading = 0;
        this.roll = 0;
        this.pitch = 0;
        this.name = Integer.toString(this.hashCode());
    }

    /**
     * Constructor for creating a {@link Waypoint} with only an x, y, and
     * heading, with a hashCode()-generated name.
     */
    public Waypoint(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.z = 0;
        this.heading = heading;
        this.roll = 0;
        this.pitch = 0;
        this.name = Integer.toString(this.hashCode());
    }

    /**
     * @return the Name associated with this {@link Waypoint}
     */
    public String getName() {
        return name;
    }

    /**
     * @return the X associated with this {@link Waypoint}
     */
    public double getX() {
        return x;
    }

    /**
     * @return the Y associated with this {@link Waypoint}
     */
    public double getY() {
        return y;
    }

    /**
     * @return the Z associated with this {@link Waypoint}
     */
    public double getZ() {
        return z;
    }

    /**
     * @return the Heading associated with this {@link Waypoint}
     */
    public double getHeading() {
        return heading;
    }

    /**
     * @return the Roll associated with this {@link Waypoint}
     */
    public double getRoll() {
        return roll;
    }

    /**
     * @return the Pitch associated with this {@link Waypoint}
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Constructs a constructor-readable String from the Waypoint's data.
     * 
     * @return the String representation of the Waypoint's data.
     */
    @Override
    public String toString() {
        return name + ": " + x + ", " + y + ", " + z + ", " + heading + ", "
                + roll + ", " + pitch;
    }

    /**
     * Calculates the hashCode from the data of this Waypoint. Ignores the name.
     * 
     * @return the hashCode of the Waypoint's data.
     */
    @Override
    public int hashCode() {
        String tmp = Integer.toString(new Double(x).hashCode())
                + Integer.toString(new Double(y).hashCode())
                + Integer.toString(new Double(z).hashCode())
                + Integer.toString(new Double(heading).hashCode())
                + Integer.toString(new Double(roll).hashCode())
                + Integer.toString(new Double(pitch).hashCode());
        return (int) (Math.pow(Double.valueOf(tmp.replaceAll("[^0-9]", "")),
                .25));
    }

    /**
     * @return a new Waypoint with an idential Name and other data.
     */
    @Override
    public Waypoint clone() {
        return new Waypoint(this.name, this.x, this.y, this.z, this.heading,
                this.roll, this.pitch);
    }

}
