/*
 * Created on Jun 5, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.feedback.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author User
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RoleVO extends BaseVO {

	public static final RoleVO OWNER = new RoleVO(new Integer(1), "Owner");

	public static final RoleVO ADMIN = new RoleVO(new Integer(2), "Admin");

	public static final RoleVO HOC = new RoleVO(new Integer(3), "HOC");

	public static final RoleVO BRANCH_MANAGER = new RoleVO(new Integer(4),
	"Branch Manager");

	public static final RoleVO MANAGER = new RoleVO(new Integer(5), "Manager");

	public Integer id;

	public String name;

	public String description;

	public Date createTime;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RoleVO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public RoleVO() {
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static RoleVO[] getRoles() {
		List list = new ArrayList();
		list.add(OWNER);
		list.add(ADMIN);
		list.add(HOC);
		list.add(BRANCH_MANAGER);
		list.add(MANAGER);

		return (RoleVO[]) list.toArray(new RoleVO[list.size()]);
	}

	public static String getRoleName(Integer roleId) {
		String roleName = "";
		if (roleId.equals(ADMIN.getId())) {
			roleName = ADMIN.getName();
		} else if (roleId.equals(OWNER.getId())) {
			roleName = OWNER.getName();
		} else if (roleId.equals(MANAGER.getId())) {
			roleName = MANAGER.getName();
		} else if (roleId.equals(HOC.getId())) {
			roleName = HOC.getName();
		} else if (roleId.equals(BRANCH_MANAGER.getId())) {
			roleName = BRANCH_MANAGER.getName();
		}
		return roleName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
