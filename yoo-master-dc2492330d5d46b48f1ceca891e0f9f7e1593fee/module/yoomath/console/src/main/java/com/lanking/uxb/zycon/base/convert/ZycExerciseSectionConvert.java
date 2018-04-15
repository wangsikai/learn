package com.lanking.uxb.zycon.base.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.zycon.base.value.CExerciseSection;
import com.lanking.uxb.zycon.base.value.CTextbookExercise;

@Component
public class ZycExerciseSectionConvert extends Converter<CExerciseSection, VSection, Long> {

	@Override
	protected Long getId(VSection s) {
		return s.getCode();
	}

	public void assemblyExercise(List<CExerciseSection> vs, List<CTextbookExercise> exercises) {
		for (CExerciseSection v : vs) {
			for (CTextbookExercise ve : exercises) {
				if (ve.getSectionCode() == v.getCode()) {
					v.getTextbookExercises().add(ve);
				}
			}
		}
	}

	@Override
	protected CExerciseSection convert(VSection s) {
		CExerciseSection v = new CExerciseSection();
		v.setPcode(s.getPcode());
		v.setCode(s.getCode());
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.setTextbookCode(s.getTextbookCode());
		return v;
	}

	private void internalAssemblySectionTree(List<CExerciseSection> dest, CExerciseSection v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (CExerciseSection pc : dest) {
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
	 * @since 2.1
	 * @param src
	 *            section VO list
	 * @return 树形结构的章节数据
	 */
	public List<CExerciseSection> assemblySectionTree(List<CExerciseSection> src) {
		List<CExerciseSection> dest = Lists.newArrayList();
		for (CExerciseSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		return dest;
	}

}
