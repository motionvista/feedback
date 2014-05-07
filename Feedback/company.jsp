<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCompanyVO" class="com.feedback.vo.CompanyVO" scope="request"/>

<%
    String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
	
	List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
	}
	
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
%>

<table border="0" width="80%">
      <tr>
        <td width="80%" align="center">
                <font color="#FF0000"><%=msg%></font>
        </td>
      </tr>
</table>

<table border="0" width="65%">
    <form name="Company" method="POST" action="Control.do?Action=SearchCompany">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New Company" name="createNewCompany" onClick="form.action='Control.do?Action=GoToCreateCompany';">
      </td>
      </tr>
      
      <tr>
      <td width="100%" align="center">
      
            
      <fieldset><legend>&nbsp;Search Company&nbsp; </legend>
      <table border="0" width="85%">  
       <tr>
       <td> Company Name :</td>
       <td><input type="text" id="companyname" name="CompanyVO.Name" value="<c:out value="${currentCompanyVO.name}" />" size="20"></td>
       <td>
       <input type="submit" value="Search Company" name="B1">
       </tr>  
       <tr>
     
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(companyList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="5" bgcolor="#FFFFFF" align="left"> <%=companyList.size()%>&nbsp;Record(s) found.</td>
  </tr>
<tr>
    <td align="center">Sr No.</td>
    <td align="center">Company Name</td>
    <td align="center">Address</td>
    <td align="center">Contact No</td>
    <td align="center">Action</td>
   </tr>
        <%
        for(int i=0,j=1;i<companyList.size();i++,j++){  
   		  CompanyVO companyVO = (CompanyVO)companyList.get(i);
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=companyVO.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=companyVO.getAddress()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=companyVO.getPhoneNo1()%></td>
          <td bgcolor="#FFFFFF" align="center"><a href="Control.do?Action=EditCompany&CompanyVO.CompId=<%=companyVO.getCompId()%>">Edit</a></td>
   </tr>
		<%}%>
</table>
<% } %>


