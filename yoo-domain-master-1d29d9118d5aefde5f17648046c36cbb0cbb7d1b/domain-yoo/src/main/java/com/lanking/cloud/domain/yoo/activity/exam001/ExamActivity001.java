package com.lanking.cloud.domain.yoo.activity.exam001;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.lanking.cloud.component.db.support.hibernate.type.JSONType;

import lombok.Getter;
import lombok.Setter;

/**
 * 考试活动
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
@Table(name = "exam_activity_001")
public class ExamActivity001 implements Serializable {

	private static final long serialVersionUID = 6399544489235723831L;

	@Id
	@Column(name = "code")
	private Long code;

	@Column(name = "name", length = 100)
	private String name;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 活动开始时间（包含）
	 */
	@Column(name = "start_time", columnDefinition = "datetime(3)")
	private Date startTime;

	/**
	 * 活动结束时间（包含）
	 */
	@Column(name = "end_time", columnDefinition = "datetime(3)")
	private Date endTime;

	/**
	 * 活动配置(包括配置) {@link ExamActivity001Cfg}
	 */
	@Type(type = JSONType.TYPE)
	@Column(name = "cfg", length = 60000)
	private ExamActivity001Cfg cfg;

}
