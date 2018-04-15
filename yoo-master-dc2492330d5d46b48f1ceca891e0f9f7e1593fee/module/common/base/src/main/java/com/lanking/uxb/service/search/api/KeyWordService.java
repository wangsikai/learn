package com.lanking.uxb.service.search.api;

import com.lanking.cloud.domain.base.search.UserKeyWord;

/**
 * 搜索热词相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年6月30日 上午9:35:12
 */
public interface KeyWordService {

	UserKeyWord save(long userId, String type, int phaseCode, int subjectCode, String word);
}
