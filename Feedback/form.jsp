<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentFormCriteriaVO" class="com.feedback.vo.FormCriteriaVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List formList = (List)request.getAttribute("formList");
	if(formList == null){
		formList = new ArrayList();
	}
	
	List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
	}
	List branchList = (List)request.getAttribute("branchList");
	if(branchList == null){
		branchList = new ArrayList();
	}
	
	List statusList = new ArrayList();
	statusList.add(TypeVO.ACTIVE);
	statusList.add(TypeVO.INACTIVE);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
	
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script language="JavaScript" src="calendar1.js"></script>

<script>
     $(document).ready(function(){
    
     $("#company").change(function(){
     fillBranch('branch', this); 
     });
    
    function fillBranch(ddId, callingElement) {
      var dd = $('#' + ddId);
      var companyid=$(callingElement).val();
         $.getJSON('WebServlet.do?Action=GetBranches&companyid='+companyid, function(data) {
                 $('>option', dd).remove();
             if (data) {
                  $.each(data, function(i, data) {
                     dd.append($('<option/>').val(data.branchId).text(data.name));
                  });
             } else {
                  dd.append($('<option/>').text("Select"));
             }
         });
    }
    
    
  });
</script>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<table border="0" width="65%">
      <form name="SearchForm" method="POST" action="Control.do?Action=SearchForm">
      
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New Form" name="createNewForm" onClick="form.action='Control.do?Action=GoToCreateForm';">
      </td>
      </tr>
      
      <tr>
      <td width="100%" align="center">
      <fieldset><legend>&nbsp;Search Form&nbsp; </legend>
      <table border="0" width="85%">       
       <tr>
        <td> Company Name :</td>
        <td>
           <select size="1" id="company" name="FormCriteriaVO.CompId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<companyList.size();i++){ 
			   	       CompanyVO companyVO  = (CompanyVO)companyList.get(i); 
			   	        String selectStr = "";
				        if(companyVO.getCompId().equals(currentFormCriteriaVO.getCompId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=companyVO.getCompId()%>" <%=selectStr%> ><%=companyVO.getName()%></option>
				     <%}%>
			</select>
       </td>
       <td> Branch Name :</td>
       <td>
           <select size="1" id="branch" name="FormCriteriaVO.BranchId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<branchList.size();i++){ 
			   	       BranchVO branchVO  = (BranchVO)branchList.get(i); 
			   	        String selectStr = "";
				        if(branchVO.getBranchId().equals(currentFormCriteriaVO.getBranchId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=branchVO.getBranchId()%>" <%=selectStr%> ><%=branchVO.getName()%></option>
				     <%}%>
			</select>
        </td>
        </tr>       
        <tr>
         <td  align="left">Status :</td>
          <td  align="left">
          <select size="1" id="status" name="FormCriteriaVO.Status">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentFormCriteriaVO.getStatus())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
          </td>
          <td colspan="2" align="right">
          <input type="submit" value="Search Form" id="b2" name="B1"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
  </tr>
</table>
<br>
 <% if(formList!= null){ %>
 <table border="0" cellspacing="1"  width="87%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="8" bgcolor="#FFFFFF" align="left"> <%=formList.size()%>&nbsp;Record(s) found.</td>
  </tr>
<tr>
    <td align="center">Sr No.</td>
    <td align="center">Form Name</td>
    <td align="center">Company</td>
    <td align="center">Branch</td>
    <td align="center">Valid From Date</td>
    <td align="center">Valid To Date</td>
    <td align="center">Status</td>
    <td align="center">Action</td>
   </tr>
        <%
        for(int i=0,j=1;i<formList.size();i++,j++){  
   		  FormVO formVO1 = (FormVO)formList.get(i);
   		   String fdate ="";
   		   String tdate="";
   		 try{
          fdate = sdf.format(formVO1.getValidFromDate());
   		  tdate = sdf.format(formVO1.getValidToDate());
   		  }catch(Exception e){
		  System.out.println("Exception2 from jsp"+e.getMessage());
		}
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center"  bgcolor="#FFFFFF"><%=formVO1.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=formVO1.getCompName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=formVO1.getBranchName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=fdate%></td>
          <td align="center" bgcolor="#FFFFFF"><%=tdate%></td>
          <td align="center" bgcolor="#FFFFFF"><%=formVO1.getStatusStr()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditForm&FormVO.FormId=<%=formVO1.getFormId()%>">Edit</a></td>
   </tr>
		<%}%>
</table>
<% } %>
