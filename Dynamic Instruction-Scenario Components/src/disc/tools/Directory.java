package disc.tools;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores the object references (and by extension, methods) to be made available
 * to the {@link Interpreter}. Can work using Class names as the reference names
 * or nicknames (what the specific instance is called). Can hold multiple
 * instances of the same object type if nicknames are used.
 * 
 * @author Liam Williams
 * @version 1.0.0
 */
public class Directory {

    Map<String, ObjectData> m = new HashMap<>();

    /**
     * Adds references to each object and its parameters.
     * 
     * @param objs
     *            Object references
     */
    public Directory(Object... objs) {
        for(int i = 0; i < objs.length; i++) {
            m.put(objs[i].getClass().getName(), new ObjectData(objs[i]));
        }
    }

    /**
     * Adds references to each object, with custom names to match them, and
     * their parameters. Indexes should align for associated things.
     * 
     * @param names
     *            The names to associate to each object
     * @param objs
     *            Object references
     */
    public Directory(String[] names, Object[] objs) {
        for(int i = 0; i < ((names.length + objs.length) / 2); i++) {
            m.put(names[i], new ObjectData(objs[i]));
        }
    }

    /**
     * Adds a reference after initialization. Uses the class's name.
     * 
     * @param obj
     */
    public void addObjectReference(Object obj) {
        m.put(obj.getClass().getName(), new ObjectData(obj));
    }

    /**
     * Adds a reference with a nickname after initialization.
     * 
     * @param name
     * @param obj
     */
    public void addObjectReference(String name, Object obj) {
        m.put(name, new ObjectData(obj));
    }

    /**
     * Looks up the given name/nickname in the {@link Directory}
     * 
     * @param className
     * @return
     */
    public Object lookupObject(String className) {
        return m.get(className).classInstance;
    }

    /**
     * Looks up the methods of the object with the given name/nickname in the
     * {@link Directory}.
     * 
     * @param className
     * @return
     */
    public Method[] lookupObjectMethods(String className) {
        return m.get(className).methods;
    }

    /**
     * Looks up the specific method of the specific object by name/nickname in
     * the {@link Directory}
     * 
     * @param className
     * @param methodName
     * @param numOfParameters
     * @return null if the method was not found
     */
    public Method lookupMethod(String className, String methodName,
            int numOfParameters) {
        if(m.get(className) == null) return null;
        Method[] ms = m.get(className).methods;
        return Arrays.stream(ms)
                .filter(m1 -> m1.getName().equals(methodName)
                        && m1.getParameterCount() == numOfParameters)
                .findAny().orElse(tryDumbLookup(ms, methodName));
    }

    /**
     * Tries a lookup without parameter count if the other lookupMethod fails.
     * 
     * @param ms
     *            An array of the methods of the object
     * @param methodName
     * @return null if the method was not found
     */
    private Method tryDumbLookup(Method[] ms, String methodName) {
        return Arrays.stream(ms).filter(m1 -> m1.getName().equals(methodName))
                .findAny().orElse(null);
    }
}

/**
 * Helper class that stores information on the object reference in the
 * {@link Directory}.
 * 
 * @author Liam
 * @version 1.0.0
 */
class ObjectData {

    public Object classInstance;
    public Method[] methods;

    /**
     * Creates an {@link ObjectData} object using the given object reference.
     * 
     * @param obj
     *            An object instance reference
     */
    public ObjectData(Object obj) {
        classInstance = obj;
        methods = obj.getClass().getDeclaredMethods();
        for(Method m : methods)
            m.setAccessible(true);
    }

}
