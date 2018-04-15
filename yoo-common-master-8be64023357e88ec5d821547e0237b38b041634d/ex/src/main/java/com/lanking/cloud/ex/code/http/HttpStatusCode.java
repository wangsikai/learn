package com.lanking.cloud.ex.code.http;

import com.lanking.cloud.ex.code.StatusCode;

/**
 * <a href="https://zh.wikipedia.org/wiki/HTTP%E7%8A%B6%E6%80%81%E7%A0%81">
 * http状态代码</a>
 * 
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年11月17日
 */
public interface HttpStatusCode extends StatusCode {

	int getCode();
}
