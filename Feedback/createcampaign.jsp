<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCampaignVO" class="com.feedback.vo.CampaignVO" scope="request"/>

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
	
	List emailTemplateList = (List)request.getAttribute("emailTemplateList");
	if(emailTemplateList == null){
		emailTemplateList = new ArrayList();
	}
	
	List smsTemplateList = (List)request.getAttribute("smsTemplateList");
	if(smsTemplateList == null){
		smsTemplateList = new ArrayList();
	}
	
	List statusList = new ArrayList();
	statusList.add(TypeVO.ACTIVE);
	statusList.add(TypeVO.INACTIVE);
	
	String srtDate = "";
    String endDate = "";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	String buttonStr = "Create Campaign";
	String labelStr = "Create Campaign";
	if(currentCampaignVO.getCampId() != null){
		buttonStr = "Save Campaign";
		labelStr = "Update Campaign";	
		try{
		srtDate = sdf.format(currentCampaignVO.getStartDate());
		endDate = sdf.format(currentCampaignVO.getEndDate());
		}catch(Exception e){
		System.out.println("Exception "+e.getMessage());
		}
	}
	
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script language="JavaScript" src="calendar1.js"></script>
<link href="CalendarControl.css" rel="stylesheet" type="text/css">
<script src="CalendarControl.js" language="javascript"></script>

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
<script type="text/javascript">
function validate(){
   var sdate = document.getElementById('srtDate').value.split("/");
   var startdate = new Date(sdate[2], sdate[1] - 1, sdate[0]);
   
   var edate = document.getElementById('endDate').value.split("/");
   var enddate = new Date(edate[2], edate[1] - 1, edate[0]);
   
   if (document.CreateCampaign.company.selectedIndex == 0) {
       alert('Please Select Company!!')
       document.CreateCampaign.company.focus();
        return false;
   }
   else if(document.CreateCampaign.campaignname.value=="")
   {
     alert("please enter Campaign name");
     document.CreateCampaign.campaignname.focus();
     return false;
   } else if(document.CreateCampaign.srtDate.value=="")
   {
     alert("please enter start date!!!");
     document.CreateCampaign.srtDate.focus();
     return false;
   }
   else if(document.CreateCampaign.endDate.value=="")
   {
     alert("please enter end date!!!");
     document.CreateCampaign.endDate.focus();
     return false;
   }
   else if(startdate>enddate){
      alert("start date should be small than end date");
      document.CreateCampaign.srtDate.focus();
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
<table border="0" width="45%">
<tr>
      <td width="100%" align="center">
      <form name="CreateCampaign" method="POST" action="Control.do?Action=CreateCampaign">
      <input type="hidden" name="CampaignVO.CampId" value="<c:out value="${currentCampaignVO.campId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">  
      
       <tr>
      <td>Company :</td>
      <td>
           <select size="1" id="company" name="CampaignVO.CompId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		  CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentCampaignVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>
        <tr>
        <td width="39%"> Branch :</td>
       <td width="31%">
           <select size="1" id="branch" name="CampaignVO.BranchId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<branchList.size();i++){ 
			   	       BranchVO branchVO  = (BranchVO)branchList.get(i); 
			   	        String selectStr = "";
				        if(branchVO.getBranchId().equals(currentCampaignVO.getBranchId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=branchVO.getBranchId()%>" <%=selectStr%> ><%=branchVO.getName()%></option>
				     <%}%>
			</select>
       </td>
        </tr>       
    
        <tr>
          <td width="36%" align="left">Campaign Name :</td>
          <td width="61%"  align="left"><input type="text" id="campaignname" name="CampaignVO.CampName" value="<c:out value="${currentCampaignVO.campName}" />" size="20"></td>
        
        </tr>       
        <tr>
          <td width="36%" align="left">Start Date :</td>
          <td width="61%" align="left">
          <!--<input type="text" id="srtDate" name="CampaignVO.StartDate" size="20" value=<%=srtDate%>>&nbsp;<a href="javascript:srtDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
          <input type="text" id="srtDate" name="CampaignVO.StartDate" size="20" onfocus="showCalendarControl(this);" value=<%=srtDate%>>
          </td>
       
        </tr> 
         <tr>
          <td width="36%" align="left">End Date :</td>
          <td width="61%" align="left">
          <!--<input type="text" id="endDate" name="CampaignVO.EndDate" size="20" value=<%=endDate%>>&nbsp;<a href="javascript:endDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
          <input type="text" id="endDate" name="CampaignVO.EndDate" size="20" onfocus="showCalendarControl(this);" value=<%=endDate%>>
          </td>
         
        </tr> 
       <tr>
       </tr>
       <tr>
       <td width="36%" align="left">Description :</td>
          <td width="61%" align="left">
          <textarea rows="2" id="description" name="CampaignVO.Description" cols="17"><c:out value="${currentCampaignVO.description}" /></textarea>
         
       </tr>
       
        <tr>
        <td width="39%"> Email Template :</td>
       <td width="31%">
           <select size="1" id="emailtemplate" name="CampaignVO.EmailTemplate">
         	<option value="">--Select--</option>
					<%for(int i=0;i<emailTemplateList.size();i++){ 
			   	       EmailTemplateVO emailTemplateVO  = (EmailTemplateVO)emailTemplateList.get(i); 
			   	        String selectStr = "";
				        if(emailTemplateVO.getId().equals(currentCampaignVO.getEmailTemplate())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=emailTemplateVO.getId()%>" <%=selectStr%> ><%=emailTemplateVO.getName()%></option>
				     <%}%>
			</select>
       </td>
        </tr>       
        
        
         <tr>
        <td width="39%"> SMS Template :</td>
       <td width="31%">
           <select size="1" id="smstemplate" name="CampaignVO.SmsTemplate">
         	<option value="">--Select--</option>
					<%for(int i=0;i<smsTemplateList.size();i++){ 
			   	       SmsTemplateVO smsTemplateVO  = (SmsTemplateVO)smsTemplateList.get(i); 
			   	        String selectStr = "";
				        if(smsTemplateVO.getId().equals(currentCampaignVO.getSmsTemplate())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=smsTemplateVO.getId()%>" <%=selectStr%> ><%=smsTemplateVO.getName()%></option>
				     <%}%>
			</select>
       </td>
        </tr>       
        
        <tr>
       <% if(ApplicationContext.isAccessible(request,PermissionVO.APPROVE_CAMPAIGN)){ %>
         <td width="27%" align="left">Approve :</td>
          <td width="71%" align="left">
          <select size="1" id="approve" name="CampaignVO.Approve">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentCampaignVO.getApprove())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
          </td>
       <%}%>
        </tr>
        <tr>
        <td></td>
        </tr>
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" id="b2" name="B1" onClick="return validate();">
                <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateCampaign';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
  </tr>
</table>

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