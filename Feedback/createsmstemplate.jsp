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
	
	String buttonStr = "Create Template";
	String labelStr = "Create Template";
	if(currentSmsTemplateVO.getId() != null){
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
    if(document.CreateSmsTemplate.name.value==""){
       alert('Please Enter Template Name!!')
       document.CreateSmsTemplate.name.focus();
        return false;
    }
   else if(document.CreateSmsTemplate.text.value=="")
   {
     alert("please enter message text!!!");
     document.CreateSmsTemplate.text.focus();
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
      <form name="CreateSmsTemplate" method="POST" action="Control.do?Action=CreateSmsTemplate">
      <input type="hidden" name="SmsTemplateVO.Id" value="<c:out value="${currentSmsTemplateVO.id}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">       
        
        
         <tr>
          <td align="left">Template Name :</td>
          <td width="61%" align="left"><input type="text" id = "name" name="SmsTemplateVO.Name" value="<c:out value="${currentSmsTemplateVO.name}" />" size="20"></td>
        </tr>
        
         <tr>
          <td width="36%" align="left">Message Text :</td>
         <td> <textarea rows="5" id="text" name="SmsTemplateVO.Text" cols="40"><c:out value="${currentSmsTemplateVO.text}" /></textarea>
         </td>
         </tr>
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
                <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateSmsTemplate';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
