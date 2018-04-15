package com.lanking.uxb.service.zuoye.value;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * 掌握情况知识点
 *
 * @author xinyu.zhou
 * @since yoomath V1.6
 */
public class VStudentExerciseKnowpoint implements Serializable {
	private static final long serialVersionUID = 5354030869545157987L;

	private int code;
	private int subjectCode;
	private String name;
	private long doCount;
	private long wrongCount;
	private double rightRate;
	private String rightRateTitle;
	private long id;
	private long studentId;
	private List<Integer> maps = Lists.newArrayList();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getSubjectCode() {
		return subjectCode;
	}

	public void setSubjectCode(int subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getRightRate() {
		return rightRate;
	}

	public void setRightRate(double rightRate) {
		this.rightRate = rightRate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getRightRateTitle() {
		return rightRateTitle;
	}

	public void setRightRateTitle(String rightRateTitle) {
		this.rightRateTitle = rightRateTitle;
	}

	public List<Integer> getMaps() {
		return maps;
	}

	public void setMaps(List<Integer> maps) {
		this.maps = maps;
	}
}
