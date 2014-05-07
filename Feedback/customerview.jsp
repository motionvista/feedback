<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCustomerVO" class="com.feedback.vo.CustomerVO" scope="request"/>

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
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	String dob="";	
	String buttonStr = "Create Customer";
	String labelStr = "Create Customer";
	if(currentCustomerVO.getCustId() != null){
		buttonStr = "Save Customer";
		labelStr = "Customer Detail";
		if(currentCustomerVO.getDob() != null){
		  dob = sdf.format(currentCustomerVO.getDob());
		}		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg="";
	}
	
	
%>

<SCRIPT type="text/javascript" language="JavaScript">
 
 functiion validation(){
 
 }
</SCRIPT>

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
      <form name="Branch" method="POST" action="Control.do?Action=">
      <input type="hidden" name="CustomerVO.CustId" value="<c:out value="${currentCustomerVO.custId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="85%">       
       <tr>
       <td width="21%"> Customer Name :</td>
       <td width="79%"><input type="text" name="CustomerVO.CustName" value="<c:out value="${currentCustomerVO.custName}" />
       " size="20" readonly></td>
       </tr> 
       <tr></tr> 
       <tr>
       <td>Mobile Number  :</td>
       <td><input type="text" name="CustomerVO.MobileNo" value="<c:out value="${currentCustomerVO.mobileNo}" />
       " size="20" readonly></td>
       </tr>
       <tr></tr>
      <tr>
         <td>Email Id :</td>
         <td><input type="text" name="CustomerVO.EmailId" value="<c:out value="${currentCustomerVO.emailId}" />
         " size="20" readonly></td>
      </tr>
      <tr></tr>
      <tr>
      <td>Date of Birth :</td>
      <td><input type="text" name="CustomerVO.Dob" value="<%=dob%>"  size="20" readonly> </td>
      </tr>
      <tr>
      </tr> 
       <tr></tr>
      <tr>
      <td>Maritial Status :</td>
      <td><input type="text" name="CustomerVO.MaritialStatus" value="<c:out value="${currentCustomerVO.maritialStatus}" />
      " size="20" readonly> </td>
      </tr>
      <tr>
      </tr>  
       <tr></tr>
      <tr>
      <td>Occupation :</td>
      <td><input type="text" name="CustomerVO.Occupation" value="<c:out value="${currentCustomerVO.occupation}" />
      " size="20" readonly> </td>
      </tr>
      <tr>
      </tr> 
       <tr></tr>
      <tr>
      <td>Location :</td>
      <td><input type="text" name="CustomerVO.Location" value="<c:out value="${currentCustomerVO.location}" />
      " size="20" readonly> </td>
      </tr>
      <tr>
      </tr>  
       <tr>
      <td>Company :</td>
      <td>
           <select size="1" id="company" name="CustomerVO.CompId" readonly>
       	   <option value="" >--Select--</option>
           <%for(int i=0;i<companyList.size();i++){ 
	   		  CompanyVO companyVO = (CompanyVO)companyList.get(i); 
	   		  String selected = "";
	   		  if(companyVO.getCompId().equals(currentCustomerVO.getCompId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option  value="<%=companyVO.getCompId()%>" <%=selected%>><%=companyVO.getName()%></option>
			<%}%>
			</select>
       </td>
       </tr>
        <tr>
      </tr>     
       <tr>
      <td>Branch :</td>
      <td>
           <select size="1" id="branch" name="CustomerVO.BranchId">
       	   <option value="">--All--</option>
           <%for(int i=0;i<branchList.size();i++){ 
	   		  BranchVO branchVO = (BranchVO)branchList.get(i); 
	   		  String selected = "";
	   		  if(branchVO.getBranchId().equals(currentCustomerVO.getBranchId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=branchVO.getBranchId()%>" <%=selected%>><%=branchVO.getName()%></option>
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
      <%--
      <input type="submit" value="<%=buttonStr%>" name="B1">
      --%>
       <input type="submit" value="Back" onClick="form.action='Control.do?Action=GoToCustomer';">
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>



