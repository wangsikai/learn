package com.lanking.cloud.domain.base.file;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.Part;

import com.lanking.cloud.domain.base.file.api.AbstractFileUtil;
import com.lanking.cloud.domain.base.file.api.CropModel;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 文件
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "file")
public class File implements Serializable {
	private static final long serialVersionUID = -8119329323244947989L;

	@Id
	private Long id;

	/**
	 * 文件空间ID
	 */
	@Column(name = "space_id", nullable = false)
	private long spaceId = 0;

	/**
	 * 文件名称
	 */
	@Column(length = 256, nullable = false)
	private String name;

	/**
	 * 大小(单位：字节)
	 */
	@Column(name = "size", nullable = false)
	private long size = 0;

	/**
	 * 长度(单位：秒)
	 */
	@Column(name = "duration", precision = 5, nullable = false)
	private int duration = 0;

	/**
	 * 截图文件ID
	 */
	@Column(name = "screenshot_id")
	private Long screenshotId = 0L;

	/**
	 * 宽度(单位：px)
	 */
	@Column(name = "width", precision = 5, nullable = false)
	private int width = 0;

	/**
	 * 高度(单位：px)
	 */
	@Column(name = "height", precision = 5, nullable = false)
	private int height = 0;

	/**
	 * 上传人ID
	 */
	@Column(name = "owner_id", nullable = false)
	private long ownerId = 0;

	/**
	 * 过期时间
	 */
	@Column(name = "expire_at", nullable = false)
	private long expireAt = 0;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", nullable = false)
	private long createAt = 0;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", nullable = false)
	private long updateAt = 0;

	/**
	 * 文件类型
	 * 
	 * @see FileType
	 */
	@Column(precision = 3, nullable = false)
	private FileType type;

	/**
	 * 状态
	 * 
	 * @see Status
	 */
	@Column(precision = 3, nullable = false)
	private Status status = Status.ENABLED;

	/**
	 * crc32校验码
	 */
	@Column(name = "crc32")
	private Long crc32;

	/**
	 * md5编码
	 */
	@Column(name = "md5")
	private String md5;

	/**
	 * 文件是否存在
	 */
	@Column(name = "exist", columnDefinition = "bit default 0")
	private boolean exist = false;

	/**
	 * 是否有引用
	 */
	@Column(name = "reference", columnDefinition = "bit default 0")
	private boolean reference = false;

	@Transient
	private Space space;
	@Transient
	private String nativePath;
	@Transient
	private Part part;
	@Transient
	private boolean preview;
	@Transient
	private CropModel cropModel;
	@Transient
	private boolean crop = false;
	@Transient
	private Long base64Size;
	@Transient
	private String base64Type;
	@Transient
	private String base64Data;
	@Transient
	private String url;
	@Transient
	private boolean correction = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Long getScreenshotId() {
		return screenshotId;
	}

	public void setScreenshotId(Long screenshotId) {
		this.screenshotId = screenshotId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getExpireAt() {
		return expireAt;
	}

	public void setExpireAt(long expireAt) {
		this.expireAt = expireAt;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public long getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(long updateAt) {
		this.updateAt = updateAt;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getCrc32() {
		return crc32;
	}

	public void setCrc32(Long crc32) {
		this.crc32 = crc32;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}

	public boolean isReference() {
		return reference;
	}

	public void setReference(boolean reference) {
		this.reference = reference;
	}

	public Space getSpace() {
		return space;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public String getNativePath() {
		return nativePath;
	}

	public void setNativePath(String nativePath) {
		this.nativePath = nativePath;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public CropModel getCropModel() {
		return cropModel;
	}

	public void setCropModel(CropModel cropModel) {
		this.cropModel = cropModel;
	}

	public boolean isCrop() {
		return crop;
	}

	public void setCrop(boolean crop) {
		this.crop = crop;
	}

	public Long getBase64Size() {
		return base64Size;
	}

	public void setBase64Size(Long base64Size) {
		this.base64Size = base64Size;
	}

	public String getBase64Type() {
		return base64Type;
	}

	public void setBase64Type(String base64Type) {
		this.base64Type = base64Type;
	}

	public String getBase64Data() {
		return base64Data;
	}

	public void setBase64Data(String base64Data) {
		this.base64Data = base64Data;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isCorrection() {
		return correction;
	}

	public void setCorrection(boolean correction) {
		this.correction = correction;
	}

	/**
	 * 是否是二级制文件
	 * 
	 * @return true|false
	 * @see FileType
	 */
	public boolean isBin() {
		return FileType.BIN == this.getType();
	}

	/**
	 * 是否是图片文件
	 * 
	 * @return true|false
	 * @see FileType
	 */
	public boolean isImage() {
		return FileType.IMAGE == this.getType();
	}

	/**
	 * 是否是音频文件
	 * 
	 * @return true|false
	 * @see FileType
	 */
	public boolean isAudio() {
		return FileType.AUDIO == this.getType();
	}

	/**
	 * 是否是视频文件
	 * 
	 * @return true|false
	 * @see FileType
	 */
	public boolean isVideo() {
		return FileType.VIDEO == this.getType();
	}

	/**
	 * 通过文件名获取对应的MP4文件名称
	 * 
	 * @return 文件名
	 */
	public String getMp4Name() {
		return name.split("\\.")[0] + "." + FileExt.MP4;
	}

	/**
	 * 通过文件名获取对应的SWF文件名称
	 * 
	 * @return 文件名
	 */
	public String getSWFName() {
		return name.split("\\.")[0] + "." + FileExt.SWF;
	}

	/**
	 * 通过文件名获取对应的PDF文件名称
	 * 
	 * @return 文件名
	 */
	public String getPDFName() {
		return name.split("\\.")[0] + "." + FileExt.PDF;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return 扩展名
	 */
	public String getExt() {
		return AbstractFileUtil.getSimpleExt(name);
	}

}
