package example.scanner;

/**
 * Created by Arek on 2017-01-12.
 */

public class DropdownElement {
    private int id;
    private String name;

    DropdownElement (int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }
}
