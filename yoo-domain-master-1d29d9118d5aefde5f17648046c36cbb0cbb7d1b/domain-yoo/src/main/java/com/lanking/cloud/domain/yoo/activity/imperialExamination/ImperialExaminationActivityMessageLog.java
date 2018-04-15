package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.base.message.api.MessageType;

import lombok.Getter;
import lombok.Setter;

/**
 * 消息处理记录
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_message_log")
public class ImperialExaminationActivityMessageLog implements Serializable {
	private static final long serialVersionUID = -3688749384595659921L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 活动代码.
	 */
	@Column(name = "activity_code", length = 32)
	private Long activityCode;

	/**
	 * 消息类型
	 * 
	 * @see MessageType
	 */
	@Column(name = "message_type", precision = 3)
	private MessageType messageType;

	/**
	 * 信息模板代码.
	 */
	@Column(name = "template_code", length = 16)
	private Integer messageTemplateCode;

	/**
	 * 处理时间.
	 */
	@Column(name = "create_time", columnDefinition = "datetime(3)")
	private Date createTime;

	/**
	 * 处理是否成功
	 * 
	 * <pre>
	 * true|1:成功
	 * false|0:失败
	 * null:未知
	 * </pre>
	 */
	@Column(name = "success")
	private Boolean success;

}
