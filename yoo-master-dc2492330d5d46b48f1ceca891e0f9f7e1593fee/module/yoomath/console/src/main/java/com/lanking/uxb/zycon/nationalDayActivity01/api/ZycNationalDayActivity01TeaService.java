package com.lanking.uxb.zycon.nationalDayActivity01.api;

import java.util.Map;

import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.nationalDayActivity01.form.ZycNationalDayActivity01Form;

/**
 * 国庆活动教师参与情况接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月27日
 */
public interface ZycNationalDayActivity01TeaService {

	@SuppressWarnings("rawtypes")
	Page<Map> queryNdaTeaRank(ZycNationalDayActivity01Form form, Pageable p);
}
