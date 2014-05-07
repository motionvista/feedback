<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentEmpVO" class="com.feedback.vo.EmpVO" scope="request"/>
<jsp:useBean id="currentRoleVO" class="com.feedback.vo.RoleVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
		
	List permissionList = Arrays.asList(PermissionVO.getPermissions());
	List rolePermissionList = (List)request.getAttribute("rolePermissionList");
	
	RoleVO[] roleList = RoleVO.getRoles();
		
	String selectedRoleId =request.getParameter("RoleId");
	if(selectedRoleId == null){
		selectedRoleId = "";
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
%>

<SCRIPT language="JavaScript">
function submitForm(){
	document.SelectRole.submit();
}
</SCRIPT>
<script type="text/javascript">
function validate(){
   if (document.SelectRole.role.selectedIndex == 0) {
       alert('Please Select Role!!')
       document.SelectRole.role.focus();
        return false;
    }
 }
</script>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
        <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>
<table border="0" cellspacing="0" width="40%">
    <tr>
      <td width="100%" align="center">
     
	 <form name="SelectRole" method="POST" action="Control.do?Action=GoToSetPermissions">
	  <input type="hidden" name="RoleVO.Id" value="<%=selectedRoleId%>" />

     
      <fieldset><legend>&nbsp;Set Permission&nbsp; </legend>
	  <table border="0" cellspacing="0" width="50%">
        <tr>
          <td width="69%" align="left">Role</td>
          <td width="50%" align="left">
       	 
		  <select size="1" name="RoleId" id="role" onChange="submitForm()">
       	   <option value="">--Select--</option>
          
		 <%for(int i=0;i<roleList.length;i++){ 
	   		  String selected = "";
	   		  if(roleList[i].getId().toString().equals(selectedRoleId)){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=roleList[i].getId()%>" <%=selected%>><%=roleList[i].getName()%></option>
			<%}%>
			</select>
			
			
          </td>
        </tr>      
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <form name="setPermission" method="POST" action="Control.do?Action=SetPermissions" onsubmit="return validate();">
 <input type="hidden" name="RoleId" value="<%=selectedRoleId%>" />
 
 <table border="0" cellspacing="1"  width="50%" bgcolor="<%=darkColor%>">
        <tr>
          <td align="center" width="10%">Action</td>
          <td align="center" width="30%">Permissions</td>
        </tr>
       <%for(int i=0;i<permissionList.size();i++){ 
   		  PermissionVO permission = (PermissionVO)permissionList.get(i);    
   		  String checked = "";
   		  if(rolePermissionList != null){
	   		  if(rolePermissionList.contains(permission.getId())){
		   		  checked = "checked";
	   		  }
   		  }
		 %>
        <tr>
          <td align="center" width="10%" bgcolor="#FFFFFF"><input type="checkbox" name="Permission" value="<%=permission.getId()%>" <%=checked%> ></td>
          <td align="center" width="30%" bgcolor="#FFFFFF"><%=permission.getName()%></td>         
        </tr>
	<%}%>
	<tr>	
	<td width="50%" colspan="2" bgcolor="#FFFFFF" align="right"><input type="submit" value="Set Permissions" name="B1"></td>
	</tr>
   </form> 
 </table>
 