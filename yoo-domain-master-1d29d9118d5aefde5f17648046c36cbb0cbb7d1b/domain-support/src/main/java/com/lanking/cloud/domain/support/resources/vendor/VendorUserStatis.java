package com.lanking.cloud.domain.support.resources.vendor;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 供应商用户相关统计信息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "vendor_user_statis")
public class VendorUserStatis implements Serializable {

	private static final long serialVersionUID = -8368935889983975291L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 供应商用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 录入题目的数量
	 */
	@Column(name = "build_q_count")
	private Long buildQCount = 0L;

	/**
	 * 一校通过的数量
	 */
	@Column(name = "pass_step1_q_count")
	private Long passStep1QCount = 0L;

	/**
	 * 已通过题目的数量
	 */
	@Column(name = "pass_q_count")
	private Long passQCount = 0L;

	/**
	 * 未校验题目的数量(录入的题目-已通过-不通过)
	 */
	@Transient
	private Long nocheckQCount = 0L;

	/**
	 * 未通过题目的数量
	 */
	@Column(name = "nopass_q_count")
	private Long nopassQCount = 0L;

	/**
	 * 存为草稿题目的数量
	 */
	@Column(name = "draft_q_count")
	private Long draftQCount = 0L;

	/**
	 * 录入员被修改的次数
	 */
	@Column(name = "build_modify_count")
	private Long buildModifyCount = 0L;

	/**
	 * 执行校验的次数
	 */
	@Column(name = "check_check_count")
	private Long checkCheckCount = 0L;

	/**
	 * 执行一校的次数
	 */
	@Column(name = "check_check1_count", columnDefinition = "bigint default 0")
	private Long checkCheck1Count = 0L;

	/**
	 * 执行二校的次数
	 */
	@Column(name = "check_check2_count", columnDefinition = "bigint default 0")
	private Long checkCheck2Count = 0L;

	/**
	 * 校验员修改的次数
	 */
	@Column(name = "check_modify_count")
	private Long checkModifyCount = 0L;

	/**
	 * 类型
	 */
	@Column(name = "type", precision = 3)
	private VendorUserStatisType type;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	private long defaultValue(Long value) {
		return value == null ? 0 : value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBuildQCount() {
		return buildQCount;
	}

	public void setBuildQCount(Long buildQCount) {
		this.buildQCount = defaultValue(buildQCount);
	}

	public Long getPassStep1QCount() {
		return passStep1QCount;
	}

	public void setPassStep1QCount(Long passStep1QCount) {
		this.passStep1QCount = defaultValue(passStep1QCount);
	}

	public Long getPassQCount() {
		return passQCount;
	}

	public void setPassQCount(Long passQCount) {
		this.passQCount = defaultValue(passQCount);
	}

	public Long getNocheckQCount() {
		setNocheckQCount(getBuildQCount() - getPassQCount() - getNopassQCount());
		return nocheckQCount;
	}

	public void setNocheckQCount(Long nocheckQCount) {
		this.nocheckQCount = defaultValue(nocheckQCount);
	}

	public Long getNopassQCount() {
		return nopassQCount;
	}

	public void setNopassQCount(Long nopassQCount) {
		this.nopassQCount = defaultValue(nopassQCount);
	}

	public Long getDraftQCount() {
		return draftQCount;
	}

	public void setDraftQCount(Long draftQCount) {
		this.draftQCount = defaultValue(draftQCount);
	}

	public Long getBuildModifyCount() {
		return buildModifyCount;
	}

	public void setBuildModifyCount(Long buildModifyCount) {
		this.buildModifyCount = defaultValue(buildModifyCount);
	}

	public Long getCheckCheckCount() {
		return checkCheckCount;
	}

	public void setCheckCheckCount(Long checkCheckCount) {
		this.checkCheckCount = defaultValue(checkCheckCount);
	}

	public Long getCheckCheck1Count() {
		return checkCheck1Count;
	}

	public void setCheckCheck1Count(Long checkCheck1Count) {
		this.checkCheck1Count = checkCheck1Count;
	}

	public Long getCheckCheck2Count() {
		return checkCheck2Count;
	}

	public void setCheckCheck2Count(Long checkCheck2Count) {
		this.checkCheck2Count = checkCheck2Count;
	}

	public Long getCheckModifyCount() {
		return checkModifyCount;
	}

	public void setCheckModifyCount(Long checkModifyCount) {
		this.checkModifyCount = defaultValue(checkModifyCount);
	}

	public VendorUserStatisType getType() {
		return type;
	}

	public void setType(VendorUserStatisType type) {
		this.type = type;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
