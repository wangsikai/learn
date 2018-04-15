package com.lanking.uxb.service.honor.api;

import java.util.List;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.honor.UserLevels;

/**
 * 会员等级对照表相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
public interface UserLevelsService {

	int MAXLEVEL = 12; // 最高等级
	int MIDDLELEVEL = 6; // 中间等级，等级大于6级，页面展示6级到12级进度条

	/**
	 * 根据等级获取对应的信息
	 * 
	 * @param level
	 *            等级
	 * @param product
	 *            产品名称(YOOMATH)
	 * @return
	 */
	UserLevels getUserLevel(int level, Product product);

	/**
	 * 根据成长值 获取对应等级
	 * 
	 * @param growth
	 *            成长值
	 * @return 对应等级
	 */
	Integer getLevelByGrowthValue(int growth);

	/**
	 * 获取等级
	 * 
	 * @param startLevel
	 *            开始等级,开区间
	 * @param endLevel
	 *            结束等级,闭区间
	 * @param product
	 * @return
	 */
	List<UserLevels> getUserLevel(int startLevel, int endLevel, Product product);
}
