package com.lanking.cloud.domain.yoomath.homework;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 作业元知识点
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "homework_metaknow")
public class HomeworkMetaKnow implements Serializable {

	private static final long serialVersionUID = 305130353956951969L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 作业ID
	 */
	@Column(name = "homework_id")
	private long homeworkId;

	/**
	 * 元知识点代码
	 */
	@Column(name = "meta_code")
	private long metaCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public long getMetaCode() {
		return metaCode;
	}

	public void setMetaCode(long metaCode) {
		this.metaCode = metaCode;
	}

}
