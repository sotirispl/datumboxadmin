package com.sotiris.adminpanel.services;

import javax.jws.WebService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;

import com.sotiris.adminpanel.models.Analysis;
import com.sotiris.adminpanel.utils.MongoConfig;

@WebService(endpointInterface="com.sotiris.adminpanel.services.AnalysisService")
public class AnalysisServiceImpl implements AnalysisService {

	@Override
	public boolean storeAnalysisData(Analysis analysis) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		System.out.println(analysis.getText());
		mongoOperation.save(analysis);
		return true;		
	}

}