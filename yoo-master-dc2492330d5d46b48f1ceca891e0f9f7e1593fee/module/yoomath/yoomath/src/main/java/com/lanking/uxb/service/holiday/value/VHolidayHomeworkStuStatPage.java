package com.lanking.uxb.service.holiday.value;

import com.lanking.cloud.sdk.value.VPage;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 教师查看假期作业 学生统计vo Page
 * 
 * @since 1.9
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月24日 下午3:52:53
 */
public class VHolidayHomeworkStuStatPage extends VPage<VHolidayStuHomework> {

	private static final long serialVersionUID = 5504764335835093041L;

	private VHomeworkClazz clazz;
	private VHolidayHomework holidayHomework;

	public VHomeworkClazz getClazz() {
		return clazz;
	}

	public void setClazz(VHomeworkClazz clazz) {
		this.clazz = clazz;
	}

	public VHolidayHomework getHolidayHomework() {
		return holidayHomework;
	}

	public void setHolidayHomework(VHolidayHomework holidayHomework) {
		this.holidayHomework = holidayHomework;
	}

}
