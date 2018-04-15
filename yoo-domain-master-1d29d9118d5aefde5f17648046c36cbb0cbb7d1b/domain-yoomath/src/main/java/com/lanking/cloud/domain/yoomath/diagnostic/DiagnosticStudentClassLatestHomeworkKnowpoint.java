package com.lanking.cloud.domain.yoomath.diagnostic;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 诊断-学生-学生在某个班级内最近n次作业的关联知识点统计
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "diagno_stu_class_latest_hk_kp")
public class DiagnosticStudentClassLatestHomeworkKnowpoint implements Serializable {

	private static final long serialVersionUID = -6363192561520822377L;

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
	 * 做题数量
	 */
	@Column(name = "do_count", precision = 7)
	private int doCount;

	/**
	 * 答对数量
	 */
	@Column(name = "right_count", precision = 7)
	private int rightCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 最近n次
	 */
	@Column(name = "times", precision = 3, columnDefinition = "tinyint default 30")
	private int times = 30;

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

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

}
