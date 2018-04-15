package com.lanking.uxb.service.resources.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.yoomath.homework.HomeworkMessageType;

/**
 * 作业留言VO
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2018年2月9日
 */
public class VHomeworkMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8009794876434808620L;
	
	/**
	 * 留言id.
	 */
	private Long id;

	/**
	 * 留言类型.
	 */
	private HomeworkMessageType type;

	/**
	 * 文本留言.
	 */
	private String comment;

	/**
	 * 语音留言时间，为0时表示没有语音
	 */
	private Integer voiceTime;

	/**
	 * 语音文件存储的url
	 */
	private String voiceUrl;
	
	/**
	 * 留言图标Key值
	 */
	private String iconKey;

	/**
	 * 留言时间.
	 */
	private Date createAt;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HomeworkMessageType getType() {
		return type;
	}

	public void setType(HomeworkMessageType type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getVoiceTime() {
		return voiceTime;
	}

	public void setVoiceTime(Integer voiceTime) {
		this.voiceTime = voiceTime;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getIconKey() {
		return iconKey;
	}

	public void setIconKey(String iconKey) {
		this.iconKey = iconKey;
	}

}
