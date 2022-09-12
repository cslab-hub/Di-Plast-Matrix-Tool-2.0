/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import afu.org.checkerframework.common.value.qual.ArrayLen;
import com.uos.matrix.helper.enums.HardnessConditions;
import com.uos.matrix.helper.enums.ImpactStrConditions;
import com.uos.matrix.helper.enums.MFIConditions;
import com.uos.matrix.helper.enums.ParticleGeometry;
import com.uos.matrix.helper.enums.PolyModifications;
import com.uos.matrix.helper.enums.RankLimitation;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.enums.VicatConditions;
import com.uos.matrix.services.RequestService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;

@Entity
@Table(name = "SUPERRECYCLED")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SuperRecycled implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    private String materialCode;

    private String supplier;//TODO: vll eigene Klasse??

    private String testedBy;

    private Material material = null;

    private ProcessTech processTech = null;

    private ParticleGeo particleGeometry = null;

    private PolymerModification polymerModifications = null;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "modificationContentName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "modificationContentValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "modificationContentValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "modificationContentVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "modificationContentUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "modificationContentRanking"))
    })
    private final RankableRangedEntry modificationContent = new RankableRangedEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "recyclingContentName")),
        @AttributeOverride(name = "value", column = @Column(name = "recyclingContentValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "recyclingContentVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "recyclingContentUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "recyclingContentRanking"))
    })
    private final RankableDoubleEntry recyclingContent = new RankableDoubleEntry();

    @Transient
    private MFIConditions currentMFICondition;

    //@Embedded
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "mfi_per_condition")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<MFIConditions, RankableRangedEntry> mFIs = new HashMap<>();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "meltFiltrationSizeName")),
        @AttributeOverride(name = "value", column = @Column(name = "meltFiltrationSizeValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "meltFiltrationSizeVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "meltFiltrationSizeUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "meltFiltrationSizeRanking"))
    })
    private final RankableIntegerEntry meltFiltrationSize = new RankableIntegerEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "kValueName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "kValueValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "kValueValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "kValueVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "kValueUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "kValueRanking"))
    })
    private final RankableRangedEntry kValue = new RankableRangedEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "densityName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "densityValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "densityValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "densityVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "densityUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "densityRanking"))
    })
    private final RankableRangedEntry density = new RankableRangedEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "bulkDensityName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "bulkDensityValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "bulkDensityValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "bulkDensityVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "bulkDensityUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "bulkDensityRanking"))
    })
    private final RankableRangedEntry bulkDensity = new RankableRangedEntry();

    /*
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "apparentDensityName")),
        @AttributeOverride(name = "value", column = @Column(name = "apparentDensityValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "apparentDensityVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "apparentDensityUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "apparentDensityRanking"))
    })
    private final RankableDoubleEntry apparentDensity = new RankableDoubleEntry();
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "eModulusName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "eModulusValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "eModulusValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "eModulusVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "eModulusUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "eModulusRanking"))
    })
    private final RankableRangedEntry eModulus = new RankableRangedEntry();
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "eModulusTDName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "eModulusTDValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "eModulusTDValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "eModulusTDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "eModulusTDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "eModulusTDRanking"))
    })
    private final RankableRangedEntry eModulusTD = new RankableRangedEntry();
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "eModulusMDName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "eModulusMDValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "eModulusMDValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "eModulusMDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "eModulusMDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "eModulusMDRanking"))
    })
    private final RankableRangedEntry eModulusMD = new RankableRangedEntry();
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "flexuralModulusName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "flexuralModulusValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "flexuralModulusValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "flexuralModulusVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "flexuralModulusUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "flexuralModulusRanking"))
    })
    private final RankableRangedEntry flexuralModulus = new RankableRangedEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "tensileStrenghtAtYieldName")),
        @AttributeOverride(name = "value", column = @Column(name = "tensileStrenghtAtYieldValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "tensileStrengthAtYieldVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "tensileStrenghtAtYieldUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "tensileStrenghtAtYieldRanking"))
    })
    private final RankableDoubleEntry tensileStrenghtAtYield = new RankableDoubleEntry();
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "tensileStrenghtAtYieldTDName")),
        @AttributeOverride(name = "value", column = @Column(name = "tensileStrenghtAtYieldTDValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "tensileStrengthAtYieldTDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "tensileStrenghtAtYieldTDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "tensileStrenghtAtYieldTDRanking"))
    })
    private final RankableDoubleEntry tensileStrenghtAtYieldTD = new RankableDoubleEntry();
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "tensileStrenghtAtYieldMDName")),
        @AttributeOverride(name = "value", column = @Column(name = "tensileStrenghtAtYieldMDValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "tensileStrengthAtYieldmDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "tensileStrenghtAtYieldMDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "tensileStrenghtAtYieldMDRanking"))
    })
    private final RankableDoubleEntry tensileStrenghtAtYieldMD = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "tensileElongationAtYieldName")),
        @AttributeOverride(name = "value", column = @Column(name = "tensileElongationAtYieldValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "tensileElongationVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "tensileElongationAtYieldUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "tensileElongationAtYieldRanking"))
    })
    private final RankableDoubleEntry tensileElongationAtYield = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "tensileElongationAtYieldTDName")),
        @AttributeOverride(name = "value", column = @Column(name = "tensileElongationAtYieldTDValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "tensileElongationTDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "tensileElongationAtYieldTDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "tensileElongationAtYieldTDRanking"))
    })
    private final RankableDoubleEntry tensileElongationAtYieldTD = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "tensileElongationAtYieldMDName")),
        @AttributeOverride(name = "value", column = @Column(name = "tensileElongationAtYieldMDValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "tensileElongationMDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "tensileElongationAtYieldMDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "tensileElongationAtYieldMDRanking"))
    })
    private final RankableDoubleEntry tensileElongationAtYieldMD = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "elongationAtBreakName")),
        @AttributeOverride(name = "value", column = @Column(name = "elongationAtBreakValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "elongationAtBreakVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "elongationAtBreakUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "elongationAtBreakRanking"))
    })
    private final RankableDoubleEntry elongationAtBreak = new RankableDoubleEntry();
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "elongationAtBreakTDName")),
        @AttributeOverride(name = "value", column = @Column(name = "elongationAtBreakTDValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "elongationAtBreakTDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "elongationAtBreakTDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "elongationAtBreakTDRanking"))
    })
    private final RankableDoubleEntry elongationAtBreakTD = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "elongationAtBreakMDName")),
        @AttributeOverride(name = "value", column = @Column(name = "elongationAtBreakMDValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "elongationAtBreakMDVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "elongationAtBreakMDUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "elongationAtBreakMDRanking"))
    })
    private final RankableDoubleEntry elongationAtBreakMD = new RankableDoubleEntry();

    @Transient
    private ImpactStrConditions currentCharpyImpactStrCondition;

    //@Embedded
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "charpyImpStr_per_condition")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<ImpactStrConditions, RankableDoubleEntry> charpyImpactStrs = new HashMap<>();

    @Transient
    private ImpactStrConditions currentIzodImpactStrCondition;

    //@Embedded
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "izodImpStr_per_condition")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<ImpactStrConditions, RankableDoubleEntry> izodImpactStrs = new HashMap<>();

    /*
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "charpyT20Name")),
        @AttributeOverride(name = "value", column = @Column(name = "charpyT20Value")),
        @AttributeOverride(name = "variation", column = @Column(name = "charpyT20Variation")),
        @AttributeOverride(name = "unit", column = @Column(name = "charpyT20Unit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "charpyT20Ranking"))
    })
    private final RankableDoubleEntry charpyT20 = new RankableDoubleEntry();
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "charpyTminus20Name")),
        @AttributeOverride(name = "value", column = @Column(name = "charpyTminus20Value")),
        @AttributeOverride(name = "variation", column = @Column(name = "charpyTminus20Variation")),
        @AttributeOverride(name = "unit", column = @Column(name = "charpyTminus20Unit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "charpyTminus20Ranking"))
    })
    private final RankableDoubleEntry charpyTminus20 = new RankableDoubleEntry();
     */
    @Transient
    private HardnessConditions currentHardness;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "hardness_per_condition")
    @MapKeyEnumerated(EnumType.STRING)
    private final Map<HardnessConditions, RankableRangedEntry> hardness = new HashMap<>();

    @Transient
    private VicatConditions currentVicatCondition;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "vicats_per_condition")
    @MapKeyEnumerated(EnumType.STRING)
    private final Map<VicatConditions, RankableDoubleEntry> vicats = new HashMap<>();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "hdtName")),
        @AttributeOverride(name = "value", column = @Column(name = "hdtValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "hdtVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "hdtUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "hdtRanking"))
    })
    private final RankableDoubleEntry hdt = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "oitName")),
        @AttributeOverride(name = "value", column = @Column(name = "oitValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "oitVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "oitUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "oitRanking"))
    })
    private final RankableDoubleEntry oit = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "ashContentName")),
        @AttributeOverride(name = "value", column = @Column(name = "ashContentValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "ashContentariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "ashContentUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "ashContentRanking"))
    })
    private final RankableDoubleEntry ashContent = new RankableDoubleEntry();

    @Enumerated(EnumType.STRING)
    private Triplet smell = Triplet.UNK;

    private Color color; //TODO maybe enum

    @Enumerated(EnumType.STRING)
    private Triplet flammability = Triplet.UNK;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "pollutionName")),
        @AttributeOverride(name = "value", column = @Column(name = "pollutionValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "pollutionVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "pollutionUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "pollutionRanking"))
    })
    private final RankableDoubleEntry pollution = new RankableDoubleEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "mouldShrinkageName")),
        @AttributeOverride(name = "valueMin", column = @Column(name = "mouldShrinkageValueMin")),
        @AttributeOverride(name = "valueMax", column = @Column(name = "mouldShrinkageValueMax")),
        @AttributeOverride(name = "variation", column = @Column(name = "mouldShrinkageVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "mouldShrinkageUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "mouldShrinkageRanking"))
    })
    private final RankableRangedEntry moldShrinkage = new RankableRangedEntry();

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "moistureContentName")),
        @AttributeOverride(name = "value", column = @Column(name = "moistureContentValue")),
        @AttributeOverride(name = "variation", column = @Column(name = "moistureContentVariation")),
        @AttributeOverride(name = "unit", column = @Column(name = "moistureContentUnit")),
        @AttributeOverride(name = "ranking", column = @Column(name = "moistureContentRanking"))
    })
    private final RankableDoubleEntry moistureContent = new RankableDoubleEntry();

    private String createdBy = null;

    private String comments = "";

    public SuperRecycled() {
        
        currentMFICondition = MFIConditions.C190C500KG;
        currentCharpyImpactStrCondition = ImpactStrConditions.NOTCHED23C;
        currentHardness = HardnessConditions.SHORED;
        currentIzodImpactStrCondition = ImpactStrConditions.NOTCHED23C;
        currentVicatCondition = VicatConditions.B50;

        kValue.setName("kValue");
        modificationContent.setName("modificationContent");
        density.setName("density");
        bulkDensity.setName("bulkDensity");
        eModulus.setName("eModulus");
        eModulusMD.setName("eModulusMD");
        eModulusTD.setName("eModulusTD");
        flexuralModulus.setName("flexuralModulus");
        moldShrinkage.setName("moldShrinkage");

        ashContent.setRanking(RankLimitation.LTE);
        //apparentDensity.setRanking(RankLimitation.CENTERED);
        moistureContent.setRanking(RankLimitation.LTE);
        pollution.setRanking(RankLimitation.LTE);
        meltFiltrationSize.setRanking(RankLimitation.LTE);

        //TODO!!!
        this.recyclingContent.setUnit(Units.PERCENT);
        for (MFIConditions m : MFIConditions.values()) {
            RankableRangedEntry r = new RankableRangedEntry();
            r.setUnit(Units.MFI);
            r.setName("mfi");
            this.mFIs.put(m, r);
        }
        this.density.setUnit(Units.GRPCM3);
        //this.apparentDensity.setUnit(Units.GRPLTR);
        this.eModulus.setUnit(Units.NPMM2);
        this.eModulusTD.setUnit(Units.NPMM2);
        this.eModulusMD.setUnit(Units.NPMM2);
        this.flexuralModulus.setUnit(Units.NPMM2);
        this.tensileStrenghtAtYield.setUnit(Units.NPMM2);
        this.tensileStrenghtAtYieldTD.setUnit(Units.NPMM2);
        this.tensileStrenghtAtYieldMD.setUnit(Units.NPMM2);
        this.tensileElongationAtYield.setUnit(Units.PERCENT);
        this.tensileElongationAtYieldTD.setUnit(Units.PERCENT);
        this.tensileElongationAtYieldMD.setUnit(Units.PERCENT);
        this.hdt.setUnit(Units.CELSIUS);

        this.oit.setUnit(Units.MINUTES);

        this.ashContent.setUnit(Units.PERCENT);
        this.elongationAtBreak.setUnit(Units.PERCENT);
        this.elongationAtBreakTD.setUnit(Units.PERCENT);
        this.elongationAtBreakMD.setUnit(Units.PERCENT);
        this.moldShrinkage.setUnit(Units.PERCENT);
        this.moistureContent.setUnit(Units.PERCENT);
        this.modificationContent.setUnit(Units.PERCENT);
        this.meltFiltrationSize.setUnit(Units.MIM);
        this.bulkDensity.setUnit(Units.KGPM3);
        this.pollution.setUnit(Units.PERCENT);

        for (VicatConditions h : VicatConditions.values()) {
            RankableDoubleEntry r = new RankableDoubleEntry();
            r.setUnit(Units.CELSIUS);
            r.setName("vicat");
            this.vicats.put(h, r);
        }

        for (HardnessConditions h : HardnessConditions.values()) {
            RankableRangedEntry r = new RankableRangedEntry();
            r.setName("hardness");
            this.hardness.put(h, r);
        }

        for (ImpactStrConditions m : ImpactStrConditions.values()) {
            RankableDoubleEntry r = new RankableDoubleEntry();
            r.setUnit(Units.KJPM2);
            r.setName("charpy");
            this.charpyImpactStrs.put(m, r);

            RankableDoubleEntry r2 = new RankableDoubleEntry();
            r2.setUnit(Units.KJPM);
            r2.setName("izod");
            this.izodImpactStrs.put(m, r2);
        }

        material = new Material(null);
        processTech = new ProcessTech(null);
        smell = Triplet.UNK;
        flammability = Triplet.UNK;
        particleGeometry = new ParticleGeo(null);
        polymerModifications = new PolymerModification(null);
        color = new Color(null);

    }

    //todo set units if value of unit is set?
    public SuperRecycled(String materialCode, String supplier, String testedBy, Material material, ProcessTech processTech, RankableRangedEntry kvalue, RankableDoubleEntry recyclingContent, MFIConditions currentMFICondition, Map<MFIConditions, RankableRangedEntry> mfis, ImpactStrConditions currentCharpyCondition, Map<ImpactStrConditions, RankableDoubleEntry> charpyimpactstrs, ImpactStrConditions currentIzoCondition, Map<ImpactStrConditions, RankableDoubleEntry> izodimpactstrs, RankableRangedEntry density, RankableRangedEntry bulkDensity, RankableRangedEntry eModulus, RankableRangedEntry eModulusTD, RankableRangedEntry eModulusMD, RankableRangedEntry flexuralModulus, RankableDoubleEntry tensileStrenghtAtYield, RankableDoubleEntry tensileStrenghtAtYieldTD, RankableDoubleEntry tensileStrenghtAtYieldMD, RankableDoubleEntry tensileElongationAtYield, RankableDoubleEntry tensileElongationAtYieldTD, RankableDoubleEntry tensileElongationAtYieldMD, RankableDoubleEntry elongationAtBreak, RankableDoubleEntry elongationAtBreakTD, RankableDoubleEntry elongationAtBreakMD, Map<HardnessConditions, RankableRangedEntry> hardness, RankableDoubleEntry hdt, Map<VicatConditions, RankableDoubleEntry> vicats, RankableDoubleEntry oit, RankableDoubleEntry ashContent, Color color, RankableDoubleEntry pollution, RankableRangedEntry mouldShrinkage, Triplet smell, Triplet flammability, PolymerModification polymerModifications, RankableDoubleEntry moistureContent, RankableIntegerEntry meltFiltrationSize, ParticleGeo particleGeometry, RankableRangedEntry modificationContent, String comments) {
        this();
        this.materialCode = materialCode;
        this.supplier = supplier;
        this.testedBy = testedBy;
        this.material = material;
        this.processTech = processTech;
        this.polymerModifications = polymerModifications;
        this.recyclingContent.copy(recyclingContent);
        this.comments = comments;

        if (currentMFICondition != null) {
            this.currentMFICondition = currentMFICondition;
        }

        for (HardnessConditions m : HardnessConditions.values()) {
            RankableRangedEntry r = new RankableRangedEntry();
            r.copy(hardness.get(m));
            this.hardness.put(m, r);
        }

        for (MFIConditions m : MFIConditions.values()) {
            RankableRangedEntry r = new RankableRangedEntry();
            r.copy(mfis.get(m));
            this.mFIs.put(m, r);
        }

        for (VicatConditions m : VicatConditions.values()) {
            RankableDoubleEntry r = new RankableDoubleEntry();
            r.copy(vicats.get(m));
            this.vicats.put(m, r);
        }

        if (currentCharpyCondition != null) {
            this.currentCharpyImpactStrCondition = currentCharpyCondition;
        }
        if (currentIzoCondition != null) {
            this.currentIzodImpactStrCondition = currentIzoCondition;
        }

        for (ImpactStrConditions m : ImpactStrConditions.values()) {
            RankableDoubleEntry r = new RankableDoubleEntry();
            r.copy(charpyimpactstrs.get(m));
            this.charpyImpactStrs.put(m, r);

            RankableDoubleEntry r2 = new RankableDoubleEntry();
            r2.copy(izodimpactstrs.get(m));
            this.izodImpactStrs.put(m, r2);

        }

        this.meltFiltrationSize.copy(meltFiltrationSize);
        this.kValue.copy(kvalue);
        this.density.copy(density);

        this.bulkDensity.copy(bulkDensity);

        this.eModulus.copy(eModulus);
        this.eModulusTD.copy(eModulusTD);
        this.eModulusMD.copy(eModulusMD);
        this.flexuralModulus.copy(flexuralModulus);

        this.tensileStrenghtAtYield.copy(tensileStrenghtAtYield);
        this.tensileStrenghtAtYieldTD.copy(tensileStrenghtAtYieldTD);
        this.tensileStrenghtAtYieldMD.copy(tensileStrenghtAtYieldMD);

        this.tensileElongationAtYield.copy(tensileElongationAtYield);
        this.tensileElongationAtYieldTD.copy(tensileElongationAtYieldTD);
        this.tensileElongationAtYieldMD.copy(tensileElongationAtYieldMD);

        this.elongationAtBreak.copy(elongationAtBreak);
        this.elongationAtBreakTD.copy(elongationAtBreakTD);
        this.elongationAtBreakMD.copy(elongationAtBreakMD);

        this.hdt.copy(hdt);
        this.particleGeometry = particleGeometry;

        this.oit.copy(oit);

        this.ashContent.copy(ashContent);
        this.moistureContent.copy(moistureContent);
        this.modificationContent.copy(modificationContent);

        this.color = color;
        this.pollution.copy(pollution);
        this.moldShrinkage.copy(mouldShrinkage);
        this.smell = smell;
        this.flammability = flammability;

        if (material == null) {
            this.material = new Material(null);
        }
        if (processTech == null) {
            this.processTech = new ProcessTech(null);
        }
        if (particleGeometry == null) {
            this.particleGeometry = new ParticleGeo(null);
        }
        if (polymerModifications == null) {
            this.polymerModifications = new PolymerModification(null);
        }
        if (color == null) {
            this.color = new Color(null);
        }
    }

    public SuperRecycled(int id, String materialCode, String supplier, String testedBy, Material material, ProcessTech processTech, RankableRangedEntry kvalue, RankableDoubleEntry recyclingContent, MFIConditions currentMFICondition, Map<MFIConditions, RankableRangedEntry> mfis, ImpactStrConditions currentCharpyCondition, Map<ImpactStrConditions, RankableDoubleEntry> charpyimpactstrs, ImpactStrConditions currentIzoCondition, Map<ImpactStrConditions, RankableDoubleEntry> izodimpactstrs, RankableRangedEntry density, RankableRangedEntry bulkDensity, RankableRangedEntry eModulus, RankableRangedEntry eModulusTD, RankableRangedEntry eModulusMD, RankableRangedEntry flexuralModulus, RankableDoubleEntry tensileStrenghtAtYield, RankableDoubleEntry tensileStrenghtAtYieldTD, RankableDoubleEntry tensileStrenghtAtYieldMD, RankableDoubleEntry tensileElongationAtYield, RankableDoubleEntry tensileElongationAtYieldTD, RankableDoubleEntry tensileElongationAtYieldMD, RankableDoubleEntry elongationAtBreak, RankableDoubleEntry elongationAtBreakTD, RankableDoubleEntry elongationAtBreakMD, Map<HardnessConditions, RankableRangedEntry> hardness, RankableDoubleEntry hdt, Map<VicatConditions, RankableDoubleEntry> vicats, RankableDoubleEntry oit, RankableDoubleEntry ashContent, Color color, RankableDoubleEntry pollution, RankableRangedEntry mouldShrinkage, Triplet smell, Triplet flammability, PolymerModification polymerModifications, RankableDoubleEntry moistureContent, RankableIntegerEntry meltFiltrationSize, ParticleGeo particleGeometry, RankableRangedEntry modificationContent, String comment) {
        this(materialCode, supplier, testedBy, material, processTech, kvalue, recyclingContent, currentMFICondition, mfis, currentCharpyCondition, charpyimpactstrs, currentIzoCondition, izodimpactstrs, density, bulkDensity, eModulus, eModulusTD, eModulusMD, flexuralModulus, tensileStrenghtAtYield, tensileStrenghtAtYieldTD, tensileStrenghtAtYieldMD, tensileElongationAtYield, tensileElongationAtYieldTD, tensileElongationAtYieldMD, elongationAtBreak, elongationAtBreakTD, elongationAtBreakMD, hardness, hdt, vicats, oit, ashContent, color, pollution, mouldShrinkage, smell, flammability, polymerModifications, moistureContent, meltFiltrationSize, particleGeometry, modificationContent, comment);
        this.id = id;
    }

    public SuperRecycled clone() {
        return new SuperRecycled(getId(), getMaterialCode(), getSupplier(), getTestedBy(), getMaterial(), getProcessTech(), getKValue(), getRecyclingContent(), getCurrentMFICondition(), getMFIs(), getCurrentCharpyImpactStrCondition(), getCharpyImpactStrs(), getCurrentIzodImpactStrCondition(), getIzodImpactStrs(), getDensity(), getBulkDensity(), getEModulus(), getEModulusTD(), getEModulusMD(), getFlexuralModulus(), getTensileStrenghtAtYield(), getTensileStrenghtAtYieldTD(), getTensileStrenghtAtYieldMD(), getTensileElongationAtYield(), getTensileElongationAtYieldTD(), getTensileElongationAtYieldMD(), getElongationAtBreak(), getElongationAtBreakTD(), getElongationAtBreakMD(), getHardness(), getHdt(), getVicats(), getOit(), getAshContent(), getColor(), getPollution(), getMouldShrinkage(), getSmell(), getFlammability(), getPolymerModifications(), getMoistureContent(), getMeltFiltrationSize(), getParticleGeometry(), getModificationContent(), getComments());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        Logger.getLogger(RequestService.class.getName()).warning("changing sup" + supplier);
        this.supplier = supplier;
    }

    public String getTestedBy() {
        return testedBy;
    }

    public void setTestedBy(String testedBy) {
        this.testedBy = testedBy;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public ProcessTech getProcessTech() {
        return processTech;
    }

    public void setProcessTech(ProcessTech processTech) {
        this.processTech = processTech;
    }

    public RankableDoubleEntry getRecyclingContent() {
        return recyclingContent;
    }

    public void setRecyclingContent(Double recyclingContent) {
        this.recyclingContent.setValue(recyclingContent);
    }

    public MFIConditions getCurrentMFICondition() {
        return currentMFICondition;
    }

    public void setCurrentMFICondition(MFIConditions currentMFICondition) {
        this.currentMFICondition = currentMFICondition;
    }

    public RankableRangedEntry getMFI() {
        return this.mFIs.get(this.currentMFICondition);
    }

    public RankableRangedEntry getMFI(MFIConditions c) {
        return this.mFIs.get(c);
    }

    public Map<MFIConditions, RankableRangedEntry> getMFIs() {
        return this.mFIs;
    }

    public void setMFI(Double mFI, MFIConditions c) {
        RankableRangedEntry r = this.mFIs.get(c);
        r.setValueMin(mFI);
    }

    public void setMFI(Double mFImin, Double mFImax, MFIConditions c) {
        RankableRangedEntry r = this.mFIs.get(c);
        r.setValueMin(mFImin);
        r.setValueMax(mFImax);
    }

    public void setMFI(Double mFImin, Double mFImax) {
        RankableRangedEntry r = this.mFIs.get(currentMFICondition);
        r.setValueMin(mFImin);
        r.setValueMax(mFImax);
    }

    public void setMFI(Double mFI) {
        RankableRangedEntry r = this.mFIs.get(currentMFICondition);
        r.setValueMin(mFI);
    }

    public void setMFIVariation(Double mFIVariation, MFIConditions c) {
        RankableRangedEntry r = this.mFIs.get(c);
        r.setVariation(mFIVariation);
    }

    public void setMFIVariation(Double mFIVariation) {
        RankableRangedEntry r = this.mFIs.get(currentMFICondition);
        r.setVariation(mFIVariation);
    }

    public RankableDoubleEntry getCharpyTensileStr() {
        return this.charpyImpactStrs.get(this.currentCharpyImpactStrCondition);
    }

    public RankableDoubleEntry getCharpyTensileStr(ImpactStrConditions c) {
        return this.charpyImpactStrs.get(c);
    }

    public void setCharpyTensileStr(Double charpyValue, ImpactStrConditions c) {
        RankableDoubleEntry r = this.charpyImpactStrs.get(c);
        r.setValue(charpyValue);
    }

    public void setCharpyTensileStr(Double charpyValue) {
        RankableDoubleEntry r = this.charpyImpactStrs.get(currentCharpyImpactStrCondition);
        r.setValue(charpyValue);
    }

    public RankableDoubleEntry getIzodTensileStr() {
        return this.izodImpactStrs.get(this.currentIzodImpactStrCondition);
    }

    public RankableDoubleEntry getIzodTensileStr(ImpactStrConditions c) {
        return this.izodImpactStrs.get(c);
    }

    public void setIzodTensileStr(Double izoValue, ImpactStrConditions c) {
        RankableDoubleEntry r = this.izodImpactStrs.get(c);
        r.setValue(izoValue);
    }

    public void setIzodTensileStr(Double izoValue) {
        RankableDoubleEntry r = this.izodImpactStrs.get(currentIzodImpactStrCondition);
        r.setValue(izoValue);
    }

    public RankableDoubleEntry getTensileStrenghtAtYield() {
        return tensileStrenghtAtYield;
    }

    public void setTensileStrenghtAtYield(Double tensileStrenghtAtYield) {
        this.tensileStrenghtAtYield.setValue(tensileStrenghtAtYield);
    }

    public RankableDoubleEntry getTensileStrenghtAtYieldTD() {
        return tensileStrenghtAtYieldTD;
    }

    public void setTensileStrenghtAtYieldTD(Double tensileStrenghtAtYield) {
        this.tensileStrenghtAtYieldTD.setValue(tensileStrenghtAtYield);
    }

    public RankableDoubleEntry getTensileStrenghtAtYieldMD() {
        return tensileStrenghtAtYieldMD;
    }

    public void setTensileStrenghtAtYieldMD(Double tensileStrenghtAtYield) {
        this.tensileStrenghtAtYield.setValue(tensileStrenghtAtYield);
    }

    public RankableDoubleEntry getTensileElongationAtYield() {
        return tensileElongationAtYield;
    }

    public void setTensileElongationAtYield(Double tensileElongationAtYield) {
        this.tensileElongationAtYield.setValue(tensileElongationAtYield);
    }

    public RankableDoubleEntry getTensileElongationAtYieldTD() {
        return tensileElongationAtYieldTD;
    }

    public void setTensileElongationAtYieldTD(Double tensileElongationAtYield) {
        this.tensileElongationAtYieldTD.setValue(tensileElongationAtYield);
    }

    public RankableDoubleEntry getTensileElongationAtYieldMD() {
        return tensileElongationAtYieldMD;
    }

    public void setTensileElongationAtYieldMD(Double tensileElongationAtYield) {
        this.tensileElongationAtYieldMD.setValue(tensileElongationAtYield);
    }

    public RankableDoubleEntry getElongationAtBreak() {
        return elongationAtBreak;
    }

    public void setElongationAtBreak(Double elongationAtBreak) {
        this.elongationAtBreak.setValue(elongationAtBreak);
    }

    public RankableDoubleEntry getElongationAtBreakTD() {
        return elongationAtBreakTD;
    }

    public void setElongationAtBreakTD(Double elongationAtBreak) {
        this.elongationAtBreakTD.setValue(elongationAtBreak);
    }

    public RankableDoubleEntry getElongationAtBreakMD() {
        return elongationAtBreakMD;
    }

    public void setElongationAtBreakMD(Double elongationAtBreak) {
        this.elongationAtBreakMD.setValue(elongationAtBreak);
    }

    public Map<HardnessConditions, RankableRangedEntry> getHardness() {
        return hardness;
    }

    public RankableRangedEntry getHardnes() {
        return this.hardness.get(this.currentHardness);
    }

    public RankableRangedEntry getHardnes(HardnessConditions c) {
        return this.hardness.get(c);
    }

    public void setHardnes(Double hardness, HardnessConditions c) {
        RankableRangedEntry r = this.hardness.get(c);
        r.setValueMin(hardness);
    }

    public void setHardnes(Double hardness) {
        RankableRangedEntry r = this.hardness.get(currentHardness);
        r.setValueMin(hardness);
    }

    public void setHardnes(Double hardnessMin, Double hardnessMax, HardnessConditions c) {
        RankableRangedEntry r = this.hardness.get(c);
        r.setValueMin(hardnessMin);
        r.setValueMax(hardnessMax);
    }

    public void setHardnes(Double hardnessMin, Double hardnessMax) {
        RankableRangedEntry r = this.hardness.get(currentHardness);
        r.setValueMin(hardnessMin);
        r.setValueMax(hardnessMax);
    }

    public void setHardnesVariation(Double hardnessVariation, HardnessConditions c) {
        RankableRangedEntry r = this.hardness.get(c);
        r.setVariation(hardnessVariation);
    }

    public void setHardnesVariation(Double hardnessVariation) {
        RankableRangedEntry r = this.hardness.get(currentHardness);
        r.setVariation(hardnessVariation);
    }

    public Map<VicatConditions, RankableDoubleEntry> getVicats() {
        return vicats;
    }

    public RankableDoubleEntry getVicat() {
        return this.vicats.get(this.currentVicatCondition);
    }

    public RankableDoubleEntry getVicat(VicatConditions c) {
        return this.vicats.get(c);
    }

    public void setVicat(Double hardness, VicatConditions c) {
        RankableDoubleEntry r = this.vicats.get(c);
        r.setValue(hardness);
    }

    public void setVicat(Double value) {
        RankableDoubleEntry r = this.vicats.get(currentVicatCondition);
        r.setValue(value);
    }

    public RankableDoubleEntry getHdt() {
        return hdt;
    }

    public void setHdt(Double hdt) {
        this.hdt.setValue(hdt);
    }

    public RankableDoubleEntry getOit() {
        return oit;
    }

    public void setOit(Double oit) {
        this.oit.setValue(oit);
    }

    public RankableDoubleEntry getAshContent() {
        return ashContent;
    }

    public void setAshContent(Double ashContent) {
        this.ashContent.setValue(ashContent);
    }

    public Triplet getSmell() {
        return smell;
    }

    public void setSmell(Triplet smell) {
        this.smell = smell;
    }

    public Triplet getFlammability() {
        return flammability;
    }

    public void setFlammability(Triplet flammability) {
        this.flammability = flammability;
    }

    public RankableDoubleEntry getPollution() {
        return pollution;
    }

    public void setPollution(Double pollution) {
        this.pollution.setValue(pollution);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ImpactStrConditions getCurrentCharpyImpactStrCondition() {
        return currentCharpyImpactStrCondition;
    }

    public void setCurrentCharpyImpactStrCondition(ImpactStrConditions currentCharpyImpactStrCondition) {
        this.currentCharpyImpactStrCondition = currentCharpyImpactStrCondition;
    }

    public ImpactStrConditions getCurrentIzodImpactStrCondition() {
        return currentIzodImpactStrCondition;
    }

    public void setCurrentIzodImpactStrCondition(ImpactStrConditions currentIzodImpactStrCondition) {
        this.currentIzodImpactStrCondition = currentIzodImpactStrCondition;
    }

    public void setMFIs(Map<MFIConditions, RankableRangedEntry> mFIs) {
        this.mFIs = mFIs;
    }

    public Map<ImpactStrConditions, RankableDoubleEntry> getCharpyImpactStrs() {
        return charpyImpactStrs;
    }

    public void setCharpyImpactStrs(Map<ImpactStrConditions, RankableDoubleEntry> charpyImpactStrs) {
        this.charpyImpactStrs = charpyImpactStrs;
    }

    public Map<ImpactStrConditions, RankableDoubleEntry> getIzodImpactStrs() {
        return izodImpactStrs;
    }

    public void setIzodImpactStrs(Map<ImpactStrConditions, RankableDoubleEntry> izodImpactStrs) {
        this.izodImpactStrs = izodImpactStrs;
    }

    public void setMeltFiltrationSize(Integer meltFiltrationSize) {
        this.meltFiltrationSize.setValue(meltFiltrationSize);
    }

    public RankableIntegerEntry getMeltFiltrationSize() {
        return meltFiltrationSize;
    }

    public void setMoistureContent(Double moistureContent) {
        this.moistureContent.setValue(moistureContent);
    }

    public RankableDoubleEntry getMoistureContent() {
        return moistureContent;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public HardnessConditions getCurrentHardness() {
        return currentHardness;
    }

    public void setCurrentHardness(HardnessConditions currentHardness) {
        this.currentHardness = currentHardness;
    }

    public VicatConditions getCurrentVicatCondition() {
        return currentVicatCondition;
    }

    public void setCurrentVicatCondition(VicatConditions currentVicatCondition) {
        this.currentVicatCondition = currentVicatCondition;
    }

    public ParticleGeo getParticleGeometry() {
        return particleGeometry;
    }

    public void setParticleGeometry(ParticleGeo particleGeometry) {
        this.particleGeometry = particleGeometry;
    }

    public PolymerModification getPolymerModifications() {
        return polymerModifications;
    }

    public void setPolymerModifications(PolymerModification polymerModifications) {
        this.polymerModifications = polymerModifications;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RankableRangedEntry getModificationContent() {
        return modificationContent;
    }

    public void setModificationContent(Double min, Double max) {
        modificationContent.setValueMin(min);
        modificationContent.setValueMax(max);
    }

    public RankableRangedEntry getKValue() {
        return kValue;
    }

    public void setKValue(Double min, Double max) {
        kValue.setValueMin(min);
        kValue.setValueMax(max);
    }

    public RankableRangedEntry getDensity() {
        return density;
    }

    public void setDensity(Double min, Double max) {
        density.setValueMin(min);
        density.setValueMax(max);
    }

    public RankableRangedEntry getBulkDensity() {
        return bulkDensity;
    }

    public void setBulkDensity(Double min, Double max) {
        bulkDensity.setValueMin(min);
        bulkDensity.setValueMax(max);
    }

    public RankableRangedEntry getEModulus() {
        return eModulus;
    }

    public void setEModulus(Double min, Double max) {
        eModulus.setValueMin(min);
        eModulus.setValueMax(max);
    }

    public RankableRangedEntry getEModulusTD() {
        return eModulusTD;
    }

    public void setEModulusTD(Double min, Double max) {
        eModulusTD.setValueMin(min);
        eModulusTD.setValueMax(max);
    }

    public RankableRangedEntry getEModulusMD() {
        return eModulusMD;
    }

    public void setEModulusMD(Double min, Double max) {
        eModulusMD.setValueMin(min);
        eModulusMD.setValueMax(max);
    }

    public RankableRangedEntry getFlexuralModulus() {
        return flexuralModulus;
    }

    public void setFlexuralModulus(Double min, Double max) {
        flexuralModulus.setValueMin(min);
        flexuralModulus.setValueMax(max);
    }

    public RankableRangedEntry getMouldShrinkage() {
        return moldShrinkage;
    }

    public void setMouldShrinkage(Double min, Double max) {
        moldShrinkage.setValueMin(min);
        moldShrinkage.setValueMax(max);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.id;
        hash = 31 * hash + Objects.hashCode(this.materialCode);
        hash = 31 * hash + Objects.hashCode(this.supplier);
        hash = 31 * hash + Objects.hashCode(this.testedBy);
        hash = 31 * hash + Objects.hashCode(this.material);
        hash = 31 * hash + Objects.hashCode(this.processTech);
        hash = 31 * hash + Objects.hashCode(this.particleGeometry);
        hash = 31 * hash + Objects.hashCode(this.polymerModifications);
        hash = 31 * hash + Objects.hashCode(this.modificationContent);
        hash = 31 * hash + Objects.hashCode(this.recyclingContent);
        hash = 31 * hash + Objects.hashCode(this.mFIs);
        hash = 31 * hash + Objects.hashCode(this.meltFiltrationSize);
        hash = 31 * hash + Objects.hashCode(this.kValue);
        hash = 31 * hash + Objects.hashCode(this.density);
        hash = 31 * hash + Objects.hashCode(this.bulkDensity);
        hash = 31 * hash + Objects.hashCode(this.eModulus);
        hash = 31 * hash + Objects.hashCode(this.eModulusTD);
        hash = 31 * hash + Objects.hashCode(this.eModulusMD);
        hash = 31 * hash + Objects.hashCode(this.flexuralModulus);
        hash = 31 * hash + Objects.hashCode(this.tensileStrenghtAtYield);
        hash = 31 * hash + Objects.hashCode(this.tensileStrenghtAtYieldTD);
        hash = 31 * hash + Objects.hashCode(this.tensileStrenghtAtYieldMD);
        hash = 31 * hash + Objects.hashCode(this.tensileElongationAtYield);
        hash = 31 * hash + Objects.hashCode(this.tensileElongationAtYieldTD);
        hash = 31 * hash + Objects.hashCode(this.tensileElongationAtYieldMD);
        hash = 31 * hash + Objects.hashCode(this.elongationAtBreak);
        hash = 31 * hash + Objects.hashCode(this.elongationAtBreakTD);
        hash = 31 * hash + Objects.hashCode(this.elongationAtBreakMD);
        hash = 31 * hash + Objects.hashCode(this.charpyImpactStrs);
        hash = 31 * hash + Objects.hashCode(this.izodImpactStrs);
        hash = 31 * hash + Objects.hashCode(this.hardness);
        hash = 31 * hash + Objects.hashCode(this.vicats);
        hash = 31 * hash + Objects.hashCode(this.hdt);
        hash = 31 * hash + Objects.hashCode(this.oit);
        hash = 31 * hash + Objects.hashCode(this.ashContent);
        hash = 31 * hash + Objects.hashCode(this.smell);
        hash = 31 * hash + Objects.hashCode(this.color);
        hash = 31 * hash + Objects.hashCode(this.flammability);
        hash = 31 * hash + Objects.hashCode(this.pollution);
        hash = 31 * hash + Objects.hashCode(this.moldShrinkage);
        hash = 31 * hash + Objects.hashCode(this.moistureContent);
        hash = 31 * hash + Objects.hashCode(this.createdBy);
        hash = 31 * hash + Objects.hashCode(this.comments);
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
        final SuperRecycled other = (SuperRecycled) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.materialCode, other.materialCode)) {
            return false;
        }
        if (!Objects.equals(this.supplier, other.supplier)) {
            return false;
        }
        if (!Objects.equals(this.testedBy, other.testedBy)) {
            return false;
        }
        if (!Objects.equals(this.color, other.color)) {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy)) {
            return false;
        }
        if (!Objects.equals(this.comments, other.comments)) {
            return false;
        }
        if (!Objects.equals(this.material, other.material)) {
            return false;
        }
        if (!Objects.equals(this.processTech, other.processTech)) {
            return false;
        }
        if (this.particleGeometry != other.particleGeometry) {
            return false;
        }
        if (this.polymerModifications != other.polymerModifications) {
            return false;
        }
        if (!Objects.equals(this.modificationContent, other.modificationContent)) {
            return false;
        }
        if (!Objects.equals(this.recyclingContent, other.recyclingContent)) {
            return false;
        }
        if (!Objects.equals(this.mFIs, other.mFIs)) {
            return false;
        }
        if (!Objects.equals(this.meltFiltrationSize, other.meltFiltrationSize)) {
            return false;
        }
        if (!Objects.equals(this.kValue, other.kValue)) {
            return false;
        }
        if (!Objects.equals(this.density, other.density)) {
            return false;
        }
        if (!Objects.equals(this.bulkDensity, other.bulkDensity)) {
            return false;
        }
        if (!Objects.equals(this.eModulus, other.eModulus)) {
            return false;
        }
        if (!Objects.equals(this.eModulusTD, other.eModulusTD)) {
            return false;
        }
        if (!Objects.equals(this.eModulusMD, other.eModulusMD)) {
            return false;
        }
        if (!Objects.equals(this.flexuralModulus, other.flexuralModulus)) {
            return false;
        }
        if (!Objects.equals(this.tensileStrenghtAtYield, other.tensileStrenghtAtYield)) {
            return false;
        }
        if (!Objects.equals(this.tensileStrenghtAtYieldTD, other.tensileStrenghtAtYieldTD)) {
            return false;
        }
        if (!Objects.equals(this.tensileStrenghtAtYieldMD, other.tensileStrenghtAtYieldMD)) {
            return false;
        }
        if (!Objects.equals(this.tensileElongationAtYield, other.tensileElongationAtYield)) {
            return false;
        }
        if (!Objects.equals(this.tensileElongationAtYieldTD, other.tensileElongationAtYieldTD)) {
            return false;
        }
        if (!Objects.equals(this.tensileElongationAtYieldMD, other.tensileElongationAtYieldMD)) {
            return false;
        }
        if (!Objects.equals(this.elongationAtBreak, other.elongationAtBreak)) {
            return false;
        }
        if (!Objects.equals(this.elongationAtBreakTD, other.elongationAtBreakTD)) {
            return false;
        }
        if (!Objects.equals(this.elongationAtBreakMD, other.elongationAtBreakMD)) {
            return false;
        }
        if (!Objects.equals(this.charpyImpactStrs, other.charpyImpactStrs)) {
            return false;
        }
        if (!Objects.equals(this.izodImpactStrs, other.izodImpactStrs)) {
            return false;
        }
        if (!Objects.equals(this.hardness, other.hardness)) {
            return false;
        }
        if (!Objects.equals(this.vicats, other.vicats)) {
            return false;
        }
        if (!Objects.equals(this.hdt, other.hdt)) {
            return false;
        }
        if (!Objects.equals(this.oit, other.oit)) {
            return false;
        }
        if (!Objects.equals(this.ashContent, other.ashContent)) {
            return false;
        }
        if (this.smell != other.smell) {
            return false;
        }
        if (this.flammability != other.flammability) {
            return false;
        }
        if (!Objects.equals(this.pollution, other.pollution)) {
            return false;
        }
        if (!Objects.equals(this.moldShrinkage, other.moldShrinkage)) {
            return false;
        }
        if (!Objects.equals(this.moistureContent, other.moistureContent)) {
            return false;
        }
        return true;
    }


    /* TODO
    public List<Integer> getComponentsNumber() {
        List<Integer> returnV = new ArrayList<>();
        int counter = 0;

        if (this.recyclingContent.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.getMFI().getValueMin() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.kValue.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.density.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.apparentDensity.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.eModulus.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.tensileStrenghtAtYield.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.tensileElongationAtYield.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.elongationAtBreak.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.charpyT20.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.charpyTminus20.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.rockwellHardness.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.hdt.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.vicat.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.oit.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.humidity.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.ashContent.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.pollution.getValue() != null) {
            returnV.add(counter);
        }
        counter++;
        if (this.moldShrinkage.getValue() != null) {
            returnV.add(counter);
        }

        return returnV;
    }

    public double[][] addComponents(int index, double[][] values, List<Integer> components) {
        Integer counter = 0;
        Integer counterF = 0;

        if (components.contains(counterF)) {
            if (this.recyclingContent.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.recyclingContent.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.getMFI().getValueMin() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.getMFI().getValueMin();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.kValue.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.kValue.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.density.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.density.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.apparentDensity.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.apparentDensity.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.eModulus.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.eModulus.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.tensileStrenghtAtYield.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.tensileStrenghtAtYield.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.tensileElongationAtYield.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.tensileElongationAtYield.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.elongationAtBreak.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.elongationAtBreak.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.charpyT20.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.charpyT20.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.charpyTminus20.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.charpyTminus20.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.rockwellHardness.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.rockwellHardness.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.hdt.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.hdt.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.vicat.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.vicat.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.oit.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.oit.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.humidity.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.humidity.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.ashContent.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.ashContent.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.pollution.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.pollution.getValue();
            }
            counter++;
        }
        counterF++;
        if (components.contains(counterF)) {
            if (this.moldShrinkage.getValue() == null) {
                values[index][counter] = -1;
            } else {
                values[index][counter] = this.moldShrinkage.getValue();
            }
        }
        return values;
    }
     */

    @Override
    public String toString() {
        return "SuperRecycled{" + "id=" + id + ", materialCode=" + materialCode + ", supplier=" + supplier + ", testedBy=" + testedBy + ", material=" + material + ", processTech=" + processTech + ", particleGeometry=" + particleGeometry + ", polymerModifications=" + polymerModifications + ", modificationContent=" + modificationContent + ", recyclingContent=" + recyclingContent + ", currentMFICondition=" + currentMFICondition + ", mFIs=" + mFIs + ", meltFiltrationSize=" + meltFiltrationSize + ", kValue=" + kValue + ", density=" + density + ", bulkDensity=" + bulkDensity + ", eModulus=" + eModulus + ", eModulusTD=" + eModulusTD + ", eModulusMD=" + eModulusMD + ", flexuralModulus=" + flexuralModulus + ", tensileStrenghtAtYield=" + tensileStrenghtAtYield + ", tensileStrenghtAtYieldTD=" + tensileStrenghtAtYieldTD + ", tensileStrenghtAtYieldMD=" + tensileStrenghtAtYieldMD + ", tensileElongationAtYield=" + tensileElongationAtYield + ", tensileElongationAtYieldTD=" + tensileElongationAtYieldTD + ", tensileElongationAtYieldMD=" + tensileElongationAtYieldMD + ", elongationAtBreak=" + elongationAtBreak + ", elongationAtBreakTD=" + elongationAtBreakTD + ", elongationAtBreakMD=" + elongationAtBreakMD + ", currentCharpyImpactStrCondition=" + currentCharpyImpactStrCondition + ", charpyImpactStrs=" + charpyImpactStrs + ", currentIzodImpactStrCondition=" + currentIzodImpactStrCondition + ", izodImpactStrs=" + izodImpactStrs + ", currentHardness=" + currentHardness + ", hardness=" + hardness + ", currentVicatCondition=" + currentVicatCondition + ", vicats=" + vicats + ", hdt=" + hdt + ", oit=" + oit + ", ashContent=" + ashContent + ", smell=" + smell + ", color=" + color + ", flammability=" + flammability + ", pollution=" + pollution + ", moldShrinkage=" + moldShrinkage + ", moistureContent=" + moistureContent + ", createdBy=" + createdBy + ", comments=" + comments + '}';
    }

    

}
