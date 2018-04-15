package com.lanking.uxb.service.zuoye.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 学生错题本中对应的ocr对应答题图片和时间VO
 * 
 * @since 2.0.3
 * @author wangsenhao
 *
 */
public class VOcrHisAnswer implements Serializable {

	private static final long serialVersionUID = -7149610851313098275L;

	private String imgUrl;

	// 支持多图
	private List<String> imgUrls;

	private Date answerAt;

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Date getAnswerAt() {
		return answerAt;
	}

	public void setAnswerAt(Date answerAt) {
		this.answerAt = answerAt;
	}

	public List<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}
}
