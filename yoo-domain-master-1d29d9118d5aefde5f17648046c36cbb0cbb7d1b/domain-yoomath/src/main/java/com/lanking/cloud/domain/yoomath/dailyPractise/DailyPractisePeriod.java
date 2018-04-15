package com.lanking.cloud.domain.yoomath.dailyPractise;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * 章节&课时关系
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "daily_practise_period")
@IdClass(DailyPractisePeriodKey.class)
public class DailyPractisePeriod extends DailyPractisePeriodKey {

	private static final long serialVersionUID = 6810573679991552651L;
}
