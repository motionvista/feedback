<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentDeviceVO" class="com.feedback.vo.DeviceVO" scope="request"/>

<%
   
    String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();

    List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
	}
	
	List branchList = (List)request.getAttribute("branchList");
	if(branchList == null){
		branchList = new ArrayList();
	}
	
	List deviceList = (List)request.getAttribute("deviceList");
	if(deviceList == null){
		deviceList = new ArrayList();
	}
    
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script>
     $(document).ready(function(){
     
     $("#company").change(function(){
     fillBranch('branch', this); 
     });
    
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
    
    
  });
</script>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<div align="center">

<table border="0" width="65%">
<form name="SearchDevice" method="POST" action="Control.do?Action=SearchDevice">
    <tr>
    </tr>
    <tr>
      <td width="100%" align="center">
      
      <fieldset><legend>&nbsp;Search Device &nbsp; </legend>
      <table border="0" width="85%">
      
       <tr>
       </tr> 
       <tr>
       
       <td>Company :</td>
       <td>
          <select size="1" id="company" name="DeviceVO.CompId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		 CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentDeviceVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
          </td>
      <td>Branch :</td>
      <td>
           <select size="1" id="branch" name="DeviceVO.BranchId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentDeviceVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
			<%}%>
			</select>
       </td>
       <td >
      <input type="submit" value="Search Device" name="B1">
      </tr>
       </tr>
       <tr></tr>
      
      <tr>
      <td colspan="4" align="right">
     
      </tr>
     </table>
     </fieldset>
     </form>
</table>	
</div>




<br>
 <%if(deviceList != null){%>
 <table border="0" cellspacing="1"  width="80%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="6" bgcolor="#FFFFFF" align="left"> <%=deviceList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
        <td align="center">Sr No</td>
		<td align="center">Device Id</td>
    	<td align="center">Company</td>
    	<td align="center">Branch</td>
        <td align="center">Status</td>
        <td align="center">Date</td>
    	  
        </tr>
       <% for(int i=0,j=1 ;i<deviceList.size(); i++,j++){ 
	     DeviceVO deviceVO = (DeviceVO)deviceList.get(i);
	   %>
        <tr>
         <td  bgcolor="#FFFFFF" align="center"><%=j%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=deviceVO.getDeviceId()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=deviceVO.getCompanyName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=deviceVO.getBranchName()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=deviceVO.getStatusStr()%></td>
         <td  bgcolor="#FFFFFF" align="center"><%=sdf.format(deviceVO.getCreateTime())%></td>
         </tr>
		<%}%>
     </table>
<% } %>

<br>



