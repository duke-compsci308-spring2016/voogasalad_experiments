package groovy;

import java.util.HashMap;
import java.util.Map;
import groovy.lang.GroovyClassLoader;


// make a class loader for Java, XStream, and Groovy to share
public class LocalClassLoader extends GroovyClassLoader {
    private Map<String, Class<?>> myClasses = new HashMap<>();

    public void add (Class<?> c) {
        myClasses.put(c.getName(), c);
    }
        
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (myClasses.containsKey(name)) {
            return myClasses.get(name);
        }
        else {
            return super.loadClass(name);
        }
    }
}
