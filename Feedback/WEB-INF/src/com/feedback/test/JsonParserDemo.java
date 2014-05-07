package com.feedback.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.feedback.vo.CustomerVO;
import com.feedback.vo.FeedbackQuestionAnsVO;
import com.feedback.vo.FeedbackVO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class JsonParserDemo {
	      private String jsonSource;
		  private boolean sourceFromFile;
		  CustomerVO cust =null;
		  public JsonParserDemo(){
			 
		  }
	
	public static void main(String[] args) {
		
		  JsonParserDemo jsonParserDemo = new JsonParserDemo();
		  CustomerVO  cust = new CustomerVO();
		  cust.setCustName("Rushikesh");
		  cust.setMobileNo("9898989898");
		  cust.setEmailId("rb@gmail.com");
		  cust.setDob(new Date());
		  cust.setMaritialStatus("Married");
		  cust.setCompId(new Integer(1));
		  cust.setOccupation("ER");
		  cust.setLocation("K N");
		 
		  String json = new Gson().toJson(cust);
		  System.out.println(json);
		  
		  Gson gson = new Gson();
		  JsonParser parser = new JsonParser();
		  JsonElement element =  parser.parse(json).getAsJsonObject();
		  CustomerVO c = (CustomerVO) gson.fromJson(element, CustomerVO.class);
		  System.out.println("Name :"+c.getCustName());
		  System.out.println("Mobile No :"+c.getMobileNo());
		  System.out.println("e id :"+c.getEmailId());
		  System.out.println("DOB :"+c.getDob());
		  System.out.println("Mart status :"+c.getMaritialStatus());
		  System.out.println("Comp Id :"+c.getCompId());
		  System.out.println("Occupation :"+c.getOccupation());
		  System.out.println("location :"+c.getLocation());
		   
		  FeedbackVO feedbackVO = new FeedbackVO();
		  feedbackVO.setCompId(new Integer(1));
		  feedbackVO.setBranchId(new Integer(1));
		  feedbackVO.setComment("Good quality..");
		  List qa = new ArrayList();
		  FeedbackQuestionAnsVO f1 = new FeedbackQuestionAnsVO();
		  f1.setQuestionId(new Integer(1));
		  f1.setQuestion("Rest Ambience");
		  f1.setAns(new Integer(2));
			
		  FeedbackQuestionAnsVO f2 = new FeedbackQuestionAnsVO();
		  f2.setQuestionId(new Integer(2));
		  f2.setQuestion("Food Quality");
		  f2.setAns(new Integer(3));
			
		 FeedbackQuestionAnsVO f3 = new FeedbackQuestionAnsVO();
		 f3.setQuestionId(new Integer(3));
		 f3.setQuestion("Service");
		 f3.setAns(new Integer(4));
			
		 qa.add(f1);
		 qa.add(f2);
		 qa.add(f3);
		
		 feedbackVO.setQuestionAns((FeedbackQuestionAnsVO[]) qa.toArray(new FeedbackQuestionAnsVO[qa.size()]));
			
		 String json1 = new Gson().toJson(feedbackVO);
		 System.out.println(json1);
			  
			  
			  
			  Gson gson1 = new Gson();
			  JsonParser parser1 = new JsonParser();
			  JsonElement element1 =  parser1.parse(json1).getAsJsonObject();
			  FeedbackVO f = (FeedbackVO) gson1.fromJson(element1, FeedbackVO.class);
			  System.out.println("Comp ID :"+f.getCompId());
			  System.out.println("Branch ID :"+f.getBranchId());
			 
			  FeedbackQuestionAnsVO [] fq = f.getQuestionAns();
			  
			  for (int i = 0; i < fq.length; i++) {
				FeedbackQuestionAnsVO fqa = (FeedbackQuestionAnsVO)fq[i];
				System.out.println("que id : "+fqa.getQuestionId());
				System.out.println("que : "+fqa.getQuestion());
				System.out.println("ans : "+fqa.getAns());
			  }	
			  
			  System.out.println("Comment  : "+f.getComment());
		  
	}
}
