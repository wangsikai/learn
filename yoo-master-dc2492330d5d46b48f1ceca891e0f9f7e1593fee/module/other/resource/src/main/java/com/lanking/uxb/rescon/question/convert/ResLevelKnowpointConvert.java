package com.lanking.uxb.rescon.question.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.question.value.VResLevelKnowpoint;
import com.lanking.uxb.service.code.value.VKnowpoint;

@Component
public class ResLevelKnowpointConvert extends Converter<VResLevelKnowpoint, VKnowpoint, Long> {

	@Override
	protected Long getId(VKnowpoint s) {
		return Long.valueOf(s.getCode());
	}

	@Override
	protected VResLevelKnowpoint convert(VKnowpoint s) {
		VResLevelKnowpoint v = new VResLevelKnowpoint();
		v.setPcode(s.getPcode());
		v.setCode(Long.valueOf(s.getCode()));
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.getAllChild().add(Long.valueOf(s.getCode()));
		return v;
	}

	private void internalAssemblySectionTree(List<VResLevelKnowpoint> dest, VResLevelKnowpoint v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VResLevelKnowpoint pc : dest) {
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
	 * @return List<VResLevelKnowpoint>
	 */
	public List<VResLevelKnowpoint> assemblySectionTree(List<VResLevelKnowpoint> src) {
		List<VResLevelKnowpoint> dest = Lists.newArrayList();
		for (VResLevelKnowpoint v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return dest;
	}

	private void assemblyFallibleCount(VResLevelKnowpoint v) {
		List<VResLevelKnowpoint> children = v.getChildren();
		if (children.size() > 0) {
			for (VResLevelKnowpoint c : children) {
				v.getAllChild().addAll(c.getAllChild());
				assemblyFallibleCount(c);
			}
		}
	}

	private void assemblyFallibleCount(List<VResLevelKnowpoint> dest) {
		for (VResLevelKnowpoint v : dest) {
			assemblyFallibleCount(v);
		}
		for (VResLevelKnowpoint v : dest) {
			assemblyFallibleCount(v);
		}
	}

}
