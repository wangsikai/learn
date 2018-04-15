package com.lanking.cloud.domain.yoomath.fallible;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教师错题
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "teacher_fallible_question")
public class TeacherFallibleQuestion implements Serializable {

	private static final long serialVersionUID = 486978981967041273L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 教师用户ID
	 */
	@Column(name = "teacher_id")
	private long teacherId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private long questionId;

	/**
	 * 练习次数
	 */
	@Column(name = "do_num")
	private int doNum;

	/**
	 * 正确次数
	 */
	@Column(name = "right_num")
	private int rightNum;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 题目基本类型
	 */
	@Column(name = "type", precision = 3)
	private Type type;

	/**
	 * 题目学科具体类型
	 */
	@Column(name = "type_code")
	private Integer typeCode;

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private Double difficulty;

	/**
	 * 教材代码
	 */
	@Deprecated
	@Column(name = "textbook_code")
	private Integer textBookCode;

	/**
	 * 章节代码
	 */
	@Deprecated
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public int getDoNum() {
		return doNum;
	}

	public void setDoNum(int doNum) {
		this.doNum = doNum;
	}

	public int getRightNum() {
		return rightNum;
	}

	public void setRightNum(int rightNum) {
		this.rightNum = rightNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public Integer getTextBookCode() {
		return textBookCode;
	}

	public void setTextBookCode(Integer textBookCode) {
		this.textBookCode = textBookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
