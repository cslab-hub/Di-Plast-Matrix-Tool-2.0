/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.entities;

import com.uos.matrix.helper.other.PasswordHashingUtils;
import com.uos.matrix.services.LoginService;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "USERENTITY")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(name = "uname", unique = true, nullable = false)
    private String uname;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "config_id")
    private ConfigEntity config;

    private final boolean adminTag;

    private String email;
    private String confirmedEmail;

    private String fullname;
    private String organisation;

    private boolean resetPW = true;
    private String resetedPW;

    private boolean confirmed = false;
    private String confirmationCodePlaceholder;

    private boolean confirmedAgreement = false;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> pastConfirmations = new ArrayList<>();
    //TODO safe confirmation objekt? With IP and so on? and Date?

    public UserEntity() {
        adminTag = false;
        this.email = "";
        config = new ConfigEntity();
    }

    public UserEntity(String email) {
        adminTag = false;
        this.email = email;
        config = new ConfigEntity();
    }

    public UserEntity(String username, String password, boolean admin, String email, String fName, String organisation) {
        this.email = email;
        this.uname = username;
        this.adminTag = admin;
        this.fullname = fName;
        this.organisation = organisation;
        config = new ConfigEntity();

        try {
            this.password = PasswordHashingUtils.generateStorngPasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return uname;
    }

    public void setUsername(String username) {
        this.uname = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        try {
            this.password = PasswordHashingUtils.generateStorngPasswordHash(password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getResetedPW() {
        return resetedPW;
    }

    public void setResetedPW(String resetedPW) {
        try {
            this.resetedPW = PasswordHashingUtils.generateStorngPasswordHash(resetedPW);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(LoginService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConfigEntity getConfig() {
        return config;
    }

    public boolean isAdminTag() {
        return adminTag;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isResetPW() {
        return resetPW;
    }

    public void setResetPW(boolean resetPW) {
        this.resetPW = resetPW;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getConfirmationCodePlaceholder() {
        return confirmationCodePlaceholder;
    }

    public void setConfirmationCodePlaceholder(String confirmationCodePlaceholder) {
        this.confirmationCodePlaceholder = confirmationCodePlaceholder;
    }

    public String getConfirmedEmail() {
        return confirmedEmail;
    }

    public void setConfirmedEmail(String confirmedEmail) {
        this.confirmedEmail = confirmedEmail;
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

    public boolean isConfirmedAgreement() {
        return confirmedAgreement;
    }

    public void setConfirmedAgreement(boolean confirmedAgreement) {
        this.confirmedAgreement = confirmedAgreement;
    }

    public List<String> getPastConfirmations() {
        return pastConfirmations;
    }
    
    public void addConfirmation(String msg){
        pastConfirmations.add(msg);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + Objects.hashCode(this.uname);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserEntity other = (UserEntity) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.uname, other.uname)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserEntity{" + "id=" + id + ", uname=" + uname + ", email=" + email + ", fullname=" + fullname + ", organisation=" + organisation + '}';
    }



}
