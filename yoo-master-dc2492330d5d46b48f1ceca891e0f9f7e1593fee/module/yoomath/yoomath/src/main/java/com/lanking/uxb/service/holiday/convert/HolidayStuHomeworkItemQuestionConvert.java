package com.lanking.uxb.service.holiday.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkAnswerImageService;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomeworkItemQuestion;

/**
 * 假期学生作业专项题目convert
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
@Component
public class HolidayStuHomeworkItemQuestionConvert extends
		Converter<VHolidayStuHomeworkItemQuestion, HolidayStuHomeworkItemQuestion, Long> {

	@Autowired
	private HolidayStuHomeworkAnswerImageService imageService;

	@Override
	protected Long getId(HolidayStuHomeworkItemQuestion s) {
		return s.getId();
	}

	@Override
	protected VHolidayStuHomeworkItemQuestion convert(HolidayStuHomeworkItemQuestion s) {
		VHolidayStuHomeworkItemQuestion v = new VHolidayStuHomeworkItemQuestion();
		v.setId(s.getId());
		v.setHolidayHomeworkId(s.getHolidayHomeworkId());
		v.setHolidayStuHomeworkId(s.getHolidayStuHomeworkId());
		v.setHolidayStuHomeworkItemId(s.getHolidayStuHomeworkItemId());
		v.setQuestionId(s.getQuestionId());
		v.setResult(s.getResult());
		v.setComment(s.getComment());

		if (s.getAnswerImg() != null && s.getAnswerImg() != 0) {
			v.setAnswerImg(FileUtil.getUrl(s.getAnswerImg()));
			v.setSolvingImg(v.getAnswerImg());
		}
		v.setType(s.getType());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VHolidayStuHomeworkItemQuestion, HolidayStuHomeworkItemQuestion, Long, List<HolidayStuHomeworkAnswerImage>>() {

					@Override
					public boolean accept(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion) {
						return holidayStuHomeworkItemQuestion.getAnswerImg() != null
								&& holidayStuHomeworkItemQuestion.getAnswerImg() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion,
							VHolidayStuHomeworkItemQuestion vHolidayStuHomeworkItemQuestion) {
						return holidayStuHomeworkItemQuestion.getId();
					}

					@Override
					public void setValue(HolidayStuHomeworkItemQuestion holidayStuHomeworkItemQuestion,
							VHolidayStuHomeworkItemQuestion vHolidayStuHomeworkItemQuestion,
							List<HolidayStuHomeworkAnswerImage> value) {
						if (CollectionUtils.isNotEmpty(value)) {
							List<String> answerImgs = new ArrayList<String>(value.size());
							List<Long> answerImgIds = new ArrayList<Long>(value.size());
							List<Long> notationAnswerImgIds = new ArrayList<Long>(value.size());
							List<String> notationAnswerImgs = new ArrayList<String>(value.size());

							for (HolidayStuHomeworkAnswerImage img : value) {
								answerImgs.add(FileUtil.getUrl(img.getAnswerImg()));
								answerImgIds.add(img.getAnswerImg());
								if (img.getNotationAnswerImg() != null) {
									notationAnswerImgIds.add(img.getNotationAnswerImg());
									notationAnswerImgs.add(FileUtil.getUrl(img.getNotationMobileImg()));
								}
							}

							vHolidayStuHomeworkItemQuestion.setNotationAnswerImgs(notationAnswerImgs);
							vHolidayStuHomeworkItemQuestion.setNotationAnswerImgIds(notationAnswerImgIds);
							vHolidayStuHomeworkItemQuestion.setAnswerImgIds(answerImgIds);
							vHolidayStuHomeworkItemQuestion.setAnswerImgs(answerImgs);
						}
					}

					@Override
					public List<HolidayStuHomeworkAnswerImage> getValue(Long key) {
						return imageService.findByItemQuestion(key);
					}

					@Override
					public Map<Long, List<HolidayStuHomeworkAnswerImage>> mgetValue(Collection<Long> keys) {
						return imageService.findByItemQuestions(keys);
					}
				});
	}
}
