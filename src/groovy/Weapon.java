package groovy;

public class Weapon {
    private String myName;
    private int myUses;

    public Weapon (String name) {
        myName = name;
        myUses = 0;
    }

    public String use () {
        System.out.println("Using " + myName);
        myUses += 1;
        return myName + " is groovy!";
    }

    public int count () {
        return myUses;
    }

    public String toString () {
        return myName;
    }
}
