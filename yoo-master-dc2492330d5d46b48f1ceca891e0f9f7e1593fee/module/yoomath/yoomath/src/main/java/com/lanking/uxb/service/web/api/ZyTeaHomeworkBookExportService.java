package com.lanking.uxb.service.web.api;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;

import freemarker.template.TemplateException;

/**
 * 教师布置作业习题导出接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月13日
 */
public interface ZyTeaHomeworkBookExportService {

	/**
	 * 导出（按试题类型分类方式）.
	 * 
	 * @param destDir
	 * @param host
	 * @param cacheFileName
	 * @param docTitle
	 * @param questions
	 * @param questionWordMLDataMap
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public File export(String destDir, String host, String cacheFileName, String docTitle, List<Question> questions,
			Map<Long, QuestionWordMLData> questionWordMLDataMap) throws Exception;
}
