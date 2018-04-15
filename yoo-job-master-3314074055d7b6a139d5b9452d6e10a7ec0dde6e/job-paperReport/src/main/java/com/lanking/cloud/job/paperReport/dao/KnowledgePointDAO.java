package com.lanking.cloud.job.paperReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;

public interface KnowledgePointDAO {

	KnowledgePoint get(Long code);

	Map<Long, KnowledgePoint> mget(Collection<Long> codes);
}
