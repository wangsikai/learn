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
 * 学生假日作业
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "holiday_stu_homework")
public class HolidayStuHomework implements Serializable {

	private static final long serialVersionUID = -3860583366335747029L;

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
	 * 学生用户ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 假日作业ID {@link HolidayHomework}.id
	 */
	@Column(name = "holiday_homework_id")
	private Long holidayHomeworkId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

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
	 * 已提交专项
	 */
	@Column(name = "commit_item_count")
	private Integer commitItemCount;

	/**
	 * 学生假期作业中专项总数
	 */
	@Column(name = "all_item_count")
	private Integer allItemCount;

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
	 * 作业平均用时
	 */
	@Column(name = "homework_time")
	private Integer homeworkTime = 0;

	/**
	 * 假期作业已经全部完成是否查看过 true -> 已经查看过 false -> 未查看
	 */
	@Column(name = "viewed")
	private Boolean viewed = false;

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

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public Integer getCommitItemCount() {
		return commitItemCount;
	}

	public void setCommitItemCount(Integer commitItemCount) {
		this.commitItemCount = commitItemCount;
	}

	public Integer getAllItemCount() {
		return allItemCount;
	}

	public void setAllItemCount(Integer allItemCount) {
		this.allItemCount = allItemCount;
	}

	public Boolean isViewed() {
		return viewed;
	}

	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}

	public boolean isInitUser() {
		return initUser;
	}

	public void setInitUser(boolean initUser) {
		this.initUser = initUser;
	}

}
