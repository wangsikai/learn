package com.lanking.uxb.service.fallible.value;

import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public class VStuFallibleTextbook extends VTextbook {
	private static final long serialVersionUID = 4910304771746125552L;

	private Long count;
	private List<VSection> sections;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<VSection> getSections() {
		return sections;
	}

	public void setSections(List<VSection> sections) {
		this.sections = sections;
	}
}
