<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentCountryVO" class="com.feedback.vo.CountryVO" scope="request"/>

<%
    String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
	
	List countryList = (List)request.getAttribute("countryList");
	if(countryList == null){
		countryList = new ArrayList();
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
    <form name="SearchCountry" method="POST" action="Control.do?Action=SearchCountry">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Add New Country" name="createNewCompany" onClick="form.action='Control.do?Action=GoToCreateCountry';">
      </td>
      </tr>
      
      <tr>
      <td width="100%" align="center">
      
            
      <fieldset><legend>&nbsp;Search Country&nbsp; </legend>
      <table border="0" width="85%">  
       <tr>
       <td> Country Name :</td>
       <td><input type="text" id="country" name="CountryVO.Name" value="<c:out value="${currentCountryVO.name}" />" size="20"></td>
       <td>
       <input type="submit" value="Search Country" name="B1">
       </tr>  
       <tr>
     
      </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(countryList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="5" bgcolor="#FFFFFF" align="left"> <%=countryList.size()%>&nbsp;Record(s) found.</td>
  </tr>
<tr>
    <td align="center">Sr No.</td>
    <td align="center">Country Name</td>
    <td align="center">Action</td>
   </tr>
        <%
        for(int i=0,j=1;i<countryList.size();i++,j++){  
   		   CountryVO countryVO = (CountryVO)countryList.get(i);
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=countryVO.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditCountry&CountryVO.CountryId=<%=countryVO.getCountryId()%>">Edit</a></td>
   </tr>
		<%}%>
</table>
<% } %>


