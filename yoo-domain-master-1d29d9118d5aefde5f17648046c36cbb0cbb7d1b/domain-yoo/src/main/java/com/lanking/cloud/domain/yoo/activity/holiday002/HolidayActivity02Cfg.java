package com.lanking.cloud.domain.yoo.activity.holiday002;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class HolidayActivity02Cfg implements Serializable {

	private static final long serialVersionUID = 4613654250338968839L;

	/**
	 * 活动代码
	 */
	private Long code;

	/**
	 * 存放周阶段
	 */
	private List<List<Date>> phases;

}
