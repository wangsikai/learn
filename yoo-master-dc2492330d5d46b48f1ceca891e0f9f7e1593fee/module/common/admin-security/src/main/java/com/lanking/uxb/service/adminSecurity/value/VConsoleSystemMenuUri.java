package com.lanking.uxb.service.adminSecurity.value;

/**
 * 支撑系统对应菜单地址代码VO
 * 
 * @author wangsenhao
 *
 */
public class VConsoleSystemMenuUri {

	private Long id;

	/**
	 * 支撑系统的ID
	 */
	private long systemId;

	/**
	 * 菜单URI
	 */
	private String menuUri;

	/**
	 * uri描述
	 */
	private String description;
	/**
	 * 系统名称
	 */
	private String systemName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSystemId() {
		return systemId;
	}

	public void setSystemId(long systemId) {
		this.systemId = systemId;
	}

	public String getMenuUri() {
		return menuUri;
	}

	public void setMenuUri(String menuUri) {
		this.menuUri = menuUri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

}
