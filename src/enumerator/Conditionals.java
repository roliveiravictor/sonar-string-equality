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
public enum Conditionals {
    IF("if"),
    AND("&&"),
    OR("||"),
    SWITCH("switch");
    
    private String name;

    public String getName() {
        return name;
    }

    private Conditionals(String name) {
        this.name = name;
    }
}
