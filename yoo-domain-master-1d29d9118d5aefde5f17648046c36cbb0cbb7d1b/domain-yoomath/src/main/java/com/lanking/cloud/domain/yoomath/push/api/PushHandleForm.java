package com.lanking.cloud.domain.yoomath.push.api;

import java.io.Serializable;
import java.util.Collection;

/**
 * 推送参数
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
public class PushHandleForm implements Serializable {
	private static final long serialVersionUID = -5918079814659372495L;
	/**
	 * 推送动作类型
	 */
	private PushHandleAction action;
	/**
	 * 作业ID
	 */
	private long homeworkId;
	/**
	 * 学生IDs
	 */
	private Collection<Long> studentIds;

	public PushHandleForm() {
		super();
	}

	public PushHandleForm(PushHandleAction action, long homeworkId) {
		super();
		this.action = action;
		this.homeworkId = homeworkId;
	}

	public PushHandleForm(PushHandleAction action, long homeworkId, Collection<Long> studentIds) {
		super();
		this.action = action;
		this.homeworkId = homeworkId;
		this.studentIds = studentIds;
	}

	public PushHandleAction getAction() {
		return action;
	}

	public void setAction(PushHandleAction action) {
		this.action = action;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public Collection<Long> getStudentIds() {
		return studentIds;
	}

	public void setStudentIds(Collection<Long> studentIds) {
		this.studentIds = studentIds;
	}

}
