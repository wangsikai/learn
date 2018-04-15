package com.lanking.uxb.service.search.api;

import java.util.List;

/**
 * 高亮条件设置
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月3日
 */
public class HighlightedConfig {

	/*
	 * 要高亮的字段名称
	 */
	private List<String> fields;
	/*
	 * 高度样式前面tag,例如:<span style="color:red">
	 */
	private String preTags = "<span style='color:red'>";
	/*
	 * 高度样式后面tag,例如:</span>
	 */
	private String postTags = "</span>";

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public String getPreTags() {
		return preTags;
	}

	public void setPreTags(String preTags) {
		this.preTags = preTags;
	}

	public String getPostTags() {
		return postTags;
	}

	public void setPostTags(String postTags) {
		this.postTags = postTags;
	}

}
