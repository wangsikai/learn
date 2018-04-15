package com.lanking.cloud.domain.common.baseData;

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
 * 敏感词
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "sensitive_word")
public class SensitiveWord implements Serializable {

	private static final long serialVersionUID = -7626256166375412067L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 敏感词
	 */
	@Column(name = "word", length = 100)
	private String word;

	/**
	 * 敏感词类型
	 */
	@Column(name = "type", precision = 3)
	private SensitiveType type;

	/**
	 * 敏感词类别
	 */
	@Column(name = "category", precision = 3)
	private int category;

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

	@Transient
	private String categoryTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public SensitiveType getType() {
		return type;
	}

	public void setType(SensitiveType type) {
		this.type = type;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
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

	public String getCategoryTitle() {
		String ct = "";
		switch (getCategory()) {
		case 1:
			ct = "粗话";
			break;
		case 2:
			ct = "毒";
			break;
		case 3:
			ct = "政治";
			break;
		case 4:
			ct = "黄";
			break;
		case 5:
			ct = "赌";
			break;
		case 6:
			ct = "器材";
			break;
		case 7:
			ct = "社会";
			break;
		case 8:
			ct = "邪教";
			break;
		case 9:
			ct = "政府";
			break;
		case 10:
			ct = "隐私";
			break;
		default:
			ct = "其他";
			break;
		}
		setCategoryTitle(ct);
		return categoryTitle;
	}

	public void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

}
