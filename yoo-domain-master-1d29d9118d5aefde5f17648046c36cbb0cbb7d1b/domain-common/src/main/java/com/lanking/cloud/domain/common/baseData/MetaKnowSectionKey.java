package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 元知识点-章节对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class MetaKnowSectionKey implements Serializable {

	private static final long serialVersionUID = 5725887532272392316L;

	/**
	 * 章节代码
	 */
	@Id
	@Column(name = "section_code", nullable = false)
	private long sectionCode;

	/**
	 * 元知识点代码
	 */
	@Id
	@Column(name = "meta_code", nullable = false)
	private int metaCode;

	public MetaKnowSectionKey(int sectionCode, int metaCode) {
		this.sectionCode = sectionCode;
		this.metaCode = metaCode;
	}

	public MetaKnowSectionKey() {
	}

	public long getSectionCode() {
		return sectionCode;
	}

	public void setSectionCode(long sectionCode) {
		this.sectionCode = sectionCode;
	}

	public int getMetaCode() {
		return metaCode;
	}

	public void setMetaCode(int metaCode) {
		this.metaCode = metaCode;
	}

	@Override
	public int hashCode() {
		return (int) (sectionCode * 37 + metaCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MetaKnowSectionKey) {
			MetaKnowSectionKey other = (MetaKnowSectionKey) obj;
			return sectionCode == other.sectionCode && metaCode == other.metaCode;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[] { sectionCode, metaCode }, '-');
	}
}
