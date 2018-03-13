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
				x = Double.valueOf(arg0.substring(o + 1, i));
				o = i;
				i = arg0.indexOf(',', o + 1);
			} else throw new WaypointException("Invalid Waypoint: does not define data.");
			if(i != -1) {
				y = Double.valueOf(arg0.substring(o + 1, i));
				o = i;
				i = arg0.indexOf(',', o + 1);
			} else throw new WaypointException("Invalid Waypoint: does not define the minimum amount of data.");
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
			} else if(o != -1){
				heading = Double.valueOf(arg0.substring(o + 1));
				roll = 0;
				pitch = 0;
				o = -1;
			}
			if(i != -1 && o != -1) {
				roll = Double.valueOf(arg0.substring(o + 1, i));
				o = i;
				i = arg0.indexOf(',', o + 1);
			} else if(o != -1){
				roll = Double.valueOf(arg0.substring(o + 1));
				pitch = 0;
				o = -1;
			}
			if(i != -1 && o != -1) pitch = Double.valueOf(arg0.substring(i));
			else if(o != -1) pitch = Double.valueOf(o + 1);
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
	
	@Override
	public String toString() {
		return name + ": " + x + ", " + y + ", " + z + ", " + heading + ", " + roll + ", " + pitch;
	}
	
	@Override
	public int hashCode() {
		String tmp = Integer.toString(new Double(x).hashCode())
				+	 Integer.toString(new Double(y).hashCode())
				+	 Integer.toString(new Double(z).hashCode())
				+	 Integer.toString(new Double(heading).hashCode())
				+	 Integer.toString(new Double(roll).hashCode())
				+	 Integer.toString(new Double(pitch).hashCode());
		return Integer.valueOf(tmp);
	}
	
	@Override
	public Waypoint clone() {
		return new Waypoint(this.name, this.x, this.y, this.z, this.heading, this.roll, this.pitch);
	}
	
}
