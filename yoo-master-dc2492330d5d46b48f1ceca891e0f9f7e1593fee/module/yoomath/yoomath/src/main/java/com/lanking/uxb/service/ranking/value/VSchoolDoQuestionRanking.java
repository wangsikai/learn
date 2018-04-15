package com.lanking.uxb.service.ranking.value;

import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 学校内班级做题排名
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
public class VSchoolDoQuestionRanking extends VDoQuestionRanking {

	private static final long serialVersionUID = 6788687369310488685L;

	// 班级信息
	private VHomeworkClazz clazz;

	public VHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VHomeworkClazz clazz) {
		this.clazz = clazz;
	}
}
