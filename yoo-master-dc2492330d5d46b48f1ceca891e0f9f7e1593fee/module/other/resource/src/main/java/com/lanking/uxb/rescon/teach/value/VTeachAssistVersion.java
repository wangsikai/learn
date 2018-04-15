package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.book.BookStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistStatus;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 教辅版本版本VO.
 * 
 * @author zemin.Song
 * 
 */
public class VTeachAssistVersion implements Serializable {

	private static final long serialVersionUID = 6997581039659689620L;

	private Long id;

	/**
	 * 关联教辅ID.
	 */
	private Long teachAssistId;

	/**
	 * 主版本标记. mainFlag 当前版本
	 */
	private boolean mainFlag;

	/**
	 * 封面. coverId
	 */
	private Long coverId;

	private String coverUrl;

	/**
	 * 教材名称. name
	 */
	private String name;

	/**
	 * 阶段.phaseCode
	 */
	private VPhase phase;

	/**
	 * 科目. subjectCode
	 */
	private String subjectName;

	/**
	 * 书本类型.
	 */
	private VResourceCategory resourceCategory;

	/**
	 * 教材版本. textbookCategoryCode
	 */
	private VTextbookCategory textbookCategory;

	/**
	 * 教材
	 */
	private VTextbook textbook;

	/**
	 * 顺序章节. sectionCodes
	 */
	private List<VSection> sections = Lists.newArrayList();

	/**
	 * 版本. version 版本号
	 */
	private Integer version;

	/**
	 * 版本状态.
	 */
	private TeachAssistStatus status;

	/**
	 * 版本描述.description
	 */
	private String description;

	/**
	 * 创建人.
	 */
	private String creator;

	/**
	 * 创建时间.
	 */
	private Date createAt;
	/**
	 * 学校名称<br>
	 * 没有学校，页面显示"通用教辅"
	 */
	private String schoolName;
	// 学校id add by xinyu.zhou
	private Long schoolId;

	private Integer phaseCode;

	private Integer subjectCode;

	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachAssistId() {
		return teachAssistId;
	}

	public void setTeachAssistId(Long teachAssistId) {
		this.teachAssistId = teachAssistId;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public VPhase getPhase() {
		return phase;
	}

	public void setPhase(VPhase phase) {
		this.phase = phase;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public VTextbookCategory getTextbookCategory() {
		return textbookCategory;
	}

	public void setTextbookCategory(VTextbookCategory textbookCategory) {
		this.textbookCategory = textbookCategory;
	}

	public VTextbook getTextbook() {
		return textbook;
	}

	public void setTextbook(VTextbook textbook) {
		this.textbook = textbook;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public TeachAssistStatus getStatus() {
		return status;
	}

	public void setStatus(TeachAssistStatus status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public List<VSection> getSections() {
		return sections;
	}

	public void setSections(List<VSection> sections) {
		this.sections = sections;
	}

	public boolean isMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(boolean mainFlag) {
		this.mainFlag = mainFlag;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public VResourceCategory getResourceCategory() {
		return resourceCategory;
	}

	public void setResourceCategory(VResourceCategory resourceCategory) {
		this.resourceCategory = resourceCategory;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Integer getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(Integer phaseCode) {
		this.phaseCode = phaseCode;
	}

	public Integer getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(Integer subjectCode) {
		this.subjectCode = subjectCode;
	}

}
