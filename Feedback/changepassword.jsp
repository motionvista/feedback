<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();

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

<table border="0" width="50%">
    <tr>
      <td width="100%" align="center">
      <form method="POST" action="Control.do?Action=ChangePassword">
      <fieldset><legend>&nbsp;Change Password&nbsp; </legend>
      <table border="0" cellspacing="1" style="border-collapse: collapse" bordercolor="#111111" width="87%" id="AutoNumber3">
              
    <tr>
      <td width="30%" valign="middle" align="left">Current Password</td>
      <td width="70%" valign="middle" align="left"><input type="password" name="CurrentPassword" size="20"/></td>
    </tr>
    <tr>
      <td width="30%" valign="middle" align="left">New Password</td>
      <td width="70%" valign="middle" align="left"><input type="password" name="NewPassword" size="20"/></td>
    </tr>
    <tr>
      <td width="30%" valign="middle" align="left">Confirm Password</td>
      <td width="70%" valign="middle" align="left"><input type="password" name="ConfirmPassword" size="20"/></td>
    </tr>
    <tr>
      <td width="30%" valign="middle" align="left">&nbsp;</td>
      <td width="70%" valign="middle" align="right">
      <input type="submit" value="Change Password"/>
      </td>
    </tr>    
        
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br> 
 </table>