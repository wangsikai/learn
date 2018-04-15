package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_pointstructure")
public class TeachAssistElementPointStructure extends AbstractTeachAssistElement {

	private static final long serialVersionUID = 1261679241474155074L;

	/**
	 * 关联知识体系ID,只存小专题 知识专项ID
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
