package com.lanking.uxb.rescon.book.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.book.value.VBookCatalog;
import com.lanking.uxb.service.counter.api.impl.BookCatalogCounterProvider;

@Component
public class ResconBookCatalogConvert extends Converter<VBookCatalog, BookCatalog, Long> {

	private final static String[] UNIT = { "", "十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿" };
	private final static String[] UPPER_CASE = { "", "一", "二", "三", "四", "五", "六", "七", "八", "九" };

	@Autowired
	private BookCatalogCounterProvider pcCounterProvider;

	@Override
	protected Long getId(BookCatalog s) {
		return s.getId();
	}

	private void internalAssemblyCatalogTree(List<VBookCatalog> dest, VBookCatalog v) {
		if (v.getLevel() == 1) {
			v.setIndex(String.valueOf(dest.size() + 1));
			dest.add(v);
		} else {
			for (VBookCatalog pc : dest) {
				if (pc.getId() == v.getPid()) {
					v.setIndex(pc.getIndex() + "." + (pc.getChildren().size() + 1));
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblyCatalogTree(pc.getChildren(), v);
				}
			}
		}
	}

	public void assemblyResourceCount(VBookCatalog catalog) {
		List<VBookCatalog> children = catalog.getChildren();
		if (children.size() > 0) {
			long rsCount = 0;
			for (VBookCatalog c : children) {
				rsCount += c.getResourceCount();
				assemblyResourceCount(c);
			}
			catalog.setResourceCount(rsCount);
		}
	}

	public void assemblyResourceCount(List<VBookCatalog> dest) {
		for (VBookCatalog pc : dest) {
			assemblyResourceCount(pc);
		}
		for (VBookCatalog pc : dest) {
			assemblyResourceCount(pc);
		}
	}

	public List<VBookCatalog> assemblyCatalogTree(List<VBookCatalog> src) {
		List<VBookCatalog> dest = Lists.newArrayList();
		for (VBookCatalog v : src) {
			internalAssemblyCatalogTree(dest, v);
		}
		for (VBookCatalog v : dest) {
			String newIndex = "";
			for (int i = v.getIndex().length() - 1; i >= 0; i--) {
				newIndex += UPPER_CASE[Integer.valueOf(String.valueOf(v.getIndex()
						.charAt(v.getIndex().length() - 1 - i)))] + UNIT[i];
			}
			if (newIndex.length() > 1 && newIndex.startsWith(UPPER_CASE[1])) {
				newIndex = newIndex.substring(1);
			}
			newIndex = "第" + newIndex + "章";
			v.setIndex(newIndex);
		}
		assemblyResourceCount(dest);
		return dest;
	}

	@Override
	protected VBookCatalog convert(BookCatalog s) {
		VBookCatalog v = new VBookCatalog();
		v.setId(s.getId());
		v.setBookVersionId(s.getBookVersionId());
		v.setName(s.getName());
		v.setLevel(s.getLevel());
		v.setSequence(s.getSequence());
		v.setPid(s.getPid());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VBookCatalog, BookCatalog, Long, Counter>() {

			@Override
			public boolean accept(BookCatalog s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookCatalog s, VBookCatalog d) {
				return s.getId();
			}

			@Override
			public void setValue(BookCatalog s, VBookCatalog d, Counter value) {
				if (value != null) {
					d.setResourceCount(value.getCount1());
				}
			}

			@Override
			public Counter getValue(Long key) {
				if (key == null) {
					return null;
				}
				return pcCounterProvider.getCounter(key);
			}

			@Override
			public Map<Long, Counter> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return pcCounterProvider.getCounters(keys);
			}

		});
	}

}
