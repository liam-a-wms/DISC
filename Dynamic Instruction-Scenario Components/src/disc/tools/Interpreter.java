package disc.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import disc.data.Instruction;
import disc.data.Scenario;

/**
 * Dynamic interpreter that can run an entire {@link Scenario} using method lookups
 * and a variable heaps. Can handle a variable return from a method and then variable
 * substitution in a later {@link Instruction}. Defaults to discarding the method return
 * unless the Instruction specifically has a "return" arg in it giving the variable a name
 * (eg "return number"). If objects are being passed around as variables then lossless 
 * serialization (using the toString() method) and deserialization (using a Constructor
 * with a String as the only argument) is expected.
 * <br>
 * The Interpreter uses two Threads. One for the Interpreter itself (which only happens if you
 * properly call start() instead of run()) and one for handling each Instruction. If an 
 * Instruction or called method causes an error or Exception, the Thread will stop and the
 * next Instruction will be picked up and handled by a new Thread.
 * 
 * @author Liam Williams
 * @version 0.1.0
 */
public class Interpreter extends Thread {

    protected ExecutorService executor = Executors.newSingleThreadExecutor();

    protected Directory dir;
    protected Scenario work;
    protected Queue<Instruction> q;
    protected Map<String, String> heap = new HashMap<>();
    protected long timeout = 1000;

    /**
     * Instantiates a new {@link Interpreter} with the given {@link Directory}
     * to execute the given {@link Scenario}.
     * 
     * @param dir The Directory to lookup methods from
     * @param scenario The Scenario to execute
     */
    public Interpreter(Directory dir, Scenario scenario) {
        this.dir = dir;
        this.work = scenario;
    }

    /**
     * Pulls the Queue of {@link Instruction}s from the {@link Scenario}.
     */
    private void init() {
        q = work.getInstructionQueue();
    }

    /**
     * Use start() to have this method called inside its own thread; runs the
     * interpreter.
     */
    public void run() {
        if(work != null) init();
        else q = null;

        Future<?> f = null;

        while(q.peek() != null) {
            if(f == null || f.isDone()) {
                f = executor
                        .submit(new InstructionHandler(q.remove(), dir, heap));
            }
            try {
                Interpreter.sleep(5);
            } catch(InterruptedException e) {}
        }
    }

}

/**
 * Given the {@link Instruction}, {@link Directory}, and active heap of the 
 * {@link Interpreter}, reads the Instruction's data and acts on it. Uses the
 * Directory to look up a method and execute it with the given args. Halts if
 * the method doesn't exist or another problem arises, which will cause the
 * Interpreter to move on to the next Instruction by calling a new
 * InstructionHandler.
 * 
 * @author Liam Williams
 * @version 1.0.0
 */
class InstructionHandler implements Runnable {

    Instruction inst;
    Directory dir;
    Map<String, String> heap;

    /**
     * Creates a new {@link InstructionHandler}.
     * 
     * @param toExecute The {@link Instruction} to read and act on
     * @param dir The {@link Directory} to use methods from
     * @param heap The heap to store data in (It's actually a Map but don't worry about it)
     */
    public InstructionHandler(Instruction toExecute, Directory dir,
            Map<String, String> heap) {
        inst = toExecute;
        this.dir = dir;
        this.heap = heap;
    }

    @Override
    public void run() {
        String returnVarName;
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
            if(!r.isEmpty())
                heap.put(r, toRun.invoke(toRunInstance, objectArgs).toString());
            else toRun.invoke(toRunInstance, objectArgs);
        } catch(IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the parameter types of the given method and converts the arg
     * array to match. Assumes correct count.
     * 
     * @param args The args array to convert
     * @param m The method to source types from
     * @return The converted array, with matching types to the method's parameter types
     */
    private Object[] parseArgs(String[] args, Method m) {
        Class<?>[] c = m.getParameterTypes();
        Object[] o = new Object[c.length];

        for(int i = 0; i < c.length; i++) {
            if(c[i].equals(Integer.TYPE) || c[i] == int.class) {
                o[i] = Integer.valueOf(args[i]);
            } else if(c[i].equals(Double.TYPE) || c[i] == double.class) {
                o[i] = Double.valueOf(args[i]);
            } else if(c[i].equals(Boolean.TYPE) || c[i] == boolean.class) {
                o[i] = Boolean.valueOf(args[i]);
            } else if(c[i].equals(Byte.TYPE) || c[i] == byte.class) {
                o[i] = Byte.valueOf(args[i]);
            } else if(c[i].equals(Long.TYPE) || c[i] == long.class) {
                o[i] = Long.valueOf(args[i]);
            } else if(c[i].equals(Character.TYPE) || c[i] == char.class) {
                o[i] = args[i].charAt(0);
            } else if(c[i].equals(Short.TYPE) || c[i] == short.class) {
                o[i] = Short.valueOf(args[i]);
            } else if(c[i].equals(Float.TYPE) || c[i] == float.class) {
                o[i] = Float.valueOf(args[i]);
            } else {
                try {
                    o[i] = c[i].getDeclaredConstructor(String.class).newInstance(args[i]);
                } catch(InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    o[i] = args[i];
                }
            }
        }
        return o;
    }

    /**
     * Checks the variable storage heap for a matching name.
     * 
     * @param varName The name to look for
     * @return A true or false value indicating whether or not the key exists
     */
    private boolean checkInHeap(String varName) {
        return heap.keySet().stream().anyMatch(s -> s.equals(varName));
    }

    /**
     * Grabs the name of the variable from an arg containing the word "return"
     * 
     * @param arg0 A return arg to be parsed
     * @return The name of the expected return key
     */
    private String parseVar(String arg0) {
        return arg0.trim().substring(6).trim();
    }

}
