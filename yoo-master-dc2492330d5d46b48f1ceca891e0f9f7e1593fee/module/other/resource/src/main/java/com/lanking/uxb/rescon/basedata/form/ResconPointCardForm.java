package com.lanking.uxb.rescon.basedata.form;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 知识卡片form
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月12日 上午10:14:54
 */
public class ResconPointCardForm {

	private Long id;

	private long knowpointCode;

	private String description;

	private String hints;

	private List<Long> examples = Lists.newArrayList();

	private Long userId;

	private Date date;

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

	public List<Long> getExamples() {
		return examples;
	}

	public void setExamples(List<Long> examples) {
		this.examples = examples;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
