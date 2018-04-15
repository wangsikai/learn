package com.lanking.uxb.service.code.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.convert.KnowpointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;

/**
 * 知识点接口
 *
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "common/knowpoint")
public class KnowpointController {

	@Autowired
	private KnowpointService knowpointService;
	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private KnowpointConvert knowpointConvert;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;

	/**
	 * 根据课程得到知识点
	 *
	 * @param subjectCode
	 *            课程代码
	 * @return 知识点
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "subjectCode") Integer subjectCode) {
		List<Knowpoint> knowpointList = knowpointService.listBySubject(subjectCode);

		return new Value(knowpointConvert.to(knowpointList));
	}

	/**
	 * 根据知识点得到元知识点
	 *
	 * @param knowPointCode
	 *            知识点代码
	 * @return 元知识点列表
	 */
	@RequestMapping(value = "query_meta_know_point", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryMetaKnowpoint(@RequestParam(value = "knowPoint") Integer knowPointCode) {
		List<MetaKnowpoint> metaKnowpoints = metaKnowpointService.listByKnowPoint(knowPointCode);

		return new Value(metaKnowpointConvert.to(metaKnowpoints));
	}
}
