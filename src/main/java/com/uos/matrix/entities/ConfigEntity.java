/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uos.matrix.entities;

import com.uos.matrix.helper.enums.Units;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONFIGENTITY")
public class ConfigEntity implements Serializable {

    private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE)
   private int id;
    
    private Units eModulusPrefferedUnit = Units.MPA;
    private Units tensileStrenghtAtYieldPrefferedUnit = Units.MPA;
    private Units densityPrefferedUnit = Units.GRPCM3;
    private Units bulkDensityPrefferedUnit = Units.KGPM3;
    private Units hDTPrefferedUnit = Units.CELSIUS;
    private Units vicatPrefferedUnit = Units.CELSIUS;
    private Units charpyImpactStrengthPrefferedUnit = Units.KJPM2;
    private Units flexuralModulusPrefferdUnit = Units.MPA;
    private Units izodImpactStrengthPrefferedUnit = Units.KJPM;
    public Units getEModulusPrefferedUnit() {
        return eModulusPrefferedUnit;
    }

    public Units getTensileStrenghtAtYieldPrefferedUnit() {
        return tensileStrenghtAtYieldPrefferedUnit;
    }

    public Units getDensityPrefferedUnit() {
        return densityPrefferedUnit;
    }

    public Units getBulkDensityPrefferedUnit() {
        return bulkDensityPrefferedUnit;
    }

    public Units getHDTPrefferedUnit() {
        return hDTPrefferedUnit;
    }

    public Units getVicatPrefferedUnit() {
        return vicatPrefferedUnit;
    }

    public void setEModulusPrefferedUnit(Units eModulusPrefferedUnit) {
        this.eModulusPrefferedUnit = eModulusPrefferedUnit;
    }

    public void setCharpyImpactStrengthPrefferedUnit(Units charpyImpactStrengthPrefferedUnit) {
        this.charpyImpactStrengthPrefferedUnit = charpyImpactStrengthPrefferedUnit;
    }

    public Units getCharpyImpactStrengthPrefferedUnit() {
        return charpyImpactStrengthPrefferedUnit;
    }

    public Units getFlexuralModulusPrefferdUnit() {
        return flexuralModulusPrefferdUnit;
    }

    public void setFlexuralModulusPrefferdUnit(Units flexuralModulusPrefferdUnit) {
        this.flexuralModulusPrefferdUnit = flexuralModulusPrefferdUnit;
    }

    public Units getIzodImpactStrengthPrefferedUnit() {
        return izodImpactStrengthPrefferedUnit;
    }

    public void setIzodImpactStrengthPrefferedUnit(Units izodImpactStrengthPrefferedUnit) {
        this.izodImpactStrengthPrefferedUnit = izodImpactStrengthPrefferedUnit;
    }

    public void setTensileStrenghtAtYieldPrefferedUnit(Units tensileStrenghtAtYieldPrefferedUnit) {
        this.tensileStrenghtAtYieldPrefferedUnit = tensileStrenghtAtYieldPrefferedUnit;
    }

    public void setDensityPrefferedUnit(Units densityPrefferedUnit) {
        this.densityPrefferedUnit = densityPrefferedUnit;
    }

    public void setBulkDensityPrefferedUnit(Units bulkDensityPrefferedUnit) {
        this.bulkDensityPrefferedUnit = bulkDensityPrefferedUnit;
    }

    public void setHDTPrefferedUnit(Units hDTPrefferedUnit) {
        this.hDTPrefferedUnit = hDTPrefferedUnit;
    }

    public void setVicatPrefferedUnit(Units vicatPrefferedUnit) {
        this.vicatPrefferedUnit = vicatPrefferedUnit;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
}
