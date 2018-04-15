package com.lanking.cloud.domain.common.resource.examPaper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 试卷
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "exam_paper")
public class ExamPaper implements Serializable {
	private static final long serialVersionUID = -1237730664117852566L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 试卷名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 试卷总分
	 */
	@Column(name = "score")
	private Integer score;

	/**
	 * 阶段
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 科目
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 试卷类型CODE
	 */
	@Column(name = "resource_category_code")
	private Integer resourceCategoryCode;

	/**
	 * 教材版本
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 章节
	 */
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 地区
	 */
	@Column(name = "district_code")
	private Long districtCode;

	/**
	 * 年份
	 */
	@Column(name = "year")
	private Integer year;

	/**
	 * 学校
	 */
	@Column(name = "school_id")
	private Long schoolId;

	/**
	 * 校本学校（试卷直接使用者）
	 */
	@Column(name = "own_school_id")
	private Long ownSchoolId;

	/**
	 * 难度（试卷发布后将试卷所包含题目的平均知识点set到此字段）
	 */
	@Column(name = "difficulty")
	private BigDecimal difficulty;

	/**
	 * 试卷状态
	 * 
	 * @see ExamPaperStatus
	 */
	@Column(precision = 3, nullable = false)
	private ExamPaperStatus status = ExamPaperStatus.EDITING;

	/**
	 * 供应商ID
	 */
	@Column(name = "vendor_id")
	private Long vendorId;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	@Transient
	private boolean initQuestions = false;
	@Transient
	private boolean initUpdators = false;
	@Transient
	private boolean initRollBackers = false;
	/**
	 * 是否显示此试卷下面所有校验题目数量
	 */
	@Transient
	private boolean initQuestionStatusCount = false;
	/**
	 * 是否显示该试卷商品信息
	 */
	@Transient
	private boolean initGoodsInfo = false;
	/**
	 * 是否初始化收藏信息
	 */
	@Transient
	private boolean initCollect = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getResourceCategoryCode() {
		return resourceCategoryCode;
	}

	public void setResourceCategoryCode(Integer resourceCategoryCode) {
		this.resourceCategoryCode = resourceCategoryCode;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Long getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(Long districtCode) {
		this.districtCode = districtCode;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getOwnSchoolId() {
		return ownSchoolId;
	}

	public void setOwnSchoolId(Long ownSchoolId) {
		this.ownSchoolId = ownSchoolId;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
	}

	public ExamPaperStatus getStatus() {
		return status;
	}

	public void setStatus(ExamPaperStatus status) {
		this.status = status;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public boolean isInitQuestions() {
		return initQuestions;
	}

	public void setInitQuestions(boolean initQuestions) {
		this.initQuestions = initQuestions;
	}

	public boolean isInitUpdators() {
		return initUpdators;
	}

	public void setInitUpdators(boolean initUpdators) {
		this.initUpdators = initUpdators;
	}

	public boolean isInitRollBackers() {
		return initRollBackers;
	}

	public void setInitRollBackers(boolean initRollBackers) {
		this.initRollBackers = initRollBackers;
	}

	public boolean isInitQuestionStatusCount() {
		return initQuestionStatusCount;
	}

	public void setInitQuestionStatusCount(boolean initQuestionStatusCount) {
		this.initQuestionStatusCount = initQuestionStatusCount;
	}

	public boolean isInitGoodsInfo() {
		return initGoodsInfo;
	}

	public void setInitGoodsInfo(boolean initGoodsInfo) {
		this.initGoodsInfo = initGoodsInfo;
	}

	public boolean isInitCollect() {
		return initCollect;
	}

	public void setInitCollect(boolean initCollect) {
		this.initCollect = initCollect;
	}

}
