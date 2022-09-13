/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.helper.other;

import com.uos.matrix.entities.RankedRecycled;
import java.util.Comparator;

/**
 *
 * 
 */
public class RankingComperator implements Comparator<RankedRecycled>{
    
    @Override
    public int compare(RankedRecycled o1, RankedRecycled o2) {
        return Double.compare(o1.getRanking(), o2.getRanking());
    }
    
}
