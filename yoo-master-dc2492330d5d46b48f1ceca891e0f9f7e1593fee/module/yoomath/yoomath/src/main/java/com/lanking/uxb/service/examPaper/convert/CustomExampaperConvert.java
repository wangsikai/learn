package com.lanking.uxb.service.examPaper.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperCfg;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.api.CustomExamTopicService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperCfgService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperClassService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperQuestionService;
import com.lanking.uxb.service.examPaper.value.VCustomExamPaperQuestion;
import com.lanking.uxb.service.examPaper.value.VCustomExampaper;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 组卷VO转换.
 * 
 * @author zemin.song
 *
 * @version 2016年8月15日
 */
@Component
public class CustomExampaperConvert extends Converter<VCustomExampaper, CustomExampaper, Long> {

	@Autowired
	private CustomExampaperCfgService cfg;
	@Autowired
	private CustomExampaperQuestionService customExampaperQuestionService;
	@Autowired
	private CustomExamTopicService customExamTopicService;
	@Autowired
	private CustomExampaperQuestionConvert customExampaperQuestionConvert;
	@Autowired
	private CustomExampaperClassService customExampaperClassService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;

	@Override
	protected Long getId(CustomExampaper s) {
		return s.getId();
	}

	@Override
	protected VCustomExampaper convert(CustomExampaper s) {
		if (s == null) {
			return null;
		}
		VCustomExampaper vo = new VCustomExampaper();
		vo.setId(s.getId());
		vo.setTitle(s.getName());
		vo.setStatus(s.getStatus());
		vo.setDownload(s.getDownload());
		vo.setDifficulty(s.getDifficulty());
		vo.setTime(s.getTime());
		vo.setScore(s.getScore());
		vo.setQuestionCount(s.getQuestionCount());
		vo.setUpdateAt(s.getUpdateAt());
		vo.setEnableAt(s.getEnableAt());
		vo.setOpenAt(s.getOpenAt());
		vo.setType(s.getType());
		return vo;
	}

	public VCustomExampaper to(CustomExampaper cep, CustomExampaperConvertOption opn) {
		cep.setClazz(opn.isShowClazz());
		cep.setShowQuestions(opn.isShowQuestions());
		cep.setShowTopic(opn.isShowTopic());
		return super.to(cep);
	}

	public List<VCustomExampaper> to(List<CustomExampaper> ceps, CustomExampaperConvertOption opn) {
		for (CustomExampaper cep : ceps) {
			cep.setClazz(opn.isShowClazz());
			cep.setShowQuestions(opn.isShowQuestions());
			cep.setShowTopic(opn.isShowTopic());
		}
		return super.to(ceps);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VCustomExampaper, CustomExampaper, Long, CustomExampaperCfg>() {

			@Override
			public boolean accept(CustomExampaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CustomExampaper s, VCustomExampaper d) {
				return s.getId();
			}

			@Override
			public void setValue(CustomExampaper s, VCustomExampaper d, CustomExampaperCfg value) {
				d.setCfg(value);
			}

			@Override
			public CustomExampaperCfg getValue(Long key) {
				return cfg.get(key);
			}

			@Override
			public Map<Long, CustomExampaperCfg> mgetValue(Collection<Long> keys) {
				return cfg.mget(keys);
			}

		});

		assemblers
				.add(new ConverterAssembler<VCustomExampaper, CustomExampaper, Long, List<VCustomExamPaperQuestion>>() {

					@Override
					public boolean accept(CustomExampaper s) {
						return s.isShowQuestions();
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(CustomExampaper s, VCustomExampaper d) {
						if (s.getId() == null) {
							return null;
						}
						return s.getId();
					}

					@Override
					public void setValue(CustomExampaper s, VCustomExampaper d, List<VCustomExamPaperQuestion> value) {
						d.setQuestions(value);
					}

					@Override
					public List<VCustomExamPaperQuestion> getValue(Long key) {
						List<CustomExampaperQuestion> ceqList = customExampaperQuestionService.findByPaper(key);
						return customExampaperQuestionConvert.to(ceqList);
					}

					@Override
					public Map<Long, List<VCustomExamPaperQuestion>> mgetValue(Collection<Long> keys) {

						return null;
					}

				});

		assemblers.add(new ConverterAssembler<VCustomExampaper, CustomExampaper, Long, List<CustomExampaperTopic>>() {

			@Override
			public boolean accept(CustomExampaper s) {
				return s.isShowTopic();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CustomExampaper s, VCustomExampaper d) {
				return s.getId();
			}

			@Override
			public void setValue(CustomExampaper s, VCustomExampaper d, List<CustomExampaperTopic> value) {
				d.setTopic(value);
			}

			@Override
			public List<CustomExampaperTopic> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return customExamTopicService.getTopicsByExamPaperId(key);
			}

			@Override
			public Map<Long, List<CustomExampaperTopic>> mgetValue(Collection<Long> keys) {
				return null;
			}

		});

		assemblers.add(new ConverterAssembler<VCustomExampaper, CustomExampaper, Long, List<VHomeworkClazz>>() {

			@Override
			public boolean accept(CustomExampaper customExampaper) {
				return customExampaper.getStatus() == CustomExampaperStatus.OPEN
						|| customExampaper.getType() == CustomExampaperType.SMART;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CustomExampaper customExampaper, VCustomExampaper vCustomExampaper) {
				return customExampaper.getId();
			}

			@Override
			public void setValue(CustomExampaper customExampaper, VCustomExampaper vCustomExampaper,
					List<VHomeworkClazz> value) {
				vCustomExampaper.setOpenClasses(value);
			}

			@Override
			public List<VHomeworkClazz> getValue(Long key) {
				List<Long> ids = customExampaperClassService.findByPaper(key);
				int classSize = ids.size();
				List<HomeworkClazz> clazzs = homeworkClassService.mgetListEnableClazz(ids);
				List<VHomeworkClazz> vclazzs = homeworkClazzConvert.to(clazzs);
				Map<Long, VHomeworkClazz> clazzMap = new HashMap<Long, VHomeworkClazz>(classSize);
				for (VHomeworkClazz v : vclazzs) {
					clazzMap.put(v.getId(), v);
				}
				vclazzs = new ArrayList<VHomeworkClazz>(classSize);
				for (Long id : ids) {
					VHomeworkClazz vclazz = clazzMap.get(id);
					if (vclazz != null) {
						vclazzs.add(vclazz);
					}
				}
				return vclazzs;
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, List<VHomeworkClazz>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				Map<Long, List<Long>> classMap = customExampaperClassService.findByPapers(keys);
				List<Long> classIds = Lists.newArrayList();
				for (List<Long> list : classMap.values()) {
					classIds.addAll(list);
				}

				Map<Long, VHomeworkClazz> enableClassMap = homeworkClazzConvert.to(homeworkClassService
						.mgetEnableClazz(classIds));
				Map<Long, List<VHomeworkClazz>> retMap = new HashMap<Long, List<VHomeworkClazz>>(classMap.size());

				for (Map.Entry<Long, List<Long>> entry : classMap.entrySet()) {
					List<VHomeworkClazz> clazzs = new ArrayList<VHomeworkClazz>(entry.getValue().size());
					for (Long classId : entry.getValue()) {
						if (enableClassMap.get(classId) == null) {
							continue;
						}

						clazzs.add(enableClassMap.get(classId));
					}

					retMap.put(entry.getKey(), clazzs);
				}
				return retMap;
			}
		});

	}
}
