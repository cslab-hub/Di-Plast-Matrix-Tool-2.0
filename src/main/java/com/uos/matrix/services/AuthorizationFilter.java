/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uos.matrix.services;

import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class AuthorizationFilter implements Filter {

    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

    public AuthorizationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURL = request.getContextPath() + "/login.xhtml";
        String changePwURL = request.getContextPath() + "/profile.xhtml";
        String resetPwURL = request.getContextPath() + "/resetpw.xhtml";
        String legalnoticeURL = request.getContextPath() + "/legalnotice.xhtml";
        String disclaimerURL = request.getContextPath() + "/disclaimer.xhtml";
        String dataPrivacyURL = request.getContextPath() + "/dataPrivacy.xhtml";
        String helpURL = request.getContextPath() + "/help.xhtml";
        String settingsURL = request.getContextPath() + "/adminsettings.xhtml";
        String codeURL = request.getContextPath() + "/confirmMail.xhtml";

        boolean loggedIn = (session != null) && (session.getAttribute("username") != null);
        boolean forcePWReset = (session != null) && (session.getAttribute("resetPW") != null) && (Boolean.parseBoolean(session.getAttribute("resetPW").toString()) == true);
        boolean isAdmin = (session != null) && (session.getAttribute("isAdmin") != null) && (Boolean.parseBoolean(session.getAttribute("isAdmin").toString()) == true);
        boolean needMailConfirmation = (session != null) && (session.getAttribute("needConfirm") != null) && (Boolean.parseBoolean(session.getAttribute("needConfirm").toString()) == true);
        boolean loginRequest = request.getRequestURI().equals(loginURL);
        boolean profileRequest = request.getRequestURI().equals(changePwURL);
        boolean settingRequest = request.getRequestURI().equals(settingsURL);
        boolean resetPWRequest = request.getRequestURI().equals(resetPwURL);
        boolean legalnoticeRequest = request.getRequestURI().equals(legalnoticeURL);
        boolean disclaimerRequest = request.getRequestURI().equals(disclaimerURL);
        boolean dataPrivacyRequest = request.getRequestURI().equals(dataPrivacyURL);
        boolean helpRequest = request.getRequestURI().equals(helpURL);
        boolean codeRequest = request.getRequestURI().equals(codeURL);
        boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");
        boolean ajaxRequest = "partial/ajax".equals(request.getHeader("Faces-Request"));

        if (resourceRequest || resetPWRequest || legalnoticeRequest || disclaimerRequest || dataPrivacyRequest || helpRequest) {
            if (!resourceRequest) { // Prevent browser from caching restricted resources. See also https://stackoverflow.com/q/4194207/157882
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                response.setDateHeader("Expires", 0); // Proxies.
            }

            if (chain != null) {
                chain.doFilter(request, response); // So, just continue request.
            } // So, just perform standard synchronous redirect.
        } else if (loggedIn || loginRequest) {
            if (needMailConfirmation) {
                if (codeRequest) {
                    if (chain != null) {
                        chain.doFilter(request, response); // So, just continue request.
                    }
                } else if (ajaxRequest) {
                    response.setContentType("text/xml");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().printf(AJAX_REDIRECT_XML, codeURL); // So, return special XML response instructing JSF ajax to send a redirect.
                } else {
                    response.sendRedirect(codeURL); // So, just perform standard synchronous redirect.
                }
            } else if (forcePWReset && !profileRequest) {
                if (ajaxRequest) {
                    response.setContentType("text/xml");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().printf(AJAX_REDIRECT_XML, changePwURL); // So, return special XML response instructing JSF ajax to send a redirect.
                } else {
                    response.sendRedirect(changePwURL); // So, just perform standard synchronous redirect.
                }
            } else if (settingRequest && !isAdmin) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {

                if (!resourceRequest) { // Prevent browser from caching restricted resources. See also https://stackoverflow.com/q/4194207/157882
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    response.setDateHeader("Expires", 0); // Proxies.
                }

                if (chain != null) {
                    chain.doFilter(request, response); // So, just continue request.
                }
            }
        } else if (ajaxRequest) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(AJAX_REDIRECT_XML, loginURL); // So, return special XML response instructing JSF ajax to send a redirect.
        } else {
            response.sendRedirect(loginURL); // So, just perform standard synchronous redirect.
        }
    }

    @Override
    public void destroy() {

    }
}
