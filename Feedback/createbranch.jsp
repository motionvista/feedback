<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentBranchVO" class="com.feedback.vo.BranchVO" scope="request"/>

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
		
	String buttonStr = "Create Branch";
	String labelStr = "Create Branch";
	if(currentBranchVO.getCompId() != null){
		buttonStr = "Save Branch";
		labelStr = "Update Branch";		
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
   
   if(document.Branch.branchname.value=="")
   {
     alert("please enter Branch name");
     document.Branch.branchname.focus();
     return false;
   }
   else if (!isNaN(document.Branch.branchname.value)) {
       alert('Please enter Branch name in text !!');
       document.Branch.branchname.focus();
       return false;
   }else if (document.Branch.company.selectedIndex == 0) {
       alert('Please Select Company!!')
       document.Branch.company.focus();
        return false;
    }else if(document.Branch.phoneno1.value==""){
     alert("please enter phone number 1 ");
     document.Branch.phoneno1.focus();
     return false;
   }
   else if (isNaN(document.Branch.phoneno1.value)) {
       alert('Please enter phone number as numeric!!');
       document.Branch.phoneno1.focus();
       return false;
   }else if (document.Branch.phoneno1.value.length < 10 ) {
        alert('Please enter phone number 10 digit only!!')
        document.Branch.phoneno1.focus();
        return false;
   }
   else if (document.Branch.phoneno1.value.length > 10 ) {
        alert('Please enter phone number 10 digit only!!')
        document.Branch.phoneno1.focus();
        return false;
   }else if (isNaN(document.Branch.phoneno2.value)) {
       alert('Please enter phone number as numeric!!');
       document.Branch.phoneno2.focus();
       return false;
   }
   else if(document.Branch.phoneno2.value != ""){
   
       if (document.Branch.phoneno2.value.length < 10 ) {
        alert('Please enter phone number 10 digit only!!')
        document.Branch.phoneno2.focus();
        return false;
       }
	   else if (document.Branch.phoneno2.value.length > 10 ) {
        alert('Please enter phone no 10 digit only!!')
        document.Branch.phoneno2.focus();
        return false;
      }
	}
   else if(document.Branch.emailid.value=="")
    {
      alert("please enter Email id");
      document.Branch.emailid.focus();
      return false;
    }else if(!filter.test(document.Branch.emailid.value)){
       alert('Please provide a valid email address');
	   document.Branch.emailid.focus();
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
      <form name="Branch" method="POST" action="Control.do?Action=CreateBranch">
      <input type="hidden" name="BranchVO.BranchId" value="<c:out value="${currentBranchVO.branchId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="85%">       
       <tr>
       <td width="16%"> Branch Name :</td>
       <td><input type="text" id="branchname" name="BranchVO.Name" value="<c:out value="${currentBranchVO.name}" />" size="20"></td>
       </tr>  
       <tr>
       </tr>  
       <tr>
      <td>Company :</td>
      <td>
           <select size="1" id="company" name="BranchVO.CompId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		  CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentBranchVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>   
       <tr>
       <td>Phone Number 1 :</td>
       <td><input type="text" id="phoneno1" name="BranchVO.PhoneNo1" value="<c:out value="${currentBranchVO.phoneNo1}" />" size="20"></td>
      </tr>  
               <tr></tr>
      <tr>
       <td>Phone Number 2 :</td>
       <td><input type="text" id="phoneno2" name="BranchVO.PhoneNo2" value="<c:out value="${currentBranchVO.phoneNo2}" />" size="20"></td>
      </tr>
               <tr></tr>
      <tr>
         <td>Email Id :</td>
         <td><input type="text" id="emailid" name="BranchVO.EmailId" value="<c:out value="${currentBranchVO.emailId}" />" size="20"></td>
      </tr>
      <tr></tr>
      <tr>
      <td>Address :</td>
      <td><textarea rows="3" id="address" name="BranchVO.Address" cols="17"><c:out value="${currentBranchVO.address}" /></textarea>
     </td>
      </tr>
      <tr>
      </tr>
     <tr>
      <td>Country :</td>
       <td width="36%" align="left">
          <select size="1" id="country" name="BranchVO.CountryId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<countryList.size();i++){ 
	   		  CountryVO countryVO = (CountryVO)countryList.get(i); 
	   		  String selected = "";
	   		  if(countryVO.getCountryId().equals(currentBranchVO.getCountryId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=countryVO.getCountryId()%>" <%=selected%>><%=countryVO.getName()%></option>
			<%}%>
			</select>
          </td>
      </tr>  <tr>
       </tr>
       <tr>
      <td>State :</td>
      <td width="32%" align="left">
          <select size="1" id="state" name="BranchVO.StateId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<stateList.size();i++){ 
	   		  StateVO stateVO = (StateVO)stateList.get(i); 
	   		  String selected = "";
	   		  if(stateVO.getStateId().equals(currentBranchVO.getStateId())){
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
           <select size="1" id="city" name="BranchVO.CityId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<cityList.size();i++){ 
	   		  CityVO cityVO = (CityVO)cityList.get(i); 
	   		  String selected = "";
	   		  if(cityVO.getCityId().equals(currentBranchVO.getCityId())){
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
      <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateBranch';"></td>
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>



