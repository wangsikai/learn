package com.lanking.uxb.rescon.exam.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory.OperateType;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperStatisticService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.exam.api.ResconExamHistoryManage;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.exam.api.ResconExamTopicManage;
import com.lanking.uxb.rescon.exam.value.VExam;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;
import com.lanking.uxb.rescon.exam.value.VOperationUser;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.DistrictConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.ResourceCatgeoryConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;

/**
 * 试卷VO
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月22日 下午8:01:48
 */
@Component
public class ResconExamConvert extends Converter<VExam, ExamPaper, Long> {
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private DistrictConvert districtConvert;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private ResourceCatgeoryConvert rcConvert;
	@Autowired
	private ResconExamPaperTopicConvert epTopicConvert;
	@Autowired
	private ResconExamTopicManage resconExamTopicManage;
	@Autowired
	private ResconExamManage resconExamManage;
	@Autowired
	private ResconExamHistoryManage resconExamHistoryManage;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ResconExamPaperStatisticService statisticService;

	@Override
	protected Long getId(ExamPaper s) {
		return s.getId();
	}

	@Override
	protected VExam convert(ExamPaper s) {
		VExam v = new VExam();
		v.setName(s.getName());
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		if (s.getDifficulty() != null) {
			v.setDifficulty(s.getDifficulty());
		}
		v.setScore(s.getScore());
		v.setStatus(s.getStatus());
		v.setYear(s.getYear() == null ? 0 : s.getYear());
		v.setDistrictCode(s.getDistrictCode());
		v.setSectionCode(s.getSectionCode() == null ? null : Long.valueOf(s.getSectionCode()));
		v.setStatus(s.getStatus());
		return v;
	}

	public VExam to(ExamPaper s, ResconExamOption option) {
		s.setInitQuestions(option.isInitQuestion());
		s.setInitRollBackers(option.isInitRollBacks());
		s.setInitUpdators(option.isInitUpdateors());
		s.setInitQuestionStatusCount(option.isInitQuestionStatusCount());
		return super.to(s);
	}

	public List<VExam> to(List<ExamPaper> ss, ResconExamOption option) {
		for (ExamPaper s : ss) {
			s.setInitQuestions(option.isInitQuestion());
			s.setInitRollBackers(option.isInitRollBacks());
			s.setInitUpdators(option.isInitUpdateors());
			s.setInitQuestionStatusCount(option.isInitQuestionStatusCount());
		}
		return super.to(ss);
	}

	public Map<Long, VExam> to(Map<Long, ExamPaper> sMap, ResconExamOption option) {
		for (ExamPaper s : sMap.values()) {
			s.setInitQuestions(option.isInitQuestion());
			s.setInitRollBackers(option.isInitRollBacks());
			s.setInitUpdators(option.isInitUpdateors());
			s.setInitQuestionStatusCount(option.isInitQuestionStatusCount());
		}
		return super.to(sMap);
	}

	@Override
	protected ExamPaper internalGet(Long id) {
		return resconExamManage.get(id);
	}

	@Override
	protected Map<Long, ExamPaper> internalMGet(Collection<Long> ids) {
		return resconExamManage.mget(Lists.newArrayList(ids));
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 创建者
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, VendorUser>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VendorUser value) {
				if (value != null) {
					d.setCreator(value.getRealName());
				}
			}

			@Override
			public VendorUser getValue(Long key) {
				return vendorUserManage.getVendorUser(key);
			}

			@Override
			public Map<Long, VendorUser> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return vendorUserManage.mgetVendorUser(keys);
			}

		});
		// 地区
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, String>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getDistrictCode();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, String value) {
				d.setDistrict(value == null ? "" : value);
			}

			@Override
			public String getValue(Long key) {
				return key == null ? null : districtService.getDistrictName(key);
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return districtService.mgetDistrictName(Lists.newArrayList(keys));
			}

		});
		// 章节
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, String>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getSectionCode() == null ? null : Long.valueOf(s.getSectionCode());
			}

			@Override
			public void setValue(ExamPaper s, VExam d, String value) {
				d.setSection(value == null ? "" : value);
			}

			@Override
			public String getValue(Long key) {
				return key == null ? null : sectionService.getSectionName(Long.valueOf(key));
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return sectionService.mgetSectionName(Lists.newArrayList(keys));
			}

		});
		// phase
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Integer, VPhase>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VExam d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VPhase value) {
				if (value != null) {
					d.setPhase(value);
				}
			}

			@Override
			public VPhase getValue(Integer key) {
				return phaseConvert.get(key);
			}

			@Override
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return phaseConvert.mget(keys);
			}

		});
		// subject
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Integer, VSubject>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VExam d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VSubject value) {
				if (value != null) {
					d.setSubject(value);
				}
			}

			@Override
			public VSubject getValue(Integer key) {
				return subjectConvert.get(key);
			}

			@Override
			public Map<Integer, VSubject> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return subjectConvert.mget(keys);
			}

		});
		// school
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, VSchool>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VSchool value) {
				d.setSchool(value == null ? null : value);
			}

			@Override
			public VSchool getValue(Long key) {
				return key == null ? null : schoolConvert.get(key);
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return schoolConvert.mget(keys);
			}

		});
		// textbook
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Integer, VTextbook>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VExam d) {
				return s.getTextbookCode();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VTextbook value) {
				d.setTextbook(value);
			}

			@Override
			public VTextbook getValue(Integer key) {
				return key == null ? null : tbConvert.get(key);
			}

			@Override
			public Map<Integer, VTextbook> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return tbConvert.mget(keys);
			}

		});
		// textbookcategory
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VExam d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VTextbookCategory value) {
				d.setTextbookCategory(value);
			}

			@Override
			public VTextbookCategory getValue(Integer key) {
				return key == null ? null : tbcConvert.get(key);
			}

			@Override
			public Map<Integer, VTextbookCategory> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return tbcConvert.mget(keys);
			}

		});

		// 类别 resourcecategory
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Integer, VResourceCategory>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VExam d) {
				return s.getResourceCategoryCode();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VResourceCategory value) {
				if (value != null) {
					d.setCategory(value);
				}
			}

			@Override
			public VResourceCategory getValue(Integer key) {
				return rcConvert.get(key);
			}

			@Override
			public Map<Integer, VResourceCategory> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return rcConvert.mget(keys);
			}

		});
		// 题目
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, List<VExamPaperTopic>>() {
			@Override
			public boolean accept(ExamPaper s) {
				return s.isInitQuestions();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, List<VExamPaperTopic> value) {
				if (value != null) {
					d.setTopics(value);
				}
			}

			@Override
			public List<VExamPaperTopic> getValue(Long key) {
				List<ExamPaperTopic> topicList = resconExamTopicManage.getTopicsByExam(key);
				return epTopicConvert.to(topicList);
			}

			@Override
			public Map<Long, List<VExamPaperTopic>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				Map<Long, List<ExamPaperTopic>> examTopicMap = resconExamTopicManage.mgetTopicsByExam(keys);
				Map<Long, List<VExamPaperTopic>> vtopicMap = Maps.newHashMap();
				for (Long key : keys) {
					vtopicMap.put(key, epTopicConvert.to(examTopicMap.get(key)));
				}
				return vtopicMap;
			}

		});
		// 题目
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, List<VExamPaperTopic>>() {
			@Override
			public boolean accept(ExamPaper s) {
				return s.isInitQuestions();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, List<VExamPaperTopic> value) {
				if (value != null) {
					d.setTopics(value);
				}
			}

			@Override
			public List<VExamPaperTopic> getValue(Long key) {
				List<ExamPaperTopic> topicList = resconExamTopicManage.getTopicsByExam(key);
				return epTopicConvert.to(topicList);
			}

			@Override
			public Map<Long, List<VExamPaperTopic>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				Map<Long, List<ExamPaperTopic>> examTopicMap = resconExamTopicManage.mgetTopicsByExam(keys);
				Map<Long, List<VExamPaperTopic>> vtopicMap = Maps.newHashMap();
				for (Long key : keys) {
					vtopicMap.put(key, epTopicConvert.to(examTopicMap.get(key)));
				}
				return vtopicMap;
			}

		});
		// 发布用户列表
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, List<VOperationUser>>() {
			@Override
			public boolean accept(ExamPaper s) {
				return s.isInitUpdators();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, List<VOperationUser> value) {
				if (value != null) {
					d.setUpdators(value);
				}
			}

			@Override
			public List<VOperationUser> getValue(Long key) {
				// 根据规则 重新编辑最多取11条
				List<ExamPaperHistory> epList = resconExamHistoryManage.getByOperate(key,
						Lists.newArrayList(OperateType.PUBLISH, OperateType.PUBLISH.REEDIT), 11);
				List<Long> userIds = Lists.newArrayList();
				for (ExamPaperHistory ep : epList) {
					userIds.add(ep.getCreateId());
				}
				Map<Long, VendorUser> vuMap = vendorUserManage.mgetVendorUser(userIds);
				List<VOperationUser> voList = Lists.newArrayList();
				for (ExamPaperHistory ep : epList) {
					VOperationUser vu = new VOperationUser();
					vu.setName(vuMap.get(ep.getCreateId()).getRealName());
					vu.setTime(ep.getCreateAt());
					vu.setType(ep.getOperateType());
					voList.add(vu);
				}
				return voList;
			}

			@Override
			public Map<Long, List<VOperationUser>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				Map<Long, List<ExamPaperHistory>> examHistoryMap = resconExamHistoryManage.mgetByOperate(
						Lists.newArrayList(keys), Lists.newArrayList(OperateType.PUBLISH, OperateType.PUBLISH.REEDIT));
				Map<Long, List<VOperationUser>> voMap = Maps.newHashMap();
				for (Long key : keys) {
					List<Long> userIds = Lists.newArrayList();
					for (ExamPaperHistory examPaperHistory : examHistoryMap.get(key)) {
						userIds.add(examPaperHistory.getCreateId());
					}
					Map<Long, VendorUser> vuMap = vendorUserManage.mgetVendorUser(userIds);
					List<VOperationUser> voList = Lists.newArrayList();
					for (ExamPaperHistory ep : examHistoryMap.get(key)) {
						VOperationUser vu = new VOperationUser();
						vu.setName(vuMap.get(ep.getCreateId()).getName());
						vu.setTime(ep.getCreateAt());
						vu.setType(OperateType.PUBLISH);
						voList.add(vu);
					}
					voMap.put(key, voList);
				}
				return voMap;
			}

		});

		// 校本学校
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, VSchool>() {
			@Override
			public boolean accept(ExamPaper s) {
				return s.getOwnSchoolId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExam d) {
				return s.getOwnSchoolId();
			}

			@Override
			public void setValue(ExamPaper s, VExam d, VSchool value) {
				d.setOwnSchool(value);
			}

			@Override
			public VSchool getValue(Long key) {
				return key == null ? null : schoolConvert.get(key);
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return schoolConvert.mget(keys);
			}
		});

		// 统计试卷中各种状态数量
		assemblers.add(new ConverterAssembler<VExam, ExamPaper, Long, Map<CheckStatus, Integer>>() {

			@Override
			public boolean accept(ExamPaper examPaper) {
				return examPaper.isInitQuestionStatusCount();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper examPaper, VExam vExam) {
				return examPaper.getId();
			}

			@Override
			public void setValue(ExamPaper examPaper, VExam vExam, Map<CheckStatus, Integer> value) {
				int total = 0, pass = 0;
				for (Map.Entry<CheckStatus, Integer> entry : value.entrySet()) {
					if (CheckStatus.PASS == entry.getKey()) {
						pass = entry.getValue();
					}

					total += entry.getValue();
				}

				vExam.setQuestionEditCount(total);
				vExam.setQuestionPassCount(pass);
			}

			@Override
			public Map<CheckStatus, Integer> getValue(Long key) {
				return statisticService.countExamQuestionCheckStatusById(key);
			}

			@Override
			public Map<Long, Map<CheckStatus, Integer>> mgetValue(Collection<Long> keys) {
				return statisticService.countExamQuestionCheckStatusByIds(keys);
			}
		});
	}
}
