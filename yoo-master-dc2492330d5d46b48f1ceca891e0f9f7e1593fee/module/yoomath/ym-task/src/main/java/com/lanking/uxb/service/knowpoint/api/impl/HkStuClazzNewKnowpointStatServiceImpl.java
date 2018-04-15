package com.lanking.uxb.service.knowpoint.api.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
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
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzKnowpointStat;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.knowpoint.api.HkStuClazzNewKnowpointStatService;

@Transactional(readOnly = true)
@Service
public class HkStuClazzNewKnowpointStatServiceImpl implements HkStuClazzNewKnowpointStatService {

	@Autowired
	@Qualifier("HomeworkRepo")
	private Repo<Homework, Long> hkRepo;

	@Autowired
	@Qualifier("HomeworkStudentClazzKnowpointStatRepo")
	private Repo<HomeworkStudentClazzKnowpointStat, Long> hkStuKpRepo;

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void statisticAfterHomework(long homeworkId) {
		Homework hk = hkRepo.get(homeworkId);
		Long homeworkClassId = hk.getHomeworkClassId();
		List<Map> stuQuestion = hkStuKpRepo.find("$getStuQuestionStat", Params.param("homeworkId", homeworkId))
				.list(Map.class);
		Set<Long> points = new HashSet<Long>();
		// 学生不同知识点对应的批改结果
		for (Map map : stuQuestion) {
			Long studentId = Long.parseLong(map.get("student_id").toString());
			Long knowledgeCode = Long.parseLong(map.get("knowledge_code").toString());
			Integer result = Integer.parseInt(map.get("result").toString());
			points.add(knowledgeCode);
			HomeworkStudentClazzKnowpointStat hkClazzKp = hkStuKpRepo.find("$getHkStuClazzNewKp", Params
					.param("studetnId", studentId).put("classId", homeworkClassId).put("knowledgeCode", knowledgeCode))
					.get();
			if (hkClazzKp != null) {
				if (HomeworkAnswerResult.findByValue(result) == HomeworkAnswerResult.RIGHT) {
					hkClazzKp.setRightNum(hkClazzKp.getRightNum() + 1);
				}
				if (HomeworkAnswerResult.findByValue(result) == HomeworkAnswerResult.WRONG) {
					hkClazzKp.setWrongNum(hkClazzKp.getWrongNum() + 1);
				}
				hkClazzKp.setRightRate(BigDecimal
						.valueOf((hkClazzKp.getRightNum() * 100f) / (hkClazzKp.getRightNum() + hkClazzKp.getWrongNum()))
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				hkClazzKp.setUpdateAt(new Date());
				hkClazzKp.setClassId(homeworkClassId);
				hkClazzKp.setKnowpointCode(knowledgeCode);
				hkStuKpRepo.save(hkClazzKp);
			} else {
				HomeworkStudentClazzKnowpointStat newHkClazzKp = new HomeworkStudentClazzKnowpointStat();
				if (HomeworkAnswerResult.findByValue(result) == HomeworkAnswerResult.RIGHT) {
					newHkClazzKp.setRightNum(1);
				} else if (HomeworkAnswerResult.findByValue(result) == HomeworkAnswerResult.WRONG) {
					newHkClazzKp.setWrongNum(1);
				} else {
					continue;
				}
				newHkClazzKp.setStudentId(studentId);
				newHkClazzKp.setCreateAt(new Date());
				newHkClazzKp.setClassId(homeworkClassId);
				newHkClazzKp.setKnowpointCode(knowledgeCode);
				newHkClazzKp.setRightRate(BigDecimal
						.valueOf((newHkClazzKp.getRightNum() * 100f)
								/ (newHkClazzKp.getRightNum() + newHkClazzKp.getWrongNum()))
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				hkStuKpRepo.save(newHkClazzKp);
			}

		}

		for (Long knowpoint : points) {
			List<HomeworkStudentClazzKnowpointStat> hkClazzKp = hkStuKpRepo.find("$getHkStuClazzNewKpOrderByRate",
					Params.param("knowpoint", knowpoint).put("classId", homeworkClassId)).list();
			Map<Long, Integer> rankMap = getRank(hkClazzKp);
			for (HomeworkStudentClazzKnowpointStat hks : hkClazzKp) {
				hks.setRank(rankMap.get(hks.getStudentId()));
				hkStuKpRepo.save(hks);
			}
		}
	}

	/**
	 * 获取学生在当前知识点的班级排名
	 * 
	 * @param list
	 * @return
	 */
	public Map<Long, Integer> getRank(List<HomeworkStudentClazzKnowpointStat> list) {
		Collections.sort(list, new Comparator<HomeworkStudentClazzKnowpointStat>() {
			@Override
			public int compare(HomeworkStudentClazzKnowpointStat o1, HomeworkStudentClazzKnowpointStat o2) {
				if (o1.getRightRate().compareTo(o2.getRightRate()) == 1) {
					return -1;
				} else if (o1.getRightRate().compareTo(o2.getRightRate()) == -1) {
					return 1;
				}
				return 0;
			}
		});

		Map<BigDecimal, Integer> dMap = new HashMap<BigDecimal, Integer>(); // 个数
		Map<BigDecimal, Integer> sMap = new HashMap<BigDecimal, Integer>(); // 排名

		int pl = 1;
		Map<Long, Integer> t = new HashMap<Long, Integer>();
		for (int i = 0; i < list.size(); i++) {
			HomeworkStudentClazzKnowpointStat hks = list.get(i);

			if (dMap.get(hks.getRightRate()) == null) {
				dMap.put(hks.getRightRate(), 1);
				pl = i + 1;
				sMap.put(hks.getRightRate(), pl);
			} else {
				pl = sMap.get(hks.getRightRate());
				dMap.put(hks.getRightRate(), dMap.get(hks.getRightRate()) + 1);
			}

			t.put(hks.getStudentId(), pl);
		}
		return t;

	}

	@Override
	public CursorPage<Long, Long> findIssueHkIds(CursorPageable<Long> cursorPageable, Date nowTime) {
		Params params = Params.param();
		if (nowTime != null) {
			params.put("nowTime", nowTime);
		}
		return hkRepo.find("$findIssueHkIds", params).fetch(cursorPageable, Long.class);
	}

	@Transactional
	@Override
	public void deleteNewAll() {
		hkStuKpRepo.execute("$deleteNewAll");
	}

}
