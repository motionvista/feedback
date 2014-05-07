package com.feedback.control;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.feedback.bl.DbConnection;
import com.feedback.bl.Service;
import com.feedback.common.EmailUtil;
import com.feedback.common.PrintUtil;
import com.feedback.vo.BranchCriteriaVO;
import com.feedback.vo.BranchVO;
import com.feedback.vo.CompanyVO;
import com.feedback.vo.CustomerVO;
import com.feedback.vo.DeviceVO;
import com.feedback.vo.EmpCriteriaVO;
import com.feedback.vo.EmpVO;
import com.feedback.vo.FeedbackQuestionAnsVO;
import com.feedback.vo.FeedbackResultVO;
import com.feedback.vo.FeedbackVO;
import com.feedback.vo.FormCriteriaVO;
import com.feedback.vo.FormQuestionVO;
import com.feedback.vo.FormVO;
import com.feedback.vo.Responce;
import com.feedback.vo.CityVO;
import com.feedback.vo.SettingVO;
import com.feedback.vo.StateVO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class WebServlet extends HttpServlet {
	public void init(ServletConfig conf) {
		try {
			super.init(conf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try {
			String action = request.getParameter("Action").trim();
			System.out.println("action----" + action);

			if (action.equals("GoToGetCompany")) {
				Service service = null;
				try {
					String dbName = request.getParameter("dbName");
					System.out.println("Db Name :" + dbName);
					service = new Service(dbName);
					CompanyVO[] companies = service
					.getCompanies(new CompanyVO());
					if (companies != null && companies.length > 0) {
						for (int i = 0; i < companies.length; i++) {
							CompanyVO companyVO = (CompanyVO) companies[i];
							System.out.println("comany id :"
									+ companyVO.getCompId());
							System.out.println("comany Name :"
									+ companyVO.getName());
							/*
							 * String json = new Gson().toJson(companyVO);
							 * response.setContentType("application/json");
							 * response.setCharacterEncoding("UTF-8");
							 * response.getWriter().write(json);
							 */
						}
						String json = new Gson().toJson(companies);
						json = "{\"Company\":" + json + "}";
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(json);
					} else {
						System.out.println("No Companies found");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					service = null;
				}
			} else if (action.equals("GoToGetBranch")) {
				Service service = null;
				try {
					String dbName = request.getParameter("dbName");
					System.out.println("Db Name :" + dbName);
					service = new Service(dbName);
					String compId = request.getParameter("compId");
					System.out.println("Copmany id:" + compId);
					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(new Integer(compId));
					BranchVO[] branches = service.getBranches(branchCriteriaVO);
					if (branches != null && branches.length > 0) {
						for (int i = 0; i < branches.length; i++) {
							BranchVO branchVO = (BranchVO) branches[i];
							System.out.println("Branch id :"
									+ branchVO.getBranchId());
							System.out.println("Branch Name :"
									+ branchVO.getName());
							// String json = new Gson().toJson(branchVO);
							// response.setContentType("application/json");
							// response.setCharacterEncoding("UTF-8");
							// response.getWriter().write(json);
						}
						String json = new Gson().toJson(branches);
						json = "{\"Branch\":" + json + "}";
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(json);
					} else {
						System.out.println("No Branche found");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			} else if (action.equals("GoToRegisterDevice")) {
				Responce res = new Responce();
				Service service = null;
				try {
					String dbName = request.getParameter("dbName");
					System.out.println("Db Name :" + dbName);
					service = new Service(dbName);
					String compId = request.getParameter("compId");
					String branchId = request.getParameter("branchId");
					String deviceId = request.getParameter("deviceId");
					System.out.println("comp id" + compId);
					System.out.println("branch id" + branchId);
					System.out.println("device id" + deviceId);
					DeviceVO deviceVO = new DeviceVO();
					deviceVO.setBranchId(new Integer(branchId));
					deviceVO.setCompId(new Integer(compId));
					deviceVO.setDeviceId(deviceId);
					int i = service.storeDevice(deviceVO);
					System.out.println("flag :" + i);
					if (i == 0) {
						res.setId(0);
						System.out.println("Success");

					} else {
						res.setId(1);
						System.out.println("Aleready exist");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					service = null;
				}
				String json = new Gson().toJson(res);
				json = "{\"flag\":[" + json + "]}";
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} else if (action.equals("GoToUnRegisterDevice")) {
				Responce res = new Responce();
				Service service = null;
				try {
					String dbName = request.getParameter("dbName");
					System.out.println("Db Name :" + dbName);
					service = new Service(dbName);
					String deviceId = request.getParameter("deviceId");
					System.out.println("device id : " + deviceId);
					DeviceVO deviceVO = new DeviceVO();
					deviceVO.setDeviceId(deviceId);
					int i = service.unregisterDevice(deviceVO);
					if (i == 0) {
						res.setId(0);
						System.out.println("Success");

					} else {
						res.setId(1);
						System.out.println("Not Success");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					service = null;
				}
				String json = new Gson().toJson(res);
				json = "{\"flag\":[" + json + "]}";
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} else if (action.equals("GoToGetFeedbackForm")) {
				Service service = null;
				try {
					String dbName = request.getParameter("dbName");
					System.out.println("Db Name :" + dbName);
					service = new Service(dbName);
					String compId = request.getParameter("compId");
					System.out.println("Copmany id:" + compId);
					String branchId = request.getParameter("branchId");
					System.out.println("Branch id:" + branchId);
					FormCriteriaVO formCriteria = new FormCriteriaVO();
					formCriteria.setCompId(new Integer(compId));
					formCriteria.setBranchId(new Integer(branchId));
					formCriteria.setStatus(new Integer(1));
					FormVO formVO = service.getForm(formCriteria);
					if (formVO != null) {
						FormQuestionVO[] questions = formVO.getQuestions();
						if (questions != null && questions.length != 0) {
							for (int i = 0; i < questions.length; i++) {
								FormQuestionVO formQuestion = (FormQuestionVO) questions[i];
								System.out.println("form id :"
										+ formQuestion.getFormId());
								System.out.println("que id :"
										+ formQuestion.getQuestionId());
								System.out.println("que :"
										+ formQuestion.getQuestion());
							}
						}

						formVO.setQuestions(questions);

						String json = new Gson().toJson(formVO);
						json = "{\"form\":[" + json + "]}";
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(json);
					} else {
						System.out.println("Form not found....");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {
					service = null;
				}

			} else if (action.equals("GoToStoreFeedback")) {

				int ansSum = 0;
				boolean flag = false;
				Service service = null;

				String dbName = request.getParameter("dbName");
				System.out.println("Db Name :" + dbName);
				service = new Service(dbName);

				Responce res = new Responce();
				String strCustomer = request.getParameter("customer");
				String strFeedback = request.getParameter("feedback");

				System.out.println("Customer  : " + strCustomer);
				System.out.println("Feedback  : " + strFeedback);

				Gson gson = new Gson();
				JsonParser parser = new JsonParser();
				JsonElement custElement = parser.parse(strCustomer)
				.getAsJsonObject();
				CustomerVO customerVO = (CustomerVO) gson.fromJson(custElement,
						CustomerVO.class);
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
				String dob = ft.format(customerVO.getDob());
				customerVO.setDob(ft.parse(dob));
				Date dateOfBirth = ft.parse(dob);
				customerVO.setAge(new Integer(new Date().getYear()
						- dateOfBirth.getYear()));
				System.out.println("id :" + customerVO.getCustId());
				System.out.println("Name :" + customerVO.getCustName());
				System.out.println("e id :" + customerVO.getEmailId());
				System.out.println("location :" + customerVO.getLocation());
				System.out.println("DOB : " + customerVO.getDob());
				System.out.println("Customer Age :" + customerVO.getAge());
				System.out.println("Comp ID :" + customerVO.getCompId());
				System.out.println("Branch ID :" + customerVO.getBranchId());

				JsonElement feedbackElement = parser.parse(strFeedback)
				.getAsJsonObject();
				FeedbackVO feedbackVO = (FeedbackVO) gson.fromJson(
						feedbackElement, FeedbackVO.class);
				// set flag depending on time.
				System.out.println("Comp ID :" + feedbackVO.getCompId());
				System.out.println("Branch ID :" + feedbackVO.getBranchId());

				FeedbackQuestionAnsVO[] fq = feedbackVO.getQuestionAns();

				for (int i = 0; i < fq.length; i++) {
					FeedbackQuestionAnsVO fqa = (FeedbackQuestionAnsVO) fq[i];
					System.out.println("que id : " + fqa.getQuestionId());
					System.out.println("que : " + fqa.getQuestion());
					System.out.println("ans : " + fqa.getAns());
					
					ansSum = ansSum + fqa.getAns().intValue();
				}
				System.out.println("Ans sum :"+ansSum);
				System.out.println("Comment  : " + feedbackVO.getComment());

				try {
					CustomerVO cust = service.storeCustomer(customerVO);
					System.out.println("Customer Id:" + cust.getCustId());
					if(ansSum < 5){
						feedbackVO.setPotentialEescalation(new Integer(2));
						flag = true;
					}else{
						feedbackVO.setPotentialEescalation(new Integer(1));
					}
					feedbackVO.setCustId(cust.getCustId());
					FeedbackVO f = service.storeCustomerFeedback(feedbackVO);
					System.out.println("Feedback Id :" + f.getFeedbackId());
					if (f != null) {
						res.setId(0);
						System.out.println("Saved");
						service.sendSms(cust.getMobileNo().toString(),"Thank you for your feedback...");
					} else {
						res.setId(1);
						System.out.println("not Saved");
					}
					if(flag){
						List empList = new ArrayList();
						EmpVO[] empVOs = null;
						EmpCriteriaVO ec1 = new EmpCriteriaVO();
						ec1.setRoleId(new Integer(1));
						empList.addAll(new ArrayList(Arrays.asList(service.getEmployees(ec1))));
						EmpCriteriaVO ec2 = new EmpCriteriaVO();
						ec2.setCompId(new Integer(2));
						ec2.setRoleId(new Integer(3));
						empList.addAll(new ArrayList(Arrays.asList(service.getEmployees(ec2))));
						EmpCriteriaVO ec3 = new EmpCriteriaVO();
						ec3.setCompId(new Integer(1));
						ec3.setBranchId(new Integer(4));
						ec3.setRoleId(new Integer(4));
						empList.addAll(new ArrayList(Arrays.asList(service.getEmployees(ec3))));
						System.out.println("list Size :"+empList.size());
					    empVOs = (EmpVO[]) empList.toArray(new EmpVO[empList.size()]);
						System.out.println("Size :"+empVOs.length);
						SettingVO settingVO = service.getSetting();
						if(empVOs != null && empVOs.length >0){
							for (int i = 0; i < empVOs.length; i++) {
								EmpVO empVO = empVOs[i];
								System.out.println("Email id :"+empVO.getEmailId());
								settingVO.setEmailTo(empVO.getEmailId());
								FeedbackResultVO feedbackResultVO = service.getFeedback(f.getFeedbackId());
								HashMap hm = new HashMap();
								hm.put("feedbackResultVO",feedbackResultVO);
								String body = PrintUtil.getText("feedbackreport.vm",hm);
								new EmailUtil().sendEmail(settingVO,"Negative Feedback", body);
							}
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Exp :" + e.getMessage());
				} finally {
					service = null;
				}
				String json = new Gson().toJson(res);
				json = "{\"flag\":[" + json + "]}";
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} else if (action.equals("GetStates")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);
				String countryId = request.getParameter("countid").trim();
				StateVO[] stateVO = service.getStates(new Integer(countryId));
				System.out.println("Lenght :" + stateVO.length);
				String json = new Gson().toJson(stateVO);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);

			} else if (action.equals("GetCities")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);
				String stateid = request.getParameter("stateid").trim();
				CityVO[] cityVO = service.getCities(new Integer(stateid));
				System.out.println("Lenght :" + cityVO.length);
				String json = new Gson().toJson(cityVO);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			} else if (action.equals("GetBranches")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);
				String companyid = request.getParameter("companyid").trim();
				BranchCriteriaVO b = new BranchCriteriaVO();
				b.setCompId(new Integer(companyid));
				BranchVO[] branchVO = service.getBranches(b);
				System.out.println("Lenght :" + branchVO.length);
				String json = new Gson().toJson(branchVO);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(json);
			}

		} catch (Exception e) {
			System.out.println("Exp 2 :" + e.getMessage());
		}

	}
}
