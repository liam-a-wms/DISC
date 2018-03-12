package disc.util;

public class WaypointException extends Exception {

	private static final long serialVersionUID = 6685172926955766265L;

	public WaypointException(String message) {
		super(message);
	}
	
	public WaypointException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
