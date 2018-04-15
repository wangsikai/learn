package com.lanking.uxb.service.thirdparty.weixin.request;

import java.io.Serializable;

/**
 * 模板消息提交参数.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
public class TemplateMessageParams implements Serializable {
	private static final long serialVersionUID = -7316973322288231038L;

	private String touser;
	private String template_id;
	private String url;
	private Object data;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
