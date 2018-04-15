package com.lanking.uxb.service.resources.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionBaseType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.resources.api.QuestionBaseTypeService;

/**
 * 题目题型与科目题型对应关系相关接口
 * 
 * @since yoomath V1.9.1
 * @author wangsenhao
 *
 */
@Transactional(readOnly = true)
@Service
public class QuestionBaseTypeServiceImpl implements QuestionBaseTypeService {

	@Autowired
	@Qualifier("QuestionBaseTypeRepo")
	Repo<QuestionBaseType, Integer> repo;

	@Override
	public List<Integer> findBaseCodeList(long questionCode) {
		return repo.find("$findBaseCodeList", Params.param("questionCode", questionCode)).list(Integer.class);
	}
}
