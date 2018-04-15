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

import com.lanking.uxb.channelSales.channel.api.CsAggSchoolService;

@Service
@Transactional(readOnly = true, value = "biDataSourceTransactionManager")
public class CsAggSchoolServiceImpl implements CsAggSchoolService {
	@Autowired
	@Qualifier("biJdbcTemplate")
	private JdbcTemplate biJdbcTemplate;

	@Override
	public Map<String, Object> studentStat(String schoolCode, Integer channelId) {
		String sql = "SELECT SUM(t.NUM_REGISTER_SUM)+SUM(t.NUM_IMPORT_SUM) as count,"
				+ "SUM(t.NUM_IMPORT_MEMBER_SUM)+SUM(t.NUM_REGISTER_MEMBER_SUM) as vipCount  "
				+ "from AGG_STUDENT t INNER JOIN DMN_SCHOOL ds ON ds.ID_SCHOOL= t.ID_SCHOOL where ds.CODE_SCHOOL =?  and  t.ID_CHANNEL=?";
		return biJdbcTemplate.queryForMap(sql, new Object[] { schoolCode, channelId });
	}

	@Override
	public Map<String, Object> teacherStat(String schoolCode, Integer channelId) {
		String sql = "SELECT SUM(t.NUM_REGISTER_SUM) + SUM(t.NUM_IMPORT_SUM) AS count,SUM(t.NUM_IMPORT_MEMBER_SUM) + SUM(t.NUM_REGISTER_MEMBER_SUM) AS vipCount "
				+ " FROM AGG_TEACHER t INNER JOIN DMN_SCHOOL ds ON ds.ID_SCHOOL= t.ID_SCHOOL"
				+ " WHERE ds.CODE_SCHOOL = ? and t.ID_CHANNEL=?";
		return biJdbcTemplate.queryForMap(sql, new Object[] { schoolCode, channelId });
	}

	@Override
	public Long getClazzHkCount(String schoolCode, Integer channelId) {
		String sql = "select sum(NUM_ASSIGN_SUM)  from AGG_CLASS_HOMEWORK ach INNER JOIN FCT_CLASS fc ON fc.ID_CLASS=ach.ID_CLASS and fc.`STATUS`=0 INNER JOIN "
				+ " FCT_TEACHER ft ON ft.ID_TEACHER =fc.ID_TEACHER INNER JOIN DMN_SCHOOL ds ON ds.ID_SCHOOL=ft.ID_SCHOOL"
				+ " where ds.CODE_SCHOOL =? and ft.ID_CHANNEL=?";
		return biJdbcTemplate.queryForObject(sql, new Object[] { schoolCode, channelId }, Long.class);
	}

	@Override
	public Map<String, Object> getNohomeWorkCount(String schoolCode, Integer channelId) {
		String sql = "select sum(t.NUM_CLASS_SUM) as clazzCount,sum(t.NUM_7D_NOHOMEWORK) as num_7d,sum(t.NUM_15D_NOHOMEWORK) as num_15d,sum(t.NUM_30D_NOHOMEWORK) as num_30d from AGG_CLASS t INNER JOIN DMN_SCHOOL ds ON ds.ID_SCHOOL=t.ID_SCHOOL where ds.CODE_SCHOOL =? and t.ID_CHANNEL=? ";
		return biJdbcTemplate.queryForMap(sql, new Object[] { schoolCode, channelId });
	}

	@Override
	public List<Map<String, Object>> getAggHomeWork(String schoolCode, Integer channelId, Integer day0) {
		String sql = "select achh.DATE,sum(achh.NUM_ASSIGN)  sum,round((SUM(achh.NUM_ASSIGN_COMMIT)*100/sum(achh.NUM_ASSIGN_STUHOMEWORK)),0) sub from AGG_CLASS_HOMEWORK_HIS achh INNER JOIN FCT_CLASS t ON achh.ID_CLASS=t.ID_CLASS  INNER JOIN FCT_TEACHER f ON  f.ID_TEACHER=t.ID_TEACHER INNER JOIN DMN_SCHOOL ds ON  ds.ID_SCHOOL=f.ID_SCHOOL  "
				+ "where t.STATUS =0 and ds.CODE_SCHOOL=? and f.ID_CHANNEL=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(schoolCode);
		params.add(channelId);
		if (day0 != null) {
			params.add(getTimeByDay0(day0));
			params.add(new Date());
			sql += "and achh.DATE >= ? and achh.DATE < ?";
		}
		sql += "GROUP BY achh.DATE";
		return biJdbcTemplate.queryForList(sql, params.toArray());
	}

	public Date getTimeByDay0(Integer day0) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -day0);
		return calendar.getTime();
	}

	@Override
	public List<Map<String, Object>> getRegisterCount(String schoolCode, Integer channelId, Integer day0) {
		String sql = "select DATE,SUM(sum) as sum from( select t.DATE,(t.NUM_REGISTER_DAYACTIVE+t.NUM_IMPORT_DAYACTIVE) as sum,ds.CODE_SCHOOL,t.ID_CHANNEL from AGG_TEACHER_HIS"
				+ " t INNER JOIN DMN_SCHOOL ds ON ds.ID_SCHOOL =t.ID_SCHOOL union all "
				+ " select t.DATE,(t.NUM_REGISTER_DAYACTIVE+t.NUM_IMPORT_DAYACTIVE) as sum,ds.CODE_SCHOOL,t.ID_CHANNEL from AGG_STUDENT_HIS "
				+ " t INNER JOIN DMN_SCHOOL ds ON ds.ID_SCHOOL =t.ID_SCHOOL "
				+ " ) t where t.CODE_SCHOOL =? and t.ID_CHANNEL=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(schoolCode);
		params.add(channelId);
		if (day0 != null) {
			params.add(getTimeByDay0(day0));
			params.add(new Date());
			sql += " and t.DATE >= ? and t.DATE < ?";
		}
		sql += " GROUP BY DATE";

		return biJdbcTemplate.queryForList(sql, params.toArray());
	}

	@Override
	public List<Map<String, Object>> getClazzDateByYeart(String schoolCode, Integer channelId, Integer year) {
		String sql = "select fc.ID_CLASS as id,fc.NAME_CLASS as name,ft.name as teaName,ach.NUM_STUDENT_SUM as sum,ach.NUM_MEMBER_SUM as vipCount,ach.NUM_ASSIGN_SUM as hkCount,round(ach.NUM_ASSIGN_COMMIT_SUM*100/ach.NUM_ASSIGN_STUHOMEWORK_SUM,0) sub "
				+ "from FCT_CLASS fc INNER JOIN AGG_CLASS_HOMEWORK ach ON fc.ID_CLASS=ach.ID_CLASS "
				+ "INNER JOIN FCT_TEACHER ft ON ft.ID_TEACHER=fc.ID_TEACHER INNER JOIN DMN_SCHOOL ds ON  ds.ID_SCHOOL=ft.ID_SCHOOL  where "
				+ " ft.ID_CHANNEL=? and ds.CODE_SCHOOL=? and fc.status=0";
		List<Object> params = new ArrayList<Object>();
		params.add(channelId);
		params.add(schoolCode);
		if (year != null) {
			params.add(year);
			sql += " DATE_FORMAT(fc.CREATE_TIME,'%X') =?  ";
		}
		return biJdbcTemplate.queryForList(sql, params.toArray());
	}

	@Override
	public List<Map<String, Object>> getNoClazzDateByDay(String schoolCode, Integer channelId, Integer day0) {
		String sql = "select ach.ID_CLASS as id,fc.NAME_CLASS as name, ft.name as teaName,ach.NUM_STUDENT_SUM as sum,ach.NUM_MEMBER_SUM as vipCount,ach.NUM_ASSIGN_SUM as hkCount,round(ach.NUM_ASSIGN_COMMIT_SUM*100/ach.NUM_ASSIGN_STUHOMEWORK_SUM,0) sub "
				+ "from AGG_CLASS_HOMEWORK ach INNER JOIN FCT_CLASS fc on ach.ID_CLASS=fc.ID_CLASS and fc.STATUS=0  "
				+ "INNER JOIN FCT_TEACHER ft ON ft.ID_TEACHER=fc.ID_TEACHER INNER JOIN DMN_SCHOOL ds ON  ds.ID_SCHOOL=ft.ID_SCHOOL "
				+ "where  ds.CODE_SCHOOL =? and ft.ID_CHANNEL =? ";
		List<Object> params = new ArrayList<Object>();
		params.add(schoolCode);
		params.add(channelId);
		if (day0 != null) {
			params.add(day0);
			sql += " and ach.NUM_NOHOMEWORK_DAY >=?";

		}
		return biJdbcTemplate.queryForList(sql, params.toArray());
	}
}
