package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Section;

/**
 * 章节相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
public interface SectionService {

	/**
	 * 根据教材代码获取章节列表(无层次结构)
	 * 
	 * @since 2.1
	 * @param textBookCode
	 *            教材代码
	 * @return 章节列表
	 */
	List<Section> findByTextbookCode(int textBookCode);

	/**
	 * 根据教材代码及章节级别进行查找
	 *
	 * @since yoomath V1.9
	 * @param textBookCode
	 *            教材代码
	 * @param level
	 *            级别
	 * @return 章节列表
	 */
	List<Section> findByTextbookCode(int textBookCode, Integer level);

	/**
	 * 根据教材代码及章节级别进行查找
	 *
	 * @since yoomath V1.9
	 * @param textBookCodes
	 *            教材代码
	 * @param level
	 *            级别
	 * @return 章节列表
	 */
	List<Section> findByTextbookCode(List<Integer> textBookCodes, Integer level);

	/**
	 * 查找章节下所有子节点
	 *
	 * @since yoomath V1.9
	 * @param code
	 *            章节码
	 * @return 子章节code集合
	 */
	List<Long> findSectionChildren(long code);

	/**
	 * 根据代码获取章节对象
	 * 
	 * @since 2.1
	 * @param code
	 *            章节代码
	 * @return 章节对象
	 */
	Section get(long code);

	/**
	 * 根据代码获取章节对象(批量)
	 * 
	 * @since 2.1
	 * @param codes
	 *            章节代码s
	 * @return 章节对象Map
	 */
	Map<Long, Section> mget(Collection<Long> keys);

	List<Section> mgetList(Collection<Long> keys);

	List<Section> mgetListByTextbookCategory(Collection<Long> keys, int textbookCategoryCode);

	/**
	 * 通过传入的code,获取父类和子类集合
	 * 
	 * @param sectionCode
	 * @return
	 */
	List<Section> mgetListByChildId(Long sectionCode);

	/**
	 * 根据章节代码获取章节名称
	 * 
	 * @param code
	 *            章节代码
	 * @return 章节名称(全称)
	 */
	String getSectionName(Long sectionCode);

	/**
	 * 根据章节代码集合获取章节名称Map
	 * 
	 * @param codes
	 *            章节代码集合
	 * @return 章节名称Map(全称)
	 */
	Map<Long, String> mgetSectionName(List<Long> codes);

	/**
	 * 获取当前章节的父章节
	 * 
	 * @param code
	 * @return
	 */
	Section getPSection(long code);

	/**
	 * 得到某个章节节点的下一叶子节点，如果此节点下面没有叶子节点，则返回版本下的第一个叶子节点
	 *
	 * @param code
	 *            当前章节节点code
	 * @param textbookCode
	 *            版本code
	 * @return {@link Section}
	 */
	Section getNextSection(long code, int textbookCode);

	/**
	 * 得到教材下的第一个章节叶子节点
	 *
	 * @param textbookCode
	 *            教材code
	 * @return {@link Section}
	 */
	Section getFirstLeafSectionByTextbookCode(int textbookCode);

	/**
	 * 查找当前章节下所有子章节(叶子节点)
	 *
	 * @param code
	 *            章节码
	 * @return 子章节(叶子节点)列表
	 */
	List<Long> findNowSectionLeafChildren(long code);

	/**
	 * 根据教材码查找对应所有叶子章节数据
	 *
	 * @param code
	 *            教材码
	 * @return 章节码列表
	 */
	List<Long> findLeafSectionByTextbook(int code);

	/**
	 * 根据章节码查询是否本章综合与测试
	 *
	 * @param code
	 *            教材码
	 * @return 章节对象
	 */
	Section findIntegrateSectionCode(long code);
}
