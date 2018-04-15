package com.lanking.cloud.domain.yoo.honor.growth;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成长值规则
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "growth_rule")
public class GrowthRule implements Serializable {

	private static final long serialVersionUID = -4200192450753039761L;

	/**
	 * 规则代码
	 */
	@Id
	@Column(name = "code", precision = 5)
	private int code;

	/**
	 * 规则关联动作
	 */
	@Column(name = "action", precision = 3)
	private GrowthAction action;

	/**
	 * 对应成长值（可正可负）
	 */
	@Column(name = "growth_value", precision = 3)
	private int growthValue;

	/**
	 * 规则具体内容，格式可以自定义，在具体规则实现中解析
	 */
	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "rule", length = 500)
	private String rule;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public GrowthAction getAction() {
		return action;
	}

	public void setAction(GrowthAction action) {
		this.action = action;
	}

	public int getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(int growthValue) {
		this.growthValue = growthValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

}
