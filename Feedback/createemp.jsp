<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentEmpVO" class="com.feedback.vo.EmpVO" scope="request"/>

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
	
	List branchList = (List)request.getAttribute("branchList");
	if(branchList == null){
		branchList = new ArrayList();
	}
	
	List roleList= (List)request.getAttribute("roleList");
	if(roleList== null){
		roleList= new ArrayList();
	}
	String dob = "";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	Integer gender = new Integer(1);
	String buttonStr = "Create Employee";
	String labelStr = "Create Employee";
	if(currentEmpVO.getEmpId() != null){
		buttonStr = "Save Employee";
		labelStr = "Update Employee";
		dob = sdf.format(currentEmpVO.getDob());	
		gender = currentEmpVO.getGender();
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg="";
	}
	String genaderMale ="";
	String genaderFeMale ="";
	if(gender == 1){
	  genaderMale="checked";
	  genaderFeMale="unchecked";
	}else if(gender == 2){
	  genaderMale="unchecked";
	  genaderFeMale="checked";
	}
	
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script language="JavaScript" src="calendar1.js"></script>
 <link href="CalendarControl.css" rel="stylesheet" type="text/css">
 <script src="CalendarControl.js" language="javascript"></script>
<script>
     $(document).ready(function(){
     
     $("#country").change(function(){
     fillState('state', this); 
     });
     
     $("#state").change(function(){
     fillCity('city', this); 
     });
     
     $("#company").change(function(){
     fillBranch('branch', this); 
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
    
    function fillBranch(ddId, callingElement) {
      var dd = $('#' + ddId);
      var companyid=$(callingElement).val();
         $.getJSON('WebServlet.do?Action=GetBranches&companyid='+companyid, function(data) {
                 $('>option', dd).remove();
             if (data) {
                  $.each(data, function(i, data) {
                     dd.append($('<option/>').val(data.branchId).text(data.name));
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
   
   var date = document.getElementById('dob').value.split("/");
   var dob = new Date(date[2], date[1] - 1, date[0]);
   var today = new Date();
   
   if(document.CreateEmp.firstname.value=="")
   {
     alert("please enter first name");
     document.CreateEmp.firstname.focus();
     return false;
   }
   else if (!isNaN(document.CreateEmp.firstname.value)) {
       alert('Please enter first name in text !!');
       document.CreateEmp.firstname.focus();
       return false;
   }
    else if(document.CreateEmp.lastname.value=="")
    {
      alert("please enter last name");
      document.CreateEmp.lastname.focus();
      return false;
    }
	 else if (!isNaN(document.CreateEmp.lastname.value)) {
       alert('Please enter last name in text!!');
       document.CreateEmp.lastname.focus();
       return false;
   }
	else if(document.CreateEmp.phoneno.value=="")
    {
      alert("please enter phone number");
      document.CreateEmp.phoneno.focus();
      return false;
    }
	else if (isNaN(document.CreateEmp.phoneno.value)) {
       alert('Please enter phone number as numeric!!');
       document.CreateEmp.phoneno.focus();
       return false;
    }
    else if (document.CreateEmp.phoneno.value.length < 10 ) {
        alert('Please enter phone as 10 digit only!!')
        document.CreateEmp.phoneno.focus();
        return false;
    }
    else if (document.CreateEmp.phoneno.value.length > 10 ) {
        alert('Please enter mobile as 10 digit only!!')
        document.CreateEmp.phoneno.focus();
        return false;
    }  else if(document.CreateEmp.mobileno.value=="")
    {
      alert("please enter mobile number");
      document.CreateEmp.mobileno.focus();
      return false;
    }
	else if (isNaN(document.CreateEmp.mobileno.value)) {
       alert('Please enter phone number as numeric!!');
       document.CreateEmp.mobileno.focus();
       return false;
    }
    else if (document.CreateEmp.mobileno.value.length < 10 ) {
        alert('Please enter mobile as 10 digit only!!')
        document.CreateEmp.mobileno.focus();
        return false;
    }
    else if (document.CreateEmp.mobileno.value.length > 10 ) {
        alert('Please enter mobile as 10 digit only!!')
        document.CreateEmp.mobileno.focus();
        return false;
    }
	else if(document.CreateEmp.emailid.value=="")
    {
      alert("please enter Email id");
      document.CreateEmp.emailid.focus();
      return false;
    }else if(!filter.test(document.CreateEmp.emailid.value)){
       alert('Please provide a valid email address');
	   document.CreateEmp.emailid.focus();
       return false;
	}
	else if(document.CreateEmp.dob.value=="")
    {
      alert("please enter Date of Birth");
      document.CreateEmp.dob.focus();
      return false;
    }
    else if(dob>today){
      alert("please enter Valid Date of Birth");
      document.CreateEmp.dob.focus();
      return false;
    }
	else if (document.CreateEmp.role.selectedIndex == 0) {
       alert('Please Select Role!!')
       document.CreateEmp.role.focus();
                return false;
    }
	else if(document.CreateEmp.loginid.value=="")
    {
     alert("please enter Login id");
     document.CreateEmp.loginid.focus();
     return false;
    }
    else if(document.CreateEmp.password.value=="")
    {
      alert("please enter password");
      document.CreateEmp.password.focus();
      return false;
    }
}
</script>
<script type="text/javascript">
function getState(countryId){
   alert(countryId);
   $.ajax({
   type:"post",
   url:"Control.do&Action=GoTo",
   data:datastr,
   success:function(msg)
   {

	   
   }
  });
}
</script>
<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<table border="0" width="70%">
    <tr>
      <td width="100%" align="center">
      <form name="CreateEmp" method="POST" action="Control.do?Action=CreateEmp">
      <input type="hidden" name="EmpVO.EmpId" value="<c:out value="${currentEmpVO.empId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="85%">       
       <tr>
       <td width="16%"> First Name :</td>
       <td><input type="text" id="firstname" name="EmpVO.FirstName" value="<c:out value="${currentEmpVO.firstName}" />" size="20"></td>
       <td width="16%">Last Name :</td>
       <td><input type="text" id="lastname" name="EmpVO.LastName" value="<c:out value="${currentEmpVO.lastName}" />" size="20"></td>
       </tr>  
       <tr>
       </tr>     
       <tr>
       <td>Phone No :</td>
       <td><input type="text" id="phoneno" name="EmpVO.PhoneNo" value="<c:out value="${currentEmpVO.phoneNo}" />" size="20"></td>
       <td>Mobile No :</td>
       <td><input type="text" id="mobileno" name="EmpVO.MobileNo" value="<c:out value="${currentEmpVO.mobileNo}" />" size="20"></td>
      </tr>  
               <tr></tr>
      <tr>
      <td>Email Id :</td>
      <td><input type="text" id="emailid" name="EmpVO.EmailId" value="<c:out value="${currentEmpVO.emailId}" />" size="20"></td>
      
      </tr>
               <tr></tr>
      <tr>
      <td>Address :</td>
      <td><input type="text" id="address" name="EmpVO.Address" value="<c:out value="${currentEmpVO.address}" />" size="20"></td>
      <td>Country :</td>
       <td width="36%" align="left">
          <select size="1" id="country" name="EmpVO.CountryId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<countryList.size();i++){ 
	   		  CountryVO countryVO = (CountryVO)countryList.get(i); 
	   		  String selected = "";
	   		  if(countryVO.getCountryId().equals(currentEmpVO.getCountryId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=countryVO.getCountryId()%>" <%=selected%>><%=countryVO.getName()%></option>
			<%}%>
			</select>
          </td>
      </tr>
              <tr></tr>
       
      <tr>
      <td>State :</td>
      <td width="32%" align="left">
          <select size="1" id="state" name="EmpVO.StateId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<stateList.size();i++){ 
	   		  StateVO stateVO = (StateVO)stateList.get(i); 
	   		  String selected = "";
	   		  if(stateVO.getStateId().equals(currentEmpVO.getStateId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=stateVO.getStateId()%>" <%=selected%>><%=stateVO.getName()%></option>
			<%}%>
			</select>
          </td>
      <td>City :</td>
      <td>
           <select size="1" id="city" name="EmpVO.CityId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<cityList.size();i++){ 
	   		  CityVO cityVO = (CityVO)cityList.get(i); 
	   		  String selected = "";
	   		  if(cityVO.getCityId().equals(currentEmpVO.getCityId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=cityVO.getCityId()%>" <%=selected%>><%=cityVO.getName()%></option>
			<%}%>
			</select>
       </td>
      </tr>
             <tr></tr>
      <tr>
      <td>Gender :</td>
      <td>
      <input type="radio" name="EmpVO.Gender" <%=genaderMale%>  value="1">Male
      <input type="radio" name="EmpVO.Gender" <%=genaderFeMale%> value="2">Female
      </td>
      <td>Date of birth :</td>
      <td>
      <!--<input type="text" name="EmpVO.Dob" value="<c:out value="${currentEmpVO.dob}" />" size="20">-->
      <!-- <input type="text" id="dob" name="EmpVO.Dob" size="20" value=<%=dob%>>&nbsp;<a href="javascript:dob.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
      <input type="text" id="dob" name="EmpVO.Dob" size="20" onfocus="showCalendarControl(this);" value=<%=dob%>>
      </td> 
      </tr>
               <tr></tr>
               
      <tr>
      <td>Company :</td>
      <td width="32%" align="left">
          <select size="1" id="company" name="EmpVO.CompId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		 CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentEmpVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
          </td>
      <td>Branch :</td>
      <td>
           <select size="1" id="branch" name="EmpVO.BranchId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentEmpVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
			<%}%>
			</select>
       </td>
      </tr>
      <tr></tr>
      <tr>
      <td>Role :</td>
      <td width="32%" align="left">
          <select size="1" id="role" name="EmpVO.RoleId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<roleList.size();i++){ 
	   		 RoleVO roleVO = (RoleVO)roleList.get(i); 
	   		  String selected = "";
	   		  if(roleVO.getId().equals(currentEmpVO.getRoleId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=roleVO.getId()%>" <%=selected%>><%=roleVO.getName()%></option>
			<%}%>
			</select>
          </td>
      </tr>
      <tr>
      </tr>
      <tr>
      <td>Login Id :</td>
      <td><input type="text" id="loginid" name="EmpVO.LoginId" value="<c:out value="${currentEmpVO.loginId}" />" size="20"></td>
      <td>Password :</td>
      <td><input type="password" id="password" name="EmpVO.Password" value="<c:out value="${currentEmpVO.password}" />" size="20"></td>
      </tr>
      <tr>
      <td>&nbsp;</td>
      <td>&nbsp;
      </td>
      </tr>
             <tr></tr>
      <tr>
      <td colspan="4" align="center">
      <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
      <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateEmp';"></td>
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<script language="JavaScript">
   <!-- // create calendar object(s) just after form tag closed
     // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
     // note: you can have as many calendar objects as you need for your application
 
   var dob= new calendar1(document.forms['CreateEmp'].elements['dob'],new Date());
   dob.year_scroll = true;
   dob.time_comp = false;
   
   //-->
 </script>


