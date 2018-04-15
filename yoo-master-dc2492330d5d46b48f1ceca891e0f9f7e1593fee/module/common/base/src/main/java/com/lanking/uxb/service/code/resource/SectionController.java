package com.lanking.uxb.service.code.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;

/**
 * 章节相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
@RestController
@RequestMapping("common/section")
public class SectionController {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;

	/**
	 * 根据教材代码获取章节数据(树形|平铺)
	 * 
	 * @since 2.1
	 * @param textbookCode
	 *            教材代码
	 * @param tree
	 *            是否为树形结构数据,默认为true
	 * @return {@link Value}
	 */

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(defaultValue = "0") int textbookCode,
			@RequestParam(defaultValue = "true") boolean tree) {
		List<Section> sections = sectionService.findByTextbookCode(textbookCode);
		if (tree) {
			return new Value(sectionConvert.assemblySectionTree(sectionConvert.to(sections)));
		}
		return new Value(sectionConvert.to(sections));
	}

	/**
	 * 根据课程代码获得章节信息
	 *
	 * @param code
	 *            code
	 * @return 教材对象
	 */

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "get_section_by_code", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSectionByCode(@RequestParam(value = "code") Long code) {
		Section section = sectionService.get(code);
		return new Value(sectionConvert.to(section));
	}

	/**
	 * 查找章节下所有子节点
	 *
	 * @param code
	 *            code
	 * @return
	 */

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "findSectionChildren", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findSectionChildren(@RequestParam(value = "code") Long code) {
		List<Long> ids = sectionService.findSectionChildren(code);
		return new Value(ids);
	}
}
