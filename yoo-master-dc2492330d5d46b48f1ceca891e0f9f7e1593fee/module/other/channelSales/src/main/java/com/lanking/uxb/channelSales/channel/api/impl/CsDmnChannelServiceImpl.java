package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.uxb.channelSales.channel.api.CsDmnChannelService;

@Service
@Transactional(readOnly = true, value = "biDataSourceTransactionManager")
public class CsDmnChannelServiceImpl implements CsDmnChannelService {
	@Autowired
	@Qualifier("biJdbcTemplate")
	private JdbcTemplate biJdbcTemplate;

	@Override
	public Integer getIdChannel(Integer channelCode) {
		String sql = "SELECT id_channel FROM DMN_CHANNEL WHERE CODE_CHANNEL = ?";
		List<Map<String, Object>> list = biJdbcTemplate.queryForList(sql, new Object[] { channelCode });
		return list.size() == 0 ? null : Integer.parseInt(list.get(0).get("id_channel").toString());
	}
}
