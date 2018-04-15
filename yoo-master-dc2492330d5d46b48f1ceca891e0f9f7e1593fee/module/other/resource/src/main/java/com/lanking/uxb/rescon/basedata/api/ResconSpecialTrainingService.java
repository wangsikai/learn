package com.lanking.uxb.rescon.basedata.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTraining;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.rescon.basedata.form.ResconSpecialTrainingForm;
import com.lanking.uxb.rescon.basedata.value.VSpecialTraining;

/**
 * 针对性训练
 * 
 * @since 2.0.1
 * @author wangsenhao
 *
 */
public interface ResconSpecialTrainingService {
	/**
	 * 根据阶段和小专题获取对应的训练列表
	 * 
	 * @param p
	 * @param subjectCode
	 * @param code
	 * @return
	 */
	Page<SpecialTraining> list(Pageable p, Long code);

	/**
	 * 根据知识专项代码查找所有的训练模块 (已通过状态)
	 *
	 * @param code
	 *            知识专项代码
	 * @return {@link List}
	 */
	List<SpecialTraining> list(long code);

	/**
	 * 保存或编辑针对性训练,创建完成默认的20题也已入库
	 * 
	 * @param form
	 * @return
	 */
	VSpecialTraining createSpecial(ResconSpecialTrainingForm form);

	/**
	 * 获取针对性训练对象
	 * 
	 * @param id
	 * @return
	 */
	SpecialTraining get(long id);

	/**
	 * 创建针对性训练按要求随机拉取20道题目<br>
	 * I 典型题<br>
	 * II 考点相关+子知识点同时相关<br>
	 * III 考点相关<br>
	 * IV 子知识点相关
	 * 
	 * @param examIds
	 * @param knowPoints
	 * @return
	 */
	List<Long> pullQuestion(List<Long> examIds, List<Long> knowPoints, Long vendorId);

	/**
	 * 更新训练状态
	 * 
	 * @param status
	 * @param id
	 */
	void updateStatus(SpecialTrainingStatus status, long id, Long userId);

	/**
	 * 获取针对性训练小专题的统计数
	 * 
	 * @param code
	 *            第二层小专题
	 * @return
	 */
	Map<Integer, Long> getStat(Long code);

	/**
	 * 通过科目获取统计
	 * 
	 * @param subjectCode
	 * @return
	 */
	Map<Integer, Long> getStatBySubject(Long subjectCode);

	/**
	 * 总数 不包括删除的
	 * 
	 * @param code
	 * @return
	 */
	Long total(Long code);

	/**
	 * 根据科目统计总数
	 * 
	 * @param subjectCode
	 * @return
	 */
	Long getTotalBySubject(Long subjectCode);

}
