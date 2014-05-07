<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCustomerCriteriaVO" class="com.feedback.vo.CustomerCriteriaVO" scope="request"/>

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
	
	List customerList = (List)request.getAttribute("customerList");
	if(customerList == null){
		customerList = new ArrayList();
	}
	
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
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

<table border="0" width="47%">
<form name="SearchCustomer" method="POST" action="Control.do?Action=SearchCustomer">
      <tr>
      </tr> 

    <tr>
      <td width="100%" align="center">
      
      <fieldset>
      <legend>&nbsp;Search Customer &nbsp; </legend>
      <table width="571"  border="0">
            
       <tr>
       <td width="39%"> Company :</td>
       <td width="31%">
           <select size="1" id="company" name="CustomerCriteriaVO.CompId">
         	<option value="">--All--</option>
					<%for(int i=0;i<companyList.size();i++){ 
			   	       CompanyVO companyVO  = (CompanyVO)companyList.get(i); 
			   	        String selectStr = "";
				        if(companyVO.getCompId().equals(currentCustomerCriteriaVO.getCompId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=companyVO.getCompId()%>" <%=selectStr%> ><%=companyVO.getName()%></option>
				     <%}%>
			</select>
       </td>
       <td>Branch :</td>
      <td>
           <select size="1" id="branch" name="CustomerCriteriaVO.BranchId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentCustomerCriteriaVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>  
       <tr>
       <td width="20%"> Customer Name :</td>
       <td width="30%"><input type="text" name="CustomerCriteriaVO.CustomerName" value="<c:out value="${currentCustomerCriteriaVO.customerName}" />" size="20"></td>
       <td width="20%"> Location :</td>
       <td width="30%"><input type="text" name="CustomerCriteriaVO.Location" value="<c:out value="${currentCustomerCriteriaVO.location}" />" size="20"></td>
       </tr>  
        <tr>
        <td colspan="4" align="right">
         <input type="submit" value="Search Customer" name="B1">
        </td>
        </tr>
     </table>
     </fieldset>
     </form>
</table>	
</div>

<br>
 <% if(customerList!= null){ %>
 <table border="0" cellspacing="1"  width="80%" bgcolor="<%=darkColor%>">
 <tr>
  <td colspan="7" bgcolor="#FFFFFF" align="left"> <%=customerList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">Customer Name</td>
          <td align="center">Mobile Number</td>
          <td align="center">Email Id</td>
          <td align="center">Location</td>
      	  <td align="center">Occupation</td>
          <td align="center">Action</td>
          
        </tr>
        <%
        for(int i=0,j=1;i<customerList.size();i++,j++){  
   		  CustomerVO customerVO = (CustomerVO)customerList.get(i);
		 %>
        <tr>
          <td bgcolor="#FFFFFF" align="center"><%=j%></td>
           <td bgcolor="#FFFFFF" align="center"><%=customerVO.getCustName()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=customerVO.getMobileNo()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=customerVO.getEmailId()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=customerVO.getLocation()%></td>
           <td bgcolor="#FFFFFF" align="center"><%=customerVO.getOccupation()%></td>
           <td bgcolor="#FFFFFF" align="center"><a href="Control.do?Action=ViewCustomer&CustomerVO.CustId=<%=customerVO.getCustId()%>">View</a></td>
        </tr>
		<%}%>
     </table>
<% } %>

<br>
