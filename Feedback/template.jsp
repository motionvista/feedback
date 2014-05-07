<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>

<%	
     String darkColor = ApplicationContext.getColor1();
     String lightColor = ApplicationContext.getColor2();
	 String VERSION = com.feedback.bl.Service.VERSION;
     String BUILD = com.feedback.bl.Service.BUILD;
      
     EmpVO currentEmpVO = ApplicationContext.getApplicationUser(request);	    	 
     	 
	 String viewJsp = (String)request.getAttribute("currentView");
	 if(viewJsp == null){
		 viewJsp = "login.jsp";
	 }

	 List masterList = new ArrayList(); //A
	 List feedbackList = new ArrayList(); //B
 	 List customerList = new ArrayList(); //C
 	 List analyticsList = new ArrayList(); //D
  	 
	 
  	 HashMap menuMap = new HashMap(); 
  	 menuMap.put("A",masterList);
	 menuMap.put("B",feedbackList);
 	 menuMap.put("C",customerList);
 	 menuMap.put("D",analyticsList);
  	 
   	
	 HashMap colorMap = new HashMap();
	 colorMap.put("A",lightColor);
	 colorMap.put("B",lightColor);
	 colorMap.put("C",lightColor);
 	 colorMap.put("D",lightColor);
	 
 	 
 	 if(ApplicationContext.isAccessible(request,PermissionVO.COMPANY)){
	 	masterList.add(new MenuItemVO("A1","Company","Control.do?Action=GoToCompany"));
	 }
 	 if(ApplicationContext.isAccessible(request,PermissionVO.BRANCH)){
	 	masterList.add(new MenuItemVO("A2","Branch","Control.do?Action=GoToBranch"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.USER)){
	 	masterList.add(new MenuItemVO("A3","Employee","Control.do?Action=GoToEmp"));
	 }
	
	 if(ApplicationContext.isAccessible(request,PermissionVO.QUESTION)){
	 	masterList.add(new MenuItemVO("A4","Question","Control.do?Action=GoToQuestion"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.FORM)){
	 	masterList.add(new MenuItemVO("A5","Feedback Form","Control.do?Action=GoToForm"));
	 }
	  if(ApplicationContext.isAccessible(request,PermissionVO.EMAIL_TEMPLATE)){
	 	masterList.add(new MenuItemVO("A6","Email Template","Control.do?Action=GoToEmailTemplate"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.SMS_TEMPLATE)){
	 	masterList.add(new MenuItemVO("A7","Sms Template","Control.do?Action=GoToSmsTemplate"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.CAMPAIGN)){
	 	masterList.add(new MenuItemVO("A8","Campaign","Control.do?Action=GoToCampaign"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.DEVICE)){
	 	masterList.add(new MenuItemVO("A9","Register Device","Control.do?Action=GoToDevice"));
	 }
     if(ApplicationContext.isAccessible(request,PermissionVO.COUNTRY)){
	 	masterList.add(new MenuItemVO("A10","Country","Control.do?Action=GoToCountry"));
	 }	
	 if(ApplicationContext.isAccessible(request,PermissionVO.STATE)){
	 	masterList.add(new MenuItemVO("A11","State","Control.do?Action=GoToState"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.CITY)){
	 	masterList.add(new MenuItemVO("A12","City","Control.do?Action=GoToCity"));
	 }
	 if(ApplicationContext.isAccessible(request,PermissionVO.SET_PERMISSION)){
	 	masterList.add(new MenuItemVO("A13","Set Permissions","Control.do?Action=GoToSetPermissions"));
	 } 
	 if(currentEmpVO != null){
	 	 masterList.add(new MenuItemVO("A14","Change Password","Control.do?Action=GoToChangePassword"));
	 }
	 
	 if(ApplicationContext.isAccessible(request,PermissionVO.SEARCH_FEEDBACK_REPORTS)){
	 	feedbackList.add(new MenuItemVO("B1","Search","Control.do?Action=GoToReports"));
	 }
	 
	 if(ApplicationContext.isAccessible(request,PermissionVO.SEARCH_CUSTOMER)){
	 	customerList.add(new MenuItemVO("C1","Search Customer","Control.do?Action=GoToCustomer"));
	 }
	 
	 if(ApplicationContext.isAccessible(request,PermissionVO.CUSTOMER_CAMPAIGN)){
	 	customerList.add(new MenuItemVO("C2","Customer Campaign","Control.do?Action=GoToCustomerCampaign"));
	 }
	  	 
	 String mId = request.getParameter("MenuId");
     
	 if(mId != null){
		 ApplicationContext.setMenuId(request,mId);
	 }
	 
	 mId = ApplicationContext.getMenuId(request);
	 if(mId == null){
		 mId = "A";
	 }	 
	 String topMenuId = mId.substring(0,1);
	 colorMap.put(topMenuId,darkColor);
	 
 	 List subMenuList = (List)menuMap.get(topMenuId);
	 if(subMenuList == null){
		 subMenuList = new ArrayList();
	 }
	
	 String firstName = "Guest";
 	 String lastName = "";
  	 String role = "";
	 if(currentEmpVO != null){ 
		firstName = currentEmpVO.getFirstName();
		lastName = currentEmpVO.getLastName();
		role = currentEmpVO.getRoleName();
     }	 
	 
%>

<html>
<head>
<title>Feedback</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta name="description" content="Electronic Answer Sheet.India's No 1 quick result platform" />
<meta name="keywords" content="Quick Results" />
<style type="text/css">
<!--
.DarkColor {background-color:<%=darkColor%>;}
.LightColor {background-color:<%=lightColor%>;}

.styleFont11 {	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 15px;
}

.styleMenuItem {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 15px; color: #75B8D5; }

a:link {
	color: #000000;
	text-decoration: none;
}
a:visited {
	text-decoration: none;
	color: #000000;
}
a:hover {
	text-decoration: none;
}
a:active {
	text-decoration: none;
}
-->
</style>

<SCRIPT type="text/javascript">
window.history.forward();
function noBack() { window.history.forward(); }
</SCRIPT>

</head>

<body onload="noBack();">

<table width="100%"  border="0">
  <tr>
    <td valign="top">
    <table width="100%"  border="0">
      <tr>
        	<td align="left"width="25%" >
        	<b><font size="5" color="#FF0000">Feedback<br>
            </font></b><font size="1" color="#FF0000">&nbsp;Vr - <%=VERSION%> - <%=BUILD%></font>
        	</td>
        	<td align="center" width="50%" ></td>        	
        	<td align="right" width="25%" >
			
      </tr>
      <tr>
      <%if(currentEmpVO != null){%>
          <td colspan="3" align="right">Welcome <b><%=firstName%>&nbsp;<%=lastName%></b>&nbsp;<a href="Control.do?Action=Logout"><b><font color="#FF0000">Logout</font></b>&nbsp;</a></td>
       <%}else{%>
          <td colspan="3" align="right">Welcome Guest</td>
       <%}%>
      </tr>
    </table>    
      <table width="100%"  border="0" cellspacing="1">
     <tr>
		<%if(currentEmpVO != null){%>         		
			<td align="center" bgcolor="<%=colorMap.get("A")%>" height="25" width="9%"><a href="Control.do?Action=GoToDefault&MenuId=A"><b><font face="Times New Roman" size="4">Master</font></b></a></td>
		<%}%>		
		<%if(currentEmpVO != null && ApplicationContext.isAccessible(request,PermissionVO.FEEDBACK_REPORTS)){%>         		
			<td align="center" bgcolor="<%=colorMap.get("B")%>" height="25" width="9%"><a href="Control.do?Action=GoToDefault&MenuId=B"><b><font face="Times New Roman" size="4">Feedback Reports</font></b></a></td>
		<%}%>				
		<%if(currentEmpVO != null && ApplicationContext.isAccessible(request,PermissionVO.CUSTOMER)){%>         		
			<td align="center" bgcolor="<%=colorMap.get("C")%>" height="25" width="9%"><a href="Control.do?Action=GoToDefault&MenuId=C"><b><font face="Times New Roman" size="4">Customer</font></b></a></td>
		<%}%>	
		
		<%if(currentEmpVO != null && ApplicationContext.isAccessible(request,PermissionVO.ANALYTICS)){%>         		
			<td align="center" bgcolor="<%=colorMap.get("D")%>" height="25" width="9%"><a href="Control.do?Action=GoToAnalytics&MenuId=D"><b><font face="Times New Roman" size="4">Analytics</font></b></a></td>
		<%}%>	
		
		<td align="center" bgcolor="#FFFFFF" height="25" width="9%">&nbsp;</td>	
	  </tr>        
      <tr valign="top" bgcolor="#FFFFFF">
          <td colspan="14" align="center" height="8" width="100%"><img src="images/bar.gif"  height="7"></td>
       </tr>
      </table>
      <table width="100%"  border="0">
        <tr>
        <%if(subMenuList.size() > 0){%>
          <td width="17%" height="513" align="center" valign="top" bgcolor="#FFFFFF"><br>			
			  <table width="87%"  border="0" align="center" cellpadding="0" cellspacing="1" bordercolor="<%= darkColor %>" bgcolor="<%= darkColor %>">
			      <tr>
					<td height="8" align="center" valign="middle" bgcolor="<%=darkColor%>"><img src="images/bar.gif" width="140" height="7"></td>
			      </tr>
				<% for(int i=0;i<subMenuList.size();i++){ 
					MenuItemVO menu = (MenuItemVO)subMenuList.get(i);
					String bgColor = "#FFFFFF";
					if(mId.equals(menu.getId())){
						bgColor = ApplicationContext.getColor2();
					}
				%>
			      <tr>
					<td height="18" align="center" valign="middle" bgcolor="<%=bgColor%>">
					<span class="styleMenuItem"><a href="<%=menu.getUrl()%>"><%=menu.getName()%></a></span></td>
			      </tr>
				<%}%>      
			  </table>			  
            <p><br>
              <br>
              <br>
          </p></td>
          <%}%>
          <td width="83%" align="center" valign="top">
        	 <jsp:include page="<%=viewJsp%>"/>                    
          </td>
        </tr>
      </table>
      <table width="100%"  border="0">
        <tr>
          <td align="center" valign="top"><p><img src="images/line.gif"  height="2"><br>
            <span class="styleFont11">Copyright &copy; 2013 Excell Technology Ventures Pvt.Ltd. All Rights Reserved </span></p>
          </td>
        </tr>
    </table>      <p>&nbsp;</p></td>
  </tr>
</table>
</body>
</html>