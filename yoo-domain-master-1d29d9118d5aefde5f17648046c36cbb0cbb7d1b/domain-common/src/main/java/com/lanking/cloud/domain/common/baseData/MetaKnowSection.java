package com.lanking.cloud.domain.common.baseData;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 元知识点-章节对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(MetaKnowSectionKey.class)
@Table(name = "metaknow_section")
public class MetaKnowSection extends MetaKnowSectionKey {

	private static final long serialVersionUID = -2597848658166120002L;

}
