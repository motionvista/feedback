<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCityVO" class="com.feedback.vo.CityVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List cityList = (List)request.getAttribute("cityList");
	if(cityList == null){
		cityList = new ArrayList();
	}
	
	List stateList = (List)request.getAttribute("stateList");
	if(stateList == null){
		stateList = new ArrayList();
	}
	
	List countryList = (List)request.getAttribute("countryList");
	if(countryList == null){
		countryList = new ArrayList();
	}
	
	String buttonStr = "Add City";
	String labelStr = "Add City";
	if(currentCityVO.getStateId() != null){
		buttonStr = "Save City";
		labelStr = "Edit City";		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
%>

<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script>
     $(document).ready(function(){
     
     $("#country").change(function(){
     fillState('state', this); 
     });
    
     function fillState(ddId, callingElement) {
      var dd = $('#' + ddId);
      var countid=$(callingElement).val();
         $.getJSON('WebServlet.do?Action=GetStates&countid='+countid, function(data) {
                 $('>option', dd).remove();
             if (data) {
                  $.each(data, function(i, data) {
                     dd.append($('<option/>').val(data.stateId).text(data.name));
                  });
             } else {
                  dd.append($('<option/>').text("Select"));
             }
         });
    }
   
  });
</script>
<script type="text/javascript">
function validate(){
    if (document.CreateCity.country.selectedIndex == 0) {
       alert('Please Select Country!!')
       document.CreateCity.country.focus();
        return false;
    }
   else if (document.CreateCity.state.selectedIndex == 0) {
       alert('Please Select State!!')
       document.CreateCity.state.focus();
        return false;
    }
   else if(document.CreateCity.city.value=="")
   {
     alert("please enter city name");
     document.CreateCity.city.focus();
     return false;
   }
   else if (!isNaN(document.CreateCity.city.value)) {
       alert('Please enter city name in text !!');
       document.CreateCity.city.focus();
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
      <form name="CreateCity" method="POST" action="Control.do?Action=CreateCity">
      <input type="hidden" name="CityVO.CityId" value="<c:out value="${currentCityVO.cityId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">       
        
        <tr>
         <td width="27%" align="left">Country :</td>
  		  <td width="71%" align="left">
          <select size="1" id="country" name="CityVO.CountryId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<countryList.size();i++){ 
	   		  CountryVO countryVO = (CountryVO)countryList.get(i); 
	   		  String selected = "";
	   		  if(countryVO.getCountryId().equals(currentCityVO.getCountryId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=countryVO.getCountryId()%>" <%=selected%>><%=countryVO.getName()%></option>
			<%}%>
			</select>
          </td>
        
        </tr>
        <tr>
         <td width="27%" align="left">State :</td>
  		  <td width="71%" align="left">
          <select size="1" id="state" name="CityVO.StateId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<stateList.size();i++){ 
	   		  StateVO stateVO = (StateVO)stateList.get(i); 
	   		  String selected = "";
	   		  if(stateVO.getStateId().equals(currentCityVO.getStateId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=stateVO.getStateId()%>" <%=selected%>><%=stateVO.getName()%></option>
			<%}%>
			</select>
          </td>
        
        </tr>
        
        <tr>
          <td width="36%" align="left">City Name :</td>
          <td width="61%" align="left"><input type="text" id = "city" name="CityVO.Name" value="<c:out value="${currentCityVO.name}" />" size="20"></td>
        </tr>       
        
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
          <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateCity';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
