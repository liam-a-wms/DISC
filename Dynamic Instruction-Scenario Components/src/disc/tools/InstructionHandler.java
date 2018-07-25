package disc.tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

import disc.data.Instruction;

/**
 * Given the {@link Instruction}, {@link Directory}, and active heap of the
 * {@link Interpreter}, reads the Instruction's data and acts on it. Uses the
 * Directory to look up a method and execute it with the given args. Halts if
 * the method doesn't exist or another problem arises, which will cause the
 * Interpreter to move on to the next Instruction by calling a new
 * InstructionHandler.
 * 
 * @author Liam Williams
 * @version 1.0.1
 */
class InstructionHandler implements Runnable {

    Instruction inst;
    Directory dir;
    Map<String, String> heap;

    /**
     * Creates a new {@link InstructionHandler}.
     * 
     * @param toExecute
     *            The {@link Instruction} to read and act on
     * @param dir
     *            The {@link Directory} to use methods from
     * @param heap
     *            The heap to store data in (It's actually a Map but don't worry
     *            about it)
     */
    public InstructionHandler(Instruction toExecute, Directory dir,
            Map<String, String> heap) {
        inst = toExecute;
        this.dir = dir;
        this.heap = heap;
    }

    @Override
    public void run() {
        String[] args = inst.getArgs();
        ArrayList<String> argList = new ArrayList<>(args.length);
        String r = "";
        for(int i = 1; i < args.length; i++) {
            if(!args[i].contains("return")) {
                if(checkInHeap(args[i].trim()))
                    argList.add(heap.get(args[i].trim()));
                else argList.add(args[i].trim());
            } else r = parseVar(args[i]);
        }
        Object toRunInstance = dir.lookupObject(inst.getTarget());
        Method toRun = dir.lookupMethod(inst.getTarget(), args[0],
                argList.size());
        args = argList.toArray(new String[argList.size()]);
        Object[] objectArgs = parseArgs(args, toRun);
        
            try {
                if(!r.isEmpty()) heap.put(r,
                        toRun.invoke(toRunInstance, objectArgs).toString());
                else toRun.invoke(toRunInstance, objectArgs);
            } catch(Exception e) {
                e.printStackTrace();
            }
        
    }

    /**
     * Reads the parameter types of the given method and converts the arg array
     * to match. Assumes correct count.
     * 
     * @param args
     *            The args array to convert
     * @param m
     *            The method to source types from
     * @return The converted array, with matching types to the method's
     *         parameter types
     */
    private Object[] parseArgs(String[] args, Method m) {
        Class<?>[] c = m.getParameterTypes();
        Object[] o = new Object[c.length];

        for(int i = 0; i < c.length; i++)
            o[i] = parseSingleArg(args[i], c[i]);

        return o;
    }

    /**
     * Helper method for parseArgs() that does the actual logic for each step of
     * the for loop. Can handle any type.
     * 
     * @param arg
     *            The arg to convert
     * @param argType
     *            The type to convert to
     * @return The converted arg
     */
    private Object parseSingleArg(String arg, Class<?> argType) {
        if(argType.equals(Integer.TYPE)) return Integer.valueOf(arg);
        else if(argType.equals(Double.TYPE)) return Double.valueOf(arg);
        else if(argType.equals(Boolean.TYPE)) return Boolean.valueOf(arg);
        else if(argType.equals(Byte.TYPE)) return Byte.valueOf(arg);
        else if(argType.equals(Long.TYPE)) return Long.valueOf(arg);
        else if(argType.equals(Character.TYPE)) return arg.charAt(0);
        else if(argType.equals(Short.TYPE)) return Short.valueOf(arg);
        else if(argType.equals(Float.TYPE)) return Float.valueOf(arg);
        else try {
            return argType.getDeclaredConstructor(String.class)
                    .newInstance(arg);
        } catch(Exception e) {
            return arg;
        }
    }

    /**
     * Checks the variable storage heap for a matching name.
     * 
     * @param varName
     *            The name to look for
     * @return A true or false value indicating whether or not the key exists
     */
    private boolean checkInHeap(String varName) {
        return heap.keySet().stream().anyMatch(s -> s.equals(varName));
    }

    /**
     * Grabs the name of the variable from an arg containing the word "return"
     * 
     * @param arg0
     *            A return arg to be parsed
     * @return The name of the expected return key
     */
    private String parseVar(String arg0) {
        return arg0.trim().substring(6).trim();
    }

}
