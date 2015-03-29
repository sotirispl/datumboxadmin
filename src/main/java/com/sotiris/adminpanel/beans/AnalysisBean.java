package com.sotiris.adminpanel.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sotiris.adminpanel.models.Analysis;
import com.sotiris.adminpanel.utils.MongoConfig;

@ManagedBean
@SessionScoped
public class AnalysisBean {

	private List<Analysis> analysis;
	
	public List<Analysis> getAnalysis() {
		return analysis;
	}

	public void setAnalysis(List<Analysis> analysis) {
		this.analysis = analysis;
	}

	public void getAllAnalysis() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		DBCollection analysisDB = mongoOperation.getCollection("analysis");
		DBCursor itr = analysisDB.find();
		analysis = new ArrayList<Analysis>();
		while(itr.hasNext()) {
			Analysis obj = (Analysis) itr.next();
			System.out.println(obj.getText());
			analysis.add(obj);
		}
	}
	
	
}
