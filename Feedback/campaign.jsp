<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCampaignCriteriaVO" class="com.feedback.vo.CampaignCriteriaVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List campaignList = (List)request.getAttribute("campaignList");
	if(campaignList == null){
		campaignList = new ArrayList();
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
      <form name="SearchCampaign" method="POST" action="Control.do?Action=SearchCampaign">      
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New Campaign" name="createNewCampaign" onClick="form.action='Control.do?Action=GoToCreateCampaign';">
      </td>
      </tr>
      
      <tr>
      <td width="100%" align="center">
      <fieldset><legend>&nbsp;Search Campaign &nbsp; </legend>
      <table border="0" width="85%">  
      
       <tr>
      <td>Company :</td>
      <td>
           <select size="1" id="company" name="CampaignCriteriaVO.CompId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		  CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentCampaignCriteriaVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
       </td>
        <td> Branch :</td>
       <td>
           <select size="1" id="branch" name="CampaignCriteriaVO.BranchId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<branchList.size();i++){ 
			   	       BranchVO branchVO  = (BranchVO)branchList.get(i); 
			   	        String selectStr = "";
				        if(branchVO.getBranchId().equals(currentCampaignCriteriaVO.getBranchId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=branchVO.getBranchId()%>" <%=selectStr%> ><%=branchVO.getName()%></option>
				     <%}%>
			</select>
       </td>
        </tr>       
       <tr>
         <td align="left">Status :</td>
          <td  align="left">
          <select size="1" id="approve" name="CampaignCriteriaVO.Status">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentCampaignCriteriaVO.getStatus())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
          </td>
          <td colspan="2" align="right">
          <input type="submit" value="Search Campaign" id="b2" name="B1"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
  </tr>
</table>
<br>
 <% if(campaignList!= null){ %>
 <table border="0" cellspacing="1"  width="87%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="8" bgcolor="#FFFFFF" align="left"> <%=campaignList.size()%>&nbsp;Record(s) found.</td>
  </tr>
<tr>
    <td align="center">Sr No.</td>
    <td align="center">Campaign Name</td>
    <td align="center">Company</td>
    <td align="center">Branch</td>
    <td align="center">Start Date</td>
    <td align="center">End Date</td>
    <td align="center">Status</td>
    <td align="center">Action</td>
   </tr>
        <%
        for(int i=0,j=1;i<campaignList.size();i++,j++){  
   		  CampaignVO campaignVO = (CampaignVO)campaignList.get(i);
   		     		  String sdate ="";
   		     		   String edate="";
   		 try{
          sdate = sdf.format(campaignVO.getStartDate());
   		  edate = sdf.format(campaignVO.getEndDate());
   		  }catch(Exception e){
		System.out.println("Exception2 "+e.getMessage());
		}
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=campaignVO.getCampName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=campaignVO.getCompName()%></td>
          <td align="center"  bgcolor="#FFFFFF"><%=campaignVO.getBranchName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=sdate%></td>
          <td align="center" bgcolor="#FFFFFF"><%=edate%></td>
          <td align="center" bgcolor="#FFFFFF"><%=campaignVO.getStatusStr()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditCampaign&CampaignVO.CampId=<%=campaignVO.getCampId()%>">Edit</a>
          </td>
   </tr>
		<%}%>
</table>
<% } %>
<script language="JavaScript">
   <!-- // create calendar object(s) just after form tag closed
     // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
     // note: you can have as many calendar objects as you need for your application
 
   var srtDate= new calendar1(document.forms['CreateCampaign'].elements['srtDate'],new Date());
   srtDate.year_scroll = true;
   srtDate.time_comp = false;
   
   var endDate= new calendar1(document.forms['CreateCampaign'].elements['endDate'],new Date());
   endDate.year_scroll = true;
   endDate.time_comp = false;
   
   //-->
 </script>