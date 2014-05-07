package com.feedback.vo;

import java.util.ArrayList;
import java.util.List;

import com.feedback.vo.TypeVO;

public class TypeVO extends BaseVO {
	// status
	public static final TypeVO ACTIVE = new TypeVO(new Integer(1), "Active");

	public static final TypeVO INACTIVE = new TypeVO(new Integer(2), "InActive");

	// question type
	public static final TypeVO OBJECTIVE = new TypeVO(new Integer(1),
	"Objective");

	// public static final TypeVO SUBJECTIVE = new TypeVO(new
	// Integer(2),"Subjective");

	// campaign type
	public static final TypeVO BOTH = new TypeVO(new Integer(1), "Both");

	public static final TypeVO SMS = new TypeVO(new Integer(2), "Sms");

	public static final TypeVO EMAIL = new TypeVO(new Integer(3), "Email");

	// Device Status
	public static final TypeVO REGISTER = new TypeVO(new Integer(1), "Register");

	public static final TypeVO UNREGISTER = new TypeVO(new Integer(2),
	"UnRegister");

	public Integer id;

	public String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TypeVO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static TypeVO[] getStatus() {
		List list = new ArrayList();
		list.add(ACTIVE);
		list.add(INACTIVE);
		return (TypeVO[]) list.toArray(new TypeVO[list.size()]);
	}

	public static TypeVO[] getQueType() {
		List list = new ArrayList();
		list.add(OBJECTIVE);
		// list.add(SUBJECTIVE);
		return (TypeVO[]) list.toArray(new TypeVO[list.size()]);
	}

	public static TypeVO[] getCampaignType() {
		List list = new ArrayList();
		list.add(BOTH);
		list.add(SMS);
		list.add(EMAIL);
		return (TypeVO[]) list.toArray(new TypeVO[list.size()]);
	}

	public static TypeVO[] getDeviceStatus() {
		List list = new ArrayList();
		list.add(REGISTER);
		list.add(UNREGISTER);
		return (TypeVO[]) list.toArray(new TypeVO[list.size()]);
	}

}
