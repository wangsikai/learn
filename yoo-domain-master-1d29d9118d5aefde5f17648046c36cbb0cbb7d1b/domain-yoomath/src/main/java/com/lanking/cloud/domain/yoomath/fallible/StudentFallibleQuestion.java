package com.lanking.cloud.domain.yoomath.fallible;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;

/**
 * 学生错题
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "student_fallible_question")
public class StudentFallibleQuestion implements Serializable {

	private static final long serialVersionUID = -6038251610595890051L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 学生用户ID
	 */
	@Column(name = "student_id")
	private Long studentId;

	/**
	 * 题目ID
	 */
	@Column(name = "question_id")
	private Long questionId;

	/**
	 * 题目基本类型
	 */
	@Column(name = "type", precision = 3)
	private Question.Type type;

	/**
	 * 学科具体题目类型
	 */
	@Column(name = "type_code")
	private Integer typeCode;

	/**
	 * 错误次数
	 */
	@Column(name = "mistake_times")
	private Integer mistakeTimes;

	/**
	 * 做题次数
	 */
	@Column(name = "do_num")
	private Long doNum;

	/**
	 * 错误次数
	 */
	@Column(name = "mistake_num")
	private Long mistakeNum;

	/**
	 * 做题次数
	 */
	@Column(name = "exercise_num")
	private Long exerciseNum;

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private Double difficulty;

	/**
	 * 最近的一次答案latex编码
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "latest_answer", length = 4000)
	private Map<Long, List<String>> latestAnswer;

	/**
	 * 最近的一次答案asciimath编码
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "latest_asciimath_answer", length = 4000)
	private Map<Long, List<String>> latestAsciimathAnswer;

	/**
	 * 答案图片列表
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "latest_answer_imgs")
	private List<Long> latestAnswerImgs = Lists.newArrayList();

	/**
	 * 最近的一次答案正确率
	 */
	@Column(name = "latest_right_rate", precision = 5)
	private Integer latestRightRate;

	/**
	 * 最近的一次答案结果
	 */
	@Column(name = "latest_result", precision = 3)
	private HomeworkAnswerResult latestResult;

	/**
	 * 最近的一次答案项结果
	 * 
	 * <pre>
	 * 1.填空题每空的对错情况
	 * </pre>
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "item_results")
	private List<HomeworkAnswerResult> latestItemResults = Lists.newArrayList();

	/**
	 * 最近的一次答案来源
	 */
	@Column(name = "latest_source", precision = 3)
	private StudentQuestionAnswerSource latestSource;

	/**
	 * ocr识别图片
	 */
	@Column(name = "ocr_image_id")
	private Long ocrImageId;

	/**
	 * ocr识别后语义识别后的知识点
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 1024, name = "ocr_knowpoint_codes")
	private List<Long> ocrKnowpointCodes = Lists.newArrayList();

	/**
	 * ocr语义标签后归档的教材代码
	 */
	@Column(name = "ocr_textbook_code")
	private Integer ocrTextbookCode;

	/**
	 * 只针对OCR库未有题目并添加图片至错题本类型的题目的答题历史（其他情况依旧存储在StudentQuestionAnswer表中）
	 *
	 * map的key为 imageId 表示答题图片 answerAt 表示答题时间
	 */
	@SuppressWarnings("rawtypes")
	@Type(type = JSONType.TYPE)
	@Column(length = 2048, name = "ocr_his_answer_imgs")
	private List<Map> ocrHisAnswerImgs = Lists.newArrayList();

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

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	/**
	 * 答案图片
	 */
	@Transient
	private Long latestAnswerImg;
	
	/**
	 * 转换变量，是否转换latex为图片（兼容设置，默认true）.
	 */
	@Transient
	private boolean initLatexImg = true;

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

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Question.Type getType() {
		return type;
	}

	public void setType(Question.Type type) {
		this.type = type;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Integer getMistakeTimes() {
		return mistakeTimes;
	}

	public void setMistakeTimes(Integer mistakeTimes) {
		this.mistakeTimes = mistakeTimes;
	}

	public Long getDoNum() {
		return doNum;
	}

	public void setDoNum(Long doNum) {
		this.doNum = doNum;
	}

	public Long getMistakeNum() {
		return mistakeNum;
	}

	public void setMistakeNum(Long mistakeNum) {
		this.mistakeNum = mistakeNum;
	}

	public Long getExerciseNum() {
		return exerciseNum;
	}

	public void setExerciseNum(Long exerciseNum) {
		this.exerciseNum = exerciseNum;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public Map<Long, List<String>> getLatestAnswer() {
		return latestAnswer;
	}

	public void setLatestAnswer(Map<Long, List<String>> latestAnswer) {
		this.latestAnswer = latestAnswer;
	}

	public Map<Long, List<String>> getLatestAsciimathAnswer() {
		return latestAsciimathAnswer;
	}

	public void setLatestAsciimathAnswer(Map<Long, List<String>> latestAsciimathAnswer) {
		this.latestAsciimathAnswer = latestAsciimathAnswer;
	}

	public List<Long> getLatestAnswerImgs() {
		return latestAnswerImgs;
	}

	public void setLatestAnswerImgs(List<Long> latestAnswerImgs) {
		this.latestAnswerImgs = latestAnswerImgs;
	}

	public Integer getLatestRightRate() {
		return latestRightRate;
	}

	public void setLatestRightRate(Integer latestRightRate) {
		this.latestRightRate = latestRightRate;
	}

	public HomeworkAnswerResult getLatestResult() {
		return latestResult;
	}

	public void setLatestResult(HomeworkAnswerResult latestResult) {
		this.latestResult = latestResult;
	}

	public List<HomeworkAnswerResult> getLatestItemResults() {
		return latestItemResults;
	}

	public void setLatestItemResults(List<HomeworkAnswerResult> latestItemResults) {
		this.latestItemResults = latestItemResults;
	}

	public StudentQuestionAnswerSource getLatestSource() {
		return latestSource;
	}

	public void setLatestSource(StudentQuestionAnswerSource latestSource) {
		this.latestSource = latestSource;
	}

	public Long getOcrImageId() {
		return ocrImageId;
	}

	public void setOcrImageId(Long ocrImageId) {
		this.ocrImageId = ocrImageId;
	}

	public List<Long> getOcrKnowpointCodes() {
		return ocrKnowpointCodes;
	}

	public void setOcrKnowpointCodes(List<Long> ocrKnowpointCodes) {
		this.ocrKnowpointCodes = ocrKnowpointCodes;
	}

	public Integer getOcrTextbookCode() {
		return ocrTextbookCode;
	}

	public void setOcrTextbookCode(Integer ocrTextbookCode) {
		this.ocrTextbookCode = ocrTextbookCode;
	}

	@SuppressWarnings("rawtypes")
	public List<Map> getOcrHisAnswerImgs() {
		return ocrHisAnswerImgs;
	}

	@SuppressWarnings("rawtypes")
	public void setOcrHisAnswerImgs(List<Map> ocrHisAnswerImgs) {
		this.ocrHisAnswerImgs = ocrHisAnswerImgs;
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

	public Long getLatestAnswerImg() {
		if (CollectionUtils.isNotEmpty(getLatestAnswerImgs())) {
			latestAnswerImg = getLatestAnswerImgs().get(0);
		}
		return latestAnswerImg;
	}

	public void setLatestAnswerImg(Long latestAnswerImg) {
		this.latestAnswerImg = latestAnswerImg;
	}

	public boolean isInitLatexImg() {
		return initLatexImg;
	}

	public void setInitLatexImg(boolean initLatexImg) {
		this.initLatexImg = initLatexImg;
	}
	
}
