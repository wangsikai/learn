package com.lanking.uxb.service.push.api;

import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;

/**
 * 推送相关实现接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface PushHandle {

	boolean accept(PushHandleAction action);

	void push(PushHandleForm form);

}
