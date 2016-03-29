package groovy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class Main {
    public void simpleExample () {
        // some example Java objects
        List<String> names = Stream.of("Zelda", "D&D").collect(Collectors.toList());

        System.out.println("Before");
        System.out.println(names.size() + ": " + names);
        System.out.println();

        // setup Groovy and make it aware of Java objects
        // make a separate engine each time to avoid accumulating values
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
        engine.put("value", 3);
        engine.put("names", names);

        // evaluate in Groovy
        System.out.println("During");
        try {
            engine.eval("println 'value = ' + value + ' type = ' + value.getClass().name");
            engine.eval("value.times { println 'Groovy!' }");
            engine.eval("answer = value + 2");
            engine.eval("names << 'Doom'");
            engine.eval("println names.size()");
            engine.eval("names.each { println it }");
        } catch (ScriptException ex) {
            System.out.println(ex);
        }
        System.out.println();

        // get created values back out of Groovy (not necessary for those just changed by method calls)
        int answer = (int)engine.get("answer");

        System.out.println("After");
        System.out.println("answer = " + answer);
        System.out.println(names.size() + ": " + names);
        System.out.println();
    }

    public void useExample () {
        // make a separate engine each time to avoid accumulating values
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
        // some example Java objects
        List<Weapon> items = null;
        Weapon w = new Weapon("Sword");

        System.out.println("Before");
        System.out.println(w.count());
        System.out.println(items);
        System.out.println();

        // setup Groovy and make it aware of Java objects
        engine.put("weapon", w);
        engine.put("items", items);

        // evaluate in Groovy
        System.out.println("During");
        try {
            engine.eval("use = weapon.use()");
            engine.eval("println items?.size()");
            engine.eval("import groovy.Weapon; items = [ new Weapon('Spell'), new Weapon('Kick'), weapon ]");
            engine.eval("println items?.size()");
            engine.eval("for (w in items) { w.use() }");
            engine.eval("items.each { it.use() }");
        } catch(ScriptException ex) {
            System.out.println(ex);
        }
        System.out.println();

        // get created values back out of Groovy (not necessary for those just changed by method calls)
        String use = (String)engine.get("use");
        items = (List<Weapon>)engine.get("items");

        System.out.println("After");
        System.out.println(use);
        System.out.println(items);
        System.out.println(w.count());
        System.out.println(items.get(0).count());
        System.out.println();
    }

    public void createExample () {
        // some example Java objects
        List<Weapon> items = new ArrayList<>();
        Weapon w = new Weapon("Sword");
        items.add(w);

        System.out.println("Before");
        System.out.println(w.count());
        System.out.println(items);
        System.out.println();

        // test streaming, note need to create custom class loader to integrate everything
        LocalClassLoader loader = new LocalClassLoader();
        XStream serializer = new XStream(new DomDriver());
        serializer.setClassLoader(loader);
        testSerialization(serializer, items);

        // note, this makes certain components aware of our class loader :(
        Thread.currentThread().setContextClassLoader(loader);
        // make a separate engine each time to avoid accumulating values
        // setup Groovy and make it aware of Java objects
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("groovy");
        engine.put("weapon", w);
        engine.put("items", items);

        // evaluate in Groovy
        System.out.println("During");
        try {
            engine.eval("import groovy.Weapon; items << new Weapon('Spell')");
            // add new Groovy class, so it can be loaded again later
            loader.add(loader.parseClass("import groovy.Weapon; public class Special extends Weapon { public Special () { super('Special Weapon'); } };"));
            engine.eval("items << new Special()");
            engine.eval("items.each { it.use() }");
        } catch(ScriptException ex) {
            System.out.println(ex);
        }
        System.out.println();

        // note, no new values created within Groovy

        System.out.println("After");
        System.out.println(w.count());
        System.out.println(items);
        for (Weapon i : items) {
            System.out.println(i.getClass() + ": " + i.use());
        }
        try {
            // note, need reflection here since Java "source" for class does not exist
            Weapon wp = (Weapon)loader.loadClass("Special").newInstance();
            System.out.println(wp);
        }
        catch (Exception e) {
            // should never happen, created in Groovy
        }
        System.out.println();

        // test streaming again (with Groovy objects)
        testSerialization(serializer, items);
    }

    private void testSerialization (XStream serializer, List<Weapon> items) {
        System.out.println("Serialization");
        String savedWeapons = serializer.toXML(items);
        System.out.println(savedWeapons);
        List<Weapon> loadedWeapons = (List<Weapon>)serializer.fromXML(savedWeapons);
        System.out.println(loadedWeapons);
        System.out.println();
    }


    public static void main (String[] args) {
        Main m = new Main();
        m.simpleExample();
        System.out.println("==========================");
        m.useExample();
        System.out.println("==========================");
        m.createExample();
    }
}
