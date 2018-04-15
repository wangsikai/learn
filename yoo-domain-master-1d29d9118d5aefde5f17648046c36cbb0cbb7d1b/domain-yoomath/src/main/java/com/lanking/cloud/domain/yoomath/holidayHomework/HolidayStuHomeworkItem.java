package com.lanking.cloud.domain.yoomath.holidayHomework;

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
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 学生假日作业项
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "holiday_stu_homework_item")
public class HolidayStuHomeworkItem implements Serializable {

	private static final long serialVersionUID = 2985677596115006739L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 假日作业类型
	 */
	@Column(name = "type", precision = 3)
	private HolidayHomeworkType type;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 假日作业ID {@link HolidayHomework}.id
	 */
	@Column(name = "holiday_homework_id")
	private Long holidayHomeworkId;

	/**
	 * 假日作业项ID {@link HolidayHomeworkItem}.id
	 */
	@Column(name = "holiday_homework_item_id")
	private Long holidayHomeworkItemId;

	/**
	 * 学生假日作业ID {@link HolidayStuHomework}.id
	 */
	@Column(name = "holiday_stu_homework_id")
	private Long holidayStuHomeworkId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 学生提交时间
	 */
	@Column(name = "submit_at", columnDefinition = "datetime(3)")
	private Date submitAt;

	/**
	 * 学生作业状态
	 */
	@Column(name = "status", precision = 3)
	private StudentHomeworkStatus status;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3)
	private Status delStatus = Status.ENABLED;

	/**
	 * 对题数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 错题数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 完成率
	 */
	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

	/**
	 * 排名
	 */
	@Column(name = "rank")
	private Integer rank;

	/**
	 * 作业用时
	 */
	@Column(name = "homework_time")
	private Integer homeworkTime = 0;

	/**
	 * 更新时间,为了对比做题答案数据(客户端自动保存答案用到)
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * convert 转换变量，是否设置用户信息（兼容设置，默认true）.
	 */
	@Transient
	private boolean initUser = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HolidayHomeworkType getType() {
		return type;
	}

	public void setType(HolidayHomeworkType type) {
		this.type = type;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getHolidayHomeworkId() {
		return holidayHomeworkId;
	}

	public void setHolidayHomeworkId(Long holidayHomeworkId) {
		this.holidayHomeworkId = holidayHomeworkId;
	}

	public Long getHolidayHomeworkItemId() {
		return holidayHomeworkItemId;
	}

	public void setHolidayHomeworkItemId(Long holidayHomeworkItemId) {
		this.holidayHomeworkItemId = holidayHomeworkItemId;
	}

	public Long getHolidayStuHomeworkId() {
		return holidayStuHomeworkId;
	}

	public void setHolidayStuHomeworkId(Long holidayStuHomeworkId) {
		this.holidayStuHomeworkId = holidayStuHomeworkId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getSubmitAt() {
		return submitAt;
	}

	public void setSubmitAt(Date submitAt) {
		this.submitAt = submitAt;
	}

	public StudentHomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(StudentHomeworkStatus status) {
		this.status = status;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public Integer getRightCount() {
		return rightCount;
	}

	public void setRightCount(Integer rightCount) {
		this.rightCount = rightCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public boolean isInitUser() {
		return initUser;
	}

	public void setInitUser(boolean initUser) {
		this.initUser = initUser;
	}

}
