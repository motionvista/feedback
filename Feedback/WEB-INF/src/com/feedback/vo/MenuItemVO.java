package com.feedback.vo;

/**
 * @author User
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class MenuItemVO {
	public String id;

	public String name;

	public String url;

	public MenuItemVO(String id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url + "&MenuId=" + id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
