<%@page import="java.util.*,com.feedback.common.ApplicationContext,com.feedback.vo.*,java.text.*" %>
<%@ taglib uri="jstl-c.tld" prefix="c" %>
<jsp:useBean id="currentFormVO" class="com.feedback.vo.FormVO" scope="request"/>
<jsp:useBean id="currentFormQuestionVO" class="com.feedback.vo.FormQuestionVO" scope="request"/>
<%
	String darkColor = ApplicationContext.getColor1();
	String lightColor = ApplicationContext.getColor2();
		
	List formList = (List)request.getAttribute("formList");
	if(formList == null){
		formList = new ArrayList();
	}
	
	List questionList = (List)request.getAttribute("questionList");
	if(questionList == null){
		questionList = new ArrayList();
	}
	
	List companyList = (List)request.getAttribute("companyList");
	if(companyList == null){
		companyList = new ArrayList();
	}
	List branchList = (List)request.getAttribute("branchList");
	if(branchList == null){
		branchList = new ArrayList();
	}
	
	FormVO formVO  = null;
	FormQuestionVO[] fq = new FormQuestionVO[0];
	
	if(request.getSession().getAttribute("formVO") != null){
		formVO  = (FormVO)request.getSession().getAttribute("formVO");
		fq = formVO.getQuestions();
	}
	
	List statusList = new ArrayList();
	statusList.add(TypeVO.ACTIVE);
	statusList.add(TypeVO.INACTIVE);
	
	String validFromDate = "";
    String validToDate = "";
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	String buttonStr = "Create Form";
	String labelStr = "Create Form";
    String lblQue = "Add Questions";
	if(currentFormVO.getFormId() != null){
		buttonStr = "Save Form";
		labelStr = "Form Details";	
		lblQue = "Questions";	
		try{
		 validFromDate = sdf.format(currentFormVO.getValidFromDate());
		 validToDate = sdf.format(currentFormVO.getValidToDate());
		}catch(Exception e){
		   System.out.println("Exception "+e.getMessage());
		}
	}
	
	QuestionVO questionVO = null;
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
	
	
%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script language="JavaScript" src="calendar1.js"></script>
<link href="CalendarControl.css" rel="stylesheet" type="text/css">
<script src="CalendarControl.js" language="javascript"></script>

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
<script type="text/javascript">
function validate1(){
   if (document.AddQuestion.questionname.selectedIndex == 0) {
       alert('Please Select Question!!')
       document.AddQuestion.questionname.focus();
        return false;
    }
}
function validate2(){

   var fdate = document.getElementById('validFromDate').value.split("/");
   var fromdate = new Date(fdate[2], fdate[1] - 1, fdate[0]);
   
   var tdate = document.getElementById('validToDate').value.split("/");
   var todate = new Date(tdate[2], tdate[1] - 1, tdate[0]);
   
   if(document.CreateForm.formname.value=="")
   {
     alert("please enter Form name");
     document.CreateForm.formname.focus();
     return false;
   }
   else if (document.CreateForm.company.selectedIndex == 0) {
       alert('Please Select Company!!')
       document.CreateForm.company.focus();
        return false;
    }
    else if(fromdate>todate){
      alert("valid from date should be small than valid to date!!!");
      document.CreateForm.validFromDate.focus();
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

<table border="0" width="58%">

<td width="100%" align="center">
      <form name="AddQuestion" method="POST" action="Control.do?Action=AddQuestion" onsubmit="return validate1();">
      <fieldset><legend>&nbsp;<%=lblQue%>&nbsp; </legend>
      <table border="0" width="83%">       
       <%-- <% if(currentFormVO.getFormId() == null){ %> --%>
       <tr>
        <td width="33%">Question :</td>
        <td width="67%" align="left">
          <select size="1" id="questionname" name="FormQuestionVO.QuestionId">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<questionList.size();i++){ 
	   		   questionVO = (QuestionVO)questionList.get(i); 
	   		  String selected = "";
	   	    %>
			 <option value="<%=questionVO.getQuestionId()%>" <%=selected%>><%=questionVO.getQuestion()%></option>
			<%}%>
			</select>
           
            <input type="submit" id="b1" value="Add Question" name="Add Question">
           
          </td>
          
        </tr> 
      <%--   <% } %>  --%>
        <tr>
        <td colspan="2" align="center">
         <% if(fq!= null && fq.length != 0){ %>
        <table border="0" cellspacing="1"  width="98%" bgcolor="<%=darkColor%>">
        <tr>
          <td width="6%">Sr No.</td>
          <td width="61%">Question</td>
           <% if(currentFormVO.getFormId() == null){ %>
          <td width="11%">Action</td>
           <% } %>
        </tr>
        <%
        for(int i=0,j=1;i<fq.length;i++,j++){  
   		  FormQuestionVO formQuestionVO = (FormQuestionVO)fq[i];
		 %>
        <tr>
          <td width="10%" bgcolor="#FFFFFF"><%=j%></td>
          <td width="61%" bgcolor="#FFFFFF"><%=formQuestionVO.getQuestion()%></td>
         <%--  <% if(currentFormVO.getFormId() == null){ %>  --%>
          <td width="11%" bgcolor="#FFFFFF"><a href="Control.do?Action=RemoveQuestion&FormQuestionVO.QuestionId=<%=formQuestionVO.getQuestionId()%>">Remove</a></td>
         <%--    <% } %>  --%>
        </tr>
		<%}%>
     </table>
     <% } %>
        
        </td>
        </tr>
</table>
</fieldset>

</form>
</td>

<tr>
      <td width="100%" align="center">
      <form name="CreateForm" method="POST" action="Control.do?Action=CreateForm">
      <input type="hidden" name="FormVO.FormId" value="<c:out value="${currentFormVO.formId}" />" />
            
      <fieldset><legend>&nbsp;<%=labelStr%>&nbsp; </legend>
      <table border="0" width="80%">       
        <tr>
          <td width="36%" align="left">Form Name :</td>
          <td width="61%" align="left"><input type="text" id = "formname" name="FormVO.Name" value="<c:out value="${currentFormVO.name}" />" size="20">        </td>
        </tr>
        <tr>
        <td width="39%"> Company Name :</td>
       <td width="31%">
           <select size="1" id="company" name="FormVO.CompId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<companyList.size();i++){ 
			   	       CompanyVO companyVO  = (CompanyVO)companyList.get(i); 
			   	        String selectStr = "";
				        if(companyVO.getCompId().equals(currentFormVO.getCompId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=companyVO.getCompId()%>" <%=selectStr%> ><%=companyVO.getName()%></option>
				     <%}%>
			</select>
       </td>
        </tr>
         <tr>
        <td width="39%"> Branch Name :</td>
       <td width="31%">
           <select size="1" id="branch" name="FormVO.BranchId">
         	<option value="">--Select--</option>
					<%for(int i=0;i<branchList.size();i++){ 
			   	       BranchVO branchVO  = (BranchVO)branchList.get(i); 
			   	        String selectStr = "";
				        if(branchVO.getBranchId().equals(currentFormVO.getBranchId())){
					       selectStr = "selected";
				        }
				    %>
				        <option value="<%=branchVO.getBranchId()%>" <%=selectStr%> ><%=branchVO.getName()%></option>
				     <%}%>
			</select>
       </td>
        </tr>       
        <tr>
          <td width="36%" align="left">Valid From Date :</td>
          <td width="61%" align="left">
          <!--<input type="text" id="validFromDate" name="FormVO.ValidFromDate" size="20" value=<%=validFromDate%>>&nbsp;<a href="javascript:validFromDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
          <input type="text" id="validFromDate" name="FormVO.ValidFromDate" size="20" onfocus="showCalendarControl(this);" value=<%=validFromDate%>>
          </td>
       
        </tr> 
         <tr>
          <td width="36%" align="left">Valid To Date :</td>
          <td width="61%" align="left">
          <!--<input type="text" id="validToDate" name="FormVO.ValidToDate" size="20" value=<%=validToDate%>>&nbsp;<a href="javascript:validToDate.popup();"><img src="images/cal.gif" width="16" height="16" border="0" alt="Click Here to Pick up the date"></a>-->
          <input type="text" id="validToDate" name="FormVO.ValidToDate" size="20" onfocus="showCalendarControl(this);" value=<%=validToDate%>>
          </td>
         
        </tr> 
        <tr>
         <td colspan="2" align="left">
          Comment : &nbsp;&nbsp;&nbsp;&nbsp;
          <select size="1" id="comment" name="FormVO.Comment">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentFormVO.getComment())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  Draw :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <select size="1" id="draw"name="FormVO.Draw">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentFormVO.getDraw())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
        
          </td>               
        </tr>
        <tr> 
        
         <td colspan="2" align="left">
          Audio: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;   
            <select size="1" id="audio" name="FormVO.Audio">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentFormVO.getAudio())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  Video :		 &nbsp;&nbsp;&nbsp;&nbsp;
          <select size="1" id="video" name="FormVO.Video">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentFormVO.getVideo())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
        
          </td>        
        </tr> 
        <tr>
         <td width="27%" align="left">Status :</td>
          <td width="71%" align="left">
          <select size="1" id="status" name="FormVO.Status">
       	   <option value="">--Select--</option>
           <%for(int i=0;i<statusList.size();i++){ 
	   		  TypeVO typeVO = (TypeVO)statusList.get(i); 
	   		  String selected = "";
	   		  if(typeVO.getId().equals(currentFormVO.getStatus())){
		   		  selected = "selected";
	   		  }
	   	    %>
			 <option value="<%=typeVO.getId()%>" <%=selected%>><%=typeVO.getName()%></option>
			<%}%>
			</select>
          </td>
        </tr>
        <tr>
        <td></td>
        </tr>
        <tr>
          <td colspan="2" align="center">
         <%--  <% if(currentFormVO.getFormId() == null){ %> --%>
          <input type="submit" value="<%=buttonStr%>" id="b2" name="B1" onClick="return validate2();">
          <input type="submit" value="Cancel" name="Cancel"  onClick="form.action='Control.do?Action=CancelCreateForm';"></td>
         <%-- <% } %> --%>
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
 
   var validFromDate= new calendar1(document.forms['CreateForm'].elements['validFromDate'],new Date());
   validFromDate.year_scroll = true;
   validFromDate.time_comp = false;
   
   var validToDate= new calendar1(document.forms['CreateForm'].elements['validToDate'],new Date());
   validToDate.year_scroll = true;
   validToDate.time_comp = false;
   
   //-->
 </script>