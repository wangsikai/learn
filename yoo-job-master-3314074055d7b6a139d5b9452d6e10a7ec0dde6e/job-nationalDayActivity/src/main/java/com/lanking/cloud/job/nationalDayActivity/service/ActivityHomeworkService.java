package com.lanking.cloud.job.nationalDayActivity.service;

public interface ActivityHomeworkService {

	void publishHomework(long teacherId, long homeworkId);

	void deleteHomework(long homeworkId);

	void issueHomework(long homeworkId);
}
