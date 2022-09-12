/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;

import com.uos.matrix.entities.RecycledEntity;
import com.uos.matrix.entities.RequestEntity;
import com.uos.matrix.entities.UserEntity;
import com.uos.matrix.entities.ValidationRequestEntity;
import com.uos.matrix.helper.other.SessionUtils;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * 
 */
@ManagedBean(name = "mailService")
@ApplicationScoped
public class MailService implements Serializable {

    private static final long serialVersionUID = 1L;

    private Properties prop;

    @ManagedProperty(value = "#{userService}")
    private UserService userService;

    @PostConstruct
    public void init() {
        prop = new Properties();
        prop.put("mail.smtp.auth", false);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "localhost");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.ssl.trust", "localhost");
    }

    private Session initMailSession() {
        return Session.getInstance(prop);
    }

    public boolean sendAccessCode(UserEntity u, UserEntity currentUser) {
        String confirmCode = getRandomNumberString();
        u.setConfirmed(false);
        u.setConfirmationCodePlaceholder(confirmCode);
        userService.updateUser(u, currentUser);
        String msg = "Username: " + u.getUsername() +"\nPlease insert the following code after your login to confirm your account: " + confirmCode;
        String subject = "[Matrix Tool 2.0] Mail Confirmation - do not reply";
        return genericMessage(subject, msg, u.getEmail());
    }

    public String sendNewRandomPW(String mail) {
        String newPW = generateCommonLangPassword();
        sendNewRandomPW(mail, newPW);
        return newPW;
    }

    public boolean mailChanged(UserEntity u, UserEntity currentUser) {
        boolean success = sendAccessCode(u, currentUser);
        if (!success) {
            return false;
        }
        String msg = "Your mail was changed to " + u.getEmail() + ".  If this was not done by you, please contact an Admin.";
        String subject = "[Matrix Tool 2.0] Mail Changed - do not reply";
        return genericMessage(subject, msg, u.getConfirmedEmail());
    }

    public boolean sendNewRandomPW(String mail, String newPW) {
        String subject = "[Matrix Tool 2.0] Password Change - do not reply";
        String msg = "Your password was changed! For the next login please use the following password: " + newPW;
        return genericMessage(subject, msg, mail);
    }

    public boolean sendFeedback(String input) {
        String subject = "[Matrix Tool 2.0] Feedback";
        return genericMessage(subject, input, "lschwenke+diplast@uos.de");
    }

    private boolean genericMessage(String subject, String msg, String targetMail) {
        if (targetMail == null || targetMail.isBlank()) {
            Logger.getLogger(ValidationRequestEntity.class.getName()).warning("###########mail-null");

            return false;
        }
        Session session = initMailSession();
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("noreply@yourdomain.de"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(targetMail));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (MessagingException ex) {
            Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public String generateCommonLangPassword() {
        String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
        String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
        String numbers = RandomStringUtils.randomNumeric(2);
        String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
        String totalChars = RandomStringUtils.randomAlphanumeric(2);
        String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
                .concat(numbers)
                .concat(specialChar)
                .concat(totalChars);
        List<Character> pwdChars = combinedChars.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(pwdChars);
        String password = pwdChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return password;
    }

    public boolean sendContact(UserEntity u, RecycledEntity r) {
        if (r == null) {
            Logger.getLogger(ValidationRequestEntity.class.getName()).warning("###########R-null");

            return false;
        }
        String subject = "[Matrix Tool 2.0] Contact Request - do not reply";
        String contactMail = userService.getUserMailByName(r.getCreatedBy());
        if (contactMail == null) {
            contactMail = userService.getAdminMail();
        }
        String msg = u.getEmail() + "requests to get in contact via the matrix tool, regarding the material: \n" + r.toString() + ".\n\n Please get back to them to discuss details, via the email: " + u.getEmail();
        return genericMessage(subject, msg, contactMail);
    }

    public boolean sendContact(UserEntity u, RequestEntity r) {
        if (r == null) {
            Logger.getLogger(ValidationRequestEntity.class.getName()).warning("###########R-null");

            return false;
        }
        String subject = "[Matrix Tool 2.0] Contact Request - do not reply";
        String contactMail = userService.getUserMailByName(r.getCreatedBy());
        if (contactMail == null) {
            contactMail = userService.getAdminMail();
        }
        String msg = u.getEmail() + "requests to get in contact via the matrix tool, regarding your request: \n" + r.toString() + ".\n\n Please get back to them to discuss details, via the email: " + u.getEmail();
        return genericMessage(subject, msg, contactMail);
    }

    public boolean requestVerification(UserEntity u, RecycledEntity r) {
        if (r == null) {
            Logger.getLogger(ValidationRequestEntity.class.getName()).warning("###########R-null");
            return false;
        }
        String subject = "[Matrix Tool 2.0] Verification Request - do not reply";
        String msg = "A verification request was made by: " + u.toString() + ".\n Contact of material provider: " + r.getCreatedBy() + ".\n Current known properties of material: " + r.toString();
        String contactMail = userService.getAdminMail();
        boolean success = genericMessage(subject, msg, contactMail);
        if (success) {
            subject = "[Matrix Tool 2.0] Confirmation for material verification - do not reply";
            msg = "This is a confirmations that a request for verification was send. PSP will make contact with you as soon as possible.\n Current known properties of material: " + r.toString();
            return genericMessage(subject, msg, u.getEmail());
        }
        return false;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
