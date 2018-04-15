package com.lanking.uxb.service.examPaper.api;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.uxb.service.examPaper.form.TeaExcellentExampaperExportForm;

/**
 * 精品试卷导出Service.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月7日
 */
public interface ExcellentExampaperExportService {

	/**
	 * 获得精品试卷.
	 * 
	 * @param id
	 * @return
	 */
	ExamPaper getPaper(long id);

	/**
	 * 创建教师精品试卷文档.
	 * 
	 * @param form
	 *            参数
	 */
	int createTeaExcellentExampaperDoc(TeaExcellentExampaperExportForm form) throws Exception;
}
