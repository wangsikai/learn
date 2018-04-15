package com.lanking.stuWeekReport.dao;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.homework.Homework;

public interface HomeworkDAO {

	Map<Long, Homework> mget(Collection<Long> ids);

}
