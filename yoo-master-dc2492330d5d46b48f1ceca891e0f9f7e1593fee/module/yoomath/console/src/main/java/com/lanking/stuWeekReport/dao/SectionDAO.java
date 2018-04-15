package com.lanking.stuWeekReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Section;

public interface SectionDAO {

	Map<Long, Section> mget(Collection<Long> codes);

}
