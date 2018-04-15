package com.lanking.uxb.rescon.question.value;

import java.io.Serializable;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 习题标签.
 * 
 * @author wlche
 *
 */
public class VQuestionTag implements Serializable {
	private static final long serialVersionUID = 4153079976724704601L;

	private long code;
	private String name;
	private String shortName;
	private QuestionTagType type;
	private Long icon;
	private Map<String, Object> cfg;
	private Integer sequence;
	private Status status;

	/**
	 * 图标地址.
	 */
	private String url;

	/**
	 * 是否为系统自动标注，系统标注的标签，无法去除
	 */
	private Boolean system = false;

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public QuestionTagType getType() {
		return type;
	}

	public void setType(QuestionTagType type) {
		this.type = type;
	}

	public Long getIcon() {
		return icon;
	}

	public void setIcon(Long icon) {
		this.icon = icon;
	}

	public Map<String, Object> getCfg() {
		return cfg;
	}

	public void setCfg(Map<String, Object> cfg) {
		this.cfg = cfg;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

}
