package com.lanking.uxb.service.teachersDay01.value;

import java.io.Serializable;

/**
 * 教师节活动01VO
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年8月28日
 */
public class VTeachersDayActiviy01TeacherTag implements Serializable {

	private static final long serialVersionUID = 2802474318511567381L;

	private long id;
	// 标签代码
	private long codeTag;
	// 标签名称
	private String tagName;
	// 次数
	private int num;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCodeTag() {
		return codeTag;
	}

	public void setCodeTag(long codeTag) {
		this.codeTag = codeTag;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
