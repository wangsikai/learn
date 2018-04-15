package com.lanking.uxb.service.zuoye.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VBookCatalog;

/**
 * 书本章节转换convert
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月4日 下午1:30:03
 */
@Component
public class ZyBookCatalogConvert extends Converter<VBookCatalog, BookCatalog, Long> {

	@Override
	protected Long getId(BookCatalog s) {
		return s.getId();
	}

	@Override
	protected VBookCatalog convert(BookCatalog s) {
		VBookCatalog v = new VBookCatalog();
		v.setCode(s.getId());
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.setPcode(s.getPid() == null ? 0 : s.getPid());
		v.getAllChild().add(s.getId());
		return v;
	}

	private void internalAssemblySectionTree(List<VBookCatalog> dest, VBookCatalog v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VBookCatalog pc : dest) {
				if (pc.getCode() == v.getPcode()) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblySectionTree(pc.getChildren(), v);
				}
			}
		}
	}

	/**
	 * 组装树形结构的章节数据
	 * 
	 * @since 2.5
	 * @param src
	 *            VBookCatalog VO list
	 * @return 树形结构的章节数据
	 */
	public List<VBookCatalog> assemblySectionTree(List<VBookCatalog> src) {
		List<VBookCatalog> dest = Lists.newArrayList();
		for (VBookCatalog v : src) {
			internalAssemblySectionTree(dest, v);
		}
		return dest;
	}

}
