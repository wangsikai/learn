package com.lanking.cloud.domain.common.baseData;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 新知识体系与章节对应关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@IdClass(KnowledgeSectionKey.class)
@Table(name = "knowledge_section")
public class KnowledgeSection extends KnowledgeSectionKey {
	private static final long serialVersionUID = 5097675996488139459L;
}
