package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;

/**
 * 所有的教辅模块都通过此value进行返回
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElement implements Serializable, Comparable<VTeachAssistElement> {
	private static final long serialVersionUID = -1262838314164810318L;

	private Long id;
	private TeachAssistElementType type;
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TeachAssistElementType getType() {
		return type;
	}

	public void setType(TeachAssistElementType type) {
		this.type = type;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	@Override
	public int compareTo(VTeachAssistElement o) {
		if (sequence > o.sequence) {
			return 1;
		} else if (sequence < o.sequence) {
			return -1;
		} else {
			if (id > o.id) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
