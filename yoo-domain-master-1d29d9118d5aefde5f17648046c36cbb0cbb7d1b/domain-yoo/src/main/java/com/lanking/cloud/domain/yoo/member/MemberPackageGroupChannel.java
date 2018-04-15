package com.lanking.cloud.domain.yoo.member;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 会员套餐组&渠道学校关系表
 * 
 * <pre>
 * 此表存储会员套餐可以使用的用户群。
 * 用户群：
 *     根据套餐组对应用户类型 {@link MemberPackageGroupType}，存储此表数据 
 *     1.所有渠道用户:如果套餐组对应所有渠道用户时,此表没有对应数据
 *     2.指定渠道用户:可以限定渠道关联的某个学校,如果不限制学校,school_id设置为0
 *     3.自主注册用户:user.user_channel_code = 10000的用户,如果套餐组对应自主注册用户时,此表没有对应数据
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "member_package_group_channel")
public class MemberPackageGroupChannel implements Serializable {

	private static final long serialVersionUID = -7995214310222937419L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 会员套餐组ID
	 */
	@Column(name = "member_package_group_id")
	private long memberPackageGroupId;

	/**
	 * 渠道CODE
	 */
	@Column(name = "user_channel_code", precision = 5)
	private int userChannelCode;

	/**
	 * 学校ID(当直接对应套餐组的情况下,此字段直接设置为0)
	 */
	@Column(name = "school_id")
	private long schoolId;

	/**
	 * 状态
	 */
	@Column(name = "status", precision = 3)
	private Status status = Status.ENABLED;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getMemberPackageGroupId() {
		return memberPackageGroupId;
	}

	public void setMemberPackageGroupId(long memberPackageGroupId) {
		this.memberPackageGroupId = memberPackageGroupId;
	}

	public int getUserChannelCode() {
		return userChannelCode;
	}

	public void setUserChannelCode(int userChannelCode) {
		this.userChannelCode = userChannelCode;
	}

	public long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
