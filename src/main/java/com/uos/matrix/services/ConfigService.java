/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.uos.matrix.services;

import com.uos.matrix.entities.ConfigEntity;
import com.uos.matrix.helper.other.HibernateUtil;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.hibernate.Session;
import org.hibernate.Transaction;

@ManagedBean(name = "configService")
@ApplicationScoped
public class ConfigService implements Serializable {

    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void init() { 
        
    }
    
    public boolean updateConfig(ConfigEntity config, ConfigEntity currentConfig) {
        if (currentConfig == null) {
            Logger.getLogger(RequestService.class.getName()).warning("##################CURRENT CONFIG IS NULL");
            return false;
        }
        if (config == null) {
            Logger.getLogger(RequestService.class.getName()).warning("##################GIVEN CONFIG IS NULL");
            return false;
        }
        if (currentConfig.getId() != config.getId()) {
            Logger.getLogger(RequestService.class.getName()).warning("##################No permission");
            return false;
        }
        try (Session session = HibernateUtil.getHibernateSession()) {

            // start a transaction
            Transaction transaction = session.beginTransaction();

            session.saveOrUpdate(config);

            transaction.commit();
            session.close();

        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################CONFIG");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            return false;
        }
        Logger.getLogger(RequestService.class.getName()).warning("##################CONFIG update success");
        return true;
    }
    
}
