package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.base.message.api.MessageType;

/**
 * 科举考试消息发送时间表.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年4月7日
 */
public class ImperialExaminationMessageTime implements Serializable {
	private static final long serialVersionUID = -6391040619871875287L;

	/**
	 * 消息类型
	 * 
	 * @see MessageType
	 */
	private MessageType messageType;

	/**
	 * 消息模板代码
	 */
	private Integer messageTemplateCode;

	/**
	 * 发送时间.
	 */
	private Date startTime;

	/**
	 * 发送范围.
	 * 
	 * <pre>
	 * 1: 未使用过APP的渠道初中教师
	 * 2: 未报名的渠道初中教师
	 * 3：所有渠道初中教师
	 * 4：已报名的渠道初中教师
	 * 5：所有初中用户
	 * 6：所有初中老师
	 * 7：所有初中学生
	 * 8：未提交作业的学生
	 * TODO 由实现者继续添加类型
	 * </pre>
	 */
	private Integer userScope = 1;

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Integer getMessageTemplateCode() {
		return messageTemplateCode;
	}

	public void setMessageTemplateCode(Integer messageTemplateCode) {
		this.messageTemplateCode = messageTemplateCode;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getUserScope() {
		return userScope;
	}

	public void setUserScope(Integer userScope) {
		this.userScope = userScope;
	}

}
