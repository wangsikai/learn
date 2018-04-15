package com.lanking.uxb.service.user.api;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.UserParameter;
import com.lanking.cloud.domain.yoo.user.UserParameterType;

/**
 * 用户通用参数接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月26日
 */
public interface UserParameterService {

	UserParameter findOne(Product product, String version, UserParameterType type, long userId);

	UserParameter save(UserParameter parameter);

	void asyncSave(UserParameter parameter);

}
