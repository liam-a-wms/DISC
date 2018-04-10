package disc.data;

import java.util.ArrayList;

/**
 * Simple data structure for storing an Instruction, parsed from a Scenario
 * file, designed for FRC. Examples of a line from a scenario file are as such:
 * <ul>
 * <p>
 * For a Delimiter: start, s1
 * <p>
 * For a Control State: control.set, manual
 * <p>
 * For a Command: waypoint.goto, cube
 * <p>
 * </ul>
 * An Instruction may have any number of arguments to it, and the first arg is
 * the word immediately following the first word on the line. The first word on
 * the line, if not a special indicator for a delimiter or control state, will
 * be stored in the "target" value. If the type is not a Command, then "target"
 * will contain "". Commas and periods are not included, as they are separators
 * and not necessary for the execution of an Instruction.
 * <p>
 * 
 * @author Liam Williams
 * @version 0.3.7
 */
public class Instruction {

    String target;
    String[] args;
    InstructionType t;

    /**
     * Constructs the {@link Instruction} from a raw, unprocessed line of a
     * scenario file.
     * <p>
     * Does not know to ignore a comment.
     * <p>
     * 
     * @param rawInstruction
     */
    public Instruction(String rawInstruction) {
        int i = 0;
        rawInstruction = rawInstruction.trim();
        i = rawInstruction.indexOf('.');
        if(i == -1) i = rawInstruction.indexOf(',');
        if(i != -1) {
            String temp = rawInstruction.substring(0, i);
            if(temp.equals("start") || temp.equals("stop")) {
                t = InstructionType.DELIMITER;
                target = "";
                args = new String[] {temp,
                        rawInstruction.substring(i + 1).trim()};
            } else if(temp.equals("control")) {
                int o = 0;
                t = InstructionType.CONTROL_STATE;
                target = "";
                ArrayList<String> a = new ArrayList<String>(0);
                while(rawInstruction.indexOf(",", i + 1) > -1) {
                    o = rawInstruction.indexOf(",", i + 1);
                    a.add(rawInstruction.substring(i + 1, o).trim());
                    i = o;
                }
                a.add(rawInstruction.substring(i + 1).trim());
                args = a.stream().toArray(String[]::new);
            } else {
                int o = 0;
                target = temp;
                t = InstructionType.COMMAND;
                ArrayList<String> a = new ArrayList<String>(0);
                while(rawInstruction.indexOf(",", i + 1) > -1) {
                    o = rawInstruction.indexOf(",", i + 1);
                    a.add(rawInstruction.substring(i + 1, o).trim());
                    i = o;
                }
                a.add(rawInstruction.substring(i + 1).trim());
                args = a.stream().toArray(String[]::new);
            }
        } else {
            target = null;
            args = null;
            t = null;
        }
    }

    /**
     * Constructs an {@link Instruction} from the given parameters.
     * <p>
     * For the manual creation of Delimiter and Control State Instruction types.
     * <p>
     * 
     * @param T
     *            The type of Instruction
     * @param args
     *            The arguments for the Instruction
     */
    public Instruction(InstructionType T, String[] args) {
        this.t = T;
        this.target = "";
        this.args = args;
    }

    /**
     * Constructs an {@link Instruction} from the given parameters.
     * <p>
     * For the manual creation of Command Instruction types.
     * <p>
     * 
     * @param T
     *            The type of Instruction
     * @param target
     *            The target of the Instruction
     * @param args
     *            The arguments for the Instruction
     */
    public Instruction(InstructionType T, String target, String[] args) {
        this.t = T;
        this.target = target;
        this.args = args;
    }

    /**
     * @return the value stored in "target".
     */
    public String getTarget() {
        return target;
    }

    /**
     * @return the args stored for the Instruction.
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * @return the number of args stored for the Instruction.
     */
    public int getNumberOfArgs() {
        return args.length;
    }

    /**
     * @return the type stored for the Instruction.
     */
    public InstructionType getT() {
        return t;
    }

    /**
     * @return the String as would be seen in a Scenario file.
     */
    @Override
    public String toString() {
        String toOut;
        if(t == InstructionType.CONTROL_STATE) {
            toOut = "control." + args[0];
            for(int i = 1; i < args.length; i++) {
                toOut += ", " + args[i];
            }
            return toOut;
        } else if(t == InstructionType.DELIMITER) {
            return args[0] + ", " + args[1];
        } else if(t == InstructionType.COMMAND) {
            toOut = target + "." + args[0];
            for(int i = 1; i < args.length; i++) {
                toOut += ", " + args[i];
            }
            return toOut;
        }
        return "NaN";
    }

    /**
     * Returns a hash code for this Instruction. Calculated with the
     * InstructionType enum ordering for the first character, then "target"'s
     * hashCode(), then each of the args' hashCode() results.
     * <p>
     * 
     * @return a hash code value for this Instruction object
     */
    @Override
    public int hashCode() {
        if(t != null) {
            StringBuilder sb = new StringBuilder("");
            switch(t) {
                case COMMAND:
                    sb.append("1");
                    break;
                case DELIMITER:
                    sb.append("2");
                    break;
                case CONTROL_STATE:
                    sb.append("3");
                    break;
            }
            if(target != null) sb.append(target.hashCode());
            for(int i = 0; i < args.length; i++)
                sb.append(args[i].hashCode());

            return (int) (Double.valueOf(sb.toString().replaceAll("[^0-9]", ""))
                    .doubleValue());
        }
        return 0;
    }

    /**
     * @return an identical, but new {@link Instruction} object.
     */
    @Override
    public Instruction clone() {
        return new Instruction(this.t, this.target, this.args);
    }

    /**
     * Defines the Instruction type, to allow easy control flow.
     */
    public enum InstructionType {
        COMMAND, DELIMITER, CONTROL_STATE
    }
}
