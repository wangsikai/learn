package com.lanking.cloud.domain.support.common.auth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 支撑系统权限-菜单资源代码表(方便码农SQL初始化新增的菜单数据)
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "con_system_menu_uri")
public class ConsoleSystemMenuUri implements Serializable {

	private static final long serialVersionUID = 4687303136399304984L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 支撑系统的ID
	 */
	@Column(name = "system_id")
	private long systemId;

	/**
	 * 菜单URI
	 */
	@Column(name = "menu_uri", length = 256)
	private String menuUri;

	/**
	 * uri描述
	 */
	@Column(name = "description", length = 128)
	private String description;

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

}
