/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.view;

import com.google.common.collect.Lists;
import com.uos.matrix.entities.Color;
import com.uos.matrix.entities.Material;
import com.uos.matrix.entities.ParticleGeo;
import com.uos.matrix.entities.PolymerModification;
import com.uos.matrix.entities.ProcessTech;
import com.uos.matrix.entities.RankableDoubleEntry;
import com.uos.matrix.entities.RankableIntegerEntry;
import com.uos.matrix.entities.RankableRangedEntry;
import com.uos.matrix.entities.RankedRecycled;
import com.uos.matrix.entities.RecycledEntity;
import com.uos.matrix.entities.RequestEntity;
import com.uos.matrix.entities.TargetMarket;
import com.uos.matrix.entities.TargetProduct;
import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.entities.ValidationRequestEntity;
import com.uos.matrix.helper.enums.InventoryStatus;
import com.uos.matrix.helper.enums.MFIConditions;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.other.SessionUtils;
import com.uos.matrix.services.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.StateManagementStrategy;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.Visibility;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

@ManagedBean
@SessionScoped
public class BuyerView implements Serializable {

    private List<RequestEntity> requests;

    private List<RequestEntity> filteredRequests;

    private boolean newObject = false;

    private UploadedFile file;
    private UploadedFiles files;
    //TODO Wenn dieser gesetzt wird auch in andere Felder setzen? Also vll current value hier drauf referenzieren?
    private RequestEntity currentRequest = new RequestEntity();
    private RequestEntity filteredByRequest;
    private RequestEntity selectedRequest;
    private RankedRecycled selectedRankingRecycled;

    private List<FilterMeta> filterBy;

    private String contactTargetProductText;
    private String contactTargetProduct;
    private String contactTargetIndustryText;
    private String contactTargetIndustry;
    private String contactSpecificApplicationText;

    //TODO fixed values + selection process?
    private List<Material> distinctMaterials;
    private List<ProcessTech> distinctProcessTechs;
    private List<Color> distinctColors;
    private List<TargetMarket> distinctTargetMarkets;
    private List<TargetProduct> distinctTargetProducts;
    private List<ParticleGeo> distinctParticleGeo;
    private List<PolymerModification> distinctPolyMods;
    private List<String> distinctSpecificApplications;

    private List<String> distinctTestedBy;

    private double mFIMin = 0.1;
    private double mFIMax = 200D;
    private double mFIStep = 0.01;
    private int mFIDecimal = 2;

    private double charpyMin;
    private double charpyMax;
    private double charpyStep;
    private int charpyDecimal;

    private double izodMin;
    private double izodMax;
    private double izodStep;
    private int izodDecimal;

    private double kvalueMin = 50D;
    private double kvalueMax = 80D;
    private double kvalueStep = 0.01;
    private int kvalueDecimal = 2;

    private double hardnessMin = 1D;
    private double hardnessMax = 100D;
    private double hardnessStep = 1D;
    private int hardnessDecimal = 0;

    private double vicatMin;
    private double vicatMax;
    private double vicatStep;
    private int vicatDecimal;

    private double densityMin;
    private double densityMax;
    private double densityStep;
    private int densityDecimal;

    private double adensityMin;
    private double adensityMax;
    private double adensityStep;
    private int adensityDecimal;

    private double eModulusMin;
    private double eModulusMax;
    private double eModulusStep;
    private int eModulusDecimal;

    private double flexuralModulusMin;
    private double flexuralModulusMax;
    private double flexuralModulusStep;
    private int flexuralModulusDecimal;

    private double strengthAtYieldMin;
    private double strengthAtYieldMax;
    private double strengthAtYieldStep;
    private int strengthAtYieldDecimal;

    private double strainAtYieldMin = 0.5;
    private double strainAtYieldMax = 50D;
    private double strainAtYieldStep = 0.5;
    private int strainAtYieldDecimal = 1;

    private double strainAtBreakMin = 1D;
    private double strainAtBreakMax = 2000D;
    private double strainAtBreakMaxMDTD = 1000D;
    private double strainAtBreakStep = 0.1;
    private int strainAtBreakDecimal = 1;

    private double hDTMin;
    private double hDTMax;
    private double hDTStep;
    private int hDTDecimal;

    private double oITMin = 1D;
    private double oITMax = 200D;
    private double oITStep = 1D;
    private int oITDecimal = 0;

    private double mouldShrinkageMin = 0D;
    private double mouldShrinkageMax = 5D;
    private double mouldShrinkageStep = 0.01D;
    private int mouldShrinkageDecimal = 2;

    private double moistMin = 0D;
    private double moistMax = 10D;
    private double moistStep = 0.001D;
    private int moistDecimal = 3;

    private double ashMin = 0D;
    private double ashMax = 60D;
    private double ashStep = 0.001D;
    private int ashDecimal = 3;

    private double pollutionMin = 0D;
    private double pollutionMax = 50D;
    private double pollutionStep = 0.01D;
    private int pollutionDecimal = 2;

    private double modificationMin = 0D;
    private double modificationMax = 80D;
    private double modificationStep = 0.5D;
    private int modificationDecimal = 1;

    private double recyclingMin = 0D;
    private double recyclingMax = 100D;
    private double recyclingStep = 1D;
    private int recyclingDecimal = 0;

    private double meltFiltrationMin = 0D;
    private double meltFiltrationMax = 1000D;
    private double meltFiltrationStep = 15D;
    private int meltFiltrationDecimal = 0;

    private List<Boolean> activeColumns1;

    private boolean showUnknownProperties = true;
    private boolean showOnlyOwned = false;
    private boolean showAutoGen = false;
    private boolean showResolved = false;
    private boolean showLoading = false;
    private boolean editable = false;

    private long lastRequestMail = 0;
    private final long timeOutTimeSeconds = 3;

    private boolean alreadyReloading = false;
    private boolean retriggerReloading = false;

    private UserEntity user;

    @ManagedProperty(value = "#{itemValueService}")
    private ItemValueService itemValueService;

    @ManagedProperty(value = "#{requestService}")
    private RequestService requestService;

    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    @ManagedProperty(value = "#{verificationRequestService}")
    private VerificationRequestService verificationService;

    private LoginService loginService;

    private UnitConverterService unitConverterService;

    @PostConstruct
    public void init() {
        requests = requestService.getRequests();

        filteredRequests = requests;
        //Material,    TestesBy    Supplier   ProcesTech RecContent  MFIWeight   MFI     k value density apDensity emodulus    TensStr     TensElo  EloBreak    Chapt20 Chapt-20    rockwell    HDT     Vicat   OIT     Humidity    AshConent   Color   Pollution   MouldShinkage   Smell
        //Material,    TestesBy    Supplier   ProcesTech RecContent  MFIWeight   MFI     k value density apDensity emodulus    TensStr     TensElo  EloBreak    Chapt20 Chapt-20    rockwell    HDT     Vicat   OIT     Humidity    AshConent   Color   Pollution   MouldShinkage   Smell      TargetMarket   TargetProduct
        activeColumns1 = Arrays.asList(true, false, false, true, true, true, true, true, true, false, true, true, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true);

        HttpSession httpSession = SessionUtils.getSession();
        loginService = (LoginService) httpSession.getAttribute("loginService");
        user = loginService.getCurrentUser();

        for (RequestEntity r : requests) {
            r.setCurrentMFICondition(currentRequest.getCurrentMFICondition());
            r.setCurrentIzodImpactStrCondition(currentRequest.getCurrentIzodImpactStrCondition());
            r.setCurrentCharpyImpactStrCondition(currentRequest.getCurrentCharpyImpactStrCondition());
            r.setCurrentHardness(currentRequest.getCurrentHardness());
            r.setCurrentVicatCondition(currentRequest.getCurrentVicatCondition());
        }
        unitConverterService = new UnitConverterService();

        reloadSideData();

    }

    public void reloadSideData() {
        if (!alreadyReloading) {
            alreadyReloading = true;

            Logger.getLogger(BuyerView.class.getName()).info("Reloading side data");

            distinctMaterials = itemValueService.getMaterials();
            distinctProcessTechs = itemValueService.getProcessTechs();
            distinctColors = itemValueService.getColors();
            distinctParticleGeo = itemValueService.getParticleGeos();
            distinctPolyMods = itemValueService.getPolymerModifications();
            distinctTargetMarkets = itemValueService.getTargetMarkets();
            distinctTargetProducts = itemValueService.getTargetProducts();
            distinctSpecificApplications = requestService.getDistinctSpecificApplication();

            distinctTestedBy = requestService.getDistinctTestedBy();
            /*
            mFIMin = requestService.getMFIMin();
            mFIMax = requestService.getMFIMax();
            charpyMin = requestService.getCharpyMin();
            charpyMax = requestService.getCharpyMax();
            izodMin = requestService.getIzodMin();
            izodMax = requestService.getIzodMax();
            hardnessMin = requestService.getIzodMin();
            hardnessMax = requestService.getIzodMax();
            vicatMin = requestService.getVicatMin();
            vicatMax = requestService.getVicatMax();
             */

            charpyMin = unitConverterService.convertUnit(1D, user.getConfig().getCharpyImpactStrengthPrefferedUnit(), Units.KJPM2);
            charpyMax = unitConverterService.convertUnit(200D, user.getConfig().getCharpyImpactStrengthPrefferedUnit(), Units.KJPM2);
            charpyStep = unitConverterService.convertUnit(0.1, user.getConfig().getCharpyImpactStrengthPrefferedUnit(), Units.KJPM2);
            charpyDecimal = unitConverterService.getFirstSignificantDigitPos(charpyStep);

            izodMin = unitConverterService.convertUnit(1D, user.getConfig().getIzodImpactStrengthPrefferedUnit(), Units.JPM);
            izodMax = unitConverterService.convertUnit(500D, user.getConfig().getIzodImpactStrengthPrefferedUnit(), Units.JPM);
            izodStep = unitConverterService.convertUnit(0.1, user.getConfig().getIzodImpactStrengthPrefferedUnit(), Units.JPM);
            izodDecimal = unitConverterService.getFirstSignificantDigitPos(izodStep);

            eModulusMin = unitConverterService.convertUnit(25D, user.getConfig().getEModulusPrefferedUnit(), Units.MPA);
            eModulusMax = unitConverterService.convertUnit(20000D, user.getConfig().getEModulusPrefferedUnit(), Units.MPA);
            eModulusStep = unitConverterService.convertUnit(5D, user.getConfig().getEModulusPrefferedUnit(), Units.MPA);
            eModulusDecimal = unitConverterService.getFirstSignificantDigitPos(eModulusStep);

            flexuralModulusMin = unitConverterService.convertUnit(25D, user.getConfig().getFlexuralModulusPrefferdUnit(), Units.MPA);
            flexuralModulusMax = unitConverterService.convertUnit(20000D, user.getConfig().getFlexuralModulusPrefferdUnit(), Units.MPA);
            flexuralModulusStep = unitConverterService.convertUnit(5D, user.getConfig().getFlexuralModulusPrefferdUnit(), Units.MPA);
            flexuralModulusDecimal = unitConverterService.getFirstSignificantDigitPos(flexuralModulusStep);

            strengthAtYieldMin = unitConverterService.convertUnit(5D, user.getConfig().getTensileStrenghtAtYieldPrefferedUnit(), Units.MPA);
            strengthAtYieldMax = unitConverterService.convertUnit(200D, user.getConfig().getTensileStrenghtAtYieldPrefferedUnit(), Units.MPA);
            strengthAtYieldStep = unitConverterService.convertUnit(0.5, user.getConfig().getTensileStrenghtAtYieldPrefferedUnit(), Units.MPA);
            strengthAtYieldDecimal = unitConverterService.getFirstSignificantDigitPos(strengthAtYieldStep);

            vicatMin = unitConverterService.convertUnit(20D, user.getConfig().getVicatPrefferedUnit(), Units.CELSIUS);
            vicatMax = unitConverterService.convertUnit(200D, user.getConfig().getVicatPrefferedUnit(), Units.CELSIUS);
            vicatStep = unitConverterService.convertUnit(1D, user.getConfig().getVicatPrefferedUnit(), Units.CELSIUS);
            vicatDecimal = unitConverterService.getFirstSignificantDigitPos(vicatStep);

            hDTMin = unitConverterService.convertUnit(20D, user.getConfig().getHDTPrefferedUnit(), Units.CELSIUS);
            hDTMax = unitConverterService.convertUnit(200D, user.getConfig().getHDTPrefferedUnit(), Units.CELSIUS);
            hDTStep = unitConverterService.convertUnit(1D, user.getConfig().getHDTPrefferedUnit(), Units.CELSIUS);
            hDTDecimal = unitConverterService.getFirstSignificantDigitPos(hDTStep);

            densityMin = unitConverterService.convertUnit(0.8, user.getConfig().getDensityPrefferedUnit(), Units.GRPCM3);
            densityMax = unitConverterService.convertUnit(2.5, user.getConfig().getDensityPrefferedUnit(), Units.GRPCM3);
            densityStep = unitConverterService.convertUnit(0.01, user.getConfig().getDensityPrefferedUnit(), Units.GRPCM3);
            densityDecimal = unitConverterService.getFirstSignificantDigitPos(densityStep);

            adensityMin = unitConverterService.convertUnit(0.25, user.getConfig().getBulkDensityPrefferedUnit(), Units.GRPCM3);
            adensityMax = unitConverterService.convertUnit(2D, user.getConfig().getBulkDensityPrefferedUnit(), Units.GRPCM3);
            adensityStep = unitConverterService.convertUnit(0.01, user.getConfig().getBulkDensityPrefferedUnit(), Units.GRPCM3);
            adensityDecimal = unitConverterService.getFirstSignificantDigitPos(adensityStep);

            if (currentRequest.getMFI().getValueMin() == null) {
                currentRequest.setMFI(mFIMin, mFIMax);
                currentRequest.setHardnes(hardnessMin, hardnessMax);
                currentRequest.setMouldShrinkage(mouldShrinkageMin, mouldShrinkageMax);
                currentRequest.setFlexuralModulus(flexuralModulusMin, flexuralModulusMax);
                currentRequest.setEModulusMD(eModulusMin, eModulusMax);
                currentRequest.setEModulusTD(eModulusMin, eModulusMax);
                currentRequest.setEModulus(eModulusMin, eModulusMax);
                currentRequest.setBulkDensity(adensityMin, adensityMax);
                currentRequest.setDensity(densityMin, densityMax);
                currentRequest.setKValue(kvalueMin, kvalueMax);
                currentRequest.setModificationContent(modificationMin, modificationMax);
            }
            /*
            densityMin = requestService.getDensityMin();
            densityMax = requestService.getDensityMax();
            adensityMin = requestService.getAdensityMin();
             */

            //convertUnits()
            //SliderValue slider = (SliderValue) FacesContext.getCurrentInstance().getViewRoot().findComponent("providerViewB:recycleds:mfiColumn:mfiSlider");
            //SliderValue slider = (SliderValue) ComponentFinder.findComponent("mfiSlider");
            //slider.setMaxValue(mFIMax);
            //slider.setMinValue(mFIMax);
            PrimeFaces.current().ajax().update("providerViewB");

            //PrimeFaces.current().ajax().update("providerViewB:recycleds:mfiColumn:mfiSlider");
            alreadyReloading = false;

            if (retriggerReloading) {
                retriggerReloading = false;
                reloadSideData();
            }
        } else {
            retriggerReloading = true;
        }
    }

    public void reloadData(UserEntity u) {
        //showLoading = true;
        //PrimeFaces.current().ajax().update("providerViewB:loadWidget");
        if (!loginService.getCurrentUser().isAdminTag()) {
            showAutoGen = false;
            showResolved = false;
        }
        if (showOnlyOwned) {
            requests = requestService.getOwnedRequests(u, showAutoGen, showResolved);
        } else {
            requests = requestService.getRequests(showAutoGen, showResolved);
            filteredRequests = requests;

        }

        for (RequestEntity r : requests) {
            r.setCurrentMFICondition(currentRequest.getCurrentMFICondition());
            r.setCurrentIzodImpactStrCondition(currentRequest.getCurrentIzodImpactStrCondition());
            r.setCurrentCharpyImpactStrCondition(currentRequest.getCurrentCharpyImpactStrCondition());
            r.setCurrentHardness(currentRequest.getCurrentHardness());
            r.setCurrentVicatCondition(currentRequest.getCurrentVicatCondition());
        }
        reloadSideData();
        //PrimeFaces.current().ajax().update("providerViewB:requests");
    }

    public void openRequestDialog() {
        if (canHandleSelected()) {
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('requestssDialog').show();");
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only edit owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public boolean canHandleSelected() {
        if (selectedRequest == null) {
            return false;
        }
        return loginService.getCurrentUser().isAdminTag() || selectedRequest.getCreatedBy().equals(loginService.getCurrentUser().getUsername());
    }

    public boolean canHandleSelected(RequestEntity r) {
        if (r == null) {
            return false;
        }
        return loginService.getCurrentUser().isAdminTag() || r.getCreatedBy().equals(loginService.getCurrentUser().getUsername());
    }

    public void deleteRecycled() {
        if (canHandleSelected()) {
            requests.remove(selectedRequest);
            requestService.deleteInstance(selectedRequest);
            selectedRequest = null;
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only delete owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void resolveRequest() {
        if (canHandleSelected()) {
            if (!showResolved) {
                requests.remove(selectedRequest);
            }

            selectedRequest.setRequestResolved(true);
            requestService.updateInstance(selectedRequest);
            selectedRequest = null;
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only resolve owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void updateInstance() {
        if (canHandleSelected()) {
            PrimeFaces.current().ajax().update("editForm:requestsDetails");
            RequestEntity r = selectedRequest;
            Logger.getLogger(BuyerView.class.getName()).warning("Do update for: " + r);
            if (newObject) {
                requestService.updateInstance(r);
            } else {
                requestService.updateInstance(r);
            }
            //selectedRecycled = null;
            if (!loginService.getCurrentUser().isAdminTag()) {
                showAutoGen = false;
            }
            requests = requestService.getRequests(showAutoGen, showResolved);
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only edit owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public boolean filterGEQ(Object value, Object filter, Locale locale) {
        if (filter == null || filter.toString().equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return Double.parseDouble(value.toString()) >= Double.parseDouble(filter.toString());

    }

    public boolean filterLEQ(Object value, Object filter, Locale locale) {
        if (filter == null || filter.toString().equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return Double.parseDouble(value.toString()) <= Double.parseDouble(filter.toString());

    }

    public boolean filterByRange(Object value, Object filter, Locale locale) {
        /*
            String filterText = (filter == null) ? null : filter.toString().trim();
            if (filterText == null || filterText.isEmpty()) {
                return true;
            }
            if (value == null) {
                return false;
            }*/
        RankableRangedEntry filterRec = (RankableRangedEntry) value;
        RankableRangedEntry toFilter = (RankableRangedEntry) filter;
        /*
            String fromPart = filterText.substring(0, filterText.indexOf("-"));
            String toPart = filterText.substring(filterText.indexOf("-") + 1);

            if(fromPart == null || fromPart.isEmpty()){
                fromPart = toPart;
            }

            if(toPart == null || toPart.isEmpty()){
                return true;
            }
            
            double fromValue = Double.parseDouble(fromPart);
            double toValue = Double.parseDouble(toPart);*/
        if (toFilter.getValueMin() == null || toFilter.getValueMax() == null) {
            return true;
        }

              if (filterRec.getName().equals("mfi")) {
            if (mFIMin == toFilter.getValueMin().doubleValue() && mFIMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().equals("kValue")) {
            if (kvalueMin == toFilter.getValueMin().doubleValue() && kvalueMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().equals("modificationContent")) {
            if (modificationMin == toFilter.getValueMin().doubleValue() && modificationMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().contains("density")) {
            if (densityMin == toFilter.getValueMin().doubleValue() && densityMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().contains("bulkDensity")) {
            if (adensityMin == toFilter.getValueMin().doubleValue() &&  adensityMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().startsWith("eModulus")) {
            if (eModulusMin == toFilter.getValueMin().doubleValue() && eModulusMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().equals("flexuralModulus")) {
            if (flexuralModulusMin == toFilter.getValueMin().doubleValue() && flexuralModulusMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().equals("moldShrinkage")) {
            if (mouldShrinkageMin == toFilter.getValueMin().doubleValue() && mouldShrinkageMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().equals("hardness")) {
            if (hardnessMin == toFilter.getValueMin().doubleValue() && hardnessMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (filterRec.getName().equals("vicat")) {
            if (vicatMin == toFilter.getValueMin().doubleValue() && vicatMax == toFilter.getValueMax().doubleValue()) {
                return true;
            }
        }

        if (filterRec.getValueMin() == null) {
            return false;
        }

        if (filterRec.getValueMax() == null) {
            return toFilter.getValueMin() <= filterRec.getValueMin() && toFilter.getValueMax() >= filterRec.getValueMin();
        }

        return toFilter.getValueMin() <= filterRec.getValueMin() && toFilter.getValueMax() >= filterRec.getValueMin()
                || toFilter.getValueMin() <= filterRec.getValueMax() && toFilter.getValueMax() >= filterRec.getValueMax()
                || toFilter.getValueMin() >= filterRec.getValueMin() && toFilter.getValueMax() <= filterRec.getValueMax()
                || toFilter.getValueMin() <= filterRec.getValueMin() && toFilter.getValueMax() >= filterRec.getValueMax();

    }

    public void initReset() {
        Logger.getLogger(ValidationRequestEntity.class.getName()).warning("Reseting Init!!! ");
        resetDataTableForm();
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('requestsTable').clearFilters();");
    }

    public void resetForm() {
        PrimeFaces.current().resetInputs("editForm:requestsDetails");
    }

    public void resetForm(String form) {
        Logger.getLogger(ValidationRequestEntity.class.getName()).warning("Reseting: " + form);

        PrimeFaces.current().resetInputs(form);
    }

    public void resetDataTableForm() {
        newSelectedRequest(user.getUsername());
        currentRequest = new RequestEntity();
        reloadSideData();
        PrimeFaces.current().resetInputs("providerViewB");
        PrimeFaces.current().ajax().update("providerViewB");

    }

    public void getContact(UserEntity u, RequestEntity r) {
        if ((lastRequestMail + timeOutTimeSeconds - (System.currentTimeMillis() / 1000)) > 0) {
            FacesMessage message = new FacesMessage("Error", "To many contact requests! Try again in a few seconds.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        lastRequestMail = System.currentTimeMillis() / 1000;

        boolean success = mailService.sendContact(u, r);
        if (success) {
            FacesMessage message = new FacesMessage("Successful", "Contact is sent.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("Error", "Could not send contact.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Logger.getLogger(ValidationRequestEntity.class.getName()).warning("Error on contect mail send");

        }
    }

    public void newSelectedRequest(String user) {
        selectedRequest = new RequestEntity();
        selectedRequest.setCreatedBy(user);
    }

    public boolean checkForEnableContextSubmit() {
        //return contactTargetIndustry == null || contactTargetIndustry.isBlank() || contactTargetProduct == null || contactTargetProduct.isBlank() || (contactTargetIndustry.equals("Other") && (contactTargetIndustryText == null || contactTargetIndustryText.isBlank())) || (contactTargetProduct.equals("Other") && (contactTargetProductText == null || contactTargetProductText.isBlank()));
        return contactTargetIndustry == null & contactTargetProduct == null;
    }

    public List<Boolean> getActiveColumns1() {
        return activeColumns1;
    }

    public void onToggle(ToggleEvent e) {
        activeColumns1.set(((Integer) e.getData()) - 1, e.getVisibility() == Visibility.VISIBLE);
    }

    public void uploadFile(UploadedFile f) {
        try {
            boolean success = requestService.uploadFile(f.getInputStream());
            if (success) {
                FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                if (!loginService.getCurrentUser().isAdminTag()) {
                    showAutoGen = false;
                }
                requests = requestService.getRequests(showAutoGen, showResolved);
                reloadSideData();

            } else {
                FacesMessage message = new FacesMessage("Fail", f.getFileName() + " could not be uplaoded or parsed");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } catch (IOException ex) {
            Logger.getLogger(BuyerView.class.getName()).log(Level.SEVERE, null, ex);
            FacesMessage message = new FacesMessage("Fail", f.getFileName() + " could not be uplaoded or parsed");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void upload() {
        if (file != null) {
            uploadFile(file);
        }
    }

    public void uploadMultiple() {
        if (files != null) {
            for (UploadedFile f : files.getFiles()) {
                uploadFile(f);
            }
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        uploadFile(event.getFile());
    }

    public void handleFilesUpload(FilesUploadEvent event) {
        for (UploadedFile f : event.getFiles().getFiles()) {
            uploadFile(f);
        }
    }

    public List<RequestEntity> getRequests() {
        return requests;
    }

    public List<RequestEntity> getFilteredRequests() {
        return filteredRequests;
    }

    public void setFilteredRequests(List<RequestEntity> filteredRequests) {
        this.filteredRequests = filteredRequests;
    }

    public boolean isShowOnlyOwned() {
        return showOnlyOwned;
    }

    public void setShowOnlyOwned(boolean showOnlyOwned) {
        this.showOnlyOwned = showOnlyOwned;
    }

    public boolean isShowAutoGen() {
        return showAutoGen;
    }

    public void setShowAutoGen(boolean showAutoGen) {
        this.showAutoGen = showAutoGen;
    }

    public boolean isShowResolved() {
        return showResolved;
    }

    public void setShowResolved(boolean showResolved) {
        this.showResolved = showResolved;
    }

    public List<InventoryStatus> getInventoryStatus() {
        return Arrays.asList(InventoryStatus.values());
    }

    public List<FilterMeta> getFilterBy() {
        return filterBy;
    }

    public void increaseRange1(Double cvalue, Double defaultV, Double defaultV2, Double limit, RankableRangedEntry r, Double step) {
        if (cvalue == null) {
            double newValue = Math.round((defaultV + step) * (1 / step)) / (1 / step);

            r.setValueMin(newValue);
            r.setValueMax(defaultV2);
        } else {
            double setValue = Math.round((cvalue + step) * (1 / step)) / (1 / step);
            if (setValue < limit) {
                r.setValueMin(setValue);
            }
        }
    }

    public void increaseRange2(Double cvalue, Double minDefault, Double limit, RankableRangedEntry r, Double step) {
        if (cvalue == null) {
            r.setValueMax(limit);
            r.setValueMin(minDefault);
        } else {
            double setValue = Math.round((cvalue + step) * (1 / step)) / (1 / step);
            if (setValue <= limit) {
                r.setValueMax(setValue);
            }
        }
    }

    public void decreaseRange1(Double cvalue, Double maxDefault, Double limit, RankableRangedEntry r, Double step) {
        if (cvalue == null) {
            r.setValueMin(limit);
            r.setValueMax(maxDefault);
        } else {
            double setValue = Math.round((cvalue - step) * (1 / step)) / (1 / step);
            if (setValue >= limit) {
                r.setValueMin(setValue);
            }
        }
    }

    public void decreaseRange2(Double cvalue, Double defaultV, Double defaultV2, Double limit, RankableRangedEntry r, Double step) {
        if (cvalue == null) {
            double newValue = Math.round((defaultV - step) * (1 / step)) / (1 / step);

            r.setValueMax(newValue);
            r.setValueMin(defaultV2);
        } else {

            double setValue = Math.round((cvalue - step) * (1 / step)) / (1 / step);
            if (setValue > limit) {
                r.setValueMax(setValue);
            }
        }
    }

    public double getMFIMin() {
        return mFIMin;
    }

    public double getMFIMax() {
        return mFIMax;
    }

    public double getHardnessMin() {
        return hardnessMin;
    }

    public double getVicatMin() {
        return vicatMin;
    }

    public double getVicatMax() {
        return vicatMax;
    }

    public double getHardnessMax() {
        return hardnessMax;
    }

    public double getCharpyMin() {
        return charpyMin;
    }

    public double getCharpyMax() {
        return charpyMax;
    }

    public double getIzodMin() {
        return izodMin;
    }

    public double getIzodMax() {
        return izodMax;
    }

    public double getDensityMin() {
        return densityMin;
    }

    public double getDensityMax() {
        return densityMax;
    }

    public double getAdensityMin() {
        return adensityMin;
    }

    public RequestEntity getFilteredByRequest() {
        return filteredByRequest;
    }

    public void setFilteredByRequest(RequestEntity filteredByRequest) {
        this.filteredByRequest = filteredByRequest;
    }

    public RequestEntity getSelectedRequest() {
        return selectedRequest;
    }

    public void setSelectedRequest(RequestEntity selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    public RankedRecycled getSelectedRankingRecycled() {
        return selectedRankingRecycled;
    }

    public void setSelectedRankingRecycled(RankedRecycled selectedRankingRecycled) {
        this.selectedRankingRecycled = selectedRankingRecycled;
    }

    public String getContactTargetProductText() {
        return contactTargetProductText;
    }

    public void setContactTargetProductText(String contactTargetProductText) {
        this.contactTargetProductText = contactTargetProductText;
    }

    public String getContactTargetProduct() {
        return contactTargetProduct;
    }

    public String getContactSpecificApplicationText() {
        return contactSpecificApplicationText;
    }

    public void setContactSpecificApplicationText(String contactSpecificApplicationText) {
        this.contactSpecificApplicationText = contactSpecificApplicationText;
    }

    public void setContactTargetProduct(String contactTargetProduct) {
        this.contactTargetProduct = contactTargetProduct;
    }

    public String getContactTargetIndustryText() {
        return contactTargetIndustryText;
    }

    public void setContactTargetIndustryText(String contactTargetIndustryText) {
        this.contactTargetIndustryText = contactTargetIndustryText;
    }

    public String getContactTargetIndustry() {
        return contactTargetIndustry;
    }

    public void setContactTargetIndustry(String contactTargetIndustry) {
        this.contactTargetIndustry = contactTargetIndustry;
    }

    public List<Material> getDistinctMaterials() {
        return distinctMaterials;
    }

    public void setDistinctMaterials(List<Material> distinctMaterials) {
        this.distinctMaterials = distinctMaterials;
    }

    public List<ProcessTech> getDistinctProcessTechs() {
        return distinctProcessTechs;
    }

    public void setDistinctProcessTechs(List<ProcessTech> distinctProcessTechs) {
        this.distinctProcessTechs = distinctProcessTechs;
    }

    public List<Color> getDistinctColors() {
        return distinctColors;
    }

    public List<ParticleGeo> getDistinctParticleGeo() {
        return distinctParticleGeo;
    }

    public List<PolymerModification> getDistinctPolyMods() {
        return distinctPolyMods;
    }

    public List<String> getDistinctTestedBy() {
        return distinctTestedBy;
    }

    public void setDistinctTestedBy(List<String> distinctTestedBy) {
        this.distinctTestedBy = distinctTestedBy;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setVerificationService(VerificationRequestService verificationService) {
        this.verificationService = verificationService;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFiles getFiles() {
        return files;
    }

    public void setFiles(UploadedFiles files) {
        this.files = files;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isShowUnknownProperties() {
        return showUnknownProperties;
    }

    public void setShowUnknownProperties(boolean showUnknownProperties) {
        this.showUnknownProperties = showUnknownProperties;
    }

    public RequestEntity getCurrentRequest() {
        return currentRequest;
    }

    public void setCurrentRequest(RequestEntity currentRequest) {
        this.currentRequest = currentRequest;
    }

    public void setRequestService(RequestService service) {
        this.requestService = service;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setItemValueService(ItemValueService itemValueService) {
        this.itemValueService = itemValueService;
    }

    public List<TargetMarket> getDistinctTargetMarkets() {
        return distinctTargetMarkets;
    }

    public List<TargetProduct> getDistinctTargetProducts() {
        return distinctTargetProducts;
    }

    public List<String> getDistinctSpecificApplications() {
        return distinctSpecificApplications;
    }

    public double getEModulusMax() {
        return eModulusMax;
    }

    public double getEModulusMin() {
        return eModulusMin;
    }

    public double getStrengthAtYieldMin() {
        return strengthAtYieldMin;
    }

    public double getStrengthAtYieldMax() {
        return strengthAtYieldMax;
    }

    public double getHDTMax() {
        return hDTMax;
    }

    public double getHDTMin() {
        return hDTMin;
    }

    public double getMFIStep() {
        return mFIStep;
    }

    public double getCharpyStep() {
        return charpyStep;
    }

    public double getIzodStep() {
        return izodStep;
    }

    public double getVicatStep() {
        return vicatStep;
    }

    public double getHardnessStep() {
        return hardnessStep;
    }

    public double getDensityStep() {
        return densityStep;
    }

    public int getAdensityDecimal() {
        return adensityDecimal;
    }

    public double getAdensityStep() {
        return adensityStep;
    }

    public int getCharpyDecimal() {
        return charpyDecimal;
    }

    public int getDensityDecimal() {
        return densityDecimal;
    }

    public int getEModulusDecimal() {
        return eModulusDecimal;
    }

    public int getHardnessDecimal() {
        return hardnessDecimal;
    }

    public int getHDTDecimal() {
        return hDTDecimal;
    }

    public int getIzodDecimal() {
        return izodDecimal;
    }

    public int getMFIDecimal() {
        return mFIDecimal;
    }

    public int getStrengthAtYieldDecimal() {
        return strengthAtYieldDecimal;
    }

    public double getStrengthAtYieldStep() {
        return strengthAtYieldStep;
    }

    public int getVicatDecimal() {
        return vicatDecimal;
    }

    public double getEModulusStep() {
        return eModulusStep;
    }

    public int getFlexuralModulusDecimal() {
        return flexuralModulusDecimal;
    }

    public double getFlexuralModulusMax() {
        return flexuralModulusMax;
    }

    public double getFlexuralModulusMin() {
        return flexuralModulusMin;
    }

    public double getFlexuralModulusStep() {
        return flexuralModulusStep;
    }

    public int getStrainAtYieldDecimal() {
        return strainAtYieldDecimal;
    }

    public double getStrainAtYieldMax() {
        return strainAtYieldMax;
    }

    public double getStrainAtYieldMin() {
        return strainAtYieldMin;
    }

    public double getStrainAtYieldStep() {
        return strainAtYieldStep;
    }

    public int getStrainAtBreakDecimal() {
        return strainAtBreakDecimal;
    }

    public double getStrainAtBreakMax() {
        return strainAtBreakMax;
    }

    public double getStrainAtBreakMaxMDTD() {
        return strainAtBreakMaxMDTD;
    }

    public double getStrainAtBreakMin() {
        return strainAtBreakMin;
    }

    public double getStrainAtBreakStep() {
        return strainAtBreakStep;
    }

    public double getHDTStep() {
        return hDTStep;
    }

    public int getOITDecimal() {
        return oITDecimal;
    }

    public double getOITMax() {
        return oITMax;
    }

    public double getOITMin() {
        return oITMin;
    }

    public double getOITStep() {
        return oITStep;
    }

    public double getMouldShrinkageMax() {
        return mouldShrinkageMax;
    }

    public double getMouldShrinkageMin() {
        return mouldShrinkageMin;
    }

    public int getMouldShrinkageDecimal() {
        return mouldShrinkageDecimal;
    }

    public double getMouldShrinkageStep() {
        return mouldShrinkageStep;
    }

    public double getMoistMin() {
        return moistMin;
    }

    public double getMoistMax() {
        return moistMax;
    }

    public double getMoistStep() {
        return moistStep;
    }

    public int getMoistDecimal() {
        return moistDecimal;
    }

    public double getAshMin() {
        return ashMin;
    }

    public double getAshMax() {
        return ashMax;
    }

    public double getAshStep() {
        return ashStep;
    }

    public int getAshDecimal() {
        return ashDecimal;
    }

    public double getPollutionMin() {
        return pollutionMin;
    }

    public double getPollutionMax() {
        return pollutionMax;
    }

    public double getPollutionStep() {
        return pollutionStep;
    }

    public int getPollutionDecimal() {
        return pollutionDecimal;
    }

    public double getModificationMin() {
        return modificationMin;
    }

    public double getModificationMax() {
        return modificationMax;
    }

    public double getModificationStep() {
        return modificationStep;
    }

    public int getModificationDecimal() {
        return modificationDecimal;
    }

    public double getRecyclingMin() {
        return recyclingMin;
    }

    public double getRecyclingMax() {
        return recyclingMax;
    }

    public double getRecyclingStep() {
        return recyclingStep;
    }

    public int getRecyclingDecimal() {
        return recyclingDecimal;
    }

    public double getMeltFiltrationMin() {
        return meltFiltrationMin;
    }

    public double getMeltFiltrationMax() {
        return meltFiltrationMax;
    }

    public double getMeltFiltrationStep() {
        return meltFiltrationStep;
    }

    public int getMeltFiltrationDecimal() {
        return meltFiltrationDecimal;
    }

    public double getAdensityMax() {
        return adensityMax;
    }

    public double getKvalueMin() {
        return kvalueMin;
    }

    public double getKvalueMax() {
        return kvalueMax;
    }

    public double getKvalueStep() {
        return kvalueStep;
    }

    public int getKvalueDecimal() {
        return kvalueDecimal;
    }

    
}
