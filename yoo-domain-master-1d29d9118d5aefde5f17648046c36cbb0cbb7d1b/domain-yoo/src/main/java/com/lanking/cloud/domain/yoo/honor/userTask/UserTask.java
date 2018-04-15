package com.lanking.cloud.domain.yoo.honor.userTask;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户任务
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月2日
 */
@Entity
@Table(name = "user_task")
public class UserTask extends UserTaskBaseInfo {

	private static final long serialVersionUID = 814384365464705918L;

	/**
	 * 代码 ({1}{产品XX}{任务类型XX}{XXXX})
	 * 
	 * <pre>
	 * 如：悠数学里面的某个新用户任务101000001,目前后台不支持动态添加任务,但是需要支持任务参数动态配置
	 * </pre>
	 */
	@Id
	@Column(name = "code", length = 10)
	private int code;

	/**
	 * 对应最新快照ID
	 */
	@Column(name = "snapshot_id")
	private long snapshotId;

	@Transient
	private boolean initUserTaskLog = false;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public long getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(long snapshotId) {
		this.snapshotId = snapshotId;
	}

	public boolean isInitUserTaskLog() {
		return initUserTaskLog;
	}

	public void setInitUserTaskLog(boolean initUserTaskLog) {
		this.initUserTaskLog = initUserTaskLog;
	}
}