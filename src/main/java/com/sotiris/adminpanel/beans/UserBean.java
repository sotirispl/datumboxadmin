package com.sotiris.adminpanel.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.sotiris.adminpanel.models.FacebookUser;
import com.sotiris.adminpanel.utils.MongoConfig;

import facebook4j.Facebook;
import facebook4j.User;

import java.io.Serializable;

@ManagedBean
@ViewScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private FacebookUser user;
	
	public UserBean() {
		//initUser();
	}
	
	
	private boolean findUser(String userId) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		Query query = new Query(Criteria.where("id").is(userId));
		FacebookUser foundUser = mongoOperation.findOne(query, FacebookUser.class);
		if(foundUser == null) {
			return false;
		}
		this.user = foundUser;
		return true;
	}
	
	
	
	
}
