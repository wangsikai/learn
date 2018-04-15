package com.lanking.uxb.rescon.basedata.value;

import java.io.Serializable;

/**
 * 知识体系
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public class VKnowledgeSystem implements Serializable {

	private static final long serialVersionUID = 7350358936671963998L;
	private Integer level;
	private Long id;
	private String name;
	private Long pcode;
	// 有校验不通过的(目前只有预置内容库用到)
	private boolean hasNoPass = false;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPcode() {
		return pcode;
	}

	public void setPcode(Long pcode) {
		this.pcode = pcode;
	}

	public boolean isHasNoPass() {
		return hasNoPass;
	}

	public void setHasNoPass(boolean hasNoPass) {
		this.hasNoPass = hasNoPass;
	}

}
