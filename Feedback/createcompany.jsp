<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCompanyVO" class="com.feedback.vo.CompanyVO" scope="request"/>

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
	
	List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
	}
		
	String buttonStr = "Create Company";
	String labelStr = "Create Company";
	if(currentCompanyVO.getCompId() != null){
		buttonStr = "Save Company";
		labelStr = "Update Company";		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg="";
	}
	
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script>
     $(document).ready(function(){
     
     $("#country").change(function(){
     fillState('state', this); 
     });
     
     $("#state").change(function(){
     fillCity('city', this); 
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
    
    function fillCity(ddId, callingElement) {
      var dd = $('#' + ddId);
      var stateid=$(callingElement).val();
         $.getJSON('WebServlet.do?Action=GetCities&stateid='+stateid, function(data) {
                 $('>option', dd).remove();
             if (data) {
                  $.each(data, function(i, data) {
                     dd.append($('<option/>').val(data.cityId).text(data.name));
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

   var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
   
   if(document.Company.companyname.value=="")
   {
     alert("please enter company name");
     document.Company.companyname.focus();
     return false;
   }
   else if (!isNaN(document.Company.companyname.value)) {
       alert('Please enter company name in text !!');
       document.Company.companyname.focus();
       return false;
   }else if(document.Company.phoneno1.value=="")
   {
     alert("please enter phone number 1 ");
     document.Company.phoneno1.focus();
     return false;
   }
   else if (isNaN(document.Company.phoneno1.value)) {
       alert('Please enter phone number 1 as numeric!!');
       document.Company.phoneno1.focus();
       return false;
   }else if (document.Company.phoneno1.value.length < 10 ) {
        alert('Please enter phone number  10 digit only!!')
        document.Company.phoneno1.focus();
        return false;
   }
   else if (document.Company.phoneno1.value.length > 10 ) {
        alert('Please enter phone number 10 digit only!!')
        document.Company.phoneno1.focus();
        return false;
   }else if (isNaN(document.Company.phoneno2.value)) {
       alert('Please enter phone number as numeric!!');
       document.Company.phoneno2.focus();
       return false;
   }
   else if(document.Company.phoneno2.value != ""){
   
       if (document.Company.phoneno2.value.length < 10 ) {
        alert('Please enter phone number 10 digit only!!')
        document.Company.phoneno2.focus();
        return false;
       }
	   else if (document.Company.phoneno2.value.length > 10 ) {
        alert('Please enter phone number 10 digit only!!')
        document.Company.phoneno2.focus();
        return false;
      }
	}
   else if(document.Company.emailid.value==""){
      alert("please enter Email id");
      document.Company.emailid.focus();
      return false;
    }else if(!filter.test(document.Company.emailid.value)){
       alert('Please provide a valid email address');
	   document.Company.emailid.focus();
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

<table border="0" width="65%">
    <tr>
      <td width="100%" align="center">
      <form name="Company" method="POST" action="Control.do?Action=CreateCompany">
      <input type="hidden" name="CompanyVO.CompId" value="<c:out value="${currentCompanyVO.compId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="85%">       
       <tr>
       <td width="16%"> Company Name :</td>
       <td><input type="text" id="companyname" name="CompanyVO.Name" value="<c:out value="${currentCompanyVO.name}" />" size="20"></td>
       </tr>  
       <tr>
       </tr>     
       <tr>
       <td>Phone Number 1 :</td>
       <td><input type="text" id="phoneno1" name="CompanyVO.PhoneNo1" value="<c:out value="${currentCompanyVO.phoneNo1}" />" size="20"></td>
      </tr>  
               <tr></tr>
      <tr>
       <td>Phone Number 2 :</td>
       <td><input type="text" id="phoneno2" name="CompanyVO.PhoneNo2" value="<c:out value="${currentCompanyVO.phoneNo2}" />" size="20"></td>
      </tr>
               <tr></tr>
      <tr>
         <td>Email Id :</td>
         <td><input type="text" id="emailid" name="CompanyVO.EmailId" value="<c:out value="${currentCompanyVO.emailId}" />" size="20"></td>
      </tr>
      <tr></tr>
      <tr>
      <td>Address :</td>
      <td><textarea rows="3" id="address"  name="CompanyVO.Address" cols="17"><c:out value="${currentCompanyVO.address}" /></textarea>
     </td>
      </tr>
      <tr>
      </tr>
       <tr>
      <td>Country :</td>
       <td width="36%" align="left">
          <select size="1" id="country" name="CompanyVO.CountryId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<countryList.size();i++){ 
	   		  CountryVO countryVO = (CountryVO)countryList.get(i); 
	   		  String selected = "";
	   		  if(countryVO.getCountryId().equals(currentCompanyVO.getCountryId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=countryVO.getCountryId()%>" <%=selected%>><%=countryVO.getName()%></option>
			<%}%>
			</select>
          </td>
      </tr>
       <tr>
       </tr>
       <tr>
      <td>State :</td>
      <td width="32%" align="left">
          <select size="1" id="state" name="CompanyVO.StateId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<stateList.size();i++){ 
	   		  StateVO stateVO = (StateVO)stateList.get(i); 
	   		  String selected = "";
	   		  if(stateVO.getStateId().equals(currentCompanyVO.getStateId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=stateVO.getStateId()%>" <%=selected%>><%=stateVO.getName()%></option>
			<%}%>
			</select>
          </td>
      
      </tr>
      <tr>
      </tr>
     <tr>
      <td>City :</td>
      <td>
           <select size="1" id="city" name="CompanyVO.CityId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<cityList.size();i++){ 
	   		  CityVO cityVO = (CityVO)cityList.get(i); 
	   		  String selected = "";
	   		  if(cityVO.getCityId().equals(currentCompanyVO.getCityId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=cityVO.getCityId()%>" <%=selected%>><%=cityVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>
      <tr>
      <td>&nbsp;</td>
      <td></td>
      </tr>
      <tr>
      <td colspan="2" align="center">
      <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
      <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateCompany';"></td>
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
