package com.lanking.cloud.domain.common.resource.question;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.common.baseData.QuestionType;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.bean.Valueable;
import com.lanking.cloud.sdk.util.CollectionUtils;

/**
 * 题目
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

	private static final long serialVersionUID = -6306205605318410674L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 题目代码
	 */
	@Column(name = "code", length = 40)
	private String code;

	/**
	 * 题目学科具体类型
	 * 
	 * @see QuestionType
	 */
	@Column(name = "type_code")
	private Integer typeCode;

	/**
	 * 题目基本类型
	 * 
	 * @see Type
	 */
	@Column(name = "type", precision = 3)
	private Type type;

	/**
	 * 教材版本代码
	 */
	@Deprecated
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材代码
	 */
	@Deprecated
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 学科代码
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 阶段代码
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 题干
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "content")
	private String content;

	/**
	 * 难度
	 */
	@Column(name = "difficulty")
	private Double difficulty;

	/**
	 * 解析
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "analysis")
	private String analysis;

	/**
	 * 提示
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "hint")
	private String hint;

	/**
	 * 子题标识
	 * 
	 * <pre>
	 * 1.subFlag = true 即 sub_flag = 1表示子题
	 * 2.subFlag = false 即 sub_flag = 0表示子题
	 * </pre>
	 */
	@Column(name = "sub_flag")
	private boolean subFlag;

	/**
	 * 序号（子题的序号）
	 */
	@Column(name = "sequence")
	private Integer sequence;

	/**
	 * 父题ID
	 */
	@Column(name = "parent_id")
	private Long parentId;

	/**
	 * 选项A
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "choice_a")
	private String choiceA;

	/**
	 * 选项B
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "choice_b")
	private String choiceB;

	/**
	 * 选项C
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "choice_c")
	private String choiceC;

	/**
	 * 选项D
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "choice_d")
	private String choiceD;

	/**
	 * 选项E
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "choice_e")
	private String choiceE;

	/**
	 * 选项F
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "choice_f")
	private String choiceF;

	/**
	 * 答案数量
	 */
	@Column(name = "answer_number")
	private Integer answerNumber;

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
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 供应商ID
	 */
	@Column(name = "vendor_id")
	private Long vendorId;

	/**
	 * 校验人ID
	 */
	@Column(name = "verify_id")
	private Long verifyId;

	/**
	 * 校验时间
	 */
	@Column(name = "verify_at", columnDefinition = "datetime(3)")
	private Date verifyAt;

	/**
	 * 二次校验人ID
	 */
	@Column(name = "verify2_id")
	private Long verify2Id;

	/**
	 * 二次校验时间
	 */
	@Column(name = "verify2_at", columnDefinition = "datetime(3)")
	private Date verify2At;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3, nullable = false)
	private CheckStatus status = CheckStatus.EDITING;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, nullable = false)
	private Status delStatus = Status.ENABLED;

	/**
	 * 章节代码
	 */
	@Deprecated
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 来源
	 */
	@Column(name = "source", length = 100)
	private String source;

	/**
	 * 校验阶段（仅针对主题）
	 */
	@Column(name = "step")
	private Integer step = 0;

	/**
	 * 是否为草稿（仅针对主题）
	 */
	@Column(name = "draft")
	private Boolean draft = false;

	/**
	 * 选项默认排列方式（仅针对选择题型）
	 */
	@Column(name = "choice_format", precision = 3)
	private ChoiceFormat choiceFormat;

	/**
	 * 题目来源平台
	 * 
	 * @see QuestionSource
	 */
	@Column(name = "question_source", precision = 3, nullable = false)
	private QuestionSource questionSource = QuestionSource.VENDOR;

	/**
	 * 学校题库,如果为0则为公共题库
	 */
	@Column(name = "school_id", columnDefinition = "bigint default 0")
	private long schoolId;

	/**
	 * 未通过理由
	 */
	@Column(name = "nopass_content", length = 500)
	private String nopassContent;

	/**
	 * 未通过图片
	 */
	@Column(name = "nopass_files", length = 100)
	private String nopassFiles;

	/**
	 * 是否为开放性答案（仅针对填空式主题）.
	 */
	@Column(name = "open_answer_flag", columnDefinition = "bit default 0")
	private Boolean openAnswerFlag = false;

	/**
	 * 是否更改方式后的新题（注意此属性用于区分旧题，判断是否需要覆盖答案中的content字段）.
	 */
	@Column(name = "new_flag", columnDefinition = "bit default 0")
	private Boolean newFlag = false;

	/**
	 * asciiMath 转换状态
	 */
	@Column(name = "ascii_status", precision = 3, nullable = false)
	private AsciiStatus asciiStatus = AsciiStatus.NOCHANGE;

	/**
	 * 知识点转换功能中添加知识点的人员（统计中不包含正常新增修改提交）
	 */
	@Column(name = "knowledge_create_id", columnDefinition = "bigint default 0")
	private Long knowledgeCreateId = 0L;

	/**
	 * 添加新知识点的时间
	 */
	@Column(name = "knowledge_create_at", columnDefinition = "datetime(3)")
	private Date knowledgeCreateAt;

	/**
	 * 应用场景编码
	 * 
	 * @since 教师端v1.3.0 2017-7-19 不再使用场景
	 */
	@Deprecated
	@Column(name = "scene_code")
	private Integer sceneCode;

	/**
	 * 题目标签
	 * 
	 * @see QuestionCategoryType
	 */
	@org.hibernate.annotations.Type(type = JSONType.TYPE)
	@Column(name = "category_types")
	private List<QuestionCategoryType> categoryTypes;

	/**
	 * 重复题展示状态.
	 */
	@Column(name = "same_show", columnDefinition = "bit default null")
	private Boolean sameShow;

	/**
	 * 重复题关联展示题目的ID
	 */
	@Column(name = "same_show_id")
	private Long sameShowId;

	/**
	 * 是否包含相似题
	 */
	@Column(name = "similar_flag")
	private Boolean hasSimilar = false;

	/**
	 * 自动知识点语义标签
	 */
	@org.hibernate.annotations.Type(type = JSONType.TYPE)
	@Column(name = "knowledge_points_auto")
	private List<Long> autoKnowledgePoints;

	/**
	 * 手工知识点标签
	 */
	@org.hibernate.annotations.Type(type = JSONType.TYPE)
	@Column(name = "knowledge_points_manual")
	private List<Long> manualKnowledgePoints;

	/**
	 * 校验打回标记（退回一校）
	 * 
	 * @since 2017-05-02 中央资源库 v1.3.3
	 */
	@Column(name = "check_refund")
	private boolean checkRefund = false;

	/**
	 * convert 转换变量，是否填充学生作业答案
	 */
	@Transient
	private Long studentHomeworkId;

	/**
	 * convert 转换变量，ZycQuestionConvert 悠数学管控台填充作业倒计时时间使用
	 * 
	 * @see com.lanking.uxb.zycon.homework.convert.ZycQuestionConvert
	 */
	@Transient
	private Long homeworkId;

	@Transient
	private Long studentQuestionId;

	/**
	 * convert 转换变量，是否填充解析字段
	 */
	@Transient
	private boolean isAnalysis = false;

	/**
	 * convert 转换变量，是否填充答案
	 */
	@Transient
	private boolean isAnswer = false;

	/**
	 * convert 转换变量，是否填充子题（复合题）
	 */
	@Transient
	private boolean initSub = false; // 复合题不使用，默认不转换

	/**
	 * convert 转换变量，是否填充当前用户收藏标记
	 */
	@Transient
	private boolean isCollect = false;

	/**
	 * convert 转换变量，是否填充所属教材版本（兼容设置，默认true）.
	 */
	@Transient
	private boolean initTextbookCategory = true;

	/**
	 * convert 转换变量，是否填充旧知识点（兼容设置，默认true）.
	 */
	@Transient
	private boolean initMetaKnowpoint = true;

	/**
	 * convert 转换变量，是否填充阶段（兼容设置，默认true）.
	 */
	@Transient
	private boolean initPhase = true;

	/**
	 * convert 转换变量，是否填充学科（兼容设置，默认true）.
	 */
	@Transient
	private boolean initSubject = true;

	/**
	 * convert 转换变量，是否填充题目类型（兼容设置，默认true）.
	 */
	@Transient
	private boolean initQuestionType = true;

	/**
	 * convert 转换变量，是否填充新知识点（兼容设置，默认true）.
	 */
	@Transient
	private boolean initKnowledgePoint = true;

	/**
	 * convert 转换变量，是否填充当前学生作对题数量及所有人做题情况（兼容设置，默认true）.
	 * <p>
	 * 客户端在使用
	 * </p>
	 */
	@Transient
	private boolean initStudentQuestionCount = true;

	/**
	 * 是否是订正题
	 */
	@Transient
	private boolean correctQuestion = false;

	/**
	 * 是否转换题目的考点数据
	 */
	@Transient
	private boolean initExamination = false;

	/**
	 * 存放临时变量
	 */
	@Transient
	private Map<String, Long> transientIds;
	@Transient
	private Map<String, Boolean> transientBooleans;

	/**
	 * 是否设置布置过作业次数
	 */
	@Transient
	private boolean initPublishCount = false;

	/**
	 * 是否设置相似题数量
	 */
	@Transient
	private boolean initQuestionSimilarCount = false;

	/**
	 * 是否设置题目标签
	 */
	@Transient
	private boolean initQuestionTag = false;
	
	/**
	 * convert 转换变量，是否转换latex为图片（兼容设置，默认true）.
	 */
	@Transient
	private boolean initLatexImg = true;
	
	/**
	 * 是否是新订正题
	 */
	@Transient
	private boolean newCorrect = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Double difficulty) {
		this.difficulty = difficulty;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public boolean isSubFlag() {
		return subFlag;
	}

	public void setSubFlag(boolean subFlag) {
		this.subFlag = subFlag;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getChoiceA() {
		return choiceA;
	}

	public void setChoiceA(String choiceA) {
		this.choiceA = choiceA;
	}

	public String getChoiceB() {
		return choiceB;
	}

	public void setChoiceB(String choiceB) {
		this.choiceB = choiceB;
	}

	public String getChoiceC() {
		return choiceC;
	}

	public void setChoiceC(String choiceC) {
		this.choiceC = choiceC;
	}

	public String getChoiceD() {
		return choiceD;
	}

	public void setChoiceD(String choiceD) {
		this.choiceD = choiceD;
	}

	public String getChoiceE() {
		return choiceE;
	}

	public void setChoiceE(String choiceE) {
		this.choiceE = choiceE;
	}

	public String getChoiceF() {
		return choiceF;
	}

	public void setChoiceF(String choiceF) {
		this.choiceF = choiceF;
	}

	public Integer getAnswerNumber() {
		return answerNumber;
	}

	public void setAnswerNumber(Integer answerNumber) {
		this.answerNumber = answerNumber;
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

	public Long getStudentHomeworkId() {
		return studentHomeworkId;
	}

	public void setStudentHomeworkId(Long studentHomeworkId) {
		this.studentHomeworkId = studentHomeworkId;
	}

	public boolean isAnalysis() {
		return isAnalysis;
	}

	public void setAnalysis(boolean isAnalysis) {
		this.isAnalysis = isAnalysis;
	}

	public boolean isAnswer() {
		return isAnswer;
	}

	public void setAnswer(boolean isAnswer) {
		this.isAnswer = isAnswer;
	}

	public boolean isInitSub() {
		return initSub;
	}

	public void setInitSub(boolean initSub) {
		this.initSub = initSub;
	}

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Long getVerifyId() {
		return verifyId;
	}

	public void setVerifyId(Long verifyId) {
		this.verifyId = verifyId;
	}

	public Date getVerifyAt() {
		return verifyAt;
	}

	public void setVerifyAt(Date verifyAt) {
		this.verifyAt = verifyAt;
	}

	public Long getVerify2Id() {
		return verify2Id;
	}

	public void setVerify2Id(Long verify2Id) {
		this.verify2Id = verify2Id;
	}

	public Date getVerify2At() {
		return verify2At;
	}

	public void setVerify2At(Date verify2At) {
		this.verify2At = verify2At;
	}

	public CheckStatus getStatus() {
		return status;
	}

	public void setStatus(CheckStatus status) {
		this.status = status;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Boolean getDraft() {
		return draft;
	}

	public void setDraft(Boolean draft) {
		this.draft = draft;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

	public ChoiceFormat getChoiceFormat() {
		if (choiceFormat == null) {
			// 历史题目默认的排列方式
			if (StringUtils.isBlank(this.choiceD)) {
				this.choiceFormat = ChoiceFormat.HORIZONTAL;
			} else if (StringUtils.isBlank(this.choiceE) && StringUtils.isNotBlank(this.choiceD)) {
				this.choiceFormat = ChoiceFormat.ABREAST;
			} else {
				this.choiceFormat = ChoiceFormat.VERTICAL;
			}
		}
		return choiceFormat;
	}

	public void setChoiceFormat(ChoiceFormat choiceFormat) {
		this.choiceFormat = choiceFormat;
	}

	public boolean isCorrectQuestion() {
		return correctQuestion;
	}

	public void setCorrectQuestion(boolean correctQuestion) {
		this.correctQuestion = correctQuestion;
	}

	public Long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(Long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Long getStudentQuestionId() {
		return studentQuestionId;
	}

	public void setStudentQuestionId(Long studentQuestionId) {
		this.studentQuestionId = studentQuestionId;
	}

	public QuestionSource getQuestionSource() {
		return questionSource == null ? QuestionSource.VENDOR : questionSource;
	}

	public void setQuestionSource(QuestionSource questionSource) {
		this.questionSource = questionSource;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public String getNopassContent() {
		return nopassContent;
	}

	public void setNopassContent(String nopassContent) {
		this.nopassContent = nopassContent;
	}

	public String getNopassFiles() {
		return nopassFiles;
	}

	public void setNopassFiles(String nopassFiles) {
		this.nopassFiles = nopassFiles;
	}

	public Map<String, Long> getTransientIds() {
		return transientIds;
	}

	public void setTransientIds(Map<String, Long> transientIds) {
		this.transientIds = transientIds;
	}

	public void putTransientId(String key, Long value) {
		if (this.transientIds == null) {
			this.transientIds = new HashMap<String, Long>();
		}
		this.transientIds.put(key, value);
	}

	public Long getTransientId(String key) {
		if (this.transientIds == null) {
			return null;
		}
		return this.transientIds.get(key);
	}

	public Map<String, Boolean> getTransientBooleans() {
		return transientBooleans;
	}

	public void setTransientBooleans(Map<String, Boolean> transientBooleans) {
		this.transientBooleans = transientBooleans;
	}

	public void putTransientBoolean(String key, Boolean value) {
		if (this.transientBooleans == null) {
			this.transientBooleans = new HashMap<String, Boolean>();
		}
		this.transientBooleans.put(key, value);
	}

	public Boolean getTransientBoolean(String key) {
		if (this.transientBooleans == null) {
			return null;
		}
		return this.transientBooleans.get(key);
	}

	public Boolean getOpenAnswerFlag() {
		return openAnswerFlag;
	}

	public void setOpenAnswerFlag(Boolean openAnswerFlag) {
		this.openAnswerFlag = openAnswerFlag;
	}

	public Boolean getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(Boolean newFlag) {
		this.newFlag = newFlag;
	}

	public AsciiStatus getAsciiStatus() {
		return asciiStatus;
	}

	public void setAsciiStatus(AsciiStatus asciiStatus) {
		this.asciiStatus = asciiStatus;
	}

	public Long getKnowledgeCreateId() {
		return knowledgeCreateId;
	}

	public void setKnowledgeCreateId(Long knowledgeCreateId) {
		this.knowledgeCreateId = knowledgeCreateId;
	}

	public Date getKnowledgeCreateAt() {
		return knowledgeCreateAt;
	}

	public void setKnowledgeCreateAt(Date knowledgeCreateAt) {
		this.knowledgeCreateAt = knowledgeCreateAt;
	}

	public Integer getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(Integer sceneCode) {
		this.sceneCode = sceneCode == null ? 0 : sceneCode;
	}

	public List<QuestionCategoryType> getCategoryTypes() {
		if (CollectionUtils.isNotEmpty(categoryTypes)) {
			// 重新对题目标签排序
			Collections.sort(categoryTypes, new Comparator<QuestionCategoryType>() {
				@Override
				public int compare(QuestionCategoryType o1, QuestionCategoryType o2) {
					if (o1.getValue() < o2.getValue()) {
						return -1;
					} else {
						return 1;
					}
				}
			});
		}
		return categoryTypes;
	}

	public void setCategoryTypes(List<QuestionCategoryType> categoryTypes) {
		this.categoryTypes = categoryTypes;
	}

	public boolean isInitExamination() {
		return initExamination;
	}

	public void setInitExamination(boolean initExamination) {
		this.initExamination = initExamination;
	}

	public Boolean getSameShow() {
		return sameShow;
	}

	public void setSameShow(Boolean sameShow) {
		this.sameShow = sameShow;
	}

	public Long getSameShowId() {
		return sameShowId;
	}

	public void setSameShowId(Long sameShowId) {
		this.sameShowId = sameShowId;
	}

	public Boolean isHasSimilar() {
		return hasSimilar == null ? false : hasSimilar;
	}

	public void setHasSimilar(Boolean hasSimilar) {
		this.hasSimilar = hasSimilar;
	}

	public List<Long> getAutoKnowledgePoints() {
		return autoKnowledgePoints;
	}

	public void setAutoKnowledgePoints(List<Long> autoKnowledgePoints) {
		this.autoKnowledgePoints = autoKnowledgePoints;
	}

	public List<Long> getManualKnowledgePoints() {
		return manualKnowledgePoints;
	}

	public void setManualKnowledgePoints(List<Long> manualKnowledgePoints) {
		this.manualKnowledgePoints = manualKnowledgePoints;
	}

	public boolean isCheckRefund() {
		return checkRefund;
	}

	public void setCheckRefund(boolean checkRefund) {
		this.checkRefund = checkRefund;
	}

	public boolean isInitTextbookCategory() {
		return initTextbookCategory;
	}

	public void setInitTextbookCategory(boolean initTextbookCategory) {
		this.initTextbookCategory = initTextbookCategory;
	}

	public boolean isInitMetaKnowpoint() {
		return initMetaKnowpoint;
	}

	public void setInitMetaKnowpoint(boolean initMetaKnowpoint) {
		this.initMetaKnowpoint = initMetaKnowpoint;
	}

	public boolean isInitPhase() {
		return initPhase;
	}

	public void setInitPhase(boolean initPhase) {
		this.initPhase = initPhase;
	}

	public boolean isInitSubject() {
		return initSubject;
	}

	public void setInitSubject(boolean initSubject) {
		this.initSubject = initSubject;
	}

	public boolean isInitQuestionType() {
		return initQuestionType;
	}

	public void setInitQuestionType(boolean initQuestionType) {
		this.initQuestionType = initQuestionType;
	}

	public boolean isInitKnowledgePoint() {
		return initKnowledgePoint;
	}

	public void setInitKnowledgePoint(boolean initKnowledgePoint) {
		this.initKnowledgePoint = initKnowledgePoint;
	}

	public boolean isInitStudentQuestionCount() {
		return initStudentQuestionCount;
	}

	public void setInitStudentQuestionCount(boolean initStudentQuestionCount) {
		this.initStudentQuestionCount = initStudentQuestionCount;
	}

	public boolean isInitPublishCount() {
		return initPublishCount;
	}

	public void setInitPublishCount(boolean initPublishCount) {
		this.initPublishCount = initPublishCount;
	}

	public boolean isInitQuestionSimilarCount() {
		return initQuestionSimilarCount;
	}

	public void setInitQuestionSimilarCount(boolean initQuestionSimilarCount) {
		this.initQuestionSimilarCount = initQuestionSimilarCount;
	}

	public boolean isInitQuestionTag() {
		return initQuestionTag;
	}

	public void setInitQuestionTag(boolean initQuestionTag) {
		this.initQuestionTag = initQuestionTag;
	}
	
	public boolean isInitLatexImg() {
		return initLatexImg;
	}

	public void setInitLatexImg(boolean initLatexImg) {
		this.initLatexImg = initLatexImg;
	}
	
	public boolean isNewCorrect() {
		return newCorrect;
	}

	public void setNewCorrect(boolean newCorrect) {
		this.newCorrect = newCorrect;
	}

	public enum Type implements Valueable {
		NULL(0),
		/**
		 * 单选
		 */
		SINGLE_CHOICE(1),
		/**
		 * 多选
		 */
		MULTIPLE_CHOICE(2),
		/**
		 * 填空
		 */
		FILL_BLANK(3),
		/**
		 * 判断
		 */
		TRUE_OR_FALSE(4),
		/**
		 * 简答
		 */
		QUESTION_ANSWERING(5),
		/**
		 * 复合
		 */
		COMPOSITE(6);

		private int value;

		Type(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return this.value;
		}

		public String getName() {
			switch (value) {
			case 0:
				return "";
			case 1:
				return "单选";
			case 2:
				return "多选";
			case 3:
				return "填空";
			case 4:
				return "判断";
			case 5:
				return "简答";
			case 6:
				return "复合";
			default:
				return "";
			}
		}

		public static Type findByValue(int value) {
			switch (value) {
			case 0:
				return NULL;
			case 1:
				return SINGLE_CHOICE;
			case 2:
				return MULTIPLE_CHOICE;
			case 3:
				return FILL_BLANK;
			case 4:
				return TRUE_OR_FALSE;
			case 5:
				return QUESTION_ANSWERING;
			case 6:
				return COMPOSITE;
			default:
				return NULL;
			}
		}
	}
}
