package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.channelSales.channel.api.CsAggClassHomeworkService;

@Service
@Transactional(readOnly = true, value = "biDataSourceTransactionManager")
public class CsAggClassHomeworkServiceImpl implements CsAggClassHomeworkService {

	@Autowired
	@Qualifier("biJdbcTemplate")
	private JdbcTemplate biJdbcTemplate;

	@Override
	public List<Map<String, Object>> queryList(String schoolName, Integer channelCode, Integer phaseCode) {
		String sql = "select b.ID_SCHOOL,sum(NUM_ASSIGN_SUM) hk_num from FCT_CLASS a inner join FCT_TEACHER b on a.ID_TEACHER = b.ID_TEACHER AND b.ID_CHANNEL = ?"
				+ " inner join AGG_CLASS_HOMEWORK c on a.ID_CLASS = c.ID_CLASS and id_school != 99999999 group by b.ID_SCHOOL";
		return biJdbcTemplate.queryForList(sql, new Object[] { channelCode });
	}

	@Override
	public Map<String, Object> queryMapByClass(Long classId) {
		String sql = "select NUM_STUDENT_SUM num_student,NUM_ASSIGN_SUM num_homework_sum,num_member_sum,round(NUM_ASSIGN_COMMIT_SUM*100/NUM_ASSIGN_STUHOMEWORK_SUM,0) commit_rate,"
				+ "round(RATE_RIGHT_SUM*100,0) right_rate " + "from AGG_CLASS_HOMEWORK where id_class = ?";
		List<Map<String, Object>> list = biJdbcTemplate.queryForList(sql, new Object[] { classId });
		return list.size() == 0 ? null : list.get(0);
	}
}
