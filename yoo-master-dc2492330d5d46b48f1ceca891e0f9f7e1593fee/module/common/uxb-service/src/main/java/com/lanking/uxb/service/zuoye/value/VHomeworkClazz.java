package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.ClazzFrom;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.user.value.VUser;

/**
 * 作业班级VO
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月23日
 */
public class VHomeworkClazz implements Serializable {

	private static final long serialVersionUID = 885156710636038462L;

	private long id;
	private String name;
	private String code;
	private String description;
	private Long teacherId;
	private VUser teacher;
	private Date createAt;
	private Date closeAt;
	private Date deleteAt;
	private Status status = Status.ENABLED;
	private Status lockStatus = Status.ENABLED;
	private int studentNum = 0;
	private VHomeworkStat homeworkStat;
	private ClazzFrom clazzFrom;
	private String fromCode;
	// 是否需要用户进行确认 @since 3.0.2
	private boolean needConfirm;

	/**
	 * @since 2.0.3
	 */
	private Long bookVersionId;
	/**
	 * @since 2.0.3
	 */
	private Long bookCataId;
	private List<VBookCatalog> recommendCatalogs;
	// 当前进度所处的根目录用于显示大的进度
	private VBookCatalog levelOneCatalog;

	// @since 3.0.2 学生是否加入此班级
	private boolean joined = false;

	// @since 教师端1.3.0 是否选中班级
	private boolean selected = false;

	// 2017-11-8 新增分组
	private List<VHomeworkClazzGroup> groupList;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code.toLowerCase();
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

	public VUser getTeacher() {
		return teacher;
	}

	public void setTeacher(VUser teacher) {
		this.teacher = teacher;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
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

	public VHomeworkStat getHomeworkStat() {
		return homeworkStat;
	}

	public void setHomeworkStat(VHomeworkStat homeworkStat) {
		this.homeworkStat = homeworkStat;
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

	public List<VBookCatalog> getRecommendCatalogs() {
		return recommendCatalogs;
	}

	public void setRecommendCatalogs(List<VBookCatalog> recommendCatalogs) {
		this.recommendCatalogs = recommendCatalogs;
	}

	public VBookCatalog getLevelOneCatalog() {
		return levelOneCatalog;
	}

	public void setLevelOneCatalog(VBookCatalog levelOneCatalog) {
		this.levelOneCatalog = levelOneCatalog;
	}

	public boolean isNeedConfirm() {
		return needConfirm;
	}

	public void setNeedConfirm(boolean needConfirm) {
		this.needConfirm = needConfirm;
	}

	public boolean isJoined() {
		return joined;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public List<VHomeworkClazzGroup> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<VHomeworkClazzGroup> groupList) {
		this.groupList = groupList;
	}

}
