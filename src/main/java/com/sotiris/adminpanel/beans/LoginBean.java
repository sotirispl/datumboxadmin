package com.sotiris.adminpanel.beans;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.sotiris.adminpanel.filters.AuthFilter;
import com.sotiris.adminpanel.models.FacebookUser;
import com.sotiris.adminpanel.utils.AppConfig;
import com.sotiris.adminpanel.utils.MongoConfig;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.User;
import facebook4j.conf.Configuration;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private FacebookUser user;
	
	private String getIndexURL(StringBuffer callbackURL) {
		int panel = callbackURL.lastIndexOf("/panel");
		if(panel >= 0) {
			callbackURL.replace(panel, callbackURL.length(), "/");
		}
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index, callbackURL.length(), "").append("/panel/index.jsf");
        return callbackURL.toString();
	}
	
	/*public void authenticate() {
		AuthFilter filter = new AuthFilter();
		
		String appId = filter.getConfig().getInitParameter("FacebookAppId");
		String appSecret = filter.getConfig().getInitParameter("FacebookAppSecret");
		
		Configuration config = AppConfig.getInstance(appId, appSecret).getFbConfig();
		FacebookFactory factory = new FacebookFactory(config);
		Facebook client = factory.getInstance();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		
		request.getSession().setAttribute("facebook", client);
        try {
			context.redirect(client.getOAuthAuthorizationURL(getIndexURL(request.getRequestURL())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
	

	public boolean findUser(String userId) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query query = new Query(Criteria.where("id").is(userId));
		FacebookUser foundUser = mongoOperation.findOne(query, FacebookUser.class);
		if(foundUser == null) {
			return false;
		}
		return true;
	}
	

	@PostConstruct
	public void initUser() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		
		try {
			FacebookUser fbUser = null;
			
			if(request.getSession().getAttribute("user") != null) {
				/*if(findUser((String) request.getSession().getAttribute("user"))) {
					response.sendRedirect(getIndexURL(request.getRequestURL()));
					return;
				}*/
			} else if(request.getParameter("code") != null) {
				Facebook client = (Facebook) request.getSession().getAttribute("facebook");
				String oauthCode = request.getParameter("code");
				client.getOAuthAccessToken(oauthCode);
				User user = client.getMe();
				fbUser = new FacebookUser(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName());
				saveUser(fbUser);
				request.getSession().setAttribute("user", user.getId());
				response.sendRedirect(getIndexURL(request.getRequestURL()));
				return;
			} else {
				HttpSession session = (HttpSession) context.getSession(false);
			    session.invalidate();
			    //authenticate();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession session = (HttpSession) context.getSession(false);
		    session.invalidate();		
		}
	}

	private void saveUser(FacebookUser user) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		
		Query query = new Query(Criteria.where("id").is(user.getId()));
		FacebookUser foundUser = mongoOperation.findOne(query, FacebookUser.class);
		if(foundUser == null) {
			mongoOperation.save(user);
		} else {
			Update update = new Update();
			update.set("email", user.getEmail());
			update.set("firstName", user.getFirstName());
			update.set("lastName", user.getLastName());
			mongoOperation.updateFirst(query, update, FacebookUser.class);
		}
		this.user = user;
	}

	public FacebookUser getUser() {
		return user;
	}

	public void setUser(FacebookUser user) {
		this.user = user;
	}
}
