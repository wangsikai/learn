package com.lanking.uxb.channelSales.channel.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.channelSales.channel.form.ChannelSchoolQueryForm;

public interface CsChannelSchoolService {
	/**
	 * 分页查询渠道商对应的学校
	 *
	 * @param queryForm
	 *            {@link ChannelSchoolQueryForm}
	 * @return {@link Page}
	 */
	Page<ChannelSchool> query(ChannelSchoolQueryForm queryForm);

	/**
	 * 查询学校列表
	 *
	 * @param queryForm
	 *            {@link ChannelSchoolQueryForm}
	 * @return {@link Page}
	 */
	Page<School> querySchool(ChannelSchoolQueryForm queryForm);

	/**
	 * + 批量通过渠道codes查询对应的学校
	 */
	Map<Integer, List<ChannelSchool>> mgetSchoolByChannel(Collection<Integer> keys);

	/**
	 * 获取渠道绑定学校时间
	 */
	Map<String, Date> getSchoolBindDate(Long schoolId);

}
