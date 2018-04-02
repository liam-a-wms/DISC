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

public class DISCBuilder {

	WaypointMap map;
	ScenarioCompressor scenarios = null;
	
	public DISCBuilder() {
		map = new WaypointMap();
		scenarios = new ScenarioCompressor();
	}
	
	public DISCBuilder(WaypointMap map, ScenarioCompressor scenarios) {
		this.map = map;
		this.scenarios = scenarios;
	}
	
	public DISCBuilder(WaypointMap map, Scenario[] scenarios) {
		this.map = map;
		this.scenarios = new ScenarioCompressor(new ArrayList<Scenario>(Arrays.asList(scenarios)));
	}
	
	public DISCBuilder(Waypoint[] map, ScenarioCompressor scenarios) {
		this.map = new WaypointMap();
		for(Waypoint w : map) this.map.addWaypoint(w);
		this.scenarios = scenarios;
	}
	
	public DISCBuilder(Waypoint[] map, Scenario[] scenarios) {
		this.map = new WaypointMap();
		for(Waypoint w : map) this.map.addWaypoint(w);
		this.scenarios = new ScenarioCompressor(new ArrayList<Scenario>(Arrays.asList(scenarios)));
	}
	
	public DISCBuilder(File input) throws FileNotFoundException {
		parse(new Scanner(input));
	}
	
	public DISCBuilder(String input) {
		parse(new Scanner(input));
	}
	
	private void parse(Scanner scn) {
		this.clearData();
		boolean isMap = false;
		StringBuilder sb = new StringBuilder("");
		while(scn.hasNextLine()) {
			String tmp = scn.nextLine().trim();
			if(tmp.contains("#$") && !sb.toString().isEmpty()) {
				if(isMap) {
					map = new WaypointMap(sb.toString());
					isMap = false;
				} else {
					scenarios.addScenario(new Scenario(sb.toString()));
				}
				sb = new StringBuilder("");
			} else if(tmp.contains("#$") && !sb.toString().isEmpty()) {
				isMap = true;
			} else if(tmp.isEmpty());
			else sb.append(tmp + "\r\n");
		}
		scn.close();
	}
	
	public void clearData() {
		map = new WaypointMap();
		if(scenarios == null) scenarios = new ScenarioCompressor();
		else scenarios.clearScenarioList();
	}
	
	public void write(File output) throws FileNotFoundException {
		PrintWriter op = new PrintWriter(output);
		op.print(this.toString());
		op.close();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("#$BEGIN WAYPOINTMAP\r\n");
		sb.append(map.toString() + "\r\n");
		sb.append("#$BEGIN SCENARIO LIST\r\n");
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
