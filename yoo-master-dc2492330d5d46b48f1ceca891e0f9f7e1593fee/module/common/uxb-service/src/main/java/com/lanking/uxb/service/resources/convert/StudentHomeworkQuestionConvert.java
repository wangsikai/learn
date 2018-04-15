package com.lanking.uxb.service.resources.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessage;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.correct.vo.AnswerCorrectResult;
import com.lanking.uxb.service.file.api.QiNiuFileService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.HomeworkMessageService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerImageService;
import com.lanking.uxb.service.resources.value.VHomeworkMessage;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.session.api.impl.Security;

@Component
public class StudentHomeworkQuestionConvert extends Converter<VStudentHomeworkQuestion, StudentHomeworkQuestion, Long> {

	@Autowired
	private StudentHomeworkAnswerImageService studentHomeworkAnswerImageService;
	@Autowired
	private QiNiuFileService qiNiuFileService;
	@Autowired
	private HomeworkMessageService hkMessageService;
	@Autowired
	private QuestionService questionService;

	@Override
	protected Long getId(StudentHomeworkQuestion s) {
		return s.getId();
	}

	@Override
	protected VStudentHomeworkQuestion convert(StudentHomeworkQuestion s) {
		VStudentHomeworkQuestion v = new VStudentHomeworkQuestion();
		v.setComment(validBlank(s.getComment()));
		v.setCorrectAt(s.getCorrectAt());
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setResult(s.getResult());
		v.setStudentHomeworkId(s.getStudentHomeworkId());

		v.setSolvingImg(FileUtil.getUrl(s.getAnswerImg()));
		v.setSolvingImgId(s.getAnswerImg() == null ? 0 : s.getAnswerImg());
		v.setAnswerImg(FileUtil.getUrl(s.getAnswerImg()));
		v.setAnswerImgId(s.getAnswerImg() == null ? 0 : s.getAnswerImg());
		v.setRevised(s.getIsRevised());
		v.setNewCorrect(s.isNewCorrect());
		// 设置标注后的图片
		if (s.getNotationAnswerImg() == null) {
			v.setNotationAnswerImgId(v.getAnswerImgId());
			v.setNotationAnswerImg(v.getAnswerImg());
		} else {
			v.setNotationAnswerImg(FileUtil.getUrl(s.getNotationAnswerImg()));
			v.setNotationAnswerImgId(s.getNotationAnswerImg());
		}

		// 设置标注的原图
		if (Security.getUserType() == UserType.TEACHER) {
			if (Security.isClient()) {
				if (s.getNotationMobileImg() == null) {
					if (s.getNotationWebImg() != null) {// 说明被web端标注过，则直接在web端标注后的合成图上标注
						v.setAnswerImg(v.getNotationAnswerImg());
						v.setAnswerImgId(v.getNotationAnswerImgId());
					}
				} else {
					v.setAnswerImg(FileUtil.getUrl(s.getNotationMobileImg()));
					v.setAnswerImgId(s.getNotationMobileImg());
				}
			} else {
				if (s.getNotationWebImg() == null) {
					if (s.getNotationMobileImg() != null) {// 说明被web端标注过，则直接在web端标注后的合成图上标注
						v.setAnswerImg(v.getNotationAnswerImg());
						v.setAnswerImgId(v.getNotationAnswerImgId());
					}
				} else {
					v.setAnswerImg(FileUtil.getUrl(s.getNotationWebImg()));
					v.setAnswerImgId(s.getNotationWebImg());
				}
			}
		}
		if (Security.isClient() && Security.getUserType() == UserType.TEACHER) {// 只有客户端才会返回此数据
			v.setAnswerNotationPoints(validBlank(s.getAnswerNotationPoints()));
		}
		v.setRightRate(s.getRightRate());
		v.setDotime(s.getDotime() == null ? 0 : s.getDotime());
		if (StringUtils.isNotBlank(s.getVoiceFileKey())) {
			v.setVoiceUrl(qiNiuFileService.getDownloadUrl(s.getVoiceFileKey()));
			v.setVoiceTime(s.getVoiceTime());
		}

		// 习题的最终批改方式
		v.setCorrectType(s.getCorrectType());

		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(
				new ConverterAssembler<VStudentHomeworkQuestion, StudentHomeworkQuestion, Long, List<StudentHomeworkAnswerImage>>() {

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
							VStudentHomeworkQuestion vStudentHomeworkQuestion) {
						return studentHomeworkQuestion.getId();
					}

					@Override
					public void setValue(StudentHomeworkQuestion studentHomeworkQuestion,
							VStudentHomeworkQuestion vStudentHomeworkQuestion, List<StudentHomeworkAnswerImage> value) {
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
							// 合成图片的ID
							vStudentHomeworkQuestion.setNotationAnswerImgIds(notationImageIds);
							// 合成图片的URL
							if (CollectionUtils.isNotEmpty(notationImageIds)) {
								vStudentHomeworkQuestion.setNotationAnswerImgs(FileUtil.getUrl(notationImageIds));
							}
							// 教师端返回批注内容
							if (Security.getUserType() == UserType.TEACHER) {
								if (Security.isClient()) {// 客户端批注的点
									vStudentHomeworkQuestion.setAnswerNotationPointList(answerNotationPointList);
								}
								if (!Security.isClient()) {// web端批注的点
									vStudentHomeworkQuestion.setAnswerNotations(answerNotations);
								}
							}
							// 学生端返回手写轨迹
							if (Security.getUserType() == UserType.STUDENT && Security.isClient()) {
								vStudentHomeworkQuestion.setHandWriting(handWritings);
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

		// 设置学生作业题目留言信息
		assemblers.add(
				new ConverterAssembler<VStudentHomeworkQuestion, StudentHomeworkQuestion, Long, List<VHomeworkMessage>>() {
					@Override
					public boolean accept(StudentHomeworkQuestion s) {
						return true;
//						return !StringUtils.isBlank(s.getVoiceFileKey());
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentHomeworkQuestion s, VStudentHomeworkQuestion d) {
						return s.getId();
					}

					@Override
					public void setValue(StudentHomeworkQuestion s, VStudentHomeworkQuestion d,
							List<VHomeworkMessage> value) {
						d.setMessages(value);
					}

					@Override
					public List<VHomeworkMessage> getValue(Long key) {
						List<VHomeworkMessage> vMessages = new ArrayList<>();
						List<HomeworkMessage> messages = hkMessageService.findByStudentHkQId(key);

						if (CollectionUtils.isNotEmpty(messages)) {
							for (HomeworkMessage message : messages) {
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
						for (Long key : keys) {
							List<VHomeworkMessage> vMessages = new ArrayList<>();
							List<HomeworkMessage> messages = hkMessageService.findByStudentHkQId(key);

							if (CollectionUtils.isNotEmpty(messages)) {
								for (HomeworkMessage message : messages) {
									VHomeworkMessage vMessage = new VHomeworkMessage();
									vMessage.setId(message.getId());
									vMessage.setType(message.getType());
									vMessage.setCreateAt(message.getCreateAt());
									vMessage.setIconKey(message.getIconKey());

									if (StringUtils.isNotBlank(message.getComment())) {
										vMessage.setComment(message.getComment());
									}

									if (StringUtils.isNotBlank(message.getVoiceFileKey())) {
										vMessage.setVoiceUrl(
												qiNiuFileService.getDownloadUrl(message.getVoiceFileKey()));
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
