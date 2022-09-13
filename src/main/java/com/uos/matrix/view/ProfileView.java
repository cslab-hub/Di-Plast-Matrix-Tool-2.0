/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.view;

import com.uos.matrix.entities.ConfigEntity;
import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.other.SessionUtils;
import com.uos.matrix.services.ConfigService;
import com.uos.matrix.services.LoginService;
import com.uos.matrix.services.MailService;
import com.uos.matrix.services.RequestService;
import com.uos.matrix.services.UserService;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.uos.matrix.services.local.PropertiesSideInfos;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class ProfileView implements Serializable {

    private String pwConfirm;
    private String userName;
    private String fullname;
    private String organisation;
    private String password;
    private String passwordV;
    private String email;

    private boolean disclaimer = false;
    private boolean dataPrivacy = false;

    private ConfigEntity config;

    private Units eModulusChoice;
    private Units tensileStrenghtAtYieldChoice;
    private Units densityChoice;
    private Units apparentDensityChoice;
    private Units hDTChoice;
    private Units vicatChoice;
    private Units charpyImpactStrengthChoice;
    private Units flexuralModulusChoice;
    private Units izodImpactStrengthChoice;

    private List<Units> eModulus;
    private List<Units> tensileStrenghtAtYield;
    private List<Units> density;
    private List<Units> apparentDensity;
    private List<Units> hDT;
    private List<Units> vicat;
    private List<Units> charpyImpactStrength;
    private List<Units> flexuralModulus;
    private List<Units> izodImpactStrength;

    private boolean pwChanged = false;
    private boolean acceptRest = false;

    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    @ManagedProperty(value = "#{configService}")
    private ConfigService configService;

    private PropertiesSideInfos propertiesSideInfos;
    private LoginService loginService;

    @PostConstruct
    public void init() {
        HttpSession httpSession = SessionUtils.getSession();
        loginService = (LoginService) httpSession.getAttribute("loginService");
        config = loginService.getCurrentUser().getConfig();
        if (loginService.getCurrentUser().isResetPW()) {
            Logger.getLogger(RequestService.class
                    .getName()).info("#forceRest PW");
            acceptRest = false;
        }
        propertiesSideInfos = new PropertiesSideInfos();
        eModulus = propertiesSideInfos.getModulusUnits();
        tensileStrenghtAtYield = propertiesSideInfos.getModulusUnits();
        flexuralModulus = propertiesSideInfos.getModulusUnits();
        density = propertiesSideInfos.getDensityUnits();
        apparentDensity = propertiesSideInfos.getDensityUnits();
        hDT = propertiesSideInfos.getTemperatureUnits();
        vicat = propertiesSideInfos.getTemperatureUnits();
        charpyImpactStrength = propertiesSideInfos.getImpactSquareUnits();
        izodImpactStrength = propertiesSideInfos.getImpactNoneSquareUnits();
        refreshData();

        if (!loginService.getCurrentUser().isConfirmedAgreement()) {
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('agreement').show();");
        }

    }

    public void refreshData() {
        userName = loginService.getCurrentUser().getUsername();
        email = loginService.getCurrentUser().getEmail();
        eModulusChoice = config.getEModulusPrefferedUnit();
        tensileStrenghtAtYieldChoice = config.getTensileStrenghtAtYieldPrefferedUnit();
        densityChoice = config.getDensityPrefferedUnit();
        apparentDensityChoice = config.getBulkDensityPrefferedUnit();
        hDTChoice = config.getHDTPrefferedUnit();
        vicatChoice = config.getVicatPrefferedUnit();
        charpyImpactStrengthChoice = config.getCharpyImpactStrengthPrefferedUnit();
        flexuralModulusChoice = config.getFlexuralModulusPrefferdUnit();
        izodImpactStrengthChoice = config.getIzodImpactStrengthPrefferedUnit();
    }

    public void setLoginService(LoginService service) {
        this.loginService = service;
    }

    public void acceptReset() {
        acceptRest = true;
    }

    public void saveUser() {
        Logger.getLogger(RequestService.class
                .getName()).info("safe");

        if (!loginService.validate(loginService.getCurrentUser().getUsername(), pwConfirm)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Please confirm your current password");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (password != null && !password.isBlank()) {
            if (password.equals(passwordV)) {
                loginService.getCurrentUser().setPassword(password);
                pwChanged = true;
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Password missmatch!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }
        }

        if (userName == null || userName.isBlank()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Username must be filled out!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        } else if (email == null || email.isBlank()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Email musst be filled out!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        if (!loginService.getCurrentUser().isConfirmedAgreement()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Please confirm the data policy and user agreement!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }

        loginService.getCurrentUser().setEmail(email);
        loginService.getCurrentUser().setUsername(userName);
        boolean success = userService.updateUser(loginService.getCurrentUser(), loginService.getCurrentUser());
        if (success) {
            FacesMessage message = new FacesMessage("Successful", "Profile is updated");
            FacesContext.getCurrentInstance().addMessage(null, message);
            if (pwChanged && loginService.getCurrentUser().isResetPW()) {
                loginService.getCurrentUser().setResetPW(false);
                userService.updateUser(loginService.getCurrentUser(), loginService.getCurrentUser());
                //boolean resetDone = loginService.updateUser(loginService.getCurrentUser());
                //Logger.getLogger(RequestService.class
                //        .getName()).warning("##################reset done: " + resetDone + " " + loginService.getCurrentUser().isResetPW());
            }

            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", loginService.getCurrentUser().getUsername());
            session.setAttribute("resetPW", loginService.getCurrentUser().isResetPW());

            if (!loginService.getCurrentUser().getEmail().equals(loginService.getCurrentUser().getConfirmedEmail())) {
                //mailService.sendAccessCode(loginService.getCurrentUser(), loginService.getCurrentUser());
                mailService.mailChanged(loginService.getCurrentUser(), loginService.getCurrentUser());
                //loginService.logout();
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Update Fail");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        pwChanged = false;

        refreshData();
    }

    public void changePrefferedUnits() {

        config.setEModulusPrefferedUnit(eModulusChoice);
        config.setTensileStrenghtAtYieldPrefferedUnit(tensileStrenghtAtYieldChoice);
        config.setDensityPrefferedUnit(densityChoice);
        config.setBulkDensityPrefferedUnit(apparentDensityChoice);
        config.setHDTPrefferedUnit(hDTChoice);
        config.setVicatPrefferedUnit(vicatChoice);
        config.setCharpyImpactStrengthPrefferedUnit(charpyImpactStrengthChoice);
        config.setFlexuralModulusPrefferdUnit(flexuralModulusChoice);
        config.setIzodImpactStrengthPrefferedUnit(izodImpactStrengthChoice);

        boolean success = configService.updateConfig(config, config);
        if (success) {
            FacesMessage message = new FacesMessage("Successful", "Units were successfully updated!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Update failed!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

        refreshData();
    }

    public void acceptAgreement() {
        loginService.getCurrentUser().setConfirmedAgreement(true);
        Date curr_date = new Date(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeFormat = df.format(curr_date);

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String msg = "The following user confirmed the disclaimer and data policy at " + timeFormat + " from the IP " + ipAddress + " with the user information: " + loginService.getCurrentUser().toString();
        Logger.getLogger(ProfileView.class.getName()).warning(msg);
        loginService.getCurrentUser().addConfirmation(msg);
        boolean success = userService.updateUser(loginService.getCurrentUser(), loginService.getCurrentUser());

        if (success) {
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('agreement').hide();");
        } else {
            Logger.getLogger(ProfileView.class.getName()).warning("Saving user error!!!!!");
        }
    }

    public void logDisclaimerAgree() {
        if (!disclaimer) {
            Date curr_date = new Date(System.currentTimeMillis());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeFormat = df.format(curr_date);

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            String msg = "The following user set confirmation of the disclaimer  " + timeFormat + " from the IP " + ipAddress + " with the user information: " + loginService.getCurrentUser().toString();
            Logger.getLogger(ProfileView.class.getName()).warning(msg);
        }
    }

    public void logDataPolicAgree() {
        if (!dataPrivacy) {
            Date curr_date = new Date(System.currentTimeMillis());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timeFormat = df.format(curr_date);

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            String msg = "The following user set confirmation of the data policy  " + timeFormat + " from the IP " + ipAddress + " with the user information: " + loginService.getCurrentUser().toString();
            Logger.getLogger(ProfileView.class.getName()).warning(msg);
        }
    }

    public Units getEModulusChoice() {
        return eModulusChoice;
    }

    public Units getTensileStrenghtAtYieldChoice() {
        return tensileStrenghtAtYieldChoice;
    }

    public Units getDensityChoice() {
        return densityChoice;
    }

    public Units getApparentDensityChoice() {
        return apparentDensityChoice;
    }

    public Units getHDTChoice() {
        return hDTChoice;
    }

    public Units getVicatChoice() {
        return vicatChoice;
    }

    public void setEModulusChoice(Units eModulusChoice) {
        this.eModulusChoice = eModulusChoice;
    }

    public void setTensileStrenghtAtYieldChoice(Units tensileStrenghtAtYieldChoice) {
        this.tensileStrenghtAtYieldChoice = tensileStrenghtAtYieldChoice;
    }

    public void setDensityChoice(Units densityChoice) {
        this.densityChoice = densityChoice;
    }

    public void setApparentDensityChoice(Units apparentDensityChoice) {
        this.apparentDensityChoice = apparentDensityChoice;
    }

    public void setHDTChoice(Units hDTChoice) {
        this.hDTChoice = hDTChoice;
    }

    public void setVicatChoice(Units vicatChoice) {
        this.vicatChoice = vicatChoice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordV() {
        return passwordV;
    }

    public void setPasswordV(String passwordV) {
        this.passwordV = passwordV;
    }

    public boolean isAcceptRest() {
        return acceptRest;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public List<Units> getEModulus() {
        return eModulus;
    }

    public List<Units> getTensileStrenghtAtYield() {
        return tensileStrenghtAtYield;
    }

    public List<Units> getDensity() {
        return density;
    }

    public List<Units> getApparentDensity() {
        return apparentDensity;
    }

    public List<Units> getHDT() {
        return hDT;
    }

    public List<Units> getVicat() {
        return vicat;
    }

    public void setEModulus(List<Units> eModulus) {
        this.eModulus = eModulus;
    }

    public void setTensileStrenghtAtYield(List<Units> tensileStrenghtAtYield) {
        this.tensileStrenghtAtYield = tensileStrenghtAtYield;
    }

    public void setDensity(List<Units> density) {
        this.density = density;
    }

    public void setApparentDensity(List<Units> apparentDensity) {
        this.apparentDensity = apparentDensity;
    }

    public void setHDT(List<Units> hDT) {
        this.hDT = hDT;
    }

    public void setVicat(List<Units> vicat) {
        this.vicat = vicat;
    }

    public String getPwConfirm() {
        return pwConfirm;
    }

    public void setPwConfirm(String pwConfirm) {
        this.pwConfirm = pwConfirm;
    }

    public Units getFlexuralModulusChoice() {
        return flexuralModulusChoice;
    }

    public void setFlexuralModulusChoice(Units flexuralModulusChoice) {
        this.flexuralModulusChoice = flexuralModulusChoice;
    }

    public Units getIzodImpactStrengthChoice() {
        return izodImpactStrengthChoice;
    }

    public void setIzodImpactStrengthChoice(Units izodImpactStrengthChoice) {
        this.izodImpactStrengthChoice = izodImpactStrengthChoice;
    }

    public List<Units> getCharpyImpactStrength() {
        return charpyImpactStrength;
    }

    public void setCharpyImpactStrength(List<Units> charpyImpactStrength) {
        this.charpyImpactStrength = charpyImpactStrength;
    }

    public List<Units> getFlexuralModulus() {
        return flexuralModulus;
    }

    public void setFlexuralModulus(List<Units> flexuralModulus) {
        this.flexuralModulus = flexuralModulus;
    }

    public List<Units> getIzodImpactStrength() {
        return izodImpactStrength;
    }

    public void setIzodImpactStrength(List<Units> izodImpactStrength) {
        this.izodImpactStrength = izodImpactStrength;
    }

    public Units getCharpyImpactStrengthChoice() {
        return charpyImpactStrengthChoice;
    }

    public void setCharpyImpactStrengthChoice(Units charpyImpactStrengthChoice) {
        this.charpyImpactStrengthChoice = charpyImpactStrengthChoice;
    }

    public String getFullname() {
        return fullname;
    }

    public String getOrganisation() {
        return organisation;
    }

    public boolean isDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(boolean disclaimer) {
        this.disclaimer = disclaimer;
    }

    public boolean isDataPrivacy() {
        return dataPrivacy;
    }

    public void setDataPrivacy(boolean dataPrivacy) {
        this.dataPrivacy = dataPrivacy;
    }

}
