package com.lanking.cloud.job.correctQuestionFailRecord.DAO;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;

/**
 * <p>Description:小优快批传提记录数据库操作接口<p>
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
public interface CorrectQuestionFailRecordDao {
	
	/**
	 * <p>Description:查询传输失败的CorrectQuestion记录<p>
	 * @date: 2018年3月19日
	 * @author: pengcheng.yu
	 * @return
	 */
	List<CorrectQuestionFailRecord> queryCorrectQuestionFailRecords();
	
	void batchSave(Collection<Long> ids);

}
