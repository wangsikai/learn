package com.lanking.uxb.rescon.statistics.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.statistics.value.VStatisKnowpoint;
import com.lanking.uxb.service.code.value.VKnowpoint;

@Component
public class ResconStatisKnowpointConvert extends Converter<VStatisKnowpoint, VKnowpoint, Long> {

	@Override
	protected Long getId(VKnowpoint s) {
		return Long.valueOf(s.getCode());
	}

	@Override
	protected VStatisKnowpoint convert(VKnowpoint s) {
		VStatisKnowpoint v = new VStatisKnowpoint();
		v.setPcode(s.getPcode());
		v.setCode(Long.valueOf(s.getCode()));
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.getAllChild().add(Long.valueOf(s.getCode()));
		return v;
	}

	private void internalAssemblySectionTree(List<VStatisKnowpoint> dest, VStatisKnowpoint v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VStatisKnowpoint pc : dest) {
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
	 * 组装树形结构的知识点数据
	 * 
	 * @param src
	 * @return List<VLevelKnowpoint>
	 */
	public List<VStatisKnowpoint> assemblySectionTree(List<VStatisKnowpoint> src) {
		List<VStatisKnowpoint> dest = Lists.newArrayList();
		for (VStatisKnowpoint v : src) {
			internalAssemblySectionTree(dest, v);
		}
		if (CollectionUtils.isNotEmpty(src) && CollectionUtils.isEmpty(dest)) {
			for (VStatisKnowpoint v : src) {
				dest.add(v);
			}
		}
		assemblyFallibleCount(dest);
		return dest;
	}

	private void assemblyFallibleCount(VStatisKnowpoint v) {
		List<VStatisKnowpoint> children = v.getChildren();
		if (children.size() > 0) {
			long passNum = 0;
			long total = 0;
			long nopassNum = 0;
			long onePassNum = 0;
			long editingNum = 0;
			for (VStatisKnowpoint c : children) {
				v.getAllChild().addAll(c.getAllChild());
				passNum += c.getPassNum();
				total += c.getTotal();
				nopassNum += c.getNoPassNum();
				onePassNum += c.getOnePassNum();
				editingNum += c.getEditingNum();
				assemblyFallibleCount(c);
			}
			v.setPassNum(passNum);
			v.setTotal(total);
			v.setOnePassNum(onePassNum);
			v.setNoPassNum(nopassNum);
			v.setEditingNum(editingNum);
		}
	}

	private void assemblyFallibleCount(List<VStatisKnowpoint> dest) {
		for (VStatisKnowpoint v : dest) {
			assemblyFallibleCount(v);
		}
		for (VStatisKnowpoint v : dest) {
			assemblyFallibleCount(v);
		}
	}

}
