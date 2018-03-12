package disc.data;

import disc.util.WaypointException;

public class Waypoint {

	protected String name;
	protected double x,
					 y,
					 z,
					 heading,
					 roll,
					 pitch;
	
	public Waypoint(String arg0) throws WaypointException {
		int o = arg0.indexOf(':');
		if(o != -1) {
			this.name = arg0.substring(0, o);
			int i = arg0.indexOf(',');
			if(i != -1) {
				x = Double.valueOf(arg0.substring(o + 1, i - 1));
				o = i;
				i = arg0.indexOf(o + 1, ',');
			} else throw new WaypointException("Invalid Waypoint: does not define data.");
			if(i != -1) {
				y = Double.valueOf(arg0.substring(o + 1, i - 1));
				o = i;
				i = arg0.indexOf(o + 1, ',');
			} else throw new WaypointException("Invalid Waypoint: does not define the minimum amount of data.");
			if(i != -1) {
				z = Double.valueOf(arg0.substring(o + 1, i - 1));
				o = i;
				i = arg0.indexOf(o + 1, ',');
			} else {
				z = 0;
				heading = 0;
				roll = 0;
				pitch = 0;
			}
			if(i != -1) {
				heading = Double.valueOf(arg0.substring(o + 1, i - 1));
				o = i;
				i = arg0.indexOf(o + 1, ',');
			} else {
				heading = 0;
				roll = 0;
				pitch = 0;
			}
			if(i != -1) {
				roll = Double.valueOf(arg0.substring(o + 1, i - 1));
				o = i;
				i = arg0.indexOf(o + 1, ',');
			} else {
				roll = 0;
				pitch = 0;
			}
			if(i != -1) pitch = Double.valueOf(arg0.substring(i + 1));
			else pitch = 0;
		} else throw new WaypointException("Invalid Waypoint: does not define name.");
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

	public String getName() {
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getHeading() {
		return heading;
	}

	public double getRoll() {
		return roll;
	}

	public double getPitch() {
		return pitch;
	}
	
}
