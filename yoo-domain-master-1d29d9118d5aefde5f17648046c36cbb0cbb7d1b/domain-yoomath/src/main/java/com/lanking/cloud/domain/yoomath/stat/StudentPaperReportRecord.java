package com.lanking.cloud.domain.yoomath.stat;

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
 * 学生纸质报告记录
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年9月25日
 */
@Getter
@Setter
@Entity
@Table(name = "student_paper_report_record")
public class StudentPaperReportRecord implements Serializable {

	private static final long serialVersionUID = 6180362970757774023L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 操作人的ID
	 */
	@Column(name = "operator")
	private long operator;

	/**
	 * 班级ID
	 */
	@Column(name = "class_id")
	private long classId;

	/**
	 * 开始时间(精度是年月日)
	 */
	@Column(name = "start_date", columnDefinition = "date")
	private Date startDate;

	/**
	 * 结束时间
	 */
	@Column(name = "end_date", columnDefinition = "date")
	private Date endDate;

	/**
	 * 是否是班级全部同学
	 */
	@Column(name = "all_student")
	private boolean allStudent;

	/**
	 * 学生IDs（存储字符串为List<Long>转json字符串）
	 * 
	 * <pre>
	 * 这个字段不管all_student是否为true都需要存储，因为班级学生是动态变动的
	 * </pre>
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column(name = "student_id_list")
	private String studentIdList;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private StudentPaperReportStatus status;

	/**
	 * 下载次数，默认为null,消息已读后设置为0
	 */
	@Column(name = "download_count")
	private Integer downloadCount;

	/**
	 * 创建时间.
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;
}
