package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassKnowpointService {
	/**
	 * 根据知识点列表及班级id列表查找数据
	 *
	 * @param classId
	 *            班级id
	 * @param codes
	 *            知识点列表
	 * @return {@link List}
	 */
	List<DiagnosticClassKnowpoint> findByCodesAndClass(long classId, Collection<Long> codes);

	/**
	 * 获得班级所有薄弱知识点数据.
	 * 
	 * @param classId
	 *            班级id
	 * @return
	 */
	List<DiagnosticClassKnowpoint> findAllByWeakDatas(long classId);

	/**
	 * 全局查找薄弱知识点
	 *
	 * @param classId
	 *            班级id
	 * @param limit
	 *            查询几条数据(首页与教学诊断中数量略有不同)
	 * @return 知识点列表
	 */
	List<DiagnosticClassKnowpoint> findWeakDatas(long classId, int limit);

	/**
	 * 全局查找薄弱知识点（添加时间限制）
	 *
	 * @param classId
	 *            班级id
	 * @param limit
	 *            查询几条数据(首页与教学诊断中数量略有不同)
	 * @param bt
	 *            限制查询开始时间
	 * @return 知识点列表
	 */
	List<DiagnosticClassKnowpoint> findWeakDatas(long classId, int limit, Date bt);

	/**
	 * 智能组卷-->薄弱知识点专练<br>
	 * 查询薄弱知识点
	 * 
	 * @param classId
	 * @param textbookCode
	 *            教材
	 * @param limit
	 * @return
	 */
	List<DiagnosticClassKnowpoint> smartWeakDatas(long classId, long textbookCode, int limit);

	/**
	 * 智能组卷-->a.平衡点性训练 /b.自订知识点考察<br>
	 * a.查询练习数最少的知识点，且练习数必须大于0<br>
	 * b.不过滤都取
	 * 
	 * @param classId
	 * @param textbookCode
	 * @param limit
	 *            为空时是自订知识点考察
	 * @return
	 */
	List<DiagnosticClassKnowpoint> smartBalanceDatas(long classId, long textbookCode, Integer limit);

	/**
	 * 通过班级和对应的知识点code查询对应的信息
	 * 
	 * @param studentId
	 * @param classId
	 * @param codes
	 * @return
	 */
	List<DiagnosticClassKnowpoint> findByCodes(long classId, List<Long> codes);

	/**
	 * 根据传入的新知识点查找教学诊断薄弱知识点
	 *
	 * @param codes
	 *            新知识点codes
	 * @param classId
	 *            班级id
	 * @return {@link List}
	 */
	List<DiagnosticClassKnowpoint> findWeakPointsByCodes(List<Long> codes, long classId);

	/**
	 * 当前知识点是班级薄弱知识点的
	 *
	 * @param classId
	 *            班级id
	 * @param codes
	 * 
	 * @return 知识点列表
	 */
	List<DiagnosticClassKnowpoint> findWeakDatasByKps(long classId, List<Long> codes);

	/**
	 * 全局查找诊断信息
	 *
	 * @param classId
	 *            班级id
	 * @param limit
	 *            查询几条数据
	 * @param bt
	 *            限制查询开始时间
	 * @return 知识点列表
	 */
	List<DiagnosticClassKnowpoint> findDiagnosticDatas(long classId, int limit, Date bt);
}
