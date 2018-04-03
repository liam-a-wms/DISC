package disc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import disc.data.Scenario;
import disc.data.Waypoint;
import disc.data.WaypointMap;

/**
 * Class for putting together an entire file with a WaypointMap, and chained Scenario list.
 * Works by using both the Scenario and WaypointMap parsers and writers.
 * <br><br>
 * Uses "#%" as a separator.
 * 
 * @author Liam Williams
 * @version 0.1.0
 */
public class DISCBuilder {

	WaypointMap map;
	ScenarioCompressor scenarios = null;
	
	/**
	 * Standard constructor for making an empty {@link DISCBuilder}.
	 */
	public DISCBuilder() {
		map = new WaypointMap();
		scenarios = new ScenarioCompressor();
	}
	
	/**
	 * Constructor primarily for the clone() method.
	 * 
	 * @param map a WaypointMap
	 * @param scenarios a ScenarioCompressor
	 */
	public DISCBuilder(WaypointMap map, ScenarioCompressor scenarios) {
		this.map = map;
		this.scenarios = scenarios;
	}
	
	/**
	 * @param map a WaypointMap
	 * @param scenarios an array of Scenarios
	 */
	public DISCBuilder(WaypointMap map, Scenario[] scenarios) {
		this.map = map;
		this.scenarios = new ScenarioCompressor(new ArrayList<Scenario>(Arrays.asList(scenarios)));
	}
	
	/**
	 * @param map an array of Waypoints
	 * @param scenarios a ScenarioCompressor
	 */
	public DISCBuilder(Waypoint[] map, ScenarioCompressor scenarios) {
		this.map = new WaypointMap();
		for(Waypoint w : map) this.map.addWaypoint(w);
		this.scenarios = scenarios;
	}
	
	/**
	 * @param map an array of Waypoints
	 * @param scenarios an array of Scenarios
	 */
	public DISCBuilder(Waypoint[] map, Scenario[] scenarios) {
		this.map = new WaypointMap();
		for(Waypoint w : map) this.map.addWaypoint(w);
		this.scenarios = new ScenarioCompressor(new ArrayList<Scenario>(Arrays.asList(scenarios)));
	}
	
	/**
	 * Builds a DISCBuilder and immediately reads a file into internal data structures.
	 * 
	 * @param input The file to be read
	 * @throws FileNotFoundException If the file doesn't exist or can't be accessed
	 */
	public DISCBuilder(File input) throws FileNotFoundException {
		parse(new Scanner(input));
	}
	
	/**
	 * Builds a DISCBuilder and immediately reads a String into internal data structures.
	 * 
	 * @param input The String to be read
	 */
	public DISCBuilder(String input) {
		parse(new Scanner(input));
	}
	
	/**
	 * Parses the data in the given Scanner into internal data structures. Clears internal data.
	 * 
	 * @param scn A non-empty scanner created from a file or String
	 */
	public void parse(Scanner scn) {
		this.clearData();
		boolean isMap = false;
		StringBuilder sb = new StringBuilder("");
		while(scn.hasNextLine()) {
			String tmp = scn.nextLine().trim();
			if(tmp.contains("#%") && !sb.toString().isEmpty()) {
				if(isMap) {
					map = new WaypointMap(sb.toString());
					isMap = false;
				} else {
					scenarios.addScenario(new Scenario(sb.toString()));
				}
				sb = new StringBuilder("");
			} else if(tmp.contains("#%") && !sb.toString().isEmpty()) {
				isMap = true;
			} else if(tmp.isEmpty());
			else sb.append(tmp + "\r\n");
		}
		scn.close();
	}
	
	/**
	 * Empties the internal data structures of all information.
	 */
	public void clearData() {
		map = new WaypointMap();
		if(scenarios == null) scenarios = new ScenarioCompressor();
		else scenarios.clearScenarioList();
	}
	
	/**
	 * Writes the internal data to a file. Overwrites existing data.
	 * 
	 * @param output The file to be written to/created
	 * @throws FileNotFoundException
	 */
	public void write(File output) throws FileNotFoundException {
		PrintWriter op = new PrintWriter(output);
		op.write(this.toString());
		op.close();
	}
	
	/**
	 * Sets the internal WaypointMap.
	 * 
	 * @param map a WaypointMap
	 */
	public void setMap(WaypointMap map) {
		this.map = map;
	}

	/**
	 * @return The internally stored WaypointMap
	 */
	public WaypointMap getMap() {
		return this.map;
	}
	
	/**
	 * Sets the internal ScenarioCompressor.
	 * 
	 * @param scenarios a ScenarioCompressor
	 */
	public void setScenarios(ScenarioCompressor scenarios) {
		this.scenarios = scenarios;
	}
	
	/**
	 * @return The internally stored ScenarioCompressor
	 */
	public ScenarioCompressor getScenarios() {
		return this.scenarios;
	}
	
	/**
	 * @return The internally stored data as would be written to a file
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("#% BEGIN WAYPOINTMAP\r\n");
		sb.append(map.toString() + "\r\n");
		sb.append("#% BEGIN SCENARIO LIST\r\n");
		sb.append(scenarios.toString());
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return (int) Math.sqrt(map.hashCode() + scenarios.hashCode());
	}
	
	@Override
	public DISCBuilder clone() {
		return new DISCBuilder(map, scenarios);
	}
}
