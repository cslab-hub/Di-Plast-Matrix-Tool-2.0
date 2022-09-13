/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services.local;

import com.uos.matrix.entities.Material;
import com.uos.matrix.entities.ProcessTech;
import com.uos.matrix.helper.enums.HardnessConditions;
import com.uos.matrix.helper.enums.ImpactStrConditions;
import com.uos.matrix.helper.enums.MFIConditions;
import com.uos.matrix.helper.enums.ParticleGeometry;
import com.uos.matrix.helper.enums.PolyModifications;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.enums.VicatConditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class PropertiesSideInfos implements Serializable {

    private final List<MFIConditions> distinctMFIWeights = Arrays.asList(MFIConditions.values());
    private final List<ImpactStrConditions> distinctImpactStrs = Arrays.asList(ImpactStrConditions.values());
    private final List<HardnessConditions> distinctHardness = Arrays.asList(HardnessConditions.values());
    private final List<VicatConditions> distinctVicats = Arrays.asList(VicatConditions.values());
    private final List<Triplet> allTriplets = Arrays.asList(Triplet.values());
    private final List<ParticleGeometry> allParticalGeos = Arrays.asList(ParticleGeometry.values());
    private final List<PolyModifications> allPolyMods = Arrays.asList(PolyModifications.values());
    private final List<Units> mfIUnits = Arrays.asList(Units.MFI);
    private final List<Units> modulusUnits = Arrays.asList(Units.NPMM2, Units.MPA, Units.PSI);
    private final List<Units> percentUnits = Arrays.asList(Units.PERCENT);
    private final List<Units> impactSquareUnits = Arrays.asList(Units.KJPM2, Units.JPM2, Units.JPCM2, Units.JPMM2, Units.FTLB2);
    private final List<Units> impactNoneSquareUnits = Arrays.asList(Units.KJPM, Units.JPM, Units.JPCM, Units.JPMM, Units.FTLB);
    private final List<Units> densityUnits = Arrays.asList(Units.GRPCM3, Units.KGPM3, Units.GRPLTR, Units.KGPLTR);
    private final List<Units> temperatureUnits = Arrays.asList(Units.CELSIUS, Units.FAHRENHEIT);
    private final List<Units> timeUnits = Arrays.asList(Units.MINUTES);
    private final List<Units> noUnits = Arrays.asList();
    private final List<Units> metersUnits = Arrays.asList(Units.MIM);

    public List<Units> getMFIUnits() {
        return mfIUnits;
    }

    public List<MFIConditions> getDistinctMFIWeights() {
        return distinctMFIWeights;
    }

    public List<ImpactStrConditions> getDistinctImpactStrs() {
        return distinctImpactStrs;
    }

    public List<Triplet> getAllTriplets() {
        return allTriplets;
    }

    public List<Units> getMfIUnits() {
        return mfIUnits;
    }

    public List<Units> getModulusUnits() {
        return modulusUnits;
    }

    public List<Units> getPercentUnits() {
        return percentUnits;
    }

    public List<Units> getImpactSquareUnits() {
        return impactSquareUnits;
    }

    public List<Units> getImpactNoneSquareUnits() {
        return impactNoneSquareUnits;
    }

    public List<Units> getDensityUnits() {
        return densityUnits;
    }

    public List<Units> getTemperatureUnits() {
        return temperatureUnits;
    }

    public List<Units> getTimeUnits() {
        return timeUnits;
    }

    public List<Units> getNoUnits() {
        return noUnits;
    }

    public List<Units> getMetersUnits() {
        return metersUnits;
    }

    public List<ParticleGeometry> getAllParticalGeos() {
        return allParticalGeos;
    }

    public List<PolyModifications> getAllPolyMods() {
        return allPolyMods;
    }

    public List<HardnessConditions> getDistinctHardness() {
        return distinctHardness;
    }

    public List<VicatConditions> getDistinctVicats() {
        return distinctVicats;
    }


}
