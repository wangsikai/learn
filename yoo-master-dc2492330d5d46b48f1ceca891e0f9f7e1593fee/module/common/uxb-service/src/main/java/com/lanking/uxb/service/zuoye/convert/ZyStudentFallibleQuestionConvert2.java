package com.lanking.uxb.service.zuoye.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.value.VOcrHisAnswer;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleQuestion;

/**
 * 题目搜藏列表接口用convert 删除不需要的initStudentQuestionCount
 * 
 * @since sprint-69
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年5月25日
 */
@Component
public class ZyStudentFallibleQuestionConvert2
		extends Converter<VStudentFallibleQuestion, StudentFallibleQuestion, Long> {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;

	@Override
	protected Long getId(StudentFallibleQuestion s) {
		return s.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected VStudentFallibleQuestion convert(StudentFallibleQuestion s) {
		VStudentFallibleQuestion v = new VStudentFallibleQuestion();
		v.setCreateAt(s.getCreateAt());
		v.setDoNum(s.getDoNum());
		v.setExerciseNum(s.getExerciseNum());
		v.setId(s.getId());
		v.setMistakeNum(s.getMistakeNum());
		v.setQuestionId(s.getQuestionId() == null ? 0 : s.getQuestionId());
		v.setUpdateAt(s.getUpdateAt());
		//questionId为空，是拍照错题，直接取数据库结果
		if(s.getQuestionId() == null){
			v.setLatestResult(s.getLatestResult());
		} else if (CollectionUtils.isEmpty(s.getLatestAnswer())
				|| CollectionUtils.isEmpty(s.getLatestAnswer().get(v.getQuestionId()))) {
			//看看是否拍照作答的
			if(CollectionUtils.isNotEmpty(s.getLatestAnswerImgs())){
				v.setLatestResult(s.getLatestResult());
			} else {
				v.setLatestAnswer(Collections.EMPTY_LIST);
				// 最近一次答案为空，自动判错 戚元鹏定 20170526
				v.setLatestResult(HomeworkAnswerResult.WRONG);
			}
		} else {
			List<String> latestAnswer = s.getLatestAnswer().get(v.getQuestionId());
			for (String la : latestAnswer) {
				la = StringUtils.defaultIfBlank(la);
			}
			v.setLatestAnswer(latestAnswer);
			v.setLatestResult(s.getLatestResult());
		}
		if (CollectionUtils.isNotEmpty(s.getLatestAnswerImgs())) {
			v.setLatestAnswerImg(FileUtil.getUrl(s.getLatestAnswerImgs().get(0)));
			for (Long latestAnswerImg : s.getLatestAnswerImgs()) {
				v.getLatestAnswerImgs().add(FileUtil.getUrl(latestAnswerImg));
			}
		}
		v.setLatestRightRate(s.getLatestRightRate());
		// 原错题练习处理的会有此问题，在Converter 端处理一下
		v.setSource(s.getLatestSource() != null && s.getLatestSource() == StudentQuestionAnswerSource.FALLIBLE
				? StudentQuestionAnswerSource.HOMEWORK : s.getLatestSource());
		v.setSourceTitle(v.getSource() == null ? StringUtils.EMPTY : v.getSource().getName());

		if (s.getOcrImageId() != null) {
			v.setOcrImg(FileUtil.getUrl(s.getOcrImageId()));
		}

		if (CollectionUtils.isNotEmpty(s.getOcrKnowpointCodes())) {
			List<Integer> codes = new ArrayList<Integer>(s.getOcrKnowpointCodes().size());
			for (Long c : s.getOcrKnowpointCodes()) {
				codes.add(c.intValue());
			}
			List<MetaKnowpoint> knowledgePoints = metaKnowpointService.mgetList(codes);
			v.setKnowpoints(metaKnowpointConvert.to(knowledgePoints));
			if (!Security.isClient()) {
				List<String> names = Lists.newArrayList();
				for (MetaKnowpoint m : knowledgePoints) {
					names.add(m.getName());
				}
				v.setKnowpointNames(names);
			}
		}
		if (CollectionUtils.isNotEmpty(s.getOcrHisAnswerImgs())) {
			List<VOcrHisAnswer> ocrHisAnswerImgs = Lists.newArrayList();
			for (Map a : s.getOcrHisAnswerImgs()) {
				VOcrHisAnswer temp = new VOcrHisAnswer();
				// 图片
				Long imageId = Long.parseLong(a.get("imageId").toString());
				temp.setImgUrl(FileUtil.getUrl(imageId));

				// 多图
				List<String> imgUrls = new ArrayList<String>();
				if (a.get("imageIds") != null) {
					JSONArray array = JSONArray.parseArray(a.get("imageIds").toString());
					for (int i = 0; i < array.size(); i++) {
						Object obj = array.get(i);
						if (obj != null) {
							imgUrls.add(FileUtil.getUrl(Long.parseLong(obj.toString())));
						}
					}

					// List<Long> imageIds = (List<Long>) a.get("imageIds");
					// for (Long id : imageIds) {
					// imgUrls.add(FileUtil.getUrl(id));
					// }
				} else {
					imgUrls.add(FileUtil.getUrl(imageId));
				}
				temp.setImgUrls(imgUrls);

				// 时间
				Long time = Long.parseLong(a.get("answerAt").toString());
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTimeInMillis(time);
				temp.setAnswerAt(gc.getTime());
				ocrHisAnswerImgs.add(temp);
			}

			v.setOcrHisAnswerImgs(ocrHisAnswerImgs);
		}
		if (v.getExerciseNum() >= 1) {
			v.setRightRate(new BigDecimal((v.getExerciseNum() - v.getMistakeNum()) * 100d / v.getExerciseNum())
					.setScale(0, BigDecimal.ROUND_HALF_UP));
		}
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VStudentFallibleQuestion, StudentFallibleQuestion, Long, VQuestion>() {

			@Override
			public boolean accept(StudentFallibleQuestion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentFallibleQuestion s, VStudentFallibleQuestion d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(StudentFallibleQuestion s, VStudentFallibleQuestion d, VQuestion value) {
				d.setQuestion(value);
			}

			@Override
			public VQuestion getValue(Long key) {
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnalysis(true); // 解析
				option.setAnswer(true); // 答案
				option.setCollect(true); // 收藏
				option.setInitExamination(true); // 考点
				option.setInitKnowledgePoint(true); // 新知识点
				option.setInitMetaKnowpoint(false);
				option.setInitPhase(false);
				option.setInitSub(false);
				option.setInitQuestionType(false);
				option.setInitTextbookCategory(false);
				return questionConvert.to(questionService.get(key), option);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				// 性能优化
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnalysis(true); // 解析
				option.setAnswer(true); // 答案
				option.setCollect(true); // 收藏
				option.setInitExamination(true); // 考点
				option.setInitKnowledgePoint(true); // 新知识点
				option.setInitMetaKnowpoint(false);
				option.setInitPhase(false);
				option.setInitSub(false);
				option.setInitQuestionType(false);
				option.setInitTextbookCategory(false);

				return questionConvert.to(questionService.mget(keys), option);
			}

		});
		assemblers.add(new ConverterAssembler<VStudentFallibleQuestion, StudentFallibleQuestion, Long, Long>() {

			@Override
			public boolean accept(StudentFallibleQuestion s) {
				return s.getQuestionId() != null && s.getQuestionId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentFallibleQuestion s, VStudentFallibleQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(StudentFallibleQuestion s, VStudentFallibleQuestion d, Long value) {
				if (d.getQuestion() == null) {
					return;
				}
				int rightAnswerSize = d.getQuestion().getAnswers().size();
				if (d.getQuestion().getType() == Type.FILL_BLANK) {// 填空题init每空的对错
					// 处理itemResult
					int itemResultSize = s.getLatestItemResults() == null ? 0 : s.getLatestItemResults().size();
					if (itemResultSize == 0) {
						d.setLatestItemResults(new ArrayList<HomeworkAnswerResult>(rightAnswerSize));
						for (int i = 0; i < rightAnswerSize; i++) {
							if (s.getLatestAnswer() == null) {
								d.getLatestItemResults().add(HomeworkAnswerResult.WRONG);
							} else {
								d.getLatestItemResults().add(s.getLatestResult());
							}
						}
					} else {
						if (itemResultSize == rightAnswerSize) {
							d.setLatestItemResults(s.getLatestItemResults());
						} else if (itemResultSize < rightAnswerSize) {// 最近一次的答案结果少空
							d.setLatestItemResults(s.getLatestItemResults());
							for (int i = 0; i < rightAnswerSize - itemResultSize; i++) {
								d.getLatestItemResults().add(s.getLatestResult());
							}
						} else {// 最近一次的答案结果少空
							d.setLatestItemResults(s.getLatestItemResults().subList(0, rightAnswerSize));
						}
					}
					// 处理answer
					int answerSize = 0;
					if (CollectionUtils.isNotEmpty(d.getLatestAnswer())) {
						answerSize = d.getLatestAnswer().size();
					}
					if (answerSize == 0) {
						for (int i = 0; i < answerSize; i++) {
							d.getLatestAnswer().add(StringUtils.EMPTY);
						}
					} else {
						if (answerSize < rightAnswerSize) {// 最近一次的答案少空
							for (int i = 0; i < rightAnswerSize - answerSize; i++) {
								d.getLatestAnswer().add(StringUtils.EMPTY);
							}
						} else {// 最近一次的答案多空
							d.setLatestAnswer(d.getLatestAnswer().subList(0, rightAnswerSize));
						}
					}
				}
				// 为客户端处理
				if (Security.isClient()) {
					if (CollectionUtils.isNotEmpty(d.getLatestAnswer())) {
						List<String> answers = new ArrayList<String>(rightAnswerSize);
						int i = 0;
						for (String answer : d.getLatestAnswer()) {
							if (d.getQuestion().getType() == Type.FILL_BLANK) {
								answers.add(QuestionUtils.process(answer,
										d.getLatestItemResults().get(i) == HomeworkAnswerResult.RIGHT, true));
							} else {
								answers.add(QuestionUtils.process(answer,
										s.getLatestResult() == HomeworkAnswerResult.RIGHT, true));
							}
							i++;
						}
						d.setLatestAnswer(answers);
					}
				}
			}

			@Override
			public Long getValue(Long key) {
				return 0L;
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				Map<Long, Long> map = new HashMap<Long, Long>(keys.size());
				for (Long key : keys) {
					map.put(key, 0L);
				}
				return map;
			}

		});

		assemblers.add(new ConverterAssembler<VStudentFallibleQuestion, StudentFallibleQuestion, Long, Long>() {

			@Override
			public boolean accept(StudentFallibleQuestion studentFallibleQuestion) {
				return studentFallibleQuestion.getQuestionId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentFallibleQuestion studentFallibleQuestion,
					VStudentFallibleQuestion vStudentFallibleQuestion) {
				return studentFallibleQuestion.getQuestionId();
			}

			@Override
			public void setValue(StudentFallibleQuestion studentFallibleQuestion,
					VStudentFallibleQuestion vStudentFallibleQuestion, Long value) {
				if (value != null) {
					vStudentFallibleQuestion.setMistakePeople(value);
				}
			}

			@Override
			public Long getValue(Long key) {
				return sfqService.countMistakePeople(Lists.<Long>newArrayList(key)).get(key);
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				return sfqService.countMistakePeople(keys);
			}
		});
	}
}
