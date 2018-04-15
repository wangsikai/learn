package com.lanking.uxb.zycon.basedata.value;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
public class ZycResourceCategory {
	private Integer id;
	private Integer parent;
	private String name;
	private Integer sequence;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
