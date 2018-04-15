package com.lanking.uxb.zycon.operation.resource;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.operation.api.ZycYoomathVersionLogService;
import com.lanking.uxb.zycon.operation.convert.VersionLogConvert;
import com.lanking.uxb.zycon.operation.form.VersionForm;
import com.lanking.uxb.zycon.operation.value.CVersionLog;

/**
 * U数学 版本更新相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月9日 下午3:03:54
 */
@RestController
@RequestMapping("zyc/versionUpdate")
public class VersionUpdateController {
	@Autowired
	private ZycYoomathVersionLogService versionService;
	@Autowired
	private VersionLogConvert versionLogConvert;

	@RequestMapping("list")
	public Value list(@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
		Page<YoomathVersionLog> versionPage = versionService.query(P.index(page, pageSize));
		VPage<CVersionLog> vpage = new VPage<CVersionLog>();
		vpage.setPageSize(pageSize);
		if (versionPage.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(versionLogConvert.to(versionPage.getItems()));

		}
		vpage.setTotal(versionPage.getTotalCount());
		vpage.setTotalPage(versionPage.getPageCount());
		vpage.setCurrentPage(page);
		return new Value(vpage);
	}

	@RequestMapping("add")
	public Value add(VersionForm form) {
		YoomathVersionLog versionLog = versionService.add(form);
		return new Value(versionLogConvert.to(versionLog));
	}

	@RequestMapping("editStatus")
	public Value changeVersionLog(Status status, Long id) {
		YoomathVersionLog versionLog = versionService.edit(id, status);
		return new Value(versionLogConvert.to(versionLog));
	}
}
