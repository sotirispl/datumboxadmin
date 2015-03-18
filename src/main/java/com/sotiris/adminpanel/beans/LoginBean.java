package com.sotiris.adminpanel.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import facebook4j.auth.OAuthAuthorization;
import facebook4j.auth.OAuthSupport;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

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
	
	private Configuration getFbConfig() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		String appId = request.getParameter("FacebookAppId");
		String appSecret = request.getParameter("FacebookAppSecret");
		
		
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		confBuilder.setDebugEnabled(true);
		confBuilder.setOAuthAppId(appId);
		confBuilder.setOAuthAppSecret(appSecret);
		confBuilder.setUseSSL(true);
		confBuilder.setJSONStoreEnabled(true);
		Configuration config = confBuilder.build();
		return config;
	}
	
	public String login() {
		Configuration config = getFbConfig();
		FacebookFactory factory = new FacebookFactory(config);
		Facebook client = factory.getInstance();
		
		try {
			OAuthSupport oAuthSupport = new OAuthAuthorization(config);
			AccessToken token = oAuthSupport.getOAuthAppAccessToken();
			User user = client.getMe();
			name = user.getFirstName() + " " + user.getLastName();
			
		} catch (FacebookException e) {
			e.printStackTrace();
			return "/login.jsf?faces-redirect=true";
		}
		return "/index.jsf?faces-redirect=true";
		
	}
	
}