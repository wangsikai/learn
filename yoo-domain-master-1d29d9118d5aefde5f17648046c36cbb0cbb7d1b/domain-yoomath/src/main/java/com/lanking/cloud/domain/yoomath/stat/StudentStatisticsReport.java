package com.lanking.cloud.domain.yoomath.stat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 学生学情报告
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_statistics_report")
public class StudentStatisticsReport implements Serializable {
	private static final long serialVersionUID = -7046002120813035512L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 统计年月
	 * 
	 * <pre>
	 * 学情报告是当月统计上个的，所以此字段可以设置如下:
	 * 		Calendar now = Calendar.getInstance();
	 *      now.set(Calendar.DAY_OF_MONTH, 1);
	 *		now.add(Calendar.MONTH, -1);
	 *		setCalDate(now.getTime());
	 * 无需关心时分秒...
	 * </pre>
	 */
	@Column(name = "cal_date", columnDefinition = "datetime(3)")
	private Date calDate;

	/**
	 * 正确率排名
	 */
	@Column(name = "rank")
	private Integer rank;

	/**
	 * 班级人数
	 */
	@Column(name = "student_num")
	private Integer studentNum;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private Long classId;

	/**
	 * 学生ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 作业总数
	 */
	@Column(name = "homework_num")
	private Integer homeworkNum;

	/**
	 * 题目总数
	 */
	@Column(name = "question_num")
	private Integer questionNum;

	/**
	 * 做题数
	 */
	@Column(name = "question_do_num")
	private Integer questionDoNum;

	/**
	 * 学科
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 平均正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 完成率
	 */
	@Column(name = "complete_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal completeRate;

	/**
	 * 正确率统计集合(存放JSON数组，包含关键数据：统计日期、正确率)
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "right_rates")
	private String rightRates;

	/**
	 * 学情分析集合（存放JSON数组，按顺序存放，包含关键数据：章节CODE、章节名称、正确率）
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "knowpoint_analysis")
	private String knowpointAnalysis;

	/**
	 * 统计时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 是否已购买
	 */
	@Column(name = "buy")
	private Boolean buy = false;

	/**
	 * 版本
	 * 
	 * <pre>
	 * 报告版本目前都为1
	 * </pre>
	 */
	@Column(name = "version", precision = 3)
	private Integer version = 1;

	@Transient
	private List<Map<String, Object>> rightRateMaps;
	@Transient
	private List<Map<String, Object>> knowpointAnalysisMaps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCalDate() {
		return calDate;
	}

	public void setCalDate(Date calDate) {
		this.calDate = calDate;
	}

	public Integer getHomeworkNum() {
		return homeworkNum;
	}

	public void setHomeworkNum(Integer homeworkNum) {
		this.homeworkNum = homeworkNum;
	}

	public Integer getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(Integer questionNum) {
		this.questionNum = questionNum;
	}

	public Integer getQuestionDoNum() {
		return questionDoNum;
	}

	public void setQuestionDoNum(Integer questionDoNum) {
		this.questionDoNum = questionDoNum;
	}

	public BigDecimal getRightRate() {
		return rightRate;
	}

	public void setRightRate(BigDecimal rightRate) {
		this.rightRate = rightRate;
	}

	public BigDecimal getCompleteRate() {
		return completeRate;
	}

	public void setCompleteRate(BigDecimal completeRate) {
		this.completeRate = completeRate;
	}

	public String getRightRates() {
		return rightRates;
	}

	public void setRightRates(String rightRates) {
		this.rightRates = rightRates;
	}

	public String getKnowpointAnalysis() {
		return knowpointAnalysis;
	}

	public void setKnowpointAnalysis(String knowpointAnalysis) {
		this.knowpointAnalysis = knowpointAnalysis;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	// extends

	@SuppressWarnings({ "rawtypes" })
	public List<Map> getRightRateMaps() {
		return JSONArray.parseArray(this.getRightRates(), Map.class);
	}

	public void setRightRateMaps(List<Map<String, Object>> rightRateMaps) {
		if (rightRateMaps != null) {
			setRightRates(JSON.toJSONString(rightRateMaps));
		}
		this.rightRateMaps = rightRateMaps;
	}

	@SuppressWarnings({ "rawtypes" })
	public List<Map> getKnowpointAnalysisMaps() {
		return JSONArray.parseArray(this.getKnowpointAnalysis(), Map.class);
	}

	public void setKnowpointAnalysisMaps(List<Map<String, Object>> knowpointAnalysisMaps) {
		if (knowpointAnalysisMaps != null) {
			setKnowpointAnalysis(JSON.toJSONString(knowpointAnalysisMaps));
		}
		this.knowpointAnalysisMaps = knowpointAnalysisMaps;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Boolean getBuy() {
		return buy;
	}

	public void setBuy(Boolean buy) {
		this.buy = buy;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

}
