package com.lanking.cloud.sdk.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageImpl;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 对象转换器抽象类
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月13日
 * @param <D>
 *            目标对象
 * @param <S>
 *            源对象
 * @param <ID>
 *            对象标识
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class Converter<D, S, ID> implements InitializingBean {
	protected Logger LOG = LoggerFactory.getLogger(getClass());

	private Map<String, Object> hints = Collections.emptyMap();
	private List<ConverterAssembler> assemblers = new ArrayList<ConverterAssembler>();

	public void setHints(Map<String, Object> hints) {
		this.hints = hints;
	}

	public void setAssemblers(List<ConverterAssembler> assemblers) {
		this.assemblers = assemblers;
	}

	public D to(S s) {
		if (s == null) {
			return null;
		}
		D d = convert(s);
		for (ConverterAssembler assembler : assemblers) {
			if (assembler.accept(hints) && assembler.accept(s)) {
				Object value = assembler.getValue(assembler.getKey(s, d));
				if (value != null) {
					assembler.setValue(s, d, value);
				}
			}
		}
		return d;
	}

	public List<D> to(List<S> ss) {
		if (CollectionUtils.isEmpty(ss)) {
			return Collections.emptyList();
		}
		List<D> ds = convert(ss);
		assembler(ss.size(), ss, ds);
		return ds;
	}

	public Map<ID, D> to(Map<ID, S> sMap) {
		if (CollectionUtils.isEmpty(sMap)) {
			return Collections.emptyMap();
		}
		final int len = sMap.size();
		List<S> ss = new ArrayList<S>(sMap.values());
		List<D> ds = convert(ss);
		Map<ID, D> dMap = new HashMap<ID, D>(ss.size());
		for (int i = 0; i < len; i++) {
			dMap.put(getId(ss.get(i)), ds.get(i));
		}
		assembler(len, ss, ds);
		return dMap;
	}

	public Map<ID, D> toMap(List<S> ss) {
		if (CollectionUtils.isEmpty(ss)) {
			return Collections.emptyMap();
		}
		final int len = ss.size();
		List<D> ds = convert(ss);
		Map<ID, D> dMap = new HashMap<ID, D>(ss.size());
		for (int i = 0; i < len; i++) {
			dMap.put(getId(ss.get(i)), ds.get(i));
		}
		assembler(len, ss, ds);
		return dMap;
	}

	private void assembler(int len, List<S> ss, List<D> ds) {
		List<Object> keys = new ArrayList<Object>(len);
		for (ConverterAssembler assembler : assemblers) {
			if (assembler.accept(hints)) {
				keys.clear();
				for (int i = 0; i < len; i++) {
					if (assembler.accept(ss.get(i))) {
						keys.add(assembler.getKey(ss.get(i), ds.get(i)));
					} else {
						// 保持keys个数，避免下面的assembler在填充value时出现index错位
						keys.add(null);
					}
				}
				List<Object> notNullKeys = CollectionUtils.trimNull(keys);
				if (notNullKeys.size() == 0) {
					continue;
				}
				Map<Object, Object> map = assembler.mgetValue(notNullKeys);
				int newLength = keys.size();
				for (int i = 0; i < newLength; i++) {
					Object key = keys.get(i);
					if (key instanceof List) {
						List list = (List) key;
						if (list.isEmpty()) {
							assembler.setValue(ss.get(i), ds.get(i), Collections.EMPTY_LIST);
						} else {
							List<Object> values = new ArrayList<Object>(list.size());
							Object val = map.get(list);
							if (val == null) {
								for (Object k : (List) key) {
									Object v = map.get(k);
									if (v != null) {
										values.add(v);
									}
								}
								assembler.setValue(ss.get(i), ds.get(i), values);
							} else {
								assembler.setValue(ss.get(i), ds.get(i), val);
							}
						}
					} else {
						if (key != null) {
							Object value = map.get(key);
							if (value != null) {
								assembler.setValue(ss.get(i), ds.get(i), value);
							}
						}
					}
				}
			}
		}
	}

	public Page<D> to(Page<S> p) {
		if (p == null || p.isEmpty()) {
			return new PageImpl<D>();
		}
		return new PageImpl<D>(to(p.getItems()), p.getTotalCount(), p.current());
	}

	public <C> CursorPage<C, D> to(CursorPage<C, S> sp) {
		if (sp == null || sp.isEmpty()) {
			return CP.empty();
		}
		return new CursorPageImpl<C, D>(sp.current(), to(sp.getItems()), sp.getTotal(), sp.getPrevCursor(),
				sp.getNextCursor());
	}

	public D get(ID id) {
		return to(internalGet(id));
	}

	public Map<ID, D> mget(Collection<ID> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyMap();
		}
		return to(internalMGet(ids));
	}

	public List<D> mgetList(Collection<ID> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		Map<ID, S> map = internalMGet(ids);
		List<S> list = new ArrayList<S>(map.size());
		for (ID id : ids) {
			S s = map.get(id);
			if (s != null) {
				list.add(s);
			}
		}
		return to(list);
	}

	public List<ID> getIds(List<S> ss) {
		if (CollectionUtils.isEmpty(ss)) {
			return Collections.emptyList();
		}
		List<ID> ids = new ArrayList<ID>(ss.size());
		for (S s : ss) {
			ids.add(getId(s));
		}
		return ids;
	}

	public S from(D d) {
		throw new NotImplementedException();
	}

	protected S internalGet(ID id) {
		throw new NotImplementedException();
	}

	protected Map<ID, S> internalMGet(Collection<ID> ids) {
		throw new NotImplementedException();
	}

	protected abstract ID getId(S s);

	protected abstract D convert(S s);

	protected List<D> convert(List<S> ss) {
		List<D> ds = new ArrayList<D>(ss.size());
		for (S s : ss) {
			ds.add(convert(s));
		}
		return ds;
	}

	protected void initAssemblers(List<ConverterAssembler> assemblers) {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initAssemblers(assemblers);
	}

	protected String validBlank(String s) {
		if (StringUtils.isBlank(s)) {
			return StringUtils.EMPTY;
		}
		return s;
	}
}
