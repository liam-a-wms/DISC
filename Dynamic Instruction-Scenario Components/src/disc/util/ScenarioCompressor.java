package disc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import disc.data.Scenario;

/**
 * Class for building an array of {@link Scenario}s and chaining them together
 * into a single file; or inversely deconstructing such chain into an array of
 * Scenarios and grabbing them as a traditional array or Queue data structure.
 * 
 * @author Liam Williams
 * @version 0.3.2
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
     * Constructor that immediately reads the given file into memory.
     * 
     * @param input
     *            The input file to be read
     * @throws FileNotFoundException
     *            If no such file exists
     */
    public ScenarioCompressor(File input) throws FileNotFoundException {
        scenarios = new ArrayList<Scenario>(0);
        this.decompress(input);
    }

    /**
     * Constructor that immediately reads the given String into internal data.
     * 
     * @param input
     *            The input String to be read
     */
    public ScenarioCompressor(String input) {
        scenarios = new ArrayList<Scenario>(0);
        this.decompress(input);
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
     * @param output
     *            The file to be chained into. Overwrites
     * @throws FileNotFoundException
     *            If the file cannot be created or accessed
     */
    public void compress(File output) throws FileNotFoundException {
        PrintWriter op = new PrintWriter(output);
        op.write(this.toString());
        op.close();
    }

    /**
     * Adds the {@link Scenario} to the {@link ScenarioCompressor}'s internal array.
     * 
     * @param toAdd
     *            the Scenario to add
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
     * Decompresses the given file into the internal array. Will clear the internal
     * array.
     * 
     * @param input
     *            The file to be decompressed
     * @throws FileNotFoundException
     *            If the file does not exist or cannot be accessed
     */
    public void decompress(Scanner scn) {
        this.clearScenarioList();
        StringBuilder sb = new StringBuilder("");
        while(scn.hasNextLine()) {
            String tmp = scn.nextLine().trim();
            if(tmp.contains("#!") && !sb.toString().isEmpty()) {
                scenarios.add(new Scenario(sb.toString()));
                sb = new StringBuilder("");
            } else if(tmp.isEmpty()) ;
            else sb.append(tmp + "\r\n");
        }
        scn.close();
        this.clean();
    }

    /**
     * "Decompresses" the given file into the internal array. Will clear the
     * internal array.
     * 
     * @param input
     *            The file to be read
     * @throws FileNotFoundException
     *            If the file does not exist or cannot be accessed
     */
    public void decompress(File input) throws FileNotFoundException {
        decompress(new Scanner(input));
    }

    /**
     * "Decompresses" the given String into the internal array. Will clear the
     * internal array.
     * 
     * @param input
     *            The String to be read
     */
    public void decompress(String input) {
        decompress(new Scanner(input));
    }

    /**
     * @return a standard array copy of the internal {@link Scenario} array
     */
    public Scenario[] getScenarios() {
        return scenarios.stream().toArray(Scenario[]::new);
    }

    /**
     * Searches the array of {@link Scenario}s for the first match by name.
     * 
     * @param name
     *            The name to match with a Scenario
     * @return The found Scenario, or null if it wasn't found
     */
    public Scenario getScenarioByName(String name) {
        return Arrays.stream(this.getScenarios()).filter(s -> s.getName().equals(name)).findFirst()
                .orElse(null);
    }

    /**
     * Searches the array of {@link Scenario}s for the first match by exact arg
     * array.
     * 
     * @param args
     *            The exact arg array to match with a Scenario
     * @return The found Scenario, or null if it wasn't found
     */
    public Scenario getScenarioByExactArg(String[] args) {
        return Arrays.stream(this.getScenarios()).filter(s -> Arrays.equals(s.getArgs(), args))
                .findFirst().orElse(null);
    }

    /**
     * Seaches the array of {@link Scenario}s for the first match by containment of
     * the given args.
     * 
     * @param args
     *            The arg array to check for containment in any Scenario
     * @return The found Scenario, or null if it wasn't found.
     */
    public Scenario getScenarioByArgContainment(String[] args) {
        for(Scenario s : this.getScenarios()) {
            int i = 0;
            for(String str : s.getArgs()) {
                if(Arrays.stream(args).filter(string -> string.equals(str)).findFirst()
                        .orElse(null) != null)
                    i++;
            }
            if(i == args.length) return s;
        }
        return null;
    }

    /**
     * @return a Queue array built from the internal {@link Scenario} array
     */
    public Queue<Scenario> getScenariosAsQueue() {
        Queue<Scenario> toReturn = new LinkedList<Scenario>();
        for(Scenario s : this.getScenarios())
            toReturn.add(s);
        return toReturn;
    }

    /**
     * Removes any empty scenarios that may have been accidentally added during
     * parsing of a file.
     */
    private void clean() {
        Scenario tmp = null;
        tmp = this.getScenarioByName(null);
        if(tmp != null && tmp.getArgs().length < 1) {
            scenarios.remove(tmp);
            this.clean();
        }
    }

    /**
     * @return The String representation of the {@link Scenario}s in this object, as
     *         they would be seen in a file.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("#! BEGIN SCENARIO CHAIN\r\n");
        Scenario[] scene = this.getScenarios();
        for(Scenario s : scene) {
            sb.append(s.toString() + "\r\n");
            sb.append("#!\r\n");
        }
        sb.append("#END SCENARIO CHAIN");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        Scenario[] op = this.getScenarios();
        long toOut = 0;
        for(Scenario s : op)
            toOut += s.hashCode();
        return (int) Math.pow(toOut, (1 / op.length));
    }

    @Override
    public ScenarioCompressor clone() {
        return new ScenarioCompressor(this.scenarios);
    }

}
