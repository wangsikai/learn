package com.lanking.uxb.service.examPaper.api;

import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm;

/**
 * 组卷导出Service.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月19日
 */
public interface CustomExampaperExportService {

	/**
	 * 创建教师组卷.
	 * 
	 * @param customExampaper
	 *            组卷
	 */
	int createTeaCustomExampaperDoc(TeaCustomExampaperExportForm form) throws Exception;
}
