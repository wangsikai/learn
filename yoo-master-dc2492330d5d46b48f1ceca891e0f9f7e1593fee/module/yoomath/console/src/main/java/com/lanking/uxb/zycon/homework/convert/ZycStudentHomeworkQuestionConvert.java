package com.lanking.uxb.zycon.homework.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerImageService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkQuestion;

@Component
public class ZycStudentHomeworkQuestionConvert extends
		Converter<VZycStudentHomeworkQuestion, StudentHomeworkQuestion, Long> {
	
	@Autowired
	private StudentHomeworkAnswerImageService studentHomeworkAnswerImageService;

	@Override
	protected Long getId(StudentHomeworkQuestion s) {
		return s.getId();
	}

	@Override
	protected VZycStudentHomeworkQuestion convert(StudentHomeworkQuestion s) {
		VZycStudentHomeworkQuestion v = new VZycStudentHomeworkQuestion();
		v.setComment(validBlank(s.getComment()));
		v.setCorrectAt(s.getCorrectAt());
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setResult(s.getResult());
		v.setStudentHomeworkId(s.getStudentHomeworkId());
		v.setRevised(s.isRevised());
		v.setNewCorrect(s.isNewCorrect());
		v.setCorrectType(s.getCorrectType());
		v.setRightRate(s.getRightRate());
		
		if (s.getAnswerImg() != null && s.getAnswerImg() != 0) {
			v.setSolvingImg(FileUtil.getUrl(s.getAnswerImg()));
		}
		if (s.getAnswerImg() != null) {
			v.setSolvingImgId(s.getAnswerImg());
		}
		if (s.getAnswerImg() != null && s.getAnswerImg() != 0) {
			v.setAnswerImg(FileUtil.getUrl(s.getAnswerImg()));
		}
		if (s.getAnswerImg() != null) {
			v.setAnswerImgId(s.getAnswerImg());
		}
		return v;
	}
	
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(
			new ConverterAssembler<VZycStudentHomeworkQuestion, StudentHomeworkQuestion, Long, List<StudentHomeworkAnswerImage>>() {
	
				@Override
				public boolean accept(StudentHomeworkQuestion studentHomeworkQuestion) {
					return true;
				}
	
				@Override
				public boolean accept(Map<String, Object> hints) {
					return true;
				}
	
				@Override
				public Long getKey(StudentHomeworkQuestion studentHomeworkQuestion,
						VZycStudentHomeworkQuestion vStudentHomeworkQuestion) {
					return studentHomeworkQuestion.getId();
				}
	
				@Override
				public void setValue(StudentHomeworkQuestion studentHomeworkQuestion,
						VZycStudentHomeworkQuestion vStudentHomeworkQuestion, List<StudentHomeworkAnswerImage> value) {
					if (CollectionUtils.isNotEmpty(value)) {
						List<Long> answerImgeIds = new ArrayList<Long>(value.size());
						List<Long> notationImageIds = new ArrayList<Long>(value.size());
						List<Long> notationMobileImageIds = new ArrayList<Long>(value.size());
						List<String> answerNotations = new ArrayList<String>(value.size());
						List<String> answerNotationPointList = new ArrayList<String>(value.size());
						List<String> handWritings = new ArrayList<String>(value.size());
						for (StudentHomeworkAnswerImage i : value) {// 有只处理一张图的可能吗？
							if (i.getAnswerImg() != null) {
								long answerImg = i.getAnswerImg();
	
								if (Security.isClient()) {
									if (i.getNotationMobileImg() == null && i.getNotationWebImg() != null) {
										// 说明被web端标注过，则直接用标注后的合成图上标注
										answerImg = i.getNotationAnswerImg();
									} else if (i.getNotationMobileImg() != null) {
										answerImg = i.getNotationMobileImg();
									}
								} else {
									if (i.getNotationWebImg() == null && i.getNotationMobileImg() != null) {
										// 说明被mobile端标注过，则直接用标注后的合成图上标注
										answerImg = i.getNotationAnswerImg();
									} else if (i.getNotationWebImg() != null) {
										answerImg = i.getNotationWebImg();
									}
								}
								answerImgeIds.add(answerImg);
							}
	
							if (i.getNotationAnswerImg() != null) {
								notationImageIds.add(i.getNotationAnswerImg());
							}
							if (i.getNotationMobileImg() != null) {
								notationMobileImageIds.add(i.getNotationMobileImg());
							}
							if (StringUtils.isNotBlank(i.getAnswerNotation())) {
								answerNotations.add(i.getAnswerNotation());
							}
							if (StringUtils.isNotBlank(i.getAnswerNotationPoints())) {
								answerNotationPointList.add(i.getAnswerNotationPoints());
							}
							if (StringUtils.isNotBlank(i.getHandWriting())) {
								handWritings.add(i.getHandWriting());
							}
						}
						// 原图(客户端基于此图做批注)
						vStudentHomeworkQuestion.setAnswerImgIds(answerImgeIds);
						// 原图URL(客户端基于此图做批注)
						if (CollectionUtils.isNotEmpty(answerImgeIds)) {
							vStudentHomeworkQuestion.setAnswerImgs(FileUtil.getUrl(answerImgeIds));
						}

						// 设置原来单张图的字段
						if (CollectionUtils.isNotEmpty(vStudentHomeworkQuestion.getAnswerImgs())) {
							vStudentHomeworkQuestion.setAnswerImg(vStudentHomeworkQuestion.getAnswerImgs().get(0));
							vStudentHomeworkQuestion
									.setAnswerImgId(vStudentHomeworkQuestion.getAnswerImgIds().get(0));
						}
					}
				}
	
				@Override
				public List<StudentHomeworkAnswerImage> getValue(Long key) {
					return studentHomeworkAnswerImageService.findByStuHkQuestion(key);
				}
	
				@Override
				public Map<Long, List<StudentHomeworkAnswerImage>> mgetValue(Collection<Long> keys) {
					return studentHomeworkAnswerImageService.mgetByStuHKQuestion(keys);
				}
			});

	}
}
