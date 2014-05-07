<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentStateVO" class="com.feedback.vo.StateVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List stateList = (List)request.getAttribute("stateList");
	if(stateList == null){
		stateList = new ArrayList();
	}
	
	List countryList = (List)request.getAttribute("countryList");
	if(countryList == null){
		countryList = new ArrayList();
	}
	
	String buttonStr = "Add State";
	String labelStr = "Add State";
	if(currentStateVO.getStateId() != null){
		buttonStr = "Save State";
		labelStr = "Edit State";		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
%>

<script type="text/javascript">
function validate(){
   if (document.CreateState.country.selectedIndex == 0) {
       alert('Please Select Country!!')
       document.CreateState.country.focus();
        return false;
    }
   else if(document.CreateState.state.value=="")
   {
     alert("please enter State name");
     document.CreateState.state.focus();
     return false;
   }
   else if (!isNaN(document.CreateState.state.value)) {
       alert('Please enter State name in text !!');
       document.CreateState.state.focus();
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
      <form name="CreateState" method="POST" action="Control.do?Action=CreateState">
      <input type="hidden" name="StateVO.StateId" value="<c:out value="${currentStateVO.stateId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">       
        
        <tr>
         <td width="27%" align="left">Country :</td>
  		  <td width="71%" align="left">
          <select size="1" id="country" name="StateVO.CountryId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<countryList.size();i++){ 
	   		  CountryVO countryVO = (CountryVO)countryList.get(i); 
	   		  String selected = "";
	   		  if(countryVO.getCountryId().equals(currentStateVO.getCountryId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=countryVO.getCountryId()%>" <%=selected%>><%=countryVO.getName()%></option>
			<%}%>
			</select>
          </td>
        
        </tr>
        
        <tr>
          <td width="36%" align="left">State Name :</td>
          <td width="61%" align="left"><input type="text" id = "state" name="StateVO.Name" value="<c:out value="${currentStateVO.name}" />" size="20"></td>
        </tr>       
        
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
          <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateState';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
