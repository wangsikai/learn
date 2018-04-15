package com.lanking.cloud.domain.common.baseData;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 知识点-元知识点对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@MappedSuperclass
public class MetaKnowKnowKey implements Serializable {

	private static final long serialVersionUID = -170254950466697823L;

	/**
	 * 知识点代码
	 */
	@Id
	@Column(name = "know_point_code", nullable = false)
	private int knowPointCode;

	/**
	 * 元知识点代码
	 */
	@Id
	@Column(name = "meta_code", nullable = false)
	private int metaCode;

	public MetaKnowKnowKey(int knowPointCode, int metaCode) {
		this.knowPointCode = knowPointCode;
		this.metaCode = metaCode;
	}

	public MetaKnowKnowKey() {
	}

	public int getKnowPointCode() {
		return knowPointCode;
	}

	public void setKnowPointCode(int knowPointCode) {
		this.knowPointCode = knowPointCode;
	}

	public int getMetaCode() {
		return metaCode;
	}

	public void setMetaCode(int metaCode) {
		this.metaCode = metaCode;
	}

	@Override
	public int hashCode() {
		return (int) (knowPointCode * 37 + metaCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MetaKnowKnowKey) {
			MetaKnowKnowKey other = (MetaKnowKnowKey) obj;
			return knowPointCode == other.knowPointCode && metaCode == other.metaCode;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[] { knowPointCode, metaCode }, '-');
	}
}
