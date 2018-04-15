package com.lanking.uxb.service.resources.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessage;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.file.api.QiNiuFileService;
import com.lanking.uxb.service.resources.api.HomeworkMessageService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VHomeworkMessage;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;

@Component
public class StudentHomeworkConvert extends Converter<VStudentHomework, StudentHomework, Long> {

	@Autowired
	private UserConvert userConvert;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private HomeworkMessageService hkMessageService;
	@Autowired
	private QiNiuFileService qiNiuFileService;
	@Autowired
	private ZyStudentHomeworkQuestionService stuHkQuestionService;
	@Override
	protected Long getId(StudentHomework s) {
		return s.getId();
	}

	public VStudentHomework to(StudentHomework s, boolean statisticCorrected, boolean initHomework, boolean initUser,
			boolean simpleHomework) {
		s.setStatisticCorrected(statisticCorrected);
		s.setInitHomework(initHomework);
		s.setInitUser(initUser);
		s.setSimpleHomework(simpleHomework);
		return super.to(s);
	}

	public VStudentHomework to(StudentHomework s, StudentHomeworkConvertOption option) {
		s.setStatisticCorrected(option.isStatisticCorrected());
		s.setInitHomework(option.isInitHomework());
		s.setInitUser(option.isInitUser());
		s.setSimpleHomework(option.isSimpleHomework());
		s.setInitStuHomeworkWrongAndCorrect(option.isInitStuHomeworkWrongAndCorrect());
		s.setInitStuHomeworkCorrectedAndCorrecting(option.isInitStuHomeworkCorrectedAndCorrecting());
		s.setStatisticTobeCorrected(option.isStatisticTobeCorrected());
		s.setInitMessages(option.isInitMessages());
		return super.to(s);
	}
	
	public List<VStudentHomework> to(List<StudentHomework> ss, boolean statisticCorrected, boolean initHomework,
			boolean initUser, boolean simpleHomework) {
		for (StudentHomework studentHomework : ss) {
			studentHomework.setStatisticCorrected(statisticCorrected);
			studentHomework.setInitHomework(initHomework);
			studentHomework.setInitUser(initUser);
			studentHomework.setSimpleHomework(simpleHomework);
		}
		return super.to(ss);
	}

	public List<VStudentHomework> to(List<StudentHomework>ss ,StudentHomeworkConvertOption option){
		for (StudentHomework studentHomework : ss) {
			studentHomework.setStatisticCorrected(option.isStatisticCorrected());
			studentHomework.setInitHomework(option.isInitHomework());
			studentHomework.setInitUser(option.isInitUser());
			studentHomework.setSimpleHomework(option.isSimpleHomework());
			studentHomework.setInitStuHomeworkWrongAndCorrect(option.isInitStuHomeworkWrongAndCorrect());
			studentHomework.setInitStuHomeworkCorrectedAndCorrecting(option.isInitStuHomeworkCorrectedAndCorrecting());
			studentHomework.setStatisticTobeCorrected(option.isStatisticTobeCorrected());
			studentHomework.setInitMessages(option.isInitMessages());
		}
		return super.to(ss);
	}
	public Map<Long, VStudentHomework> to(Map<Long, StudentHomework> sMap, boolean statisticCorrected,
			boolean initHomework, boolean initUser, boolean simpleHomework) {
		for (StudentHomework s : sMap.values()) {
			s.setStatisticCorrected(statisticCorrected);
			s.setInitHomework(initHomework);
			s.setInitUser(initUser);
			s.setSimpleHomework(simpleHomework);
		}
		return super.to(sMap);
	}
	
	public Map<Long, VStudentHomework> to(Map<Long, StudentHomework> sMap ,StudentHomeworkConvertOption option){
		for (StudentHomework s : sMap.values()) {
			s.setStatisticCorrected(option.isStatisticCorrected());
			s.setInitHomework(option.isInitHomework());
			s.setInitUser(option.isInitUser());
			s.setSimpleHomework(option.isSimpleHomework());
			s.setInitStuHomeworkWrongAndCorrect(option.isInitStuHomeworkWrongAndCorrect());
			s.setInitStuHomeworkCorrectedAndCorrecting(option.isInitStuHomeworkCorrectedAndCorrecting());
			s.setStatisticTobeCorrected(option.isStatisticTobeCorrected());
			s.setInitMessages(option.isInitMessages());
		}
		return super.to(sMap);
	}

	public Map<Long, VStudentHomework> toMap(List<StudentHomework> ss, boolean statisticCorrected, boolean initHomework,
			boolean initUser, boolean simpleHomework) {
		for (StudentHomework studentHomework : ss) {
			studentHomework.setStatisticCorrected(statisticCorrected);
			studentHomework.setInitHomework(initHomework);
			studentHomework.setInitUser(initUser);
			studentHomework.setSimpleHomework(simpleHomework);
		}
		return super.toMap(ss);
	}

	@Override
	protected VStudentHomework convert(StudentHomework s) {
		VStudentHomework v = new VStudentHomework();
		v.setCreateAt(s.getCreateAt());
		v.setHomeworkId(s.getHomeworkId());
		v.setId(s.getId());
		v.setIssueAt(s.getIssueAt());
		v.setStudentId(s.getStudentId());
		v.setSubmitAt(s.getSubmitAt());
		v.setStuSubmitAt(s.getStuSubmitAt());
		v.setRightRate(s.getRightRate());
		v.setRightRateCorrect(s.getRightRateCorrect());
		v.setStatus(s.getStatus());
		v.setHomeworkTime(s.getHomeworkTime() == null ? 0 : s.getHomeworkTime());
		v.setRank(s.getRank() != null ? s.getRank() : s.getRightRate() == null ? 0 : 1);
		v.setCorrected(s.isCorrected());
		v.setStudentCorrected(s.isStudentCorrected());
		v.setWrongCount(s.getWrongCount() == null ? 0 : s.getWrongCount());
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());
		v.setHalfWrongCount(s.getHalfWrongCount() == null ? 0 : s.getHalfWrongCount());
		v.setAutoManualAllCorrected(s.isAutoManualAllCorrected());
		v.setCompletionRate(s.getCompletionRate() == null
				? BigDecimal.valueOf(0)
				: s.getCompletionRate().setScale(0, BigDecimal.ROUND_HALF_UP));
		v.setUpdateAt(s.getUpdateAt());
		
		v.setCorrectStatus(s.getCorrectStatus());
		
		v.setRevisalAnswerCorrectStatus(shqService.getCorrectAnswerCorrectStatus(s.getId()));
		// 处理android bug
		if (s.getStatus() == StudentHomeworkStatus.ISSUED && s.getRightRate() != null
				&& s.getCompletionRate() == null) {
			v.setCompletionRate(BigDecimal.valueOf(100));
		}
		
		long teacherCorrectCount = shqService.getCorrectAnswerTeacherCorrectCount(s.getId());
		if (teacherCorrectCount > 0L) {
			v.setRevisalAnswerTeacherCorrectStatus(false);
		} else {
			v.setRevisalAnswerTeacherCorrectStatus(true);
		}
		
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 设置用户信息
		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework, Long, VUser>() {
			@Override
			public boolean accept(StudentHomework s) {
				return s.isInitUser();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework s, VStudentHomework d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(StudentHomework s, VStudentHomework d, VUser value) {
				d.setUser(value);
			}

			@Override
			public VUser getValue(Long key) {
				return userConvert.get(key, new UserConvertOption(true));
			}

			@Override
			public Map<Long, VUser> mgetValue(Collection<Long> keys) {
				return userConvert.mget(keys, new UserConvertOption(true));
			}

		});
		// 设置作业
		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework, String, VHomework>() {

			@Override
			public boolean accept(StudentHomework s) {
				return s.isInitHomework();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public String getKey(StudentHomework s, VStudentHomework d) {
				return s.getHomeworkId() + "_" + s.isSimpleHomework();
			}

			@Override
			public void setValue(StudentHomework s, VStudentHomework d, VHomework value) {
				if (value != null) {
					d.setHomework(value);
					if (s.getCompletionRate() != null) {
						int completionCount = BigDecimal.valueOf(value.getQuestionCount()
								* d.getCompletionRate().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 100)
								.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
						d.setCompletionCount(completionCount);
					}
				}
			}

			@Override
			public VHomework getValue(String key) {
				if (key == null) {
					return null;
				}
				Long hid = Long.parseLong(key.split("_")[0]);
				boolean isSimpleHomework = Boolean.parseBoolean(key.split("_")[1]);
				Homework homework = homeworkService.get(hid);

				VHomework vhomework = null;
				if (isSimpleHomework) {
					vhomework = homeworkConvert.simpleConvert(homework);
				} else {
					HomeworkConvertOption option = new HomeworkConvertOption();
					option.setInitCount(true);
					option.setInitExercise(true);
					option.setInitMetaKnowpoint(false);
					option.setInitKnowledgePoint(true);
					option.setInitSectionOrBookCatalog(true);
					vhomework = homeworkConvert.to(homework, option);
				}

				return vhomework;
			}

			@Override
			public Map<String, VHomework> mgetValue(Collection<String> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}

				List<Long> simpleHids = new ArrayList<Long>(keys.size());
				List<Long> notSimpleHids = new ArrayList<Long>(keys.size());
				for (String key : keys) {
					long hid = Long.parseLong(key.split("_")[0]);
					boolean isSimpleHomework = Boolean.parseBoolean(key.split("_")[1]);
					if (isSimpleHomework) {
						simpleHids.add(hid);
					} else {
						notSimpleHids.add(hid);
					}
				}

				Map<String, VHomework> vhomeworkMap = new HashMap<String, VHomework>(keys.size());
				if (simpleHids.size() > 0) {
					List<Homework> homeworks = homeworkService.mgetList(simpleHids);
					for (Homework h : homeworks) {
						vhomeworkMap.put(h.getId() + "_true", homeworkConvert.simpleConvert(h));
					}
				}
				if (notSimpleHids.size() > 0) {
					List<Homework> homeworks = homeworkService.mgetList(notSimpleHids);
					HomeworkConvertOption option = new HomeworkConvertOption();
					option.setInitCount(true);
					option.setInitExercise(true);
					option.setInitMetaKnowpoint(false);
					option.setInitKnowledgePoint(true);
					option.setInitSectionOrBookCatalog(true);
					List<VHomework> vhomeworks = homeworkConvert.to(homeworks, option);
					for (VHomework h : vhomeworks) {
						vhomeworkMap.put(h.getId() + "_false", h);
					}
				}
				return vhomeworkMap;
			}
		});

		// 设置是否批改完
//		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework, Long, Boolean>() {
//
//			@Override
//			public boolean accept(StudentHomework s) {
//				return s.isStatisticCorrected();
//			}
//
//			@Override
//			public boolean accept(Map<String, Object> hints) {
//				return true;
//			}
//
//			@Override
//			public Long getKey(StudentHomework s, VStudentHomework d) {
//				return s.getId();
//			}
//
//			@Override
//			public void setValue(StudentHomework s, VStudentHomework d, Boolean value) {
//				if (value != null) {
//					d.setCorrected(value);
//				}
//			}
//
//			@Override
//			public Boolean getValue(Long key) {
//				if (key == null) {
//					return null;
//				}
//				Set<Long> keys = Sets.newHashSet();
//				keys.add(key);
//				return shaService.countNotCorrected(keys).get(key) == 0;
//			}
//
//			@Override
//			public Map<Long, Boolean> mgetValue(Collection<Long> keys) {
//				Map<Long, Boolean> map = Maps.newHashMap();
//				if (CollectionUtils.isEmpty(keys)) {
//					return map;
//				}
//				Map<Long, Long> countMap = shaService.countNotCorrected(keys);
//				for (Long key : countMap.keySet()) {
//					if (countMap.get(key) == 0) {
//						map.put(key, true);
//					} else {
//						map.put(key, false);
//					}
//				}
//				return map;
//			}
//
//		});
//		
		// 设置学生作业留言信息
		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework, Long, List<VHomeworkMessage>>() {
			@Override
			public boolean accept(StudentHomework s) {
				return s.isInitMessages();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework s, VStudentHomework d) {
				//目前没有针对单个学生作业的留言，获取的是老师对整份作业的留言
				return s.getHomeworkId();
			}

			@Override
			public void setValue(StudentHomework s, VStudentHomework d, List<VHomeworkMessage> value) {
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
		
		//设置错题数、已订正数
		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework,Long,Map<Long,Map>>(){

			@Override
			public boolean accept(StudentHomework s) {
				return s.isInitStuHomeworkWrongAndCorrect();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework s, VStudentHomework d) {
				return s.getId();
			}

			@Override
			public void setValue(StudentHomework s, VStudentHomework d, Map<Long,Map> value) {
				if(null != value && !value.isEmpty()){
					d.setRevisalQuestionTotal(Integer.parseInt(value.get("wrong_count")+""));
					d.setRevisaledQuestionCount(Integer.parseInt(value.get("correct_count")+""));
				}
			}

			@Override
			public Map getValue(Long key) {
				if(null != key){
					Collection<Long> keys = new ArrayList<Long>();
					keys.add(key);
					List<Map> list = stuHkQuestionService.findWrongAndcorrectionQuestion(keys);
					if(null != list && list.size()>0){
						return list.get(0);
					}
				}
				return new HashMap();
			}

			@Override
			public Map<Long, Map<Long, Map>> mgetValue(Collection<Long> keys) {
				List<Map> list = stuHkQuestionService.findWrongAndcorrectionQuestion(keys);
				Map<Long, Map<Long,Map>> returnValue = new HashMap<Long,Map<Long,Map>>();
				if(null != list && list.size()>0){
					for(Map map:list){
						returnValue.put(Long.parseLong(String.valueOf(map.get("id"))), map);
					}
				}
				return returnValue;
			}
			
		});
		
		//统计正在人工批改的题和已经批改过的题
		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework,String,Map<Long,Map>>(){

			@Override
			public boolean accept(StudentHomework s) {
				return s.isInitStuHomeworkCorrectedAndCorrecting();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public String getKey(StudentHomework s, VStudentHomework d) {
				return s.getId()+"_"+s.getCorrectStatus();
			}

			@Override
			public void setValue(StudentHomework s, VStudentHomework d, Map<Long,Map> value) {
				if(null != value && !value.isEmpty()){
					d.setCorrectedCount(Integer.parseInt(value.get("corrected_count")+""));
					d.setCorrectingCount(Integer.parseInt(value.get("correcting_count")+""));
				}
			}

			@Override
			public Map getValue(String key) {
				if (null != key){
					Collection<Long> studentHomeworkIds = new ArrayList<Long>();
					String[] keyArray = key.split("_");
					Long studentHomeworkId = Long.parseLong(keyArray[0]);
					String correctStatus = keyArray[1];
					//只有批改中的状态的学生作业才需要去查询正在人工批改的题和已经批改过的题
					if("CORRECTING".equals(correctStatus)){
						studentHomeworkIds.add(studentHomeworkId);
					}
					if(studentHomeworkIds.size()<=0){
						return new HashMap();
					}
					List<Map> list = stuHkQuestionService.findCorrectedAndcorrectingQuestion(studentHomeworkIds);
					if(null != list && list.size()>0){
						return list.get(0);
					}
				}
				return new HashMap();
			}

			@Override
			public Map<String, Map<Long, Map>> mgetValue(Collection<String> keys) {
				if (null != keys && keys.size()>0){
					Collection<Long> studentHomeworkIds = new ArrayList<Long>();
					for(String key:keys){
						String[] keyArray = key.split("_");
						Long studentHomeworkId = Long.parseLong(keyArray[0]);
						String correctStatus = keyArray[1];
						//只有批改中的状态的学生作业才需要去查询已批改好的题目数和正在人工批改中的题目数
						if("CORRECTING".equals(correctStatus)){
							studentHomeworkIds.add(studentHomeworkId);
						}
					}
					if(studentHomeworkIds.size()<=0){
						return new HashMap();
					}
					List<Map> list = stuHkQuestionService.findCorrectedAndcorrectingQuestion(studentHomeworkIds);
					Map<String, Map<Long,Map>> returnValue = null;
					if(null != list && list.size()>0){
						returnValue = new HashMap<String,Map<Long,Map>>();
						for(Map map:list){
							returnValue.put(map.get("stu_homework_id")+"_CORRECTING", map);
						}
					}
					return returnValue;
				}
				return new HashMap();
			}

		});
		// 统计待批改题目数
		assemblers.add(new ConverterAssembler<VStudentHomework, StudentHomework, Long, Integer>() {

			@Override
			public boolean accept(StudentHomework s) {
				return s.isStatisticTobeCorrected();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework s, VStudentHomework d) {
//				if(null != s.getCorrectStatus() && s.getCorrectStatus()==StudentHomeworkCorrectStatus.TOBE_CORRECTED){
//					return s.getId();
//				}else{
//					return null;
//				}
				return s.getId();
			}

			@Override
			public void setValue(StudentHomework s, VStudentHomework d, Integer value) {
				if (value != null) {
					d.setToBeCorrectedCount(value);
				}
			}

			@Override
			public Integer getValue(Long key) {
				if (null != key) {
					Collection<Long> keys = new ArrayList<Long>();
					keys.add(key);
					List<Map> maps = stuHkQuestionService.staticToBeCorrectedQuestionCount(keys);
					if(null != maps && maps.size()>0){
						Integer result = Integer.parseInt(maps.get(0).get("tobecorrectedcount")+"");
						return result;
					}
				}
				return 0;
			}

			@Override
			public Map<Long, Integer> mgetValue(Collection<Long> keys) {
				Map<Long, Integer> resurltMap = Maps.newHashMap();
				if(null != keys && keys.size()>0){
					List<Map> maps = stuHkQuestionService.staticToBeCorrectedQuestionCount(keys);
					for(Map map : maps){
						resurltMap.put(Long.parseLong(map.get("stuhomeworkid")+""), Integer.parseInt(map.get("tobecorrectedcount")+""));
					}
				}
				return resurltMap;
			}
		});
	}
}
