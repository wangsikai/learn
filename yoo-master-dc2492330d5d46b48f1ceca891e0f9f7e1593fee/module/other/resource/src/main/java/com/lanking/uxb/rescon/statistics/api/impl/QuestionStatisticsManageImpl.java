package com.lanking.uxb.rescon.statistics.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.stat.KnowledgeQuestionStat;
import com.lanking.cloud.domain.common.resource.stat.SectionQuestionStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.statistics.api.QuestionStatisticsManage;
import com.lanking.uxb.rescon.statistics.convert.ResconStatisKnowpointConvert;
import com.lanking.uxb.rescon.statistics.convert.ResconStatisNewKnowpointConvert;
import com.lanking.uxb.rescon.statistics.form.QuestionStatisForm;
import com.lanking.uxb.rescon.statistics.value.VQuestionStatis;
import com.lanking.uxb.rescon.statistics.value.VStatisKnowpoint;
import com.lanking.uxb.service.code.convert.KnowledgeSystemConvert;
import com.lanking.uxb.service.code.convert.KnowpointConvert;

@Transactional(readOnly = true)
@Service
public class QuestionStatisticsManageImpl implements QuestionStatisticsManage {

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("KnowledgeQuestionStatRepo")
	Repo<KnowledgeQuestionStat, Long> knowledgeQuestionRepo;

	@Autowired
	@Qualifier("KnowledgeSystemRepo")
	Repo<KnowledgeSystem, Long> knowledgeSystemRepo;

	@Autowired
	@Qualifier("SectionRepo")
	Repo<Section, Long> sectionRepo;

	@Autowired
	@Qualifier("KnowpointRepo")
	Repo<Knowpoint, Integer> knowpointRepo;

	@Autowired
	@Qualifier("SectionQuestionStatRepo")
	Repo<SectionQuestionStat, Long> sectionQuestionStatRepo;

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconStatisKnowpointConvert zlkConvert;
	@Autowired
	private ResconStatisNewKnowpointConvert newZlkConvert;
	@Autowired
	private KnowpointConvert knowPointConvert;
	@Autowired
	private KnowledgeSystemConvert knowledgeSystemConvert;

	@Override
	public List<Map> getQuestionBySections(List<Long> list, int version) {
		return sectionQuestionStatRepo.find("$getQuestionBySections",
				Params.param("version", version).put("sectionCodes", list)).list(Map.class);
	}

	@Override
	public List<VQuestionStatis> getSectionStatis(List<Section> sectionList, List<Map> questionList) {
		List<VQuestionStatis> qStatisList = new ArrayList<VQuestionStatis>();
		// 版本号长度
		int textBookLength = sectionList.get(0).getTextbookCode().toString().length();
		Map<Long, Map> statisMap = new HashMap<Long, Map>();
		for (Map map : questionList) {
			statisMap.put(Long.parseLong(String.valueOf(map.get("code"))), map);
		}
		for (Section sec : sectionList) {
			if (statisMap.get(sec.getCode()) == null) {
				Map<String, Object> newMap = new HashMap<String, Object>();
				newMap.put("name", sec.getName());
				newMap.put("editing", 0L);
				newMap.put("nopass", 0L);
				newMap.put("pass", 0L);
				newMap.put("total", 0L);
				newMap.put("onepass", 0L);
				newMap.put("pcode", sec.getPcode());
				newMap.put("level", (long) sec.getLevel());
				newMap.put("code", sec.getCode());
				statisMap.put(sec.getCode(), newMap);
			}
		}
		for (Long code : statisMap.keySet()) {
			int sectionCodeLength = code.toString().length();
			if (sectionCodeLength > textBookLength) {
				for (int i = textBookLength; i < sectionCodeLength; i = i + 2) {
					Map<String, Object> newMap = new HashMap<String, Object>();
					Long newCode = Long.parseLong(code.toString().substring(0, i));
					if (statisMap.get(newCode) != null) {
						newMap.put("name", statisMap.get(newCode).get("name"));
						newMap.put("code", newCode);
						newMap.put("level", (long) (newCode.toString().length() - textBookLength) / 2);
						newMap.put(
								"pcode",
								newCode.toString().length() == textBookLength + 2 ? 0 : Long.parseLong(code.toString()
										.substring(0, i - 2)));
						newMap.put(
								"editing",
								Long.parseLong(String.valueOf(statisMap.get(code).get("editing")))
										+ Long.parseLong(String.valueOf(statisMap.get(newCode).get("editing"))));
						newMap.put(
								"nopass",
								Long.parseLong(String.valueOf(statisMap.get(code).get("nopass")))
										+ Long.parseLong(String.valueOf(statisMap.get(newCode).get("nopass"))));
						newMap.put(
								"pass",
								Long.parseLong(String.valueOf(statisMap.get(code).get("pass")))
										+ Long.parseLong(String.valueOf(statisMap.get(newCode).get("pass"))));
						newMap.put(
								"onepass",
								Long.parseLong(String.valueOf(statisMap.get(code).get("onepass")))
										+ Long.parseLong(String.valueOf(statisMap.get(newCode).get("onepass"))));
						newMap.put(
								"total",
								Long.parseLong(String.valueOf(statisMap.get(code).get("total")))
										+ Long.parseLong(String.valueOf(statisMap.get(newCode).get("total"))));
						statisMap.put(newCode, newMap);
					}
				}
			}
		}
		Map<Long, Map> newStatisMap = new LinkedHashMap<Long, Map>();
		for (Section sec : sectionList) {
			newStatisMap.put(sec.getCode(), statisMap.get(sec.getCode()));
		}
		for (Long code : newStatisMap.keySet()) {
			VQuestionStatis vqs = new VQuestionStatis();
			vqs.setCode(code);
			vqs.setName(statisMap.get(code).get("name").toString());
			vqs.setLevel(Integer.parseInt(statisMap.get(code).get("level").toString()));
			vqs.setPcode(Long.parseLong(statisMap.get(code).get("pcode").toString()));
			vqs.setEditingNum(Long.parseLong(statisMap.get(code).get("editing").toString()));
			vqs.setPassNum(Long.parseLong(statisMap.get(code).get("pass").toString()));
			vqs.setNoPassNum(Long.parseLong(statisMap.get(code).get("nopass").toString()));
			vqs.setOnepassNum(Long.parseLong(statisMap.get(code).get("onepass").toString()));
			vqs.setTotal(Long.parseLong(statisMap.get(code).get("total").toString()));
			vqs.getAllChild().add(vqs.getCode());
			qStatisList.add(vqs);
		}
		return assemblySectionTree(qStatisList);
	}

	private void internalAssemblySectionTree(List<VQuestionStatis> dest, VQuestionStatis v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VQuestionStatis pc : dest) {
				if (pc.getCode() == v.getPcode().longValue()) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblySectionTree(pc.getChildren(), v);
				}
			}
		}
	}

	public List<VQuestionStatis> assemblySectionTree(List<VQuestionStatis> src) {
		List<VQuestionStatis> dest = Lists.newArrayList();
		for (VQuestionStatis v : src) {
			internalAssemblySectionTree(dest, v);
		}
		if (CollectionUtils.isNotEmpty(src) && CollectionUtils.isEmpty(dest)) {
			for (VQuestionStatis v : src) {
				dest.add(v);
			}
		}
		return dest;
	}

	@Override
	public Map<String, Map<Long, Integer>> getKnowPoint(QuestionStatisForm form) {
		Params params = Params.param();
		params.put("subject_code", form.getSubjectCode());
		params.put("version", form.getVersion());
		List<KnowledgeQuestionStat> kqsList = knowledgeQuestionRepo.find("$findAll", params).list();
		Map<String, Map<Long, Integer>> map = Maps.newHashMap();
		Map<Long, Integer> totalMap = Maps.newHashMap();
		Map<Long, Integer> passMap = Maps.newHashMap();
		Map<Long, Integer> nopassMap = Maps.newHashMap();
		Map<Long, Integer> editingMap = Maps.newHashMap();
		Map<Long, Integer> onepassMap = Maps.newHashMap();
		if (CollectionUtils.isNotEmpty(kqsList)) {
			for (KnowledgeQuestionStat kqs : kqsList) {
				totalMap.put(kqs.getKnowpointCode(), kqs.getInputCount());
				passMap.put(kqs.getKnowpointCode(), kqs.getPassCount());
				nopassMap.put(kqs.getKnowpointCode(), kqs.getNopassCount());
				editingMap.put(kqs.getKnowpointCode(), kqs.getNocheckCount());
				onepassMap.put(kqs.getKnowpointCode(), kqs.getOnepassCount());
			}
			map.put("total", totalMap);
			map.put("pass", passMap);
			map.put("nopass", nopassMap);
			map.put("editing", editingMap);
			map.put("onepass", onepassMap);
		}
		return map;
	}

	@Override
	public List<Section> findByTextbookCodeAndName(int textBookCode, String name) {
		Params params = Params.param();
		params.put("textBookCode", textBookCode);
		if (name != null) {
			params.put("name", "%" + name + "%");
		}
		return sectionRepo.find("$findByTextbookCodeAndName", params).list();
	}

	@Override
	public List<Knowpoint> listBySubjectAndKey(QuestionStatisForm form) {
		Params params = Params.param();
		params.put("subjectCode", form.getSubjectCode());
		if (form.getKey() != null) {
			params.put("name", "%" + form.getKey() + "%");
		}
		return knowpointRepo.find("$listBySubjectAndKey", params).list();
	}

	@Override
	public List<KnowledgeSystem> newlistBySubjectAndKey(QuestionStatisForm form) {
		Params params = Params.param();
		params.put("subjectCode", form.getSubjectCode());
		if (form.getKey() != null) {
			params.put("name", "%" + form.getKey() + "%");
		}
		return knowledgeSystemRepo.find("$listBySubjectAndKey", params).list();
	}

	@Override
	public List<VQuestionStatis> querySectionStatis(QuestionStatisForm form) {
		List<Section> sections = findByTextbookCodeAndName(form.getTextbookCode(), form.getKey());
		List<Long> sectionList = new ArrayList<Long>();
		for (Section sec : sections) {
			sectionList.add(sec.getCode());
		}
		if (sectionList.size() == 0) {
			return new ArrayList<VQuestionStatis>();
		}
		List<Map> questionList = getQuestionBySections(sectionList, form.getVersion());
		List<VQuestionStatis> qs = getSectionStatis(sections, questionList);
		return qs;
	}

	@Override
	public HSSFWorkbook exportBySection(List<VQuestionStatis> oldList) {
		List<VQuestionStatis> list = new ArrayList<VQuestionStatis>();
		for (VQuestionStatis v : oldList) {
			list.add(v);
			if (!v.getChildren().isEmpty()) {
				for (int i = 0; i < v.getChildren().size(); i++) {
					list.add(v.getChildren().get(i));
					if (!v.getChildren().get(i).getChildren().isEmpty()) {
						for (int j = 0; j < v.getChildren().get(i).getChildren().size(); j++) {
							list.add(v.getChildren().get(i).getChildren().get(j));
						}
					}
				}
			}
		}
		String[] excelHeader = { "编号", "章节目录", "已录入", "已通过", "一校通过", "未校验", "不通过" };
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Campaign");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		for (int i = 0; i < excelHeader.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			VQuestionStatis vQuestionStatis = list.get(i);
			row.createCell(0).setCellValue(vQuestionStatis.getCode().toString());
			row.createCell(1).setCellValue(vQuestionStatis.getName());
			row.createCell(2).setCellValue(vQuestionStatis.getTotal());
			row.createCell(3).setCellValue(vQuestionStatis.getPassNum());
			row.createCell(4).setCellValue(vQuestionStatis.getOnepassNum());
			row.createCell(5).setCellValue(vQuestionStatis.getEditingNum());
			row.createCell(6).setCellValue(vQuestionStatis.getNoPassNum());
		}
		return wb;
	}

	@Override
	public HSSFWorkbook exportByKnowpoint(List<VStatisKnowpoint> oldList) {
		List<VStatisKnowpoint> list = new ArrayList<VStatisKnowpoint>();
		for (VStatisKnowpoint v : oldList) {
			list.add(v);
			if (!v.getChildren().isEmpty()) {
				for (int i = 0; i < v.getChildren().size(); i++) {
					list.add(v.getChildren().get(i));
					if (!v.getChildren().get(i).getChildren().isEmpty()) {
						for (int j = 0; j < v.getChildren().get(i).getChildren().size(); j++) {
							list.add(v.getChildren().get(i).getChildren().get(j));
							if (!v.getChildren().get(i).getChildren().get(j).getChildren().isEmpty()) {
								for (int k = 0; k < v.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
									list.add(v.getChildren().get(i).getChildren().get(j).getChildren().get(k));
								}
							}
						}
					}
				}
			}
		}
		String[] excelHeader = { "编号", "知识点目录", "已录入", "已通过", "一校通过", "未校验", "不通过" };
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Campaign");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		for (int i = 0; i < excelHeader.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			VStatisKnowpoint vStatisKnowpoint = list.get(i);
			row.createCell(0).setCellValue(vStatisKnowpoint.getCode().toString());
			row.createCell(1).setCellValue(vStatisKnowpoint.getName());
			row.createCell(2).setCellValue(vStatisKnowpoint.getTotal());
			row.createCell(3).setCellValue(vStatisKnowpoint.getPassNum());
			row.createCell(4).setCellValue(vStatisKnowpoint.getOnePassNum());
			row.createCell(5).setCellValue(vStatisKnowpoint.getEditingNum());
			row.createCell(6).setCellValue(vStatisKnowpoint.getNoPassNum());
		}
		return wb;
	}

	@Override
	public List<VStatisKnowpoint> queryKnowpointStatis(QuestionStatisForm form) {
		List<VStatisKnowpoint> vlkList = null;
		if (form.getVersion() == 1) {
			List<Knowpoint> kpList = listBySubjectAndKey(form);
			vlkList = zlkConvert.to(knowPointConvert.to(kpList));
		}
		if (form.getVersion() == 2) {
			List<KnowledgeSystem> kpList = newlistBySubjectAndKey(form);
			vlkList = newZlkConvert.to(knowledgeSystemConvert.to(kpList));
		}
		Map<String, Map<Long, Integer>> kpMap = getKnowPoint(form);
		Map<Long, Integer> totalMap = Maps.newHashMap();
		Map<Long, Integer> passMap = Maps.newHashMap();
		Map<Long, Integer> nopassMap = Maps.newHashMap();
		Map<Long, Integer> editingMap = Maps.newHashMap();
		Map<Long, Integer> onepassMap = Maps.newHashMap();
		if (kpMap.size() > 0) {
			for (VStatisKnowpoint v : vlkList) {
				if (kpMap.get("total").containsKey(v.getCode().longValue())) {
					totalMap = kpMap.get("total");
					v.setTotal(totalMap.get(v.getCode().longValue()).longValue());
				}
				if (kpMap.get("pass").containsKey(v.getCode().longValue())) {
					passMap = kpMap.get("pass");
					v.setPassNum(passMap.get(v.getCode().longValue()).longValue());
				}
				if (kpMap.get("nopass").containsKey(v.getCode().longValue())) {
					nopassMap = kpMap.get("nopass");
					v.setNoPassNum(nopassMap.get(v.getCode().longValue()).longValue());
				}
				if (kpMap.get("editing").containsKey(v.getCode().longValue())) {
					editingMap = kpMap.get("editing");
					v.setEditingNum(editingMap.get(v.getCode().longValue()).longValue());
				}
				if (kpMap.get("onepass").containsKey(v.getCode().longValue())) {
					onepassMap = kpMap.get("onepass");
					v.setOnePassNum(onepassMap.get(v.getCode().longValue()).longValue());
				}
			}
		}
		if (form.getVersion() == 1) {
			return zlkConvert.assemblySectionTree(vlkList);
		}
		if (form.getVersion() == 2) {
			return newZlkConvert.assemblySectionTree(vlkList);
		}
		return null;
	}

}
