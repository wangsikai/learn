package com.lanking.uxb.service.examPaper.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 组卷班级相关接口
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface CustomExampaperClassService {

	/**
	 * 保存数据
	 *
	 * @param paperId
	 *            试卷id
	 * @param classId
	 *            班级id
	 */
	void create(long paperId, long classId);

	/**
	 * 根据组卷id查找班级列表
	 *
	 * @param paperId
	 *            组卷id
	 * @return {@link List}
	 */
	List<Long> findByPaper(long paperId);

	/**
	 * 查询多个组卷所对应的班级
	 *
	 * @param paperIds
	 *            组卷id列表
	 * @return {@link Map}
	 */
	Map<Long, List<Long>> findByPapers(Collection<Long> paperIds);
}
