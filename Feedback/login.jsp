
<%
String uId = request.getParameter("LoginId");
String password = request.getParameter("Password");
if(uId == null){
uId="";
password="";
}
String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg="";
	}
%>
<br>
<br>
<br>
<script type="text/javascript">
function validate(){

  if(document.login.LoginId.value=="")
   {
   alert("please enter login id");
   document.login.LoginId.focus();
   return false;
   }
   else if(document.login.Password.value=="")
    {
    alert("please enter pasword");
    document.login.Password.focus();
    return false;
    }
}
</script>

<table border="0" width="100%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>
<form name= "login" method="post" action="Control.do?Action=Login" onsubmit="return validate();">
<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1">
  <tr>
    <td width="100%" align="center"><br>
&nbsp;<table border="0" cellpadding="0" cellspacing="3" style="border-collapse: collapse" bordercolor="#111111" width="35%" id="AutoNumber2">
      <tr>
        <td width="50%">Login Id</td>
        <td width="50%">
        
        <p><input type="text" name="LoginId" value="<%=uId%>" size="20"></td>
      </tr>
      <tr><br></tr>
      <tr>
        <td width="50%">Password</td>
        <td width="50%"><input type="password" name="Password" value="<%=password%>" size="20"></td>
      </tr>
      <tr>
        <td width="100%" colspan="2" align="center">
		&nbsp;
      </tr>
      <tr>
        <td width="100%" colspan="2" align="center">
        <input type="submit" value="Login" name="B1"></td>
      </tr>
    </table>
    <p><br>
&nbsp;</td>
  </tr>
</table>
</form>
<br>
<br>
<br>