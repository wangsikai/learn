package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 知识拓朴图模块
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_pointmap")
public class TeachAssistElementPointMap extends AbstractTeachAssistElement {

	private static final long serialVersionUID = -2325159326569012291L;

	/**
	 * 关联知识体系ID
	 */
	@Column(name = "knowpoint_system")
	private Long knowpointSystem;

	public Long getKnowpointSystem() {
		return knowpointSystem;
	}

	public void setKnowpointSystem(Long knowpointSystem) {
		this.knowpointSystem = knowpointSystem;
	}
}
