package com.lanking.uxb.zycon.qs.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lanking.cloud.domain.yoomath.school.SchoolQuestion;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 学校题库service
 *
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public interface ZycSchoolQuestionService {
	/**
	 * 分页查询校本中的题目
	 *
	 * @param pageable
	 *            分页数据
	 * @param schoolId
	 *            学校id
	 * @param subjectCode
	 *            学科代码
	 * @return {@link SchoolQuestion}
	 */
	Page<SchoolQuestion> page(Pageable pageable, long schoolId, Integer subjectCode);

	/**
	 * 根据id删除校本题目
	 *
	 * @param id
	 *            题目id
	 * @param schoolId
	 *            学校id
	 */
	void delete(long id, long schoolId);

	/**
	 * 校本题库的导入接口
	 *
	 * @param questionCodes
	 *            题目编号列表
	 * @return Map对象返回两个key->object, errorCount -> 导入失败数量, successCount ->
	 *         导入成功数量, fileName -> 文件存储的名称
	 */
	Map<String, Object> importQuestion(List<String> questionCodes, long schoolId) throws IOException;

	/**
	 * 得到上传文件中的题目Code列表
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return Question.code列表
	 */
	Map<String, Object> getCodeFromImportFile(HttpServletRequest request);

	/**
	 * 得到下载的InputStream
	 *
	 * @param fileName
	 *            文件名
	 * @return InputStream
	 * @throws FileNotFoundException
	 */
	InputStream download(String fileName) throws FileNotFoundException;

	/**
	 * 更新学校题库缓存
	 * 
	 * @param schoolId
	 * @param questionIds
	 */
	void updateSchoolCache(Long schoolId, List<Long> questionIds);

	/**
	 * 查找学样下的题目数量
	 *
	 * @param schoolId
	 *            学校id
	 * @return 题目数量
	 */
	long countSchoolQuestion(long schoolId);

	/**
	 * mget题目学校下的题目数量
	 *
	 * @param schoolIds
	 *            学校id列表
	 * @return schoolId -> 校本题目数量
	 */
	Map<Long, Long> mgetCountSchoolQuestion(Collection<Long> schoolIds);
}
