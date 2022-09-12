/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.enums;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 
 */
public enum ImpactStrConditions implements Serializable {

    NOTCHED23C("Notched 23\u00b0C"), UNNOTCHED23C("Unnotched 23\u00b0C"), NOTCHED0C("Notched 0\u00b0C"), UNNOTCHED0C("Unnotched 0\u00b0C"), NOTCHEDM20C("Notched -20\u00b0C"), UNNOTCHEDM20C("Unnotched -20\u00b0C");

    public final String name;

    public String getName() {
        return name;
    }

    private ImpactStrConditions(String name) {
        this.name = name;
    }

    public static List<String> stringValues() {
        return Arrays.asList(MFIConditions.values()).stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    public static ImpactStrConditions getConditionByName(String name) {
        for (ImpactStrConditions u : ImpactStrConditions.values()) {
            name = name.replace("\u00b0", "°");
            String uname = u.getName().replace("\u00b0", "°");
            if (uname.substring(0, uname.length()-2).equals(name.substring(0, name.length()-2))) {
                return u;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
