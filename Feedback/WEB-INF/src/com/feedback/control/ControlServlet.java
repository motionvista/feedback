package com.feedback.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Workbook;

import com.feedback.bl.DbConnection;
import com.feedback.bl.Service;
import com.feedback.common.ApplicationContext;
import com.feedback.common.EmailUtil;
import com.feedback.vo.EmpVO;
import com.feedback.common.ObjectUtil;
import com.feedback.vo.BranchCriteriaVO;
import com.feedback.vo.BranchVO;
import com.feedback.vo.CampaignVO;
import com.feedback.vo.CompanyVO;
import com.feedback.vo.CountryVO;
import com.feedback.vo.CityVO;
import com.feedback.vo.CustomerCriteriaVO;
import com.feedback.vo.CustomerVO;
import com.feedback.vo.EmpCriteriaVO;
import com.feedback.vo.FeedbackCriteriaVO;
import com.feedback.vo.FeedbackResultVO;
import com.feedback.vo.FormCriteriaVO;
import com.feedback.vo.FormQuestionVO;
import com.feedback.vo.FormVO;
import com.feedback.vo.QuestionVO;
import com.feedback.vo.SettingVO;
import com.feedback.vo.StateVO;
import com.feedback.vo.RoleVO;
import com.feedback.vo.DeviceVO;
import com.feedback.vo.CustCampaignVO;
import com.feedback.vo.CampaignCriteriaVO;
import com.feedback.vo.EmailTemplateVO;
import com.feedback.vo.SmsTemplateVO;
import com.feedback.common.PrintUtil;
import com.feedback.vo.CampaignTemplateVO;

public class ControlServlet extends HttpServlet {
	public void init(ServletConfig conf) {
		try {
			super.init(conf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildObject(HttpServletRequest request) throws Exception {
		Enumeration en = request.getParameterNames();
		HashMap iMap = new HashMap();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String val = request.getParameter(key);
			iMap.put(key, val);
		}

		HashMap oMap = ObjectUtil.toObjects(iMap);
		Iterator itr = oMap.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			request.setAttribute(key, oMap.get(key));
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

			if (action != null && !action.equals("Login")) {
				if (ApplicationContext.getApplicationUser(request) == null) {
					request.getSession().setAttribute("dbName", action);
					// action = "Logout";
				}
			}

			if (action.equals("Login")) {
				try {
					String dbName = (String) request.getSession().getAttribute(
					"dbName");
					// Service service = new Service(dbName);

					DbConnection dbConnection = new DbConnection(dbName);//
					Service service = new Service(dbConnection);//

					String uId = request.getParameter("LoginId");
					String password = request.getParameter("Password");

					EmpVO emp = service.getUserByLoginId(uId);
					if (emp != null && emp.getPassword().equals(password)) {
						ApplicationContext.setApplicationUser(request, emp);
						request.setAttribute("currentView", "welcome.jsp");
						request.getSession().setAttribute("dbConnection",
								dbConnection);//
					} else {
						request.setAttribute("LoginId", uId);
						request.setAttribute("Password", password);
						request.setAttribute("currentView", "login.jsp");
						request.setAttribute("msg",
						"Please enter correct login id and password.");
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					request.setAttribute("currentView", "login.jsp");
					request.setAttribute("msg",
					"Please enter valid url and login.");
				}
			} else if (action.equals("Logout")) {
				try {
					String dbName = (String) request.getSession().getAttribute(
					"dbName");
					action = dbName;
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					dbConnection = null;
					try {
						response
						.sendRedirect("http://localhost:8080/Feedback/Control.do?Action="
								+ dbName);
					} catch (Exception e) {
						System.out.println("Exp : " + e.getMessage());
					}
					request.getSession().invalidate();
					return;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("GoToDefault")) {
				request.setAttribute("currentView", "welcome.jsp");
			} else if (action.equals("GoToSetPermissions")) {
				try {

					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service = new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					Integer selectedRoleId = null;
					RoleVO currentRoleVO = (RoleVO) request
					.getAttribute("currentRoleVO");
					String roleId = request.getParameter("RoleId");
					if (roleId != null && !roleId.trim().equals("")) {
						selectedRoleId = new Integer(roleId);
					}
					if (selectedRoleId != null) {
						request.setAttribute("rolePermissionList", Arrays
								.asList(service
										.getRolePermissions(selectedRoleId)));
					}
					request.setAttribute("RoleId", selectedRoleId);
					request.setAttribute("currentRoleVO", currentRoleVO);
					request.setAttribute("currentView", "setpermission.jsp");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("SetPermissions")) {
				Integer selectedRoleId = null;
				try {

					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service = new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					selectedRoleId = new Integer(request.getParameter("RoleId"));
					String[] permissionsStr = request
					.getParameterValues("Permission");
					Integer[] permissions = new Integer[permissionsStr.length];

					for (int i = 0; i < permissionsStr.length; i++) {
						permissions[i] = new Integer(permissionsStr[i]);
					}
					service.storeRolePermissions(selectedRoleId, permissions);
					request.setAttribute("currentView", "setpermission.jsp");
					if (selectedRoleId != null) {
						request.setAttribute("rolePermissionList", Arrays
								.asList(service
										.getRolePermissions(selectedRoleId)));
					}
					request
					.setAttribute("msg",
					"Permissions set successfully.");
					request.setAttribute("roleList", Arrays.asList(RoleVO
							.getRoles()));
					request.setAttribute("RoleId", selectedRoleId);
				} catch (Exception ex) {
					ex.printStackTrace();
					request.setAttribute("msg",
					"Error while setting permissions.");
				}
			} else if (action.equals("GoToCountry")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service = new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("currentView", "country.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("GoToCreateCountry")) {
				request.setAttribute("currentView", "createcountry.jsp");
			} else if (action.equals("SearchCountry")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service = new Service(dbName);
					buildObject(request);
					CountryVO currentCountryVO = (CountryVO) request
					.getAttribute("currentCountryVO");
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(currentCountryVO)));
					request.setAttribute("currentView", "country.jsp");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CreateCountry")) {
				try {

					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service = new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					String msg = null;
					boolean valid = true;
					buildObject(request);
					CountryVO currentCountryVO = (CountryVO) request
					.getAttribute("currentCountryVO");
					if (currentCountryVO.getName().trim().equals("")) {
						msg = "Please enter Country";
						valid = false;
					}
					if (valid) {
						try {
							service.storeCountry(currentCountryVO);
							msg = "Country saved successfully.";
							request.removeAttribute("currentCountryVO");
							request.setAttribute("msg", msg);
							request.setAttribute("countryList", Arrays
									.asList(service
											.getCountries(new CountryVO())));
							request.setAttribute("currentView", "country.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("msg", msg);
							request.setAttribute("currentView",
							"createcountry.jsp");
						}
					} else {
						request.setAttribute("msg", msg);
						request
						.setAttribute("currentView",
						"createcountry.jsp");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					request.setAttribute("msg", ex.getMessage());
					request.setAttribute("currentView", "createcountry.jsp");
				}
			} else if (action.equals("CancelCreateCountry")) {
				try {
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					request.removeAttribute("currentCountryVO");
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("currentView", "country.jsp");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("EditCountry")) {
				try {
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer countryId = new Integer(request
							.getParameter("CountryVO.CountryId"));
					CountryVO currentCountryVO = service.getCountry(countryId);
					request.setAttribute("currentCountryVO", currentCountryVO);
					request.setAttribute("currentView", "createcountry.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToState")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("currentView", "state.jsp");
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("SearchState")) {
				try {

					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					StateVO currentStateVO = (StateVO) request
					.getAttribute("currentStateVO");
					request.setAttribute("currentView", "state.jsp");
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(currentStateVO)));
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToCreateState")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("currentView", "createstate.jsp");
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CreateState")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					String msg = null;
					boolean valid = true;
					buildObject(request);
					StateVO currentStateVO = (StateVO) request
					.getAttribute("currentStateVO");
					if (currentStateVO.getName().trim().equals("")) {
						msg = "Please enter State";
						valid = false;
					}
					if (valid) {
						try {
							service.storeState(currentStateVO);
							msg = "State saved successfully.";
							request.removeAttribute("currentStateVO");
							request.setAttribute("msg", msg);
							request.setAttribute("stateList", Arrays
									.asList(service.getStates(new StateVO())));
							request.setAttribute("countryList", Arrays
									.asList(service
											.getCountries(new CountryVO())));
							request.setAttribute("currentView", "state.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("msg", msg);
							request.setAttribute("countryList", Arrays
									.asList(service
											.getCountries(new CountryVO())));
							request.setAttribute("currentView",
							"createstate.jsp");
						}
					} else {
						request.setAttribute("msg", msg);
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("currentView", "createstate.jsp");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CancelCreateState")) {
				try {
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					request.removeAttribute("currentStateVO");
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("currentView", "state.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("EditState")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer stateId = new Integer(request
							.getParameter("StateVO.StateId"));
					StateVO currentStateVO = service.getState(stateId);
					request.setAttribute("currentStateVO", currentStateVO);
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("currentView", "createstate.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToCity")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("currentView", "city.jsp");
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(new CityVO())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("SearchCity")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					CityVO currentCityVO = (CityVO) request
					.getAttribute("currentCityVO");
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(currentCityVO.getCountryId())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(currentCityVO)));
					request.setAttribute("currentView", "city.jsp");

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("GoToCreateCity")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("currentView", "createcity.jsp");
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CreateCity")) {
				try {
					String msg = null;
					boolean valid = true;
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					CityVO currentCityVO = (CityVO) request
					.getAttribute("currentCityVO");
					if (currentCityVO.getName().trim().equals("")) {
						msg = "Please enter City";
						valid = false;
					}
					if (valid) {
						try {
							service.storeCity(currentCityVO);
							request.setAttribute("msg",
							"City saved successfully.");
							request.removeAttribute("currentCityVO");
							request.setAttribute("msg", msg);
							request.setAttribute("countryList", Arrays
									.asList(service
											.getCountries(new CountryVO())));
							request.setAttribute("stateList", Arrays
									.asList(service.getStates(new StateVO())));
							request.setAttribute("cityList", Arrays
									.asList(service.getCities(new CityVO())));
							request.setAttribute("currentView", "city.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("msg", msg);
							request.setAttribute("currentView",
							"createcity.jsp");
							request.setAttribute("countryList", Arrays
									.asList(service
											.getCountries(new CountryVO())));
							request.setAttribute("stateList", Arrays
									.asList(service.getStates(new StateVO())));
						}
					} else {
						request.setAttribute("msg", msg);
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("currentView", "createcity.jsp");
						request.setAttribute("stateList", Arrays.asList(service
								.getStates(new StateVO())));
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CancelCreateCity")) {
				try {
					buildObject(request);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.removeAttribute("currentCityVO");
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(new CityVO())));
					request.setAttribute("currentView", "city.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("EditCity")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer cityId = new Integer(request
							.getParameter("CityVO.CityId"));
					CityVO currentCityVO = service.getCity(cityId);
					request.setAttribute("currentCityVO", currentCityVO);
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(currentCityVO.getCountryId())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(new CityVO())));
					request.setAttribute("currentView", "createcity.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToCompany")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "company.jsp");
			} else if (action.equals("SearchCompany")) {
				buildObject(request);
				CompanyVO currentCompanyVO = (CompanyVO) request
				.getAttribute("currentCompanyVO");
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(currentCompanyVO)));
				request.setAttribute("currentView", "company.jsp");
			} else if (action.equals("GoToCreateCompany")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("stateList", Arrays.asList(service
						.getStates(new StateVO())));
				request.setAttribute("countryList", Arrays.asList(service
						.getCountries(new CountryVO())));
				request.setAttribute("cityList", Arrays.asList(service
						.getCities(new CityVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "createcompany.jsp");
			} else if (action.equals("CreateCompany")) {

				String msg = "";
				boolean valid = true;
				buildObject(request);
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				CompanyVO currentCompanyVO = (CompanyVO) request
				.getAttribute("currentCompanyVO");
				if (currentCompanyVO.getName().trim().equals("")) {
					msg = "Please enter company name.";
					valid = false;
				} else if (currentCompanyVO.getPhoneNo1().trim().equals("")) {
					msg = "Please enter Phone Number";
					valid = false;
				} else if (currentCompanyVO.getEmailId().trim().equals("")) {
					msg = "Please enter email.";
					valid = false;
				}
				if (valid) {
					try {
						service.storeCompany(currentCompanyVO);
						msg = "Company Saved  successfully....";
						request.removeAttribute("currentCompanyVO");
						request.setAttribute("msg", msg);
						request.setAttribute("stateList", Arrays.asList(service
								.getStates(new StateVO())));
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("cityList", Arrays.asList(service
								.getCities(new CityVO())));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("currentView", "company.jsp");
					} catch (Exception ex) {
						msg = ex.getMessage();
						request.setAttribute("msg", msg);
						request.setAttribute("stateList", Arrays.asList(service
								.getStates(new StateVO())));
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("cityList", Arrays.asList(service
								.getCities(new CityVO())));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request
						.setAttribute("currentView",
						"createcompany.jsp");
					}
				} else {
					request.setAttribute("msg", msg);
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(new CityVO())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("currentView", "createcompany.jsp");
				}

			} else if (action.equals("CancelCreateCompany")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				buildObject(request);
				request.removeAttribute("currentCompanyVO");
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "company.jsp");
			} else if (action.equals("EditCompany")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer compId = new Integer(request
							.getParameter("CompanyVO.CompId"));
					CompanyVO currentCompanyVO = service.getCompany(compId);
					request.setAttribute("currentCompanyVO", currentCompanyVO);
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(currentCompanyVO.getCountryId())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(currentCompanyVO.getStateId())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("currentView", "createcompany.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("GoToBranch")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "branch.jsp");

			} else if (action.equals("GoToCreateBranch")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("stateList", Arrays.asList(service
						.getStates(new StateVO())));
				request.setAttribute("countryList", Arrays.asList(service
						.getCountries(new CountryVO())));
				request.setAttribute("cityList", Arrays.asList(service
						.getCities(new CityVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "createbranch.jsp");
			} else if (action.equals("CreateBranch")) {
				String msg = "";
				boolean valid = true;
				buildObject(request);
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				BranchVO currentBranchVO = (BranchVO) request
				.getAttribute("currentBranchVO");
				if (currentBranchVO.getName().trim().equals("")) {
					msg = "Please enter company name.";
					valid = false;
				} else if (currentBranchVO.getPhoneNo1().trim().equals("")) {
					msg = "Please enter Phone Number";
					valid = false;
				} else if (currentBranchVO.getEmailId().trim().equals("")) {
					msg = "Please enter email.";
					valid = false;
				}
				if (valid) {
					try {
						service.storeBranch(currentBranchVO);
						msg = "Branch Saved  successfully....";
						request.removeAttribute("currentBranchVO");
						request.setAttribute("msg", msg);
						request.setAttribute("stateList", Arrays.asList(service
								.getStates(new StateVO())));
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("cityList", Arrays.asList(service
								.getCities(new CityVO())));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request.setAttribute("currentView", "branch.jsp");
					} catch (Exception ex) {
						msg = ex.getMessage();
						request.setAttribute("msg", msg);
						request.setAttribute("stateList", Arrays.asList(service
								.getStates(new StateVO())));
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("cityList", Arrays.asList(service
								.getCities(new CityVO())));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("currentView", "createbranch.jsp");
					}
				} else {
					request.setAttribute("msg", msg);
					request.setAttribute("stateList", Arrays.asList(service
							.getStates(new StateVO())));
					request.setAttribute("countryList", Arrays.asList(service
							.getCountries(new CountryVO())));
					request.setAttribute("cityList", Arrays.asList(service
							.getCities(new CityVO())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("currentView", "createbranch.jsp");
				}

			} else if (action.equals("CancelCreateBranch")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				buildObject(request);
				request.removeAttribute("currentBranchVO");
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "branch.jsp");
			} else if (action.equals("EditBranch")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				Integer branchId = new Integer(request
						.getParameter("BranchVO.BranchId"));
				BranchVO currentBranchVO = new BranchVO();
				currentBranchVO = service.getBranch(branchId);
				request.setAttribute("currentBranchVO", currentBranchVO);
				request.setAttribute("countryList", Arrays.asList(service
						.getCountries(new CountryVO())));
				request.setAttribute("stateList", Arrays.asList(service
						.getStates(currentBranchVO.getCountryId())));
				request.setAttribute("cityList", Arrays.asList(service
						.getCities(currentBranchVO.getStateId())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("currentView", "createbranch.jsp");

			} else if (action.equals("SearchBranch")) {
				try {
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					BranchCriteriaVO currentBranchCriteriaVO = (BranchCriteriaVO) request
					.getAttribute("currentBranchCriteriaVO");
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(currentBranchCriteriaVO)));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("currentView", "branch.jsp");

				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("GoToEmp")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("empList", Arrays.asList(service
						.getEmployees(new EmpCriteriaVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("roleList", Arrays.asList(RoleVO
						.getRoles()));
				request.setAttribute("currentView", "emp.jsp");

			} else if (action.equals("GoToCreateEmp")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("stateList", Arrays.asList(service
						.getStates(new StateVO())));
				request.setAttribute("countryList", Arrays.asList(service
						.getCountries(new CountryVO())));
				request.setAttribute("cityList", Arrays.asList(service
						.getCities(new CityVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("roleList", Arrays.asList(RoleVO
						.getRoles()));
				request.setAttribute("currentView", "createemp.jsp");

			} else if (action.equals("CreateEmp")) {
				String msg = "";
				boolean valid = true;
				buildObject(request);
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				EmpVO currentEmpVO = (EmpVO) request
				.getAttribute("currentEmpVO");
				if (currentEmpVO.getFirstName().trim().equals("")) {
					msg = "Please enter first name.";
					valid = false;
				} else if (currentEmpVO.getLastName().trim().equals("")) {
					msg = "Please enter last name.";
					valid = false;
				} else if (currentEmpVO.getEmailId().trim().equals("")) {
					msg = "Please enter email.";
					valid = false;
				} else if (currentEmpVO.getMobileNo().equals("")) {
					msg = "Please enter mobile no.";
					valid = false;
				}
				if (valid) {
					try {
						service.storeEmp(currentEmpVO);
						msg = "Employee Save Successfully....";
						request.removeAttribute("currentEmpVO");
						request.setAttribute("msg", msg);
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request.setAttribute("roleList", Arrays.asList(RoleVO
								.getRoles()));
						request.setAttribute("empList", Arrays.asList(service
								.getEmployees(new EmpCriteriaVO())));
						request.setAttribute("currentView", "emp.jsp");
					} catch (Exception ex) {
						msg = ex.getMessage();
						request.setAttribute("msg", msg);
						request.setAttribute("stateList", Arrays.asList(service
								.getStates(new StateVO())));
						request.setAttribute("countryList", Arrays
								.asList(service.getCountries(new CountryVO())));
						request.setAttribute("cityList", Arrays.asList(service
								.getCities(new CityVO())));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request.setAttribute("roleList", Arrays.asList(RoleVO
								.getRoles()));
						request.setAttribute("currentView", "createemp.jsp");
					}
				} else {
					request.removeAttribute("currentEmpVO");
					request.setAttribute("msg", msg);
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("roleList", Arrays.asList(RoleVO
							.getRoles()));
					request.setAttribute("empList", Arrays.asList(service
							.getEmployees(new EmpCriteriaVO())));
					request.setAttribute("currentView", "createemp.jsp");
				}
			} else if (action.equals("CancelCreateEmp")) {
				buildObject(request);
				request.removeAttribute("currentEmpVO");
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("empList", Arrays.asList(service
						.getEmployees(new EmpCriteriaVO())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("roleList", Arrays.asList(RoleVO
						.getRoles()));
				request.setAttribute("currentView", "emp.jsp");
			} else if (action.equals("EditEmp")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				Integer empId = new Integer(request.getParameter("EmpVO.EmpId"));
				EmpVO currentEmpVO = new EmpVO();
				currentEmpVO = service.getEmployee(empId);

				request.setAttribute("currentEmpVO", currentEmpVO);
				request.setAttribute("countryList", Arrays.asList(service
						.getCountries(new CountryVO())));
				request.setAttribute("stateList", Arrays.asList(service
						.getStates(currentEmpVO.getCountryId())));
				request.setAttribute("cityList", Arrays.asList(service
						.getCities(currentEmpVO.getStateId())));
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
				branchCriteriaVO.setCompId(currentEmpVO.getCompId());
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(branchCriteriaVO)));
				request.setAttribute("roleList", Arrays.asList(RoleVO
						.getRoles()));
				request.setAttribute("currentView", "createemp.jsp");
			} else if (action.equals("SearchEmp")) {
				try {
					buildObject(request);
					EmpCriteriaVO currentEmpCriteriaVO = (EmpCriteriaVO) request
					.getAttribute("currentEmpCriteriaVO");
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("empList", Arrays.asList(service
							.getEmployees(currentEmpCriteriaVO)));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("roleList", Arrays.asList(RoleVO
							.getRoles()));
					request.setAttribute("currentView", "emp.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (action.equals("GoToChangePassword")) {

				request.setAttribute("currentView", "changepassword.jsp");
			} else if (action.equals("ChangePassword")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				EmpVO currentEmp = ApplicationContext
				.getApplicationUser(request);
				String cp = request.getParameter("CurrentPassword");
				String newp = request.getParameter("NewPassword");
				String confirmP = request.getParameter("ConfirmPassword");

				boolean valid = true;
				Integer newPasswordNo = null;
				try {
					newPasswordNo = new Integer(newp);
				} catch (Exception ex) {
				}
				String msg = "";
				if (!currentEmp.getPassword().equals(cp)) {
					valid = false;
					msg = "Please enter correct current password.";
				} else if (!newp.equals(confirmP)) {
					valid = false;
					msg = "Please enter correct new password and confirm password.";
				}
				if (valid) {
					try {
						currentEmp.setPassword(newp);
						service.storeEmp(currentEmp);
						msg = "Password changed successfully.";
					} catch (Exception ex) {
						msg = "Error:" + ex.getMessage();
					}
				}
				request.setAttribute("currentView", "changepassword.jsp");
				request.setAttribute("msg", msg);

			} else if (action.equals("GoToQuestion")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("currentView", "question.jsp");
					request.setAttribute("questionList", Arrays.asList(service
							.getQuestions(new QuestionVO())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("GoToCreateQuestion")) {
				request.setAttribute("currentView", "createquestion.jsp");
			} else if (action.equals("SearchQuestion")) {
				buildObject(request);
				QuestionVO currentQuestionVO = (QuestionVO) request
				.getAttribute("currentQuestionVO");
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				request.setAttribute("currentView", "question.jsp");
				request.setAttribute("questionList", Arrays.asList(service
						.getQuestions(currentQuestionVO)));
			} else if (action.equals("CreateQuestion")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					String msg = null;
					boolean valid = true;
					buildObject(request);
					QuestionVO currentQuestionVO = (QuestionVO) request
					.getAttribute("currentQuestionVO");
					if (currentQuestionVO.getQuestion().trim().equals("")) {
						msg = "Please enter Question";
						valid = false;
					}
					if (currentQuestionVO.getType() == null) {
						msg = "Please Select Question Type";
						valid = false;
					}
					if (valid) {
						try {
							service.storeQuestion(currentQuestionVO);
							request.setAttribute("msg",
							"Question saved successfully.");
							request.removeAttribute("currentQuestionVO");
							request.setAttribute("msg", msg);
							request.setAttribute("questionList", Arrays
									.asList(service
											.getQuestions(new QuestionVO())));
							request.setAttribute("currentView", "question.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("msg", msg);
							request.setAttribute("currentView",
							"createquestion.jsp");
						}
					} else {
						request.setAttribute("msg", msg);
						request.setAttribute("currentView",
						"createquestion.jsp");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CancelCreateQuestion")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					buildObject(request);
					request.removeAttribute("currentQuestionVO");
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("currentView", "question.jsp");
					request.setAttribute("questionList", Arrays.asList(service
							.getQuestions(new QuestionVO())));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("EditQuestion")) {
				try {

					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer queId = new Integer(request
							.getParameter("QuestionVO.QuestionId"));
					QuestionVO currentQuestionVO = service.getQuestion(queId);
					request
					.setAttribute("currentQuestionVO",
							currentQuestionVO);
					request.setAttribute("questionList", Arrays.asList(service
							.getQuestions(new QuestionVO())));
					request.setAttribute("currentView", "createquestion.jsp");

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToForm")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("formList", Arrays.asList(service
							.getForms(new FormCriteriaVO())));
					request.setAttribute("currentView", "form.jsp");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formId");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("SearchForm")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					FormCriteriaVO currentFormCriteriaVO = (FormCriteriaVO) request
					.getAttribute("currentFormCriteriaVO");
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(currentFormCriteriaVO
							.getCompId());
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(branchCriteriaVO)));
					request.setAttribute("formList", Arrays.asList(service
							.getForms(currentFormCriteriaVO)));
					request.setAttribute("currentView", "form.jsp");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formId");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("GoToCreateForm")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("questionList", Arrays.asList(service
							.getQuestions(new QuestionVO())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("formList", Arrays.asList(service
							.getForms(new FormCriteriaVO())));
					request.setAttribute("currentView", "createform.jsp");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formId");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("AddQuestion")) {
				buildObject(request);
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				Integer formId = (Integer) request.getSession().getAttribute(
				"formId");
				FormVO currentFormVO = new FormVO();
				if (formId != null) {
					FormVO f = service.getForm(formId);
					currentFormVO.setFormId(f.getFormId());
					currentFormVO.setName(f.getName());
					currentFormVO.setCompId(f.getCompId());
					currentFormVO.setBranchId(f.getBranchId());
					currentFormVO.setValidFromDate(f.getValidFromDate());
					currentFormVO.setValidToDate(f.getValidToDate());
					currentFormVO.setComment(f.getComment());
					currentFormVO.setAudio(f.getAudio());
					currentFormVO.setDraw(f.getDraw());
					currentFormVO.setVideo(f.getVideo());
					currentFormVO.setStatus(f.getStatus());
				}
				// FormVO currentFormVO = (FormVO)
				// request.getAttribute("currentFormVO");
				FormQuestionVO currentFormQuestionVO = (FormQuestionVO) request
				.getAttribute("currentFormQuestionVO");
				QuestionVO quesstionVO = service
				.getQuestion(currentFormQuestionVO.getQuestionId());
				currentFormQuestionVO.setQuestion(quesstionVO.getQuestion());

				FormVO formVO = (FormVO) request.getSession().getAttribute(
						"formVO");

				if (formVO == null) {
					formVO = new FormVO();
					List q = new ArrayList();
					q.add(currentFormQuestionVO);
					formVO.setQuestions((FormQuestionVO[]) q
							.toArray(new FormQuestionVO[q.size()]));
				} else {

					List fQuestions = Arrays.asList(formVO.getQuestions());
					List qList = new ArrayList(fQuestions);
					qList.add(currentFormQuestionVO);
					formVO.setQuestions((FormQuestionVO[]) qList
							.toArray(new FormQuestionVO[qList.size()]));
				}

				request.getSession().setAttribute("formVO", formVO);
				request.setAttribute("currentFormVO", currentFormVO);
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("questionList", Arrays.asList(service
						.getQuestions(new QuestionVO())));
				request.setAttribute("formList", Arrays.asList(service
						.getForms(new FormCriteriaVO())));
				request.setAttribute("currentView", "createform.jsp");

			} else if (action.equals("RemoveQuestion")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				Integer formId = (Integer) request.getSession().getAttribute(
				"formId");
				FormVO currentFormVO = null;
				if (formId != null) {
					FormVO f = service.getForm(formId);
					currentFormVO = new FormVO();
					currentFormVO.setFormId(f.getFormId());
					currentFormVO.setName(f.getName());
					currentFormVO.setCompId(f.getCompId());
					currentFormVO.setBranchId(f.getBranchId());
					currentFormVO.setValidFromDate(f.getValidFromDate());
					currentFormVO.setValidToDate(f.getValidToDate());
					currentFormVO.setComment(f.getComment());
					currentFormVO.setAudio(f.getAudio());
					currentFormVO.setDraw(f.getDraw());
					currentFormVO.setVideo(f.getVideo());
					currentFormVO.setStatus(f.getStatus());
				}

				Integer questionId = new Integer(request
						.getParameter("FormQuestionVO.QuestionId"));
				FormVO formVO = (FormVO) request.getSession().getAttribute(
				"formVO");

				List fQuestions = Arrays.asList(formVO.getQuestions());
				List qList = new ArrayList();
				Iterator itr = fQuestions.iterator();
				while (itr.hasNext()) {
					FormQuestionVO fq = (FormQuestionVO) itr.next();
					if (!fq.getQuestionId().equals(questionId)) {
						qList.add(fq);
					}
				}
				formVO.setQuestions((FormQuestionVO[]) qList
						.toArray(new FormQuestionVO[qList.size()]));
				request.getSession().setAttribute("formVO", formVO);
				request.setAttribute("currentFormVO", currentFormVO);
				request.setAttribute("companyList", Arrays.asList(service
						.getCompanies(new CompanyVO())));
				request.setAttribute("branchList", Arrays.asList(service
						.getBranches(new BranchCriteriaVO())));
				request.setAttribute("questionList", Arrays.asList(service
						.getQuestions(new QuestionVO())));
				request.setAttribute("formList", Arrays.asList(service
						.getForms(new FormCriteriaVO())));
				request.setAttribute("currentView", "createform.jsp");

			}

			else if (action.equals("CreateForm")) {
				try {
					String msg = null;
					boolean valid = true;
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					FormVO formVO = (FormVO) request.getSession().getAttribute(
					"formVO");
					FormVO currentFormVO = (FormVO) request
					.getAttribute("currentFormVO");
					if (currentFormVO.getName().trim().equals("")) {
						msg = "Please enter Form Name";
						valid = false;
					}
					if (formVO == null) {
						msg = "Please add  questions";
						valid = false;
					}
					if (formVO != null) {
						currentFormVO.setQuestions(formVO.getQuestions());
					}
					if (valid) {
						try {
							service.storeForm(currentFormVO);
							msg = "form saved successufully...";
							request.removeAttribute("currentFormVO");
							request.getSession().removeAttribute("formVO");
							request.getSession().removeAttribute("formId");
							request.setAttribute("msg", msg);
							request.setAttribute("companyList", Arrays
									.asList(service
											.getCompanies(new CompanyVO())));
							request
							.setAttribute(
									"branchList",
									Arrays
									.asList(service
											.getBranches(new BranchCriteriaVO())));
							request.setAttribute("formList", Arrays
									.asList(service
											.getForms(new FormCriteriaVO())));
							request.setAttribute("currentView", "form.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("questionList", Arrays
									.asList(service
											.getQuestions(new QuestionVO())));
							request.setAttribute("companyList", Arrays
									.asList(service
											.getCompanies(new CompanyVO())));
							request
							.setAttribute(
									"branchList",
									Arrays
									.asList(service
											.getBranches(new BranchCriteriaVO())));
							request.setAttribute("formList", Arrays
									.asList(service
											.getForms(new FormCriteriaVO())));
							request.setAttribute("currentView",
							"createform.jsp");
						}
					} else {
						request
						.setAttribute(
								"questionList",
								Arrays
								.asList(service
										.getQuestions(new QuestionVO())));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request.setAttribute("formList", Arrays.asList(service
								.getForms(new FormCriteriaVO())));
						request.setAttribute("currentView", "createform.jsp");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CancelCreateForm")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					request.removeAttribute("currentFormVO");
					request.getSession().removeAttribute("formVO");
					request.getSession().removeAttribute("formId");
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("formList", Arrays.asList(service
							.getForms(new FormCriteriaVO())));
					request.setAttribute("currentView", "form.jsp");

				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("EditForm")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer formId = new Integer(request
							.getParameter("FormVO.FormId"));
					FormVO currentFormVO = service.getForm(formId);
					FormVO formVO = new FormVO();
					formVO.setQuestions(currentFormVO.getQuestions());
					request.getSession().setAttribute("formVO", formVO);
					request.getSession().setAttribute("formId",
							currentFormVO.getFormId());
					request.setAttribute("currentFormVO", currentFormVO);
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(currentFormVO.getCompId());
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(branchCriteriaVO)));
					request.setAttribute("questionList", Arrays.asList(service
							.getQuestions(new QuestionVO())));
					request.setAttribute("formList", Arrays.asList(service
							.getForms(new FormCriteriaVO())));
					request.setAttribute("currentView", "createform.jsp");

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToCustomer")) {
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				EmpVO empVO = ApplicationContext.getApplicationUser(request);
				if (empVO.getRoleId().toString().equals("1")
						|| empVO.getRoleId().toString().equals("2")) {
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("customerList", Arrays.asList(service
							.getCustomers(new CustomerCriteriaVO())));
				} else if (empVO.getRoleId().toString().equals("3")) {

					CompanyVO companyVO = new CompanyVO();
					companyVO.setCompId(empVO.getCompId());
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(companyVO)));

					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(empVO.getCompId());
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(branchCriteriaVO)));

					CustomerCriteriaVO customerCriteriaVO = new CustomerCriteriaVO();
					customerCriteriaVO.setCompId(empVO.getCompId());
					request.setAttribute("customerList", Arrays.asList(service
							.getCustomers(customerCriteriaVO)));

				} else if (empVO.getRoleId().toString().equals("4")
						|| empVO.getRoleId().toString().equals("5")) {
					CompanyVO companyVO = new CompanyVO();
					companyVO.setCompId(empVO.getCompId());
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(companyVO)));

					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(empVO.getCompId());
					branchCriteriaVO.setBranchId(empVO.getBranchId());
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(branchCriteriaVO)));

					CustomerCriteriaVO customerCriteriaVO = new CustomerCriteriaVO();
					customerCriteriaVO.setCompId(empVO.getCompId());
					customerCriteriaVO.setBranchId(empVO.getBranchId());
					request.setAttribute("customerList", Arrays.asList(service
							.getCustomers(customerCriteriaVO)));

				}

				request.setAttribute("currentView", "customer.jsp");
			} else if (action.equals("SearchCustomer")) {
				try {
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					CustomerCriteriaVO currentCustomerCriteriaVO = (CustomerCriteriaVO) request
					.getAttribute("currentCustomerCriteriaVO");

					EmpVO empVO = ApplicationContext
					.getApplicationUser(request);

					if (empVO.getRoleId().toString().equals("1")
							|| empVO.getRoleId().toString().equals("2")) {

						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(currentCustomerCriteriaVO
								.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

					} else if (empVO.getRoleId().toString().equals("3")) {

						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						currentCustomerCriteriaVO.setCompId(empVO.getCompId());

					} else if (empVO.getRoleId().toString().equals("4")
							|| empVO.getRoleId().toString().equals("5")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						branchCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						currentCustomerCriteriaVO.setCompId(empVO.getCompId());
						currentCustomerCriteriaVO.setBranchId(empVO
								.getBranchId());

					}

					request.setAttribute("customerList", Arrays.asList(service
							.getCustomers(currentCustomerCriteriaVO)));
					request.setAttribute("currentView", "customer.jsp");

				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("ViewCustomer")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer custId = new Integer(request
							.getParameter("CustomerVO.CustId"));
					CustomerVO currentCustomerVO = service.getCustomer(custId);
					request
					.setAttribute("currentCustomerVO",
							currentCustomerVO);
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("currentView", "customerview.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (action.equals("GoToCampaign")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					request.setAttribute("campaignList", Arrays.asList(service
							.getCampaigns(new CampaignCriteriaVO())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("currentView", "campaign.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("SearchCampaign")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					CampaignCriteriaVO currentCampaignCriteriaVO = (CampaignCriteriaVO) request
					.getAttribute("currentCampaignCriteriaVO");
					request.setAttribute("campaignList", Arrays.asList(service
							.getCampaigns(currentCampaignCriteriaVO)));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(currentCampaignCriteriaVO
							.getCompId());
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(branchCriteriaVO)));
					request.setAttribute("currentView", "campaign.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("importCampaignToExcell")) {
				try {
					Date d = new Date();
					String filename = "campaign-" + d.toString() + ".xls";

					response.setContentType("application/vnd.ms-excel");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + filename + "");

					// FileOutputStream fileOut = new
					// FileOutputStream(filename);
					HSSFWorkbook workbook = new HSSFWorkbook();
					HSSFSheet worksheet = workbook
					.createSheet("Campaign Worksheet");

					// worksheet.setDisplayGridlines(false);
					// worksheet.setPrintGridlines(false);
					worksheet.setFitToPage(true);
					worksheet.setHorizontallyCenter(true);
					PrintSetup printSetup = worksheet.getPrintSetup();
					printSetup.setLandscape(true);

					worksheet.setAutobreaks(true);
					printSetup.setFitHeight((short) 1);
					printSetup.setFitWidth((short) 1);

					worksheet.createFreezePane(0, 1);

					worksheet.protectSheet("1234");

					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					CampaignVO[] campaignVOs = service
					.getCampaigns(new CampaignCriteriaVO());

					if (campaignVOs != null && campaignVOs.length > 0) {
						HSSFCellStyle cellStyle = workbook.createCellStyle();
						cellStyle = workbook.createCellStyle();
						cellStyle
						.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);// set
						// the
						// foreground
						// color.
						cellStyle
						.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);// set
						// the
						// fill
						// pattern.
						cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

						// cellStyle.setWrapText(true); //wrap the text if large
						// then on new line.

						/*
						 * cellStyle.setBorderRight(CellStyle.BORDER_THICK);//set
						 * the top,bottom,left & right border and its color
						 * cellStyle.setBorderLeft(CellStyle.BORDER_THICK);
						 */

						HSSFRow hrow = worksheet.createRow(0);
						// hrow.setHeightInPoints(20.0f); set the row heights
						// not its text betn it.
						HSSFCell hcell1 = hrow.createCell(0);
						hcell1.setCellValue("Campaign Id");
						hcell1.setCellStyle(cellStyle);
						HSSFCell hcell2 = hrow.createCell(1);
						hcell2.setCellValue("Campaign Name");
						hcell2.setCellStyle(cellStyle);
						HSSFCell hcell3 = hrow.createCell(2);
						hcell3.setCellValue("Description");
						hcell3.setCellStyle(cellStyle);
						HSSFCell hcell4 = hrow.createCell(3);
						hcell4.setCellValue("Company Name");
						hcell4.setCellStyle(cellStyle);
						HSSFCell hcell5 = hrow.createCell(4);
						hcell5.setCellValue("Branch Name");
						hcell5.setCellStyle(cellStyle);
						for (int i = 0, rw = 1; i < campaignVOs.length; i++, rw++) {
							HSSFRow row = worksheet.createRow(rw);
							HSSFCellStyle cellStyle1 = workbook
							.createCellStyle();
							cellStyle1.setAlignment(CellStyle.ALIGN_CENTER);
							CampaignVO campaignVO = campaignVOs[i];
							HSSFCell cell1 = row.createCell(0);
							cell1.setCellStyle(cellStyle1);
							cell1.setCellValue(campaignVO.getCampId()
									.toString());
							HSSFCell cell2 = row.createCell(1);
							cell2.setCellValue(campaignVO.getCampName());
							cell2.setCellStyle(cellStyle1);
							HSSFCell cell3 = row.createCell(2);
							cell3.setCellValue(campaignVO.getDescription());
							cell3.setCellStyle(cellStyle1);
							HSSFCell cell4 = row.createCell(3);
							cell4.setCellValue(campaignVO.getCompName());
							cell4.setCellStyle(cellStyle1);
							HSSFCell cell5 = row.createCell(4);
							cell5.setCellValue(campaignVO.getBranchName());
							cell5.setCellStyle(cellStyle1);
						}

					}

					/*
					 * worksheet.setColumnWidth(0, 256*20);// set the width of
					 * column. worksheet.setColumnWidth(1, 256*20);
					 * worksheet.setColumnWidth(2, 256*20);
					 * worksheet.setColumnWidth(3, 256*20);
					 * worksheet.setColumnWidth(4, 256*20);
					 */

					worksheet.autoSizeColumn(0);// auto size the column.
					worksheet.autoSizeColumn(1);
					worksheet.autoSizeColumn(2);
					worksheet.autoSizeColumn(3);
					worksheet.autoSizeColumn(4);

					/*
					 * workbook.write(fileOut); fileOut.flush();
					 * fileOut.close();
					 */

					workbook.write(response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();

					// request.setAttribute("currentView", "analytics.jsp");

					return;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else if (action.equals("GoToCreateCampaign")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("emailTemplateList", Arrays
							.asList(service
									.getEmailTemplates(new EmailTemplateVO())));
					request.setAttribute("smsTemplateList", Arrays
							.asList(service
									.getSmsTemplates(new SmsTemplateVO())));
					request.setAttribute("currentView", "createcampaign.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("CreateCampaign")) {
				String msg = "";
				boolean valid = true;
				buildObject(request);
				// String dbName = (String)
				// request.getSession().getAttribute("dbName");
				// Service service= new Service(dbName);
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				CampaignVO currentCampaignVO = (CampaignVO) request
				.getAttribute("currentCampaignVO");
				if (currentCampaignVO.getCampName().trim().equals("")) {
					msg = "Please enter Campaign name.";
					valid = false;
				}/*
				 * else if(currentCampaignVO.getStartDate().equals("")){ msg =
				 * "Please enter Start Date"; valid = false; }else
				 * if(currentCampaignVO.getEndDate().equals("")){ msg =
				 * "Please enter end Date."; valid = false; }
				 */
				if (valid) {
					try {
						service.storeCampaign(currentCampaignVO);
						msg = "Campaign Saved  successfully....";
						request.removeAttribute("currentCampaignVO");
						request.setAttribute("msg", msg);
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request
						.setAttribute(
								"campaignList",
								Arrays
								.asList(service
										.getCampaigns(new CampaignCriteriaVO())));
						request.setAttribute("currentView", "campaign.jsp");
					} catch (Exception ex) {
						msg = ex.getMessage();
						request.setAttribute("msg", msg);
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request
						.setAttribute(
								"emailTemplateList",
								Arrays
								.asList(service
										.getEmailTemplates(new EmailTemplateVO())));
						request.setAttribute("smsTemplateList", Arrays
								.asList(service
										.getSmsTemplates(new SmsTemplateVO())));
						request.setAttribute("currentView",
						"createcampaign.jsp");
					}
				} else {
					request.setAttribute("msg", msg);
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("emailTemplateList", Arrays
							.asList(service
									.getEmailTemplates(new EmailTemplateVO())));
					request.setAttribute("smsTemplateList", Arrays
							.asList(service
									.getSmsTemplates(new SmsTemplateVO())));
					request.setAttribute("currentView", "createcampaign.jsp");
				}
			} else if (action.equals("CancelCreateCampaign")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					buildObject(request);
					request.removeAttribute("currentCampaignVO");
					request.setAttribute("campaignList", Arrays.asList(service
							.getCampaigns(new CampaignCriteriaVO())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("currentView", "campaign.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("EditCampaign")) {
				try {

					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer campaignId = new Integer(request
							.getParameter("CampaignVO.CampId"));
					CampaignVO currentCampaignVO = service
					.getCampaign(campaignId);
					request
					.setAttribute("currentCampaignVO",
							currentCampaignVO);
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
					branchCriteriaVO.setCompId(currentCampaignVO.getCompId());
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(branchCriteriaVO)));
					request.setAttribute("emailTemplateList", Arrays
							.asList(service
									.getEmailTemplates(new EmailTemplateVO())));
					request.setAttribute("smsTemplateList", Arrays
							.asList(service
									.getSmsTemplates(new SmsTemplateVO())));
					request.setAttribute("currentView", "createcampaign.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("GoToReports")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					EmpVO empVO = ApplicationContext
					.getApplicationUser(request);
					if (empVO.getRoleId().toString().equals("1")
							|| empVO.getRoleId().toString().equals("2")) {
						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request
						.setAttribute(
								"feedbackList",
								Arrays
								.asList(service
										.getFeedbacks(new FeedbackCriteriaVO())));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
					} else if (empVO.getRoleId().toString().equals("3")) {

						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						FeedbackCriteriaVO feedbackCriteriaVO = new FeedbackCriteriaVO();
						feedbackCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("feedbackList", Arrays
								.asList(service
										.getFeedbacks(feedbackCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

					} else if (empVO.getRoleId().toString().equals("4")
							|| empVO.getRoleId().toString().equals("5")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						branchCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						FeedbackCriteriaVO feedbackCriteriaVO = new FeedbackCriteriaVO();
						feedbackCriteriaVO.setCompId(empVO.getCompId());
						feedbackCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("feedbackList", Arrays
								.asList(service
										.getFeedbacks(feedbackCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setBranchId(empVO.getBranchId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
					}

					request.setAttribute("currentView", "feedback.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("SearchFeedback")) {
				try {
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);

					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					FeedbackCriteriaVO currentFeedbackCriteriaVO = (FeedbackCriteriaVO) request
					.getAttribute("currentFeedbackCriteriaVO");

					EmpVO empVO = ApplicationContext
					.getApplicationUser(request);

					if (empVO.getRoleId().toString().equals("1")
							|| empVO.getRoleId().toString().equals("2")) {
						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setStatus(new Integer(1));
						campaignCriteriaVO.setCompId(currentFeedbackCriteriaVO
								.getCompId());
						campaignCriteriaVO
						.setBranchId(currentFeedbackCriteriaVO
								.getBranchId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(currentFeedbackCriteriaVO
								.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
					} else if (empVO.getRoleId().toString().equals("3")) {

						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						currentFeedbackCriteriaVO.setCompId(empVO.getCompId());

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO
						.setBranchId(currentFeedbackCriteriaVO
								.getBranchId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

					} else if (empVO.getRoleId().toString().equals("4")
							|| empVO.getRoleId().toString().equals("5")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						branchCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						currentFeedbackCriteriaVO.setCompId(empVO.getCompId());
						currentFeedbackCriteriaVO.setBranchId(empVO
								.getBranchId());

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setBranchId(empVO.getBranchId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
					}

					request.setAttribute("feedbackList", Arrays.asList(service
							.getFeedbacks(currentFeedbackCriteriaVO)));
					request.setAttribute("currentView", "feedback.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("ViewFeedback")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					Integer feedbackId = new Integer(request
							.getParameter("FeedbackResultVO.FeedbackId"));
					FeedbackResultVO currentFeedbackResultVO = service
					.getFeedback(feedbackId);

					request.setAttribute("currentFeedbackResultVO",
							currentFeedbackResultVO);
					request.setAttribute("currentView", "feedbackview.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("GoToDevice")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					request.setAttribute("deviceList", Arrays.asList(service
							.getDevices(new DeviceVO())));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("currentView", "device.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (action.equals("SearchDevice")) {
				try {
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					DeviceVO currentDeviceVO = (DeviceVO) request
					.getAttribute("currentDeviceVO");
					request.setAttribute("deviceList", Arrays.asList(service
							.getDevices(currentDeviceVO)));
					request.setAttribute("companyList", Arrays.asList(service
							.getCompanies(new CompanyVO())));
					request.setAttribute("branchList", Arrays.asList(service
							.getBranches(new BranchCriteriaVO())));
					request.setAttribute("currentView", "device.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}

			} else if (action.equals("GiveCampaign")) {
				String msg = "";
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//

					DateFormat df = DateFormat
					.getDateInstance(DateFormat.DEFAULT);
					Integer campId = new Integer(request.getParameter("CampId"));
					Integer campType = new Integer(request
							.getParameter("CampType"));
					String[] customersId = request
					.getParameterValues("CustomersId");
					if (campId != null && campType != null
							&& customersId != null) {
						SettingVO settingVO = service.getSetting();
						for (int i = 0; i < customersId.length; i++) {
							CustomerVO customerVO = service
							.getCustomer(new Integer(customersId[i]));
							CustCampaignVO custCampaignVO = new CustCampaignVO();
							custCampaignVO.setCampId(campId);
							custCampaignVO
							.setCustId(new Integer(customersId[i]));
							custCampaignVO.setCompId(customerVO.getCompId());
							custCampaignVO
							.setBranchId(customerVO.getBranchId());
							CustCampaignVO cc = service
							.storeCustCampaign(custCampaignVO);
							msg = "Campaign given successfully.....";

							CampaignVO campaignVO = service.getCampaign(campId);
							// CompanyVO companyVO =
							// service.getCompany(customerVO.getCompId());
							if (campType.toString().equals("1")) {
								// System.out.println("Both");

								Integer emailTemplateId = campaignVO
								.getSmsTemplate();
								EmailTemplateVO emailTemplateVO = service
								.getEmailTemplate(emailTemplateId);
								settingVO.setEmailTo(customerVO.getEmailId());
								Integer smsTemplateId = campaignVO
								.getSmsTemplate();
								SmsTemplateVO smsTemplateVO = service
								.getSmsTemplate(smsTemplateId);
								new EmailUtil().sendEmail(settingVO,
										emailTemplateVO.getSubject(),
										emailTemplateVO.getBody());

								service.sendSms(customerVO.getMobileNo()
										.toString(), smsTemplateVO.getText());

								/*
								 * Using my template.
								 * service.sendSms(customerVO.getMobileNo().toString(),"Your
								 * Id : "+cc.getId()+"
								 * "+campaignVO.getDescription()); HashMap hm =
								 * new HashMap(); CampaignTemplateVO
								 * campaignTemplateVO = new
								 * CampaignTemplateVO();
								 * campaignTemplateVO.setCustomerCampaignId(cc.getId());
								 * campaignTemplateVO.setCampName(campaignVO.getCampName());
								 * campaignTemplateVO.setDescription(campaignVO.getDescription());
								 * 
								 * String startDate =
								 * df.format(campaignVO.getStartDate()); String
								 * endDate = df.format(campaignVO.getEndDate());
								 * 
								 * campaignTemplateVO.setStartDate(startDate);
								 * campaignTemplateVO.setEndDate(endDate);
								 * campaignTemplateVO.setCompName(companyVO.getName());
								 * campaignTemplateVO.setCompAddress(companyVO.getAddress());
								 * 
								 * hm.put("msg","This is system generated email
								 * do not reply it.");
								 * hm.put("campaignTemplateVO",
								 * campaignTemplateVO); String body =
								 * PrintUtil.getText("campaign.vm", hm);
								 * 
								 * new EmailUtil().sendEmail(settingVO,"Campaign
								 * from "+companyVO.getName(),body);
								 */

							} else if (campType.toString().equals("2")) {
								// System.out.println("SMS");

								Integer smsTemplateId = campaignVO
								.getSmsTemplate();
								SmsTemplateVO smsTemplateVO = service
								.getSmsTemplate(smsTemplateId);
								service.sendSms(customerVO.getMobileNo()
										.toString(), smsTemplateVO.getText());

								/*
								 * Using my template
								 * service.sendSms(customerVO.getMobileNo().toString(),"Your
								 * Id : "+cc.getId()+"
								 * "+CampaignVO.getDescription());
								 */
							} else if (campType.toString().equals("3")) {
								// System.out.println("Email");

								Integer emailTemplateId = campaignVO
								.getEmailTemplate();
								EmailTemplateVO emailTemplateVO = service
								.getEmailTemplate(emailTemplateId);
								settingVO.setEmailTo(customerVO.getEmailId());
								new EmailUtil().sendEmail(settingVO,
										emailTemplateVO.getSubject(),
										emailTemplateVO.getBody());

								/*
								 * HashMap hm = new HashMap();
								 * CampaignTemplateVO campaignTemplateVO = new
								 * CampaignTemplateVO();
								 * campaignTemplateVO.setCustomerCampaignId(cc.getId());
								 * campaignTemplateVO.setCampName(campaignVO.getCampName());
								 * campaignTemplateVO.setDescription(campaignVO.getDescription());
								 * 
								 * String startDate =
								 * df.format(campaignVO.getStartDate()); String
								 * endDate = df.format(campaignVO.getEndDate());
								 * 
								 * campaignTemplateVO.setStartDate(startDate);
								 * campaignTemplateVO.setEndDate(endDate);
								 * campaignTemplateVO.setCompName(companyVO.getName());
								 * campaignTemplateVO.setCompAddress(companyVO.getAddress());
								 * hm.put("msg","This is system generated email
								 * do not reply it.");
								 * hm.put("campaignTemplateVO",
								 * campaignTemplateVO); String body =
								 * PrintUtil.getText("campaign.vm", hm); new
								 * EmailUtil().sendEmail(settingVO,"Campaign
								 * from "+companyVO.getName(),body);
								 */
							}
						}
					} else {
						msg = "Please select customer....";
					}
					request.setAttribute("campId", campId);
					request.setAttribute("campType", campType);
					EmpVO empVO = ApplicationContext
					.getApplicationUser(request);
					if (empVO.getRoleId().toString().equals("1")
							|| empVO.getRoleId().toString().equals("2")) {
						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request
						.setAttribute(
								"feedbackList",
								Arrays
								.asList(service
										.getFeedbacks(new FeedbackCriteriaVO())));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
					} else if (empVO.getRoleId().toString().equals("3")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						FeedbackCriteriaVO feedbackCriteriaVO = new FeedbackCriteriaVO();
						feedbackCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("feedbackList", Arrays
								.asList(service
										.getFeedbacks(feedbackCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

					} else if (empVO.getRoleId().toString().equals("4")
							|| empVO.getRoleId().toString().equals("5")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						branchCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						FeedbackCriteriaVO feedbackCriteriaVO = new FeedbackCriteriaVO();
						feedbackCriteriaVO.setCompId(empVO.getCompId());
						feedbackCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("feedbackList", Arrays
								.asList(service
										.getFeedbacks(feedbackCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setBranchId(empVO.getBranchId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
					}
					request.setAttribute("msg", msg);
					request.setAttribute("currentView", "feedback.jsp");
				} catch (Exception e) {
					System.out.println("Exp :" + e.getMessage());
				}
			} else if (action.equals("GoToCustomerCampaign")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					EmpVO empVO = ApplicationContext
					.getApplicationUser(request);
					if (empVO.getRoleId().toString().equals("1")
							|| empVO.getRoleId().toString().equals("2")) {
						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						request.setAttribute("branchList", Arrays
								.asList(service
										.getBranches(new BranchCriteriaVO())));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));
						request
						.setAttribute(
								"custCampaignList",
								Arrays
								.asList(service
										.getCustomerCampaigns(new CustCampaignVO())));

					} else if (empVO.getRoleId().toString().equals("3")) {

						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

						CustCampaignVO custCampaignVO = new CustCampaignVO();
						custCampaignVO.setCompId(empVO.getCompId());
						request.setAttribute("custCampaignList", Arrays
								.asList(service
										.getCustomerCampaigns(custCampaignVO)));

					} else if (empVO.getRoleId().toString().equals("4")
							|| empVO.getRoleId().toString().equals("5")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						branchCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setBranchId(empVO.getBranchId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

						CustCampaignVO custCampaignVO = new CustCampaignVO();
						custCampaignVO.setCompId(empVO.getCompId());
						custCampaignVO.setBranchId(empVO.getBranchId());
						request.setAttribute("custCampaignList", Arrays
								.asList(service
										.getCustomerCampaigns(custCampaignVO)));

					}

					request.setAttribute("currentView", "custcampaign.jsp");
				} catch (Exception e) {
					System.out.println("Exception :" + e.getMessage());
				}

			} else if (action.equals("SearchCustCampaign")) {
				try {
					buildObject(request);
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					CustCampaignVO currentCustCampaignVO = (CustCampaignVO) request
					.getAttribute("currentCustCampaignVO");

					EmpVO empVO = ApplicationContext
					.getApplicationUser(request);

					if (empVO.getRoleId().toString().equals("1")
							|| empVO.getRoleId().toString().equals("2")) {
						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setStatus(new Integer(1));

						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(new CompanyVO())));
						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(currentCustCampaignVO
								.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

					} else if (empVO.getRoleId().toString().equals("3")) {

						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

						currentCustCampaignVO.setCompId(empVO.getCompId());

					} else if (empVO.getRoleId().toString().equals("4")
							|| empVO.getRoleId().toString().equals("5")) {
						CompanyVO companyVO = new CompanyVO();
						companyVO.setCompId(empVO.getCompId());
						request.setAttribute("companyList", Arrays
								.asList(service.getCompanies(companyVO)));

						BranchCriteriaVO branchCriteriaVO = new BranchCriteriaVO();
						branchCriteriaVO.setCompId(empVO.getCompId());
						branchCriteriaVO.setBranchId(empVO.getBranchId());
						request.setAttribute("branchList", Arrays
								.asList(service.getBranches(branchCriteriaVO)));

						CampaignCriteriaVO campaignCriteriaVO = new CampaignCriteriaVO();
						campaignCriteriaVO.setCompId(empVO.getCompId());
						campaignCriteriaVO.setBranchId(empVO.getBranchId());
						campaignCriteriaVO.setStatus(new Integer(1));
						request.setAttribute("campaignList", Arrays
								.asList(service
										.getCampaigns(campaignCriteriaVO)));

						currentCustCampaignVO.setCompId(empVO.getCompId());
						currentCustCampaignVO.setBranchId(empVO.getBranchId());

					}

					request
					.setAttribute(
							"custCampaignList",
							Arrays
							.asList(service
									.getCustomerCampaigns(currentCustCampaignVO)));
					request.setAttribute("currentView", "custcampaign.jsp");
				} catch (Exception e) {
					System.out.println("Exception :" + e.getMessage());
				}

			} else if (action.equals("GoToEmailTemplate")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				try {
					request.setAttribute("emailTemplateList", Arrays
							.asList(service
									.getEmailTemplates(new EmailTemplateVO())));
					request.setAttribute("currentView", "emailtemplate.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("GoToCreateEmailTemplate")) {
				request.setAttribute("currentView", "createemailtemplate.jsp");
			} else if (action.equals("SearchEmailTemplate")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				try {
					buildObject(request);
					EmailTemplateVO currentEmailTemplateVO = (EmailTemplateVO) request
					.getAttribute("currentEmailTemplateVO");
					request
					.setAttribute(
							"emailTemplateList",
							Arrays
							.asList(service
									.getEmailTemplates(currentEmailTemplateVO)));
					request.setAttribute("currentView", "emailtemplate.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("CreateEmailTemplate")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					String msg = null;
					boolean valid = true;
					buildObject(request);
					EmailTemplateVO currentEmailTemplateVO = (EmailTemplateVO) request
					.getAttribute("currentEmailTemplateVO");
					if (currentEmailTemplateVO.getName().trim().equals("")) {
						msg = "Please enter Template name";
						valid = false;
					}
					if (valid) {
						try {
							service.storeEmailTemplate(currentEmailTemplateVO);
							msg = "Email Template saved successfully.";
							request.removeAttribute("currentEmailTemplateVO");
							request.setAttribute("msg", msg);
							request
							.setAttribute(
									"emailTemplateList",
									Arrays
									.asList(service
											.getEmailTemplates(new EmailTemplateVO())));
							request.setAttribute("currentView",
							"emailtemplate.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("msg", msg);
							request.setAttribute("currentView",
							"createemailtemplate.jsp");
						}
					} else {
						request.setAttribute("msg", msg);
						request.setAttribute("currentView",
						"createemailtemplate.jsp");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CancelCreateEmailTemplate")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				buildObject(request);
				request.removeAttribute("currentEmailTemplateVO");
				try {
					request.setAttribute("emailTemplateList", Arrays
							.asList(service
									.getEmailTemplates(new EmailTemplateVO())));
					request.setAttribute("currentView", "emailtemplate.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("EditEmailTemplate")) {
				try {
					// String dbName = (String)
					// request.getSession().getAttribute("dbName");
					// Service service= new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer emailTemplateId = new Integer(request
							.getParameter("EmailTemplateVO.Id"));
					EmailTemplateVO currentEmailTemplateVO = service
					.getEmailTemplate(emailTemplateId);
					request.setAttribute("currentEmailTemplateVO",
							currentEmailTemplateVO);
					request.setAttribute("currentView",
					"createemailtemplate.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToSmsTemplate")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				try {
					request.setAttribute("smsTemplateList", Arrays
							.asList(service
									.getSmsTemplates(new SmsTemplateVO())));
					request.setAttribute("currentView", "smstemplate.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("GoToCreateSMSTemplate")) {
				request.setAttribute("currentView", "createsmstemplate.jsp");
			} else if (action.equals("SearchSmsTemplate")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				try {
					buildObject(request);
					SmsTemplateVO currentSmsTemplateVO = (SmsTemplateVO) request
					.getAttribute("currentSmsTemplateVO");
					request.setAttribute("smsTemplateList", Arrays
							.asList(service
									.getSmsTemplates(currentSmsTemplateVO)));
					request.setAttribute("currentView", "smstemplate.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("CreateSmsTemplate")) {
				try {
					//String dbName =  (String) request.getSession().getAttribute("dbName");
					//Service service=  new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					String msg = null;
					boolean valid = true;
					buildObject(request);
					SmsTemplateVO currentSmsTemplateVO = (SmsTemplateVO) request
					.getAttribute("currentSmsTemplateVO");
					if (currentSmsTemplateVO.getName().trim().equals("")) {
						msg = "Please enter Template name";
						valid = false;
					}
					if (valid) {
						try {
							service.storeSmsTemplate(currentSmsTemplateVO);
							msg = "Sms Template saved successfully.";
							request.removeAttribute("currentSmsTemplateVO");
							request.setAttribute("msg", msg);
							request
							.setAttribute(
									"smsTemplateList",
									Arrays
									.asList(service
											.getSmsTemplates(new SmsTemplateVO())));
							request.setAttribute("currentView",
							"smstemplate.jsp");
						} catch (Exception ex) {
							msg = ex.getMessage();
							request.setAttribute("msg", msg);
							request.setAttribute("currentView",
							"createsmstemplate.jsp");
						}
					} else {
						request.setAttribute("msg", msg);
						request.setAttribute("currentView",
						"createsmstemplate.jsp");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (action.equals("CancelCreateSmsTemplate")) {
				DbConnection dbConnection = (DbConnection) request.getSession()
				.getAttribute("dbConnection");//
				Service service = new Service(dbConnection);//
				buildObject(request);
				request.removeAttribute("currentSmsTemplateVO");
				try {
					request.setAttribute("smsTemplateList", Arrays
							.asList(service
									.getSmsTemplates(new SmsTemplateVO())));
					request.setAttribute("currentView", "smstemplate.jsp");
				} catch (Exception e) {
					// TODO: handle exception
				}
			} else if (action.equals("EditSmsTemplate")) {
				try {
					//String dbName =  (String) request.getSession().getAttribute("dbName");
					//Service service=  new Service(dbName);
					DbConnection dbConnection = (DbConnection) request
					.getSession().getAttribute("dbConnection");//
					Service service = new Service(dbConnection);//
					Integer smsTemplateId = new Integer(request
							.getParameter("SmsTemplateVO.Id"));
					SmsTemplateVO currentSmsTemplateVO = service
					.getSmsTemplate(smsTemplateId);
					request.setAttribute("currentSmsTemplateVO",
							currentSmsTemplateVO);
					request
					.setAttribute("currentView",
					"createsmstemplate.jsp");
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			} else if (action.equals("GoToAnalytics")) {
				request.setAttribute("currentView", "analytics.jsp");
			} else if (action.equals("Analytics")) {
				request.setAttribute("currentView", "analytics.jsp");
			}

			request.getRequestDispatcher(File.separator + "template.jsp")
			.forward(request, response);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}