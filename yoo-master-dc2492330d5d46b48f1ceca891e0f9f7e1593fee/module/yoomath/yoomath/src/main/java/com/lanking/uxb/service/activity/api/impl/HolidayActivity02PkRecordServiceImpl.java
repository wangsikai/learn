package com.lanking.uxb.service.activity.api.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.api.HolidayActivity02PkRecordService;

/**
 * 假期活动02接口实现
 * 
 * @author qiuxue.jiang
 *
 */
@Service
@Transactional(readOnly = true)
public class HolidayActivity02PkRecordServiceImpl implements HolidayActivity02PkRecordService {
	@Autowired
	@Qualifier("HolidayActivity02PKRecordRepo")
	private Repo<HolidayActivity02PKRecord, Long> repo;

	@Override
	@Transactional
	public HolidayActivity02PKRecord addPkRecord(Boolean history, HolidayActivity02PKRecord userRecord,
			HolidayActivity02PKRecord pkUserRecord) {
		if(!history){
			repo.save(userRecord);
			repo.save(pkUserRecord);
			
			userRecord.setPkRecordId(pkUserRecord.getId());
			pkUserRecord.setPkRecordId(userRecord.getId());
			
			repo.save(userRecord);
			repo.save(pkUserRecord);
			
			return pkUserRecord;
		} else {
			repo.save(userRecord);
			
			return null;
		}
	}

	@Override
	public List<HolidayActivity02PKRecord> listAllPkRecord(long activityCode, long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		List<HolidayActivity02PKRecord> records = repo.find("$findAllPkRecords", params).list();
		
		return records;
	}

	@Override
	public HolidayActivity02PKRecord getARandomPkRecord(long activityCode, long userId) {
		Params params = Params.param("userId", userId);
		params.put("code", activityCode);
		//先取一千条出来
		List<HolidayActivity02PKRecord> records = repo.find("$getRandomPkRecords", params).list();
		// 打乱顺序
        Collections.shuffle(records);
        return records.get(0);
	}

	@Override
	public HolidayActivity02PKRecord get(long pkId) {
		return repo.get(pkId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> listAllPkRecord(Long code, Long userId, Integer size) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		if (size != null) {
			params.put("size", size);
		}
		
		return repo.find("$getAllPkRecordBySize", params).list(Map.class);
	}

	
}
