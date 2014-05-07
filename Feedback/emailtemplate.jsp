<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentEmailTemplateVO" class="com.feedback.vo.EmailTemplateVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List emailTemplateList = (List)request.getAttribute("emailTemplateList");
	if(emailTemplateList == null){
		emailTemplateList = new ArrayList();
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
     <form name="SearchEmailTemplate" method="POST" action="Control.do?Action=SearchEmailTemplate">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New EmailTemplate" name="createNewEmailTemplate" onClick="form.action='Control.do?Action=GoToCreateEmailTemplate';">
      </td>
      </tr>
      <tr>
      <td width="100%" align="center">
      <fieldset><legend>&nbsp;Search Email Template&nbsp; </legend>
      <table border="0" width="85%">       
        
         <tr>
          <td align="left">Template Name :</td>
          <td width="61%" align="left"><input type="text" id = "name" name="EmailTemplateVO.Name" value="<c:out value="${currentEmailTemplateVO.name}" />" size="20"></td>
          <td>
          <input type="submit" value="Search Email Template" name="B1">
          </td>
        </tr>
        
       
        
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(emailTemplateList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="4" bgcolor="#FFFFFF" align="left"> <%=emailTemplateList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">Template Name</td>
          <td align="center">Subject</td>
          <td align="center">Action</td>
        </tr>
        <%
        for(int i=0,j=1;i<emailTemplateList.size();i++,j++){  
   		  EmailTemplateVO emailTemplateVO = (EmailTemplateVO)emailTemplateList.get(i);
		%>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=emailTemplateVO.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=emailTemplateVO.getSubject()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditEmailTemplate&EmailTemplateVO.Id=<%=emailTemplateVO.getId()%>">Edit</a></td>
        </tr>
		<%}%>
     </table>
<% } %>