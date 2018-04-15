package com.lanking.cloud.domain.common.resource.question;

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

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;
import com.lanking.cloud.sdk.bean.Status;

import lombok.Getter;
import lombok.Setter;

/**
 * 相似题组
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Getter
@Setter
@Entity
@Table(name = "question_similar")
public class QuestionSimilar implements Serializable {
	private static final long serialVersionUID = -2353760444775165211L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 基准题目ID
	 */
	@Column(name = "base_question_id")
	private Long baseQuestionId;

	/**
	 * 相似题组ID排序字串后的MD5值
	 */
	@Column(name = "md5")
	private String md5;

	/**
	 * 相似题组集合字串
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "questions", length = 4000)
	private List<Long> likeQuestions;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 相似题组状态（仅剩一道题时失效）.
	 */
	@Column(name = "status", precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	/**
	 * 供应商ID
	 */
	@Column(name = "vendor_id")
	private Long vendorId;

	/**
	 * 转换标记.
	 */
	@Column(name = "change_flag")
	private boolean changeFlag;

	/**
	 * 前台使用的相似题数量（仅包含单选题、填空题、简答题）
	 * 
	 * @since 2017-09-23
	 */
	@Column(name = "show_count", precision = 3)
	private int showCount;
}
