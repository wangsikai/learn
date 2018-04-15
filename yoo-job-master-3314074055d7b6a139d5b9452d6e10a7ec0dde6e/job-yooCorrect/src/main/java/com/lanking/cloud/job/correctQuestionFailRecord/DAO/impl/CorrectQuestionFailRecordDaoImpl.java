package com.lanking.cloud.job.correctQuestionFailRecord.DAO.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;
import com.lanking.cloud.job.correctQuestionFailRecord.DAO.CorrectQuestionFailRecordDao;
import com.lanking.cloud.sdk.data.Params;

/**
 * <p>Title:</p>
 * <p>Description:<p>
 * @author pengcheng.yu
 * @date 2018年3月19日
 * @since 小优秀快批
 */
@Component
public class CorrectQuestionFailRecordDaoImpl implements CorrectQuestionFailRecordDao {

	@Autowired
	@Qualifier("CorrectQuestionFailRecordRepo")
	private Repo<CorrectQuestionFailRecord,Long> repo;
	@Override
	public List<CorrectQuestionFailRecord> queryCorrectQuestionFailRecords() {
		return repo.find("$queryCorrectQuestionFailRecords").list();
	}
	@Override
	public void batchSave(Collection<Long> ids) {
		Params params = Params.param();
		params.put("ids", ids);
		params.put("successAt", new Date());
		repo.execute("$deleteRecords", params);
	}

}
