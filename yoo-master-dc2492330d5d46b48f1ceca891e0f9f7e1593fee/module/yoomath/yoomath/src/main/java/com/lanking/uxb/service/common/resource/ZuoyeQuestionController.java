package com.lanking.uxb.service.common.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.ex.core.IllegalArgFormatException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.RSACoder;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.common.value.VQuestionBase;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.ex.QuestionException;

/**
 * 提供习题相关restAPI
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
@RestController
@RequestMapping("zy/question")
public class ZuoyeQuestionController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseConvert<VQuestionBase> questionConvert;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "detail", method = { RequestMethod.GET, RequestMethod.POST })
	public Value detail(String id, String code) {
		if (StringUtils.isBlank(id) && StringUtils.isBlank(code)) {
			return new Value(new MissingArgumentException());
		}
		if (StringUtils.isNotBlank(id)) {
			String newId = id;
			if (id.length() > 18) {
				try {
					byte[] newbyte = RSACoder.parseHexStr2Byte(id);
					byte[] result = RSACoder.decryptByPrivateKey(newbyte, Env.getString("yoomath.rsa.privateKey"));
					newId = new String(result);
				} catch (Exception e) {
					// 错误的参数码
					return new Value(new IllegalArgFormatException());
				}
			}

			Question question = questionService.get(Long.parseLong(newId));
			if (question == null || question.getStatus() != CheckStatus.PASS
					|| question.getDelStatus() != Status.ENABLED) {
				return new Value(new QuestionException(QuestionException.QUESTION_NOT_EXIST));
			}
			return new Value(questionConvert.to(question, new QuestionBaseConvertOption(false, true, true, false)));
		} else if (StringUtils.isNotBlank(code)) {
			String newCode = code;
			if (newCode.length() > 9) {
				try {
					byte[] newbyte = RSACoder.parseHexStr2Byte(code);
					byte[] result = RSACoder.decryptByPrivateKey(newbyte, Env.getString("yoomath.rsa.privateKey"));

					newCode = new String(result);
				} catch (Exception e) {
					// 错误的参数码
					return new Value(new IllegalArgFormatException());
				}
			}

			Question question = questionService.findByCode(newCode);
			if (question == null || question.getStatus() != CheckStatus.PASS
					|| question.getDelStatus() != Status.ENABLED) {
				return new Value(new QuestionException(QuestionException.QUESTION_NOT_EXIST));
			}
			return new Value(questionConvert.to(question, new QuestionBaseConvertOption(false, true, true, false)));
		}
		return new Value();
	}
}
