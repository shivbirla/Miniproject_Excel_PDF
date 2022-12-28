package com.nt.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.nt.request.SearchRequest;
import com.nt.response.SearchResponse;

public interface IReportService {

	public List<String>  getUniquePlanNames();
	
	public List<String> getUniquePlanStatus();
	
	public List<SearchResponse> getData(SearchRequest request);
	
	public void generateExcel(HttpServletResponse response) throws Exception;
	
	public void generatePdf(HttpServletResponse response) throws Exception;
}
 