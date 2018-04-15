package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Phase;

/**
 * 提供阶段的相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月24日
 *
 */
public interface PhaseService {

	int PHASE_HIGH = 3;// 高中
	int PHASE_MIDDLE = 2;// 初中

	Phase get(int code);

	List<Phase> getAll();

	Map<Integer, Phase> mgetAll();

	Map<Integer, Phase> mget(Collection<Integer> codes);

}
