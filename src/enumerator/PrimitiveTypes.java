package enumerator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author victor.rocha
 */
public enum PrimitiveTypes {
    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    CHAR("char"),
    STRING("String"),
    BOOLEAN("boolean");
    
    private String name;

    public String getName() {
        return name;
    }

    private PrimitiveTypes(String name) {
        this.name = name;
    }
}
