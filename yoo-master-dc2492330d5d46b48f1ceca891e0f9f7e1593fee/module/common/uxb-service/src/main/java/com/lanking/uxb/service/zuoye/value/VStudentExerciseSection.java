package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.uxb.service.code.value.VSection;

/**
 * 章节学习情况统计
 *
 * @author xinyu.zhou
 * @since yoomath V1.6
 */
public class VStudentExerciseSection implements Serializable {
	private static final long serialVersionUID = -1983278958757558976L;
	private long id;
	private long doCount;
	private long wrongCount;
	// 掌握情况
	private double complete;
	// 掌握情况百分比
	private String completeTitle;
	// 对应的章节信息
	private VSection section;
	private Long lastdoCount = 0L;
	private Long lastWrongCount = 0L;
	// 上个月掌握情况
	private double lastMonthcomplete;

	private List<VStudentExerciseSection> children = Lists.newArrayList();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDoCount() {
		return doCount;
	}

	public void setDoCount(long doCount) {
		this.doCount = doCount;
	}

	public long getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(long wrongCount) {
		this.wrongCount = wrongCount;
	}

	public double getComplete() {
		return complete;
	}

	public void setComplete(double complete) {
		this.complete = complete;
	}

	public String getCompleteTitle() {
		return completeTitle;
	}

	public void setCompleteTitle(String completeTitle) {
		this.completeTitle = completeTitle;
	}

	public VSection getSection() {
		return section;
	}

	public void setSection(VSection section) {
		this.section = section;
	}

	public List<VStudentExerciseSection> getChildren() {
		return children;
	}

	public void setChildren(List<VStudentExerciseSection> children) {
		this.children = children;
	}

	public Long getLastdoCount() {
		return lastdoCount;
	}

	public void setLastdoCount(Long lastdoCount) {
		this.lastdoCount = lastdoCount;
	}

	public Long getLastWrongCount() {
		return lastWrongCount;
	}

	public void setLastWrongCount(Long lastWrongCount) {
		this.lastWrongCount = lastWrongCount;
	}

	public double getLastMonthcomplete() {
		return lastMonthcomplete;
	}

	public void setLastMonthcomplete(double lastMonthcomplete) {
		this.lastMonthcomplete = lastMonthcomplete;
	}

}
