package com.lanking.cloud.domain.yoo.activity.holiday002;

import java.io.Serializable;
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

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "holiday_activity_02_question")
public class HolidayActivity02Question implements Serializable {

	private static final long serialVersionUID = 8285489313660911185L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	/**
	 * {@link HolidayActivity02PKRecord.id}
	 */
	@Column(name = "pk_record_id")
	private Long pkRecordId;

	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	@Column(name = "textbook_code")
	private Integer textbookCode;

	@Column(name = "question_count", precision = 3)
	private Integer questionCount;

	@Type(type = JSONType.TYPE)
	@Column(length = 4000, name = "question_list")
	private List<Long> questionList = Lists.newArrayList();

}
