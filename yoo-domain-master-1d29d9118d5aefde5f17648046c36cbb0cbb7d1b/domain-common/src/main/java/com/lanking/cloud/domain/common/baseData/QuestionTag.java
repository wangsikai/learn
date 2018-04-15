package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.common.resource.question.QuestionCategoryType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 题目标签<br>
 * 原来枚举{@link QuestionCategoryType}转换成表
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年7月18日
 */
@Entity
@Table(name = "question_tag")
public class QuestionTag implements Serializable {

	private static final long serialVersionUID = -1976630007246867154L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long code;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 40)
	private String name;

	/**
	 * 简称
	 */
	@Column(name = "short_name", length = 10)
	private String shortName;

	/**
	 * 类型
	 */
	@Column(name = "type", precision = 3)
	private QuestionTagType type;

	/**
	 * 图标
	 */
	@Column(name = "icon")
	private Long icon;

	/**
	 * 相关配置(如果是阀值,名称体现出最大还是最小)
	 * 
	 * <pre>
	 * keys:
	 * minPulishCount:布置次数
	 * minCollectCount:收藏次数
	 * maxRightRate:最高的正确率(如:50%,对应值存50)
	 * </pre>
	 */
	@org.hibernate.annotations.Type(type = JSONType.TYPE)
	@Column(name = "cfg", length = 60000)
	private Map<String, Object> cfg;

	/**
	 * 序号
	 */
	@Column(name = "sequence", columnDefinition = "bigint default 0")
	private Integer sequence;

	/**
	 * 状态
	 * 
	 * <pre>
	 * Status.ENABLED:开启
	 * Status.DISABLED:隐藏、禁用
	 * Status.DELETED:删除
	 * </pre>
	 */
	@Column(precision = 3, columnDefinition = "tinyint default 0")
	private Status status = Status.ENABLED;

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

	@SuppressWarnings("deprecation")
	public static long getTagCode(QuestionCategoryType questionCategoryType) {
		if (questionCategoryType == QuestionCategoryType.EXAMPLE) {
			// 例题
			return 4;
		} else if (questionCategoryType == QuestionCategoryType.TYPICAL) {
			// 典型题
			return 5;
		} else if (questionCategoryType == QuestionCategoryType.SIMULATION) {
			// 模拟题
			return 6;
		} else if (questionCategoryType == QuestionCategoryType.TRUTH) {
			// 真题
			return 7;
		} else if (questionCategoryType == QuestionCategoryType.FINALE) {
			// 压轴题
			return 8;
		}
		return 0;
	}
}
