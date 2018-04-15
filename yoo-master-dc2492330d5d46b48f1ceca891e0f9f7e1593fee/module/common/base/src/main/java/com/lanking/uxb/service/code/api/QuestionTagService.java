package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.QuestionTag;

public interface QuestionTagService {

	List<QuestionTag> getQuestionTag(Collection<Long> ids);

	Map<Long, QuestionTag> mget(Collection<Long> ids);

}
