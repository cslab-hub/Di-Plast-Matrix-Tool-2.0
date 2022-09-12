/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;

import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.helper.other.PasswordHashingUtils;
import com.uos.matrix.helper.other.SessionUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "loginService")
@SessionScoped
public class LoginService implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pwd = "";
    private String msg;
    private String username;
    private UserEntity currentUser;
    private String confirmationCode;
    private String resetMail;

    private int wrongAttempts = 0;
    private final int maxTries = 3;
    private long lastWrongLogin = 0;
    private final long timeOutTimeSeconds = 10;
    private final long timeOutForMail = 300;
    private long triedResetMail = 0;

    private boolean correctLogin = false;

    @ManagedProperty(value = "#{mailService}")
    private MailService mailService;

    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public UserEntity getCurrentUser() {
        return currentUser;
    }

    public int getWrongAttempts() {
        return wrongAttempts;
    }

    public boolean isWrongInput() {
        return correctLogin;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    public String getResetMail() {
        return resetMail;
    }

    public void setResetMail(String resetMail) {
        this.resetMail = resetMail;
    }

    @PostConstruct
    public void init() {
    }

    //validate login
    public String validateUsernamePassword() {
        Logger.getLogger(RequestService.class.getName()).warning("Login Attempt");
        if ((lastWrongLogin + timeOutTimeSeconds - (System.currentTimeMillis() / 1000)) < 0) {
            wrongAttempts = 0;
        }
        if (wrongAttempts < maxTries) {
            correctLogin = validate(username, pwd);
            if (correctLogin) {
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("username", username);
                session.setAttribute("isAdmin", currentUser.isAdminTag());
                session.setAttribute("resetPW", currentUser.isResetPW());
                session.setAttribute("needConfirm", !currentUser.isConfirmed());
                wrongAttempts = 0;
                if (currentUser.isConfirmed()) {
                    return "user";
                } else {
                    return "code";
                }
            } else {
                wrongAttempts++;
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Incorrect Username and Password",
                                "Please enter correct username and Password. Tries left: " + (maxTries - wrongAttempts)));

                lastWrongLogin = System.currentTimeMillis() / 1000;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "To many wrong logins",
                            "Please try again in " + (lastWrongLogin + timeOutTimeSeconds - (System.currentTimeMillis() / 1000)) + " seconds"));
        }

        return "login";
    }

    public String resetPWLink() {
        return "resetPW";
    }

    public String backHome() {
        try {
            if (currentUser != null) {

                FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");

                return "user";
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "backLogin";
    }

    public String returnFromResetLink() {
        return "backLogin";
    }

    public void resetPW() {
        Logger.getLogger(RequestService.class.getName()).info("RESET Mail" + resetMail);
        if ((triedResetMail + timeOutForMail - (System.currentTimeMillis() / 1000)) < 0) {
            triedResetMail = System.currentTimeMillis() / 1000;
            UserEntity resultUser = null;

            resultUser = userService.getUserByEmail(resetMail);

            if (resultUser != null) {
                String newPW = mailService.generateCommonLangPassword();
                resultUser.setResetedPW(newPW);
                resultUser.setResetPW(true);
                userService.updateUser(resultUser, resultUser);
                mailService.sendNewRandomPW(resetMail, newPW);
                System.out.println(newPW);
            }

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Success",
                            "New password was send to " + resetMail));
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "To many password resets",
                            "Please try again in " + (triedResetMail + timeOutForMail - (System.currentTimeMillis() / 1000)) + " seconds"));
        }
    }

    //logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        currentUser = null;
        session.invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");

        } catch (IOException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "login";
    }

    public boolean validate(String user, String password) {
        try {
            UserEntity resultUser = userService.getUserByName(user);

            if (resultUser == null) {
                resultUser = userService.getUserByEmail(user);
                if (resultUser == null) {
                    return false;
                }
            }

            if (PasswordHashingUtils.validatePassword(password, resultUser.getPassword())) {
                currentUser = resultUser;
                return true;
            } else if (resultUser.isResetPW() && PasswordHashingUtils.validatePassword(password, resultUser.getResetedPW())) {
                currentUser = resultUser;
                currentUser.setPassword(currentUser.getResetedPW());
                userService.updateUser(resultUser, currentUser);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################VALIDATE");
            Logger.getLogger(RequestService.class.getName()).warning(e.toString());
        }
        return false;
    }

    public String validateConfirmationCode() {
        Logger.getLogger(RequestService.class.getName()).warning("Code Attempt");
        if ((lastWrongLogin + timeOutTimeSeconds - (System.currentTimeMillis() / 1000)) < 0) {
            wrongAttempts = 0;
        }
        if (wrongAttempts < maxTries) {
            if (confirmationCode.equals(currentUser.getConfirmationCodePlaceholder())) {
                currentUser.setConfirmationCodePlaceholder(null);
                currentUser.setConfirmed(true);
                currentUser.setConfirmedEmail(currentUser.getEmail());
                userService.updateUser(currentUser, currentUser);

                HttpSession session = SessionUtils.getSession();
                session.setAttribute("username", username);
                session.setAttribute("isAdmin", currentUser.isAdminTag());
                session.setAttribute("resetPW", currentUser.isResetPW());
                session.setAttribute("needConfirm", !currentUser.isConfirmed());

                return "user";
            } else {
                wrongAttempts++;
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Incorrect Code",
                                "Please enter correct code. Tries left: " + (maxTries - wrongAttempts)));

                lastWrongLogin = System.currentTimeMillis() / 1000;
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "To many wrong treis",
                            "Please try again in " + (lastWrongLogin + timeOutTimeSeconds - (System.currentTimeMillis() / 1000)) + " seconds"));
        }

        return "code";
    }

    public String abortMailChange() {
        if (currentUser != null && currentUser.getConfirmedEmail() != null && !currentUser.getConfirmedEmail().isBlank()) {
            currentUser.setEmail(currentUser.getConfirmedEmail());
            currentUser.setConfirmationCodePlaceholder(null);
            currentUser.setConfirmed(true);
            userService.updateUser(currentUser, currentUser);

            HttpSession session = SessionUtils.getSession();
            session.setAttribute("username", username);
            session.setAttribute("isAdmin", currentUser.isAdminTag());
            session.setAttribute("resetPW", currentUser.isResetPW());
            session.setAttribute("needConfirm", !currentUser.isConfirmed());

            return "user";
        }
        return "code";
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
