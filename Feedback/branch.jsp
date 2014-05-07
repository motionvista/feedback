<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentBranchCriteriaVO" class="com.feedback.vo.BranchCriteriaVO" scope="request"/>

<%
    String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
   
	List branchList = (List)request.getAttribute("branchList");
	if(branchList == null){
		branchList = new ArrayList();
	}
	
	System.out.println("List Size ="+branchList.size() );
				
	List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
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
<form name="SearchBranch" method="POST" action="Control.do?Action=SearchBranch">
<tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New Branch" name="createNew" onClick="form.action='Control.do?Action=GoToCreateBranch';">
      </td>
      </tr> 

       <tr>
      <td width="100%" align="center">
      
      <fieldset><legend>&nbsp;Search Branch &nbsp; </legend>
      <table  border="0">
            
       <tr>
       <td width="39%"> Company Name :</td>
       <td width="31%"  width="85%">
           <select size="1" name="BranchCriteriaVO.CompId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<companyList.size();i++){ 
			   	       CompanyVO companyVO  = (CompanyVO)companyList.get(i); 
			   	        String selectStr = "";
				        if(companyVO.getCompId().equals(currentBranchCriteriaVO.getCompId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=companyVO.getCompId()%>" <%=selectStr%> ><%=companyVO.getName()%></option>
				     <%}%>
			</select>
       </td>
       <td width="10%">
       </td>
       <td width="30%">
       <input type="submit" value="Search Branch" name="B1">
       </td>
       </tr>  
       <tr>
       </tr>  
        
     </table>
     </fieldset>
     </form>
</table>	
</div>

<br>
 <% if(branchList!= null){ %>
 <table border="0" cellspacing="1"  width="80%" bgcolor="<%=darkColor%>">
 <tr>
  <td colspan="6" bgcolor="#FFFFFF" align="left"> <%=branchList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">Branch Name</td>
          <td align="center">Company</td>
          <td align="center">Address</td>
      	  <td align="center">Contact No</td>
          <td align="center">Action</td>
          
        </tr>
        <%
        for(int i=0,j=1;i<branchList.size();i++,j++){  
   		  BranchVO branchVO = (BranchVO)branchList.get(i);
		%>
        <tr>
          <td bgcolor="#FFFFFF" align="center"><%=j%></td>
           <td bgcolor="#FFFFFF" align="center"><%=branchVO.getName()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=branchVO.getCompName()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=branchVO.getAddress()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=branchVO.getPhoneNo1()%></td>
           <td bgcolor="#FFFFFF" align="center"><a href="Control.do?Action=EditBranch&BranchVO.BranchId=<%=branchVO.getBranchId()%>">Edit</a></td>
        </tr>
		<%}%>
     </table>
<% } %>

<br>
