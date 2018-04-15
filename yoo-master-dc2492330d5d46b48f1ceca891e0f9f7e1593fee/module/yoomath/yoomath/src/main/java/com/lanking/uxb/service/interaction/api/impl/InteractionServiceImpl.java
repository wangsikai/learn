package com.lanking.uxb.service.interaction.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.interaction.Interaction;
import com.lanking.cloud.domain.yoomath.interaction.InteractionStatus;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzStat;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.interaction.api.InteractionService;
import com.lanking.uxb.service.interaction.convert.InteractionConvert;
import com.lanking.uxb.service.interaction.value.VInteraction;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

@Transactional(readOnly = true)
@Service
public class InteractionServiceImpl implements InteractionService {
	@Autowired
	@Qualifier("InteractionRepo")
	private Repo<Interaction, Long> repo;

	@Autowired
	private ZyStudentHomeworkService studentHomeworkService;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ZyHomeworkStudentClazzStatService zyHomeworkStudentClazzStatService;
	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private InteractionConvert interConvert;

	@Override
	public Page<Interaction> query(Long userId, Pageable p) {
		return repo.find("$query", Params.param("userId", userId)).fetch(p);
	}

	@Transactional
	@Override
	public void updateStatus(Long id, InteractionStatus status) {
		Interaction interaction = repo.get(id);
		interaction.setUpdateAt(new Date());
		interaction.setStatus(status);
		repo.save(interaction);
	}

	@Override
	public Interaction get(Long id) {
		return repo.get(id);
	}

	@Override
	public List<Interaction> queryIndex(Long userId) {
		return repo.find("$queryIndex", Params.param("userId", userId)).list();
	}

	@Transactional
	@Override
	public void delIndexInteraction(Long id) {
		Interaction interaction = repo.get(id);
		interaction.setUpdateAt(new Date());
		interaction.setHomePageShow(false);
		repo.save(interaction);

	}

	@Transactional
	@Override
	public void saveInteraction(Long classId, Long studentId, Long teacherId, InteractionType type,
			InteractionStatus status, Integer p1, Integer p2) {
		Interaction interaction = new Interaction();
		interaction.setClassId(classId);
		interaction.setStudentId(studentId);
		interaction.setTeacherId(teacherId);
		interaction.setStatus(status);
		interaction.setType(type);
		interaction.setCreateAt(new Date());
		if (p1 != null) {
			interaction.setP1(String.valueOf(p1));
		}
		if (p2 != null) {
			interaction.setP2(String.valueOf(p2));
		}
		repo.save(interaction);

	}

	@Transactional
	@Override
	public List<Long> mostImprovedHandle(Long classId) {
		HomeworkClazz hkClass = hkClassService.get(classId);
		Map<String, Map<Long, Integer>> map = studentHomeworkService.getMostImprovedMap(classId, 3);
		List<Long> improveUserIds = new ArrayList<Long>();
		if (CollectionUtils.isNotEmpty(map)) {
			Map<Long, Integer> improveRankMap = map.get("improveRankMap");
			Map<Long, Integer> rankMap = map.get("rankMap");
			Set<Long> improveUserIds_temp = improveRankMap.keySet();
			improveUserIds.addAll(improveUserIds_temp);
			if (CollectionUtils.isNotEmpty(improveUserIds)) {
				for (Long studentId : improveUserIds) {
					this.saveInteraction(classId, studentId, hkClass.getTeacherId(), InteractionType.MOST_IMPROVED_STU,
							InteractionStatus.UNSENT_PRAISE, rankMap.get(studentId), improveRankMap.get(studentId));
				}
			}
		}
		return improveUserIds;

	}

	@Transactional
	@Override
	public void oneHomeworkTop5Handle(Long homeworkId) {
		Homework hk = hkService.get(homeworkId);
		HomeworkClazz hkClass = hkClassService.get(hk.getHomeworkClassId());
		if (hk.getCommitCount() < 20) {
			return;
		}
		// 查出前六名，如果第五名和第六名正确率不一样，则只取前五个
		List<StudentHomework> shs = new ArrayList<StudentHomework>();
		List<StudentHomework> stuHs = studentHomeworkService.listTop5ByHomework(homeworkId, 6);
		// 如果第六个不同于第五个,直接取前五个即可
		if (stuHs.size() > 5) {
			if (stuHs.get(4).getRightRate() != stuHs.get(5).getRightRate()) {
				shs = stuHs.subList(0, 5);
			} else {
				// 如果第五个同于第六个，把这个正确率或排名的学生全部查出
				// 查出来对应的，并取两个无重复并集
				shs = stuHs;
				List<StudentHomework> list = studentHomeworkService.findByRank(homeworkId, stuHs.get(4).getRank());
				shs.removeAll(list);
				shs.addAll(list);
			}
		} else {
			shs = stuHs;
		}
		if (CollectionUtils.isNotEmpty(shs)) {
			for (StudentHomework shk : shs) {
				this.saveInteraction(hk.getHomeworkClassId(), shk.getStudentId(), hkClass.getTeacherId(),
						InteractionType.ONE_HOMEWORK_TOP5, InteractionStatus.UNSENT_PRAISE, null, null);
			}
		}
	}

	@Transactional
	@Override
	public void classHomeworkTop5Handle(Long classId) {
		HomeworkClazz hkClass = hkClassService.get(classId);
		List<HomeworkStudentClazzStat> vhks = new ArrayList<HomeworkStudentClazzStat>();
		List<HomeworkStudentClazzStat> stuHs = zyHomeworkStudentClazzStatService.findTopStudent(classId, 6);
		// 如果第六个不同于第五个,直接取前五个即可
		if (stuHs.size() > 5) {
			// 如果第五个同于第六个，把这个正确率或排名的学生全部查出
			// 查出来对应的，并取两个无重复并集
			if (stuHs.get(4).getDays30RightRate() != stuHs.get(5).getDays30RightRate()) {
				vhks = stuHs.subList(0, 5);
			} else {
				vhks = stuHs;
				List<HomeworkStudentClazzStat> list = zyHomeworkStudentClazzStatService.findStudentByRightRate(classId,
						stuHs.get(4).getDays30RightRate());
				vhks.removeAll(list);
				vhks.addAll(list);
			}
		} else {
			vhks = stuHs;
		}
		if (CollectionUtils.isNotEmpty(vhks)) {
			for (HomeworkStudentClazzStat stat : vhks) {
				this.saveInteraction(classId, stat.getStudentId(), hkClass.getTeacherId(),
						InteractionType.CLASS_HOMEWORK_TOP5, InteractionStatus.UNSENT_PRAISE, null, null);
			}
		}

	}

	@Transactional
	@Override
	public void mostBackwardHandle(Long classId, List<Long> improveUserIds) {
		HomeworkClazz hkClass = hkClassService.get(classId);
		Map<String, Map<Long, Integer>> map = studentHomeworkService.getMostBackwardMap(classId, 3);
		if (CollectionUtils.isNotEmpty(map)) {
			Map<Long, Integer> backRankMap = map.get("backRankMap");
			Map<Long, Integer> rankMap = map.get("rankMap");
			Set<Long> backwardUserIdsAll_temp = backRankMap.keySet();
			List<Long> backwardUserIdsAll = new ArrayList<Long>();
			backwardUserIdsAll.addAll(backwardUserIdsAll_temp);
			List<Long> backwardUserIds = new ArrayList<Long>();
			// 退步的不包含进步的,处理类似不进不退等导致同样是进步前三和退步前三的情况，只在进步中显示
			if (CollectionUtils.isNotEmpty(improveUserIds)) {
				for (Long id : backwardUserIdsAll) {
					if (!improveUserIds.contains(id)) {
						backwardUserIds.add(id);
					}
				}
			} else {
				backwardUserIds = backwardUserIdsAll;
			}
			if (CollectionUtils.isNotEmpty(backwardUserIds)) {
				for (Long studentId : backwardUserIds) {
					this.saveInteraction(classId, studentId, hkClass.getTeacherId(), InteractionType.MOST_BACKWARD_STU,
							InteractionStatus.CRITICISM, rankMap.get(studentId), backRankMap.get(studentId));
				}
			}
		}

	}

	@Transactional
	@Override
	public void seriesNotsubmitHandle(Long homeworkId) {
		Homework hk = hkService.get(homeworkId);
		HomeworkClazz hkClass = hkClassService.get(hk.getHomeworkClassId());
		// 最近下发的三次作业
		List<Homework> hkList = zyHkService.getLatestIssuedHomeWorks(hk.getHomeworkClassId(), 3);
		// 没有三次下发的作业
		if (hkList.size() != 3) {
			return;
		}
		// 判断三次作业中，有效提交是否均大于20
		for (Homework homework : hkList) {
			if (homework.getCommitCount() < 20) {
				return;
			}
		}
		List<Long> notSubmitList1 = studentHomeworkService.findNotSubmitStus(hkList.get(0).getId());
		List<Long> notSubmitList2 = studentHomeworkService.findNotSubmitStus(hkList.get(1).getId());
		List<Long> notSubmitList3 = studentHomeworkService.findNotSubmitStus(hkList.get(2).getId());
		// 取三者的交集学生id集合
		notSubmitList1.retainAll(notSubmitList2);
		notSubmitList1.retainAll(notSubmitList3);
		if (CollectionUtils.isNotEmpty(notSubmitList1)) {
			for (Long studentId : notSubmitList1) {
				this.saveInteraction(hk.getHomeworkClassId(), studentId, hkClass.getTeacherId(),
						InteractionType.SERIES_NOTSUBMIT_STU, InteractionStatus.CRITICISM, null, null);
			}
		}
	}

	@Override
	public List<Interaction> queryIndexHonourList(List<Long> classIds) {
		// 获取已发送小红花的学生记录
		Params params = Params.param();
		if (classIds != null) {
			params.put("clazzIds", classIds);
		}
		return repo.find("$queryIndexHonourList", params).list();
	}

	@Override
	public Page<Interaction> queryHonourList(List<Long> classIds, Pageable p, Long userId) {
		Params params = Params.param();
		if (classIds != null) {
			params.put("clazzIds", classIds);
		}
		if (userId != null) {
			params.put("userId", userId);
		}
		return repo.find("$queryHonourList", params).fetch(p);
	}

	@Override
	public Map<Long, List<VInteraction>> findByClassIds(Long userId, List<Long> classIds) {
		List<Interaction> list = new ArrayList<Interaction>();
		for (Long classId : classIds) {
			List<Interaction> singleList = repo.find("$queryIndex2",
					Params.param("userId", userId).put("classId", classId)).list();
			list.addAll(singleList);
		}
		List<VInteraction> vlist = interConvert.to(list);
		Map<Long, List<VInteraction>> interMap = new HashMap<Long, List<VInteraction>>();
		for (VInteraction v : vlist) {
			List<VInteraction> temp = interMap.get(v.getClassId());
			if (temp == null) {
				temp = new ArrayList<VInteraction>();
				interMap.put(v.getClassId(), temp);
			}
			temp.add(v);
		}
		return interMap;
	}
}
