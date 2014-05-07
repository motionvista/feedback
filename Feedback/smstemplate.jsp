<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentSmsTemplateVO" class="com.feedback.vo.SmsTemplateVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List smsTemplateList = (List)request.getAttribute("smsTemplateList");
	if(smsTemplateList == null){
		smsTemplateList = new ArrayList();
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

<table border="0" width="65%">
     <form name="SearchSmsTemplate" method="POST" action="Control.do?Action=SearchSmsTemplate">
       <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New SMS Template" name="createNewCompany" onClick="form.action='Control.do?Action=GoToCreateSMSTemplate';">
      </td>
      </tr>
      <tr>
      <td width="100%" align="center">
      <fieldset><legend>&nbsp;Search SMS Template&nbsp; </legend>
      <table border="0" width="85%">       
         <tr>
          <td align="left">Template Name :</td>
          <td width="61%" align="left"><input type="text" id = "name" name="SmsTemplateVO.Name" value="<c:out value="${currentSmsTemplateVO.name}" />" size="20"></td>
          <td>
          <input type="submit" value="Search SMS Template" name="B1"></td>         
         </tr>
        
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(smsTemplateList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="4" bgcolor="#FFFFFF" align="left"> <%=smsTemplateList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">Template Name</td>
          <td align="center">Action</td>
        </tr>
        <%
        for(int i=0,j=1;i<smsTemplateList.size();i++,j++){  
   		  SmsTemplateVO smsTemplateVO = (SmsTemplateVO)smsTemplateList.get(i);
		%>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center"" bgcolor="#FFFFFF"><%=smsTemplateVO.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditSmsTemplate&SmsTemplateVO.Id=<%=smsTemplateVO.getId()%>">Edit</a></td>
        </tr>
		<%}%>
     </table>
<% } %>