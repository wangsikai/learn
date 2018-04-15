package com.lanking.uxb.service.honor.api;

import com.lanking.cloud.domain.yoo.honor.point.PointAction;
import com.lanking.cloud.domain.yoo.honor.point.PointLog;
import com.lanking.cloud.domain.yoo.honor.point.PointRule;

public interface PointProcessor {

	boolean accpet(PointAction action);

	PointAction getAction();

	PointRule getPointRule();

	PointLog process(PointLog userPointLog);

}
