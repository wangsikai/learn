package com.lanking.uxb.rescon.teach.value;

import java.util.List;

/**
 * 知识点模块Value
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementKnowledgeSpec extends VTeachAssistElement {
	private static final long serialVersionUID = 1855266056020900573L;

	private boolean review;
	private List<VTeachAssistElementKnowledgeSpecKp> kps;

	public List<VTeachAssistElementKnowledgeSpecKp> getKps() {
		return kps;
	}

	public void setKps(List<VTeachAssistElementKnowledgeSpecKp> kps) {
		this.kps = kps;
	}

	public boolean isReview() {
		return review;
	}

	public void setReview(boolean review) {
		this.review = review;
	}
}
