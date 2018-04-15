package com.lanking.cloud.domain.yoomath.diagnostic;

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

/**
 * 诊断-学生-学生在某个班级内知识点维度的答题统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "diagno_stu_class_kp")
public class DiagnosticStudentClassKnowpoint implements Serializable {

	private static final long serialVersionUID = -3246311362025595534L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private long id;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private long studentId;

	/**
	 * 知识点代码
	 */
	@Column(name = "knowpoint_code")
	private long knowpointCode;

	/**
	 * 最小难度
	 */
	@Column(name = "min_difficulty")
	private BigDecimal minDifficulty;

	/**
	 * 最大难度
	 */
	@Column(name = "max_difficulty")
	private BigDecimal maxDifficulty;

	/**
	 * 基础题数量
	 */
	@Column(name = "do_hard1_count", precision = 7)
	private int doHard1Count;

	/**
	 * 基础题答对数量
	 */
	@Column(name = "right_hard1_count", precision = 7)
	private int rightHard1Count;

	/**
	 * 提高题数量
	 */
	@Column(name = "do_hard2_count", precision = 7)
	private int doHard2Count;

	/**
	 * 提高题答对数量
	 */
	@Column(name = "right_hard2_count", precision = 7)
	private int rightHard2Count;

	/**
	 * 冲刺题数量
	 */
	@Column(name = "do_hard3_count", precision = 7)
	private int doHard3Count;

	/**
	 * 冲刺题答对数量
	 */
	@Column(name = "right_hard3_count", precision = 7)
	private int rightHard3Count;

	/**
	 * 做题数量
	 */
	@Column(name = "do_count", precision = 7)
	private int doCount;

	/**
	 * 做对数量
	 */
	@Column(name = "right_count", precision = 7)
	private int rightCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public BigDecimal getMinDifficulty() {
		return minDifficulty;
	}

	public void setMinDifficulty(BigDecimal minDifficulty) {
		this.minDifficulty = minDifficulty;
	}

	public BigDecimal getMaxDifficulty() {
		return maxDifficulty;
	}

	public void setMaxDifficulty(BigDecimal maxDifficulty) {
		this.maxDifficulty = maxDifficulty;
	}

	public int getDoHard1Count() {
		return doHard1Count;
	}

	public void setDoHard1Count(int doHard1Count) {
		this.doHard1Count = doHard1Count;
	}

	public int getRightHard1Count() {
		return rightHard1Count;
	}

	public void setRightHard1Count(int rightHard1Count) {
		this.rightHard1Count = rightHard1Count;
	}

	public int getDoHard2Count() {
		return doHard2Count;
	}

	public void setDoHard2Count(int doHard2Count) {
		this.doHard2Count = doHard2Count;
	}

	public int getRightHard2Count() {
		return rightHard2Count;
	}

	public void setRightHard2Count(int rightHard2Count) {
		this.rightHard2Count = rightHard2Count;
	}

	public int getDoHard3Count() {
		return doHard3Count;
	}

	public void setDoHard3Count(int doHard3Count) {
		this.doHard3Count = doHard3Count;
	}

	public int getRightHard3Count() {
		return rightHard3Count;
	}

	public void setRightHard3Count(int rightHard3Count) {
		this.rightHard3Count = rightHard3Count;
	}

	public int getDoCount() {
		return doCount;
	}

	public void setDoCount(int doCount) {
		this.doCount = doCount;
	}

	public int getRightCount() {
		return rightCount;
	}

	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
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

}
