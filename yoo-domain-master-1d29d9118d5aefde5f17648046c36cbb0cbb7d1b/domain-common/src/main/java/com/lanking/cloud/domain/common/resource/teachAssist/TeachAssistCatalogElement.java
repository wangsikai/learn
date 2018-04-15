package com.lanking.cloud.domain.common.resource.teachAssist;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 教辅目录-元素
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_catalog_element")
public class TeachAssistCatalogElement implements Serializable {

	private static final long serialVersionUID = -5842058626226147511L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 教辅目录ID
	 */
	@Column(name = "teachassist_catalog_id")
	private Long teachassistCatalogId;

	/**
	 * 教辅元素ID
	 */
	@Column(name = "element_id")
	private Long elementId;

	/**
	 * 模块类型
	 */
	@Column(name = "type", precision = 3)
	private TeachAssistElementType type;

	/**
	 * 序号
	 */
	@Column(name = "sequence", precision = 3)
	private int sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachassistCatalogId() {
		return teachassistCatalogId;
	}

	public void setTeachassistCatalogId(Long teachassistCatalogId) {
		this.teachassistCatalogId = teachassistCatalogId;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public TeachAssistElementType getType() {
		return type;
	}

	public void setType(TeachAssistElementType type) {
		this.type = type;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
