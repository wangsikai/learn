package com.lanking.uxb.service.knowpointCard.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.recommend.RecommendKnowpointCard;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.knowpointCard.api.KnowpointCardRecommendService;

@Transactional(readOnly = true)
@Service
public class KnowpointCardRecommendServiceImpl implements KnowpointCardRecommendService {

	@Autowired
	@Qualifier("RecommendKnowpointCardRepo")
	Repo<RecommendKnowpointCard, Long> repo;

	@Transactional
	@Override
	public Page<Long> recommendKnowpointCard(int page, int size) {
		// 获取作业中的学生
		Page<Long> studentPage = repo.find("$taskGetStudentIds").fetch(P.index(page, size), Long.class);
		// 获取作业任务并且选出最优推送数据
		Params param = Params.param("students", studentPage.getItems());
		List<Map> dataMap = repo.find("$taskGetKnowpointCard", param).list(Map.class);
		// 二 未关闭的历史知识点卡片 user_id ,difficulty
		List<RecommendKnowpointCard> oldRecommendList = repo.find("$taskGetKnowpointCardByStatus", param).list();
		// 关闭
		for (RecommendKnowpointCard rkc : oldRecommendList) {
			rkc.setStatus(Status.DISABLED);
		}
		List<RecommendKnowpointCard> newRecommendList = new ArrayList<RecommendKnowpointCard>();
		// 组装新推送知识点卡片List
		List<RecommendKnowpointCard> newRkcList = new ArrayList<RecommendKnowpointCard>();
		Date date = new Date();
		RecommendKnowpointCard rCard = null;
		for (long studentId : studentPage.getItems()) {
			go: for (Map data : dataMap) {
				if (null != data.get("cardid") && null != data.get("student_id")
						&& Long.parseLong(data.get("student_id").toString()) == studentId) {
					for (RecommendKnowpointCard rkc : oldRecommendList) {
						if (rkc.getUserId() == studentId
								&& rkc.getKnowpointCardId() == Long.parseLong(data.get("cardid").toString())) {
							// 如果相同开启旧的
							rkc.setStatus(Status.ENABLED);
							break go;
						}

					}
					rCard = new RecommendKnowpointCard();
					rCard.setCreateAt(date);
					rCard.setKnowpointCardId(Long.parseLong(data.get("cardid").toString()));
					rCard.setUserId(studentId);
					rCard.setStatus(Status.ENABLED);
					newRkcList.add(rCard);
					break;
				}
			}
		}
		// 完成知识点卡片推送
		repo.save(newRkcList);
		// 关闭历史推荐
		for (RecommendKnowpointCard rkc : newRecommendList) {
			if (rkc.getStatus() == Status.DISABLED) {
				newRkcList.add(rkc);
			}
		}
		repo.save(newRkcList);
		return studentPage;

	}

}
