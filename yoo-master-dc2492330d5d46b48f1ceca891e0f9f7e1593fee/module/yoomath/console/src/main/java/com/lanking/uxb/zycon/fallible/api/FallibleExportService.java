package com.lanking.uxb.zycon.fallible.api;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.domain.yoomath.fallible.ClassFallibleExportRecord;

/**
 * 学生错题导出接口.
 * 
 * @author <a href="mailto:pengcheng.yu@elanking.com">pengcheng.yu</a>
 *
 * @version 2018年1月4日
 */

public interface FallibleExportService {

	/**
	 * 导出学生错题.
	 * 
	 * @param destDir
	 *            WORD目标文件夹目录
	 * @param docTitle
	 *            标题
	 * @param sectionList
	 *            章节集合
	 * @param historyMap
	 *            答题历史集合
	 * @param sfQuestionRateMap
	 *            正确率集合
	 * @param host
	 *            本地服务host
	 * @param hash
	 *            文档hash
	 * @param questionWordMLDataMap
	 *            预处理习题WordML集合
	 * @param qrcodeMap
	 *            习题二维码图片集合
	 * @param outLocalImgMap
	 *            外部处理的图片集合（答题历史图片）
	 * @return
	 * @throws Exception
	 */
	public File exportStuFallible(String destDir, String docTitle, List<Map<String, Object>> sectionList,
			Map<Long, List<Map<String, Object>>> historyMap, Map<String, String> sfQuestionRateMap, String host,
			String docName, Map<Long, QuestionWordMLData> questionWordMLDataMap, Map<Long, String> qrcodeMap,
			Map<String, String> outLocalImgMap) throws Exception;
	
	
	/**
	 * 批量导出学生错题本
	 * @param host
	 * @param sectionCodes 要导出的章节列表
	 * @param studentIds 学生列表
	 * @param clazzId 班级id
	 * @return
	 */
	ClassFallibleExportRecord batchWriteFileAndRecordTask(final String host,final List<Long> sectionCodes,final List<Long> allStudentIds,final List<Long> studentIds,final long clazzId,final int exportType,final String clazzName,final Map<Long,String> stuMap,final String schoolName);
}
