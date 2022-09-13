/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import com.uos.matrix.helper.enums.Triplet;
import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * 
 */
public class RankedRecycled implements Serializable  {

    private static final long serialVersionUID = 1L;    
    
    private double ranking;
    
    private RecycledEntity recycled;
    
    public RankedRecycled(){
        super();
    }

    public RankedRecycled(double ranking, RecycledEntity recycled){
        this.recycled = recycled;
        this.ranking = ranking;
    }
    
    
    public double getRanking() {
        return ranking;
    }

    public void setRanking(double ranking) {
        this.ranking = ranking;
    }

    public RecycledEntity getRecycled() {
        return recycled;
    }

    public void setRecycled(RecycledEntity recycled) {
        this.recycled = recycled;
    }
    
    
}
