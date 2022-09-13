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
import com.uos.matrix.entities.TargetMarket;
import com.uos.matrix.entities.TargetProduct;
import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.helper.enums.ParticleGeometry;
import com.uos.matrix.helper.enums.PolyModifications;
import com.uos.matrix.helper.other.HibernateUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.hibernate.Session;
import org.hibernate.Transaction;

@ManagedBean(name = "userService")
@ApplicationScoped
public class UserService implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<String> defaultColors = Arrays.asList("Blue", "Red", "Black", "Transparent", "Grey", "White", "Green", "Yellow", "Light", "Mix", "Natural", "light blue", "Greyeen", "Ivory", "Light grey", "Dark grey", "Toasted", "Brown");
    private final List<String> defaultMaterials = Arrays.asList("PP homopolymer", "PP copolymer", "PP","PET","PE-MD","PE-LLD","PE-LD","PE-HD","PE","PA 66","PA 12","PA 46","PA 6","HIPS","ABS","Mix", "Mix ABS/PS", "Mix ABS/PS/PE/PP", "Mix PE/PP");
    private final List<String> defaultProcessTech = Arrays.asList("Blown film", "Injection molding", "Extrusion", "Blow molding", "Cast film", "Fiber spinning", "Rotational molding", "Thermoforming");
    private final List<String> defaultTargetMarkets = Arrays.asList("Automotive", "Packaging", "Construction", "Infrastructure");
    //TODOPS



    //private final List<String> defaultTargetProducts = Arrays.asList("Flue gas exhaust flame retardant", "Pipe", "Profile", "Turbo", "Electro Tube", "Sewer fittings", "Infiltration crates", "Inspection pit", "Drain", "Fittings black", "Fence pit", "Gullies", "Sewers", "Meter box plate", "street gully","Roof penetration flue gas", "Wall duct flue gas", "Floor ventilation", "Bracket", "Eaves Vent", "Heat pump casing", "Electro fittings", "tube black", "Sewer Pipe", "Cover profile", "Roof foil", "Drinking water pipe", "Drainage Tube", "Roof bottom frost", "Ventilation pan", "Cable Protection Tube", "Ventilation shaft", "Cable protection fitting", "Pre-insulated pipe", "Tube", "Dashboard", "Charging station valve");
    private final List<String> defaultTargetProducts = Arrays.asList("Electrical cables","Carpet","Profiles","Gullies","Automotive interior","Piping","Fittings","Ventilation","Flue gas","Casings","Infiltration crate","Tubing","Automotive exterior", "Flexible packaging", "Caps and closures", "Rigid packaging");
    @PostConstruct
    public void init() {
        //TODO user creation if no admin exists
        try (Session session = HibernateUtil.getHibernateSession()) {

            // start a transaction
            Transaction transaction = session.beginTransaction();
            List<UserEntity> admin = session.createQuery("SELECT u FROM UserEntity u WHERE u.uname = :username", UserEntity.class).setParameter("username", "Admin").getResultList();
            if (admin == null || admin.isEmpty()) {

                UserEntity u = new UserEntity("Admin", "admin", true, "test@test.de", "", "");
                u.setResetPW(false);
                u.setConfirmed(true);
                u.setConfirmedAgreement(true);
                session.save(u);

                for (ParticleGeometry p : ParticleGeometry.values()) {
                    ParticleGeo pg = new ParticleGeo(p.getName());
                    session.save(pg);
                }

                for (PolyModifications p : PolyModifications.values()) {
                    PolymerModification pg = new PolymerModification(p.getName());
                    session.save(pg);
                }
                for (String p : defaultColors) {
                    Color c = new Color(p);
                    session.save(c);
                }
                for (String p : defaultMaterials) {
                    Material c = new Material(p);
                    session.save(c);
                }
                for (String p : defaultProcessTech) {
                    ProcessTech c = new ProcessTech(p);
                    session.save(c);
                }
                for (String p : defaultTargetMarkets) {
                    TargetMarket c = new TargetMarket(p);
                    session.save(c);
                }
                for (String p : defaultTargetProducts) {
                    TargetProduct c = new TargetProduct(p);
                    session.save(c);
                }
                transaction.commit();
                session.close();
            }
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################USER");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
    }

    public String getUserMailByName(String name) {
        try (Session session = HibernateUtil.getHibernateSession()) {
            UserEntity resultUser = session.createQuery("SELECT u FROM UserEntity u WHERE u.uname = :username", UserEntity.class).setParameter("username", name).getSingleResult();
            session.close();
            return resultUser.getEmail();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################VALIDATE");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }
        return null;
    }

    public String getAdminMail() {
        return getUserMailByName("Admin");
    }

    public boolean updateUser(UserEntity user, UserEntity currentUser) {
        if (currentUser == null) {
            Logger.getLogger(RequestService.class.getName()).warning("##################CURRENT USER IS NULL");
            return false;
        }
        if (user == null) {
            Logger.getLogger(RequestService.class.getName()).warning("##################GIVEN USER IS NULL");
            return false;
        }
        if (!currentUser.isAdminTag() && currentUser.getId() != user.getId()) {
            Logger.getLogger(RequestService.class.getName()).warning("##################No permission");
            return false;
        }
        try (Session session = HibernateUtil.getHibernateSession()) {

            // start a transaction
            Transaction transaction = session.beginTransaction();

            session.saveOrUpdate(user);

            transaction.commit();
            session.close();

        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################USER");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            return false;
        }
        Logger.getLogger(RequestService.class.getName()).warning("##################User update success");
        return true;
    }

    public boolean deleteNewUser(UserEntity user, UserEntity currentUser) {
        if (!currentUser.isAdminTag()) {
            return false;
        }

        try (Session session = HibernateUtil.getHibernateSession()) {

            // start a transaction
            Transaction transaction = session.beginTransaction();

            session.delete(user);

            transaction.commit();
            session.close();

        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################USER");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            return false;
        }
        return true;
    }

    public List<UserEntity> getAllUsers(UserEntity currentUser) {
        List<UserEntity> allUsers = new ArrayList<>();
        if (!currentUser.isAdminTag()) {
            return allUsers;
        }
        try (Session session = HibernateUtil.getHibernateSession()) {
            allUsers = session.createQuery("SELECT a from UserEntity a", UserEntity.class).getResultList();
            allUsers.remove(currentUser);
            session.close();
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################4");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
            //if (transaction != null) {
            //    transaction.rollback();
            //}
        }
        //Logger.getLogger(RequestService.class.getName()).warning("******************** " + allUsers.size());
        return allUsers;
    }

    public UserEntity getUserByName(String user) {
        try (Session session = HibernateUtil.getHibernateSession()) {
            // start a transaction
            UserEntity resultUser = session.createQuery("SELECT u FROM UserEntity u WHERE u.uname = :username", UserEntity.class).setParameter("username", user).getSingleResult();
            session.close();
            return resultUser;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################VALIDATE");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }
        return null;
    }

    public UserEntity getUserByEmail(String email) {
        try (Session session = HibernateUtil.getHibernateSession()) {
            // start a transaction
            UserEntity resultUser = session.createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class).setParameter("email", email).getSingleResult();
            session.close();
            return resultUser;
        } catch (Exception e) {
            Logger.getLogger(RequestService.class.getName()).warning("##################VALIDATE");
            Logger.getLogger(RequestService.class.getName()).warning(e.getMessage());
        }
        return null;
    }
}
