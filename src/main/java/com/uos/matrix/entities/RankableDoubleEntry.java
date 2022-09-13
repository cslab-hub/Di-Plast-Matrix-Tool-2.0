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
public class RankableDoubleEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Double value;

    private Double variation;

    private Units unit;

    @Transient
    private List<Units> possibleUnits;

    private RankLimitation ranking = RankLimitation.GTE;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getVariation() {
        return variation;
    }

    public void setVariation(Double variation) {
        this.variation = variation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public RankLimitation getRanking() {
        return ranking;
    }

    public void setRanking(RankLimitation ranking) {
        this.ranking = ranking;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.value);
        hash = 79 * hash + Objects.hashCode(this.variation);
        hash = 79 * hash + Objects.hashCode(this.unit);
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
        final RankableDoubleEntry other = (RankableDoubleEntry) obj;
        if (!Objects.equals(this.value, other.value)) {
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
        if (value == null) {
            return "null";
        }
        return value.toString();
    }

    public List<Units> getPossibleUnits() {
        return possibleUnits;
    }

    public void setPossibleUnits(List<Units> possibleUnits) {
        this.possibleUnits = possibleUnits;
    }
    
    public void copy(RankableDoubleEntry r){
        this.name = r.name;
        this.value = r.getValue();
        this.unit = r.getUnit();
        this.ranking = r.getRanking();
        this.unit = r.getUnit();
        this.variation = r.getVariation();
        this.possibleUnits = r.getPossibleUnits();
    }

}
