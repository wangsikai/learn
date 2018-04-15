package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionScene;

/**
 * 习题应用场景服务.
 * 
 * @author wlche
 *
 */
public interface ResconQuestionSceneManage {
	QuestionScene get(int code);

	Map<Integer, QuestionScene> mget(Collection<Integer> codes);

	List<QuestionScene> mgetList(Collection<Integer> codes);

	List<QuestionScene> findList();
}
