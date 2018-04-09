package disc.util;

/**
 * Simple Exception for Waypoint-related things. May have features or other
 * things added in the future.
 * 
 * @author Liam Williams
 * @version 0.1.1
 */
public class WaypointException extends Exception {

    private static final long serialVersionUID = 6685172926955766265L;

    public WaypointException(String message) {
        super(message);
    }

    public WaypointException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
