package com.lanking.uxb.zycon.activity.api;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.uxb.zycon.activity.form.ZycExamConfigForm;

public interface ZycImperialExamConfigService {

	/**
	 * 保存和编辑
	 * 
	 * @param form
	 */
	void save(ZycExamConfigForm form);

	/**
	 * 获取最大code
	 * 
	 * @return
	 */
	long maxCode();

	/**
	 * 
	 */
	ImperialExaminationActivity queryByCode(Long code);

	/**
	 * 保存和编辑
	 * 
	 * @param form
	 */
	void save2(ZycExamConfigForm form);
}
