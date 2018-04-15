package com.lanking.uxb.service.examPaper.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopicType;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.CustomExampaperService;
import com.lanking.uxb.service.examPaper.cache.CustomExampaperStudentNoticeCacheService;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvert;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvertOption;
import com.lanking.uxb.service.examPaper.value.VCustomExamPaperQuestion;
import com.lanking.uxb.service.examPaper.value.VCustomExampaper;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 组卷相关接口
 * 
 * @since 2.5.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年10月8日
 */
@RestController
@RequestMapping(value = "zy/m/t/ep")
public class ZyMTeaCustomExampaperController {

	private Logger logger = LoggerFactory.getLogger(ZyMTeaCustomExampaperController.class);

	@Autowired
	private CustomExampaperService exampaperService;
	@Autowired
	private CustomExampaperStudentNoticeCacheService cacheService;
	@Autowired
	private CustomExampaperConvert customExampaperConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(Long cursor, @RequestParam(value = "size", defaultValue = "20") int size) {
		size = Math.min(size, 20);
		long now = System.currentTimeMillis();
		cursor = cursor == null ? now : (cursor > now ? now : cursor);
		CursorPage<Date, CustomExampaper> cursorPage = exampaperService.queryCustomExampapers(Security.getUserId(),
				CP.cursor(new Date(cursor), size));
		VCursorPage<VCustomExampaper> vcursorPage = new VCursorPage<VCustomExampaper>();
		vcursorPage.setCursor(cursorPage.getNextCursor() == null ? cursor : cursorPage.getNextCursor().getTime());
		if (cursorPage.isNotEmpty()) {
			vcursorPage.setItems(customExampaperConvert.to(cursorPage.getItems()));
		} else {
			vcursorPage.setItems(Collections.EMPTY_LIST);
		}
		return new Value(vcursorPage);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail(long id) {
		CustomExampaper cep = exampaperService.get(id);
		if (cep.getStatus() == CustomExampaperStatus.DELETE || cep.getStatus() == CustomExampaperStatus.DRAFT) {
			return new Value(new IllegalArgException());
		}
		if (cep.getCreateId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}
		CustomExampaperConvertOption ceco = new CustomExampaperConvertOption(true, true, true);
		VCustomExampaper customExampaper = customExampaperConvert.to(cep, ceco);
		ValueMap vm = ValueMap.value();
		// 获取可选班级列表
		if (customExampaper.getStatus() == CustomExampaperStatus.ENABLED
				&& customExampaper.getType() == CustomExampaperType.MANUAL) {
			List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isEmpty(clazzs)) {
				customExampaper.setOpenClasses(Collections.EMPTY_LIST);
			} else {
				int classSize = clazzs.size();
				Map<Long, VHomeworkClazz> vMap = new HashMap<Long, VHomeworkClazz>(classSize);
				List<VHomeworkClazz> vList = homeworkClassConvert.to(clazzs);
				for (VHomeworkClazz v : vList) {
					vMap.put(v.getId(), v);
				}
				vList = new ArrayList<VHomeworkClazz>(classSize);
				for (HomeworkClazz s : clazzs) {
					vList.add(vMap.get(s.getId()));
				}
				customExampaper.setOpenClasses(vList);
			}
		} else {
			// 重新过滤掉已经转让给其他老师的班级
			List<VHomeworkClazz> classList = new ArrayList<VHomeworkClazz>();
			List<VHomeworkClazz> openClasses = customExampaper.getOpenClasses();
			for (VHomeworkClazz v : openClasses) {
				if (v.getTeacherId() == Security.getUserId()) {
					classList.add(v);
				}
			}
			customExampaper.setOpenClasses(classList);
		}
		// 获取难度分布情况和题目列表
		Map<String, List<VCustomExamPaperQuestion>> questionMap = Maps.newHashMap();
		Map<String, List<Integer>> diffCountMap = Maps.newHashMap();
		for (CustomExampaperTopic ct : customExampaper.getTopic()) {
			questionMap.put(ct.getId().toString(), Lists.<VCustomExamPaperQuestion> newArrayList());
			diffCountMap.put(ct.getId().toString(), Lists.newArrayList(0, 0, 0));
		}
		for (VCustomExamPaperQuestion vq : customExampaper.getQuestions()) {
			String topicId = String.valueOf(vq.getTopicId());
			questionMap.get(topicId).add(vq);
			VQuestion v = vq.getQuestion();
			if (v.getDifficulty() >= .8) {
				diffCountMap.get(topicId).add(0, diffCountMap.get(topicId).get(0) + 1);
				diffCountMap.get(topicId).remove(1);
			} else if (v.getDifficulty() >= .4 && v.getDifficulty() < .8) {
				diffCountMap.get(topicId).add(1, diffCountMap.get(topicId).get(1) + 1);
				diffCountMap.get(topicId).remove(2);
			} else if (v.getDifficulty() < .4) {
				diffCountMap.get(topicId).add(2, diffCountMap.get(topicId).get(2) + 1);
				diffCountMap.get(topicId).remove(3);
			}
		}
		customExampaper.setQuestions(null);
		vm.put("customExampaper", customExampaper);
		Map<String, Map<String, Object>> map = Maps.newHashMap();
		for (CustomExampaperTopic ct : customExampaper.getTopic()) {
			Map<String, Object> oneMap = Maps.newHashMap();
			List<VCustomExamPaperQuestion> qs = questionMap.get(ct.getId().toString());
			oneMap.put("questions", qs);
			oneMap.put("questionDifficulty", diffCountMap.get(ct.getId().toString()));
			StringBuilder title = new StringBuilder("共").append(qs.size()).append("题");
			int total = 0;
			for (VCustomExamPaperQuestion vc : qs) {
				total += vc.getScore();
			}
			if (ct.getType() == CustomExampaperTopicType.SINGLE_CHOICE) {
				title.append("，每题").append(customExampaper.getCfg().getSingleChoiceScore().intValue()).append("分");
			} else if (ct.getType() == CustomExampaperTopicType.FILL_BLANK) {
				title.append("，每空").append(customExampaper.getCfg().getFillBlankScore().intValue()).append("分");
			}
			title.append("，总计").append(total).append("分");
			oneMap.put("title", title.toString());
			map.put(ct.getId().toString(), oneMap);
		}
		vm.put("questionInfo", map);
		return new Value(vm);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "open", method = { RequestMethod.POST, RequestMethod.GET })
	public Value open(final long id, @RequestParam(value = "classIds") final List<Long> classIds) {
		CustomExampaper paper = exampaperService.get(id);
		if (paper == null || (paper.getType() == CustomExampaperType.MANUAL && CollectionUtils.isEmpty(classIds))) {
			return new Value(new IllegalArgException());
		}
		if (paper.getStatus() != CustomExampaperStatus.ENABLED) {// 必须
			return new Value(new IllegalArgException());
		}
		// 更新状态分发学生
		final long nowMillis = System.currentTimeMillis();
		exampaperService.updateStatus(id, CustomExampaperStatus.OPEN);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<HomeworkStudentClazz> students = exampaperService.open(id, classIds);
					for (HomeworkStudentClazz s : students) {
						cacheService.update(s.getStudentId(), nowMillis);
					}
				} catch (Exception e) {
					exampaperService.updateStatus(id, CustomExampaperStatus.ENABLED);
					logger.error("open paper error paperId: {}", id);
				}
			}
		});
		return new Value();
	}

	/**
	 * 删除组卷
	 * 
	 * @since 2.5.0
	 * @param id
	 *            组卷ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(long id) {
		exampaperService.deleteCustomExamPaper(id);
		return new Value();
	}
}
