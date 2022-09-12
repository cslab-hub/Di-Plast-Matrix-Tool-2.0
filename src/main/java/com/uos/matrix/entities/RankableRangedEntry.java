/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import com.uos.matrix.helper.enums.RankLimitation;
import com.uos.matrix.helper.enums.Units;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 *
 * 
 */
@Embeddable
public class RankableRangedEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Double valueMin;

    private Double valueMax;

    private Double variation;

    private Units unit;

    @Transient
    private List<Units> possibleUnits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValueMin() {
        return valueMin;
    }

    public void setValueMin(Double valueMin) {
        this.valueMin = valueMin;
    }

    public Double getValueMax() {
        return valueMax;
    }

    public void setValueMax(Double valueMax) {
        this.valueMax = valueMax;
    }

    public Double getVariation() {
        return variation;
    }

    public void setVariation(Double variation) {
        this.variation = variation;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.valueMin);
        hash = 67 * hash + Objects.hashCode(this.valueMax);
        hash = 67 * hash + Objects.hashCode(this.variation);
        hash = 67 * hash + Objects.hashCode(this.unit);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RankableRangedEntry other = (RankableRangedEntry) obj;
        if (!Objects.equals(this.valueMin, other.valueMin)) {
            return false;
        }
        if (!Objects.equals(this.valueMax, other.valueMax)) {
            return false;
        }
        if (!Objects.equals(this.variation, other.variation)) {
            return false;
        }
        if (this.unit != other.unit) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "RankableRangedEntry{" + "name=" + name + ", valueMin=" + valueMin + ", valueMax=" + valueMax + ", unit=" + unit + '}';
    }

    public List<Units> getPossibleUnits() {
        return possibleUnits;
    }

    public void setPossibleUnits(List<Units> possibleUnits) {
        this.possibleUnits = possibleUnits;
    }

    public void copy(RankableRangedEntry r) {
        this.name = r.name;
        this.valueMax = r.getValueMax();
        this.valueMin = r.getValueMin();
        this.unit = r.getUnit();
        this.unit = r.getUnit();
        this.variation = r.getVariation();
        this.possibleUnits = r.getPossibleUnits();
    }

}
