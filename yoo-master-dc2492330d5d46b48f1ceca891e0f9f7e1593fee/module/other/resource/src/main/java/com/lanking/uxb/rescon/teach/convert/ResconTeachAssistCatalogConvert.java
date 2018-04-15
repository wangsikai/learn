package com.lanking.uxb.rescon.teach.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistCatalog;

/**
 * 教辅菜单转换.
 * 
 * @author wlche
 *
 */
@Component
public class ResconTeachAssistCatalogConvert extends Converter<VTeachAssistCatalog, TeachAssistCatalog, Long> {

	@Override
	protected Long getId(TeachAssistCatalog s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistCatalog convert(TeachAssistCatalog s) {
		VTeachAssistCatalog vo = new VTeachAssistCatalog();
		vo.setCheckStatus(s.getCheckStatus());
		vo.setId(s.getId());
		vo.setLevel(s.getLevel());
		vo.setName(s.getName());
		vo.setPid(s.getPid());
		vo.setSequence(s.getSequence());
		vo.setTeachassistVersionId(s.getTeachassistVersionId());
		return vo;
	}

	public List<VTeachAssistCatalog> assemblyCatalogTree(List<VTeachAssistCatalog> src) {
		List<VTeachAssistCatalog> dest = Lists.newArrayList();
		for (VTeachAssistCatalog v : src) {
			internalAssemblyCatalogTree(dest, v);
		}
		assemblyResourceCount(dest);
		return dest;
	}

	private void internalAssemblyCatalogTree(List<VTeachAssistCatalog> dest, VTeachAssistCatalog v) {
		if (v.getLevel() == 1) {
			v.setIndex(String.valueOf(dest.size() + 1));
			dest.add(v);
		} else {
			for (VTeachAssistCatalog pc : dest) {
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

	private void assemblyResourceCount(VTeachAssistCatalog catalog) {
		List<VTeachAssistCatalog> children = catalog.getChildren();
		if (children.size() > 0) {
			long rsCount = 0;
			for (VTeachAssistCatalog c : children) {
				rsCount += c.getResourceCount();
				assemblyResourceCount(c);
			}
			catalog.setResourceCount(rsCount);
		}
	}

	private void assemblyResourceCount(List<VTeachAssistCatalog> dest) {
		for (VTeachAssistCatalog pc : dest) {
			assemblyResourceCount(pc);
		}
		for (VTeachAssistCatalog pc : dest) {
			assemblyResourceCount(pc);
		}
	}
}
