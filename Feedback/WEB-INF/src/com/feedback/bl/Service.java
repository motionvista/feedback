package com.feedback.bl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.feedback.vo.BranchCriteriaVO;
import com.feedback.vo.BranchVO;
import com.feedback.vo.CampaignCriteriaVO;
import com.feedback.vo.CampaignVO;
import com.feedback.vo.CompanyVO;
import com.feedback.vo.CustCampaignVO;
import com.feedback.vo.CustomerCriteriaVO;
import com.feedback.vo.CustomerVO;
import com.feedback.vo.DeviceVO;
import com.feedback.vo.EmailTemplateVO;
import com.feedback.vo.EmpCriteriaVO;
import com.feedback.vo.EmpVO;
import com.feedback.vo.FeedbackCriteriaVO;
import com.feedback.vo.FeedbackQuestionAnsVO;
import com.feedback.vo.FeedbackResultVO;
import com.feedback.vo.FeedbackVO;
import com.feedback.vo.FormCriteriaVO;
import com.feedback.vo.FormQuestionVO;
import com.feedback.vo.FormVO;
import com.feedback.vo.QuestionVO;
import com.feedback.vo.RoleVO;
import com.feedback.vo.SettingVO;
import com.feedback.vo.CountryVO;
import com.feedback.vo.CityVO;
import com.feedback.vo.SmsTemplateVO;
import com.feedback.vo.StateVO;
import com.feedback.vo.RolePermissionVO;

public class Service {
	public static final String VERSION = "1.0";

	public static final String BUILD = "01102013";

	private static SessionFactory sessionFactory = null;

	private static final String dbDriver = "com.mysql.jdbc.Driver";

	private static String dbUrl = "jdbc:mysql://127.0.0.1/";

	private static final String dbUser = "root";

	private static final String dbPassword = "1234";

	private static SettingVO settingVO = null;

	private Connection conn = null;

	// private static ConnectionPool pool = null;

	public Service(String dbName) {
		try {
			System.out.println("with db name call");
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl + dbName, dbUser,dbPassword);

			// Create the SessionFactory from hibernate.cfg.xml
			Configuration c = new Configuration();
			c.setProperty("hibernate.connection.url", dbUrl + dbName);
			Configuration cfg = c.configure();
			sessionFactory = cfg.buildSessionFactory();

			settingVO = getSetting();

		} catch (Throwable ex) {
			ex.printStackTrace();
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public Service(DbConnection dbConnection) {
		System.out.println("with db call");

		try {
			conn = dbConnection.getConnection();
			sessionFactory = dbConnection.getConfiguration().buildSessionFactory();
			settingVO = getSetting();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Session getSession() throws Exception {
		return sessionFactory.openSession();
	}

	private void close(Session session) {
		try {
			session.connection().close();
			session.close();
			// session.getSessionFactory().close();
			// sessionFactory.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public SettingVO getSetting() throws Exception {
		Session session = getSession();
		SettingVO settingVO = null;
		try {
			session.beginTransaction();
			settingVO = (SettingVO) session.createQuery("FROM SettingVO u WHERE u.Id=?").setInteger(0, 1).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return settingVO;
	}

	public boolean sendSms(String number, String sms) throws Exception {
		boolean flag = false;
		try {

			String smsUrl = settingVO.getSmsUrl();
			smsUrl = smsUrl.replaceAll("#1", number);
			smsUrl = smsUrl.replaceAll("#2", URLEncoder.encode(sms, "UTF-8"));
			// System.out.println("smsUrl -->"+smsUrl);

			URL url = new URL(smsUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String line;
			StringBuffer buffer = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			System.out.println("SMS Server Response : " + buffer.toString());
			rd.close();
			con.disconnect();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return flag;
	}

	public EmpVO getUserByLoginId(String loginId) throws Exception {
		Session session = getSession();
		EmpVO emp = null;
		try {
			session.beginTransaction();
			emp = (EmpVO) session.createQuery("FROM EmpVO u WHERE u.LoginId=? ").setString(0, loginId).uniqueResult();
			session.getTransaction().commit();
			if (emp != null) {
				List plist = session.createQuery("SELECT rp.PermissionId FROM RolePermissionVO rp WHERE rp.RoleId=?")
						.setInteger(0, emp.getRoleId().intValue()).list();
				emp.setPermissions(plist);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return emp;
	}

	public void storeRolePermissions(Integer roleId, Integer[] permissionIds)
	throws Exception {
		Session session = getSession();
		try {
			session.beginTransaction();
			int l = session.createQuery("DELETE FROM RolePermissionVO rp WHERE rp.RoleId=?")
			.setInteger(0, roleId.intValue()).executeUpdate();
			session.getTransaction().commit();
			session.beginTransaction();
			for (int i = 0; i < permissionIds.length; i++) {
				RolePermissionVO roleP = new RolePermissionVO();
				roleP.setRoleId(roleId);
				roleP.setPermissionId(permissionIds[i]);
				session.save(roleP);
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
	}

	public Integer[] getRolePermissions(Integer roleId) throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			list = session
			.createQuery("SELECT rp.PermissionId FROM RolePermissionVO rp WHERE rp.RoleId=?")
					.setInteger(0, roleId.intValue()).list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (Integer[]) list.toArray(new Integer[list.size()]);
	}

	public CountryVO storeCountry(CountryVO countryVO) throws Exception {
		Session session = getSession();
		try {
			CountryVO found = null;
			session.beginTransaction();

			if (countryVO.getCountryId() == null) {
				found = (CountryVO) session.createQuery("FROM CountryVO c WHERE c.Name=?").setString(0,
						countryVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("Country already exists.");
				}
				countryVO.setCreateTime(new Date());
				session.save(countryVO);
			} else {
				/*
				 * found = (CountryVO) session.createQuery("FROM CountryVO c
				 * WHERE
				 * c.Name=?").setString(0,countryVO.getName()).uniqueResult();
				 * if (found != null) { throw new Exception("Country already
				 * exists."); }
				 */
				countryVO.setCreateTime(new Date());
				session.update(countryVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return countryVO;
	}

	public CountryVO[] getCountries(CountryVO countryVO) throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			if (countryVO.getName() != null) {
				list = session
				.createQuery("FROM CountryVO c WHERE c.Name LIKE '%"+ countryVO.getName()+ "%'ORDER BY c.Name ASC").list();
			} else {
				list = session.createQuery("FROM CountryVO c ORDER BY c.Name ASC").list();
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (CountryVO[]) list.toArray(new CountryVO[list.size()]);
	}

	public CountryVO getCountry(Integer id) throws Exception {
		Session session = getSession();
		CountryVO countryVO = null;
		try {
			session.beginTransaction();
			countryVO = (CountryVO) session.createQuery("FROM CountryVO c WHERE c.CountryId=?").setInteger(0,id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return countryVO;
	}

	public StateVO storeState(StateVO stateVO) throws Exception {
		Session session = getSession();
		try {
			StateVO found = null;
			session.beginTransaction();

			if (stateVO.getStateId() == null) {
				found = (StateVO) session.createQuery("FROM StateVO c WHERE c.Name=?").setString(0,stateVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("Country already exists.");
				}
				stateVO.setCreateTime(new Date());
				session.save(stateVO);
			} else {
				/*
				 * found = (StateVO) session.createQuery("FROM StateVO c WHERE
				 * c.Name=?").setString(0,stateVO.getName()).uniqueResult(); if
				 * (found != null) { throw new Exception("State already
				 * exists."); }
				 */
				stateVO.setCreateTime(new Date());
				session.update(stateVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return stateVO;
	}

	public StateVO[] getStates(StateVO stateVO) throws Exception {

		ArrayList list = new ArrayList();
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer(" SELECT s.state_id,s.name,s.country_id");
			sb.append(",c.name");
			sb.append(" from state s,country c where s.country_id = c.country_id");
			if (stateVO.getName() != null) {
				sb.append(" AND s.name like'%" + stateVO.getName() + "%'");
			}
			if (stateVO.getCountryId() != null) {
				sb.append(" AND s.country_id=" + stateVO.getCountryId());
			}
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				StateVO stateVO2 = new StateVO();
				stateVO2.setStateId(new Integer(rs.getInt(1)));
				stateVO2.setName(rs.getString(2));
				stateVO2.setCountryId(new Integer(rs.getInt(3)));
				stateVO2.setCountryName(rs.getString(4));
				list.add(stateVO2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (StateVO[]) list.toArray(new StateVO[list.size()]);
	}

	public StateVO[] getStates(Integer countryId) throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			list = session.createQuery("FROM StateVO c WHERE c.CountryId='" + countryId+ "' ORDER BY c.Name ASC").list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (StateVO[]) list.toArray(new StateVO[list.size()]);
	}

	public StateVO getState(Integer id) throws Exception {
		Session session = getSession();
		StateVO stateVO = null;
		try {
			session.beginTransaction();
			stateVO = (StateVO) session.createQuery("FROM StateVO c WHERE c.StateId=?").setInteger(0,id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return stateVO;
	}

	public CityVO storeCity(CityVO cityVO) throws Exception {
		Session session = getSession();
		try {
			CityVO found = null;
			session.beginTransaction();

			if (cityVO.getCityId() == null) {
				found = (CityVO) session.createQuery("FROM CityVO c WHERE c.Name=?").setString(0,cityVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("City already exists!!!!");
				}
				cityVO.setCreateTime(new Date());
				session.save(cityVO);
			} else {
				/*
				 * found = (CityVO) session.createQuery("FROM CityVO c WHERE
				 * c.Name=?").setString(0,cityVO.getName()).uniqueResult(); if
				 * (found != null) { throw new Exception("City already
				 * exists."); }
				 */
				cityVO.setCreateTime(new Date());
				session.update(cityVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return cityVO;
	}

	public CityVO[] getCities(CityVO cityVO) throws Exception {

		ArrayList list = new ArrayList();
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer(" SELECT c.city_id,c.name,c.state_id,c.country_id");
			sb.append(",s.name");
			sb.append(",cn.name");
			sb.append(" from city c,state s,country cn where s.state_id = c.state_id AND cn.country_id = c.country_id");

			if (cityVO.getCountryId() != null) {
				sb.append(" AND c.country_id=" + cityVO.getCountryId());
			}
			if (cityVO.getStateId() != null) {
				sb.append(" AND c.state_id=" + cityVO.getStateId());
			}
			if (cityVO.getName() != null) {
				sb.append(" AND c.name like'%" + cityVO.getName() + "%'");
			}

			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				CityVO cityVO2 = new CityVO();
				cityVO2.setCityId(new Integer(rs.getInt(1)));
				cityVO2.setName(rs.getString(2));
				cityVO2.setStateId(new Integer(rs.getInt(3)));
				cityVO2.setCountryId(new Integer(rs.getInt(4)));
				cityVO2.setStateName(rs.getString(5));
				cityVO2.setCountryName(rs.getString(6));
				list.add(cityVO2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (CityVO[]) list.toArray(new CityVO[list.size()]);
	}

	public CityVO[] getCities(Integer stateId) throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			list = session.createQuery("FROM CityVO c WHERE c.StateId='" + stateId+ "' ORDER BY c.Name ASC").list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (CityVO[]) list.toArray(new CityVO[list.size()]);
	}

	public CityVO getCity(Integer id) throws Exception {
		Session session = getSession();
		CityVO cityVO = null;
		try {
			session.beginTransaction();
			cityVO = (CityVO) session.createQuery("FROM CityVO c WHERE c.CityId=?").setInteger(0,id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return cityVO;
	}

	public EmpVO storeEmp(EmpVO empVO) throws Exception {
		Session session = getSession();
		try {
			session.beginTransaction();
			if (empVO.getEmpId() == null) {
				EmpVO found = (EmpVO) session.createQuery("FROM EmpVO u WHERE u.MobileNo=? OR u.LoginId=?")
				.setString(0, empVO.getMobileNo()).setString(1,empVO.getLoginId()).uniqueResult();
				if (found != null) {
					throw new Exception("Employee already exists.");
				}
				empVO.setCreateTime(new Date());
				session.save(empVO);

			} else {
				/*
				 * EmpVO dbUser = getEmployee(empVO.getLoginId()); if
				 * (!dbUser.getLoginId().equals(empVO.getLoginId()) &&
				 * !dbUser.getPassword().equals(empVO.getPassword())) { throw
				 * new Exception("Login Id & Password can't be changed."); }
				 */
				empVO.setCreateTime(new Date());
				session.update(empVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}

		return empVO;
	}

	public EmpVO[] getEmployees(EmpCriteriaVO empCriteriaVO) throws Exception {
		ArrayList list = new ArrayList();
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer(" SELECT e.emp_id,e.first_name,e.role_id,c.comp_name,b.branch_name,e.email_id from employee e, branch b,company c where e.comp_id = c.comp_id AND e.branch_id = b.branch_id");
			if (empCriteriaVO.getFirstName() != null) {
				sb.append(" AND e.first_name Like '%"+ empCriteriaVO.getFirstName() + "%'");
			}

			if (empCriteriaVO.getRoleId() != null) {
				sb.append(" AND e.role_id = " + empCriteriaVO.getRoleId());
			}

			if (empCriteriaVO.getCompId() != null) {
				sb.append(" AND e.comp_id = " + empCriteriaVO.getCompId());
			}

			if (empCriteriaVO.getBranchId() != null) {
				sb.append(" AND e.branch_id = " + empCriteriaVO.getBranchId());
			}

			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				EmpVO empVO = new EmpVO();
				empVO.setEmpId(new Integer(rs.getInt(1)));
				empVO.setFirstName((rs.getString(2)));
				empVO.setRoleName(RoleVO.getRoleName(new Integer(rs.getString(3))));
				empVO.setCompanyName(rs.getString(4));
				empVO.setBranchName(rs.getString(5));
				empVO.setEmailId(rs.getString(6));
				list.add(empVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (EmpVO[]) list.toArray(new EmpVO[list.size()]);
	}

	public EmpVO getEmployee(Integer id) throws Exception {
		Session session = getSession();
		EmpVO empVO = null;

		try {
			session.beginTransaction();
			empVO = (EmpVO) session.createQuery("FROM EmpVO c WHERE c.EmpId=?").setInteger(0, id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return empVO;
	}

	public EmpVO getEmployee(String loginId) throws Exception {
		Session session = getSession();
		EmpVO empVO = null;

		try {
			session.beginTransaction();
			empVO = (EmpVO) session.createQuery("FROM EmpVO c WHERE c.LoginId=?").setString(0, loginId).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return empVO;
	}

	public QuestionVO[] getQuestions(QuestionVO questionVO) throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			if (questionVO.getQuestion() != null) {
				list = session.createQuery("FROM QuestionVO c WHERE c.Question LIKE '%"+ questionVO.getQuestion().trim()+ "%' ORDER BY c.Question ASC").list();
			} else {
				list = session.createQuery("FROM QuestionVO c ORDER BY c.Question ASC").list();
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (QuestionVO[]) list.toArray(new QuestionVO[list.size()]);
	}

	public QuestionVO getQuestion(Integer id) throws Exception {
		Session session = getSession();
		QuestionVO questionVO = null;
		try {
			session.beginTransaction();
			questionVO = (QuestionVO) session.createQuery("FROM QuestionVO c WHERE c.QuestionId=?").setInteger(0,id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return questionVO;
	}

	public QuestionVO storeQuestion(QuestionVO questionVO) throws Exception {
		Session session = getSession();
		try {
			QuestionVO found = null;
			session.beginTransaction();

			if (questionVO.getQuestionId() == null) {
				found = (QuestionVO) session.createQuery("FROM QuestionVO c WHERE c.Question=?").setString(0,
						questionVO.getQuestion()).uniqueResult();
				if (found != null) {
					throw new Exception("Question already exists.");
				}
				questionVO.setCreateTime(new Date());
				session.save(questionVO);
			} else {
				/*
				 * found = (QuestionVO) session.createQuery("FROM QuestionVO c
				 * WHERE
				 * c.Question=?").setString(0,questionVO.getQuestion()).uniqueResult();
				 * if (found != null) { throw new Exception("Question already
				 * exists."); }
				 */
				questionVO.setCreateTime(new Date());
				session.update(questionVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return questionVO;
	}

	public FormVO storeForm(FormVO formVO) throws Exception {
		Session session = getSession();
		try {
			session.beginTransaction();
			int count = 0;
			Statement smt = session.connection().createStatement();
			ResultSet rs = smt
			.executeQuery("SELECT count(*) FROM form_master where status = '"+ 1+ "' AND comp_id='"+ formVO.getCompId()+ "' AND branch_id = '"+ formVO.getBranchId()+ "'");
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (formVO.getFormId() == null) {

				if (count > 0 && formVO.getStatus().toString().equals("1")) {
					throw new Exception("Please Inactive another form for this company and Branch");
				} else {
					formVO.setCreateTime(new Date());
					session.save(formVO);
					FormQuestionVO fq[] = formVO.getQuestions();
					for (int i = 0; i < fq.length; i++) {
						fq[i].setFormId(formVO.getFormId());
						session.save(fq[i]);
					}
				}
			} else {
				Integer fId = new Integer(0);
				if (count == 1) {
					Statement smt1 = session.connection().createStatement();
					ResultSet rs1 = smt1.executeQuery("SELECT form_id FROM form_master where status = '"+ 1+ "' AND comp_id='"+ formVO.getCompId()+ "' AND branch_id = '"+ formVO.getBranchId() + "'");
					while (rs1.next()) {
						fId = new Integer(rs1.getInt(1));
					}
				}
				if (count > 0&& formVO.getStatus().toString().equals("1")&& !fId.toString().equals(formVO.getFormId().toString())) {
					throw new Exception("Please Inactive another form for this company and Branch");
				} else {
					formVO.setCreateTime(new Date());
					Statement stmt = session.connection().createStatement();
					StringBuffer sb = new StringBuffer("DELETE FROM form WHERE form_id="+ formVO.getFormId());
					stmt.executeUpdate(sb.toString());
					session.update(formVO);
					FormQuestionVO fq[] = formVO.getQuestions();
					for (int i = 0; i < fq.length; i++) {
						fq[i].setFormId(formVO.getFormId());
						session.save(fq[i]);
					}
				}
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return formVO;
	}

	public FormVO[] getForms(FormCriteriaVO formCriteriaVO) throws Exception {
		Session session = getSession();
		List list = new ArrayList();
		try {
			session.beginTransaction();
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer(" SELECT f.form_id,f.name,f.valid_from_date,f.valid_to_date,f.status,c.comp_name,b.branch_name from form_master f,company c,branch b where f.comp_id = c.comp_id AND f.branch_id = b.branch_id");
			if (formCriteriaVO.getCompId() != null) {
				sb.append(" AND f.comp_id=" + formCriteriaVO.getCompId());
			}
			if (formCriteriaVO.getBranchId() != null) {
				sb.append(" AND f.branch_id=" + formCriteriaVO.getBranchId());
			}
			if (formCriteriaVO.getStatus() != null) {
				sb.append(" AND f.status=" + formCriteriaVO.getStatus());
			}
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				FormVO formVO = new FormVO();
				formVO.setFormId(new Integer(rs.getInt(1)));
				formVO.setName(rs.getString(2));
				formVO.setValidFromDate(rs.getDate(3));
				formVO.setValidToDate(rs.getDate(4));
				formVO.setStatus(new Integer(rs.getInt(5)));
				formVO.setCompName(rs.getString(6));
				formVO.setBranchName(rs.getString(7));
				list.add(formVO);
			}

			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (FormVO[]) list.toArray(new FormVO[list.size()]);
	}

	public FormVO getForm(FormCriteriaVO formCriteriaVO) throws Exception {
		Session session = getSession();
		FormVO formVO = null;
		ArrayList list = new ArrayList();
		try {
			session.beginTransaction();
			formVO = (FormVO) session
			.createQuery("FROM FormVO c WHERE c.CompId=? AND c.BranchId=? AND c.Status=?")
					.setInteger(0, formCriteriaVO.getCompId().intValue())
					.setInteger(1, formCriteriaVO.getBranchId().intValue())
					.setInteger(2, formCriteriaVO.getStatus().intValue())
					.uniqueResult();
			// list = session.createQuery("FROM FormQuestionVO c WHERE
			// c.FormId=?").setInteger(0,formVO.getFormId().intValue()).list();
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer("select f.id,f.form_id,f.question_id,f.question,q.type,q.choice1,q.choice2,q.choice3,q.choice4 from form f,question q where q.question_id = f.question_id and form_id ='"
					+ formVO.getFormId() + "'");
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				FormQuestionVO formQue = new FormQuestionVO();
				formQue.setId(new Integer(rs.getInt(1)));
				formQue.setFormId(new Integer(rs.getInt(2)));
				formQue.setQuestionId(new Integer(rs.getInt(3)));
				formQue.setQuestion(rs.getString(4));
				formQue.setQueType(new Integer(rs.getInt(5)));
				formQue.setChoice1(rs.getString(6));
				formQue.setChoice2(rs.getString(7));
				formQue.setChoice3(rs.getString(8));
				formQue.setChoice4(rs.getString(9));
				list.add(formQue);
			}

			formVO.setQuestions((FormQuestionVO[]) list
					.toArray(new FormQuestionVO[list.size()]));
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return formVO;
	}

	public FormVO getForm(Integer id) throws Exception {
		Session session = getSession();
		FormVO formVO = null;
		List list = null;
		try {
			session.beginTransaction();
			formVO = (FormVO) session.createQuery("FROM FormVO c WHERE c.FormId=?").setInteger(0,
					id.intValue()).uniqueResult();
			list = session.createQuery("FROM FormQuestionVO c WHERE c.FormId=?")
			.setInteger(0, id.intValue()).list();
			formVO.setQuestions((FormQuestionVO[]) list.toArray(new FormQuestionVO[list.size()]));
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return formVO;
	}

	public CompanyVO storeCompany(CompanyVO companyVO) throws Exception {
		Session session = getSession();

		try {
			CompanyVO found = null;
			session.beginTransaction();

			if (companyVO.getCompId() == null) {
				found = (CompanyVO) session.createQuery("FROM CompanyVO c WHERE c.Name=?").setString(0,
						companyVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("Company already exists.");
				}
				companyVO.setCreateTime(new Date());
				session.save(companyVO);
				// Company as a branch.
				BranchVO branchVO = new BranchVO();
				branchVO.setName(companyVO.getName());
				branchVO.setCompId(companyVO.getCompId());
				branchVO.setPhoneNo1(companyVO.getPhoneNo1());
				branchVO.setPhoneNo2(companyVO.getPhoneNo2());
				branchVO.setEmailId(companyVO.getEmailId());
				branchVO.setAddress(companyVO.getAddress());
				branchVO.setCityId(companyVO.getCityId());
				branchVO.setStateId(companyVO.getStateId());
				branchVO.setCountryId(companyVO.getCountryId());
				storeBranch(branchVO);

			} else {
				companyVO.setCreateTime(new Date());
				session.update(companyVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return companyVO;
	}

	public CompanyVO[] getCompanies(CompanyVO companyVO) throws Exception {
		Session session = getSession();
		List list = null;
		try {

			session.beginTransaction();

			if (companyVO.getCompId() == null) {
				if (companyVO.getName() != null) {
					list = session.createQuery("FROM CompanyVO c WHERE c.Name LIKE '%"+ companyVO.getName()
							+ "%' ORDER BY c.Name ASC").list();
				} else {
					list = session.createQuery("FROM CompanyVO c ORDER BY c.Name ASC").list();
				}
			} else {
				list = session.createQuery("FROM CompanyVO c WHERE c.CompId = '"
						+ companyVO.getCompId()+ "' ORDER BY c.Name ASC").list();
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (CompanyVO[]) list.toArray(new CompanyVO[list.size()]);
	}

	public CompanyVO getCompany(Integer id) throws Exception {
		Session session = getSession();
		CompanyVO companyVO = null;
		try {
			session.beginTransaction();
			companyVO = (CompanyVO) session.createQuery("FROM CompanyVO c WHERE c.CompId=?").setInteger(0,
					id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return companyVO;
	}

	public BranchVO storeBranch(BranchVO branchVO) throws Exception {
		Session session = getSession();
		try {
			BranchVO found = null;
			session.beginTransaction();

			if (branchVO.getBranchId() == null) {
				found = (BranchVO) session.createQuery("FROM BranchVO c WHERE c.Name=?").setString(0,
						branchVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("Branch already exists.");
				}
				branchVO.setCreateTime(new Date());
				session.save(branchVO);
			} else {
				branchVO.setCreateTime(new Date());
				session.update(branchVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return branchVO;
	}

	public BranchVO[] getBranches(BranchCriteriaVO branchCriteriaVO)
	throws Exception {
		ArrayList list = new ArrayList();
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer(" SELECT b.branch_id,b.branch_name,b.address,b.phone_no1,c.comp_name from branch b,company c where b.comp_id = c.comp_id");
			if (branchCriteriaVO.getCompId() != null) {
				sb.append(" AND b.comp_id=" + branchCriteriaVO.getCompId());
			}
			if (branchCriteriaVO.getBranchId() != null) {
				sb.append(" AND b.branch_id=" + branchCriteriaVO.getBranchId());
			}
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				BranchVO branchVO = new BranchVO();
				branchVO.setBranchId(new Integer(rs.getInt(1)));
				branchVO.setName(rs.getString(2));
				branchVO.setAddress(rs.getString(3));
				branchVO.setPhoneNo1(rs.getString(4));
				branchVO.setCompName(rs.getString(5));
				list.add(branchVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (BranchVO[]) list.toArray(new BranchVO[list.size()]);
	}

	public BranchVO getBranch(Integer id) throws Exception {
		Session session = getSession();
		BranchVO branchVO = null;
		try {
			session.beginTransaction();
			branchVO = (BranchVO) session.createQuery("FROM BranchVO c WHERE c.BranchId=?").setInteger(0,
					id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return branchVO;
	}

	public EmailTemplateVO storeEmailTemplate(EmailTemplateVO emailTemplateVO)
	throws Exception {
		Session session = getSession();
		try {
			EmailTemplateVO found = null;
			session.beginTransaction();

			if (emailTemplateVO.getId() == null) {
				found = (EmailTemplateVO) session.createQuery("FROM EmailTemplateVO c WHERE c.Name=?").setString(0,
						emailTemplateVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("Email Template already exists.");
				}
				emailTemplateVO.setCreateTime(new Date());
				session.save(emailTemplateVO);
			} else {
				/*
				 * found = (EmailTemplateVO) session.createQuery("FROM
				 * EmailTemplateVO c WHERE
				 * c.Name=?").setString(0,emailTemplateVO.getName()).uniqueResult();
				 * if (found != null) { throw new Exception("Email Template
				 * already exists."); }
				 */
				emailTemplateVO.setCreateTime(new Date());
				session.update(emailTemplateVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return emailTemplateVO;
	}

	public EmailTemplateVO[] getEmailTemplates(EmailTemplateVO emailTemplateVO)
	throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			if (emailTemplateVO.getName() != null) {
				list = session.createQuery("FROM EmailTemplateVO c WHERE c.Name LIKE '%"+ emailTemplateVO.getName()
						+ "%' ORDER BY c.Name ASC").list();
			} else {
				list = session.createQuery("FROM EmailTemplateVO c ORDER BY c.Name ASC").list();
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (EmailTemplateVO[]) list
		.toArray(new EmailTemplateVO[list.size()]);
	}

	public EmailTemplateVO getEmailTemplate(Integer id) throws Exception {
		Session session = getSession();
		EmailTemplateVO emailTemplateVO = null;
		try {
			session.beginTransaction();
			emailTemplateVO = (EmailTemplateVO) session.createQuery("FROM EmailTemplateVO c WHERE c.Id=?").setInteger(0,
					id.intValue()).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return emailTemplateVO;
	}

	public SmsTemplateVO storeSmsTemplate(SmsTemplateVO smsTemplateVO)
	throws Exception {
		Session session = getSession();
		try {
			SmsTemplateVO found = null;
			session.beginTransaction();

			if (smsTemplateVO.getId() == null) {
				found = (SmsTemplateVO) session.createQuery("FROM SmsTemplateVO c WHERE c.Name=?").setString(0,
						smsTemplateVO.getName()).uniqueResult();
				if (found != null) {
					throw new Exception("Sms Template already exists.");
				}
				smsTemplateVO.setCreateTime(new Date());
				session.save(smsTemplateVO);
			} else {
				/*
				 * found = (EmailTemplateVO) session.createQuery("FROM
				 * EmailTemplateVO c WHERE
				 * c.Name=?").setString(0,emailTemplateVO.getName()).uniqueResult();
				 * if (found != null) { throw new Exception("Email Template
				 * already exists."); }
				 */
				smsTemplateVO.setCreateTime(new Date());
				session.update(smsTemplateVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return smsTemplateVO;
	}

	public SmsTemplateVO[] getSmsTemplates(SmsTemplateVO smsTemplateVO)
	throws Exception {
		Session session = getSession();
		List list = null;
		try {
			session.beginTransaction();
			if (smsTemplateVO.getName() != null) {
				list = session.createQuery("FROM SmsTemplateVO c WHERE c.Name LIKE '%"+ smsTemplateVO.getName()+ "%' ORDER BY c.Name ASC").list();
			} else {
				list = session.createQuery("FROM SmsTemplateVO c ORDER BY c.Name ASC").list();
			}
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (SmsTemplateVO[]) list.toArray(new SmsTemplateVO[list.size()]);
	}

	public SmsTemplateVO getSmsTemplate(Integer id) throws Exception {
		Session session = getSession();
		SmsTemplateVO smsTemplateVO = null;
		try {
			session.beginTransaction();
			smsTemplateVO = (SmsTemplateVO) session.createQuery("FROM SmsTemplateVO c WHERE c.Id=?").setInteger(0,
					id.intValue()).uniqueResult();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return smsTemplateVO;
	}

	public CampaignVO storeCampaign(CampaignVO campaignVO) throws Exception {
		Session session = getSession();
		try {
			// CampaignVO found = null;
			session.beginTransaction();

			if (campaignVO.getCampId() == null) {
				/*
				 * found = (CampaignVO) session.createQuery("FROM CampaignVO c
				 * WHERE
				 * c.CampName=?").setString(0,campaignVO.getCampName()).uniqueResult();
				 * if (found != null) { throw new Exception("Campaign already
				 * exists."); }
				 */
				campaignVO.setCreateTime(new Date());
				session.save(campaignVO);
			} else {
				campaignVO.setCreateTime(new Date());
				session.update(campaignVO);
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return campaignVO;
	}

	public CampaignVO[] getCampaigns(CampaignCriteriaVO campaignCriteriaVO)
	throws Exception {
		Session session = getSession();
		ArrayList list = new ArrayList();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer("SELECT c.camp_id,c.comp_id,c.branch_id,c.name,c.start_date,c.end_date,c.description,c.approve");
			sb.append(",cm.comp_name,b.branch_name from campaign c,branch b,company cm");
			sb.append(" where c.comp_id = cm.comp_id AND c.branch_id = b.branch_id");
			if (campaignCriteriaVO.getCompId() != null) {
				sb.append(" AND c.comp_id=" + campaignCriteriaVO.getCompId());
			}
			if (campaignCriteriaVO.getBranchId() != null) {
				sb.append(" AND c.branch_id="+ campaignCriteriaVO.getBranchId());
			}
			if (campaignCriteriaVO.getStatus() != null) {
				sb.append(" AND c.approve=" + campaignCriteriaVO.getStatus());
			}
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				CampaignVO campaignVO = new CampaignVO();
				campaignVO.setCampId(new Integer(rs.getInt(1)));
				campaignVO.setCompId(new Integer(rs.getInt(2)));
				campaignVO.setBranchId(new Integer(rs.getInt(3)));
				campaignVO.setCampName(rs.getString(4));
				campaignVO.setStartDate(rs.getDate(5));
				campaignVO.setEndDate(rs.getDate(6));
				campaignVO.setDescription(rs.getString(7));
				campaignVO.setApprove(new Integer(rs.getString(8)));
				campaignVO.setCompName(rs.getString(9));
				campaignVO.setBranchName(rs.getString(10));
				list.add(campaignVO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (CampaignVO[]) list.toArray(new CampaignVO[list.size()]);
	}

	public CampaignVO getCampaign(Integer id) throws Exception {
		Session session = getSession();
		CampaignVO campaignVO = null;
		try {
			session.beginTransaction();
			campaignVO = (CampaignVO) session.createQuery("FROM CampaignVO c WHERE c.CampId=?").setInteger(0,id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return campaignVO;
	}

	public CustomerVO[] getCustomers(CustomerCriteriaVO custCriteria)
	throws Exception {
		Session session = getSession();
		String query = "";

		StringBuffer sb = new StringBuffer("From CustomerVO c Where");

		if (custCriteria.getCompId() != null) {
			sb.append(" c.CompId = " + custCriteria.getCompId() + " AND");
		}
		if (custCriteria.getBranchId() != null) {
			sb.append(" c.BranchId = " + custCriteria.getBranchId() + " AND");
		}
		if (custCriteria.getCustomerName() != null) {
			sb.append(" c.CustName like '%" + custCriteria.getCustomerName()+ "%' AND");
		}
		if (custCriteria.getLocation() != null) {
			sb.append(" c.Location like '%" + custCriteria.getLocation()+ "%' AND");
		}
		if (custCriteria.getCompId() == null && custCriteria.getCustomerName() == null && custCriteria.getLocation() == null) {
			query = sb.toString().replaceAll("Where", "");
		} else {

			query = sb.toString().substring(0, sb.lastIndexOf("AND"));
		}
		query = query + " ORDER BY c.CustName ASC";

		List list = null;
		try {
			session.beginTransaction();
			list = session.createQuery(query).list();
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return (CustomerVO[]) list.toArray(new CustomerVO[list.size()]);
	}

	public CustomerVO getCustomer(Integer id) throws Exception {
		Session session = getSession();
		CustomerVO customerVO = null;
		try {
			session.beginTransaction();
			customerVO = (CustomerVO) session.createQuery("FROM CustomerVO c WHERE c.CustId=?").setInteger(0,id.intValue()).uniqueResult();
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return customerVO;
	}

	public FeedbackResultVO[] getFeedbacks(FeedbackCriteriaVO feedbackCriteriaVO)
	throws Exception {
		ArrayList list = new ArrayList();
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer("SELECT f.feedback_id,f.comment");
			sb.append(",c.cust_id,c.cust_name,c.location,c.email_id,c.mobile_no");
			sb.append(" FROM feedback_master f,customer c");
			sb.append(" where f.cust_id = c.cust_id");

			if (feedbackCriteriaVO.getCompId() != null) {
				sb.append(" AND f.comp_id = " + feedbackCriteriaVO.getCompId());
			}

			if (feedbackCriteriaVO.getBranchId() != null) {
				sb.append(" AND f.branch_id = "+ feedbackCriteriaVO.getBranchId());
			}
			if (feedbackCriteriaVO.getCustomerName() != null) {
				sb.append(" AND c.cust_name Like '%"+ feedbackCriteriaVO.getCustomerName() + "%'");
			}

			if (feedbackCriteriaVO.getLocation() != null) {
				sb.append(" AND c.location Like '%"+ feedbackCriteriaVO.getLocation() + "%'");
			}

			if (feedbackCriteriaVO.getFromDate() != null && feedbackCriteriaVO.getToDate() != null) {
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				c.setTime(feedbackCriteriaVO.getToDate());
				c.add(Calendar.DATE, 1);
				Date toDate = c.getTime();
				String fdate = ft.format(feedbackCriteriaVO.getFromDate());
				String tdate = ft.format(toDate);
				sb.append(" AND f.create_time BETWEEN '" + fdate + "' AND '"+ tdate + "' ");
			}

			ResultSet rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				FeedbackResultVO feedbackResultVO = new FeedbackResultVO();
				feedbackResultVO.setFeedbackId(new Integer(rs.getInt(1)));
				feedbackResultVO.setComment(rs.getString(2));
				feedbackResultVO.setCustId(new Integer(rs.getString(3)));
				feedbackResultVO.setCustName(rs.getString(4));
				feedbackResultVO.setLocation(rs.getString(5));
				feedbackResultVO.setEmailId(rs.getString(6));
				feedbackResultVO.setMobileNo(rs.getString(7));
				list.add(feedbackResultVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (FeedbackResultVO[]) list.toArray(new FeedbackResultVO[list
		                                                              .size()]);
	}

	public FeedbackResultVO getFeedback(Integer feedbackId) throws Exception {
		FeedbackResultVO feedbackResultVO = null;
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer("SELECT f.feedback_id,f.comment,f.create_time");
			sb.append(",c.cust_name,c.mobile_no,c.email_id,c.occupation,c.dob");
			sb.append(",cm.comp_name,b.branch_name");
			sb.append(" FROM feedback_master f,customer c,company cm,branch b");
			sb.append(" where f.cust_id = c.cust_id AND f.comp_id = cm.comp_id AND b.branch_id = f.branch_id AND");
			sb.append(" f.feedback_id = " + feedbackId);

			ResultSet rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				feedbackResultVO = new FeedbackResultVO();
				feedbackResultVO.setFeedbackId(new Integer(rs.getInt(1)));
				feedbackResultVO.setComment(rs.getString(2));
				feedbackResultVO.setCreateTime(rs.getDate(3));
				feedbackResultVO.setCustName(rs.getString(4));
				feedbackResultVO.setMobileNo(rs.getString(5));
				feedbackResultVO.setEmailId(rs.getString(6));
				feedbackResultVO.setOccupation(rs.getString(7));
				feedbackResultVO.setDob(rs.getDate(8));
				feedbackResultVO.setCompName(rs.getString(9));
				feedbackResultVO.setBranchName(rs.getString(10));
			}

			ArrayList list = new ArrayList();
			Statement stmt1 = session.connection().createStatement();
			StringBuffer sb1 = new StringBuffer("SELECT fq.feedback_id,fq.question_id,fq.question,fq.ans");
			sb1.append(" FROM feedback_question_ans fq");
			sb1.append(" where fq.feedback_id=" + feedbackId);

			ResultSet rs1 = stmt1.executeQuery(sb1.toString());
			while (rs1.next()) {
				FeedbackQuestionAnsVO feedbackQuestionAns = new FeedbackQuestionAnsVO();
				feedbackQuestionAns.setFeedbackId(new Integer(rs1.getInt(1)));
				feedbackQuestionAns.setQuestionId(new Integer(rs1.getInt(2)));
				feedbackQuestionAns.setQuestion(rs1.getString(3));
				feedbackQuestionAns.setAns(new Integer(rs1.getInt(4)));
				list.add(feedbackQuestionAns);
			}

			feedbackResultVO.setQuestionAns((FeedbackQuestionAnsVO[]) list
					.toArray(new FeedbackQuestionAnsVO[list.size()]));

		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return feedbackResultVO;
	}

	public DeviceVO[] getDevices(DeviceVO device) throws Exception {
		ArrayList list = new ArrayList();
		Session session = getSession();
		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer("SELECT d.id,d.device_id,d.status,d.create_time");
			sb.append(",c.comp_name,b.branch_name");
			sb.append(" FROM device d,company c,branch b");
			sb.append(" where c.comp_id = d.comp_id AND b.branch_id = d.branch_id");

			if (device.getCompId() != null) {
				sb.append(" AND d.comp_id = " + device.getCompId());
			}

			if (device.getBranchId() != null) {
				sb.append(" AND d.branch_id = " + device.getBranchId());
			}

			ResultSet rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				DeviceVO d = new DeviceVO();
				d.setId(new Integer(rs.getInt(1)));
				d.setDeviceId(rs.getString(2));
				d.setStatus(new Integer(rs.getInt(3)));
				d.setCreateTime(rs.getDate(4));
				d.setCompanyName(rs.getString(5));
				d.setBranchName(rs.getString(6));
				list.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (DeviceVO[]) list.toArray(new DeviceVO[list.size()]);
	}

	public int storeDevice(DeviceVO deviceVO) throws Exception {
		Session session = getSession();
		int flag = 0;
		try {
			DeviceVO found = null;
			session.beginTransaction();

			if (deviceVO.getId() == null) {
				found = (DeviceVO) session.createQuery("FROM DeviceVO d WHERE d.DeviceId=? AND d.Status=?")
				.setString(0, deviceVO.getDeviceId()).setInteger(1, 1).uniqueResult();
				if (found != null) {
					if (found.getCompId().toString().equals(deviceVO.getCompId().toString())&& found.getBranchId().toString().equals(deviceVO.getBranchId().toString())) {
						flag = 0;
					} else {
						flag = 1;
					}
				} else {
					deviceVO.setStatus(new Integer(1));
					deviceVO.setCreateTime(new Date());
					session.save(deviceVO);
				}
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return flag;
	}

	public int unregisterDevice(DeviceVO deviceVO) throws Exception {
		Session session = getSession();
		int flag = 0;
		try {
			DeviceVO found = null;
			session.beginTransaction();

			if (deviceVO.getId() == null) {
				found = (DeviceVO) session.createQuery("FROM DeviceVO d WHERE d.DeviceId=?").setString(0,
						deviceVO.getDeviceId()).uniqueResult();
				if (found != null) {
					found.setStatus(new Integer(2));
					session.update(found);
				} else {
					deviceVO.setCreateTime(new Date());
					flag = 1;
				}
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return flag;
	}

	public CustCampaignVO storeCustCampaign(CustCampaignVO custCampaignVO)
	throws Exception {
		Session session = getSession();

		try {
			session.beginTransaction();

			if (custCampaignVO.getCampId() != null&& custCampaignVO.getCustId() != null) {

				custCampaignVO.setCreateTime(new Date());
				session.save(custCampaignVO);
			}

			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return custCampaignVO;
	}

	public CustCampaignVO[] getCustomerCampaigns(CustCampaignVO custCampaignVO)
	throws Exception {
		ArrayList list = new ArrayList();
		Session session = getSession();

		try {
			Statement stmt = session.connection().createStatement();
			StringBuffer sb = new StringBuffer("SELECT cc.id");
			sb.append(",c.cust_name");
			sb.append(",cp.name");
			sb.append(",comp.comp_name");
			sb.append(",b.branch_name");
			sb.append(" FROM cust_campaign cc ,customer c,campaign cp,company comp,branch b");
			sb.append(" where cc.cust_id = c.cust_id AND cc.camp_id = cp.camp_id");
			sb.append(" AND cc.comp_id = comp.comp_id AND cc.branch_id = b.branch_id");

			if (custCampaignVO.getCompId() != null) {
				sb.append(" AND cc.comp_id = " + custCampaignVO.getCompId());
			}
			if (custCampaignVO.getBranchId() != null) {
				sb.append(" AND cc.branch_id = "+ custCampaignVO.getBranchId());
			}
			if (custCampaignVO.getCustName() != null) {
				sb.append(" AND c.cust_name Like '%"+ custCampaignVO.getCustName() + "%'");
			}
			if (custCampaignVO.getCampId() != null) {
				sb.append(" AND cc.camp_id = " + custCampaignVO.getCampId());
			}

			ResultSet rs = stmt.executeQuery(sb.toString());

			while (rs.next()) {
				CustCampaignVO custCampaign = new CustCampaignVO();
				custCampaign.setId(new Integer(rs.getInt(1)));
				custCampaign.setCustName(rs.getString(2));
				custCampaign.setCampName(rs.getString(3));
				custCampaign.setCompName(rs.getString(4));
				custCampaign.setBranchName(rs.getString(5));
				list.add(custCampaign);
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw e;
		} finally {
			close(session);
		}
		return (CustCampaignVO[]) list.toArray(new CustCampaignVO[list.size()]);
	}

	public CustomerVO storeCustomer(CustomerVO custVO) throws Exception {
		Session session = getSession();
		try {
			session.beginTransaction();
			if (custVO.getCustId() == null) {
				CustomerVO found = (CustomerVO) session.createQuery("FROM CustomerVO u WHERE u.MobileNo=? OR u.EmailId=? AND u.CompId=? AND u.BranchId=?")
						.setString(0, custVO.getMobileNo()).setString(1,custVO.getEmailId()).setInteger(2,custVO.getCompId().intValue()).setInteger(3,custVO.getBranchId().intValue()).uniqueResult();
				if (found != null) {
					custVO.setCustId(found.getCustId());
					custVO.setCreateTime(new Date());
					SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String dob = ft.format(custVO.getDob());
					String createTime = ft1.format(new Date());

					StringBuffer sb = new StringBuffer();
					sb.append("UPDATE customer SET");
					sb.append(" cust_name = '" + custVO.getCustName() + "'");
					sb.append(",mobile_no ='" + custVO.getMobileNo() + "' ");
					sb.append(",email_id = '" + custVO.getEmailId() + "'");
					sb.append(",dob = '" + dob + "'");
					sb.append(",age = '" + custVO.getAge() + "'");
					sb.append(",maritial_status = '"+ custVO.getMaritialStatus() + "'");
					sb.append(",occupation = '" + custVO.getOccupation() + "'");
					sb.append(",location = '" + custVO.getLocation() + "'");
					sb.append(",comp_id = '" + custVO.getCompId() + "'");
					sb.append(",branch_id = '" + custVO.getBranchId() + "'");
					sb.append(",create_time = '" + createTime + "'");
					sb.append(" WHERE cust_id=" + found.getCustId());
					Statement stmt = session.connection().createStatement();
					stmt.executeUpdate(sb.toString());

				} else {
					custVO.setCreateTime(new Date());
					session.save(custVO);
				}
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}

		return custVO;
	}

	public FeedbackVO storeCustomerFeedback(FeedbackVO feedbackVO)
	throws Exception {
		Session session = getSession();
		try {
			session.beginTransaction();
			feedbackVO.setCreateTime(new Date());
			session.save(feedbackVO);
			FeedbackQuestionAnsVO fqa[] = feedbackVO.getQuestionAns();
			if (fqa != null && fqa.length > 0) {
				for (int i = 0; i < fqa.length; i++) {
					fqa[i].setFeedbackId(feedbackVO.getFeedbackId());
					session.save(fqa[i]);
				}
			}
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
			session.getTransaction().rollback();
			throw ex;
		} finally {
			close(session);
		}
		return feedbackVO;
	}
}
