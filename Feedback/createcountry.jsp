<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCountryVO" class="com.feedback.vo.CountryVO" scope="request"/>


<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List countryList = (List)request.getAttribute("countryList");
	if(countryList == null){
		countryList = new ArrayList();
	}
	
	String buttonStr = "Add Country";
	String labelStr = "Add Country";
	if(currentCountryVO.getCountryId() != null){
		buttonStr = "Save Country";
		labelStr = "Edit Country";		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
%>

<script type="text/javascript">
function validate(){
   if(document.CreateCountry.country.value=="")
   {
     alert("please enter country name");
     document.CreateCountry.country.focus();
     return false;
   }
   else if (!isNaN(document.CreateCountry.country.value)) {
       alert('Please enter country name in text !!');
       document.CreateCountry.country.focus();
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
      <form name="CreateCountry" method="POST" action="Control.do?Action=CreateCountry">
      <input type="hidden" name="CountryVO.CountryId" value="<c:out value="${currentCountryVO.countryId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">       
        <tr>
          <td width="36%" align="left">Country Name :</td>
          <td width="61%" align="left"><input type="text" id = "country" name="CountryVO.Name" value="<c:out value="${currentCountryVO.name}" />" size="20"></td>
        </tr>       
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
          <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateCountry';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
