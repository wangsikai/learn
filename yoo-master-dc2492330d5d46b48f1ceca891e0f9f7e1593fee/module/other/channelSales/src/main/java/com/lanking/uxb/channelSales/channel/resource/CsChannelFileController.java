package com.lanking.uxb.channelSales.channel.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.channel.ChannelFile;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsChannelFileService;
import com.lanking.uxb.channelSales.channel.convert.CsChannelFileConvert;
import com.lanking.uxb.channelSales.channel.form.ChannelFileForm;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 渠道资料管理.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月3日
 */
@RestController
@RequestMapping("channelSales/cfile")
public class CsChannelFileController {

	@Autowired
	private CsChannelFileService channelFileService;
	@Autowired
	private CsChannelFileConvert channelFileConvert;

	/**
	 * 保存资料.
	 * 
	 * @param form
	 *            表单数据
	 * @return
	 */
	@RequestMapping("save")
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	public Value saveChannelFile(ChannelFileForm form) {
		if (form == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			channelFileService.save(form, Security.getUserId());
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 查询资料.
	 * 
	 * @return
	 */
	@RequestMapping("query")
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	public Value queryChannelFile() {
		List<ChannelFile> files = channelFileService.findAll();
		return new Value(channelFileConvert.to(files));
	}

	/**
	 * 删除资料.
	 * 
	 * @return
	 */
	@RequestMapping("delete")
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	public Value deleteFile(Long fid) {
		try {
			channelFileService.deleteFile(fid);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}
}
