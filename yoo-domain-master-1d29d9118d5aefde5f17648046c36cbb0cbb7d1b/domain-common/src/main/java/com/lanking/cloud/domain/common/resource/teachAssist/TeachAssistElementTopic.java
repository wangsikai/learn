package com.lanking.cloud.domain.common.resource.teachAssist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 专题模块
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "teachassist_e_topic")
public class TeachAssistElementTopic extends AbstractTeachAssistElement {

	private static final long serialVersionUID = 3960727767199964612L;

	/**
	 * 教辅专题模块-专题类型
	 * 
	 * @see TeachAssistElementTopicType
	 */
	@Column(name = "topic_type", precision = 3)
	private TeachAssistElementTopicType topicType;

	public TeachAssistElementTopicType getTopicType() {
		return topicType;
	}

	public void setTopicType(TeachAssistElementTopicType topicType) {
		this.topicType = topicType;
	}
}
