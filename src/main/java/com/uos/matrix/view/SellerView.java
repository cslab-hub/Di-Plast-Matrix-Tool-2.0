/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.view;

import com.google.common.collect.Lists;
import com.uos.matrix.UIClasses.SliderValue;
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
import com.uos.matrix.entities.RequestContext;
import com.uos.matrix.entities.RequestedCombi;
import com.uos.matrix.entities.TargetMarket;
import com.uos.matrix.entities.TargetProduct;
import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.entities.ValidationRequestEntity;
import com.uos.matrix.helper.enums.InventoryStatus;
import com.uos.matrix.helper.enums.MFIConditions;
import com.uos.matrix.helper.enums.ParticleGeometry;
import com.uos.matrix.helper.enums.PolyModifications;
import com.uos.matrix.helper.enums.RankingModes;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.other.HibernateUtil;
import com.uos.matrix.helper.other.RankingComperator;
import com.uos.matrix.helper.other.SessionUtils;
import com.uos.matrix.helper.quantileTransformer.ITransformer;
import com.uos.matrix.helper.quantileTransformer.NormalizerTransformer;
import com.uos.matrix.helper.quantileTransformer.QuantileTransformer;
import com.uos.matrix.helper.quantileTransformer.StandardizeTransformer;
import com.uos.matrix.services.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.PostLoad;
import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.openimaj.math.matrix.MatrixUtils;
import org.openimaj.math.statistics.distribution.MixtureOfGaussians;
import org.openimaj.ml.gmm.GaussianMixtureModelEM;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.PrimeFacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.Visibility;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.file.UploadedFiles;

@ManagedBean
@SessionScoped
public class SellerView implements Serializable {

    private List<RecycledEntity> recycled;

    private List<RecycledEntity> filteredRecycled;

    private List<RankedRecycled> recycledRanking;

    private List<RequestEntity> requests;

    private boolean isRanking = false;
    private boolean doRankingAgain = false;

    private boolean newObject = false;

    private UploadedFile file;
    private UploadedFiles files;

    //TODO Wenn dieser gesetzt wird auch in andere Felder setzen? Also vll current value hier drauf referenzieren?
    private RequestEntity currentRequest = new RequestEntity();

    private RequestEntity filteredByRequest;
    private RecycledEntity selectedRecycled;
    private RankedRecycled selectedRankingRecycled;

    private List<FilterMeta> filterBy;

    private TargetProduct contactTargetProductText;
    private TargetProduct contactTargetProduct;
    private TargetMarket contactTargetIndustryText;
    private TargetMarket contactTargetIndustry;
    private String contactSpecificApplicationText;

    //TODO fixed values + selection process?
    private List<Material> distinctMaterials;
    private List<ProcessTech> distinctProcessTechs;
    private List<Color> distinctColors;
    private List<TargetMarket> distinctTargetMarkets;
    private List<TargetProduct> distinctTargetProducts;
    private List<ParticleGeo> distinctParticleGeo;
    private List<PolymerModification> distinctPolyMods;

    private List<String> distinctTestedBy;

    private double mFIMin = 0.1;
    private double mFIMax = 200D;
    private double mFIStep = 0.01;
    private int mFIDecimal = 2;

    private double kvalueMin = 50D;
    private double kvalueMax = 80D;
    private double kvalueStep = 0.01;
    private int kvalueDecimal = 2;

    private double charpyMin;
    private double charpyMax;
    private double charpyStep;
    private int charpyDecimal;

    private double izodMin;
    private double izodMax;
    private double izodStep;
    private int izodDecimal;

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
    private List<Boolean> activeColumns2;

    private boolean showUnknownProperties = false;
    private Boolean showUnknownPropertiesNew = null;
    private boolean showOnlyOwned = false;
    private boolean showLoading = false;
    private boolean editable = false;

    private long lastRequestMail = 0;
    private final long timeOutTimeSeconds = 3;

    private String previousPage = null;

    private UserEntity user;

    private RankingModes rankingMode = RankingModes.Standardizer;

    @ManagedProperty(value = "#{itemValueService}")
    private ItemValueService itemValueService;

    @ManagedProperty(value = "#{sellerService}")
    private SellerService sellerService;

    @ManagedProperty(value = "#{requestService}")
    private RequestService requestService;

    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    @ManagedProperty(value = "#{verificationRequestService}")
    private VerificationRequestService verificationService;

    @ManagedProperty(value = "#{requestCombiService}")
    private RequestCombiService requestCombiService;

    private LoginService loginService;
    private UnitConverterService unitConverterService;

    private boolean alreadyReloading = false;
    private boolean retriggerReloading = false;

    @PostConstruct
    public void init() {
        recycled = sellerService.getProducts();
        requests = requestService.getRequests();

        filteredRecycled = recycled;
        //currentRequest = new RequestEntity();
        //Material,    TestesBy    Supplier   ProcesTech RecContent  MFIWeight   MFI     k value density apDensity emodulus    TensStr     TensElo  EloBreak    Chapt20 Chapt-20    rockwell    HDT     Vicat   OIT     Humidity    AshConent   Color   Pollution   MouldShinkage   Smell
        activeColumns1 = Arrays.asList(true, false, false, true, true, true, true, true, true, false, true, true, false, false, false, true, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
        activeColumns2 = activeColumns1;

        HttpSession httpSession = SessionUtils.getSession();
        loginService = (LoginService) httpSession.getAttribute("loginService");

        user = loginService.getCurrentUser();

        for (RecycledEntity r : recycled) {
            r.setCurrentMFICondition(currentRequest.getCurrentMFICondition());
            r.setCurrentIzodImpactStrCondition(currentRequest.getCurrentIzodImpactStrCondition());
            r.setCurrentCharpyImpactStrCondition(currentRequest.getCurrentCharpyImpactStrCondition());
            r.setCurrentHardness(currentRequest.getCurrentHardness());
            r.setCurrentVicatCondition(currentRequest.getCurrentVicatCondition());
        }
        unitConverterService = new UnitConverterService();

        reloadData(user);
        //initReset();

    }

    public void reloadSideData() {
        if (!alreadyReloading) {
            alreadyReloading = true;

            Logger.getLogger(SellerView.class.getName()).info("Reloading side data");

            /*
            mFIMin = sellerService.getMFIMin();
            mFIMax = sellerService.getMFIMax();
            charpyMin = sellerService.getCharpyMin();
            charpyMax = sellerService.getCharpyMax();
            izodMin = sellerService.getIzodMin();
            izodMax = sellerService.getIzodMax();
            hardnessMin = sellerService.getIzodMin();
            hardnessMax = sellerService.getIzodMax();
            vicatMin = sellerService.getVicatMin();
            vicatMax = sellerService.getVicatMax();
             */
            if (currentRequest.getMFI().getValueMin() == null) {
                //currentRequest.setCharpyTensileStr(charpyMin);
                //currentRequest.setIzodTensileStr(izodMin);
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
                //currentRequest.setVicat(vicatMin);
            }

            /*
            densityMin = sellerService.getDensityMin();
            densityMax = sellerService.getDensityMax();
            adensityMin = sellerService.getAdensityMin();
            adensityMax = sellerService.getAdensityMax();
             */
            //SliderValue slider = (SliderValue) FacesContext.getCurrentInstance().getViewRoot().findComponent("providerView:recycleds:mfiColumn:mfiSlider");
            //SliderValue slider = (SliderValue) ComponentFinder.findComponent("mfiSlider");
            //slider.setMaxValue(mFIMax);
            //slider.setMinValue(mFIMax);
            PrimeFaces.current().ajax().update("providerView:recycleds");

            //PrimeFaces.current().ajax().update("providerView:recycleds:mfiColumn:mfiSlider");
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
        //PrimeFaces.current().ajax().update("providerView:loadWidget");
        Logger.getLogger(SellerView.class.getName()).info("Reloading main data");

        if (showOnlyOwned) {
            recycled = sellerService.getOwnedProducts(u);
            requests = requestService.getRequests();
            filteredRecycled = recycled;
        } else {
            recycled = sellerService.getProducts();
            requests = requestService.getRequests();
            filteredRecycled = recycled;
        }

        for (RecycledEntity r : recycled) {
            r.setCurrentMFICondition(currentRequest.getCurrentMFICondition());
            r.setCurrentIzodImpactStrCondition(currentRequest.getCurrentIzodImpactStrCondition());
            r.setCurrentCharpyImpactStrCondition(currentRequest.getCurrentCharpyImpactStrCondition());
            r.setCurrentHardness(currentRequest.getCurrentHardness());
            r.setCurrentVicatCondition(currentRequest.getCurrentVicatCondition());
        }

        distinctMaterials = itemValueService.getMaterials();
        distinctProcessTechs = itemValueService.getProcessTechs();
        distinctColors = itemValueService.getColors();
        distinctParticleGeo = itemValueService.getParticleGeos();
        distinctPolyMods = itemValueService.getPolymerModifications();
        distinctTargetMarkets = itemValueService.getTargetMarkets();
        distinctTargetProducts = itemValueService.getTargetProducts();

        distinctTestedBy = sellerService.getDistinctTestedBy();

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

        reloadSideData();
        //PrimeFaces.current().ajax().update("providerView:recycled");
    }

    public void deleteRecycled() {
        if (canHandleSelected()) {
            Logger.getLogger(SellerView.class.getName()).info("Deleting recycled " + selectedRecycled);
            recycled.remove(selectedRecycled);
            sellerService.deleteInstance(selectedRecycled);
            selectedRecycled = null;
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only delete owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void openRequestDialog() {
        if (canHandleSelected()) {
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('recycledsDialog').show();");
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only edit owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public boolean canHandleSelected() {
        if (selectedRecycled == null) {
            return false;
        }
        return loginService.getCurrentUser().isAdminTag() || selectedRecycled.getCreatedBy().equals(loginService.getCurrentUser().getUsername());
    }

    public boolean canHandleSelected(RecycledEntity r) {
        if (r == null) {
            return false;
        }
        return loginService.getCurrentUser().isAdminTag() || r.getCreatedBy().equals(loginService.getCurrentUser().getUsername());
    }

    public void updateInstance() {
        if (showUnknownPropertiesNew != null) {
            showUnknownProperties = showUnknownPropertiesNew.booleanValue();
            showUnknownPropertiesNew = null;
        }

        if (canHandleSelected()) {
            Logger.getLogger(SellerView.class.getName()).info("Updating recycled " + selectedRecycled);
            PrimeFaces.current().ajax().update("editForm:recycledDetails");
            RecycledEntity r = new RecycledEntity();
            if (!r.equals(selectedRecycled)) {
                r = selectedRecycled;
                Logger.getLogger(SellerView.class.getName()).info("Do update for: " + r);
                if (newObject) {
                    sellerService.updateInstance(r);
                } else {
                    sellerService.updateInstance(r);
                }
                //selectedRecycled = null;
                recycled = sellerService.getProducts();
                PrimeFaces.current().ajax().update("providerView:recycleds");
            }
        } else {
            FacesMessage message = new FacesMessage("Error", "You can only edit owned elements");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }


    /*public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (LangUtils.isValueBlank(filterText)) {
            return true;
        }

        Recycled recycled = (Recycled) value;
        return product.getName().toLowerCase().contains(filterText)
                || product.getCode().toLowerCase().contains(filterText)
                || product.getDescription().toLowerCase().contains(filterText)
                || Double.toString(product.getPrice()).contains(filterText)
                || product.getCategory().toLowerCase().contains(filterText)
                || Integer.toString(product.getQuantity()).contains(filterText)
                || Integer.toString(product.getRating()).contains(filterText)
                || product.getInventoryStatus().name().toLowerCase().contains(filterText);
    }*/
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

        if (filterRec.getValueMin() == null) {
            return false;
        }

        if (checkRanges(filterRec, toFilter)) {
            return true;
        }

        if (filterRec.getValueMax() == null) {
            return toFilter.getValueMin() <= filterRec.getValueMin() && toFilter.getValueMax() >= filterRec.getValueMin();
        }

        return toFilter.getValueMin() <= filterRec.getValueMin() && toFilter.getValueMax() >= filterRec.getValueMin()
                || toFilter.getValueMin() <= filterRec.getValueMax() && toFilter.getValueMax() >= filterRec.getValueMax()
                || toFilter.getValueMin() >= filterRec.getValueMin() && toFilter.getValueMax() <= filterRec.getValueMax()
                || toFilter.getValueMin() <= filterRec.getValueMin() && toFilter.getValueMax() >= filterRec.getValueMax();

    }

    private boolean checkRanges(RankableRangedEntry value, RankableRangedEntry filter) {
        if (value.getName().equals("mfi")) {
            if (mFIMin == filter.getValueMin().doubleValue() && mFIMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().equals("kValue")) {
            if (kvalueMin == filter.getValueMin().doubleValue() && kvalueMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().equals("modificationContent")) {
            if (modificationMin == filter.getValueMin().doubleValue() && modificationMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().contains("density")) {
            if (densityMin == filter.getValueMin().doubleValue() && densityMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().contains("bulkDensity")) {
            if (adensityMin == filter.getValueMin().doubleValue() && adensityMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().startsWith("eModulus")) {
            if (eModulusMin == filter.getValueMin().doubleValue() && eModulusMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().equals("flexuralModulus")) {
            if (flexuralModulusMin == filter.getValueMin().doubleValue() && flexuralModulusMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().equals("moldShrinkage")) {
            if (mouldShrinkageMin == filter.getValueMin().doubleValue() && mouldShrinkageMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().equals("hardness")) {
            if (hardnessMin == filter.getValueMin().doubleValue() && hardnessMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        } else if (value.getName().equals("vicat")) {
            if (vicatMin == filter.getValueMin().doubleValue() && vicatMax == filter.getValueMax().doubleValue()) {
                return true;
            }
        }
        return false;
    }

    public void updateRanking() {
        PrimeFaces.current().ajax().update("rankingView:rankingRecycleds");

        //rankingView:rankingRecycleds
    }

    public synchronized boolean doRanking() {
        //variables: string error (0.2), missing error (factor 1), ranking algo (selectable)

        //todo remove if set value methods also set units? or improve clone!
        Logger.getLogger(SellerView.class.getName()).log(Level.WARNING, "Check Ranking {0}", currentRequest.getContext().getTargetProduct());
        Logger.getLogger(SellerView.class.getName()).warning(currentRequest.getCurrentMFICondition().toString());
        currentRequest = currentRequest.clone();

        //System.out.println(filteredByRequest);
        //System.out.println(currentRequest);
        if (isRanking == true) {
            Logger.getLogger(SellerView.class.getName()).warning("Ranking in progress");
            doRankingAgain = true;
            return false;
        }

        if ((filteredByRequest == null || !currentRequest.toString().equals(filteredByRequest.toString())) && isRanking == false) {
            Logger.getLogger(SellerView.class.getName()).warning("Start Ranking");
            isRanking = true;

            Logger.getLogger(SellerView.class.getName()).warning(currentRequest.toString());
            if (filteredByRequest != null) {
                Logger.getLogger(SellerView.class.getName()).warning(filteredByRequest.toString());
            }
            filteredByRequest = currentRequest.clone();

            //filter all requests by context
            List<RequestEntity> filteredRequests = new ArrayList<>();

            if (requests.isEmpty()) {
                isRanking = false;
                return true;
            }

            for (RequestEntity r : requests) {
                boolean market = true;
                boolean product = true;
                if (r.getContext() != null && r.getContext().getTargetMarket() != null) {
                    market = false;
                    if (r.getContext().getTargetMarket().equals(filteredByRequest.getContext().getTargetMarket())) {
                        market = true;
                    }
                }
                if (r.getContext() != null && r.getContext().getTargetProduct() != null) {
                    product = false;
                    if (r.getContext().getTargetProduct().equals(filteredByRequest.getContext().getTargetProduct())) {
                        product = true;
                    }
                }
                if (market && product) {
                    filteredRequests.add(r);
                }

            }

            if (filteredRequests.isEmpty()) {
                Logger.getLogger(SellerView.class.getName()).warning("Context Empty");
                filteredRequests = requests;
            }

            Logger.getLogger(SellerView.class.getName()).log(Level.WARNING, "Do Ranking for: {0}", currentRequest);
            //do subgroups

            //List<Integer> filteredComponents = filteredByRequest.getComponentsNumber();
            //double[][] data = new double[filteredRequests.size()][filteredComponents.size()];
            //for (int i = 0; i < filteredRequests.size(); i++) {
            //    data = filteredRequests.get(i).addComponents(i, data, filteredComponents);
            //}
            //final GaussianMixtureModelEM gmmem = new GaussianMixtureModelEM(10, GaussianMixtureModelEM.CovarianceType.Diagonal);//, 1e-3, 1e-6, 100, 5, EnumSet.allOf(GaussianMixtureModelEM.UpdateOptions.class), EnumSet.allOf(GaussianMixtureModelEM.UpdateOptions.class));
            //final MixtureOfGaussians model = gmmem.estimate(data);
            //System.out.println("com.uos.matrix.view.SellerView.doRanking()*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            //System.out.println(MatrixUtils.toString(model.gaussians[0].getCovariance()));
            ///System.out.println();
            //System.out.println(MatrixUtils.toString(model.gaussians[1].getCovariance()));
            //System.out.println(model.gaussians.length);
            //TODO get all gaussians and print contains log probability?
            double[] allRanks = new double[recycled.size()];

            try {
                List<PropertyDescriptor> pds = Arrays.asList(Introspector.getBeanInfo(RecycledEntity.class).getPropertyDescriptors()).stream()
                        .filter(pd -> Objects.nonNull(pd.getReadMethod()))
                        .collect(Collectors.toList());

                for (PropertyDescriptor pd : pds) {
                    String[] skips = new String[]{"comments", "id", "class", "flammability", "materialCode", "smell", "vicats", "izodImpactStrs", "hardness", "charpyTensileStrs", "MFIs"};
                    if (Arrays.asList(skips).contains(pd.getName())) {
                        continue;
                    }
                    Object target = pd.getReadMethod().invoke(filteredByRequest);
                    if (target != null) {
                        if (checkIfNullRankableValue(target)) {
                            continue;
                        }
                        //todo maybe eher alle palstics statt die subgroup nehmen? Weil je context könnte so eine null verteilung zu "wichtig" sein?
                        //TODO check if  recycled or filtered group of requests does make more sense

                        Stream<Object> contextStream = filteredRequests.stream().map((RequestEntity r) -> {
                            try {
                                return pd.getReadMethod().invoke(r);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                Logger.getLogger(SellerView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }).filter(Objects::nonNull);

                        //TODO string compare einbauen!?!
                        Double[] xValues = null;
                        double nonNullCounter = 0;
                        if (pd.getPropertyType() == RankableDoubleEntry.class) {
                            xValues = contextStream.map(r -> {
                                return ((RankableDoubleEntry) r).getValue();
                            }).filter(Objects::nonNull).toArray(Double[]::new);
                        } else if (pd.getPropertyType() == RankableIntegerEntry.class) {
                            xValues = contextStream.map(r -> {
                                Integer v = ((RankableIntegerEntry) r).getValue();
                                if (v == null) {
                                    return null;
                                } else {
                                    return v.doubleValue();
                                }
                            }).filter(Objects::nonNull).toArray(Double[]::new);
                        } else if (pd.getPropertyType() == RankableRangedEntry.class) {
                            xValues = contextStream.map(r -> {
                                RankableRangedEntry p = (RankableRangedEntry) r;
                                if(p.getValueMin() != null && p.getValueMax() != null){
                                    return (p.getValueMin() + p.getValueMax()) /2;
                                }
                                return p.getValueMin();
                            }).filter(Objects::nonNull).toArray(Double[]::new);
                        } else {
                            nonNullCounter = contextStream.filter(Objects::nonNull).count();
                            //Logger.getLogger(SellerView.class.getName()).log(Level.WARNING, "This explaints a lot {0}", pd.getPropertyType());
                        }

                        //TODO: Error bei nur min mfi recycled (hat irgendwie error values)
                        //TODO: Null counter geht irgendwie nicht richtig?????
                        ITransformer qt = new StandardizeTransformer();
                        double nullError;

                        if (xValues == null || xValues.length == 0) {
                            //Logger.getLogger(SellerView.class.getName()).warning("Empty xValues warn");
                            nullError = 1 - ((filteredRequests.size() - nonNullCounter) / filteredRequests.size());
                            //continue;
                        } else {
                            if (rankingMode == null) {
                                Logger.getLogger(SellerView.class.getName()).warning("Ranking mode null. Using default normalizer");
                            } else {
                                switch (rankingMode) {
                                    case MinMax:
                                        qt = new NormalizerTransformer();
                                        break;
                                    case Quantalizer:
                                        qt = new QuantileTransformer(xValues.length, xValues.length);
                                        break;
                                    case Standardizer:
                                        qt = new StandardizeTransformer();
                                        break;
                                }
                            }

                            qt.fit(xValues);

                            Logger.getLogger(SellerView.class.getName()).warning(xValues.length + " xValues warn -----");
                            Logger.getLogger(SellerView.class.getName()).warning(filteredRequests.size() + " xValues warn");
                            Logger.getLogger(SellerView.class.getName()).warning((filteredRequests.size() - xValues.length) + " xValues warn");
                            Logger.getLogger(SellerView.class.getName()).warning((((double) filteredRequests.size() - xValues.length) / filteredRequests.size()) + " xValues warn");

                            nullError = 1 - (((double) filteredRequests.size() - xValues.length) / filteredRequests.size());

                        }
                        final ITransformer gtf = qt;

                        //ArrayMath.standardize(xValues);
                        //Double[] x = filteredRequests.stream().map(r -> r.getDensity().getValue()).toArray(Double[]::new);
                        //
                        //qt.fit(x);
                        //Double[] x_transformed = qt.transform(x, Double[].class, true);
                        Double[] newRanks = recycled.stream().map((RecycledEntity r) -> {
                            try {
                                return pd.getReadMethod().invoke(r);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                Logger.getLogger(SellerView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            return null;
                        }).filter(Objects::nonNull).map(r -> {
                            if (pd.getPropertyType() == String.class) {
                                return stringRanking((String) target, (String) r, nullError);
                            } else if (pd.getPropertyType() == Material.class || pd.getPropertyType() == ProcessTech.class || pd.getPropertyType() == ParticleGeo.class || pd.getPropertyType() == PolymerModification.class) {
                                return stringRanking(target.toString(), r.toString(), nullError);
                            } else if (pd.getPropertyType() == RankableDoubleEntry.class) {
                                return numericRanking((RankableDoubleEntry) target, (RankableDoubleEntry) r, nullError, gtf);
                            } else if (pd.getPropertyType() == RankableIntegerEntry.class) {
                                return numericRanking((RankableIntegerEntry) target, (RankableIntegerEntry) r, nullError, gtf);
                            } else if (pd.getPropertyType() == RankableRangedEntry.class) {
                                return rangedRanking((RankableRangedEntry) target, (RankableRangedEntry) r, gtf, nullError);
                            } else {
                                return null;
                            }
                        }).toArray(Double[]::new);

                        for (int i = 0; i < newRanks.length; i++) {
                            if (newRanks[i] != null) {
                                allRanks[i] += newRanks[i];
                            }
                        }
                        if (qt != null) {
                            qt.shutdown();
                        }
                    }
                }

                List<RankedRecycled> rankedRecycleds = new ArrayList<>();
                for (int i = 0; i < recycled.size(); i++) {
                    RecycledEntity r = recycled.get(i);

                    rankedRecycleds.add(new RankedRecycled(allRanks[i], r));
                }
                Logger.getLogger(SellerView.class.getName()).warning(Arrays.toString(allRanks));
                recycledRanking = rankedRecycleds;
                Collections.sort(recycledRanking, new RankingComperator());

                //PrimeFaces.current().ajax().update("foo:bar");
            } catch (IntrospectionException ex) {
                Logger.getLogger(SellerView.class.getName()).log(Level.SEVERE, null, ex);
                isRanking = false;
                return false;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(SellerView.class.getName()).log(Level.SEVERE, null, ex);
            }

            PrimeFaces.current().ajax().update("rankingView:rankingRecycleds");

            Logger.getLogger(SellerView.class.getName()).warning("Normal Finish for Ranking");

            isRanking = false;
            if (doRankingAgain) {
                doRankingAgain = false;
                return doRanking();
            }

            return true;
        }
        isRanking = false;
        if (doRankingAgain) {
            doRankingAgain = false;
            return doRanking();
        }
        return false;
    }

    private double stringRanking(String query, String recycled, double nullError) {
        if (query == null || query.isBlank()) {
            return 0;
        }
        if (recycled == null) {
            return nullRanking(nullError);
        }
        if (recycled.contains(query)) {
            return 0;
        }
        return 0.2;
    }

    private double numericRanking(RankableDoubleEntry query, RankableDoubleEntry recycled, double nullError, ITransformer qt) {
        if (query.getValue() == null) {
            return 0;
        }
        if (recycled == null || recycled.getValue() == null) {
            return nullRanking(nullError);
        }
        Double tQuery = qt.transform(query.getValue(), Double.class, true);
        Double tRecycled = qt.transform(recycled.getValue(), Double.class, true);

        switch (query.getRanking()) {
            case CENTERED:
                return Math.pow(tQuery - tRecycled, 2);
            case LTE:
                if (tRecycled <= tQuery) {
                    return 0;
                }
                return Math.pow(tQuery - tRecycled, 2);
            case GTE:
                if (tRecycled >= tQuery) {
                    return 0;
                }
                return Math.pow(tQuery - tRecycled, 2);
        }
        return 1;
    }

    private double numericRanking(RankableIntegerEntry query, RankableIntegerEntry recycled, double nullError, ITransformer qt) {
        if (query.getValue() == null) {
            return 0;
        }
        if (recycled == null || recycled.getValue() == null) {
            return nullRanking(nullError);
        }
        Double tQuery = qt.transform(query.getValue(), Double.class, true);
        Double tRecycled = qt.transform(recycled.getValue(), Double.class, true);
        switch (query.getRanking()) {
            case CENTERED:
                return Math.pow(tQuery - tRecycled, 2);
            case LTE:
                if (tRecycled <= tQuery) {
                    return 0;
                }
                return Math.pow(tQuery - tRecycled, 2);
            case GTE:
                if (tRecycled >= tQuery) {
                    return 0;
                }
                return Math.pow(tQuery - tRecycled, 2);
        }
        return 1;
    }

    private double rangedRanking(RankableRangedEntry query, RankableRangedEntry recycled, ITransformer qt, double nullError) {
        if (query == null || query.getValueMin() == null || query.getValueMax() == null) {
            return 0;
        }
        if (checkRanges(recycled, query)) {
            return 0;
        }
        if (recycled == null || recycled.getValueMin() == null) {
            //return nullRanking(nullError);
            return nullRanking(1);
        }

        Double queryMin = qt.transform(query.getValueMin(), Double.class, true);
        Double queryMax = qt.transform(query.getValueMax(), Double.class, true);
        Double recycledMin = qt.transform(recycled.getValueMin(), Double.class, true);
        Double recycledMax = qt.transform(recycled.getValueMax(), Double.class, true);

        if (queryMax != null) {
            if (recycledMax == null) {
                if (recycledMin >= queryMin && recycledMin <= queryMax) {
                    return 0;
                } else {
                    double toMin = Math.abs(recycledMin - queryMin);
                    double toMax = Math.abs(recycledMin - queryMax);
                    if (toMax >= toMin) {
                        return Math.pow(toMin, 2);
                    } else {
                        return Math.pow(toMax, 2);
                    }
                }
            }

            if (queryMin <= recycledMin && queryMax >= recycledMax) {
                return 0;
            } else if (queryMin <= recycledMin && queryMax >= recycledMin) {
                //TODO maybe 0?
                return 0;
                //return Math.pow(Math.abs(queryMax) - Math.abs(recycledMax), 2);
            } else if (queryMin <= recycledMax && queryMax >= recycledMax) {
                //TODO maybe 0?
                return 0;
                //return Math.pow(Math.abs(queryMin) - Math.abs(recycledMin), 2);
            } else if (queryMin >= recycledMin && queryMax <= recycledMax) {
                //TODO maybe 0?
                return 0;
                //return Math.pow(Math.abs(queryMin) - Math.abs(recycledMin) + Math.abs(queryMax) - Math.abs(recycledMax), 2);
            } else if (queryMin >= recycledMax) {
                //falls rest 0 das *2 raus?
                return Math.pow(Math.abs(queryMin - recycledMax), 2);
            } else {
                return Math.pow(Math.abs(queryMax - recycledMin), 2);
            }
        } else if (queryMin >= recycledMin && queryMin <= recycledMax) {
            return 0;
        } else {
            double toMin = Math.abs(queryMin - recycledMin);
            double toMax = Math.abs(queryMin - recycledMax);
            if (toMax >= toMin) {
                return Math.pow(toMin, 2);
            } else {
                return Math.pow(toMax, 2);
            }
        }
    }

    private double nullRanking(double nullError) {
        //TODO better handle null????? subgroup dependent???
        return Math.pow(nullError, 2);
    }

    private boolean checkIfNullRankableValue(Object target) {
        if (target instanceof RankableDoubleEntry) {
            return ((RankableDoubleEntry) target).getValue() == null;
        }
        if (target instanceof RankableIntegerEntry) {
            return ((RankableIntegerEntry) target).getValue() == null;
        }
        if (target instanceof RankableRangedEntry) {
            return ((RankableRangedEntry) target).getValueMin() == null;
        }
        return false;
    }

    public void getContact(UserEntity u, RecycledEntity r) {
        HttpSession httpSession = SessionUtils.getSession();
        loginService = (LoginService) httpSession.getAttribute("loginService");
        Logger.getLogger(SellerView.class.getName()).log(Level.INFO, "Get recycled contect. User: {0}. Recycled: {1}", new Object[]{selectedRecycled, r});

        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ip = httpServletRequest.getRemoteAddr();
        Logger.getLogger(SellerView.class.getName()).log(Level.INFO, "User {0} accepted to get contact for {1} from ip {2}", new Object[]{r, selectedRecycled, ip});

        if ((lastRequestMail + timeOutTimeSeconds - (System.currentTimeMillis() / 1000)) > 0) {
            FacesMessage message = new FacesMessage("Error", "To many contact requests! Try again in a few seconds.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        lastRequestMail = System.currentTimeMillis() / 1000;

        filteredByRequest.setBuyerName(u.getUsername());
        filteredByRequest.setAutoRequest(true);
        if (contactTargetIndustry != null) {
            filteredByRequest.getContext().setTargetMarket(contactTargetIndustry);
        }

        if (contactTargetProduct != null) {
            filteredByRequest.getContext().setTargetProduct(contactTargetProduct);
        }

        if (contactSpecificApplicationText != null && !contactSpecificApplicationText.isBlank()) {
            filteredByRequest.getContext().setSpecificApplication(contactSpecificApplicationText);
        }

        requestService.createInstance(filteredByRequest);
        //TODO vorher auf dopplungen achten?

        RequestedCombi combi = new RequestedCombi(u, filteredByRequest, r);
        requestCombiService.createInstance(combi);

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

    public void requestMaterialVerification(UserEntity u, RecycledEntity r) {
        ValidationRequestEntity v = new ValidationRequestEntity();
        v.setRecycled(r);
        v.setRequestFrom(u);
        //v.setBasedOn(filteredByRequest);
        boolean existing = verificationService.createInstance(v);
        if (!existing) {
            FacesMessage message = new FacesMessage("Error", "Request already exists!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        boolean success = mailService.requestVerification(u, selectedRecycled);
        if (success) {
            FacesMessage message = new FacesMessage("Successful", "Request is sent.");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } else {
            FacesMessage message = new FacesMessage("Error", "Could not send request.");
            FacesContext.getCurrentInstance().addMessage(null, message);
            Logger.getLogger(ValidationRequestEntity.class.getName()).warning("Error on verification mail send");

        }
    }

    public boolean checkForEnableContextSubmit() {
        return contactTargetIndustry == null & contactTargetProduct == null;
    }

    public List<Boolean> getActiveColumns1() {
        return activeColumns1;
    }

    public List<Boolean> getActiveColumns2() {
        return activeColumns2;
    }

    public void onToggle(ToggleEvent e) {
        activeColumns1.set(((Integer) e.getData()) - 2, e.getVisibility() == Visibility.VISIBLE);
        activeColumns2.set(((Integer) e.getData()) - 2, e.getVisibility() == Visibility.VISIBLE);
        Logger.getLogger(SellerView.class.getName()).warning(Arrays.toString(activeColumns1.toArray()));
        Logger.getLogger(SellerView.class.getName()).warning(Arrays.toString(activeColumns2.toArray()));
    }

    //                            <!--   <p:ajax event="slideEnd" onstart="$(PrimeFaces.escapeClientId('#{p:component('filter')}'))[0].value = $(PrimeFaces.escapeClientId('#{p:component('mfiSlider')}_input'))[0].minValue + '-' + $(PrimeFaces.escapeClientId('#{p:component('mfiSlider')}_input'))[0].maxValue" oncomplete="PF('dataTable').filter()" /> -->
    public List<RecycledEntity> getRecycled() {
        return recycled;
    }

    public List<RecycledEntity> getFilteredRecycled() {
        return filteredRecycled;
    }

    public void setFilteredRecycled(List<RecycledEntity> filteredRecycled) {
        this.filteredRecycled = filteredRecycled;
    }

    public List<RankedRecycled> getRecycledRanking() {
        return recycledRanking;
    }

    public List<InventoryStatus> getInventoryStatus() {
        return Arrays.asList(InventoryStatus.values());
    }

    public void setSellerService(SellerService service) {
        this.sellerService = service;
    }

    public void setRequestService(RequestService service) {
        this.requestService = service;
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

    public boolean isShowOnlyOwned() {
        return showOnlyOwned;
    }

    public void setShowOnlyOwned(boolean showOnlyOwned) {
        this.showOnlyOwned = showOnlyOwned;
    }

    public boolean isShowLoading() {
        return showLoading;
    }

    public void setShowLoading(boolean showLoading) {
        this.showLoading = showLoading;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public RecycledEntity getSelectedRecycled() {
        return selectedRecycled;
    }

    public void setSelectedRecycled(RecycledEntity selectedRecycled) {
        PrimeFaces.current().ajax().update("providerView:editDialog");
        this.selectedRecycled = selectedRecycled;
    }

    public RankedRecycled getSelectedRankingRecycled() {
        return selectedRankingRecycled;
    }

    public void setSelectedRankingRecycled(RankedRecycled selectedRankingRecycled) {
        this.selectedRankingRecycled = selectedRankingRecycled;
    }

    public void newSelectedRecycled(String user) {
        selectedRecycled = new RecycledEntity();
        showUnknownPropertiesNew = showUnknownProperties;
        showUnknownProperties = true;
        selectedRecycled.setCreatedBy(user);
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

    public void uploadFile(UploadedFile f) {
        try {
            boolean success = sellerService.uploadFile(f.getInputStream());
            if (success) {
                FacesMessage message = new FacesMessage("Successful", f.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
                recycled = sellerService.getProducts();
                requests = requestService.getRequests();
                currentRequest = new RequestEntity();
                reloadData(user);

            } else {
                FacesMessage message = new FacesMessage("Fail", f.getFileName() + " could not be uplaoded or parsed");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(SellerView.class.getName()).log(Level.SEVERE, null, ex);
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

    public void initReset() {
        Logger.getLogger(ValidationRequestEntity.class.getName()).warning("Reseting Init!!! ");
        resetDataTableForm();
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('recycledTable').clearFilters();");
    }

    public void resetForm() {
        if (showUnknownPropertiesNew != null) {
            showUnknownProperties = showUnknownPropertiesNew.booleanValue();
            showUnknownPropertiesNew = null;
        }
        PrimeFaces.current().resetInputs("editForm:recycledDetails");
    }

    public void resetForm(String form) {
        Logger.getLogger(ValidationRequestEntity.class.getName()).warning("Reseting: " + form);

        PrimeFaces.current().resetInputs(form);
    }

    public void resetDataTableForm() {
        newSelectedRecycled(user.getUsername());
        if (showUnknownPropertiesNew != null) {
            showUnknownProperties = showUnknownPropertiesNew.booleanValue();
            showUnknownPropertiesNew = null;
        }
        currentRequest = new RequestEntity();
        reloadSideData();
        PrimeFaces.current().resetInputs("providerView");
        PrimeFaces.current().ajax().update("providerView");

    }

    public List<Material> getDistinctMaterials() {
        return distinctMaterials;
    }

    public List<ProcessTech> getDistinctProcessTechs() {
        return distinctProcessTechs;
    }

    public List<Color> getDistinctColors() {
        return distinctColors;
    }

    public List<TargetMarket> getDistinctTargetMarkets() {
        return distinctTargetMarkets;
    }

    public List<TargetProduct> getDistinctTargetProducts() {
        return distinctTargetProducts;
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

    public double getMFIMin() {
        return mFIMin;
    }

    public double getMFIMax() {
        return mFIMax;
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

    public double getAdensityMax() {
        return adensityMax;
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

    public TargetMarket getContactTargetIndustry() {
        return contactTargetIndustry;
    }

    public void setContactTargetIndustry(TargetMarket contactTargetIndustry) {
        this.contactTargetIndustry = contactTargetIndustry;
    }

    public TargetProduct getContactTargetProduct() {
        return contactTargetProduct;
    }

    public void setContactTargetProduct(TargetProduct contactTargetProduct) {
        this.contactTargetProduct = contactTargetProduct;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setVerificationService(VerificationRequestService verificationService) {
        this.verificationService = verificationService;
    }

    public void setRequestCombiService(RequestCombiService requestCombiService) {
        this.requestCombiService = requestCombiService;
    }

    public RankingModes getRankingMode() {
        return rankingMode;
    }

    public void setRankingMode(RankingModes rankingMode) {
        filteredByRequest = null;
        this.rankingMode = rankingMode;
    }

    public List<RankingModes> getAllRankingModes() {
        return Arrays.asList(RankingModes.values());
    }

    public UserEntity getUser() {
        return user;
    }

    public void setItemValueService(ItemValueService itemValueService) {
        this.itemValueService = itemValueService;
    }

    public String getContactSpecificApplicationText() {
        return contactSpecificApplicationText;
    }

    public void setContactSpecificApplicationText(String contactSpecificApplicationText) {
        this.contactSpecificApplicationText = contactSpecificApplicationText;
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

}
