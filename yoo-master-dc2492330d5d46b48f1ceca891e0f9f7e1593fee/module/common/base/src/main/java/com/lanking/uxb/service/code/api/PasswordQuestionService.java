package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.PasswordQuestion;

/**
 * 提供密码问题代码表的相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年2月2日
 */
public interface PasswordQuestionService {

	PasswordQuestion get(Integer code);

	List<PasswordQuestion> getAll();

	List<PasswordQuestion> mgetList(Collection<Integer> codes);

	Map<Integer, PasswordQuestion> mget(Collection<Integer> codes);

}
