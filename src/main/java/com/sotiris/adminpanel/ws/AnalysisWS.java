package com.sotiris.adminpanel.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import com.sotiris.adminpanel.models.Analysis;
import com.sotiris.adminpanel.services.AnalysisService;

@WebService(serviceName="AnalysisWS")
@SOAPBinding(style = Style.RPC, use = Use.LITERAL)
public class AnalysisWS {

	AnalysisService analysisService;
	
	@WebMethod(exclude=true)
	public void setAnalysisService(AnalysisService analysisService) {
		this.analysisService = analysisService;
	}
	
	@WebMethod(operationName="storeAnalysisData")
	public boolean storeAnalysisData(Analysis analysis) {
		return analysisService.storeAnalysisData(analysis);
	}
	
}
