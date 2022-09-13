/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;


import com.uos.matrix.entities.RecycledEntity;
import com.uos.matrix.entities.RequestedCombi;
import com.uos.matrix.helper.other.HibernateUtil;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * 
 */
@ManagedBean(name = "requestCombiService")
@ApplicationScoped
public class RequestCombiService implements Serializable{

    @PostConstruct
    public void init() {

    }

    
    public boolean createInstance(RequestedCombi r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        try (Session session = HibernateUtil.getHibernateSession()) {
            Transaction t = session.beginTransaction();
            session.save(r);
            t.commit();
            session.close();
            Logger.getLogger(RequestedCombi.class.getName()).warning("done saving");
        } catch (Exception e) {
            Logger.getLogger(RequestedCombi.class.getName()).warning("##################4");
            Logger.getLogger(RequestedCombi.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
            return false;
        }
        return true;
    }

    
    public boolean updateInstance(RequestedCombi r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        try (Session session = HibernateUtil.getHibernateSession()) {
            Transaction t = session.beginTransaction();
            session.saveOrUpdate(r);
            t.commit();
            session.close();
            Logger.getLogger(RequestedCombi.class.getName()).warning("done saving");
        } catch (Exception e) {
            Logger.getLogger(RequestedCombi.class.getName()).warning("##################4");
            Logger.getLogger(RequestedCombi.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
            return false;
        }
        return true;
    }

    
    public void deleteInstance(RequestedCombi r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");

        try (Session session = HibernateUtil.getHibernateSession()) {
            Transaction t = session.beginTransaction();
            session.delete(r);
            t.commit();
            session.close();
            Logger.getLogger(RequestedCombi.class.getName()).warning("done deleting");
        } catch (Exception e) {
            Logger.getLogger(RequestedCombi.class.getName()).warning("##################4");
            Logger.getLogger(RequestedCombi.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }

    }
}
