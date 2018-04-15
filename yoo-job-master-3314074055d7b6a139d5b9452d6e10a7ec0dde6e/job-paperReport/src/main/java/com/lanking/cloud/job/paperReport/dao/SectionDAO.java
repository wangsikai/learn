package com.lanking.cloud.job.paperReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Section;

public interface SectionDAO {

	Section get(Long code);

	Map<Long, Section> mget(Collection<Long> codes);
}
