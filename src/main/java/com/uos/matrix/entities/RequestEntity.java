/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import com.uos.matrix.helper.enums.HardnessConditions;
import com.uos.matrix.helper.enums.ImpactStrConditions;
import com.uos.matrix.helper.enums.MFIConditions;
import com.uos.matrix.helper.enums.ParticleGeometry;
import com.uos.matrix.helper.enums.PolyModifications;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.VicatConditions;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "REQUESTENTITY")
public class RequestEntity extends SuperRecycled implements Serializable {

    private static final long serialVersionUID = 1L;

    @Embedded
    private RequestContext context;

    private String buyerName;

    private boolean autoRequest = false;

    private boolean requestResolved = false;

    public RequestEntity() {
        super();
        context = new RequestContext();

    }

    public RequestEntity(String materialCode, String buyerName, RequestContext context, String supplier, String testedBy, Material material, ProcessTech processTech, RankableRangedEntry kvalue, RankableDoubleEntry recyclingContent, MFIConditions currentMFICondition, Map<MFIConditions, RankableRangedEntry> mfis, ImpactStrConditions currentCharpyCondition, Map<ImpactStrConditions, RankableDoubleEntry> charpyimpactstrs, ImpactStrConditions currentIzoCondition, Map<ImpactStrConditions, RankableDoubleEntry> izodimpactstrs, RankableRangedEntry density, RankableRangedEntry bulkDensity, RankableRangedEntry eModulus, RankableRangedEntry eModulusTD, RankableRangedEntry eModulusMD, RankableRangedEntry flexuralModulus, RankableDoubleEntry tensileStrenghtAtYield, RankableDoubleEntry tensileStrenghtAtYieldTD, RankableDoubleEntry tensileStrenghtAtYieldMD, RankableDoubleEntry tensileElongationAtYield, RankableDoubleEntry tensileElongationAtYieldTD, RankableDoubleEntry tensileElongationAtYieldMD, RankableDoubleEntry elongationAtBreak, RankableDoubleEntry elongationAtBreakTD, RankableDoubleEntry elongationAtBreakMD, Map<HardnessConditions, RankableRangedEntry> hardness, RankableDoubleEntry hdt, Map<VicatConditions, RankableDoubleEntry> vicat, RankableDoubleEntry oit, RankableDoubleEntry ashContent, Color color, RankableDoubleEntry pollution, RankableRangedEntry mouldShrinkage, Triplet smell, Triplet flammability, PolymerModification polymerModifications, RankableDoubleEntry moistureContent, RankableIntegerEntry meltFiltrationSize, ParticleGeo particleGeometry, RankableRangedEntry modificationContent, String comment) {
        super(materialCode, supplier, testedBy, material, processTech, kvalue, recyclingContent, currentMFICondition, mfis, currentCharpyCondition, charpyimpactstrs, currentIzoCondition, izodimpactstrs, density, bulkDensity, eModulus, eModulusTD, eModulusMD, flexuralModulus, tensileStrenghtAtYield, tensileStrenghtAtYieldTD, tensileStrenghtAtYieldMD, tensileElongationAtYield, tensileElongationAtYieldTD, tensileElongationAtYieldMD, elongationAtBreak, elongationAtBreakMD, elongationAtBreakMD, hardness, hdt, vicat, oit, ashContent, color, pollution, mouldShrinkage, smell, flammability, polymerModifications, moistureContent, meltFiltrationSize, particleGeometry, modificationContent, comment);
        this.buyerName = buyerName;
        if (context != null) {
            this.context = context;
        } else {
            this.context = new RequestContext();
        }
    }

    public RequestEntity clone() {
        return new RequestEntity(getMaterialCode(), getBuyerName(), getContext(), getSupplier(), getTestedBy(), getMaterial(), getProcessTech(), getKValue(), getRecyclingContent(), getCurrentMFICondition(), getMFIs(), getCurrentCharpyImpactStrCondition(), getCharpyImpactStrs(), getCurrentIzodImpactStrCondition(), getIzodImpactStrs(), getDensity(), getBulkDensity(), getEModulus(), getEModulusTD(), getEModulusMD(), getFlexuralModulus(), getTensileStrenghtAtYield(), getTensileStrenghtAtYieldTD(), getTensileStrenghtAtYieldMD(), getTensileElongationAtYield(), getTensileElongationAtYieldTD(), getTensileElongationAtYieldMD(), getElongationAtBreak(), getElongationAtBreakTD(), getElongationAtBreakMD(), getHardness(), getHdt(), getVicats(), getOit(), getAshContent(), getColor(), getPollution(), getMouldShrinkage(), getSmell(), getFlammability(), getPolymerModifications(), getMoistureContent(), getMeltFiltrationSize(), getParticleGeometry(), getModificationContent(), getComments());
    }

    public RequestContext getContext() {
        return context;
    }

    public void setContext(RequestContext context) {
        this.context = context;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.context);
        hash = 67 * hash + Objects.hashCode(this.buyerName);
        return hash + super.hashCode();
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
        final RequestEntity other = (RequestEntity) obj;
        if (!Objects.equals(this.buyerName, other.buyerName)) {
            return false;
        }
        if (!Objects.equals(this.context, other.context)) {
            return false;
        }
        return super.equals(obj);
    }

    public boolean isAutoRequest() {
        return autoRequest;
    }

    public void setAutoRequest(boolean autoRequest) {
        this.autoRequest = autoRequest;
    }

    public boolean isRequestResolved() {
        return requestResolved;
    }

    public void setRequestResolved(boolean requestResolved) {
        this.requestResolved = requestResolved;
    }



    
}
