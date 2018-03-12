package disc.data;

public class Waypoint {

	String name;
	double x;
	double y;
	double z;
	double heading;
	double roll;
	double pitch;
	
	public Waypoint(String arg0) {
		//TODO add String translation
	}
	
	public Waypoint(String name, double x, double y, double z, double heading, double roll, double pitch) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
		this.roll = roll;
		this.pitch = pitch;
	}
	
}
