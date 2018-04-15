/**
 * 活动参与用户vo
 */
package com.lanking.uxb.zycon.activity.value;

import java.util.List;

import com.lanking.cloud.sdk.value.VPage;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
public class VPageHolidayActivity01Rank<Map> extends VPage {

	private static final long serialVersionUID = -7372719027923352395L;

	private List<List<Long>> periods;

	public List<List<Long>> getPeriods() {
		return periods;
	}

	public void setPeriods(List<List<Long>> periods) {
		this.periods = periods;
	}

}
