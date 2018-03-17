package disc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import disc.util.WaypointException;

public class WaypointMap {
	
	Map<String, Waypoint> m = new HashMap<String, Waypoint>();
	boolean inDegrees = false;
	
	public WaypointMap() {}
	
	public WaypointMap(File input) throws FileNotFoundException {
		parse(new Scanner(input));
	}
	
	public WaypointMap(String input) {
		parse(new Scanner(input));
	}
	
	public WaypointMap(HashMap<String, Waypoint> map, boolean inDegrees) {
		this.m = map;
		this.inDegrees = inDegrees;
	}
	
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
				} catch (WaypointException e) {
					e.printStackTrace();
				}
			}
			
		}
		scn.close();
	}

	public void generateDefaultMap(double xLowerBound, double xUpperBound, double yLowerBound, double yUpperBound, int decimalPrecision) {
		for(double y = yLowerBound; y <= yUpperBound; y += round(1 / Math.pow(10, decimalPrecision), decimalPrecision + 1)) {
			for(double x = xLowerBound; x <= xUpperBound; x += round(1 / Math.pow(10, decimalPrecision), decimalPrecision + 1)) {
				Waypoint t = new Waypoint(round(x, decimalPrecision), round(y, decimalPrecision));
				m.put(t.name, t);
			}
		}
	}
	
	private double round(double val, int place) {
		return Math.floor((val * Math.pow(10, place))) / Math.pow(10, place);
	}
	
	public void addWaypoint(Waypoint toAdd) {
		m.put(toAdd.getName(), toAdd);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		if(inDegrees) sb.append("#$DEGREES\r\n");
		else sb.append("#$RADIANS\r\n");
		Iterator<String> iter = m.keySet().iterator();
		while(iter.hasNext()) sb.append(m.get(iter.next()).toString() + "\r\n");
		return sb.toString();
	}
	
}
