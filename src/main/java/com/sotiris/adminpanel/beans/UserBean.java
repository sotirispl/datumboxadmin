package com.sotiris.adminpanel.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import facebook4j.Facebook;
import facebook4j.User;

import java.io.IOException;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String name;
	
	@PostConstruct
	public void initUser() throws IOException {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		
		try {
			Facebook client = (Facebook) request.getSession().getAttribute("facebook");
			String oauthCode = request.getParameter("code");
			client.getOAuthAccessToken(oauthCode);
			User user = client.getMe();
			name = user.getFirstName() + " " + user.getLastName();
			
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession session = (HttpSession) context.getSession(false);
		    session.invalidate();
		    context.redirect("/login.jsf?faces-redirect=true");
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
