package disc.tools;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Directory {

    Map<String, ObjectData> m = new HashMap<>();

    public Directory(Object... objs) {
        for(int i = 0; i < objs.length; i++) {
            m.put(objs[i].getClass().getName(), new ObjectData(objs[i]));
        }
    }

    public Directory(String[] names, Object[] objs) {
        for(int i = 0; i < ((names.length + objs.length) / 2); i++) {
            m.put(names[i], new ObjectData(objs[i]));
        }
    }

    public void addObjectReference(Object obj) {
        m.put(obj.getClass().getName(), new ObjectData(obj));
    }

    public void addObjectReference(String name, Object obj) {
        m.put(name, new ObjectData(obj));
    }

    public Object lookupObject(String className) {
        return m.get(className).classInstance;
    }
    
    public Method[] lookupObjectMethods(String className) {
        return m.get(className).methods;
    }
    
    public Method lookupMethod(String className, String methodName,
            int numOfParameters) {
        if(m.get(className) == null) return null;
        Method[] ms = m.get(className).methods;
        return Arrays.stream(ms)
                     .filter(m1 -> m1.getName().equals(methodName) 
                             && m1.getParameterCount() == numOfParameters)
                     .findAny()
                     .orElse(tryDumbLookup(ms, methodName));
    }
    
    private Method tryDumbLookup(Method[] ms, String methodName) {
        return Arrays.stream(ms)
                     .filter(m1 -> m1.getName().equals(methodName))
                     .findAny()
                     .orElse(null);
    }
}

class ObjectData {

    public Object classInstance;
    public Method[] methods;

    public ObjectData(Object obj) {
        classInstance = obj;
        methods = obj.getClass().getDeclaredMethods();
        for(Method m : methods) m.setAccessible(true);
    }

}
