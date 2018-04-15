package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.channelSales.channel.api.CsAggStudentService;

@Service
@Transactional(readOnly = true, value = "biDataSourceTransactionManager")
public class CsAggStudentServiceImpl implements CsAggStudentService {
	@Autowired
	@Qualifier("biJdbcTemplate")
	private JdbcTemplate biJdbcTemplate;

	@Override
	public List<Map<String, Object>> queryList(Integer channelCode, String schoolName, Integer phaseCode) {
		String sql = "SELECT t.*,round(t.vip_num*100/t.user_num,2) convert_rate FROM (SELECT b.code_school,a.id_school"
				+ ",b.name_school,c.code_phase id_phase," + "SUM(NUM_REGISTER_SUM)+SUM(NUM_IMPORT_SUM) user_num,"
				+ "SUM(NUM_IMPORT_MEMBER_SUM)+SUM(NUM_REGISTER_MEMBER_SUM) vip_num "
				+ "FROM AGG_STUDENT a INNER JOIN DMN_SCHOOL b ON a.id_school = b.id_school INNER JOIN DMN_PHASE c on b.id_phase = c.id_phase "
				+ "WHERE a.id_school != 99999999 AND a.ID_SCHOOL IS NOT NULL AND a.id_channel = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(channelCode);
		if (schoolName != null) {
			params.add("%" + schoolName + "%");
			sql += "and b.NAME_SCHOOL like ?";
		}
		if (phaseCode != null) {
			params.add(phaseCode);
			sql += " and c.code_phase= ?";
		}
		sql += " GROUP BY b.code_school ) t " + "ORDER BY t.vip_num/t.user_num desc";
		return biJdbcTemplate.queryForList(sql, params.toArray());
	}
}
