package com.lanking.cloud.domain.yoo.honor.userTask;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 用户任务快照
 * 
 * <pre>
 * 1.创建/修改用户任务的时同步创建快照,快照的最新一条和用户任务表里面的一致
 * 2.创建更新信息存储在快照表中
 * </pre>
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
@Entity
@Table(name = "user_task_snapshot")
public class UserTaskSnapshot extends UserTaskBaseInfo {

	private static final long serialVersionUID = 814384365464705918L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 代码({1}{产品XX}{任务类型XX}{XXXX})
	 * 
	 * <pre>
	 * 如：悠数学里面的某个新用户任务 101000001,目前后台不支持动态添加任务,但是需要支持任务参数动态配置
	 * </pre>
	 */
	@Column(name = "code", length = 10)
	private int code;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private Long updateId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

}