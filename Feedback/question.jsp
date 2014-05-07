<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentQuestionVO" class="com.feedback.vo.QuestionVO" scope="request"/>

<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List questionList = (List)request.getAttribute("questionList");
	if(questionList == null){
		questionList = new ArrayList();
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
<form name="SearchQuestion" method="POST" action="Control.do?Action=SearchQuestion">
      <tr>
      <td colspan="4" align="right">
      <input type="submit" value="Create New Question" name="createNewQuestion" onClick="form.action='Control.do?Action=GoToCreateQuestion';">
      </td>
      </tr>
      <tr>
      <td width="100%" align="center">
      <form name="SearchQuestion" method="POST" action="Control.do?Action=SearchQuestion">
            
      <fieldset><legend>&nbsp;Search Question&nbsp; </legend>
      <table border="0" width="86%">       
        
        <tr>
         <td width="36%" align="left">Question :</td>
          <td width="61%" align="left"><input type="text" id="que" name="QuestionVO.Question" value="<c:out value="${currentQuestionVO.question}" />
          " size="40"></td>
          <td>
          <input type="submit" value="Search Question" name="B1"></td>
        </tr>
        
       </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
<br>
 <% if(questionList!= null){ %>
 <table border="0" cellspacing="1"  width="78%" bgcolor="<%=darkColor%>">
 <tr>
 <td width="100%" colspan="3" bgcolor="#FFFFFF" align="left"> <%=questionList.size()%>&nbsp;Record(s) found.</td>
  </tr>
        <tr>
          <td align="center">Sr No.</td>
          <td align="center">Question</td>
          <td align="center">Action</td>
        </tr>
        <%
        for(int i=0,j=1;i<questionList.size();i++,j++){  
   		  QuestionVO questionVO = (QuestionVO)questionList.get(i);
		 %>
        <tr>
          <td align="center" bgcolor="#FFFFFF"><%=j%></td>
          <td align="center" bgcolor="#FFFFFF"><%=questionVO.getQuestion()%></td>
          <td align="center" bgcolor="#FFFFFF"><a href="Control.do?Action=EditQuestion&QuestionVO.QuestionId=<%=questionVO.getQuestionId()%>">Edit</a></td>
        </tr>
		<%}%>
     </table>
<% } %>