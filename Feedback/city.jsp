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
	
	List countryList = (List)request.getAttribute("countryList");
	if(countryList == null){
		countryList = new ArrayList();
	}
	
	List stateList = (List)request.getAttribute("stateList");
	if(stateList == null){
		stateList = new ArrayList();
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
<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<table border="0" width="65%">
      <form name="SearchCity" method="POST" action="Control.do?Action=SearchCity">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Add New City" name="createNewCity" onClick="form.action='Control.do?Action=GoToCreateCity';">
      </td>
      </tr>
      <tr>
      <td width="100%" align="center">
      <fieldset><legend>&nbsp;Search City&nbsp; </legend>
      <table border="0" width="85%">       
        <tr>
          <td  align="left">Country :</td>
  		  <td  align="left">
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
         <td  align="left">State :</td>
  		 <td align="left">
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
         <td  align="left">City Name :</td>
         <td  align="left"><input type="text" id = "city" name="CityVO.Name" value="<c:out value="${currentCityVO.name}" />" size="20"></td>
          <td colspan="2" align="right">
          <input type="submit" value="Search City" name="B1"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(cityList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="5" bgcolor="#FFFFFF" align="left"> <%=cityList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">City</td>
          <td align="center">State</td>
          <td align="center">Country</td>
          <td align="center">Action</td>
        </tr>
        <%
        for(int i=0,j=1;i<cityList.size();i++,j++){  
   		  CityVO cityVO = (CityVO)cityList.get(i);
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=cityVO.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=cityVO.getStateName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=cityVO.getCountryName()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditCity&CityVO.CityId=<%=cityVO.getCityId()%>">Edit</a></td>
        </tr>
		<%}%>
     </table>
<% } %>