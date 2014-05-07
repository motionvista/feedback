package com.feedback.common;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.stream.events.Comment;


public class ObjectUtil {
	public static final HashMap classNameMap = new HashMap();
	
	static{
		classNameMap.put("EmpVO","com.feedback.vo.EmpVO");
		classNameMap.put("RoleVO","com.feedback.vo.RoleVO");
		classNameMap.put("EmpCriteriaVO","com.feedback.vo.EmpCriteriaVO");
		classNameMap.put("CountryVO","com.feedback.vo.CountryVO");
		classNameMap.put("StateVO","com.feedback.vo.StateVO");
		classNameMap.put("CityVO","com.feedback.vo.CityVO");
		classNameMap.put("QuestionVO","com.feedback.vo.QuestionVO");
		classNameMap.put("FormQuestionVO","com.feedback.vo.FormQuestionVO");
		classNameMap.put("FormVO","com.feedback.vo.FormVO");
		classNameMap.put("FormCriteriaVO","com.feedback.vo.FormCriteriaVO");
		classNameMap.put("CompanyVO","com.feedback.vo.CompanyVO");
		classNameMap.put("BranchVO","com.feedback.vo.BranchVO");
		classNameMap.put("BranchCriteriaVO","com.feedback.vo.BranchCriteriaVO");
		classNameMap.put("CampaignVO","com.feedback.vo.CampaignVO");
		classNameMap.put("CampaignCriteriaVO","com.feedback.vo.CampaignCriteriaVO");
		classNameMap.put("CustomerCriteriaVO","com.feedback.vo.CustomerCriteriaVO");
		classNameMap.put("FeedbackCriteriaVO","com.feedback.vo.FeedbackCriteriaVO");
		classNameMap.put("DeviceVO","com.feedback.vo.DeviceVO");
		classNameMap.put("CustCampaignVO","com.feedback.vo.CustCampaignVO");
		classNameMap.put("EmailTemplateVO","com.feedback.vo.EmailTemplateVO");
		classNameMap.put("SmsTemplateVO","com.feedback.vo.SmsTemplateVO");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		HashMap map = new HashMap();
		map.put("UserVO.Id", "12abc");
		map.put("UserVO.FirstName", "Rushi");
		map.put("UserVO.CreateTime", "12/08/1977dd");
		map = toObjects(map);		
		
	}
	
	public static HashMap toObjects(HashMap inputMap)throws Exception{
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");;
		HashMap objMap = new HashMap();
		Iterator itr = inputMap.keySet().iterator();
		while(itr.hasNext()){
			String key = (String)itr.next();
			String val = (String)inputMap.get(key);
			if(key.indexOf(".") == -1){
				continue;
			}
			
			String className = key.substring(0,key.indexOf(".")).trim();
			String fieldName = key.substring(key.indexOf(".")+1).trim();
			String start = ""+fieldName.charAt(0);
			start = start.toLowerCase();
			fieldName = fieldName.substring(1);
			fieldName = start+fieldName;
			
			Object obj = objMap.get("current"+className);
			if(obj == null){
				String fullClassName = (String)classNameMap.get(className);
				if(fullClassName == null){
					System.out.println("Error - Please enter full class name in ObjectUtil.java for "+className);
				}
				obj = Class.forName(fullClassName).newInstance();
				objMap.put("current"+className,obj);
			}
						
			Field field = obj.getClass().getField(fieldName);
			field.setAccessible(true);
			String type = field.getType().getName();
			try{
				if(type.equals("java.lang.String")){
					field.set(obj, val);
				}else if(type.equals("java.lang.Integer")){
					field.set(obj, new Integer(val));
				}else if(type.equals("java.lang.Long")){
					field.set(obj, new Long(val));
				}else if(type.equals("java.lang.Float")){
					field.set(obj, new Float(val));
				}else if(type.equals("java.lang.Double")){
					field.set(obj, new Double(val));
				}else if(type.equals("java.util.Date")){				
					field.set(obj, dateFormatter.parse(val));
				}
			}catch(Exception ex){
				System.out.println("Error for "+key+"-"+ex.getMessage());
				
			}
			
		}
		
		return objMap;
	}
	

}
