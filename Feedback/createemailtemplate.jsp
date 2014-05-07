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
	
	String buttonStr = "Create Template";
	String labelStr = "Create Template";
	if(currentEmailTemplateVO.getId() != null){
		buttonStr = "Edit Template";
		labelStr = "Edit Template";		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
%>

<script type="text/javascript">
function validate(){
    if(document.CreateEmailTemplate.name.value==""){
       alert('Please Enter Template Name!!')
       document.CreateEmailTemplate.name.focus();
        return false;
    }
   else if(document.CreateEmailTemplate.subject.value=="")
   {
     alert("please enter Subject!!!");
     document.CreateEmailTemplate.subject.focus();
     return false;
   }
    else if(document.CreateEmailTemplate.body.value=="")
   {
     alert("please enter body!!");
     document.CreateEmailTemplate.body.focus();
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

<table border="0" width="60%">
    <tr>
      <td width="100%" align="center">
      <form name="CreateEmailTemplate" method="POST" action="Control.do?Action=CreateEmailTemplate">
      <input type="hidden" name="EmailTemplateVO.Id" value="<c:out value="${currentEmailTemplateVO.id}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">       
        
        
         <tr>
          <td align="left">Template Name :</td>
          <td width="61%" align="left"><input type="text" id = "name" name="EmailTemplateVO.Name" value="<c:out value="${currentEmailTemplateVO.name}" />" size="20"></td>
        </tr>
        
        <tr>
          <td width="36%" align="left">Email Subject :</td>
          <td width="61%" align="left"><input type="text" id = "subject" name="EmailTemplateVO.Subject" value="<c:out value="${currentEmailTemplateVO.subject}" />" size="50"></td>
        </tr>       
         <tr>
          <td width="36%" align="left">Email Body :</td>
         <td> <textarea rows="5" id="body" name="EmailTemplateVO.Body" cols="40"><c:out value="${currentEmailTemplateVO.body}" /></textarea>
         </td>
         </tr>
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
                <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateEmailTemplate';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
