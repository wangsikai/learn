package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.homework.HomeworkOperationLog;
import com.lanking.cloud.domain.yoomath.homework.HomeworkOperationType;

/**
 * 作业操作log
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月10日
 */
public interface ZyHomeworkOperationLogService {

	HomeworkOperationLog log(HomeworkOperationType type, long homeworkId);

	boolean exist(HomeworkOperationType type, long homeworkId);
}
