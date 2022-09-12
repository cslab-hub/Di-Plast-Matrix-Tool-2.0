/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.view;

import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.helper.other.SessionUtils;
import com.uos.matrix.services.GlobalSettingsService;
import com.uos.matrix.services.LoginService;
import com.uos.matrix.services.MailService;
import com.uos.matrix.services.UserService;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;

@ManagedBean
@ViewScoped
public class SettingsView implements Serializable {

    private UserEntity selectedUser;
    private boolean newUser = false;

    private String userName;
    private String fullname;
    private String organisation;
    private String password;
    private String passwordV;
    private String email;
    private boolean isAdmin = false;
    private boolean resetPW = true;

    private List<UserEntity> users;

    private LoginService loginService;

    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    @ManagedProperty(value = "#{globalSettingsService}")
    private GlobalSettingsService globalSettingsService;

    @PostConstruct
    public void init() {
        HttpSession httpSession = SessionUtils.getSession();
        loginService = (LoginService) httpSession.getAttribute("loginService");
        refreshUserList();
    }

    public void refreshUserList() {
        users = userService.getAllUsers(loginService.getCurrentUser());
        PrimeFaces.current().ajax().update("settingsView:advanced");
    }

    public void setLoginService(LoginService service) {
        this.loginService = service;
    }

    public UserEntity getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserEntity selectedUser) {
        //Logger.getLogger(RequestService.class.getName()).warning("##################SET " + selectedUser);
        newUser = false;
        PrimeFaces.current().resetInputs("settingsView:controlCard");

        if (selectedUser != null) {
            userName = selectedUser.getUsername();
            fullname = selectedUser.getFullname();
            organisation = selectedUser.getOrganisation();
            email = selectedUser.getEmail();
            resetPW = selectedUser.isResetPW();
            isAdmin = selectedUser.isAdminTag();
        }
        this.selectedUser = selectedUser;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void forceResetPW() {
        if (selectedUser != null) {
            String newForcePW = mailService.generateCommonLangPassword();
            selectedUser.setResetPW(true);
            selectedUser.setPassword(newForcePW);

            boolean success = userService.updateUser(selectedUser, loginService.getCurrentUser());
            if (success) {
                FacesMessage message = new FacesMessage("Successful", userName + " is updated");
                FacesContext.getCurrentInstance().addMessage(null, message);
                mailService.sendNewRandomPW(email, newForcePW);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", userName + " is not updated. No permission!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void resentConfirmationCode() {
        if (selectedUser != null && !selectedUser.isConfirmed()) {
            boolean success = mailService.sendAccessCode(selectedUser, loginService.getCurrentUser());
            if (success) {
                FacesMessage message = new FacesMessage("Successful", "Confirmation Code is sent");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", userName + " is not updated. No permission!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void createNewUser() {
        Logger.getLogger(SettingsView.class.getName()).info("CREATE user");
        PrimeFaces.current().resetInputs("settingsView:controlCard");
        newUser = true;
        this.selectedUser = new UserEntity();
        userName = "";
        password = "";
        email = "";
        fullname = "";
        organisation = "";
        isAdmin = false;
        resetPW = true;

    }

    public void saveUser() {
        Logger.getLogger(SettingsView.class
                .getName()).warning("##################safe");

        /*if (password != null) {
            if (password.equals(passwordV)) {
                selectedUser.setPassword(password);
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Password missmatch!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                return;
            }

        } else if (newUser) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Password must be filled out!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }*/
        if (selectedUser == null) {
            return;
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
        if (newUser) {
            newUser = false;
            saveNewUser();
        } else {

            selectedUser.setEmail(email);
            selectedUser.setUsername(userName);
            selectedUser.setFullname(fullname);
            selectedUser.setOrganisation(organisation);
            boolean success = userService.updateUser(selectedUser, loginService.getCurrentUser());
            if (success) {
                FacesMessage message = new FacesMessage("Successful", userName + " is updated");
                FacesContext.getCurrentInstance().addMessage(null, message);
                if (!selectedUser.getEmail().equals(selectedUser.getConfirmedEmail())) {
                    //mailService.sendAccessCode(selectedUser, loginService.getCurrentUser());
                    mailService.mailChanged(selectedUser, loginService.getCurrentUser());
                }
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", userName + " is not updated. No permission!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
        refreshUserList();
        selectedUser = null;
    }

    public void saveNewUser() {
        String generatedPW = mailService.generateCommonLangPassword();
        UserEntity u = new UserEntity(userName, generatedPW, isAdmin, email, fullname, organisation);
        u.setResetPW(resetPW);
        boolean success = userService.updateUser(u, loginService.getCurrentUser());

        if (success) {
            FacesMessage message = new FacesMessage("Successful", userName + " is created");
            FacesContext.getCurrentInstance().addMessage(null, message);
            mailService.sendNewRandomPW(email, generatedPW);
            mailService.sendAccessCode(u, loginService.getCurrentUser());
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", userName + " is not created. No permission or username already exists!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void deleteUser() {
        Logger.getLogger(SettingsView.class
                .getName()).info("delete " + selectedUser);

        newUser = false;
        boolean success = userService.deleteNewUser(selectedUser, loginService.getCurrentUser());
        if (success) {
            FacesMessage message = new FacesMessage("Successful", userName + " is deleted");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", userName + " is not deleted. No permission or username already exists!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        refreshUserList();
        selectedUser = null;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
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

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isResetPW() {
        return resetPW;
    }

    public void setResetPW(boolean resetPW) {
        this.resetPW = resetPW;
    }

    public String getPasswordV() {
        return passwordV;
    }

    public void setPasswordV(String passwordV) {
        this.passwordV = passwordV;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setGlobalSettingsService(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    public boolean getGlobalPrivateServerSetting() {
        return globalSettingsService.isPrivateDataOnly();
    }

    public void setGlobalPrivateServerSetting(boolean setting) {
        globalSettingsService.setPrivateDataOnly(setting);
    }

}
