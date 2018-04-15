package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 习题内容模块
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_practice")
public class TeachAssistElementPractice extends AbstractTeachAssistElement {

	private static final long serialVersionUID = -3790944521843282434L;
}
