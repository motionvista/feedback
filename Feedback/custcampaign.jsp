<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCustCampaignVO" class="com.feedback.vo.CustCampaignVO" scope="request"/>

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
	
	List campaignList = (List)request.getAttribute("campaignList");
	if(campaignList == null){
		campaignList = new ArrayList();
	}
	
	List custCampaignList = (List)request.getAttribute("custCampaignList");
	if(custCampaignList == null){
		custCampaignList = new ArrayList();
	}
	
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

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

<div align="center">

<table border="0" width="47%">
<form name="SearchCustCustomer" method="POST" action="Control.do?Action=SearchCustCampaign">
      <tr>
      </tr> 

    <tr>
      <td width="100%" align="center">
      
      <fieldset>
      <legend>&nbsp;Search Customer Campaign &nbsp; </legend>
      <table width="571"  border="0">
       
       <tr>
       <td> Company :</td>
       <td>
           <select size="1" id="company" name="CustCampaignVO.CompId">
         	<option value="">--All--</option>
					<%for(int i=0;i<companyList.size();i++){ 
			   	       CompanyVO companyVO  = (CompanyVO)companyList.get(i); 
			   	        String selectStr = "";
				        if(companyVO.getCompId().equals(currentCustCampaignVO.getCompId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=companyVO.getCompId()%>" <%=selectStr%> ><%=companyVO.getName()%></option>
				     <%}%>
			</select>
       </td>
       <td>Branch :</td>
      <td>
           <select size="1" id="branch" name="CustCampaignVO.BranchId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentCustCampaignVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>     
       <tr>
       <td> Customer Name :</td>
       <td><input type="text" name="CustCampaignVO.CustName" value="<c:out value="${currentCustCampaignVO.custName}" />" size="20"></td>
      
       <td> Campaign :</td>
       <td>
           <select size="1" name="CustCampaignVO.CampId">
         	<option value="">--All--</option>
					<%for(int i=0;i<campaignList.size();i++){ 
			   	       CampaignVO campaignVO  = (CampaignVO)campaignList.get(i); 
			   	        String selectStr = "";
				        if(campaignVO.getCampId().equals(currentCustCampaignVO.getCampId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=campaignVO.getCampId()%>" <%=selectStr%> ><%=campaignVO.getCampName()%></option>
				     <%}%>
			</select>
       </td>
       </tr>
       <tr>
        <td colspan="4" align="right">
        <input type="submit" value="Search" name="B1">
       </td>
       
       </tr>  
      </table>
     </fieldset>
     </form>
</table>	
</div>

<br>
 <% if(custCampaignList!= null){ %>
 <table border="0" cellspacing="1"  width="80%" bgcolor="<%=darkColor%>">
 <tr>
  <td colspan="6" bgcolor="#FFFFFF" align="left"> <%=custCampaignList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">Customer Name</td>
          <td align="center">Campaign Name</td>
          <td align="center">Company</td>
          <td align="center">Branch</td>
        </tr>
        <%
        for(int i=0,j=1;i<custCampaignList.size();i++,j++){  
   		  CustCampaignVO custCampaignVO = (CustCampaignVO)custCampaignList.get(i);
		 %>
        <tr>
          <td bgcolor="#FFFFFF" align="center"><%=j%></td>
           <td bgcolor="#FFFFFF" align="center"><%=custCampaignVO.getCustName()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=custCampaignVO.getCampName()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=custCampaignVO.getCompName()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=custCampaignVO.getBranchName()%></td>
        </tr>
		<%}%>
     </table>
<% } %>

<br>
