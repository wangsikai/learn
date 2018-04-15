package com.lanking.uxb.rescon.error.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.question.QuestionError;
import com.lanking.cloud.domain.support.resources.question.QuestionErrorType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.error.api.ResconErrorManage;
import com.lanking.uxb.rescon.error.value.VErrorUser;

@Transactional(readOnly = true)
@Service
public class ResconErrorManageImpl implements ResconErrorManage {
	@Autowired
	@Qualifier("QuestionErrorRepo")
	private Repo<QuestionError, Long> questionErrorRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryErrorQuestion(Pageable p) {
		return questionErrorRepo.find("$queryErrorQuestionCount", Params.param()).fetch(p, Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<VErrorUser> queryError(Long questionId) {
		List<Map> list = questionErrorRepo.find("$queryError", Params.param("questionId", questionId)).list(Map.class);
		List<VErrorUser> newList = new ArrayList<VErrorUser>();
		for (Map pa : list) {
			VErrorUser vErrorUser = new VErrorUser();
			String sb = "";
			if (pa.get("types") != null) {
				String[] types = String.valueOf(pa.get("types"))
						.substring(0, String.valueOf(pa.get("types")).length() - 1).split(",");
				for (String type : types) {
					if (QuestionErrorType.CONTENT == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "题干有误,";
					}
					if (QuestionErrorType.ANSWER == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "答案有误,";
					}
					if (QuestionErrorType.HINT == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "提示有误,";
					}
					if (QuestionErrorType.ANALYSIS == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "解析有误,";
					}

				}
			}
			vErrorUser.setErrorReason(sb + String.valueOf(pa.get("description")));
			vErrorUser.setErrorReason(String.valueOf(pa.get("description")));
			vErrorUser.setErrorTime((Date) pa.get("create_at"));
			vErrorUser.setUserName(String.valueOf(pa.get("name")));
			newList.add(vErrorUser);
		}
		return newList;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, List<VErrorUser>> mQueryError(Collection<Long> questionIds) {
		List<Map> list = questionErrorRepo.find("$mQueryError", Params.param("questionIds", questionIds)).list(
				Map.class);
		Map<Long, List<VErrorUser>> map = new HashMap<Long, List<VErrorUser>>();

		for (Long questionId : questionIds) {
			map.put(questionId, new ArrayList<VErrorUser>());
		}
		for (Map pa : list) {
			List<VErrorUser> vlist = map.get(Long.parseLong(String.valueOf(pa.get("question_id"))));
			VErrorUser vErrorUser = new VErrorUser();
			String sb = "";
			if (pa.get("types") != null) {
				String[] types = String.valueOf(pa.get("types"))
						.substring(0, String.valueOf(pa.get("types")).length() - 1).split(",");
				for (String type : types) {
					if (QuestionErrorType.CONTENT == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "题干有误,";
					}
					if (QuestionErrorType.ANSWER == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "答案有误,";
					}
					if (QuestionErrorType.HINT == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "提示有误,";
					}
					if (QuestionErrorType.ANALYSIS == QuestionErrorType.findByValue(Integer.parseInt(type))) {
						sb += "解析有误,";
					}

				}
			}
			vErrorUser.setErrorReason(sb + String.valueOf(pa.get("description")));
			vErrorUser.setErrorTime((Date) pa.get("create_at"));
			vErrorUser.setUserName(String.valueOf(pa.get("name")));
			vlist.add(vErrorUser);
			map.put(Long.parseLong(String.valueOf(pa.get("question_id"))), vlist);
		}
		return map;
	}

	@Override
	@Transactional
	public void updateStatusByQuestionId(Long questionId, Status status) {
		questionErrorRepo.execute("update question_error set status=:status where question_id=:questionId", Params
				.param("status", status.getValue()).put("questionId", questionId));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getlatestQuestionError() {
		Long newQuesitonId = questionErrorRepo.find("$getlatestQuestionError", Params.param()).get(Long.class);
		if (newQuesitonId == null) {
			return null;
		}
		Map map = questionErrorRepo.find("$queryOneErrorQuestionCount", Params.param("questionId", newQuesitonId)).get(
				Map.class);
		return map;
	}
}
