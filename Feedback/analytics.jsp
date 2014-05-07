<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*"%>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentFeedbackCriteriaVO" class="com.feedback.vo.FeedbackCriteriaVO" scope="request"/>
<%
	String fromDate = "";
	String toDate = "";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
 <script language="JavaScript" src="calendar1.js"></script>
 <link href="CalendarControl.css" rel="stylesheet" type="text/css">
 <script src="CalendarControl.js" language="javascript"></script>
<SCRIPT language="JavaScript">
function stateSelected(){
	document.Analytics.submit();
}
</SCRIPT>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
              
        </td>
      </tr>
</table>

<table border="0" width="45%">
    <form name="Analytics" method="POST" action="Control.do?Action=Analytics">
    <tr>
      <td width="100%" align="center">
      </td>
    </tr>
    <tr>
    <td>
     <input type="text" id="fromDate" name="FeedbackCriteriaVO.FromDate" size="10" value=<%=fromDate%>>&nbsp;<a href="javascript:fromDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>
    </td> <td> <input type="text" id="toDate" name="FeedbackCriteriaVO.ToDate" size="20"  onfocus="showCalendarControl(this);">
    </td>
    </tr>
    <tr>
    <td>
     <input type="submit" value="Import To Excel" name="importtoexcel" onClick="form.action='Control.do?Action=importCampaignToExcell';">
    </td>
    </tr>
          </form>
 </table>
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

