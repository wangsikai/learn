package com.lanking.uxb.zycon.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;

/**
 * 提供元知识点和习题关系的相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:12:32
 */
public interface ZycQuestionMetaKnowService {

	List<MetaKnowpoint> listByQuestion(long questionId);

	Map<Long, List<MetaKnowpoint>> mListByQuestions(Collection<Long> questionIds);
}
