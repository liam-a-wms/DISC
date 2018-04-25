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

public class Interpreter extends Thread {

    protected ExecutorService executor = Executors.newSingleThreadExecutor();

    protected Directory dir;
    protected Scenario work;
    protected Queue<Instruction> q;
    protected Map<String, String> heap = new HashMap<>();
    protected long timeout = 1000;

    public Interpreter(Directory dir) {
        this.dir = dir;
    }

    /**
     * Please use this one, it's the only one I've tested.<br>
     * Uses the fully qualified class name when referring to objects. Not fully
     * typesafe when calling methods.
     * 
     * @param dir
     * @param scenario
     */
    public Interpreter(Directory dir, Scenario scenario) {
        this.dir = dir;
        this.work = scenario;
    }

    private void init() {
        q = work.getInstructionQueue();
    }

    /**
     * Use start() to have this method called inside its own thread, runs the
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

class InstructionHandler implements Runnable {

    Instruction inst;
    Directory dir;
    Map<String, String> heap;

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

    private boolean checkInHeap(String varName) {
        return heap.keySet().stream().anyMatch(s -> s.equals(varName));
    }

    private String parseVar(String arg0) {
        return arg0.substring(6).trim();
    }

}
