/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.enums;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 *
 * 
 */
public enum Units implements Serializable {
    NONE(""), PERCENT("%"), MFI("g/10min"), NPMM2("N/mm\u00b2"), KG("kg"), PSI("psi"), GRPCM3("g/cm\u00b3"), KGPM3("kg/m\u00b3"), GRPLTR("g/ltr"), KGPLTR("kg/ltr"), MPA("MPa"), CELSIUS("\u00b0C"), FAHRENHEIT("\u00b0F"), MINUTES("min"), JPCM2("J/cm\u00b2"), JPM2("J/m\u00b2"), JPMM2("J/mm\u00b2"), KJPM2("kJ/m\u00b2"), FTLB2("ft*lb/in\u00b2"), JPCM("J/cm"), JPM("J/m"), JPMM("J/mm"), KJPM("kJ/m"), FTLB("ft*lb/in"), MIM("µm");

    public final String name;

    public String getName() {
        return name;
    }

    private Units(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static Units getUnitByName(String name) {
        for (Units u : Units.values()) {
            name = name.replace("\u00b2", "2").replace("\u00b3", "3").replace("\u00b0", "°");
            String uname = u.getName().replace("\u00b2", "2").replace("\u00b3", "3").replace("\u00b0", "°");
            uname = new String(uname.getBytes(), StandardCharsets.ISO_8859_1);
            if (uname.equals(name)) {
                return u;
            }
            if (name.length() == 2) {
                if (name.endsWith("C")) {
                    return CELSIUS;
                }
                if (name.endsWith("m")) {
                    return MIM;
                }
            }
        }
        System.out.println("UNIT ERROR###################");
        System.out.println(name);
        return null;
    }

}
