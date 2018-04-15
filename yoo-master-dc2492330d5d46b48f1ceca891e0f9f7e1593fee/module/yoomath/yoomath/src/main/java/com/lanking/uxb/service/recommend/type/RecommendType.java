package com.lanking.uxb.service.recommend.type;

import com.lanking.cloud.sdk.bean.Valueable;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 推荐来源
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年8月3日
 */
public enum RecommendType implements Valueable {

	/**
	 * 热门题目
	 */
	HOT_QUESTION(0),
	/**
	 * 常用教辅
	 */
	COMMON_BOOK(1),
	/**
	 * 薄弱知识点
	 */
	WEAK_KNOWLEDGEPOINT(2),
	/**
	 * 考点
	 */
	EXAMINATION_POINT(3),
	/**
	 * 好题本
	 */
	COLLECTION_QUESTION(4),
	/**
	 * 错题本
	 */
	FALLIBLE_QUESTION(5);

	private int value;

	RecommendType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public String getName(String inName) {
		switch (value) {
		case 0:
			return "根据“热门题目”推荐";
		case 1:
			if (StringUtils.isNotBlank(inName)) {
				return "根据《" + inName + "》推荐";
			}
			return "根据《教辅书名称》推荐";
		case 2:
			return "根据“班级薄弱知识点”推荐";
		case 3:
			return "根据“重要考点”推荐";
		case 4:
			return "根据“好题本中题目”推荐";
		case 5:
			return "根据“错题本中错题”推荐";
		default:
			return "";
		}
	}

	public static RecommendType findByValue(int value) {
		switch (value) {
		case 0:
			return HOT_QUESTION;
		case 1:
			return COMMON_BOOK;
		case 2:
			return WEAK_KNOWLEDGEPOINT;
		case 3:
			return EXAMINATION_POINT;
		case 4:
			return COLLECTION_QUESTION;
		case 5:
			return FALLIBLE_QUESTION;
		default:
			return HOT_QUESTION;
		}
	}
}
