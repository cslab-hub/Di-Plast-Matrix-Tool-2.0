/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;

import com.uos.matrix.entities.Color;
import com.uos.matrix.entities.Material;
import com.uos.matrix.entities.ParticleGeo;
import com.uos.matrix.entities.PolymerModification;
import com.uos.matrix.entities.ProcessTech;
import com.uos.matrix.entities.QColor;
import com.uos.matrix.entities.QMaterial;
import com.uos.matrix.entities.QParticleGeo;
import com.uos.matrix.entities.QPolymerModification;
import com.uos.matrix.entities.QProcessTech;
import com.uos.matrix.entities.QTargetMarket;
import com.uos.matrix.entities.QTargetProduct;
import com.uos.matrix.entities.TargetMarket;
import com.uos.matrix.entities.TargetProduct;
import com.uos.matrix.helper.other.HibernateUtil;
import com.uos.matrix.helper.other.SessionUtils;
import com.querydsl.jpa.impl.JPAQuery;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

@ManagedBean(name = "itemValueService")
@ApplicationScoped
public class ItemValueService implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean checkPermission() {
        HttpSession session = SessionUtils.getSession();
        return (((boolean) session.getAttribute("isAdmin")));
    }

    public List<Material> getMaterials() {
        List<Material> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QMaterial request = QMaterial.material;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }

        return materials;
    }

    public List<ProcessTech> getProcessTechs() {
        List<ProcessTech> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QProcessTech request = QProcessTech.processTech;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }
        return materials;
    }

    public List<Color> getColors() {
        List<Color> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QColor request = QColor.color;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }

        return materials;
    }

    public List<ParticleGeo> getParticleGeos() {
        List<ParticleGeo> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QParticleGeo request = QParticleGeo.particleGeo;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }

        return materials;
    }

    public List<PolymerModification> getPolymerModifications() {
        List<PolymerModification> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QPolymerModification request = QPolymerModification.polymerModification;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }

        return materials;
    }

    public List<TargetMarket> getTargetMarkets() {
        List<TargetMarket> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QTargetMarket request = QTargetMarket.targetMarket;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }

        return materials;
    }

    public List<TargetProduct> getTargetProducts() {
        List<TargetProduct> materials = new ArrayList<>();
        try (Session session = HibernateUtil.getHibernateSession()) {

            QTargetProduct request = QTargetProduct.targetProduct;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            materials = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }

        return materials;
    }

    public void createInstance(Material r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.save(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void updateInstance(Material r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.saveOrUpdate(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void deleteInstance(Material r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("deleting " + r);
                session.delete(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done deleting");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void createInstance(ParticleGeo r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.save(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void updateInstance(ParticleGeo r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.saveOrUpdate(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void deleteInstance(ParticleGeo r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("deleting " + r);
                session.delete(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done deleting");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void createInstance(PolymerModification r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.save(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void updateInstance(PolymerModification r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.saveOrUpdate(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void deleteInstance(PolymerModification r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("deleting " + r);
                session.delete(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done deleting");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void createInstance(TargetMarket r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.save(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void updateInstance(TargetMarket r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.saveOrUpdate(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void deleteInstance(TargetMarket r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("deleting " + r);
                session.delete(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done deleting");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void createInstance(TargetProduct r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.save(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void updateInstance(TargetProduct r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.saveOrUpdate(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void deleteInstance(TargetProduct r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("deleting " + r);
                session.delete(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done deleting");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void createInstance(ProcessTech r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.save(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void updateInstance(ProcessTech r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("saving " + r);
                session.saveOrUpdate(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done saving");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }

    public void deleteInstance(ProcessTech r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission()) {
            try (Session session = HibernateUtil.getHibernateSession()) {
                Transaction t = session.beginTransaction();
                Logger.getLogger(RequestService.class.getName()).warning("deleting " + r);
                session.delete(r);
                t.commit();
                session.close();
                Logger.getLogger(RequestService.class.getName()).warning("done deleting");
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################4");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            }
        }
    }
}
