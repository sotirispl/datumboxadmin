package com.sotiris.adminpanel.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sotiris.adminpanel.utils.AppConfig;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;

import java.io.IOException;
import java.io.Serializable;
 
@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
 
	private static final long serialVersionUID = 1L;
 
	private String name;
 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void authenticate() {
		Configuration config = AppConfig.getInstance().getFbConfig();
		FacebookFactory factory = new FacebookFactory(config);
		Facebook client = factory.getInstance();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		
		request.getSession().setAttribute("facebook", client);
        StringBuffer callbackURL = request.getRequestURL();
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index, callbackURL.length(), "").append("/index.jsf");
        try {
			response.sendRedirect(client.getOAuthAuthorizationURL(callbackURL.toString()));
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	
}