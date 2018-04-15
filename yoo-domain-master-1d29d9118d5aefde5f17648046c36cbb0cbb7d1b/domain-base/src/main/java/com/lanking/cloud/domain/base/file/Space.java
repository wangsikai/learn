package com.lanking.cloud.domain.base.file;

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
 * 文件存储空间
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "file_space")
public class Space implements Serializable {
	private static final long serialVersionUID = -7758339752353581908L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 名称
	 */
	@Column(name = "name", unique = true)
	private String name;

	/**
	 * 文件数量
	 */
	@Column(name = "file_count")
	private long fileCount;

	/**
	 * 单个文件大小限制(单位：字节)
	 */
	@Column(name = "max_file_size")
	private long maxFileSize;

	/**
	 * 空间大小限制(单位：字节)
	 */
	@Column(name = "size")
	private long size;

	/**
	 * 已存储文件大小(单位：字节)
	 */
	@Column(name = "used_size")
	private long usedSize;

	/**
	 * 是否要认证权限
	 * 
	 * <pre>
	 *    true(db存储为1):需要登录才能上传文件到此空间
	 *    false(db存储为0):无需登录
	 * </pre>
	 */
	@Column(name = "authorization")
	private boolean authorization;

	/**
	 * 是否支持预览
	 */
	@Column(name = "preview")
	private boolean preview;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFileCount() {
		return fileCount;
	}

	public void setFileCount(long fileCount) {
		this.fileCount = fileCount;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(long usedSize) {
		this.usedSize = usedSize;
	}

	public boolean isAuthorization() {
		return authorization;
	}

	public void setAuthorization(boolean authorization) {
		this.authorization = authorization;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
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
