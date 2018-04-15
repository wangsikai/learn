package com.lanking.uxb.rescon.question.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.QuestionTag;
import com.lanking.cloud.domain.common.baseData.QuestionTagType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question2Tag;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.question.api.ResconQuestionTagManage;

/**
 * 题目标签管理接口实现.
 * 
 * @author wlche
 *
 */

@Transactional(readOnly = true)
@Service
public class ResconQuestionTagManageImpl implements ResconQuestionTagManage {

	@Autowired
	@Qualifier("QuestionTagRepo")
	Repo<QuestionTag, Long> questionTagRepo;
	@Autowired
	@Qualifier("Question2TagRepo")
	Repo<Question2Tag, Long> question2TagRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Override
	public Map<Long, QuestionTag> mget(Collection<Long> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return Maps.newHashMap();
		}
		return questionTagRepo.mget(codes);
	}

	@Override
	public List<QuestionTag> listAll(Status status, QuestionTagType questionTagType) {
		Params params = Params.param();
		if (status != null) {
			params.put("status", status.getValue());
		}
		if (questionTagType != null) {
			params.put("questionTagType", questionTagType.getValue());
		}
		return questionTagRepo.find("$listAll", params).list();
	}

	@Override
	@Transactional
	public QuestionTag saveOrUpdateTag(Long code, String name, String shortName, String cfg, Long icon, Integer squence,
			Status status) {
		QuestionTag questionTag = null;
		if (code == null) {
			questionTag = new QuestionTag();
			questionTag.setSequence(squence);
		} else {
			questionTag = questionTagRepo.get(code);
		}
		questionTag.setName(name);
		questionTag.setShortName(shortName);
		questionTag.setIcon(icon);
		if (StringUtils.isNotEmpty(cfg)) {
			JSONObject obj = JSONObject.parseObject(cfg);
			Map<String, Object> map = new HashMap<String, Object>(1);
			if (obj.get("minPulishCount") != null) {
				map.put("minPulishCount", obj.getInteger("minPulishCount"));
			} else if (obj.get("maxRightRate") != null) {
				map.put("maxRightRate", obj.getInteger("maxRightRate"));
				map.put("minDoNum", obj.getInteger("minDoNum"));
			} else if (obj.get("minCollectCount") != null) {
				map.put("minCollectCount", obj.getInteger("minCollectCount"));
			}
			questionTag.setType(QuestionTagType.SYSTEM);
			questionTag.setCfg(map);
		} else {
			questionTag.setType(QuestionTagType.MANUAL);
		}
		questionTag.setStatus(status);

		questionTagRepo.save(questionTag);
		return questionTag;
	}

	@Override
	@Transactional
	public QuestionTag moveTag(Long code, String op) {
		List<QuestionTag> questionTags = this.listAll(null, null);
		List<QuestionTag> first = new ArrayList<QuestionTag>();
		List<QuestionTag> last = new ArrayList<QuestionTag>();

		QuestionTag questionTag = questionTagRepo.get(code);
		for (QuestionTag tag : questionTags) {
			if (tag.getCode() != code) {
				if (tag.getSequence() < questionTag.getSequence()) {
					first.add(tag);
				} else {
					last.add(tag);
				}
			}
		}

		if ("up".equals(op)) {
			questionTag.setSequence(questionTag.getSequence() - 1);
			if (first.size() > 0) {
				QuestionTag t = first.get(first.size() - 1);
				t.setSequence(t.getSequence() + 1);
			}
		} else {
			questionTag.setSequence(questionTag.getSequence() + 1);
			if (last.size() > 0) {
				QuestionTag t = last.get(0);
				t.setSequence(t.getSequence() - 1);
			}
		}

		first.add(questionTag);
		first.addAll(last);
		questionTagRepo.save(first);

		return questionTag;
	}

	@Override
	public List<Question2Tag> listByQuestion(long questionId) {
		return question2TagRepo.find("$listByQuestion", Params.param("questionId", questionId)).list();
	}

	@Override
	public Map<Long, List<Question2Tag>> mgetByQuestions(Collection<Long> questionIds) {
		if (CollectionUtils.isEmpty(questionIds)) {
			return Maps.newHashMap();
		}
		List<Question2Tag> all = question2TagRepo.find("$listByQuestions", Params.param("questionIds", questionIds))
				.list();
		Map<Long, List<Question2Tag>> map = new HashMap<Long, List<Question2Tag>>(questionIds.size());

		for (Question2Tag question2Tag : all) {
			List<Question2Tag> question2Tags = map.get(question2Tag.getQuestionId());
			if (question2Tags == null) {
				question2Tags = new ArrayList<Question2Tag>();
				map.put(question2Tag.getQuestionId(), question2Tags);
			}
			question2Tags.add(question2Tag);
		}

		return map;
	}
}
