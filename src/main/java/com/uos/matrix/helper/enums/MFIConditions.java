/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.enums;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * 
 */
public enum MFIConditions implements Serializable {
    C190C216KG("190\u00b0C/2,16kg"), C190C500KG("190\u00b0C/5kg"), C190C2160KG("190\u00b0C/21,6kg"), C200C500KG("200\u00b0C/5kg"), C220C1000KG("220\u00b0C/10kg"), C230C216KG("230\u00b0C/2,16kg"), C250C500KG("250\u00b0C/5kg"), C250C1000KG("250\u00b0C/10kg"), C300C125KG("300\u00b0C/1,25kg");

    public final String name;

    public String getName() {
        return name;
    }

    private MFIConditions(String name) {
        this.name = name;
    }

    public static List<String> stringValues() {
        return Arrays.asList(MFIConditions.values()).stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return getName();
    }

    public static MFIConditions getConditionByName(String name) {
        for (MFIConditions u : MFIConditions.values()) {
            String uname = u.getName().replace("\u00b0", "°");
            if (uname.substring(0, 3).equals(name.substring(0, 3)) && uname.substring(6).equals(name.substring(6))) {
                return u;
            }
        }
        return null;
    }
}
