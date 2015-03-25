package com.sotiris.adminpanel.filters;

import java.io.IOException;

import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.sotiris.adminpanel.beans.LoginBean;
import com.sotiris.adminpanel.utils.AppConfig;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

public class AuthFilter implements Filter{

	private LoginBean loginBean;
	
	private FilterConfig filterConfig;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	private String getIndexURL(StringBuffer callbackURL) {
		int panel = callbackURL.lastIndexOf("/panel");
		if(panel >= 0) {
			callbackURL.replace(panel, callbackURL.length(), "/");
		}
        int index = callbackURL.lastIndexOf("/");
        callbackURL.replace(index, callbackURL.length(), "").append("/panel/index.jsf");
        return callbackURL.toString();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// Get the loginBean from session attribute
		//LoginBean loginBean = (LoginBean)((HttpServletRequest)request).getSession().getAttribute("loginBean");
		
		// For the first application request there is no loginBean in the session so user needs to log in
		// For other requests loginBean is present but we need to check if user has logged in successfully
		HttpSession session = ((HttpServletRequest) request).getSession(false);
		String user = (session != null) ? (String) session.getAttribute("user") : null;
		
		if(user == null && ((HttpServletRequest) request).getParameter("code") == null) {
			String appId = filterConfig.getInitParameter("FacebookAppId");
			String appSecret = filterConfig.getInitParameter("FacebookAppSecret");
			Configuration config = AppConfig.getInstance(appId, appSecret).getFbConfig();
			FacebookFactory factory = new FacebookFactory(config);
			Facebook client = factory.getInstance();
			
			((HttpServletRequest) request).getSession().setAttribute("facebook", client);
	        try {
				((HttpServletResponse) response).sendRedirect(client.getOAuthAuthorizationURL(getIndexURL(((HttpServletRequest) request).getRequestURL())));
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return;
			
			//loginBean.authenticate();
			//String contextPath = ((HttpServletRequest)request).getContextPath();
			//((HttpServletResponse)response).sendRedirect(contextPath.substring(0, contextPath.lastIndexOf("/")) + "/login.jsf");
		} 
		
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

	public FilterConfig getConfig() {
		return this.filterConfig;
	}

}
