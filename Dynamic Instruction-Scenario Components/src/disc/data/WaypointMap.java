package disc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
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
		double x = xLowerBound;
		double y = yLowerBound;
		
		while(y <= yUpperBound) {
			do {
				Waypoint t = new Waypoint(x, y);
				m.put(t.getName(), t);
				x += (1 / (10 ^ decimalPrecision));
			} while(x <= xUpperBound);
			x = xLowerBound;
			y += (1 / (10 ^ decimalPrecision));
		}
	}
	
}
