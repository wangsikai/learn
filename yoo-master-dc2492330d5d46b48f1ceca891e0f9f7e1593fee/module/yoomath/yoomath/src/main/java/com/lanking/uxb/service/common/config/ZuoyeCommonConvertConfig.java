package com.lanking.uxb.service.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.value.VQuestionBase;
import com.lanking.uxb.service.holiday.value.VHolidayQuestion;

@Configuration
public class ZuoyeCommonConvertConfig {

	@Bean
	public QuestionBaseConvert<VQuestionBase> questionBaseConvert() {
		QuestionBaseConvert<VQuestionBase> convert = new QuestionBaseConvert<VQuestionBase>(VQuestionBase.class);
		try {
			convert.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convert;
	}

	@Bean
	public QuestionBaseConvert<VHolidayQuestion> holidayQuestionBaseConvert() {
		QuestionBaseConvert<VHolidayQuestion> convert = new QuestionBaseConvert<VHolidayQuestion>(
				VHolidayQuestion.class);
		try {
			convert.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convert;
	}

}