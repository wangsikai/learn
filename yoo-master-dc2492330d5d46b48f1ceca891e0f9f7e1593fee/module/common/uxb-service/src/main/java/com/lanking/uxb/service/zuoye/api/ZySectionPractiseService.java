package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.sectionPractise.SectionPractise;
import com.lanking.uxb.service.zuoye.form.SectionPractiseForm;

import java.util.Map;

/**
 * 章节练习service
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.1
 */
public interface ZySectionPractiseService {

	/**
	 * 创建章节练习
	 *
	 * @param form
	 *            {@link SectionPractiseForm}
	 * @return {@link SectionPractise}
	 */
	Map<String, Object> commit(SectionPractiseForm form);

	/**
	 * 根据id得到章节练习记录
	 *
	 * @param id
	 *            章节练习记录id
	 * @return {@link SectionPractise}
	 */
	SectionPractise get(long id);

	/**
	 * 暂存章节练习信息
	 * 
	 * @param form
	 *            {@link SectionPractiseForm}
	 * @return {@link SectionPractise}
	 */
	SectionPractise draft(SectionPractiseForm form);
}
