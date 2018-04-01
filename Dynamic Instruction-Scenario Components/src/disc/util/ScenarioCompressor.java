package disc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import disc.data.Scenario;

/**
 * Class for building an array of {@link Scenario}s and chaining them together into a single file;
 * or inversely deconstructing such chain into an array of Scenarios and grabbing them as a traditional
 * array or Queue data structure.
 * 
 * @author Liam Williams
 * @version 0.2.1
 */
public class ScenarioCompressor {

	ArrayList<Scenario> scenarios;
	
	/**
	 * Standard constructor to build a {@link ScenarioCompressor}
	 */
	public ScenarioCompressor() {
		scenarios = new ArrayList<Scenario>(0);
	}
	
	/**
	 * Constructor primarily for the clone() method
	 */
	public ScenarioCompressor(ArrayList<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	/**
	 * Compresses the internal array of {@link Scenario}s into a file
	 * 
	 * @param output The file to be chained into. Overwrites
	 * @throws FileNotFoundException If the file cannot be created or accessed
	 */
	public void compress(File output) throws FileNotFoundException {
		PrintWriter op = new PrintWriter(output);
		op.write("#! BEGIN SCENARIO CHAIN\r\n");
		Scenario[] scene = this.getScenarios();
		for(Scenario s : scene) {
			op.println(s.toString());
			op.print("#!\r\n");
		}
		op.print("#END SCENARIO CHAIN");
		op.close();
	}
	
	/**
	 * Adds the {@link Scenario} to the {@link ScenarioCompressor}'s internal array.
	 * 
	 * @param toAdd the Scenario to add
	 */
	public void addScenario(Scenario toAdd) {
		scenarios.add(toAdd);
	}
	
	/**
	 * Empties the internal {@link Scenario} array.
	 */
	public void clearScenarioList() {
		scenarios = new ArrayList<Scenario>(0);
	}
	
	/**
	 * Decompresses the given file into the internal array. Will not clear the internal array.
	 * 
	 * @param input The file to be decompressed
	 * @throws FileNotFoundException If the file does not exist or cannot be accessed
	 */
	public void decompress(File input) throws FileNotFoundException {
		this.clearScenarioList();
		Scanner scn = new Scanner(input);
		StringBuilder sb = new StringBuilder("");
		while(scn.hasNextLine()) {
			String tmp = scn.nextLine().trim();
			if(tmp.contains("#!") && !sb.toString().isEmpty()) {
				scenarios.add(new Scenario(sb.toString()));
				sb = new StringBuilder("");
			} else if(tmp.isEmpty());
			else sb.append(tmp + "\r\n");
		}
		scn.close();
	}
	
	/**
	 * @return a standard array copy of the internal {@link Scenario} array
	 */
	public Scenario[] getScenarios() {
		return scenarios.stream().toArray(Scenario[]::new);
	}
	
	public Scenario getScenarioByName(String name) {
		return Arrays.stream(this.getScenarios())
					 .filter(s -> s.getName().equals(name))
					 .findFirst()
					 .orElse(null);
	}
	
	public Scenario getScenarioByExactArg(String[] args) {
		return Arrays.stream(this.getScenarios())
					 .filter(s -> Arrays.equals(s.getArgs(), args))
					 .findFirst()
					 .orElse(null);		
	}
	
	public Scenario getScenarioByArgContainment(String[] args) {
		for(Scenario s : this.getScenarios()) {
			int i = 0;
			for(String str : s.getArgs()) {
				if(Arrays.stream(args)
						 .filter(string -> string.equals(str))
						 .findFirst()
						 .orElse(null) != null) i++;
			}
			if (i == args.length) return s;
		}
		return null;
	}
	
	/**
	 * @return a Queue array built from the internal {@link Scenario} array
	 */
	public Queue<Scenario> getScenariosAsQueue() {
		Queue<Scenario> toReturn = new LinkedList<Scenario>();
		for(Scenario s : this.getScenarios()) toReturn.add(s);
		return toReturn;
	}
	
}
