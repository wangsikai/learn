package com.lanking.cloud.domain.yoomath.homework;

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
 * 学生作业
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_homework")
public class StudentHomework implements Serializable {

	private static final long serialVersionUID = -7902700562385339930L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 作业ID
	 */
	@Column(name = "homework_id")
	private Long homeworkId;

	/**
	 * 正确率
	 */
	@Column(name = "right_rate", scale = 2)
	private BigDecimal rightRate;

	/**
	 * 正确率（订正后的正确率）
	 */
	@Column(name = "right_rate_correct", scale = 2)
	private BigDecimal rightRateCorrect;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 提交时间
	 * 
	 * <pre>
	 * submit_at 学生主动提交或者自动提交时有值
	 * stu_submit_at学生主动提交或者自动提交时学生作业有答案时有值
	 * </pre>
	 */
	@Column(name = "submit_at", columnDefinition = "datetime(3)")
	private Date submitAt;

	/**
	 * 学生提交作业时间
	 */
	@Column(name = "stu_submit_at", columnDefinition = "datetime(3)")
	private Date stuSubmitAt;

	/**
	 * 下发时间
	 */
	@Column(name = "issue_at", columnDefinition = "datetime(3)")
	private Date issueAt;

	/**
	 * 学生作业状态
	 */
	@Column(name = "status", precision = 3)
	private StudentHomeworkStatus status;

	/**
	 * 记录老师有没有批改过
	 * 
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 */
	@Deprecated
	@Column(name = "corrected", columnDefinition = "bit default 0")
	private boolean corrected = false;

	/**
	 * 记录学生有没有批改过
	 * 
	 * @since 小优快批，2018-2-12，早已不用该字段，切勿使用！
	 */
	@Deprecated
	@Column(name = "student_corrected", columnDefinition = "bit default 1")
	private boolean studentCorrected = true;

	/**
	 * 作业用时
	 */
	@Column(name = "homework_time", columnDefinition = "bigint default 0")
	private Integer homeworkTime = 0;

	/**
	 * 此次作业班级排名
	 */
	@Column(name = "rank")
	private Integer rank;

	/**
	 * 答对数量
	 */
	@Column(name = "right_count")
	private Integer rightCount;

	/**
	 * 答错数量
	 */
	@Column(name = "wrong_count")
	private Integer wrongCount;

	/**
	 * 半错数量
	 */
	@Column(name = "half_wrong_count")
	private Integer halfWrongCount;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, columnDefinition = "tinyint default 0")
	private Status delStatus = Status.ENABLED;

	/**
	 * 单选多选判断填空有没有都被批改过,如果都被批改过，此时前台老师可以批改简答题(人工智能批改完)
	 * 
	 * @since 小悠快批 2018-2-11 不再使用该字段判断，教师是否可以批改应该判断correct_status字段
	 */
	@Deprecated
	@Column(name = "auto_manual_all_corrected", columnDefinition = "bit default 0")
	private boolean autoManualAllCorrected = false;

	/**
	 * 完成率
	 */
	@Column(name = "completion_rate", scale = 2)
	private BigDecimal completionRate;

	/**
	 * 表示学生是否已经查看过此次作业 true -> 已经查看过 false -> 未查看过
	 */
	@Column(name = "viewed")
	private Boolean viewed = false;

	/**
	 * 学生作业批改状态（教师视角）.
	 * 
	 * @since 小优快批
	 */
	@Column(name = "correct_status", precision = 3)
	private StudentHomeworkCorrectStatus correctStatus = StudentHomeworkCorrectStatus.DEFAULT;

	/**
	 * 更新时间，客户端自动保存答案比较的时候需要用到.
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 是否统计有没有批改完
	 */
	@Transient
	private boolean statisticCorrected = false;
	/**
	 * 是否设置作业对象
	 */
	@Transient
	private boolean initHomework = false;
	/**
	 * 是否设置用户信息
	 */
	@Transient
	private boolean initUser = false;
	/**
	 * 是否只获取简单的作业对象
	 */
	@Transient
	private boolean simpleHomework = false;

	/**
	 * 是否统计学生的错题和已订正的题
	 */
	@Transient
	private boolean initStuHomeworkWrongAndCorrect = false;

	/**
	 * 是否统计已批改的题目数量和正在人工批改中的的题目数
	 */
	@Transient
	private boolean initStuHomeworkCorrectedAndCorrecting = false;

	/**
	 * 是否统计待批改题目数
	 */
	@Transient
	private boolean statisticTobeCorrected = false;

	/**
	 * 是否查询留言信息
	 */
	@Transient
	private boolean initMessages = false;

	public boolean isInitMessages() {
		return initMessages;
	}

	public void setInitMessages(boolean initMessages) {
		this.initMessages = initMessages;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
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

	public Date getSubmitAt() {
		return submitAt;
	}

	public void setSubmitAt(Date submitAt) {
		this.submitAt = submitAt;
	}

	public Date getStuSubmitAt() {
		return stuSubmitAt;
	}

	public void setStuSubmitAt(Date stuSubmitAt) {
		this.stuSubmitAt = stuSubmitAt;
	}

	public Date getIssueAt() {
		return issueAt;
	}

	public void setIssueAt(Date issueAt) {
		this.issueAt = issueAt;
	}

	public StudentHomeworkStatus getStatus() {
		return status;
	}

	public void setStatus(StudentHomeworkStatus status) {
		this.status = status;
	}

	/**
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 * @return
	 */
	@Deprecated
	public boolean isCorrected() {
		return corrected;
	}

	/**
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 * @param corrected
	 */
	@Deprecated
	public void setCorrected(boolean corrected) {
		this.corrected = corrected;
	}

	/**
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 * @return
	 */
	@Deprecated
	public boolean isStudentCorrected() {
		return studentCorrected;
	}

	/**
	 * @since 小优快批，2018-2-12，新流程判断不再使用该字段，切勿使用！
	 * @param studentCorrected
	 */
	@Deprecated
	public void setStudentCorrected(boolean studentCorrected) {
		this.studentCorrected = studentCorrected;
	}

	public Integer getHomeworkTime() {
		return homeworkTime;
	}

	public void setHomeworkTime(Integer homeworkTime) {
		this.homeworkTime = homeworkTime;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
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

	public Integer getHalfWrongCount() {
		return halfWrongCount;
	}

	public void setHalfWrongCount(Integer halfWrongCount) {
		this.halfWrongCount = halfWrongCount;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	/**
	 * 调用的地方需注意，不再使用该字段判断是否需要教师批改.
	 * 
	 * @since 小悠快批 2019-2-11
	 * @return
	 */
	@Deprecated
	public boolean isAutoManualAllCorrected() {
		return autoManualAllCorrected;
	}

	/**
	 * 调用的地方需注意，不再使用该字段判断是否需要教师批改.
	 * 
	 * @since 小悠快批 2019-2-11
	 * @return
	 */
	@Deprecated
	public void setAutoManualAllCorrected(boolean autoManualAllCorrected) {
		this.autoManualAllCorrected = autoManualAllCorrected;
	}

	public BigDecimal getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(BigDecimal completionRate) {
		this.completionRate = completionRate;
	}

	public boolean isStatisticCorrected() {
		return statisticCorrected;
	}

	public void setStatisticCorrected(boolean statisticCorrected) {
		this.statisticCorrected = statisticCorrected;
	}

	public boolean isInitHomework() {
		return initHomework;
	}

	public void setInitHomework(boolean initHomework) {
		this.initHomework = initHomework;
	}

	public boolean isInitUser() {
		return initUser;
	}

	public void setInitUser(boolean initUser) {
		this.initUser = initUser;
	}

	public boolean isSimpleHomework() {
		return simpleHomework;
	}

	public void setSimpleHomework(boolean simpleHomework) {
		this.simpleHomework = simpleHomework;
	}

	public Boolean isViewed() {
		return viewed;
	}

	public void setViewed(Boolean viewed) {
		this.viewed = viewed;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public StudentHomeworkCorrectStatus getCorrectStatus() {
		return correctStatus;
	}

	public void setCorrectStatus(StudentHomeworkCorrectStatus correctStatus) {
		this.correctStatus = correctStatus;
	}

	public boolean isInitStuHomeworkWrongAndCorrect() {
		return initStuHomeworkWrongAndCorrect;
	}

	public void setInitStuHomeworkWrongAndCorrect(boolean initStuHomeworkWrongAndCorrect) {
		this.initStuHomeworkWrongAndCorrect = initStuHomeworkWrongAndCorrect;
	}

	public boolean isInitStuHomeworkCorrectedAndCorrecting() {
		return initStuHomeworkCorrectedAndCorrecting;
	}

	public void setInitStuHomeworkCorrectedAndCorrecting(boolean initStuHomeworkCorrectedAndCorrecting) {
		this.initStuHomeworkCorrectedAndCorrecting = initStuHomeworkCorrectedAndCorrecting;
	}

	public boolean isStatisticTobeCorrected() {
		return statisticTobeCorrected;
	}

	public void setStatisticTobeCorrected(boolean statisticTobeCorrected) {
		this.statisticTobeCorrected = statisticTobeCorrected;
	}

	public BigDecimal getRightRateCorrect() {
		return rightRateCorrect;
	}

	public void setRightRateCorrect(BigDecimal rightRateCorrect) {
		this.rightRateCorrect = rightRateCorrect;
	}

}
