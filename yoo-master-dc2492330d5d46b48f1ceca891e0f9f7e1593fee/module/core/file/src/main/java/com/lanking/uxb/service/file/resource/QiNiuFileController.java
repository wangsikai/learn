package com.lanking.uxb.service.file.resource;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.file.api.QiNiuFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 七牛相关接口
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月22日
 */
@RestController
@RequestMapping({ "f/qn", "fs/qn" })
public class QiNiuFileController {
	@Autowired
	private QiNiuFileService qiNiuFileService;

	/**
	 * 获得相应的token以及bucket
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "config", method = { RequestMethod.GET, RequestMethod.POST })
	public Value config(String scene) {

		String token = qiNiuFileService.getUpToken(scene);
		String bucket = qiNiuFileService.getBucket(scene);

		Map<String, String> retMap = new HashMap<String, String>();
		retMap.put("token", token);
		retMap.put("bucket", bucket);

		return new Value(retMap);
	}
}
