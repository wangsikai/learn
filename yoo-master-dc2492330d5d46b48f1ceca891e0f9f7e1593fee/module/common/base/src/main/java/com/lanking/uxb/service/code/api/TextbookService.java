package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.frame.system.Product;

/**
 * 教材相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
public interface TextbookService {

	Textbook get(int code);

	List<Textbook> getAll();

	Map<Integer, Textbook> mget(Collection<Integer> codes);

	List<Textbook> mgetList(Collection<Integer> codes);

	List<Textbook> find(int phaseCode, Integer categoryCode, Integer subjectCode);

	/**
	 * @since yoomath V1.2
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @param categoryCodes
	 *            版本代码,必传否则请调用<br>
	 *            find(int phaseCode, Integer categoryCode, Integer subjectCode)
	 * @return List
	 */
	List<Textbook> find(int phaseCode, Integer subjectCode, Collection<Integer> categoryCodes);

	/**
	 * 查找教材列表
	 * 
	 * @param product
	 *            产品可选
	 * @param phaseCode
	 *            阶段代码(必传)
	 * @param subjectCode
	 *            学科代码(必传)
	 * @param categoryCodes
	 *            版本代码集合(必传)
	 * @return 教材列表
	 */
	List<Textbook> find(Product product, int phaseCode, Integer subjectCode, Collection<Integer> categoryCodes);

	/**
	 * 查找教材列表
	 * 
	 * @param product
	 *            产品可选
	 * @param phaseCode
	 *            阶段代码(必传)
	 * @param subjectCode
	 *            学科代码(必传)
	 * @param categoryCode
	 *            版本代码(必传)
	 * @return 教材列表
	 */
	List<Textbook> find(Product product, int phaseCode, Integer subjectCode, Integer categoryCode);

}
