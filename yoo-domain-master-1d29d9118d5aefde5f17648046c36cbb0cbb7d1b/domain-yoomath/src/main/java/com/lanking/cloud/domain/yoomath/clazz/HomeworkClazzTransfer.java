package com.lanking.cloud.domain.yoomath.clazz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 班级转让
 * 
 * <pre>
 * 1.触发转让后在此表中添加一条记录,此记录只作为一个log来使用,相关数据字段会在homework_class中冗余
 * </pre>
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月29日
 */
@Entity
@Table(name = "homework_class_transfer")
public class HomeworkClazzTransfer implements Serializable {

	private static final long serialVersionUID = -8587469063023828368L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 班级ID
	 */
	@Column(name = "homework_class_id")
	private Long homeworkClassId;

	/**
	 * from
	 */
	@Column(name = "from0")
	private long from;

	/**
	 * to
	 */
	@Column(name = "to0")
	private long to;

	/**
	 * from用户的开始时间
	 * 
	 * <pre>
	 * 1.如果是第一次转让,此时间则为班级的创建时间
	 * 2.非首次转让,此时间则为homework_class.start_at
	 * </pre>
	 */
	@Column(name = "start_at", columnDefinition = "datetime(3)")
	private Date startAt;

	/**
	 * from用户的结束时间
	 * 
	 * <pre>
	 * 1.当前时间
	 * </pre>
	 */
	@Column(name = "end_at", columnDefinition = "datetime(3)")
	private Date endAt;

	/**
	 * 转让时间
	 */
	@Column(name = "transfer_at", columnDefinition = "datetime(3)")
	private Date transferAt;

	/**
	 * 接收方收到消息的时间
	 */
	@Column(name = "read_at", columnDefinition = "datetime(3)")
	private Date readAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHomeworkClassId() {
		return homeworkClassId;
	}

	public void setHomeworkClassId(Long homeworkClassId) {
		this.homeworkClassId = homeworkClassId;
	}

	public long getFrom() {
		return from;
	}

	public void setFrom(long from) {
		this.from = from;
	}

	public long getTo() {
		return to;
	}

	public void setTo(long to) {
		this.to = to;
	}

	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Date getTransferAt() {
		return transferAt;
	}

	public void setTransferAt(Date transferAt) {
		this.transferAt = transferAt;
	}

	public Date getReadAt() {
		return readAt;
	}

	public void setReadAt(Date readAt) {
		this.readAt = readAt;
	}

}
