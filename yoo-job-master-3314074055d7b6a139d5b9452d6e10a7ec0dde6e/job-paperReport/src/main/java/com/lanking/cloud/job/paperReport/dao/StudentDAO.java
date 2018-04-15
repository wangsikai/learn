package com.lanking.cloud.job.paperReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoo.user.Student;

public interface StudentDAO {

	Student get(Long id);

	Map<Long, Student> mget(Collection<Long> ids);
}
