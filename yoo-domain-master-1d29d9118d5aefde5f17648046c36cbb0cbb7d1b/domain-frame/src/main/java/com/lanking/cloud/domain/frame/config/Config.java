package com.lanking.cloud.domain.frame.config;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 配置
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "config")
public class Config implements Serializable {

	private static final long serialVersionUID = -115621998684492685L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * key
	 */
	@Column(name = "key0", length = 128)
	private String key;

	/**
	 * value
	 */
	@Column(name = "value", length = 4000)
	private String value;

	/**
	 * 备注
	 */
	@Column(name = "note", length = 128)
	private String note;

	/**
	 * 实时生效属性
	 */
	@Column(name = "real_time")
	private Boolean realTime;

	@Transient
	private boolean syncZookeeper = false;
	@Transient
	private String zookeeperValue;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getRealTime() {
		return realTime;
	}

	public void setRealTime(Boolean realTime) {
		this.realTime = realTime;
	}

	public boolean isSyncZookeeper() {
		return syncZookeeper;
	}

	public void setSyncZookeeper(boolean syncZookeeper) {
		this.syncZookeeper = syncZookeeper;
	}

	public String getZookeeperValue() {
		return zookeeperValue;
	}

	public void setZookeeperValue(String zookeeperValue) {
		this.zookeeperValue = zookeeperValue;
	}

}
