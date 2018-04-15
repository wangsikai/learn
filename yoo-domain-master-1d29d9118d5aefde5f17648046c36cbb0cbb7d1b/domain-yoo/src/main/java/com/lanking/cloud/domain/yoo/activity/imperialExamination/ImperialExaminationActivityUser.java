package com.lanking.cloud.domain.yoo.activity.imperialExamination;

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

import lombok.Getter;
import lombok.Setter;

/**
 * 科举考试参与人员
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
@Entity
@Table(name = "imperial_exam_activity_user")
public class ImperialExaminationActivityUser implements Serializable {

	private static final long serialVersionUID = -6262796843563410189L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	@Column(name = "activity_code")
	private long activityCode;

	@Column(name = "grade", precision = 3)
	private ImperialExaminationGrade grade;

	@Column(name = "textbook_category_code")
	private Integer textbookCategoryCode;

	/**
	 * 考场
	 */
	@Column(name = "room", precision = 3)
	private Integer room;

	@Column(name = "user_id")
	private long userId;

	@Column(name = "name", length = 64)
	private String name;

	@Column(name = "mobile", length = 20)
	private String mobile;

	@Type(type = JSONType.TYPE)
	@Column(name = "class_list", length = 1000)
	private List<Long> classList;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

}
