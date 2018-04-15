package com.lanking.uxb.rescon.basedata.value;

import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.uxb.rescon.question.value.VQuestion;

/**
 * 知识点
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月12日 上午10:38:47
 */
public class VResconKnowpointCard {

	private Long id;

	private long knowpointCode;

	private String description;

	private String hints;

	private List<VQuestion> exampleQuestions = Lists.newArrayList();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getKnowpointCode() {
		return knowpointCode;
	}

	public void setKnowpointCode(long knowpointCode) {
		this.knowpointCode = knowpointCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHints() {
		return hints;
	}

	public void setHints(String hints) {
		this.hints = hints;
	}

	public List<VQuestion> getExampleQuestions() {
		return exampleQuestions;
	}

	public void setExampleQuestions(List<VQuestion> exampleQuestions) {
		this.exampleQuestions = exampleQuestions;
	}

}
