package disc.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import disc.data.Instruction;
import disc.data.Scenario;

/**
 * Dynamic interpreter that can run an entire {@link Scenario} using method
 * lookups and a variable heaps. Can handle a variable return from a method and
 * then variable substitution in a later {@link Instruction}. Defaults to
 * discarding the method return unless the Instruction specifically has a
 * "return" arg in it giving the variable a name (eg "return number"). If
 * objects are being passed around as variables then lossless serialization
 * (using the toString() method) and deserialization (using a Constructor with a
 * String as the only argument) is expected. <br>
 * The Interpreter uses two Threads. One for the Interpreter itself (which only
 * happens if you properly call start() instead of run()) and one for handling
 * each Instruction. If an Instruction or called method causes an error or
 * Exception, the Thread will stop and the next Instruction will be picked up
 * and handled by a new Thread.
 * 
 * @author Liam Williams
 * @version 0.2.1
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
     * @param dir
     *            The Directory to lookup methods from
     * @param scenario
     *            The Scenario to execute
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
    @Override
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
            } catch(InterruptedException e) {
                q.clear();
                Thread.currentThread().interrupt();
            }
        }
    }

}