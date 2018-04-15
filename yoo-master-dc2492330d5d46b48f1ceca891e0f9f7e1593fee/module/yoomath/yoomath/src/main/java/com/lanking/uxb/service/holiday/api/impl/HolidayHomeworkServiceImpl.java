package com.lanking.uxb.service.holiday.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHolidayHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkItemPublishForm;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkPublishForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;

@Service
@Transactional(readOnly = true)
public class HolidayHomeworkServiceImpl implements HolidayHomeworkService {

	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;
	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	private Repo<HolidayHomeworkItem, Long> hdHomeworkItemRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkRepo")
	private Repo<HolidayStuHomework, Long> hdStuHomework;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> hdStuHomeworkItem;
	@Autowired
	private HolidayHomeworkItemService holidayHomeworkItemService;
	@Autowired
	private HolidayHomeworkItemQuestionService holidayHomeworkItemQuestionService;
	@Autowired
	private HolidayStuHomeworkItemService stuHomeworkServce;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private MqSender mqSender;

	@Override
	public HolidayHomework get(long id) {
		return holidayHomeworkRepo.get(id);
	}

	@Override
	public Map<Long, HolidayHomework> mget(Collection<Long> ids) {
		return holidayHomeworkRepo.mget(ids);
	}

	@Override
	@Transactional
	public HolidayHomework publish(HolidayHomeworkPublishForm form) {
		HolidayHomework homework = new HolidayHomework();
		homework.setCompletionRate(null);
		homework.setCreateAt(new Date());
		homework.setCreateId(form.getCreateId());
		homework.setDeadline(form.getDeadline());
		homework.setDelStatus(Status.ENABLED);
		homework.setDifficulty(form.getDifficulty());
		homework.setHomeworkClassId(form.getHomeworkClassId());
		homework.setHomeworkTime(0);
		homework.setMetaKnowpoints(form.getMetaKnowpoints());
		homework.setKnowledgePoints(form.getKnowledgePoints());
		homework.setName(form.getName());
		homework.setQuestionCount(form.getQuestionCount());
		homework.setStartTime(form.getStartTime());
		homework.setType(form.getType());
		homework.setOriginalCreateId(form.getCreateId());
		if (form.getStartTime().getTime() <= System.currentTimeMillis()) {
			homework.setStatus(HomeworkStatus.PUBLISH);
		} else {
			homework.setStatus(HomeworkStatus.INIT);
		}
		holidayHomeworkRepo.save(homework);

		for (Map<String, Object> sectionQuestion : form.getSectionQuestions()) {
			HolidayHomeworkItemPublishForm itemForm = new HolidayHomeworkItemPublishForm();
			JSONArray jsonArray = (JSONArray) sectionQuestion.get("questions");
			List<Long> questionIds = new ArrayList<Long>(jsonArray.size());
			for (Object obj : jsonArray) {
				questionIds.add(Long.valueOf(obj.toString()));
			}
			jsonArray = (JSONArray) sectionQuestion.get("metaKnows");
			if (jsonArray != null && jsonArray.size() > 0) {
				List<Long> metaKnows = new ArrayList<Long>(jsonArray.size());
				for (Object obj : jsonArray) {
					metaKnows.add(Long.valueOf(obj.toString()));
				}
				itemForm.setMetaKnows(metaKnows);
			}

			jsonArray = (JSONArray) sectionQuestion.get("knowledgePoints");
			if (jsonArray != null && jsonArray.size() > 0) {
				List<Long> knowledgePoints = new ArrayList<Long>(jsonArray.size());
				for (Object obj : jsonArray) {
					knowledgePoints.add(Long.valueOf(obj.toString()));
				}
				itemForm.setKnowedgePoints(knowledgePoints);
			}

			itemForm.setDifficulty(BigDecimal.valueOf(Double.valueOf(sectionQuestion.get("difficulty").toString())));
			itemForm.setHkId(homework.getId());
			itemForm.setClassId(form.getHomeworkClassId());
			itemForm.setName(((JSONObject) sectionQuestion.get("section")).getString("name"));
			itemForm.setStartTime(form.getStartTime());
			itemForm.setCreateId(form.getCreateId());
			itemForm.setStatus(homework.getStatus());
			itemForm.setType(homework.getType());
			itemForm.setQuestionIds(questionIds);
			itemForm.setDeadline(form.getDeadline());

			HolidayHomeworkItem item = holidayHomeworkItemService.create(itemForm);
			holidayHomeworkItemQuestionService.create(questionIds, item.getId());
		}

		return homework;
	}

	@Override
	public CursorPage<Long, HolidayHomework> queryNotPublishHomework(Date now, CursorPageable<Long> pageable) {
		return holidayHomeworkRepo.find("$queryNotPublishHomework", Params.param("nowtime", now)).fetch(pageable);
	}

	@Override
	@Transactional
	public void updateHolidayHomeworkStatus(long id, HomeworkStatus homeworkStatus) {
		holidayHomeworkRepo.execute("$updateStatus", Params.param("id", id).put("status", homeworkStatus.getValue()));
	}

	@Transactional
	@Override
	public int delete(long teacherId, long homeworkId) {
		// 更新假期作业表
		int count = holidayHomeworkRepo.execute("$updateHolidayHomework",
				Params.param("homeworkId", homeworkId).put("teacherId", teacherId));
		// 更新假期作业项表
		int count2 = hdHomeworkItemRepo.execute("$updateHolidayHomeworkItem",
				Params.param("homeworkId", homeworkId).put("teacherId", teacherId));

		// 若数据不存在，则不更新学生作业表
		if (count > 0 && count2 > 0) {
			// 更新学生作业表
			hdStuHomework.execute("$updateStuHolidayHomework", Params.param("homeworkId", homeworkId));
			// 更新学生作业项表
			hdStuHomeworkItem.execute("$updateStuHolidayHomeworkItem", Params.param("homeworkId", homeworkId));
		}
		return count;
	}

	@Override
	public CursorPage<Long, HolidayHomework> queryAfterDeadline(Date now, CursorPageable<Long> cursor) {
		return holidayHomeworkRepo.find("$queryAfterDeadline", Params.param("now", now)).fetch(cursor);
	}

	@Override
	@Transactional
	public void updateStatus(Long id, HomeworkStatus status) {
		updateAfterDeadLine(id);
		holidayHomeworkRepo.execute("$updateStatus", Params.param("id", id).put("status", status.getValue()));
		hdHomeworkItemRepo.execute("$updateStatusByHomework",
				Params.param("hdHkId", id).put("status", status.getValue()));
		hdStuHomework.execute("$updateStatus",
				Params.param("hdHkId", id).put("status", StudentHomeworkStatus.ISSUED.getValue()));
	}

	@Transactional
	@Override
	public void uptHolidayHomeworkCompleRate(long holidayHomeworkId) {
		HolidayHomework homework = holidayHomeworkRepo.get(holidayHomeworkId);
		List<HolidayStuHomeworkItem> hdStuHomeworkItem = stuHomeworkServce.queryStuHkItems(holidayHomeworkId, null,
				null, null);
		int submitCount = 0;
		for (HolidayStuHomeworkItem holidayStuHomeworkItem : hdStuHomeworkItem) {
			if (holidayStuHomeworkItem.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
				submitCount++;
			}
		}
		homework.setCompletionRate(BigDecimal.valueOf(Math.round(submitCount * 100 / hdStuHomeworkItem.size())));
		holidayHomeworkRepo.save(homework);
	}

	@Transactional
	@Override
	public void updateAfterDeadLine(Long holidayHomeworkId) {
		// 获取未提交的学生专项ID集合
		List<HolidayStuHomeworkItem> items = stuHomeworkServce.queryStuItems(holidayHomeworkId,
				StudentHomeworkStatus.NOT_SUBMIT);
		for (HolidayStuHomeworkItem item : items) {
			if (item.getCompletionRate() == null) {
				item.setCompletionRate(BigDecimal.valueOf(0));
			} else {
				// 如果大于0
				if (item.getCompletionRate().compareTo(BigDecimal.valueOf(0)) == 1) {
					item.setCompletionRate(BigDecimal.valueOf(100));
					item.setStatus(StudentHomeworkStatus.SUBMITED);
				} else {
					item.setCompletionRate(BigDecimal.valueOf(0));
				}
			}
			HolidayStuHomework holidayStuHomework = hdStuHomework.get(item.getHolidayStuHomeworkId());
			if (item.getCompletionRate() != null) {
				if (item.getCompletionRate().compareTo(BigDecimal.valueOf(0)) == 1) {
					holidayStuHomework.setCommitItemCount(holidayStuHomework.getCommitItemCount() + 1);
				}
			}
			hdStuHomework.save(holidayStuHomework);
			HolidayHomeworkItem holidayHomeworkItem = hdHomeworkItemRepo.get(item.getHolidayHomeworkItemId());
			if (item.getCompletionRate() != null) {
				if (item.getCompletionRate().compareTo(BigDecimal.valueOf(0)) == 1) {
					holidayHomeworkItem.setCommitCount(holidayHomeworkItem.getCommitCount() + 1);
				}
			}
			hdStuHomeworkItem.save(item);
			hdHomeworkItemRepo.save(holidayHomeworkItem);
			holidayStuHomeworkService.uptStuHomeworkCompleteRate(item.getHolidayStuHomeworkId());
			// 只有做了才批改
			if (item.getCompletionRate() != null) {
				if (item.getCompletionRate().compareTo(BigDecimal.valueOf(0)) == 1) {
					stuHomeworkServce.correctHolidayStuHk(item.getId());
				}
			}

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("studentId", item.getStudentId());
			jsonObject.put("holidayStuHomeworkItemId", item.getId());

			mqSender.send(MqYoomathHolidayHomeworkRegistryConstants.EX_YM_HOLIDAYHOMEWORK,
					MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_COMMIT,
					MQ.builder().data(jsonObject).build());
		}
	}

	@Override
	public Page<HolidayHomework> queryHolidayHomeworkWeb2(ZyHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("createId", query.getTeacherId());
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = new HashSet<Integer>(query.getStatus().size());
			for (HomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			params.put("status", statusVals);
		}
		if (query.getClassId() == null) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(query.getTeacherId());
			Set<Long> clazzIds = Sets.newHashSet(-1L);
			for (HomeworkClazz clazz : clazzs) {
				clazzIds.add(clazz.getId());
			}
			params.put("homeworkClassIds", clazzIds);
		} else {
			params.put("homeworkClassId", query.getClassId());
		}
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keys", "%" + query.getKey().replace(" ", "") + "%");
		}
		if (query.getBeginTime() != null && query.getEndTime() != null) {
			params.put("bt", query.getBeginTime());
			params.put("et", query.getEndTime());
		}
		return holidayHomeworkRepo.find("$queryHolidayHomeworkWeb2", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	public Map<Long, Integer> queryHolidayHomeworkItemCount(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return new HashMap<Long, Integer>(0);
		}
		List list = holidayHomeworkRepo.find("$queryHolidayHomeworkItemCount", Params.param("ids", ids))
				.list(List.class);
		Map<Long, Integer> map = new HashMap<Long, Integer>(list.size());
		for (Object objs : list) {
			List obj = (List) objs;
			map.put(Long.parseLong(obj.get(0).toString()), Integer.parseInt(obj.get(1).toString()));
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Date getFirstCreateAt(Long teacherId) {
		Params params = Params.param("teacherId", teacherId);
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(teacherId);
		Set<Long> clazzIds = Sets.newHashSet();
		for (HomeworkClazz clazz : clazzs) {
			clazzIds.add(clazz.getId());
		}
		if (clazzIds.size() > 0) {
			params.put("homeworkClassIds", clazzIds);
		}
		List<Map> list = holidayHomeworkRepo.find("$getFirstCreateAt", params).list(Map.class);
		return list.size() == 0 ? null : (Date) list.get(0).get("ct");
	}

	@Override
	public long allCountByCreateId(long createId) {
		return holidayHomeworkRepo.find("$countByCreateId", Params.param("createId", createId)).count();
	}
}
