package com.uos.matrix.view;

import com.uos.matrix.helper.other.SessionUtils;
import com.uos.matrix.services.LoginService;
import com.uos.matrix.services.MailService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
@ViewScoped
public class FeedbackView implements Serializable{
   
    private String input;
    
   @ManagedProperty(value = "#{mailService}")
    private MailService mailService;
    
    private LoginService loginService;
    
    private long lastEmailSent = 0;
    private final long timeOut = 60;
    
    @PostConstruct
    public void init() {
        HttpSession httpSession = SessionUtils.getSession();
        loginService = (LoginService) httpSession.getAttribute("loginService");
        input = "";
    }
    
    public void setInput(String input) {
        this.input = input;
    }
    
    public String getInput() {
        return input;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

   public void setMailService(MailService mailService) {
        this.mailService = mailService;
   }
   
    public void sendEmail() {
        if (!input.isBlank() || !input.isEmpty() || !input.equals("")) {
            if ((lastEmailSent + timeOut - (System.currentTimeMillis() / 1000)) < 0) {
                FacesMessage message = new FacesMessage("Successful", "Feedback was sent!");
                FacesContext.getCurrentInstance().addMessage(null, message);
                input += "\n\nThis message was sent by User: " + loginService.getCurrentUser().getUsername() + " E-Mail: " + loginService.getCurrentUser().getEmail();
                lastEmailSent = System.currentTimeMillis() / 1000;
                mailService.sendFeedback(input);
                input = "";
            } else {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "You have to wait " + (lastEmailSent + timeOut - (System.currentTimeMillis() / 1000)) + " seconds before you can send feedback again!");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        } else {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Fail", "Input cannot be empty!");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
        
    }
    
}
