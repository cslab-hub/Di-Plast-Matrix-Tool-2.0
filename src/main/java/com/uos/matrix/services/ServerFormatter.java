/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;

import com.uos.matrix.helper.enums.Triplet;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "serverFormatter")
@ApplicationScoped
public class ServerFormatter implements Serializable {

    private static final long serialVersionUID = 1L;

    public Triplet getTriplet(String input) {
        if (input == null || input.toLowerCase().equals("null") || input.isBlank()) {
            return Triplet.UNK;
        } else if (Boolean.getBoolean(input)) {
            return Triplet.YES;
        } else {
            return Triplet.NO;
        }
    }

    public Integer getInteger(String string) {
        try {
            if (string == null || string.equals("NULL") || string.isBlank()) {
                return null;
            }
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Double getDouble(String string) {
        try {
            if (string == null || string.equals("NULL") || string.isBlank()) {
                return null;
            }
            return Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public Float getFloat(String string) {
        try {
            if (string == null || string.equals("NULL") || string.isBlank()) {
                return null;
            }
            return Float.parseFloat(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public String formatString(String string) {
        if (string.equals("NULL")) {
            return null;
        } else {
            return string;
        }
    }

    public String formatDouble(double value, int number) {
        return String.format(Locale.US, " %,." + number + "f", value);
    }
}
