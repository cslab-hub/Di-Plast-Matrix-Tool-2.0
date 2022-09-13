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
import com.uos.matrix.entities.QRequestEntity;
import com.uos.matrix.entities.RankableDoubleEntry;
import com.uos.matrix.entities.RankableIntegerEntry;
import com.uos.matrix.entities.RankableRangedEntry;
import com.uos.matrix.entities.RecycledEntity;
import java.util.List;
import javax.annotation.PostConstruct;
import com.uos.matrix.entities.RequestEntity;
import com.uos.matrix.entities.RequestContext;
import com.uos.matrix.entities.TargetMarket;
import com.uos.matrix.entities.TargetProduct;
import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.helper.enums.HardnessConditions;
import com.uos.matrix.helper.enums.ImpactStrConditions;
import com.uos.matrix.helper.enums.InventoryStatus;
import com.uos.matrix.helper.enums.MFIConditions;
import com.uos.matrix.helper.enums.Triplet;
import com.uos.matrix.helper.enums.Units;
import com.uos.matrix.helper.enums.VicatConditions;
import com.uos.matrix.helper.other.HibernateUtil;
import com.uos.matrix.helper.other.SessionUtils;
import com.uos.matrix.view.SellerView;
import com.querydsl.core.support.QueryBase;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

@ManagedBean(name = "requestService")
@ApplicationScoped
public class RequestService implements Serializable {

    private List<RequestEntity> requests;

    private static final long serialVersionUID = 2L;

    @ManagedProperty(value = "#{globalSettingsService}")
    private GlobalSettingsService globalSettingsService;

    @ManagedProperty(value = "#{serverFormatter}")
    private ServerFormatter formatterService;

    @PostConstruct
    public void init() {
        requests = new ArrayList<>();

        requests = getRequests();
    }

    public List<RequestEntity> getRequests() {
        return getRequests(false, false);
    }

    public List<RequestEntity> getRequests(boolean autoRequest, boolean resolved) {
        HttpSession httpsSession = SessionUtils.getSession();
        String currentUserName = (String) httpsSession.getAttribute("username");
        return getRequests(autoRequest, resolved, globalSettingsService.isPrivateDataOnly(), currentUserName);
    }

    private List<RequestEntity> getRequests(boolean autoRequest, boolean resolved, boolean privateRequests, String currentUserName) {
        try (Session session = HibernateUtil.getHibernateSession()) {
            QRequestEntity request = QRequestEntity.requestEntity;
            EntityManager em = session.getEntityManagerFactory().createEntityManager();
            JPAQuery query = new JPAQuery(em);
            query.from(request);

            //TODO test!!!
            if (!autoRequest) {
                query.where(request.autoRequest.eq(false));
            }
            if (!resolved) {
                query.where(request.requestResolved.eq(false));
            }
            if (privateRequests) {
                HttpSession httpsSession = SessionUtils.getSession();
                boolean isAdmin = (boolean) httpsSession.getAttribute("isAdmin");
                if (!isAdmin) {
                    query.where(request.createdBy.eq(currentUserName));
                }
            }

            requests = query.fetch();
            em.close();
            session.close();
        } catch (Exception e) {
            //if (transaction != null) {
            //    transaction.rollback();
            ////}
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }
        //Logger.getLogger(RequestService.class.getName()).warning("*****+++#++++******** " + requests.size());
        return requests;
    }

    public List<RequestEntity> getOwnedRequests(UserEntity u) {
        return getOwnedRequests(u, false, false);
    }

    public List<RequestEntity> getOwnedRequests(UserEntity u, boolean autoRequest, boolean resolved) {
        return getRequests(autoRequest, resolved, true, u.getUsername());
    }

    public void createInstance(RequestEntity r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission(r)) {
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
                //if (transaction != null) {
                //    transaction.rollback();
                //}
            }
        }
    }

    public void updateInstance(RequestEntity r) {
        Logger.getLogger(RequestService.class.getName()).warning("start saving");
        if (checkPermission(r)) {
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
                //if (transaction != null) {
                //    transaction.rollback();
                //}
            }
        }
    }

    public void deleteInstance(RequestEntity r) {
        Logger.getLogger(RequestService.class.getName()).warning("start deleting");
        if (checkPermission(r)) {
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
                //if (transaction != null) {
                //    transaction.rollback();
                //}
            }
        }
    }

    private JPAQuery generateQuery(Session session, Expression ex, boolean distinct) {
        QRequestEntity request = QRequestEntity.requestEntity;
        EntityManager em = session.getEntityManagerFactory().createEntityManager();
        JPAQuery query = new JPAQuery(em);
        query.from(request);
        query.select(ex);

        if (globalSettingsService.isPrivateDataOnly()) {
            HttpSession httpsSession = SessionUtils.getSession();
            boolean isAdmin = (boolean) httpsSession.getAttribute("isAdmin");
            if (!isAdmin) {
                String currentUserName = (String) httpsSession.getAttribute("username");
                query.where(request.createdBy.eq(currentUserName));
            }
        }
        if (distinct) {
            query.distinct();
        }

        return query;
    }

    public List<String> getDistinctTestedBy() {
        try (Session session = HibernateUtil.getHibernateSession()) {
            JPAQuery query = generateQuery(session, QRequestEntity.requestEntity.testedBy, true);
            List<String> output = query.fetch();
            //List<String> output = session.createQuery("SELECT DISTINCT a.testedBy FROM RequestEntity a", String.class).getResultList();
            List<String> output2 = output.stream().filter(Objects::nonNull).collect(Collectors.toList());
            session.close();
            return output2;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
        return new ArrayList<>();
    }

    public List<String> getDistinctSpecificApplication() {
        try (Session session = HibernateUtil.getHibernateSession()) {
            JPAQuery query = generateQuery(session, QRequestEntity.requestEntity.context.specificApplication, true);
            List<String> output = query.fetch();
            //List<String> output = session.createQuery("SELECT DISTINCT a.testedBy FROM RequestEntity a", String.class).getResultList();
            List<String> output2 = output.stream().filter(Objects::nonNull).collect(Collectors.toList());
            session.close();
            return output2;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
        return new ArrayList<>();
    }

    public List<Triplet> getAllTriplets() {
        return Arrays.asList(Triplet.values());
    }

    private boolean checkPermission(RequestEntity r) {
        HttpSession session = SessionUtils.getSession();
        return (((boolean) session.getAttribute("isAdmin")) || ((String) session.getAttribute("username")).equals(r.getCreatedBy()));
    }

    public boolean uploadFile(InputStream is) {
        Transaction transaction = null;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            HttpSession httpSession = SessionUtils.getSession();

            try (Session session = HibernateUtil.getHibernateSession()) {

                // start a transaction
                transaction = session.beginTransaction();
                int k = 0;
                boolean first = true;
                List<String> columns = new ArrayList<>();
                while ((line = br.readLine()) != null) {

                    String[] values = line.split(";");
                    List<String> entries = Arrays.asList(values);
                    if (first) {
                        first = false;
                        columns = entries;
                        continue;
                    }
                    RequestEntity r = new RequestEntity();
                    r.setCreatedBy(httpSession.getAttribute("username").toString());
                    if (formatString(entries.get(0).toLowerCase()).startsWith("converter")) {

                        entries = entries.stream().map(a -> formatString(a)).collect(Collectors.toList());

                        for (int i = 0; i < columns.size(); i++) {
                            String e = columns.get(i);
                            RankableDoubleEntry d;
                            RankableIntegerEntry in;
                            RankableRangedEntry ra;
                            String subS;
                            if (i >= entries.size() || entries.get(i) == null) {
                                continue;
                            }
                            switch (e) {
                                case "Converter ID":
                                    r.setBuyerName(entries.get(i));
                                    break;
                                case "Material ID":
                                    r.setMaterialCode(entries.get(i));
                                    break;
                                case "Polymer Type":
                                    Material m = new Material(formatString(entries.get(i)));
                                    Material loadedM = session.byNaturalId(Material.class).using("name", m.getName()).load();
                                    if (loadedM == null) {
                                        session.save(m);
                                    } else {
                                        m = loadedM;
                                    }
                                    r.setMaterial(m);
                                    break;
                                case "Target industry":
                                    TargetMarket tm = new TargetMarket(formatString(entries.get(i)));
                                    TargetMarket loadedTM = session.byNaturalId(TargetMarket.class).using("name", tm.getName()).load();
                                    if (loadedTM == null) {
                                        session.save(tm);
                                    } else {
                                        tm = loadedTM;
                                    }
                                    r.getContext().setTargetMarket(tm);
                                    break;
                                case "General target application":
                                    TargetProduct tp = new TargetProduct(formatString(entries.get(i)));
                                    TargetProduct loadedTP = session.byNaturalId(TargetProduct.class).using("name", tp.getName()).load();
                                    if (loadedTP == null) {
                                        session.save(tp);
                                    } else {
                                        tp = loadedTP;
                                    }
                                    r.getContext().setTargetProduct(tp);
                                    break;
                                case "Specific target application":
                                    String ta = entries.get(i);
                                    r.getContext().setSpecificApplication(ta);
                                    break;
                                case "Polymer modification":
                                    PolymerModification poly = new PolymerModification(entries.get(i));
                                    PolymerModification loadedpoly = session.byNaturalId(PolymerModification.class).using("name", poly.getName()).load();
                                    if (loadedpoly == null) {
                                        session.save(poly);
                                    } else {
                                        poly = loadedpoly;
                                    }
                                    r.setPolymerModifications(poly);
                                    break;
                                case "Modification content":
                                    ra = r.getModificationContent();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Recyclate content":
                                    d = r.getRecyclingContent();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Processing technology":
                                    ProcessTech process = new ProcessTech(entries.get(i));
                                    ProcessTech loadedprocess = session.byNaturalId(ProcessTech.class).using("name", process.getName()).load();
                                    if (loadedprocess == null) {
                                        session.save(process);
                                    } else {
                                        process = loadedprocess;
                                    }
                                    r.setProcessTech(process);
                                    break;
                                case "Particle geometry":
                                    ParticleGeo part = new ParticleGeo(entries.get(i));
                                    ParticleGeo loadedpart = session.byNaturalId(ParticleGeo.class).using("name", part.getName()).load();
                                    if (loadedpart == null) {
                                        session.save(part);
                                    } else {
                                        part = loadedpart;
                                    }
                                    r.setParticleGeometry(part);
                                    break;
                                case "Melt filtration size":
                                    in = r.getMeltFiltrationSize();
                                    in.setValue(formatterService.getInteger(entries.get(i)));
                                    i++;
                                    in.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Color":
                                    Color c = new Color(entries.get(i));
                                    Color loadedc = session.byNaturalId(Color.class).using("name", c.getName()).load();
                                    if (loadedc == null) {
                                        session.save(c);
                                    } else {
                                        c = loadedc;
                                    }
                                    r.setColor(c);
                                    break;
                                case "Density":
                                    ra = r.getDensity();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Bulk density":
                                    ra = r.getBulkDensity();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "K-Value":
                                    ra = r.getKValue();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    break;

                                case "E-modulus":
                                    ra = r.getEModulus();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;

                                case "E-modulus MD":
                                    ra = r.getEModulusMD();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "E-modulus TD":
                                    ra = r.getEModulusTD();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Flexural modulus":

                                    ra = r.getFlexuralModulus();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strength at yield":
                                    d = r.getTensileStrenghtAtYield();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strength at yield MD":
                                    d = r.getTensileStrenghtAtYieldMD();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strength at yield TD":
                                    d = r.getTensileStrenghtAtYieldTD();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strain at yield":
                                    d = r.getTensileElongationAtYield();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strain at yield MD":
                                    d = r.getTensileElongationAtYieldMD();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strain at yield TD":
                                    d = r.getTensileElongationAtYieldTD();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strain at break":
                                    d = r.getElongationAtBreak();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strain at break MD":
                                    d = r.getElongationAtBreakMD();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Strain at break TD":
                                    d = r.getElongationAtBreakTD();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "OIT":
                                    d = r.getOit();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "HDT":
                                    d = r.getHdt();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Mold shrinkage":
                                    ra = r.getMouldShrinkage();
                                    ra.setValueMin(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    ra.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Maximum moisture content":
                                    d = r.getMoistureContent();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Maximum ash content":
                                    d = r.getAshContent();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                case "Maximum pollution content":
                                    d = r.getPollution();
                                    d.setValue(formatterService.getDouble(entries.get(i)));
                                    i++;
                                    d.setUnit(Units.getUnitByName(entries.get(i)));
                                    break;
                                default:
                                    if (e.startsWith("MFI")) {
                                        MFIConditions cond = MFIConditions.getConditionByName(e.substring(5, e.length() - 1));
                                        if (cond == null) {
                                            Logger.getLogger(SellerView.class.getName()).warning("No condition found!!!! Using default for: " + entries.get(i));
                                            cond = MFIConditions.C190C500KG;
                                        }

                                        r.getMFIs().get(cond).setValueMin(formatterService.getDouble(entries.get(i)));
                                        i++;
                                        r.getMFIs().get(cond).setUnit(Units.getUnitByName(entries.get(i)));
                                    } else if (e.startsWith("Notched charpy")) {
                                        subS = e.substring(e.length() - 4);
                                        ImpactStrConditions cond = ImpactStrConditions.getConditionByName("Notched " + subS);
                                        if (cond == null) {
                                            Logger.getLogger(SellerView.class.getName()).warning("No condition found!!!! Using default for: " + entries.get(i));
                                            cond = ImpactStrConditions.NOTCHED23C;
                                        }

                                        r.getCharpyImpactStrs().get(cond).setValue(formatterService.getDouble(entries.get(i)));
                                        i++;
                                        r.getCharpyImpactStrs().get(cond).setUnit(Units.getUnitByName(entries.get(i)));
                                    } else if (e.startsWith("Unnotched charpy")) {
                                        subS = e.substring(e.length() - 4);
                                        ImpactStrConditions cond = ImpactStrConditions.getConditionByName("Unnotched " + subS);
                                        if (cond == null) {
                                            Logger.getLogger(SellerView.class.getName()).warning("No condition found!!!! Using default for: " + entries.get(i));
                                            cond = ImpactStrConditions.NOTCHED23C;
                                        }

                                        r.getCharpyImpactStrs().get(cond).setValue(formatterService.getDouble(entries.get(i)));
                                        i++;
                                        r.getCharpyImpactStrs().get(cond).setUnit(Units.getUnitByName(entries.get(i)));
                                    } else if (e.startsWith("Notched Izod")) {
                                        subS = e.substring(e.length() - 4);
                                        ImpactStrConditions cond = ImpactStrConditions.getConditionByName("Notched " + subS);
                                        if (cond == null) {
                                            Logger.getLogger(SellerView.class.getName()).warning("No condition found!!!! Using default for: " + entries.get(i));
                                            cond = ImpactStrConditions.NOTCHED23C;
                                        }

                                        r.getIzodImpactStrs().get(cond).setValue(formatterService.getDouble(entries.get(i)));
                                        i++;
                                        r.getIzodImpactStrs().get(cond).setUnit(Units.getUnitByName(entries.get(i)));
                                    } else if (e.startsWith("Unnotched Izod")) {
                                        subS = e.substring(e.length() - 4);
                                        ImpactStrConditions cond = ImpactStrConditions.getConditionByName("Unnotched " + subS);
                                        if (cond == null) {
                                            Logger.getLogger(SellerView.class.getName()).warning("No condition found!!!! Using default for: " + entries.get(i));
                                            cond = ImpactStrConditions.NOTCHED23C;
                                        }

                                        r.getIzodImpactStrs().get(cond).setValue(formatterService.getDouble(entries.get(i)));
                                        i++;
                                        r.getIzodImpactStrs().get(cond).setUnit(Units.getUnitByName(entries.get(i)));
                                    } else if (e.startsWith("Vicat")) {
                                        Double value = formatterService.getDouble(entries.get(i));
                                        i++;
                                        Units u = Units.getUnitByName(entries.get(i));
                                        i++;
                                        VicatConditions cond = VicatConditions.getConditionByName(entries.get(i));
                                        if (cond == null) {
                                            Logger.getLogger(SellerView.class.getName()).warning("No condition found!!!! Using default for: " + entries.get(i));
                                            cond = VicatConditions.B50;
                                        }
                                        r.getVicats().get(cond).setUnit(u);
                                        r.getVicats().get(cond).setValue(value);
                                    } else if (e.contains("hardness")) {
                                        HardnessConditions cond;
                                        if (e.startsWith("Shore A")) {
                                            cond = HardnessConditions.SHOREA;
                                        } else if (e.startsWith("Shore D")) {
                                            cond = HardnessConditions.SHORED;
                                        } else if (e.startsWith("Rockwell")) {
                                            cond = HardnessConditions.ROCKWELLE;
                                        } else {
                                            cond = HardnessConditions.ROCKWELLE;
                                        }
                                        r.getHardness().get(cond).setValueMin(formatterService.getDouble(entries.get(i)));
                                    } else {
                                        r.setComments(r.getComments() + e + ": " + entries.get(i) + " \n");
                                    }

                            }
                        }

                        Logger.getLogger(RequestService.class.getName()).warning("Mass-creating " + r);
                        session.save(r);
                        requests.add(r);
                    }
                }
                Logger.getLogger(SellerView.class.getName()).warning("Commiting!!!!");
                transaction.commit();
                session.close();
                return true;
            } catch (Exception e) {
                Logger.getLogger(RequestService.class.getName()).warning("##################2");
                Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
                Logger.getLogger(SellerView.class.getName()).warning("Error 1" + e.getMessage());
                return false;
                //if (transaction != null) {
                //    transaction.rollback();
                //}
            }

        } catch (IOException e) {
            Logger.getLogger(RequestService.class.getName()).warning("################################");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            return false;
        }
    }

    public double getCharpyMin() {
        return 0;
    }

    public double getIzodMin() {
        return 0;
    }

    public double getHardnessMin() {
        return 0;
    }

    public double getVicatMin() {
        return 0;
    }

    public double getCharpyMax() {/*
        double max = requests.stream().flatMap(c -> c.getCharpyImpactStrs().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMin()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);
        double max2 = requests.stream().flatMap(c -> c.getCharpyImpactStrs().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMax()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);

        if (max2 > max) {
            max = max2;
        }
        if (max < 100) {
            max = 100;
        } */
        return 100;
    }

    public double getIzodMax() {
        /*double max = requests.stream().flatMap(c -> c.getIzodImpactStrs().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMin()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);
        double max2 = requests.stream().flatMap(c -> c.getIzodImpactStrs().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMax()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);

        if (max2 > max) {
            max = max2;
        }
        if (max < 100) {
            max = 100;
        }*/
        double max = 500;
        return max;
    }

    public double getHardnessMax() {
        double max = requests.stream().flatMap(c -> c.getHardness().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMin()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);
        double max2 = requests.stream().flatMap(c -> c.getHardness().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMax()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);

        if (max2 > max) {
            max = max2;
        }
        if (max < 100) {
            max = 100;
        }
        return max;
    }

    public double getVicatMax() {/*
        double max = requests.stream().flatMap(c -> c.getVicats().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMin()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);
        double max2 = requests.stream().flatMap(c -> c.getVicats().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMax()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(100);

        if (max2 > max) {
            max = max2;
        }
        if (max < 500) {
            max = 500;
        }*/ 
        return 500;
    }

    public double getMFIMin() {
        /**
         * try (Session session = HibernateUtil.getHibernateSession()) {
         * JPAQuery query = generateQuery(session,
         * QRecycledEntity.recycledEntity.mFIs.get(QRecycledEntity.recycledEntity.currentMFICondition).valueMin.min(),
         * false); query.isNotNull(); Double minValue = (Double)
         * query.fetchOne(); //Double minValue = session.createQuery("SELECT
         * min(a.mFI.valueMin) FROM RecycledEntity a WHERE a.mFI.valueMin is not
         * null", Double.class).iterate().next(); session.close(); if (minValue
         * == null || minValue > 0) { return 0; }
         * //Logger.getLogger(RequestService.class.getName()).warning("###########min"
         * + minValue); return minValue; } catch (Exception e) {
         * Logger.getLogger(RequestService.class.getName()).warning("##################4");
         * Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
         * //if (transaction != null) { // transaction.rollback(); //} }
         */
        return 0;
    }

    public double getMFIMax() {
        /*
        try (Session session = HibernateUtil.getHibernateSession()) {
            JPAQuery query = generateQuery(session, QRecycledEntity.recycledEntity.mFIs.get(QRecycledEntity.recycledEntity.currentMFICondition).valueMin.max(), false);
            query.isNotNull();
            Double minValue = (Double) query.fetchOne();
            JPAQuery query2 = generateQuery(session, QRecycledEntity.recycledEntity.mFIs.get(QRecycledEntity.recycledEntity.currentMFICondition).valueMax.max(), false);
            query2.isNotNull();
            Double maxValue = (Double) query.fetchOne();
            ///Double minValue = session.createQuery("SELECT max(a.mFI.valueMax) FROM RequestEntity a WHERE a.mFI.valueMax is not null", Double.class).iterate().next();

            if (maxValue > minValue) {
                minValue = maxValue;
            }
            //Double minValue = session.createQuery("SELECT max(a.mFI.valueMax) FROM RecycledEntity a WHERE a.mFI.valueMax is not null", Double.class).iterate().next();
            session.close();
            if (minValue == null) {
                return 70;
            }
            //Logger.getLogger(RequestService.class.getName()).warning("###########max" + minValue);
            return minValue;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
         */
        double max = requests.stream().flatMap(c -> c.getMFIs().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMin()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(70);
        double max2 = requests.stream().flatMap(c -> c.getMFIs().entrySet().stream()).map(c -> c.getValue()).filter(Objects::nonNull).map(d -> d.getValueMax()).filter(Objects::nonNull).mapToDouble(d -> d).max().orElse(70);

        if (max2 > max) {
            max = max2;
        }
        if (max < 70) {
            max = 70;
        }
        return max;

    }

    public double getDensityMin() {
        try (Session session = HibernateUtil.getHibernateSession()) {
            JPAQuery query = generateQuery(session, QRequestEntity.requestEntity.density.valueMin.min(), false);
            query.isNotNull();
            Double minValue = (Double) query.fetchOne();
            //Double minValue = session.createQuery("SELECT min(a.density.value) FROM RequestEntity a WHERE a.density.value is not null", Double.class).iterate().next();
            session.close();
            if (minValue == null) {
                return 0;
            }
            return minValue;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
        return 0;
    }

    public double getDensityMax() {
        try (Session session = HibernateUtil.getHibernateSession()) {
            JPAQuery query = generateQuery(session, QRequestEntity.requestEntity.density.valueMin.max(), false);
            query.isNotNull();
            Double minValue = (Double) query.fetchOne();
            ///Double minValue = session.createQuery("SELECT max(a.density.value) FROM RequestEntity a WHERE a.density.value is not null", Double.class).iterate().next();
            session.close();
            if (minValue == null) {
                return 0;
            }
            return minValue;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
        return 2;
    }

    public double getAdensityMin() {
        try (Session session = HibernateUtil.getHibernateSession()) {
            JPAQuery query = generateQuery(session, QRequestEntity.requestEntity.bulkDensity.valueMin.min(), false);
            query.isNotNull();
            Double minValue = (Double) query.fetchOne();
            //Integer minValue = session.createQuery("SELECT min(a.apparentDensity.value) FROM RequestEntity a WHERE a.apparentDensity.value is not null", Integer.class).iterate().next();
            session.close();
            if (minValue == null) {
                return 0;
            }
            return minValue;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
        return 0;
    }

    public void setGlobalSettingsService(GlobalSettingsService globalSettingsService) {
        this.globalSettingsService = globalSettingsService;
    }

    public String formatString(String string) {
        if (string.equals("NULL") || string.isBlank()) {
            return null;
        } else {
            return string;
        }
    }

    public void setFormatterService(ServerFormatter formatterService) {
        this.formatterService = formatterService;
    }

}
