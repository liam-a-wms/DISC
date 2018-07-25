package disc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import disc.util.WaypointException;

/**
 * Advanced data structure that wraps a Map of {@link Waypoint}s with additional
 * features. Parses a .waypoint file storing Waypoint data, or a String version
 * of such a file. Can store an internal boolean value dictating whether the
 * Waypoints contained within have heading, roll, and pitch values in radians or
 * degrees.
 * 
 * @author Liam Williams
 * @version 0.2.2
 */
public class WaypointMap {

    Map<String, Waypoint> m = new HashMap<String, Waypoint>();
    boolean inDegrees = false;

    /**
     * Blank constructor for creating an empty {@link WaypointMap}
     */
    public WaypointMap() {}

    /**
     * Creates a {@link WaypointMap} from a .waypoint file.
     * 
     * @param input
     *            The file to be read
     * @throws FileNotFoundException
     *             If the file doesn't exist
     */
    public WaypointMap(File input) throws FileNotFoundException {
        parse(new Scanner(input));
    }

    /**
     * Creates a {@link WaypointMap} from a String representation.
     * 
     * @param input
     *            The String to be read
     */
    public WaypointMap(String input) {
        parse(new Scanner(input));
    }

    /**
     * Constructor for cloning a {@link WaypointMap}.
     * 
     * @param map
     *            The Map storing the Waypoints
     * @param inDegrees
     *            Whether the data is in degrees or radians
     */
    public WaypointMap(HashMap<String, Waypoint> map, boolean inDegrees) {
        this.m = map;
        this.inDegrees = inDegrees;
    }

    /**
     * Reads the data of the file/String into the {@link WaypointMap}'s internal
     * data.
     */
    private void parse(Scanner scn) {
        Waypoint t;
        while(scn.hasNextLine()) {
            String tmp = scn.nextLine().trim();
            if(tmp.startsWith("#$")) {
                if(tmp.contains("DEGREES")) inDegrees = true;
                else if(tmp.contains("RADIANS")) inDegrees = false;
                else inDegrees = false;
            } else if(!(tmp.startsWith("#") || tmp.startsWith("# "))) {
                try {
                    t = new Waypoint(tmp);
                    m.put(t.getName(), t);
                } catch(WaypointException e) {
                    e.printStackTrace();
                }
            }

        }
        scn.close();
    }

    /**
     * Searches the internal map for a {@link Waypoint} of the given name.
     * 
     * @param name
     *            The name of the Waypoint
     * @return The found Waypoint, or null if it does not exist.
     */
    public Waypoint get(String name) {
        if(name != null) return m.get(name);
        else return null;
    }

    /**
     * Generates a set of "default" waypoints sequentially to the given decimal
     * place. decimalPrecision values greater than 1 might take excessively long
     * to generate.
     * 
     * Don't use this. It's a performance killer.
     * 
     * @param xLowerBound
     *            The lower bound of X values
     * @param xUpperBound
     *            The upper bound of X values
     * @param yLowerBound
     *            The lower bound of Y values
     * @param yUpperBound
     *            The upper bound of Y values
     * @param decimalPrecision
     *            The decimal place to which values are generated. 0 is whole
     *            numbers, 1 to the tens, etc
     */
    @Deprecated
    public void generateDefaultMap(double xLowerBound, double xUpperBound,
            double yLowerBound, double yUpperBound, int decimalPrecision) {
        for(double y = yLowerBound; y <= yUpperBound; y += round(
                1 / Math.pow(10, decimalPrecision), decimalPrecision + 1)) {
            for(double x = xLowerBound; x <= xUpperBound; x += round(
                    1 / Math.pow(10, decimalPrecision), decimalPrecision + 1)) {
                Waypoint t = new Waypoint(round(x, decimalPrecision),
                        round(y, decimalPrecision));
                m.put(t.name, t);
            }
        }
    }

    /**
     * Helper method that rounds to the given decimal place. Cleans
     * floating-point imprecision.
     */
    private double round(double val, int place) {
        return Math.floor((val * Math.pow(10, place))) / Math.pow(10, place);
    }

    /**
     * Adds the given {@link Waypoint} to the {@link WaypointMap}.
     * 
     * @param toAdd
     *            The Waypoint to add
     */
    public void addWaypoint(Waypoint toAdd) {
        m.put(toAdd.getName(), toAdd);
    }

    /**
     * Removes the {@link Waypoint} associated with the key.
     * 
     * @param waypointName
     *            The key to remove a Waypoint from
     */
    public void removeWaypoint(String waypointName) {
        m.remove(waypointName);
    }

    /**
     * Returns a Constructor-readable representation of the {@link WaypointMap}.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if(inDegrees) sb.append("#$DEGREES\r\n");
        else sb.append("#$RADIANS\r\n");
        Iterator<String> iter = m.keySet().iterator();
        while(iter.hasNext())
            sb.append(m.get(iter.next()).toString() + "\r\n");
        return sb.toString();
    }

    /**
     * Returns a hashCode of the {@link WaypointMap}.
     */
    @Override
    public int hashCode() {
        String tmp = (inDegrees) ? "1" : "0";
        Iterator<String> iter = m.keySet().iterator();
        while(iter.hasNext())
            tmp += iter.next().hashCode();
        return (int) (Double.valueOf(tmp).doubleValue());
    }

    /**
     * Returns a new {@link WaypointMap} with identical internal data.
     */
    @Override
    public WaypointMap clone() {
        return new WaypointMap((HashMap<String, Waypoint>) this.m,
                this.inDegrees);
    }
}
