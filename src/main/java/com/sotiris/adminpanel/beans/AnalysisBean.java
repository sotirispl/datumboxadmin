package com.sotiris.adminpanel.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

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
	private MongoOperations mongoOperation;
	
	private SelectItem[] modesOptions;
	private SelectItem[] uidsOptions;
	
	public AnalysisBean() {
		analysis = new ArrayList<Analysis>();
	}
	
	public List<Analysis> getAnalysis() {
		return analysis;
	}

	public void setAnalysis(List<Analysis> analysis) {
		this.analysis = analysis;
	}
	
	public SelectItem[] getModesOptions() {
		return modesOptions;
	}
	public void setModesOptions(SelectItem[] modesOptions) {
		this.modesOptions = modesOptions;
	}
	
	public SelectItem[] getUidsOptions() {
		return uidsOptions;
	}
	public void setUidsOptions(SelectItem[] uidsOptions) {
		this.uidsOptions = uidsOptions;
	}
	
	private SelectItem[] createFilterOptions(List<String> data)  {
        SelectItem[] options = new SelectItem[data.size() + 1];

        options[0] = new SelectItem("", "Select");
        
        Iterator<String> itr = data.iterator();
        int i = 1;
        while(itr.hasNext()) {
        	String tmp = itr.next();
        	options[i] = new SelectItem(tmp, tmp);
        	i++;
        }

        return options;
    }
	
	@PostConstruct
	public void getAllAnalysis() {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
		analysis = mongoOperation.findAll(Analysis.class);
		
		ArrayList<String> modeList = new ArrayList<String>();
		ArrayList<String> userList = new ArrayList<String>();
		
		for(Analysis obj : analysis) {

			if(!userList.contains(obj.getUserid())) {
				userList.add(obj.getUserid());
			}
		}
		for(Analysis obj : analysis) {
			if(!modeList.contains(obj.getMode())) {
				modeList.add(obj.getMode());
			}
		}

		modesOptions = createFilterOptions(modeList);
		uidsOptions = createFilterOptions(userList);
	}
}
