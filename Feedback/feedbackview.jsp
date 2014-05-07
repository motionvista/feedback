<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentFeedbackResultVO" class="com.feedback.vo.FeedbackResultVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
	
	String dob = "";
	String date = "";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	
	SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");	
	String buttonStr = "Create Feedback";
	String labelStr = "Create Feedback";
	
	FeedbackQuestionAnsVO[] feedbackQuestionAnsVO = new FeedbackQuestionAnsVO[0];
	
	if(currentFeedbackResultVO.getFeedbackId() != null){
		buttonStr = "Save Feedback";
		labelStr = "Feedback Detail";
		dob = sdf.format(currentFeedbackResultVO.getDob());
		date = sdf1.format(currentFeedbackResultVO.getCreateTime());
		feedbackQuestionAnsVO = currentFeedbackResultVO.getQuestionAns(); 		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg="";
	}
	
%>

<SCRIPT type="text/javascript" language="JavaScript">
 
 functiion validation(){
 
 }
</SCRIPT>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<table border="0" width="65%">
    <tr>
      <td width="100%" align="center">
      <form name="Branch" method="POST" action="Control.do?Action=GoToReports">
      <input type="hidden" name="FeedbackResultVO.FeedbackId" value="<c:out value="${currentFeedbackResultVO.feedbackId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="85%">   
  
        <tr>
       <td width="21%"> Company :</td>
       <td width="36%"><input type="text" name="FeedbackResultVO.CompName" value="<c:out value="${currentFeedbackResultVO.compName}" />
       " size="20" readonly></td>
       <td width="18%">Branch  :</td>
       <td width="25%"><input type="text" name="FeedbackResultVO.BranchName" value="<c:out value="${currentFeedbackResultVO.branchName}" />
       " size="20" readonly></td>
       </tr> 
       <tr>
       </tr>    
       <tr>
       <td width="21%"> Customer Name :</td>
       <td width="36%"><input type="text" name="FeedbackResultVO.CustName" value="<c:out value="${currentFeedbackResultVO.custName}" />
       " size="20" readonly></td>
       <td width="18%">Date  :</td>
       <td width="25%"><input type="text" name="FeedbackResultVO.Date" value="<%=date%>"  size="20"></td>
       </tr> 
       <tr></tr> 
       <tr>
       <td>Mobile Number  :</td>
       <td><input type="text" name="FeedbackResultVO.MobileNo" value="<c:out value="${currentFeedbackResultVO.mobileNo}" />
       " size="20" readonly></td>
        <td>Email Id :</td>
         <td><input type="text" name="FeedbackResultVO.EmailId" value="<c:out value="${currentFeedbackResultVO.emailId}" />
         " size="20" readonly></td>
       </tr>
       <tr></tr>
      <tr>
      <td>Occupation :</td>
      <td><input type="text" name="FeedbackResultVO.Occupation" value="<c:out value="${currentFeedbackResultVO.occupation}" />
      " size="20" readonly> </td>
      <td>Date of Birth :</td>
      <td><input type="text" name="FeedbackResultVO.Dob" value="<%=dob%>"  size="20" readonly> </td>
      </tr>
      <tr>
      </tr>
      <tr>
      <td colspan="4" align="center">
          <% if(feedbackQuestionAnsVO!= null && feedbackQuestionAnsVO.length != 0){ %>
          <fieldset><legend>&nbsp;Question Ans </legend>
           <table border="0" cellspacing="1"  width="100%" bgcolor="<%=darkColor%>">
           <tr>
            <td width="6%">Sr No.</td>
            <td width="61%">Question</td>
            <td width="11%">Ans</td>
           </tr>
         <%
          for(int i=0,j=1;i<feedbackQuestionAnsVO.length;i++,j++){  
   		  FeedbackQuestionAnsVO queans = (FeedbackQuestionAnsVO)feedbackQuestionAnsVO[i];
		 %>
          <tr>
           <td width="10%" bgcolor="#FFFFFF"><%=j%></td>
           <td width="61%" bgcolor="#FFFFFF"><%=queans.getQuestion()%></td>
           <td width="11%" bgcolor="#FFFFFF"><%=queans.getAns()%></td>
         </tr>
		<%}%>
         </table>
         </fieldset>
        <% } %>
     </td>
      </tr>
      
      <tr>
      </tr> 
      <tr>
      <td>Comment :</td>
      <td><textarea rows="3"  name="FeedbackResultVO.Comment" cols="17" readonly><c:out value="${currentFeedbackResultVO.comment}" /></textarea> </td>
      </tr>
      <tr>
      <td>&nbsp;</td>
      <td></td>
      </tr>
      <tr>
      <td colspan="4" align="center">
      <%--
      <input type="submit" value="<%=buttonStr%>"  onClick="form.action='Control.do?Action=GoToReports';" name="B1">
      --%>
       <input type="submit" value="Back">
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>



