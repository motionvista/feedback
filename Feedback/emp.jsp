<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentEmpCriteriaVO" class="com.feedback.vo.EmpCriteriaVO" scope="request"/>

<%
   
    String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();

    List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
	}
	
	List branchList = (List)request.getAttribute("branchList");
	if(branchList == null){
		branchList = new ArrayList();
	}

    List roleList= (List)request.getAttribute("roleList");
	if(roleList== null){
		roleList= new ArrayList();
	}
	
	List empList = (List)request.getAttribute("empList");
	if(empList == null){
		empList = new ArrayList();
	}
	
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
%>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<div align="center">

<table border="0" width="65%">
<form name="SearchEmp" method="POST" action="Control.do?Action=SearchEmp">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New Employee" name="createNew" onClick="form.action='Control.do?Action=GoToCreateEmp';">
      </td>
      </tr> 

    <tr>
      <td width="100%" align="center">
      
      <fieldset><legend>&nbsp;Search Employee &nbsp; </legend>
      <table border="0" width="85%">
            
       <tr>
       <td width="20%"> Employee Name :</td>
       <td width="30%"><input type="text" name="EmpCriteriaVO.FirstName" value="<c:out value="${currentEmpCriteriaVO.firstName}" />" size="20"></td>
    
        <td>Role :</td>
        <td width="32%" align="left">
          <select size="1" name="EmpCriteriaVO.RoleId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<roleList.size();i++){ 
	   		 RoleVO roleVO = (RoleVO)roleList.get(i); 
	   		  String selected = "";
	   		  if(roleVO.getId().equals(currentEmpCriteriaVO.getRoleId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=roleVO.getId()%>" <%=selected%>><%=roleVO.getName()%></option>
			<%}%>
			</select>
          </td>
       </tr>  
       <tr>
       </tr> 
       <tr>
       
       <td>Company :</td>
      <td width="32%" align="left">
          <select size="1" name="EmpCriteriaVO.CompId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		 CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentEmpCriteriaVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
          </td>
      <td>Branch :</td>
      <td>
           <select size="1" name="EmpCriteriaVO.BranchId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentEmpCriteriaVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
			<%}%>
			</select>
       </td>
       
       </tr> 
        <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Search Employee" name="B1">
      </tr>
     </table>
     </fieldset>
     </form>
</table>	
</div>




<br>
 <%if(empList != null){%>
 <table border="0" cellspacing="1"  width="80%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="6" bgcolor="#FFFFFF" align="left"> <%=empList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
        <td align="center">Sr No</td>
		<td align="center">Name</td>
    	<td align="center">Company</td>
    	<td align="center">Branch</td>
    	<td align="center">Role</td>
		<td align="center">Action</td>
          
        </tr>
       <% for(int i=0,j=1 ;i<empList.size(); i++,j++){ 
	     EmpVO empVO = (EmpVO)empList.get(i);
	   %>
        <tr>
         <td  bgcolor="#FFFFFF" align="center"><%=j%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=empVO.getFirstName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=empVO.getCompanyName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=empVO.getBranchName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=empVO.getRoleName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><a href="Control.do?Action=EditEmp&EmpVO.EmpId=<%=empVO.getEmpId()%>">Edit</a></td>			
        </tr>
		<%}%>
     </table>
<% } %>

<br>



