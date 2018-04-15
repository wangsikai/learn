package com.lanking.uxb.zycon.nationalDayActivity01.api;

import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01H5PVUV;

/**
 * 国庆活动页面用户访问详细记录接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月27日
 */
public interface ZycNationalDayActivity01H5PVUVService {

	NationalDayActivity01H5PVUV getH5PVUV(NationalDayActivity01H5 h5, long viewAt);
}
