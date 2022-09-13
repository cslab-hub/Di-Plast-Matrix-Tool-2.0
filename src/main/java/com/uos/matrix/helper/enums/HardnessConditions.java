/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 
 */
public enum HardnessConditions {

    SHORE00("Shore 00"), SHOREA("Shore A"), SHORED("Shore D"), ROCKWELLE("Rockwell E"), ROCKWELLM("Rockwell M"), ROCKWELLR("Rockwell R");

    public final String name;

    public String getName() {
        return name;
    }

    private HardnessConditions(String name) {
        this.name = name;
    }

    public static List<String> stringValues() {
        return Arrays.asList(MFIConditions.values()).stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    public static HardnessConditions getConditionByName(String name) {
        for (HardnessConditions u : HardnessConditions.values()) {
            String uname = u.getName();
            if (uname.equals(name)) {
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
