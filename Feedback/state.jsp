<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentStateVO" class="com.feedback.vo.StateVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List stateList = (List)request.getAttribute("stateList");
	if(stateList == null){
		stateList = new ArrayList();
	}
	
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

<table border="0" width="60%">
    <form name="SearchState" method="POST" action="Control.do?Action=SearchState">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Add New State" name="createNewState" onClick="form.action='Control.do?Action=GoToCreateState';">
      </td>
      </tr>
      
      <tr>
      <td width="100%" align="center">
      <fieldset><legend>&nbsp;Search State </legend>
      <table border="0" width="85%">       
        
        <tr>
         <td  align="left">State Name :</td>
         <td  align="left"><input type="text" id = "state" name="StateVO.Name" value="<c:out value="${currentStateVO.name}" />" size="20"></td>
         <td  align="left">Country :</td>
  		 <td align="left">
          <select size="1" id="country" name="StateVO.CountryId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<countryList.size();i++){ 
	   		  CountryVO countryVO = (CountryVO)countryList.get(i); 
	   		  String selected = "";
	   		  if(countryVO.getCountryId().equals(currentStateVO.getCountryId())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=countryVO.getCountryId()%>" <%=selected%>><%=countryVO.getName()%></option>
			<%}%>
			</select>
          </td>
        
        </tr>
        
        <tr>
          <td colspan="4" align="right">
          <input type="submit" value="Search State" name="B1"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(stateList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="4" bgcolor="#FFFFFF" align="left"> <%=stateList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">State</td>
          <td align="center">Country</td>
          <td align="center">Action</td>
        </tr>
        <%
        for(int i=0,j=1;i<stateList.size();i++,j++){  
   		  StateVO stateVO = (StateVO)stateList.get(i);
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=stateVO.getName()%></td>
          <td align="center" bgcolor="#FFFFFF"><%=stateVO.getCountryName()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditState&StateVO.StateId=<%=stateVO.getStateId()%>">Edit</a></td>
        </tr>
		<%}%>
     </table>
<% } %>