package com.lanking.uxb.rescon.question.form;

import java.io.Serializable;
import java.util.List;

/**
 * 重复题组.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月20日
 */
public class SimilarSameQuestionForm implements Serializable {
	private static final long serialVersionUID = -1977327329914416509L;

	/**
	 * 重复题组中的展示题.
	 */
	private Long showId;

	/**
	 * 重复题组（不包含展示题）.
	 */
	private List<Long> sameIds;

	public Long getShowId() {
		return showId;
	}

	public void setShowId(Long showId) {
		this.showId = showId;
	}

	public List<Long> getSameIds() {
		return sameIds;
	}

	public void setSameIds(List<Long> sameIds) {
		this.sameIds = sameIds;
	}
}
