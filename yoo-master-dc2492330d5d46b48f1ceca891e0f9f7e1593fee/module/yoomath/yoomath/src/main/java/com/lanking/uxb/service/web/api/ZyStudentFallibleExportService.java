package com.lanking.uxb.service.web.api;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;

/**
 * 学生错题导出接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月13日
 */
public interface ZyStudentFallibleExportService {

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
			int hash, Map<Long, QuestionWordMLData> questionWordMLDataMap, Map<Long, String> qrcodeMap,
			Map<String, String> outLocalImgMap) throws Exception;
	
	/**
	 * 生成word文档任务
	 * 
	 * @param host
	 *            服务地址
	 * @param studentId
	 *            用户ID
	 * @param hash
	 *            文件hash
	 * @param fquesions
	 *            问题集合
	 * @param sectionCodes
	 *            章节代码
	 * @param map
	 *            记录返回值
	 * @param buy
	 *            是否已购买兑换
	 * @param attachData
	 *            其他查询参数集合
	 * @param printOrderID
	 *            关联的待打印订单ID
	 * @param isMember
	 * @param createAt
	 *            文档创建时间
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Object writeFileAndRecordTask(final String host, final long studentId, final int hash,
			final List<Map> fquesions, final List<Long> sectionCodes, final Map<String, Object> map, final boolean buy,
			final String attachData, final Long printOrderID, final boolean isMember, final Date createAt);
}
