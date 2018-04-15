package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.LongTypeList;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教辅版本
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_version")
@TypeDef(name = "list", defaultForType = List.class, typeClass = LongTypeList.class)
public class TeachAssistVersion implements Serializable {

	private static final long serialVersionUID = 5928120848319239256L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 128)
	private String name;

	/**
	 * 封面ID
	 */
	@Column(name = "cover_id")
	private Long coverId;

	/**
	 * 描述
	 */
	@Column(name = "description", length = 512)
	private String description;

	/**
	 * 阶段
	 */
	@Column(name = "phase_code")
	private Integer phaseCode;

	/**
	 * 学科
	 */
	@Column(name = "subject_code")
	private Integer subjectCode;

	/**
	 * 版本
	 */
	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 教材
	 */
	@Column(name = "textbook_code")
	private Integer textbookCode;

	/**
	 * 直接对应章节
	 */
	@Column(name = "section_code")
	private Long sectionCode;

	/**
	 * 顺序章节（展示）
	 */
	@Type(type = "list")
	@Column(name = "section_codes", length = 100)
	private List<Long> sectionCodes;

	/**
	 * 学校ID,非学校教辅则此字段需要设置为0
	 */
	@Column(name = "school_id")
	private long schoolId = 0;

	/**
	 * 教辅ID
	 */
	@Column(name = "teachassist_id")
	private long teachassistId;

	/**
	 * 当前主版本
	 */
	@Column(name = "main_flag")
	private boolean mainFlag = true;

	/**
	 * 版本号
	 */
	@Column(name = "version", precision = 3)
	private int version = 1;

	/**
	 * 删除状态
	 */
	@Column(name = "del_status", precision = 3, nullable = false)
	private Status delStatus = Status.ENABLED;

	/**
	 * 教辅状态
	 * 
	 * @see TeachAssistStatus
	 */
	@Column(name = "status", precision = 3)
	private TeachAssistStatus teachAssistStatus = TeachAssistStatus.EDITING;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCoverId() {
		return coverId;
	}

	public void setCoverId(Long coverId) {
		this.coverId = coverId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getTextbookCategoryCode() {
		return textbookCategoryCode;
	}

	public void setTextbookCategoryCode(Integer textbookCategoryCode) {
		this.textbookCategoryCode = textbookCategoryCode;
	}

	public Integer getTextbookCode() {
		return textbookCode;
	}

	public void setTextbookCode(Integer textbookCode) {
		this.textbookCode = textbookCode;
	}

	public Long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(Long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public List<Long> getSectionCodes() {
		return sectionCodes;
	}

	public void setSectionCodes(List<Long> sectionCodes) {
		this.sectionCodes = sectionCodes;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public long getTeachassistId() {
		return teachassistId;
	}

	public void setTeachassistId(long teachassistId) {
		this.teachassistId = teachassistId;
	}

	public boolean isMainFlag() {
		return mainFlag;
	}

	public void setMainFlag(boolean mainFlag) {
		this.mainFlag = mainFlag;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Status getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Status delStatus) {
		this.delStatus = delStatus;
	}

	public TeachAssistStatus getTeachAssistStatus() {
		return teachAssistStatus;
	}

	public void setTeachAssistStatus(TeachAssistStatus teachAssistStatus) {
		this.teachAssistStatus = teachAssistStatus;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
