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
public enum VicatConditions {

    A50("A50"), B50("B50"), A120("A120"), B120("B120");

    public final String name;

    public String getName() {
        return name;
    }

    private VicatConditions(String name) {
        this.name = name;
    }

    public static List<String> stringValues() {
        return Arrays.asList(MFIConditions.values()).stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    public static VicatConditions getConditionByName(String name) {
        for (VicatConditions u : VicatConditions.values()) {
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
