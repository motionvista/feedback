/*
 * Created on Jun 5, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.feedback.vo;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Branch;

/**
 * @author User
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class PermissionVO extends BaseVO {

	// Master Permissions.
	public static final PermissionVO MANAGE_MASTER = new PermissionVO(
			new Integer(100), "Manage Master");

	public static final PermissionVO COMPANY = new PermissionVO(
			new Integer(101), "Company");

	public static final PermissionVO BRANCH = new PermissionVO(
			new Integer(102), "Branch");

	public static final PermissionVO USER = new PermissionVO(new Integer(103),
	"User");

	public static final PermissionVO CAMPAIGN = new PermissionVO(new Integer(
			104), "Campaign");

	public static final PermissionVO APPROVE_CAMPAIGN = new PermissionVO(
			new Integer(113), "Approve Campaign");

	public static final PermissionVO FORM = new PermissionVO(new Integer(105),
	"Form");

	public static final PermissionVO QUESTION = new PermissionVO(new Integer(
			106), "Question");

	public static final PermissionVO CITY = new PermissionVO(new Integer(107),
	"City");

	public static final PermissionVO STATE = new PermissionVO(new Integer(108),
	"State");

	public static final PermissionVO COUNTRY = new PermissionVO(
			new Integer(109), "Country");

	public static final PermissionVO SET_PERMISSION = new PermissionVO(
			new Integer(110), "Set Permission");

	public static final PermissionVO CHANGE_PASSWORD = new PermissionVO(
			new Integer(111), "Change Password");

	public static final PermissionVO DEVICE = new PermissionVO(
			new Integer(112), "Device");

	public static final PermissionVO EMAIL_TEMPLATE = new PermissionVO(
			new Integer(113), "Email Template");

	public static final PermissionVO SMS_TEMPLATE = new PermissionVO(
			new Integer(114), "Sms Template");

	// Feedback reports.
	public static final PermissionVO FEEDBACK_REPORTS = new PermissionVO(
			new Integer(200), "Feedback Reports");

	public static final PermissionVO SEARCH_FEEDBACK_REPORTS = new PermissionVO(
			new Integer(201), "Search Feedback Reports");

	// Customer
	public static final PermissionVO CUSTOMER = new PermissionVO(new Integer(
			300), "Customer");

	public static final PermissionVO SEARCH_CUSTOMER = new PermissionVO(
			new Integer(301), "Search Customer");

	public static final PermissionVO CREATE_CUSTOMER = new PermissionVO(
			new Integer(302), "Create Customer");

	public static final PermissionVO DELETE_CUSTOMER = new PermissionVO(
			new Integer(303), "Delete Customer");

	public static final PermissionVO GIVE_CAMPAIGN = new PermissionVO(
			new Integer(304), "Give Campaign");

	public static final PermissionVO CUSTOMER_CAMPAIGN = new PermissionVO(
			new Integer(305), "Customer Campaign");

	// Analytics
	public static final PermissionVO ANALYTICS = new PermissionVO(new Integer(
			400), "Analytics");

	private Integer id;

	private String name;

	public PermissionVO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	public static PermissionVO[] getPermissions() {
		List list = new ArrayList();
		// Permission for Master.
		list.add(COMPANY);
		// list.add(MANAGE_MASTER);
		list.add(BRANCH);
		list.add(USER);
		list.add(EMAIL_TEMPLATE);
		list.add(SMS_TEMPLATE);
		list.add(CAMPAIGN);
		list.add(APPROVE_CAMPAIGN);
		list.add(FORM);
		list.add(QUESTION);
		list.add(CITY);
		list.add(STATE);
		list.add(COUNTRY);
		list.add(SET_PERMISSION);
		list.add(CHANGE_PASSWORD);
		list.add(DEVICE);

		// Permission for Feedback Reports.
		list.add(FEEDBACK_REPORTS);
		list.add(SEARCH_FEEDBACK_REPORTS);
		// Permission for Customer.
		list.add(CUSTOMER);
		list.add(SEARCH_CUSTOMER);
		list.add(CREATE_CUSTOMER);
		list.add(DELETE_CUSTOMER);
		list.add(GIVE_CAMPAIGN);
		list.add(CUSTOMER_CAMPAIGN);
		//permission for analytics.
		list.add(ANALYTICS);
		return (PermissionVO[]) list.toArray(new PermissionVO[list.size()]);

	}

}
