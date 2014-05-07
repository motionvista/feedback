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

	List typeList = new ArrayList();
	typeList.add(TypeVO.OBJECTIVE);
	
	String buttonStr = "Create Question";
	String labelStr = "Create Question";
	if(currentQuestionVO.getQuestionId() != null){
		buttonStr = "Save Question";
		labelStr = "Edit Question";		
	}
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	currentQuestionVO.setLable1("Bad");
	currentQuestionVO.setLable2("Ok");
	currentQuestionVO.setLable3("Good");
    currentQuestionVO.setLable4("Excellent");
    currentQuestionVO.setRating1(new Integer(1));
    currentQuestionVO.setRating2(new Integer(2));
    currentQuestionVO.setRating3(new Integer(3));
    currentQuestionVO.setRating4(new Integer(4));
%>

<script type="text/javascript">
function validate(){
   if(document.CreateQuestion.que.value == ""){
       alert('Please enter Question !!');
	   document.CreateQuestion.que.focus();
	   return false;
   }else if (document.CreateQuestion.type.selectedIndex == 0) {
       alert('Please Select Question Type!!');
       document.CreateQuestion.type.focus();
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

<table border="0" width="53%">
<tr>
      <td width="100%" align="center">
      <form name="CreateQuestion" method="POST" action="Control.do?Action=CreateQuestion">
      <input type="hidden" name="QuestionVO.QuestionId" value="<c:out value="${currentQuestionVO.questionId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="86%">       
        
        <tr>
         <td width="36%" align="left">Question :</td>
          <td width="61%" align="left"><input type="text" id="que" name="QuestionVO.Question" value="<c:out value="${currentQuestionVO.question}" />
          " size="40"></td>
        </tr>
        
         <tr>
          <td width="36%" align="left">Type :</td>
          <td width="61%" align="left">
              <select size="1" id="type" name="QuestionVO.Type">
       	     <option value="">--Select--</option>
             <%for(int i=0;i<typeList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)typeList.get(i); 
              String selected = "";
	   		  if(typeVO.getId().equals(currentQuestionVO.getType())){
		   		  selected = "selected";
	   		  }
	   		 
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
          </td>
        </tr> 
       <tr>
       <td colspan="2" align="center" >
       <table>
        <tr>
          <td width="36%" align="center">Lables</td>
          <td width="36%" align="left">Rating </td>
        </tr>       
        <tr>
          <td width="61%" align="center"><input type="text" id = "l1" editable="false" name="QuestionVO.Lable1" value="<c:out value="${currentQuestionVO.lable1}" />" size="20" readonly></td>
           <td width="61%" align="left"><input type="text" id = "l1" name="QuestionVO.Rating1" value="<c:out value="${currentQuestionVO.rating1}" />" size=3" readonly></td>
        </tr>       
        <tr>
          <td width="61%" align="center"><input type="text" id = "l2" name="QuestionVO.Lable2" value="<c:out value="${currentQuestionVO.lable2}" />" size="20" readonly></td>
          <td width="61%" align="left"><input type="text" id = "l1" name="QuestionVO.Rating2" value="<c:out value="${currentQuestionVO.rating2}" />" size="3" readonly></td>
        </tr>       
        <tr>
          <td width="61%" align="center"><input type="text" id = "l3" name="QuestionVO.Lable3" value="<c:out value="${currentQuestionVO.lable3}" />" size="20" readonly></td>
           <td width="61%" align="left"><input type="text" id = "l1" name="QuestionVO.Rating3" value="<c:out value="${currentQuestionVO.rating3}" />" size="3" readonly></td>
        </tr>  
        <tr>     
         <td width="61%" align="center"><input type="text" id = "l4" name="QuestionVO.Lable4" value="<c:out value="${currentQuestionVO.lable4}" />" size="20" readonly></td>
          <td width="61%" align="left"><input type="text" id = "l1" name="QuestionVO.Rating4" value="<c:out value="${currentQuestionVO.rating4}" />" size="3" readonly></td>
        </tr>
       </table>
       </td>
       </tr>

       
        
         <tr>
         <td width="36%" align="left">Analytics Keyword :</td>
          <td width="61%" align="left"><input type="text" id="keyword" name="QuestionVO.Keyword" value="<c:out value="${currentQuestionVO.keyword}"/>
          " size="20"></td>
        </tr>
        <tr>
          <td colspan="2" align="center">
          <input type="submit" value="<%=buttonStr%>" name="B1" onClick="return validate();">
          <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateQuestion';"></td>
        </tr>
      </table></fieldset>
      </form>
      </td>
    </tr>
 </table>
