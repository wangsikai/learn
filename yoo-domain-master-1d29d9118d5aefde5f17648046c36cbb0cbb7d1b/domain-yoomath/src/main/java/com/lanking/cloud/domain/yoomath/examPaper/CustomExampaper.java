package com.lanking.cloud.domain.yoomath.examPaper;

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
 * 组卷
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "custom_exampaper")
public class CustomExampaper implements Serializable {

	private static final long serialVersionUID = 1230022711971153455L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 类型
	 */
	@Column(name = "type", precision = 3)
	private CustomExampaperType type;

	/**
	 * 试卷考试时间（分钟）
	 */
	@Column(name = "time0", precision = 5)
	private Integer time;

	/**
	 * 试卷总分
	 */
	@Column(name = "score", precision = 5)
	private Integer score;

	/**
	 * 题目数量
	 */
	@Column(name = "question_count", precision = 3)
	private Integer questionCount;

	/**
	 * 难度
	 */
	@Column(name = "difficulty", scale = 2)
	private BigDecimal difficulty;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 版本代码
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材代码
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private CustomExampaperStatus status;

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
	 * 转为正式时间
	 */
	@Column(name = "enable_at", columnDefinition = "datetime(3)")
	private Date enableAt;

	/**
	 * 开卷时间
	 */
	@Column(name = "open_at", columnDefinition = "datetime(3)")
	private Date openAt;

	/**
	 * 删除时间
	 */
	@Column(name = "delete_at", columnDefinition = "datetime(3)")
	private Date deleteAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 是否已下载
	 */
	@Column(name = "download")
	private Boolean download = false;

	/**
	 * 是否转换题型信息
	 */
	@Transient
	private boolean isShowTopic = false;
	/**
	 * 是否转换班级信息.
	 */
	@Transient
	private boolean isClazz = false;
	/**
	 * 是否转换习题信息.
	 */
	@Transient
	private boolean isShowQuestions = false;

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

	public CustomExampaperType getType() {
		return type;
	}

	public void setType(CustomExampaperType type) {
		this.type = type;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(Integer questionCount) {
		this.questionCount = questionCount;
	}

	public BigDecimal getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(BigDecimal difficulty) {
		this.difficulty = difficulty;
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

	public CustomExampaperStatus getStatus() {
		return status;
	}

	public void setStatus(CustomExampaperStatus status) {
		this.status = status;
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

	public Date getEnableAt() {
		return enableAt;
	}

	public void setEnableAt(Date enableAt) {
		this.enableAt = enableAt;
	}

	public Date getOpenAt() {
		return openAt;
	}

	public void setOpenAt(Date openAt) {
		this.openAt = openAt;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Boolean getDownload() {
		return download;
	}

	public void setDownload(Boolean download) {
		this.download = download;
	}

	public boolean isShowTopic() {
		return isShowTopic;
	}

	public void setShowTopic(boolean isShowTopic) {
		this.isShowTopic = isShowTopic;
	}

	public boolean isClazz() {
		return isClazz;
	}

	public void setClazz(boolean isClazz) {
		this.isClazz = isClazz;
	}

	public boolean isShowQuestions() {
		return isShowQuestions;
	}

	public void setShowQuestions(boolean isShowQuestions) {
		this.isShowQuestions = isShowQuestions;
	}

}
