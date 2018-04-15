package com.lanking.uxb.service.knowpoint.api.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.domain.yoomath.stat.HomeworkClazzKnowpointStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.knowpoint.api.HkClazzNewKnowpointStatService;

@Transactional(readOnly = true)
@Service
public class HkClazzNewKnowpointStatServiceImpl implements HkClazzNewKnowpointStatService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> hkRepo;

	@Autowired
	@Qualifier("HomeworkQuestionRepo")
	private Repo<HomeworkQuestion, Long> hkQuestionRepo;

	@Autowired
	@Qualifier("HomeworkClazzKnowpointStatRepo")
	private Repo<HomeworkClazzKnowpointStat, Long> hkClazzKpRepo;

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void statisticAfterHomework(long homeworkId) {
		Homework hk = hkRepo.get(homeworkId);
		// 作业班级id
		Long homeworkClassId = hk.getHomeworkClassId();
		// 知识点和homework_question
		List<Map> hkQuestion = hkQuestionRepo.find("$getHkQuestionStat", Params.param("homeworkId", homeworkId))
				.list(Map.class);
		Set<Long> points = new HashSet<Long>();
		for (Map data : hkQuestion) {
			points.add(Long.parseLong(String.valueOf(data.get("knowledge_code"))));
		}
		// 知识点和题目正确错误数的关系
		Map<Long, Map> knowMap = new HashMap<Long, Map>();
		for (Map map : hkQuestion) {
			knowMap.put(Long.parseLong(map.get("knowledge_code").toString()), map);
		}
		// 查询学生知识点统计表里数据
		List<HomeworkClazzKnowpointStat> hkClazzKp = hkClazzKpRepo
				.find("$getHkClazzNewKp", Params.param("classId", homeworkClassId)).list();
		Map<Long, HomeworkClazzKnowpointStat> pointMap = new HashMap<Long, HomeworkClazzKnowpointStat>();
		for (HomeworkClazzKnowpointStat hsk : hkClazzKp) {
			pointMap.put(hsk.getKnowpointCode(), hsk);
		}
		if (CollectionUtils.isEmpty(points)) {
			return;
		}
		for (Long knowpoint : points) {
			if (knowMap.get(knowpoint) != null) {
				Long rightcount = Long.parseLong(knowMap.get(knowpoint).get("rightcount").toString());
				Long wrongcount = Long.parseLong(knowMap.get(knowpoint).get("wrongcount").toString());
				// 判断原表中是否含当前班级当前知识点的数据
				HomeworkClazzKnowpointStat editHkClazzKp = pointMap.get(knowpoint);
				if (editHkClazzKp != null) {
					// 已存在更新数据
					editHkClazzKp.setRightNum(editHkClazzKp.getRightNum() + rightcount);
					editHkClazzKp.setWrongNum(editHkClazzKp.getWrongNum() + wrongcount);
					if (editHkClazzKp.getRightNum() == 0 && editHkClazzKp.getWrongNum() == 0) {
						editHkClazzKp.setRightRate(BigDecimal.valueOf(0));
					} else {
						editHkClazzKp.setRightRate(BigDecimal
								.valueOf((editHkClazzKp.getRightNum() * 100f)
										/ (editHkClazzKp.getRightNum() + editHkClazzKp.getWrongNum()))
								.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					editHkClazzKp.setUpdateAt(new Date());
					editHkClazzKp.setKnowpointCode(knowpoint);
					hkClazzKpRepo.save(editHkClazzKp);
				} else {
					// 不存在新增数据
					HomeworkClazzKnowpointStat addHkClazzKp = new HomeworkClazzKnowpointStat();
					addHkClazzKp.setClassId(homeworkClassId);
					addHkClazzKp.setRightNum(rightcount);
					addHkClazzKp.setWrongNum(wrongcount);
					if (addHkClazzKp.getRightNum() == 0 && addHkClazzKp.getWrongNum() == 0) {
						addHkClazzKp.setRightRate(BigDecimal.valueOf(0));
					} else {
						addHkClazzKp.setRightRate(BigDecimal
								.valueOf((addHkClazzKp.getRightNum() * 100f)
										/ (addHkClazzKp.getRightNum() + addHkClazzKp.getWrongNum()))
								.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					addHkClazzKp.setCreateAt(new Date());
					addHkClazzKp.setKnowpointCode(knowpoint);
					hkClazzKpRepo.save(addHkClazzKp);
				}
			}

		}
	}

	@Transactional
	@Override
	public void deleteNewAll() {
		hkClazzKpRepo.execute("$deleteNewAll");
	}

}
