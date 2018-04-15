package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;

/**
 * 教材版本相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
public interface TextbookCategoryService {
	// 苏教版
	int SU_JIAO_BAN = 12;
	// 苏科版
	int SU_KE_BAN = 15;

	TextbookCategory get(Integer code);

	List<TextbookCategory> getAll();

	Map<Integer, TextbookCategory> mget(Collection<Integer> codes);

	List<TextbookCategory> mgetList(Collection<Integer> codes);

	/**
	 * 按照产品和阶段查找版本(目前只实现了Product.YOOMATH)
	 * 
	 * @since yoomath V1.2
	 * @param product
	 *            产品
	 * @param codePhase
	 *            阶段
	 * @return List
	 */
	List<TextbookCategory> find(Product product, Integer codePhase);

}
