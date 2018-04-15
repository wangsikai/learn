package com.lanking.uxb.zycon.base.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.zycon.base.api.ZycTeachVersionService;
import com.lanking.uxb.zycon.base.value.CTeachVersion;

@Transactional(readOnly = true)
@Service
public class ZycTeachVersionServiceImpl implements ZycTeachVersionService {

	@Autowired
	@Qualifier("TextbookCategoryRepo")
	Repo<TextbookCategory, Integer> textbookCategoryRepo;

	@Autowired
	@Qualifier("TextbookRepo")
	Repo<Textbook, Integer> textbookRepo;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	public List<CTeachVersion> getMathTextList(Integer subjectCode, String phaseName, String flag) {
		Params params = Params.param();
		params.put("subjectCode", subjectCode);
		if (flag.equals("math")) {
			params.put("yoomathStatus", Status.ENABLED.getValue());
			if ("high".equals(phaseName)) {
				params.put("highStatus", Status.ENABLED.getValue());
			}
			if ("middle".equals(phaseName)) {
				params.put("middleStatus", Status.ENABLED.getValue());
			}
			if ("primary".equals(phaseName)) {
				params.put("primaryStatus", Status.ENABLED.getValue());
			}
		}
		List<Map> tbcList = textbookCategoryRepo.find("$getMathTextList", params).list(Map.class);
		return convert(tbcList, phaseName);
	}

	public List<CTeachVersion> convert(List<Map> tbcList, String phaseName) {
		List<CTeachVersion> list = new ArrayList<CTeachVersion>();
		for (Map pa : tbcList) {
			CTeachVersion tc = new CTeachVersion();
			tc.setTcode(Integer.parseInt(String.valueOf(pa.get("tcode"))));
			tc.setTname(String.valueOf(pa.get("tname")));
			tc.setTsequence(Integer.parseInt(String.valueOf(pa.get("tsequence"))));
			tc.setVcode(Integer.parseInt(String.valueOf(pa.get("vcode"))));
			tc.setVname(String.valueOf(pa.get("vname")));
			tc.setVsequence(Integer.parseInt(String.valueOf(pa.get("vsequence"))));
			if ("high".equals(phaseName)) {
				tc.setvStatus(Status.findByValue(Integer.parseInt(String.valueOf(pa.get("hstatus")))));
			}
			if ("middle".equals(phaseName)) {
				tc.setvStatus(Status.findByValue(Integer.parseInt(String.valueOf(pa.get("mstatus")))));
			}
			if ("primary".equals(phaseName)) {
				tc.setvStatus(Status.findByValue(Integer.parseInt(String.valueOf(pa.get("pstatus")))));
			}
			tc.settStatus(Status.findByValue(Integer.parseInt(String.valueOf(pa.get("tstatus")))));
			tc.setPhaseName(Integer.parseInt(pa.get("phase_code").toString()) == PhaseService.PHASE_MIDDLE ? "middle"
					: "high");
			list.add(tc);
		}
		return list;
	}

	@Transactional
	@Override
	public void save(List<CTeachVersion> jsonlist, String phaseName) {
		List<Integer> vList = new ArrayList<Integer>();
		for (CTeachVersion tv : jsonlist) {
			if (!vList.contains(tv.getVcode())) {
				TextbookCategory textbookCategory = textbookCategoryRepo.get(tv.getVcode());
				if (tv.getvStatus() == Status.ENABLED) {
					textbookCategory.setYoomathStatus(tv.getvStatus());
				}
				if ("high".equals(phaseName)) {
					textbookCategory.setHighStatus(tv.getvStatus());
				}
				if ("middle".equals(phaseName)) {
					textbookCategory.setMiddleStatus(tv.getvStatus());
				}
				if ("primary".equals(phaseName)) {
					textbookCategory.setPrimaryStatus(tv.getvStatus());
				}
				textbookCategoryRepo.save(textbookCategory);
				vList.add(tv.getVcode());
			}
			Textbook textbook = textbookRepo.get(tv.getTcode());
			textbook.setYoomathStatus(tv.gettStatus());
			textbookRepo.save(textbook);
		}
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event1 = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.TEXTBOOK.name());
		sender.send(event1);
		ClusterEvent<String> event2 = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.TEXTBOOKCATEGORY.name());
		sender.send(event2);
	}
}
