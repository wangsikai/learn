package com.lanking.uxb.service.examPaper.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.examPaper.ex.CustomExampaperException;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperForm;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperQuery;

/**
 * 组卷接口
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface CustomExampaperService {
	/**
	 * 根据id查询数据
	 *
	 * @param id
	 *            组卷id
	 * @return {@link CustomExampaper}
	 */
	CustomExampaper get(long id);

	/**
	 * 根据id集合查询数据
	 * 
	 * @param ids
	 *            id集合
	 * @return {@link CustomExampaper}
	 */
	Map<Long, CustomExampaper> mget(Collection<Long> ids);

	/**
	 * 开卷
	 *
	 * @param id
	 *            组卷id
	 * @param classIds
	 *            班级列表
	 * @return 需要更新缓存的学生id列表
	 */
	List<HomeworkStudentClazz> open(long id, Collection<Long> classIds);

	/**
	 * 生成正式组卷
	 *
	 * @param id
	 *            组卷ID
	 */
	Value enabled(long id);

	/**
	 * 改变试卷状态
	 *
	 * @param id
	 *            组卷ID
	 * @param status
	 *            CustomExampaperStatus
	 */
	Value updateStatus(long id, CustomExampaperStatus status);

	/**
	 * 保存，创建组卷
	 *
	 * @param CustomExamPaperForm
	 *            组卷
	 * @return
	 */
	CustomExampaper createCustomExamPaper(CustomExamPaperForm form) throws CustomExampaperException;

	/**
	 * 删除组卷
	 *
	 * @param id
	 *            组卷id
	 * @return
	 */
	Value deleteCustomExamPaper(Long examPaperId);

	/**
	 * 查询组卷
	 * 
	 * @param userId
	 *            用户ID
	 * @return List
	 */
	Page<CustomExampaper> queryCustomExampapers(CustomExamPaperQuery query, Pageable pageable);

	CursorPage<Date, CustomExampaper> queryCustomExampapers(long teacherId, CursorPageable<Date> pageable);

	/**
	 * 更新下载状态.
	 * 
	 * @param id
	 */
	void updateDownloadFlag(long id);

	/**
	 * 查找所有组卷记录个数
	 * 
	 * @param createId
	 *            组卷创建人
	 * @return
	 */
	long countAllCustomExampapers(long createId);
}
