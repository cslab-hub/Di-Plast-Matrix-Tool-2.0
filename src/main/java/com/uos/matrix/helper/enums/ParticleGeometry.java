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
public enum ParticleGeometry implements Serializable {
    GRANULATE("Granulate"), FALAKES("Flakes"), RIGIDFLAKES("Rigid flakes"), SHREDDEDFILM("Shredded film"), POWDER("Poweder"), SHREDDEDFIBER("Shredded fiber");

    public final String name;

    public String getName() {
        return name;
    }

    private ParticleGeometry(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
