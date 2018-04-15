package com.lanking.cloud.domain.yoo.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.frame.system.YooApp;

/**
 * banner
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "banner")
public class Banner implements Serializable {

	private static final long serialVersionUID = 2247323807191453815L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 图片ID
	 */
	@Column(name = "image_id")
	private Long imageId;

	/**
	 * 跳转资源URL
	 */
	@Column(name = "url", length = 500)
	private String url;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private Integer sequence;

	/**
	 * 所属APP,为空时表示为web端的
	 * 
	 * @see YooApp
	 */
	@Column(name = "app", precision = 3)
	private YooApp app;

	/**
	 * 位置
	 */
	@Column(name = "location", precision = 3, columnDefinition = "tinyint default 0")
	private BannerLocation location;

	/**
	 * 状态
	 * 
	 * @see BannerStatus
	 */
	@Column(name = "status", precision = 3, columnDefinition = "tinyint default 2")
	private BannerStatus status;

	/**
	 * 开始时间
	 */
	@Column(name = "start_at", columnDefinition = "datetime(3)")
	private Date startAt;

	/**
	 * 结束时间
	 */
	@Column(name = "end_at", columnDefinition = "datetime(3)")
	private Date endAt;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_id")
	private Long createId;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新人ID
	 */
	@Column(name = "update_id")
	private Long updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public YooApp getApp() {
		return app;
	}

	public void setApp(YooApp app) {
		this.app = app;
	}

	public BannerLocation getLocation() {
		return location;
	}

	public void setLocation(BannerLocation location) {
		this.location = location;
	}

	public BannerStatus getStatus() {
		return status;
	}

	public void setStatus(BannerStatus status) {
		this.status = status;
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

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
