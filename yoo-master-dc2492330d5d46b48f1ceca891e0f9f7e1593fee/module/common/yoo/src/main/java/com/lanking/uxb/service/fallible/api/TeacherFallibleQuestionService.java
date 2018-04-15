package com.lanking.uxb.service.fallible.api;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;

public interface TeacherFallibleQuestionService {

	long update(long teacherId, long questionId, HomeworkAnswerResult result, Integer subjectCode, Type type,
			Integer typeCode, Double difficulty);
}
