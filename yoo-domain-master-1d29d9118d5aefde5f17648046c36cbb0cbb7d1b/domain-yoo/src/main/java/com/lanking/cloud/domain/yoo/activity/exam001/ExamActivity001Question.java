package com.lanking.cloud.domain.yoo.activity.exam001;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.type.JSONType;

import lombok.Getter;
import lombok.Setter;

/**
 * 考试活动试卷题目
 * 
 * <pre>
 * 2017.12.26期末考试活动
 * </pre>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年12月26日
 */
@Setter
@Getter
@Entity
@Table(name = "exam_activity_001_question")
public class ExamActivity001Question implements Serializable {

	private static final long serialVersionUID = 1996134198635574311L;

	@Id
	@Column(name = "code")
	private Long code;

	@Column(name = "name", length = 128)
	private String name;

	@Column(name = "type", precision = 3)
	private com.lanking.cloud.domain.common.resource.question.Question.Type type;

	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	@Column(name = "grade")
	private Integer grade;

	@Column(name = "difficulty", scale = 2)
	private BigDecimal difficulty;

	@Column(name = "question_count", precision = 3)
	private Integer questionCount;

	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "question_list")
	private List<Long> questionList = Lists.newArrayList();

}
