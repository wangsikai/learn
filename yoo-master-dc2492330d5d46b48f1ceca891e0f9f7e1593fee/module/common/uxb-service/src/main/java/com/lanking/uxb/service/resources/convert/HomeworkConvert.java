package com.lanking.uxb.service.resources.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessage;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.counter.api.impl.HomeworkCounterProvider;
import com.lanking.uxb.service.file.api.QiNiuFileService;
import com.lanking.uxb.service.resources.api.ExerciseService;
import com.lanking.uxb.service.resources.api.HomeworkMessageService;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VHomeworkMessage;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzGroupConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;

/**
 * 作业转换.
 * 
 * @author
 *
 * @since 学生端v1.3.4 2017-5-27 优化知识点取值方式，批量获取，添加初始化开关
 */
@Component
public class HomeworkConvert extends Converter<VHomework, Homework, Long> {

	@Autowired
	private HomeworkCounterProvider homeworkCounterProvider;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private ZyBookService bookService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private ZyHomeworkClassGroupService hkClassGroupService;
	@Autowired
	private ZyHomeworkClazzGroupConvert hkClassGroupConvert;
	@Autowired
	private HomeworkMessageService hkMessageService;
	@Autowired
	private QiNiuFileService qiNiuFileService;

	@Override
	protected Long getId(Homework s) {
		return s.getId();
	}

	public VHomework simpleConvert(Homework s) {
		VHomework v = new VHomework();
		v.setCourseId(s.getCourseId() == null ? 0 : s.getCourseId());
		v.setHomeworkClazzId(s.getHomeworkClassId() == null ? 0 : s.getHomeworkClassId());
		v.setCreateAt(s.getCreateAt());
		v.setDeadline(s.getDeadline());
		v.setExerciseId(s.getExerciseId());
		v.setId(s.getId());
		v.setIssueAt(s.getIssueAt());
		v.setName(s.getName());
		v.setStartTime(s.getStartTime());
		//@since 小优快批
		v.setNewHomeworkStatus(getStatus(s.getStartTime().getTime(), s.getDeadline().getTime(), s));
		v.setTobeCorrected(s.isTobeCorrected());//待批改标记，存在于作业中和已截止状态中
		//v.setStatus(s.getStatus());
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());
		v.setWrongCount(s.getWrongCount() == null ? 0 : s.getWrongCount());
		v.setHalfWrongCount(s.getHalfWrongCount() == null ? 0 : s.getHalfWrongCount());
		v.setRightRate(s.getRightRate());
		v.setHomeworkTime(s.getHomeworkTime() == null ? 0 : s.getHomeworkTime());
		v.setDistributeCount(s.getDistributeCount() == null ? 0 : s.getDistributeCount());
		v.setCommitCount(s.getCommitCount() == null ? 0 : s.getCommitCount());
		v.setCorrectingCount(s.getCorrectingCount() == null ? 0 : s.getCorrectingCount());
		v.setQuestionCount(s.getQuestionCount() == null ? 0 : s.getQuestionCount());
		v.setDifficulty(s.getDifficulty() == null ? BigDecimal.valueOf(0) : s.getDifficulty());
		v.setTextbookCode(s.getTextbookCode());
		v.setCorrectingType(s.getCorrectingType());
		// 1是普通作业
		v.setType(1);
		v.setDelStatus(s.getDelStatus());
		//v.setAutoIssue(s.isAutoIssue());
		return v;
	}

	/**
	 * 获取班级作业状态
	 * @since 小优快批
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private String getStatus(Long startTime, Long endTime, Homework s) {
//		Long currentTime = System.currentTimeMillis();
		String status = "0";
//		if (startTime - currentTime > 0) {
//			status = "0";// 待分发
//		} else if (startTime < currentTime && currentTime < endTime) {
//			if (s.getCommitCount().longValue() == s.getDistributeCount().longValue()) {
//				status = "2";// 已截至
//			} else {
//				status = "1";// 作业中
//			}
//		} else if (currentTime >= endTime) {
//			status = "2";// 已截至
//		}
		
		if (s.getStatus() == HomeworkStatus.INIT) {
			status = "0";// 待分发
		} else if (s.getStatus() == HomeworkStatus.PUBLISH) {
			status = "1";// 作业中
		} else {
			status = "2";// 已截至
		}
		return status;
	}

	@Override
	protected VHomework convert(Homework s) {
		VHomework v = new VHomework();
		v.setCourseId(s.getCourseId() == null ? 0 : s.getCourseId());
		v.setHomeworkClazzId(s.getHomeworkClassId() == null ? 0 : s.getHomeworkClassId());
		v.setCreateAt(s.getCreateAt());
		v.setDeadline(s.getDeadline());
		v.setExerciseId(s.getExerciseId());
		v.setId(s.getId());
		v.setIssueAt(s.getIssueAt());
		v.setName(s.getName());
		v.setStartTime(s.getStartTime());
		v.setStatus(s.getStatus());
		//@since 小优快批
		v.setNewHomeworkStatus(getStatus(s.getStartTime().getTime(), s.getDeadline().getTime(), s));
		v.setTobeCorrected(s.isTobeCorrected());//待批改标记，存在于作业中和已截止状态中
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());
		v.setWrongCount(s.getWrongCount() == null ? 0 : s.getWrongCount());
		v.setRightRate(s.getRightRate());
		v.setHomeworkTime(s.getHomeworkTime() == null ? 0 : s.getHomeworkTime());
		v.setDistributeCount(s.getDistributeCount() == null ? 0 : s.getDistributeCount());
		v.setCommitCount(s.getCommitCount() == null ? 0 : s.getCommitCount());
		v.setCorrectingCount(s.getCorrectingCount() == null ? 0 : s.getCorrectingCount());
		v.setQuestionCount(s.getQuestionCount() == null ? 0 : s.getQuestionCount());
		v.setDifficulty(s.getDifficulty() == null ? BigDecimal.valueOf(0) : s.getDifficulty());
		v.setTextbookCode(s.getTextbookCode());
		v.setCorrectingType(s.getCorrectingType());
		if (s.getLastCommitAt() != null) {// 最后一个已经提交
			v.setAllCommitMement(System.currentTimeMillis() > s.getLastCommitAt().getTime()
					+ Env.getInt("homework.allcommit.then") * 60 * 1000);
		}
		v.setHasQuestionAnswering(s.isHasQuestionAnswering());
		v.setDelStatus(s.getDelStatus());
		//v.setAutoIssue(s.isAutoIssue());
		v.setTimeLimit(s.getTimeLimit());
		return v;
	}

	public VHomework to(Homework s, HomeworkConvertOption option) {
		s.setInitCount(option.isInitCount());
		s.setInitExercise(option.isInitExercise());
		s.setInitKnowledgePoint(option.isInitKnowledgePoint());
		s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
		s.setInitSectionOrBookCatalog(option.isInitSectionOrBookCatalog());
		s.setInitMessages(option.isInitMessages());
		return super.to(s);
	}

	public List<VHomework> to(List<Homework> ss, HomeworkConvertOption option) {
		for (Homework s : ss) {
			s.setInitCount(option.isInitCount());
			s.setInitExercise(option.isInitExercise());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitSectionOrBookCatalog(option.isInitSectionOrBookCatalog());
			s.setInitMessages(option.isInitMessages());
		}
		return super.to(ss);
	}

	public Map<Long, VHomework> to(Map<Long, Homework> sMap, HomeworkConvertOption option) {
		for (Homework s : sMap.values()) {
			s.setInitCount(option.isInitCount());
			s.setInitExercise(option.isInitExercise());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitSectionOrBookCatalog(option.isInitSectionOrBookCatalog());
			s.setInitMessages(option.isInitMessages());
		}
		return super.to(sMap);
	}

	public Map<Long, VHomework> toMap(List<Homework> ss, HomeworkConvertOption option) {
		for (Homework s : ss) {
			s.setInitCount(option.isInitCount());
			s.setInitExercise(option.isInitExercise());
			s.setInitKnowledgePoint(option.isInitKnowledgePoint());
			s.setInitMetaKnowpoint(option.isInitMetaKnowpoint());
			s.setInitSectionOrBookCatalog(option.isInitSectionOrBookCatalog());
			s.setInitMessages(option.isInitMessages());
		}
		return super.toMap(ss);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 统计字段
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, Counter>() {

			@Override
			public boolean accept(Homework s) {
				return s.isInitCount();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				return s.getId();
			}

			@Override
			public void setValue(Homework s, VHomework d, Counter value) {
				if (value != null) {
					d.setQuestionCount(value.getCount1());
				}
			}

			@Override
			public Counter getValue(Long key) {
				return homeworkCounterProvider.getCounter(key);
			}

			@Override
			public Map<Long, Counter> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return homeworkCounterProvider.getCounters(keys);
			}
		});

		// exercise 便于后面 两个 assemblers 判断 是否进入
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, Exercise>() {

			@Override
			public boolean accept(Homework s) {
				return s.isInitExercise();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				return s.getExerciseId();
			}

			@Override
			public void setValue(Homework s, VHomework d, Exercise value) {
				s.setExercise(value);
			}

			@Override
			public Exercise getValue(Long key) {
				if (key == null) {
					return null;
				}
				return exerciseService.get(key);
			}

			@Override
			public Map<Long, Exercise> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return exerciseService.mget(keys);
			}
		});
		// 章节信息(系统自带的章节)
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, Section>() {

			@Override
			public boolean accept(Homework s) {
				return s.isInitSectionOrBookCatalog() && s.getExercise().getBookId() == null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				return s.getExercise().getSectionCode();
			}

			@Override
			public void setValue(Homework s, VHomework d, Section value) {
				d.setSectionName(value.getName());
			}

			@Override
			public Section getValue(Long key) {
				if (key == null) {
					return null;
				}
				return sectionService.get(key);
			}

			@Override
			public Map<Long, Section> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}

				return sectionService.mget(keys);
			}
		});
		// 章节信息(添加的书本中的章节名称)
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, BookCatalog>() {

			@Override
			public boolean accept(Homework s) {
				return s.isInitSectionOrBookCatalog() && s.getExercise().getBookId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				return s.getExercise().getSectionCode();
			}

			@Override
			public void setValue(Homework s, VHomework d, BookCatalog value) {
				d.setSectionName(value.getName());
			}

			@Override
			public BookCatalog getValue(Long key) {
				if (key == null) {
					return null;
				}

				return bookService.getBookCatalog(key);
			}

			@Override
			public Map<Long, BookCatalog> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return bookService.mgetBookCatalogs(Lists.newArrayList(keys));
			}
		});

		// 旧知识点
		assemblers.add(new ConverterAssembler<VHomework, Homework, List<Integer>, List<VMetaKnowpoint>>() {
			@Override
			public boolean accept(Homework s) {
				return s.isInitMetaKnowpoint();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public List<Integer> getKey(Homework s, VHomework d) {
				List<Integer> metaKnowpointCodes = Lists.newArrayList();
				List<Long> metaKnowpoints = s.getMetaKnowpoints();
				if (metaKnowpoints != null) {
					for (Long code : metaKnowpoints) {
						metaKnowpointCodes.add(code.intValue());
					}
				}
				return metaKnowpointCodes;
			}

			@Override
			public void setValue(Homework s, VHomework d, List<VMetaKnowpoint> value) {
				d.setMetaKnowpoints(value == null ? new ArrayList<VMetaKnowpoint>(0) : value);
			}

			@Override
			public List<VMetaKnowpoint> getValue(List<Integer> key) {
				return CollectionUtils.isEmpty(key) ? null
						: metaKnowpointConvert.to(metaKnowpointService.mgetList(key));
			}

			@Override
			public Map<List<Integer>, List<VMetaKnowpoint>> mgetValue(Collection<List<Integer>> keys) {
				Map<List<Integer>, List<VMetaKnowpoint>> rmap = new HashMap<List<Integer>, List<VMetaKnowpoint>>(
						keys.size());
				Set<Integer> allCodes = new HashSet<Integer>();
				Iterator<List<Integer>> iterator = keys.iterator();
				while (iterator.hasNext()) {
					allCodes.addAll(iterator.next());
				}
				if (allCodes.size() == 0) {
					return rmap;
				}
				Map<Integer, VMetaKnowpoint> vmkMap = metaKnowpointConvert.to(metaKnowpointService.mget(allCodes));
				while (iterator.hasNext()) {
					List<Integer> key = iterator.next();
					List<VMetaKnowpoint> vmks = new ArrayList<VMetaKnowpoint>(key.size());
					for (Integer code : key) {
						vmks.add(vmkMap.get(code));
					}
					rmap.put(key, vmks);
				}
				return rmap;
			}

		});

		// 新知识点
		assemblers.add(new ConverterAssembler<VHomework, Homework, List<Long>, List<VKnowledgePoint>>() {
			@Override
			public boolean accept(Homework s) {
				return s.isInitKnowledgePoint();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public List<Long> getKey(Homework s, VHomework d) {
				return s.getKnowledgePoints();
			}

			@Override
			public void setValue(Homework s, VHomework d, List<VKnowledgePoint> value) {
				d.setKnowledgePoints(value == null ? new ArrayList<VKnowledgePoint>(0) : value);
			}

			@Override
			public List<VKnowledgePoint> getValue(List<Long> key) {
				return CollectionUtils.isEmpty(key) ? null
						: knowledgePointConvert.to(knowledgePointService.mgetList(key));
			}

			@Override
			public Map<List<Long>, List<VKnowledgePoint>> mgetValue(Collection<List<Long>> keys) {
				Map<List<Long>, List<VKnowledgePoint>> rmap = new HashMap<List<Long>, List<VKnowledgePoint>>(
						keys.size());
				Set<Long> allCodes = new HashSet<Long>();
				Iterator<List<Long>> iterator = keys.iterator();
				while (iterator.hasNext()) {
					allCodes.addAll(iterator.next());
				}
				if (allCodes.size() == 0) {
					return rmap;
				}
				Map<Long, VKnowledgePoint> vmkMap = knowledgePointConvert.to(knowledgePointService.mget(allCodes));
				iterator = keys.iterator();
				while (iterator.hasNext()) {
					List<Long> key = iterator.next();
					List<VKnowledgePoint> vmks = new ArrayList<VKnowledgePoint>(key.size());
					for (Long code : key) {
						vmks.add(vmkMap.get(code));
					}
					rmap.put(key, vmks);
				}
				return rmap;
			}

		});

		// 教师名称
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, String>() {

			@Override
			public boolean accept(Homework arg0) {
				return arg0.isInitTeacherName();
			}

			@Override
			public boolean accept(Map<String, Object> arg0) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				// 班级转让过的情况下，统一取原始创建作业的用户id 2017/8/11柴林森定
				return s.getOriginalCreateId();
			}

			@Override
			public String getValue(Long key) {
				User user = userService.get(key);
				if (user != null) {
					return user.getName();
				}
				return null;
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				Map<Long, String> dataMap = new HashMap<>();
				Set<Long> ids = new HashSet<Long>(keys.size());
				for (Long id : keys) {
					ids.add(id);
				}
				Map<Long, User> userMap = userService.getUsers(ids);
				if (!userMap.isEmpty()) {
					for (Entry<Long, User> entry : userMap.entrySet()) {
						dataMap.put(entry.getKey(), entry.getValue().getName());
					}
				}

				return dataMap;
			}

			@Override
			public void setValue(Homework s, VHomework d, String value) {
				d.setTeacherName(value);
			}
		});

		// 新增布置作业给小组
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, VHomeworkClazzGroup>() {

			@Override
			public boolean accept(Homework s) {
				return s.getHomeworkClassGroupId() != null && s.getHomeworkClassGroupId() != 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				return s.getHomeworkClassGroupId();
			}

			@Override
			public void setValue(Homework s, VHomework d, VHomeworkClazzGroup value) {
				d.setGroup(value);
			}

			@Override
			public VHomeworkClazzGroup getValue(Long key) {
				return hkClassGroupConvert.to(hkClassGroupService.get(key));
			}

			@Override
			public Map<Long, VHomeworkClazzGroup> mgetValue(Collection<Long> keys) {
				return hkClassGroupConvert.to(hkClassGroupService.mget(keys));
			}

		});
		
		// 作业留言
		assemblers.add(new ConverterAssembler<VHomework, Homework, Long, List<VHomeworkMessage>>() {
			@Override
			public boolean accept(Homework s) {
				return s.isInitMessages();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VHomework d) {
				return s.getId();
			}

			@Override
			public void setValue(Homework s, VHomework d, List<VHomeworkMessage> value) {
				d.setMessages(value);
			}

			@Override
			public List<VHomeworkMessage> getValue(Long key) {
				List<VHomeworkMessage> vMessages = new ArrayList<>();
				//目前没有针对单个学生作业的留言，获取的是老师对整份作业的留言
				List<HomeworkMessage> messages = hkMessageService.findByHkId(key);
				
				if(CollectionUtils.isNotEmpty(messages)){
					for(HomeworkMessage message:messages){
						VHomeworkMessage vMessage = new VHomeworkMessage();
						vMessage.setId(message.getId());
						vMessage.setType(message.getType());
						vMessage.setCreateAt(message.getCreateAt());
						vMessage.setIconKey(message.getIconKey());
						
						if (StringUtils.isNotBlank(message.getComment())) {
							vMessage.setComment(message.getComment());
						}
						
						if (StringUtils.isNotBlank(message.getVoiceFileKey())) {
							vMessage.setVoiceUrl(qiNiuFileService.getDownloadUrl(message.getVoiceFileKey()));
							vMessage.setVoiceTime(message.getVoiceTime());
						}
						vMessages.add(vMessage);
					}
				}
				return vMessages;
			}

			@Override
			public Map<Long, List<VHomeworkMessage>> mgetValue(Collection<Long> keys) {
				Map<Long, List<VHomeworkMessage>> messageMaps = new HashMap<>();
				for(Long key:keys){
					List<VHomeworkMessage> vMessages = new ArrayList<>();
					//目前没有针对单个学生作业的留言，获取的是老师对整份作业的留言
					List<HomeworkMessage> messages = hkMessageService.findByHkId(key);
					
					if(CollectionUtils.isNotEmpty(messages)){
						for(HomeworkMessage message:messages){
							VHomeworkMessage vMessage = new VHomeworkMessage();
							vMessage.setId(message.getId());
							vMessage.setType(message.getType());
							vMessage.setCreateAt(message.getCreateAt());
							vMessage.setIconKey(message.getIconKey());
							
							if (StringUtils.isNotBlank(message.getComment())) {
								vMessage.setComment(message.getComment());
							}
							
							if (StringUtils.isNotBlank(message.getVoiceFileKey())) {
								vMessage.setVoiceUrl(qiNiuFileService.getDownloadUrl(message.getVoiceFileKey()));
								vMessage.setVoiceTime(message.getVoiceTime());
							}
							vMessages.add(vMessage);
						}
					}
					
					messageMaps.put(key, vMessages);
				}
				
				return messageMaps;
			}
		});
	}
}
