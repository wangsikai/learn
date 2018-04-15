package com.lanking.uxb.zycon.mall.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.zycon.mall.form.ExcellentPaperForm;

/**
 * 提供试卷相关接口
 * 
 * @since 2.0.7
 * @author zemin.song
 * @version 2016年9月6日 18:04:47
 */
public interface ZycExcellentPaperManage {

	ExamPaper get(long id);

	Map<Long, ExamPaper> mget(List<Long> ids);

	/**
	 * 试卷查询接口
	 * 
	 * @param queryForm
	 *            查询form
	 * @return 查询结果 分页
	 */
	Page<ExamPaper> queryResconExam(ExcellentPaperForm queryForm);

}
