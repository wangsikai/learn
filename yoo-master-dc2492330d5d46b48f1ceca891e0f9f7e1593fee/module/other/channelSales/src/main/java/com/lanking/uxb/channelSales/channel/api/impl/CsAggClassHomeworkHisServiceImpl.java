package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.channelSales.channel.api.CsAggClassHomeworkHisService;

@Service
@Transactional(readOnly = true, value = "biDataSourceTransactionManager")
public class CsAggClassHomeworkHisServiceImpl implements CsAggClassHomeworkHisService {

	@Autowired
	@Qualifier("biJdbcTemplate")
	private JdbcTemplate biJdbcTemplate;

	@Override
	public List<Map<String, Object>> hkStat(Long classId, Integer day0) {
		String sql = "SELECT NUM_ASSIGN num_homework,date, ROUND(NUM_ASSIGN_COMMIT*100/NUM_ASSIGN_STUHOMEWORK,0) commit_rate FROM AGG_CLASS_HOMEWORK_HIS "
				+ "WHERE id_class = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(classId);
		if (day0 != null) {
			params.add(getTimeByDay0(day0));
			params.add(new Date());
			sql += "and DATE >= ? and DATE < ?";
		}
		return biJdbcTemplate.queryForList(sql, params.toArray());
	}

	public Date getTimeByDay0(Integer day0) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -day0);
		return calendar.getTime();
	}
}
