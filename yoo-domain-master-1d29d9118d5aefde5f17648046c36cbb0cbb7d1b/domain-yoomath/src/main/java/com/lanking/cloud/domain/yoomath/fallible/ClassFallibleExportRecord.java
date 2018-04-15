package com.lanking.cloud.domain.yoomath.fallible;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

import lombok.Getter;
import lombok.Setter;

/**
 * 班级导出错题记录.
 * 
 * @author wlche
 *
 */
@Getter
@Setter
@Entity
@Table(name = "class_fallible_export_record")
public class ClassFallibleExportRecord implements Serializable {
	private static final long serialVersionUID = -3724661818450848964L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private Long classId;

	/**
	 * 是否是班级全部同学
	 */
	@Column(name = "all_student")
	private boolean allStudent;

	/**
	 * 所有学生的IDs（存储字符串为List<Long>转json字符串）
	 * 
	 * <pre>
	 * 这个字段不管all_student是否为true都需要存储，因为班级学生是动态变动的
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "all_student_id_list")
	private String allStudentIdList;

	/**
	 * 导出学生的IDs（存储字符串为List<Long>转json字符串）
	 * 
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "student_id_list")
	private String studentIdList;
	
	/**
	 * 导出没有错题记录的学生的名字（存储字符串为List<String>转json字符串）
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "no_question_student_name_list")
	private String nqStudentNameList;

	/**
	 * 章节CODE集合
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "section_codes")
	private String sectionCodes;

	/**
	 * 下载次数，默认为null,消息已读后设置为0
	 */
	@Column(name = "download_count")
	private Integer downloadCount;

	/**
	 * 操作人的ID
	 */
	@Column(name = "operator")
	private long operator;

	/**
	 * 创建时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 数据状态
	 */
	@Column(name = "status", precision = 3)
	private ClassFallibleExportRecordStatus status = ClassFallibleExportRecordStatus.INIT;
	
	/**
	 * 名称
	 */
	@Column(name = "name", length = 100)
	private String name;
}
