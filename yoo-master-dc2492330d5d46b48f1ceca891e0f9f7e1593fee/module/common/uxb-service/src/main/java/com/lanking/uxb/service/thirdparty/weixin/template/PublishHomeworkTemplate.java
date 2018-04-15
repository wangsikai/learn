package com.lanking.uxb.service.thirdparty.weixin.template;

/**
 * 发布作业模板.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
public class PublishHomeworkTemplate extends MessageTemplate {

	/**
	 * 通知内容.
	 */
	private String keyword1;

	/**
	 * 通知人.
	 */
	private String keyword2;

	/**
	 * 通知时间.
	 */
	private String keyword3;

	public String getKeyword1() {
		return keyword1;
	}

	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}

	public String getKeyword2() {
		return keyword2;
	}

	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}

	public String getKeyword3() {
		return keyword3;
	}

	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}
}
