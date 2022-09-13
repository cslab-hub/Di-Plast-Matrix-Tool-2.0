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
public class RankableIntegerEntry implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Integer value;

    private Integer variation;

    private Units unit;

    @Transient
    private List<Units> possibleUnits;

    private RankLimitation ranking = RankLimitation.GTE;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getVariation() {
        return variation;
    }

    public void setVariation(Integer variation) {
        this.variation = variation;
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
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.value);
        hash = 89 * hash + Objects.hashCode(this.variation);
        hash = 89 * hash + Objects.hashCode(this.unit);
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
        final RankableIntegerEntry other = (RankableIntegerEntry) obj;
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
        if (variation == null) {
            return value.toString();
        }
        return value.toString() + "±" + variation.toString();
    }

    public List<Units> getPossibleUnits() {
        return possibleUnits;
    }

    public void setPossibleUnits(List<Units> possibleUnits) {
        this.possibleUnits = possibleUnits;
    }

        public void copy(RankableIntegerEntry r){
        this.value = r.getValue();
        this.unit = r.getUnit();
        this.ranking = r.getRanking();
        this.unit = r.getUnit();
        this.variation = r.getVariation();
        this.possibleUnits = r.getPossibleUnits();
    }
}
