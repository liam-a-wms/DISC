package disc.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Data structure for storing an entire Scenario file's contents, as an array of
 * {@link Instruction}s with attached arguments such as the name and additional
 * information for control flow (eg start position for the given scenario).
 * <p>
 * Name and other arguments are looked for in any line starting with two '#'
 * characters.
 * <p>
 * eg:<br>
 * ##name=That Scenario -> Assigns a name of "That Scenario"<br>
 * ##startPos=s1 -> Assigns args[0] as "s1"<br>
 * ##anyArg=bohemia -> Assigns args[1] as "bohemia"<br>
 * <p>
 * The Scenario constructor does not care what the name of the argument is,
 * unless it is specifically "name", and it will place the arguments into the
 * "args" array in order of finding it in the file.
 * <p>
 * Comments are begun with a single '#' character, and ignored by the
 * constructor.
 * <p>
 * eg:<br>
 * #This is a comment.
 * 
 * @author Liam Williams
 * @version 0.3.5
 */
public class Scenario {

    String scenarioName;
    String[] args;
    Instruction[] instructions;

    /**
     * Constructs a {@link Scenario} from the given file.<br>
     * Calls the {@link Instruction} constructor for each non-comment, non-empty
     * line.
     * 
     * @param scenarioFile
     *            A File object of a Scenario file.
     * @throws FileNotFoundException
     *             If the file doesn't actually exist.
     */
    public Scenario(File scenarioFile) throws FileNotFoundException {
        parse(new Scanner(scenarioFile));
    }

    /**
     * Constructs a {@link Scenario} from the given String.<br>
     * Calls the {@link Instruction} constructor for each non-comment, non-empty
     * line.
     * 
     * @param scenarioFile
     *            A File object of a Scenario file.
     * @throws FileNotFoundException
     *             If the file doesn't actually exist.
     */
    public Scenario(String scenarioFile) {
        parse(new Scanner(scenarioFile));
    }

    /**
     * Constructs a new {@link Scenario} from the given parameters.<br>
     * Does not check for compliance.
     * 
     * @param name
     * @param args
     * @param instructions
     */
    public Scenario(String name, String[] args, Instruction[] instructions) {
        this.scenarioName = name;
        this.args = args;
        this.instructions = instructions;
    }

    /**
     * Helper method that reads the Scanner's data into the data structure.<br>
     * Does not check for compliance.
     * 
     * @param scn
     *            A Scanner containing a Scenario's data.
     */
    private void parse(Scanner scn) {
        ArrayList<String> toArgs = new ArrayList<String>(0);
        ArrayList<Instruction> toInstructions = new ArrayList<Instruction>(0);
        while(scn.hasNextLine()) {
            String tmp = scn.nextLine().trim();
            if(tmp.startsWith("##")) {
                if(tmp.startsWith("##name=")) scenarioName = tmp.substring(7);
                else toArgs.add(tmp.substring(tmp.indexOf('=') + 1));
            } else if(!((tmp.startsWith("#") || tmp.startsWith("# "))
                    || tmp.isEmpty()))
                toInstructions.add(new Instruction(tmp));
        }
        scn.close();
        args = toArgs.toArray(new String[toArgs.size()]);
        instructions = toInstructions.stream().toArray(Instruction[]::new);
    }

    /**
     * Converts the array of {@link Instruction}s into a Queue.
     * 
     * @return A LinkedList implementation of a Queue of Instructions.
     */
    public Queue<Instruction> getInstructionQueue() {
        Queue<Instruction> q = new LinkedList<Instruction>();
        for(Instruction i : instructions)
            q.add(i);
        return q;
    }

    /**
     * @return the array of {@link Instruction}s of the {@link Scenario}
     */
    public Instruction[] getInstructions() {
        return instructions;
    }

    /**
     * @return the name of the {@link Scenario}
     */
    public String getName() {
        return scenarioName;
    }

    /**
     * @return the array of args of the {@link Scenario}
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @return a String that fully represents the scenario, as would be found in
     *         a Scenario file.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("##name=" + scenarioName + "\r\n");
        for(int i = 0; i < args.length; i++)
            sb.append("##arg" + i + "=" + args[i] + "\r\n");
        sb.append("\r\n");
        for(int i = 0; i < instructions.length; i++)
            if(!instructions[i].toString().equals("NaN"))
                sb.append(instructions[i].toString() + "\r\n");
        return sb.toString();
    }

    /**
     * Calculates a hash code using the hashCode() methods of the
     * "scenarioName", "args", and each of the {@link Instruction}s stored in
     * the {@link Scenario}.
     * 
     * @return A unique hash code for this Scenario object
     */
    @Override
    public int hashCode() {
        String toOut = "";
        toOut += scenarioName.hashCode();
        for(String s : args)
            toOut += s.hashCode();
        for(Instruction i : instructions)
            toOut += i.hashCode();
        return (int) (Double.valueOf(toOut).doubleValue());
    }

    /**
     * @return an identical, but new {@link Scenario} object.
     */
    @Override
    public Scenario clone() {
        return new Scenario(this.scenarioName, this.args, this.instructions);
    }
}
