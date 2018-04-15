package com.lanking.cloud.domain.yoomath.clazz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 班级
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "homework_class")
public class HomeworkClazz implements Serializable {

	private static final long serialVersionUID = -5465614323210990927L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;

	/**
	 * 班级代码
	 */
	@Column(name = "code", length = 32)
	private String code;

	/**
	 * 描述
	 */
	@Column(name = "description")
	private String description;

	/**
	 * 教师ID
	 */
	@Column(name = "teacher_id")
	private Long teacherId;

	/**
	 * 原始创建人
	 */
	@Column(name = "original_teacher_id")
	private Long originalTeacherId;

	/**
	 * teacher_id用户管理此班级的开始时间
	 */
	@Column(name = "start_at", columnDefinition = "datetime(3)")
	private Date startAt;

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
	 * 关闭时间
	 */
	@Column(name = "close_at", columnDefinition = "datetime(3)")
	private Date closeAt;

	/**
	 * 删除时间
	 */
	@Column(name = "delete_at", columnDefinition = "datetime(3)")
	private Date deleteAt;

	/**
	 * 班级状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	/**
	 * 班级锁状态(ENABLED表示开放状态)
	 */
	@Column(name = "lock_status", precision = 3, columnDefinition = "tinyint default 0")
	private Status lockStatus = Status.ENABLED;

	/**
	 * 学生数量
	 */
	@Column(name = "student_num", precision = 10, columnDefinition = "int default 0")
	private int studentNum = 0;

	/**
	 * 班级来源
	 */
	@Column(name = "clazz_from", precision = 3, columnDefinition = "tinyint default 0")
	private ClazzFrom clazzFrom = ClazzFrom.DEFAULT;

	/**
	 * 第三方来源编码
	 */
	@Column(name = "from_code", length = 60)
	private String fromCode;

	/**
	 * 班级选定的书本(此字段有值表示此班级已经设置过推荐作业的进度)
	 */
	@Column(name = "book_version_id")
	private Long bookVersionId;

	/**
	 * 班级书本进度章节id(此字段为-1表示此教辅推荐作业已经结束)
	 */
	@Column(name = "book_cata_id")
	private Long bookCataId;

	/**
	 * 加入班级是否需要老师进行确认
	 */
	@Column(name = "need_confirm", columnDefinition = "bit default 0")
	private boolean needConfirm = false;

	/**
	 * 入学年份
	 */
	@Column(name = "school_year", precision = 5)
	private Integer schoolYear;

	@Transient
	private boolean initTeacher = false;
	@Transient
	private boolean initStat = false;
	@Transient
	private boolean initClassGroup = false;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public Long getOriginalTeacherId() {
		return originalTeacherId;
	}

	public void setOriginalTeacherId(Long originalTeacherId) {
		this.originalTeacherId = originalTeacherId;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
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

	public Date getCloseAt() {
		return closeAt;
	}

	public void setCloseAt(Date closeAt) {
		this.closeAt = closeAt;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Status lockStatus) {
		this.lockStatus = lockStatus;
	}

	public int getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(int studentNum) {
		this.studentNum = studentNum;
	}

	public ClazzFrom getClazzFrom() {
		return clazzFrom;
	}

	public void setClazzFrom(ClazzFrom clazzFrom) {
		this.clazzFrom = clazzFrom;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public Long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(Long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public Long getBookCataId() {
		return bookCataId;
	}

	public void setBookCataId(Long bookCataId) {
		this.bookCataId = bookCataId;
	}

	public boolean isNeedConfirm() {
		return needConfirm;
	}

	public void setNeedConfirm(boolean needConfirm) {
		this.needConfirm = needConfirm;
	}

	public Integer getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(Integer schoolYear) {
		this.schoolYear = schoolYear;
	}

	public boolean isInitTeacher() {
		return initTeacher;
	}

	public void setInitTeacher(boolean initTeacher) {
		this.initTeacher = initTeacher;
	}

	public boolean isInitStat() {
		return initStat;
	}

	public void setInitStat(boolean initStat) {
		this.initStat = initStat;
	}

	public boolean isInitClassGroup() {
		return initClassGroup;
	}

	public void setInitClassGroup(boolean initClassGroup) {
		this.initClassGroup = initClassGroup;
	}

}
