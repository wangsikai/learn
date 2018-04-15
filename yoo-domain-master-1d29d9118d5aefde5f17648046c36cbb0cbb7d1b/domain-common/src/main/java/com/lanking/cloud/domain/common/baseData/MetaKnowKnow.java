package com.lanking.cloud.domain.common.baseData;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 知识点-元知识点对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(MetaKnowKnowKey.class)
@Table(name = "metaknow_know")
public class MetaKnowKnow extends MetaKnowKnowKey {

	private static final long serialVersionUID = 5959858148689042172L;

}
