package com.sotiris.adminpanel.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

public class AppConfig {

	protected static AppConfig instance;
	private static Configuration config;
	private static String appId;
	private static String appSecret;
	
	public String getAppId() {
		return appId;
	}

	public static void setAppId(String id) {
		appId = id;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public static void setAppSecret(String secret) {
		appSecret = secret;
	}

	
	private AppConfig() {
		System.out.println(appId+" "+appSecret);
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		confBuilder.setDebugEnabled(true);
		confBuilder.setOAuthAppId(appId);
		confBuilder.setOAuthAppSecret(appSecret);
		confBuilder.setUseSSL(true);
		confBuilder.setOAuthPermissions("public_profile, email");
		confBuilder.setJSONStoreEnabled(true);
		config = confBuilder.build();
	}
	
	public static AppConfig getInstance(String id, String secret) {
		setAppId(id);
		setAppSecret(secret);
		if(instance == null) {
			instance = new AppConfig();
		}
		return instance;
	}
	
	public Configuration getFbConfig() {
		return config;
	}
}
