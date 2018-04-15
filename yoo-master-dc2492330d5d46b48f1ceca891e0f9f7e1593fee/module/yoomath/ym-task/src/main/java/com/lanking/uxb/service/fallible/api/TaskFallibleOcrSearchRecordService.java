package com.lanking.uxb.service.fallible.api;

import java.util.Date;
import java.util.List;

import com.lanking.cloud.domain.yoomath.fallible.FallibleOcrSearchRecord;

/**
 * ocr、search结果记录接口
 * 
 * @since 2.2.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月1日
 */
public interface TaskFallibleOcrSearchRecordService {

	FallibleOcrSearchRecord create(long fileId, List<Long> questions, Date createAt);

	void choose(long fileId, long chooseQuestion, Date chooseAt);
}
