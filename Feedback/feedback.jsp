<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentFeedbackCriteriaVO" class="com.feedback.vo.FeedbackCriteriaVO" scope="request"/>

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
	
	List feedbackList = (List)request.getAttribute("feedbackList");
	if(feedbackList == null){
		feedbackList = new ArrayList();
	}
	
	List campaignList = (List)request.getAttribute("campaignList");
	if(campaignList == null){
		campaignList = new ArrayList();
	}
	
	List campTypeList = new ArrayList();
	campTypeList.add(TypeVO.BOTH);
	campTypeList.add(TypeVO.SMS);
	campTypeList.add(TypeVO.EMAIL);
	
	
	Integer campId = (Integer)request.getAttribute("campId");
	Integer campType = (Integer)request.getAttribute("campType");
	
	String fromDate = "";
	String toDate = "";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	if(currentFeedbackCriteriaVO.getFromDate() != null && currentFeedbackCriteriaVO.getFromDate() != null){
	fromDate = sdf.format( currentFeedbackCriteriaVO.getFromDate());
	toDate = sdf.format(currentFeedbackCriteriaVO.getToDate());
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
   if (document.GiveCampaign.campaignid.selectedIndex == 0) {
       alert('Please Select Campaign!!')
       document.GiveCampaign.campaignid.focus();
        return false;
    }
	 if (document.GiveCampaign.campaigntype.selectedIndex == 0) {
       alert('Please Select Campaign Type!!')
       document.GiveCampaign.campaigntype.focus();
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

<div align="center">

<table border="0" width="65%">
<form name="SearchFeedback" method="POST" action="Control.do?Action=SearchFeedback">
    <tr>
    </tr>
    <tr>
      <td width="100%" align="center">
      
      <fieldset><legend>&nbsp;Search Feedback &nbsp; </legend>
      <table border="0" width="85%">
      
       <tr>
       </tr> 
       <tr>
       
       <td>Company :</td>
       <td width="32%" align="left">
          <select size="1" id="company" name="FeedbackCriteriaVO.CompId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		 CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentFeedbackCriteriaVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
          </td>
      <td>Branch :</td>
      <td>
           <select size="1" id="branch" name="FeedbackCriteriaVO.BranchId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentFeedbackCriteriaVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>
       <tr></tr>
       <tr>
       <td width="20%"> Customer Name :</td>
       <td width="30%"><input type="text" name="FeedbackCriteriaVO.CustomerName" value="<c:out value="${currentFeedbackCriteriaVO.customerName}" />" size="20"></td>
       <td width="20%"> Location :</td>
       <td width="30%"><input type="text" name="FeedbackCriteriaVO.Location" value="<c:out value="${currentFeedbackCriteriaVO.location}" />" size="20"></td>
       </tr>
       <tr>
       <td colspan="3">From Date :
       <!--</td>
       <td colspan="2">-->
        <!--<input type="text" id="fromDate" name="FeedbackCriteriaVO.FromDate" size="10" value=<%=fromDate%>>&nbsp;<a href="javascript:fromDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
        <input type="text" id="fromDate" name="FeedbackCriteriaVO.FromDate" size="10" onfocus="showCalendarControl(this);"  value=<%=fromDate%>>
       &nbsp;To Date :&nbsp;
       <!--<input type="text" id="toDate" name="FeedbackCriteriaVO.ToDate" size="10" value=<%=toDate%>>&nbsp;<a href="javascript:toDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
      <input type="text" id="toDate" name="FeedbackCriteriaVO.ToDate" size="10"  onfocus="showCalendarControl(this);" value=<%=toDate%>>
       </td>
       
      <td colspan="1" align="right">
      <input type="submit" value="Search Feedback" name="B1">
      </tr>
     </table>
     </fieldset>
     </form>
</table>


<form name="GiveCampaign" method="POST" action="Control.do?Action=GiveCampaign" onsubmit="return validate();">
  
<table border="0" width="65%">
    <tr>
    </tr>
 
    <tr>
      <% if(ApplicationContext.isAccessible(request,PermissionVO.GIVE_CAMPAIGN)){ %>
      <td width="100%" align="center">

        <fieldset><legend>&nbsp;Give Campaign &nbsp; </legend>
        <table border="0" width="85%">
      
         <tr>
          </tr> 
          <tr>
       
          <td>Campaign :</td>
           <td  align="left">
             <select size="1" id="campaignid" name="CampId">
       	     <option value="">--Select--</option>
             <%for(int i=0;i<campaignList.size();i++){ 
	   		  CampaignVO campaignVO = (CampaignVO)campaignList.get(i); 
	   		  String selected = "";
	   		   if(campaignVO.getCampId().equals(campId)){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=campaignVO.getCampId()%>" <%=selected%>><%=campaignVO.getCampName()%></option>
			<%}%>
			</select>
          </td>
       <td>Type : </td>
       <td  align="left">
          <select size="1" id="campaigntype" name="CampType">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<campTypeList.size();i++){ 
	   		 TypeVO typeVO = (TypeVO)campTypeList.get(i); 
	   		  String selected = "";
	   		   if(typeVO.getId().equals(campType)){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
			</td>
       <td>
       <input type="submit" value="Give Campaign" name="B2">
       </td>
      </tr>

     </table>
     </fieldset>

    </td>
        <%}%>
    </tr>

</table>	

 <%if(feedbackList != null){%>
 <table border="0" cellspacing="1"  width="80%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="8" bgcolor="#FFFFFF" align="left"> <%=feedbackList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
        <td align="center"></td>
        <td align="center">Sr No</td>
		<td align="center">Customer Name</td>
    	<td align="center">Mobile No</td>
        <td align="center">Email Id</td>
    	<td align="center">Location</td>
    	<td align="center">Comment</td>
    	<td align="center">Action</td>
          
        </tr>
       <% for(int i=0,j=1 ;i<feedbackList.size(); i++,j++){ 
	     FeedbackResultVO feedbackResultVO = (FeedbackResultVO)feedbackList.get(i);
	   %>
        <tr>
         <td  bgcolor="#FFFFFF" align="center" ><input type="checkbox" name="CustomersId" value="<%=feedbackResultVO.getCustId()%>" ></td>
         <td  bgcolor="#FFFFFF" align="center"><%=j%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=feedbackResultVO.getCustName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=feedbackResultVO.getMobileNo()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=feedbackResultVO.getEmailId()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=feedbackResultVO.getLocation()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=feedbackResultVO.getComment()%></td>
         <td  bgcolor="#FFFFFF" align="center"><a href="Control.do?Action=ViewFeedback&FeedbackResultVO.FeedbackId=<%=feedbackResultVO.getFeedbackId()%>">View</a></td>			
        </tr>
		<%}%>
     </table>
<% } %>
</form>

<br>
<script language="JavaScript">
   <!-- // create calendar object(s) just after form tag closed
     // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
     // note: you can have as many calendar objects as you need for your application
 
   var fromDate= new calendar1(document.forms['SearchFeedback'].elements['fromDate'],new Date());
   fromDate.year_scroll = true;
   fromDate.time_comp = false;
   
   var toDate= new calendar1(document.forms['SearchFeedback'].elements['toDate'],new Date());
   toDate.year_scroll = true;
   toDate.time_comp = false;
   
   //-->
 </script>


