/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.enums;

import java.io.Serializable;

/**
 *
 * 
 */
public enum PolyModifications implements Serializable{
    TALCUM("Talcum"), GLASSFIBER("Glas fiber"), FLAMERETARDANT("Flame retardant"), NUCLEATED("Nucleated"), MINERAL("Mineral"), CARBONFIBER("Carbon fiber");
    
    public final String name;

    public String getName() {
        return name;
    }

    private PolyModifications(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
