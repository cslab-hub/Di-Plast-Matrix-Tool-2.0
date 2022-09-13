/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;

import com.uos.matrix.entities.RecycledEntity;
import com.uos.matrix.helper.other.SessionUtils;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "globalSettingsService")
@ApplicationScoped
public class GlobalSettingsService implements Serializable {

    private boolean privateDataOnly = false;

    private static final long serialVersionUID = 1L;

    public boolean isPrivateDataOnly() {
        return privateDataOnly;
    }

    public void setPrivateDataOnly(boolean privateDataOnly) {
        Logger.getLogger(GlobalSettingsService.class.getName()).warning("Start " + privateDataOnly);
        if(checkPermission()){
            Logger.getLogger(GlobalSettingsService.class.getName()).warning("Done " + privateDataOnly);
            this.privateDataOnly = privateDataOnly;
        }
    }    

    private boolean checkPermission() {
        HttpSession session = SessionUtils.getSession();
        return (Boolean.parseBoolean(session.getAttribute("isAdmin").toString()));
    }
}
