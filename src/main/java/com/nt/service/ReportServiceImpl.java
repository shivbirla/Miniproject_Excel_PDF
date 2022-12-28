package com.nt.service;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nt.entity.EligibilityDetails;
import com.nt.repo.EligibilityDetailsRepo;
import com.nt.request.SearchRequest;
import com.nt.response.SearchResponse;

@Service
public class ReportServiceImpl implements IReportService {

	@Autowired
	private EligibilityDetailsRepo eligibilityRepo;

	@Override
	public List<String> getUniquePlanNames() {
		List<String> findPlanNames = eligibilityRepo.findPlanNames();
		return findPlanNames;
	}

	@Override
	public List<String> getUniquePlanStatus() {
		List<String> findPlanStatus = eligibilityRepo.findPlanStatus();
		return findPlanStatus;
	}

	@Override
	public List<SearchResponse> getData(SearchRequest request) {
		
		List<SearchResponse> responses = new ArrayList<>();
		
		EligibilityDetails queryBuilder = new EligibilityDetails();
		
		String planName = request.getPlanName();		
		
		if(planName!=null && !planName.equals("")) {
			queryBuilder.setPlanName(planName);
		}
		
		String planStatus = request.getPlanStatus();
		if(planStatus!=null && !planStatus.equals("")) {
			queryBuilder.setPlanStatus(planStatus);
		}
		
		LocalDate planStartDate = request.getPlanStartDate();
		if(planStartDate!=null) {
			queryBuilder.setPlanStart_date(planStartDate);
		}
		
		LocalDate planEndDate = request.getPlanEndDate();
		if(planEndDate!=null) {
			queryBuilder.setPlanStart_date(planEndDate);
		}
		
         Example<EligibilityDetails> example = Example.of(queryBuilder);

		List<EligibilityDetails> entities = eligibilityRepo.findAll(example);

		for (EligibilityDetails entity : entities) {
			SearchResponse sr = new SearchResponse();
			BeanUtils.copyProperties(entity, sr);//source object,destination obj
			responses.add(sr);
		}
		return responses;
	}

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {
         List<EligibilityDetails> entities = eligibilityRepo.findAll();
         
         HSSFWorkbook workbook = new HSSFWorkbook();
         HSSFSheet sheet = workbook.createSheet();
         HSSFRow headeRow = sheet.createRow(0);
         
         headeRow.createCell(0).setCellValue("Name");
         headeRow.createCell(1).setCellValue("Email");
         headeRow.createCell(2).setCellValue("Mobile");
         headeRow.createCell(3).setCellValue("Gender");
         headeRow.createCell(4).setCellValue("SSN");
         
         int i =1;
         for(EligibilityDetails entity : entities) {       
        	 HSSFRow dataRow = sheet.createRow(i);
        	 dataRow.createCell(0).setCellValue(entity.getName());
        	 dataRow.createCell(1).setCellValue(entity.getEmail());
        	 dataRow.createCell(2).setCellValue(entity.getMobile());
        	 dataRow.createCell(3).setCellValue(String.valueOf(entity.getGender()));
        	 dataRow.createCell(4).setCellValue(entity.getSsn());
        	i++;
         }
         ServletOutputStream outputStream = response.getOutputStream();
	   workbook.write(outputStream);
	   workbook.close();
	   outputStream.close();
	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
               List<EligibilityDetails> entities  = eligibilityRepo.findAll();
               Document document = new Document(PageSize.A4);
               
               PdfWriter.getInstance(document, response.getOutputStream());
	     
               document.open();
               Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
               font.setSize(18);
               font.setColor(Color.RED);
               
               Paragraph p = new Paragraph("Search Report",font);
               p.setAlignment(Paragraph.ALIGN_CENTER);
               
               document.add(p);
               
               PdfPTable table =  new PdfPTable(5);
               table.setWidthPercentage(100f);
               table.setWidths(new float[] {1.5f,3.5f,3.0f,1.5f,3.5f});
               table.setSpacingBefore(10);
               
                PdfPCell cell = new PdfPCell();
               cell.setBackgroundColor(Color.DARK_GRAY);
               cell.setPadding(5);
               
               font = FontFactory.getFont(FontFactory.HELVETICA);
               font.setColor(Color.white);
               
               cell.setPhrase(new Phrase("Name",font));
               table.addCell(cell);
               
               cell.setPhrase(new Phrase("E-mail",font));
               table.addCell(cell);
               
               cell.setPhrase(new Phrase("phon",font));
               table.addCell(cell);
               
               cell.setPhrase(new Phrase("Gender",font));
               table.addCell(cell);
               
               cell.setPhrase(new Phrase("SSN",font));
               table.addCell(cell);
               
               for(EligibilityDetails entity:entities) {
            	   table.addCell(entity.getName());
            	   table.addCell(entity.getEmail());
            	   table.addCell(String.valueOf(entity.getMobile()));
            	   table.addCell(String.valueOf(entity.getGender()));
            	   table.addCell(String.valueOf(entity.getSsn()));
               }
               
               document.add(table);
               document.close();
	      }
	}


