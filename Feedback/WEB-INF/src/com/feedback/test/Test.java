package com.feedback.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.feedback.bl.Service;
import com.feedback.common.EmailUtil;
import com.feedback.common.PrintUtil;
import com.feedback.vo.CampaignVO;
import com.feedback.vo.CountryVO;
import com.feedback.vo.EmpCriteriaVO;
import com.feedback.vo.EmpVO;
import com.feedback.vo.FeedbackResultVO;
import com.feedback.vo.SettingVO;

public class Test {
public static void main(String[] args) {
	
	/*SimpleDateFormat s = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss a");
	SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	try {
		Date d = sf.parse("23-10-2013 00:00:00 AM");
		System.out.println("Before Date:"+d);
		
		System.out.println("After :"+s.format(d));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	
	/*DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT);
	SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat s1 = new SimpleDateFormat("dd/MM/yyyy");
	Date d = null;
	try {
		d = s.parse("2011-10-01 00:00:00.0");
		System.out.println("date :"+d);
		   String date=df.format(d);
		   Date d1 = df.parse(date);
		   System.out.println("String Date "+date);
		   System.out.println("Date 1 "+d1);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}catch (Exception e) {
		// TODO: handle exception
	}*/
	
	try {
		List empList = new ArrayList();
		EmpVO[] empVOs = null;
		Service service = new Service("feedback");
		EmpCriteriaVO ec1 = new EmpCriteriaVO();
		ec1.setRoleId(new Integer(1));
		empList.addAll(new ArrayList(Arrays.asList(service.getEmployees(ec1))));
		EmpCriteriaVO ec2 = new EmpCriteriaVO();
		ec2.setCompId(new Integer(2));
		ec2.setRoleId(new Integer(3));
		empList.addAll(new ArrayList(Arrays.asList(service.getEmployees(ec2))));
		EmpCriteriaVO ec3 = new EmpCriteriaVO();
		ec3.setCompId(new Integer(1));
		ec3.setBranchId(new Integer(4));
		ec3.setRoleId(new Integer(4));
		empList.addAll(new ArrayList(Arrays.asList(service.getEmployees(ec3))));
		System.out.println("list Size :"+empList.size());
	    empVOs = (EmpVO[]) empList.toArray(new EmpVO[empList.size()]);
		System.out.println("Size :"+empVOs.length);
		
		if(empVOs != null && empVOs.length >0){
			for (int i = 0; i < empVOs.length; i++) {
				EmpVO empVO = empVOs[i];
				System.out.println("Email id :"+empVO.getEmailId());
				SettingVO settingVO = service.getSetting();
				settingVO.setEmailTo(empVO.getEmailId());
				FeedbackResultVO feedbackResultVO = service.getFeedback(new Integer(7));
				HashMap hm = new HashMap();
				hm.put("feedbackResultVO",feedbackResultVO);
				String body = PrintUtil.getText("feedbackreport.vm",hm);
				new EmailUtil().sendEmail(settingVO,"Negative Feedback", body);
			}
		}
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
}
}
