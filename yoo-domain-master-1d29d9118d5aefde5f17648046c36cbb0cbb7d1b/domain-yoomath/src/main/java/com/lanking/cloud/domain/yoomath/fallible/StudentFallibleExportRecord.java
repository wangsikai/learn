package com.lanking.cloud.domain.yoomath.fallible;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 学生导出错题记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_fallible_export_record")
public class StudentFallibleExportRecord implements Serializable {
	private static final long serialVersionUID = 2279438989855034443L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生用户ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 文件hash
	 */
	@Column(name = "hash")
	private Integer hash;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 50)
	private String name;

	/**
	 * 文件大小KB
	 */
	@Column(name = "size")
	private Double size;

	/**
	 * 类型
	 */
	@Column(name = "extend", length = 10)
	private String extend;

	/**
	 * 文档创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date creatAt;

	/**
	 * 题目数
	 */
	@Column(name = "count")
	private Integer count;

	/**
	 * 金币售价
	 */
	@Column(name = "sell_price")
	private Integer sellPrice;

	/**
	 * 纸币售价
	 */
	@Column(name = "sell_price_rmb", scale = 2)
	private BigDecimal sellPriceRMB;

	/**
	 * 章节集合
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 1000, name = "item_results")
	private List<Long> sectionCodes = Lists.newArrayList();

	/**
	 * 是否已购买兑换
	 */
	@Column(name = "buy")
	private Boolean buy = false;

	/**
	 * 数据状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	/**
	 * 除章节之外的其他查询参数集合
	 */
	@Column(name = "attach_data", length = 512)
	private String attachData;

	/**
	 * 关联的待打印订单ID
	 */
	@Column(name = "fallible_question_print_order_id")
	private Long fallibleQuestionPrintOrderId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;

	}

	public Integer getHash() {
		return hash;
	}

	public void setHash(Integer hash) {
		this.hash = hash;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public Date getCreatAt() {
		return creatAt;
	}

	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public Boolean getBuy() {
		return buy;
	}

	public void setBuy(Boolean buy) {
		this.buy = buy;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getAttachData() {
		return attachData;
	}

	public void setAttachData(String attachData) {
		this.attachData = attachData;
	}

	public Long getFallibleQuestionPrintOrderId() {
		return fallibleQuestionPrintOrderId;
	}

	public void setFallibleQuestionPrintOrderId(Long fallibleQuestionPrintOrderId) {
		this.fallibleQuestionPrintOrderId = fallibleQuestionPrintOrderId;
	}

	public BigDecimal getSellPriceRMB() {
		return sellPriceRMB;
	}

	public void setSellPriceRMB(BigDecimal sellPriceRMB) {
		this.sellPriceRMB = sellPriceRMB;
	}
}
