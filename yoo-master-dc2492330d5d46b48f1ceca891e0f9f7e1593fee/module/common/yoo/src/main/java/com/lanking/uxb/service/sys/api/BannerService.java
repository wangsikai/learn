package com.lanking.uxb.service.sys.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.common.Banner;

public interface BannerService {

	List<Banner> listEnable(BannerQuery query);

}
