/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import com.uos.matrix.helper.enums.Triplet;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name="REQUESTCONTEXT")
@Embeddable
public class RequestContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private TargetMarket targetMarket;

    private TargetProduct targetProduct;

    private String specificApplication;

    public RequestContext() {
        super();
        targetMarket = new TargetMarket(null);
        targetProduct = new TargetProduct(null);
    }

    // public RequestContext(String materialCode, String targetMarket, String targetProduct, String supplier, String testedBy, String market, Material material, ProcessTech processTech, Double recyclingContent, Double mFIWeight, Double mFIMin, Double mFIMax, Double kValue, Double density, Integer apparentDensity, Double eModulus, Double tensileStrenghtAtYield, Double tensileElongationAtYield, Double elongationAtBreak, Double charpyT20, Double charpyTminus20, Integer rockwellHardness, Integer hdt, Integer vicat, Double oit, Double humidity, Double ashContent, String color, Double pollution, Double mouldShrinkage, Triplet smell, Triplet flammability) {
    public RequestContext(TargetMarket targetMarket, TargetProduct targetProduct) {
        if (targetMarket == null) {
            this.targetMarket = new TargetMarket(null);
        } else {
            this.targetMarket = targetMarket;
        }
        if (targetProduct == null) {
            this.targetProduct = new TargetProduct(null);
        } else {
            this.targetProduct = targetProduct;
        }
    }

    public RequestContext clone() {
        //return new RequestContext(getMaterialCode(), getTargetMarket(), getTargetProduct(), getSupplier(), getTestedBy(), getPlasticsMarket(), getMaterial(), getProcessTech(), getRecyclingContent().getValue(), getMFIWeight().getValue(), getMFI().getValueMin(), getMFI().getValueMax(), getKValue().getValue(), getDensity().getValue(), getApparentDensity().getValue(), getEModulus().getValue(), getTensileStrenghtAtYield().getValue(), getTensileElongationAtYield().getValue(), getElongationAtBreak().getValue(), getCharpyT20().getValue(), getCharpyTminus20().getValue(), getRockwellHardness().getValue(), getHdt().getValue(), getVicat().getValue(), getOit().getValue(), getHumidity().getValue(), getAshContent().getValue(), getColor(), getPollution().getValue(), getMouldShrinkage().getValue(), getSmell(), getFlammability());
        return new RequestContext(targetMarket, targetProduct);
    }

    public TargetMarket getTargetMarket() {
        return targetMarket;
    }

    public void setTargetMarket(TargetMarket targetMarket) {
        this.targetMarket = targetMarket;
    }

    public TargetProduct getTargetProduct() {
        return targetProduct;
    }

    public void setTargetProduct(TargetProduct targetProduct) {
        this.targetProduct = targetProduct;
    }

    public String getSpecificApplication() {
        return specificApplication;
    }

    public void setSpecificApplication(String SpecificApplication) {
        this.specificApplication = SpecificApplication;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.targetMarket);
        hash = 59 * hash + Objects.hashCode(this.targetProduct);
        hash = 59 * hash + Objects.hashCode(this.specificApplication);
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
        final RequestContext other = (RequestContext) obj;
        if (!Objects.equals(this.specificApplication, other.specificApplication)) {
            return false;
        }
        if (!Objects.equals(this.targetMarket, other.targetMarket)) {
            return false;
        }
        if (!Objects.equals(this.targetProduct, other.targetProduct)) {
            return false;
        }
        return true;
    }

}
