package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 作业留言.
 * 
 * @since 小优快批
 *
 */
@Entity
@Getter
@Setter
@Table(name = "homework_message")
public class HomeworkMessage implements Serializable {
	private static final long serialVersionUID = -2325820639608591097L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 留言场景.
	 */
	@Column(name = "scene", precision = 3)
	private HomeworkMessageScene scene;

	/**
	 * 留言类型.
	 */
	@Column(name = "message_type", precision = 3)
	private HomeworkMessageType type;

	/**
	 * 不同留言类型对应的业务数据ID.
	 * <p>
	 * 针对学生单个习题：student_homework_question_id <br>
	 * 针对学生单个作业：student_homework_id <br>
	 * 针对整份作业：homework_id <br>
	 * </p>
	 */
	@Column(name = "biz_id")
	private Long bizId;

	/**
	 * 文本留言.
	 */
	@Column(name = "comment", length = 500)
	private String comment;

	/**
	 * 语音留言时间，为0时表示没有语音
	 */
	@Column(name = "voice_time")
	private Integer voiceTime;

	/**
	 * 文件存储hash key值,关联于七牛存储
	 */
	@Column(name = "voice_file_key")
	private String voiceFileKey;

	/**
	 * 留言图标Key值
	 */
	@Column(name = "icon_key", length = 20)
	private String iconKey;

	/**
	 * 留言时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 留言人.
	 */
	@Column(name = "creator")
	private Long creator;

	/**
	 * 留言人身份.
	 */
	@Column(name = "user_type", precision = 3, nullable = false)
	private UserType userType;

	/**
	 * 留言状态.
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;
}
