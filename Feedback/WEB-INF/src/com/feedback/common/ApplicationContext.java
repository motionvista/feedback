package com.feedback.common;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.feedback.vo.PermissionVO;
import com.feedback.vo.EmpVO;

/**
 *
 */
public class ApplicationContext {	
	public static final String LOGIN_EMP = "LoginEmp";
	public static final String MENU_ID = "MenuId";

	public static String getApplicationUserName(HttpServletRequest request) {
		EmpVO emp = getApplicationUser(request);
		String name = "Guest";
		if(emp != null){
			name = emp.getFirstName();
		}
		return name;
	}

	public static EmpVO getApplicationUser(HttpServletRequest request) {
		return (EmpVO) request.getSession().getAttribute(LOGIN_EMP);
	}

	public static void setApplicationUser(HttpServletRequest request, EmpVO emp) {
		request.getSession().setAttribute(LOGIN_EMP, emp);
	}

	public static String getMenuId(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(MENU_ID);
	}

	public static void setMenuId(HttpServletRequest request, String menuId) {
		request.getSession().setAttribute(MENU_ID, menuId);
	}

	public static String getColor1(){
		return "#75B8D5";
	}
	public static String getColor2(){
		return "#CCCCCC";
	}
	public static String getColor3(){
		return "#CC3300";
	}
	
	public static boolean isAccessible(HttpServletRequest request, PermissionVO permissionVO){
		EmpVO currentSystemEmp = getApplicationUser(request);
		boolean flag = false;
		if(currentSystemEmp != null){
			List list = currentSystemEmp.getPermissions();

			for(int i=0; i<list.size(); i++){
				if(permissionVO.getId().equals((Integer)list.get(i))){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public static int getPageSize(){
		int size = 25; //default
		return size;
	}
}