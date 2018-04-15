package com.lanking.cloud.job.paperReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;

public interface HomeworkClazzDAO {

	HomeworkClazz get(Long classId);

	Map<Long, HomeworkClazz> mget(Collection<Long> classIds);
}
