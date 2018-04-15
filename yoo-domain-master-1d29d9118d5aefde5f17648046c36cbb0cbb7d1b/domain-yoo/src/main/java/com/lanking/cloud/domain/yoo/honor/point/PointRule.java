package com.lanking.cloud.domain.yoo.honor.point;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 积分规则
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "point_rule")
public class PointRule implements Serializable {

	private static final long serialVersionUID = -7588164171277341319L;

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
	private PointAction action;

	/**
	 * 对应积分值（可正可负）
	 */
	@Column(name = "point", precision = 3)
	private int point;

	/**
	 * 描述
	 */
	@Column(name = "description", length = 500)
	private String description;

	/**
	 * 规则具体内容，格式可以自定义，在具体规则实现中解析
	 */
	@Column(name = "rule", length = 500)
	private String rule;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public PointAction getAction() {
		return action;
	}

	public void setAction(PointAction action) {
		this.action = action;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
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
