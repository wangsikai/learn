package com.lanking.cloud.component.db.support.jdbcTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.lanking.cloud.component.db.sql.SqlRenderer;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * just do sql renderer
 * 
 * @since 4.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年6月5日
 */
public class JdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {

	private SqlRenderer sqlRenderer;

	private String renderer(String sql) {
		if (sql.startsWith("$")) {
			String[] args = sql.split("->");
			return sqlRenderer.render(args[0].replace("$", StringUtils.EMPTY), args[1]);
		}
		return sql;
	}

	private String[] renderer(String[] sqls) {
		String[] $sqls = new String[sqls.length];
		int index = 0;
		for (String sql : sqls) {
			$sqls[index] = renderer(sql);
			index++;
		}
		return $sqls;
	}

	// setter getter
	public SqlRenderer getSqlRenderer() {
		return sqlRenderer;
	}

	public void setSqlRenderer(SqlRenderer sqlRenderer) {
		this.sqlRenderer = sqlRenderer;
	}

	// constructor
	public JdbcTemplate() {
		super();
	}

	public JdbcTemplate(DataSource dataSource, boolean lazyInit) {
		super(dataSource, lazyInit);
	}

	public JdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	// custom constructor
	public JdbcTemplate(SqlRenderer sqlRenderer) {
		super();
		this.sqlRenderer = sqlRenderer;
	}

	public JdbcTemplate(DataSource dataSource, boolean lazyInit, SqlRenderer sqlRenderer) {
		super(dataSource, lazyInit);
		this.sqlRenderer = sqlRenderer;
	}

	public JdbcTemplate(DataSource dataSource, SqlRenderer sqlRenderer) {
		super(dataSource);
		this.sqlRenderer = sqlRenderer;
	}

	// override api
	@Override
	public void execute(String sql) throws DataAccessException {
		super.execute(renderer(sql));
	}

	@Override
	public <T> T query(String sql, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(renderer(sql), rse);
	}

	@Override
	public void query(String sql, RowCallbackHandler rch) throws DataAccessException {
		super.query(renderer(sql), rch);
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(renderer(sql), rowMapper);
	}

	@Override
	public Map<String, Object> queryForMap(String sql) throws DataAccessException {
		return super.queryForMap(renderer(sql));
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper) throws DataAccessException {
		return super.queryForObject(renderer(sql), rowMapper);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
		return super.queryForObject(renderer(sql), requiredType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
		return super.queryForList(renderer(sql), elementType);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
		return super.queryForList(renderer(sql));
	}

	@Override
	public SqlRowSet queryForRowSet(String sql) throws DataAccessException {
		return super.queryForRowSet(renderer(sql));
	}

	@Override
	public int update(String sql) throws DataAccessException {
		return super.update(renderer(sql));
	}

	@Override
	public int[] batchUpdate(String... sql) throws DataAccessException {
		return super.batchUpdate(renderer(sql));
	}

	@Override
	public <T> T execute(String sql, PreparedStatementCallback<T> action) throws DataAccessException {
		return super.execute(renderer(sql), action);
	}

	@Override
	public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(renderer(sql), pss, rse);
	}

	@Override
	public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse)
			throws DataAccessException {
		return super.query(renderer(sql), args, argTypes, rse);
	}

	@Override
	public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) throws DataAccessException {
		return super.query(renderer(sql), args, rse);
	}

	@Override
	public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
		return super.query(renderer(sql), rse, args);
	}

	@Override
	public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) throws DataAccessException {
		super.query(renderer(sql), pss, rch);
	}

	@Override
	public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) throws DataAccessException {
		super.query(renderer(sql), args, argTypes, rch);
	}

	@Override
	public void query(String sql, Object[] args, RowCallbackHandler rch) throws DataAccessException {
		super.query(renderer(sql), args, rch);
	}

	@Override
	public void query(String sql, RowCallbackHandler rch, Object... args) throws DataAccessException {
		super.query(renderer(sql), rch, args);
	}

	@Override
	public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.query(renderer(sql), pss, rowMapper);
	}

	@Override
	public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.query(renderer(sql), args, argTypes, rowMapper);
	}

	@Override
	public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return super.query(renderer(sql), args, rowMapper);
	}

	@Override
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		return super.query(renderer(sql), rowMapper, args);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper)
			throws DataAccessException {
		return super.queryForObject(renderer(sql), args, argTypes, rowMapper);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) throws DataAccessException {
		return super.queryForObject(renderer(sql), args, rowMapper);
	}

	@Override
	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
		return super.queryForObject(renderer(sql), rowMapper, args);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, int[] argTypes, Class<T> requiredType)
			throws DataAccessException {
		return super.queryForObject(renderer(sql), args, argTypes, requiredType);
	}

	@Override
	public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) throws DataAccessException {
		return super.queryForObject(renderer(sql), args, requiredType);
	}

	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType, Object... args) throws DataAccessException {
		return super.queryForObject(renderer(sql), requiredType, args);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.queryForMap(renderer(sql), args, argTypes);
	}

	@Override
	public Map<String, Object> queryForMap(String sql, Object... args) throws DataAccessException {
		return super.queryForMap(renderer(sql), args);
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType)
			throws DataAccessException {
		return super.queryForList(renderer(sql), args, argTypes, elementType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) throws DataAccessException {
		return super.queryForList(renderer(sql), args, elementType);
	}

	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) throws DataAccessException {
		return super.queryForList(renderer(sql), elementType, args);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		return super.queryForList(renderer(sql), args, argTypes);
	}

	@Override
	public List<Map<String, Object>> queryForList(String sql, Object... args) throws DataAccessException {
		return super.queryForList(renderer(sql), args);
	}

	@Override
	public SqlRowSet queryForRowSet(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.queryForRowSet(renderer(sql), args, argTypes);
	}

	@Override
	public SqlRowSet queryForRowSet(String sql, Object... args) throws DataAccessException {
		return super.queryForRowSet(renderer(sql), args);
	}

	@Override
	public int update(String sql, PreparedStatementSetter pss) throws DataAccessException {
		return super.update(renderer(sql), pss);
	}

	@Override
	public int update(String sql, Object[] args, int[] argTypes) throws DataAccessException {
		return super.update(renderer(sql), args, argTypes);
	}

	@Override
	public int update(String sql, Object... args) throws DataAccessException {
		return super.update(renderer(sql), args);
	}

	@Override
	public int[] batchUpdate(String sql, BatchPreparedStatementSetter pss) throws DataAccessException {
		return super.batchUpdate(renderer(sql), pss);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs) throws DataAccessException {
		return super.batchUpdate(renderer(sql), batchArgs);
	}

	@Override
	public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) throws DataAccessException {
		return super.batchUpdate(renderer(sql), batchArgs, argTypes);
	}

	@Override
	public <T> int[][] batchUpdate(String sql, Collection<T> batchArgs, int batchSize,
			ParameterizedPreparedStatementSetter<T> pss) throws DataAccessException {
		return super.batchUpdate(renderer(sql), batchArgs, batchSize, pss);
	}
}
