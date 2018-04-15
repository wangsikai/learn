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
 * 班级学情报告
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "class_statistics_report")
public class ClassStatisticsReport implements Serializable {
	private static final long serialVersionUID = -2265822045021642277L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private Long classId;

	/**
	 * 教师ID
	 */
	@Column(name = "teacher_id")
	private Long teacherId;

	/**
	 * 统计年月
	 * 
	 * <pre>
	 * 学情报告是当月统计上个的，所以此字段可以设置如下:
	 * 		Calendar now = Calendar.getInstance();
	 *      now.set(Calendar.DAY_OF_MONTH, 1);
	 * 	now.add(Calendar.MONTH, -1);
	 * 	setCalDate(now.getTime());
	 * 无需关心时分秒...
	 * </pre>
	 */
	@Column(name = "cal_date", columnDefinition = "date")
	private Date calDate;

	/**
	 * 作业平均正确率
	 */
	@Column(name = "right_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal rightRate;

	/**
	 * 作业平均完成率
	 */
	@Column(name = "complete_rate", scale = 2, columnDefinition = "decimal default 0.00")
	private BigDecimal completeRate;

	/**
	 * 作业总数
	 */
	@Column(name = "homework_num")
	private Integer homeworkNum;

	/**
	 * 学科
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 班级人数(取统计时的数据)
	 */
	@Column(name = "student_num")
	private Integer studentNum;

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
	 * 班级每份作业的相关数据
	 * 
	 * <pre>
	 * 数据格式如下:
	 * [
	 * 	{
	 * 		"createAt":1492681321558,
	 * 		"id":703647525105573888,
	 * 		"name":"乡试试题",
	 * 		"questionCount":1,
	 * 		"rightRate":-1,
	 * 		"beginAt":1492681321558
	 * 	}
	 *  ...
	 * ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "right_rates")
	private String rightRates;

	/**
	 * 学生作业正确率的平均数据
	 * 
	 * <pre>
	 * 数据格式如下:
	 * {
	 * 		maxRightRate:{studentId:1,rightRate:100},----------------最高平均正确率
	 *  	minRightRate:{studentId:2,rightRate:10},-----------------最低平均正确率
	 *  	avgRightRate:[-------------------------------------------每个学生的平均正确率
	 *  		{studentId:1,rightRate:100,rank:1,rankingFloating:1},
	 *  		{studentId:2,rightRate:10,rank:1,rankingFloating:-11}
	 *  	],
	 *  	distributedRightRate:[-----------------------------------平均正确率分布
	 *  		{leRightRate:0,reRightRate:59,studentCount:10},
	 *      	{leRightRate:60,reRightRate:69,studentCount:10}
	 *  	]
	 *  }
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "student_right_rates")
	private String studentRightRates;

	/**
	 * 知识点学情分析集合
	 * 
	 * <pre>
	 * version=1,此字段被存储为章节的了,原因不详
	 * 数据格式如下:
	 * [
	 * 	{
	 * 	"code":1522020101,
	 * 		"complete":0,
	 * 		"level":1,
	 * 		"pcode":0,
	 * 		"sectionName":"第1章我们与数学同行数学同行数学同行"
	 * }
	 *  ...
	 * ]
	 * version=2
	 * 数据格式如下:
	 * [
	 * 	{
	 * 		sectionCode:1,
	 *  	sectionName:'集合',
	 *      knowledgePoints:[{
	 *      	code:1,
	 *      	name:'集合',
	 *      	score:'100',
	 *      	masterStatus:1
	 *      	}
	 *      	....
	 *      ]     
	 *  }...
	 * ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "knowpoint_analysis")
	private String knowpointAnalysis;

	/**
	 * 章节学情分析集合
	 * 
	 * <pre>
	 * version=1,无此字段
	 * version=2
	 * 数据格式如下:
	 * [
	 * 	{
	 * 		code:1,
	 *  	name:'集合',
	 * 		score:'100',
	 *     	masterStatus:1
	 *      children:[{
	 *      	code:1,
	 *      	name:'集合',
	 *      	score:'100',
	 *      	masterStatus:1
	 *      }...
	 *      ]     
	 *  }
	 * ]
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "section_analysis")
	private String sectionAnalysis;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 版本
	 * 
	 * <pre>
	 * 报告版本
	 * 1:calDate <= 2017.7
	 * 2:calDate >= 2017.8
	 * </pre>
	 */
	@Column(name = "version", precision = 3)
	private Integer version = 1;

	@Transient
	private List<Map<String, Object>> rightRateMaps;
	@Transient
	private List<Map<String, Object>> knowpointAnalysisMaps;
	@Transient
	private List<Map<String, Object>> sectionAnalysisMaps;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Date getCalDate() {
		return calDate;
	}

	public void setCalDate(Date calDate) {
		this.calDate = calDate;
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

	public Integer getHomeworkNum() {
		return homeworkNum;
	}

	public void setHomeworkNum(Integer homeworkNum) {
		this.homeworkNum = homeworkNum;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
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

	public String getRightRates() {
		return rightRates;
	}

	public void setRightRates(String rightRates) {
		this.rightRates = rightRates;
	}

	public String getStudentRightRates() {
		return studentRightRates;
	}

	public void setStudentRightRates(String studentRightRates) {
		this.studentRightRates = studentRightRates;
	}

	public String getKnowpointAnalysis() {
		return knowpointAnalysis;
	}

	public void setKnowpointAnalysis(String knowpointAnalysis) {
		this.knowpointAnalysis = knowpointAnalysis;
	}

	public String getSectionAnalysis() {
		return sectionAnalysis;
	}

	public void setSectionAnalysis(String sectionAnalysis) {
		this.sectionAnalysis = sectionAnalysis;
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

	@SuppressWarnings({ "rawtypes" })
	public List<Map> getSectionAnalysisMaps() {
		return JSONArray.parseArray(this.getSectionAnalysis(), Map.class);
	}

	public void setSectionAnalysisMaps(List<Map<String, Object>> sectionAnalysisMaps) {
		if (knowpointAnalysisMaps != null) {
			setKnowpointAnalysis(JSON.toJSONString(sectionAnalysisMaps));
		}
		this.sectionAnalysisMaps = sectionAnalysisMaps;
	}
}
