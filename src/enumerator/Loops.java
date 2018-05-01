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
public enum Loops {
    FOR("for"),
    WHILE("while");
    
    private String name;

    public String getName() {
        return name;
    }

    private Loops(String name) {
        this.name = name;
    }
}
