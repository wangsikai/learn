package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.UserType;

import lombok.Getter;
import lombok.Setter;

/**
 * 习题申诉.
 * 
 * @since 小优快批
 *
 */
@Getter
@Setter
@Entity
@Table(name = "question_appeal")
public class QuestionAppeal implements Serializable {
	private static final long serialVersionUID = 434358061204299167L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 申诉类型.
	 */
	@Column(name = "appeal_type", precision = 3)
	private AppealType type;

	/**
	 * 习题来源.
	 */
	@Column(name = "question_source", precision = 3)
	private CorrectQuestionSource source;

	/**
	 * 业务ID，根据习题来源定义.
	 * <p>
	 * 例如作业，此处为student_homework_question_id
	 * </p>
	 */
	@Column(name = "biz_id")
	private Long bizId;

	/**
	 * 申诉时该题目批改结果正确率（例如50%，存储50）.
	 */
	@Column(name = "appeal_right_rate", precision = 5)
	private Integer appealRightRate;
	
	/**
	 * 申诉时该题目批改结果.
	 */
	@Column(name = "result", precision = 3)
	private HomeworkAnswerResult result;
	
	/**
	 * 申诉时该题目答案批改结果.
	 * 
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "item_results")
	private List<HomeworkAnswerResult> itemResults = Lists.newArrayList();
	
	/**
	 * 申诉时该题目的订正题批改结果正确率（例如50%，存储50）.
	 */
	@Column(name = "correct_appeal_right_rate", precision = 5)
	private Integer correctAppealRightRate;
	
	/**
	 * 申诉时该题目的订正题批改结果.
	 */
	@Column(name = "correct_result", precision = 3)
	private HomeworkAnswerResult correctResult;
	
	/**
	 * 申诉时该题目的订正题答案批改结果.
	 * 
	 */
	@Type(type = JSONType.TYPE)
	@Column(length = 128, name = "correct_item_results")
	private List<HomeworkAnswerResult> correctItemResults = Lists.newArrayList();

	/**
	 * 申诉时该题目批改人（为空时表示非人工批改）.
	 */
	@Column(name = "appeal_correct_user_id")
	private Long appealCorrectUserId;
	
	/**
	 * 习题的批改方式
	 * 
	 * @since 小优快批
	 */
	@Column(name = "correct_type", precision = 3)
	private QuestionCorrectType correctType = QuestionCorrectType.DEFAULT;


	/**
	 * 申诉留言.
	 */
	@Column(name = "comment", length = 500)
	private String comment;

	/**
	 * 申诉时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 申诉人.
	 */
	@Column(name = "creator")
	private Long creator;

	/**
	 * 申诉人身份.
	 */
	@Column(name = "user_type", precision = 3, nullable = false)
	private UserType userType;

	/**
	 * 申诉状态.
	 */
	@Column(name = "status", precision = 3)
	private QuestionAppealStatus status = QuestionAppealStatus.INIT;

	/**
	 * 发放金币数.
	 */
	@Column(name = "coins")
	private Integer coins;
}
