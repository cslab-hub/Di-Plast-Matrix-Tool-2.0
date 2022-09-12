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
import com.uos.matrix.helper.enums.RankLimitation;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.enums.VicatConditions;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "RECYCLEDENTITY")
public class RecycledEntity extends SuperRecycled implements Serializable {

    private static final long serialVersionUID = 1L;

    public RecycledEntity() {
        super();
    }

    public RecycledEntity(String materialCode, String supplier, String testedBy, Material material, ProcessTech processTech, RankableRangedEntry kvalue, RankableDoubleEntry recyclingContent, MFIConditions currentMFICondition, Map<MFIConditions, RankableRangedEntry> mfis, ImpactStrConditions currentCharpyCondition, Map<ImpactStrConditions, RankableDoubleEntry> charpyimpactstrs, ImpactStrConditions currentIzoCondition, Map<ImpactStrConditions, RankableDoubleEntry> izodimpactstrs, RankableRangedEntry density, RankableRangedEntry bulkDensity, RankableRangedEntry eModulus, RankableRangedEntry eModulusTD, RankableRangedEntry eModulusMD, RankableRangedEntry flexuralModulus, RankableDoubleEntry tensileStrenghtAtYield, RankableDoubleEntry tensileStrenghtAtYieldTD, RankableDoubleEntry tensileStrenghtAtYieldMD, RankableDoubleEntry tensileElongationAtYield, RankableDoubleEntry tensileElongationAtYieldTD, RankableDoubleEntry tensileElongationAtYieldMD, RankableDoubleEntry elongationAtBreak, RankableDoubleEntry elongationAtBreakTD, RankableDoubleEntry elongationAtBreakMD, Map<HardnessConditions, RankableRangedEntry> hardness, RankableDoubleEntry hdt, Map<VicatConditions, RankableDoubleEntry> vicats, RankableDoubleEntry oit, RankableDoubleEntry ashContent, Color color, RankableDoubleEntry pollution, RankableRangedEntry mouldShrinkage, Triplet smell, Triplet flammability, PolymerModification polymerModifications, RankableDoubleEntry moistureContent, RankableIntegerEntry meltFiltrationSize, ParticleGeo particleGeometry, RankableRangedEntry modificationContent, String comments) {
        super(materialCode, supplier, testedBy, material, processTech, kvalue, recyclingContent, currentMFICondition, mfis, currentCharpyCondition, charpyimpactstrs, currentIzoCondition, izodimpactstrs, density, bulkDensity, eModulus, eModulusTD, eModulusMD, flexuralModulus, tensileStrenghtAtYield, tensileStrenghtAtYieldTD, tensileStrenghtAtYieldMD, tensileElongationAtYield, tensileElongationAtYieldTD, tensileElongationAtYieldMD, elongationAtBreak, elongationAtBreakMD, elongationAtBreakMD, hardness, hdt, vicats, oit, ashContent, color, pollution, mouldShrinkage, smell, flammability, polymerModifications, moistureContent, meltFiltrationSize, particleGeometry, modificationContent, comments);
    }

}
