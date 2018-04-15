package com.lanking.uxb.service.index.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.index.api.impl.QuestionSimilarService;
import com.lanking.uxb.service.index.value.QuestionSimilarBaseIndexDoc;

/**
 * 相似题搜索基础题目索引.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月20日
 */
@Component
@Transactional(readOnly = true)
public class QuestionSimilarBaseIndexConvert extends Converter<QuestionSimilarBaseIndexDoc, Question, Long> {
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private QuestionSimilarService questionSimilarService;

	@Override
	protected Long getId(Question s) {
		return s.getId();
	}

	@Override
	protected QuestionSimilarBaseIndexDoc convert(Question s) {
		if (s != null) {
			QuestionSimilarBaseIndexDoc doc = new QuestionSimilarBaseIndexDoc();
			doc.setQuestionId(s.getId());
			doc.setPhaseCode(s.getPhaseCode());
			doc.setSubjectCode(s.getSubjectCode());
			doc.setStatus(s.getStatus().getValue());
			doc.setType(s.getType().getValue());
			doc.setSchoolId(s.getSchoolId());
			doc.setVendorId(s.getVendorId());

			// 处理content
			String content = s.getContent();
			// content = content.replaceAll("<[^>]*>", ""); // 去除标签
			content = content.replaceAll(
					"<(p|/p|table|/table|tr|/tr|td|/td|th|/th|span|/span|ux-mth|/ux-mth|img|br|/br|div|/div|ux-blank|/ux-blank)+[\\s\\S]*?>",
					"");
			content = content.replace("&nbsp;", " "); // 去除&nbsp;

			doc.setContent(content);
			return doc;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 新知识点
		assemblers.add(new ConverterAssembler<QuestionSimilarBaseIndexDoc, Question, Long, List<KnowledgePoint>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, QuestionSimilarBaseIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, QuestionSimilarBaseIndexDoc d, List<KnowledgePoint> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					List<Long> knowledgePointCodes = Lists.newArrayList();
					for (KnowledgePoint k : value) {
						Long code = k.getCode();
						knowledgePointCodes.add(code);
						knowledgePointCodes.add(code / 100);
						knowledgePointCodes.add(code / 1000);
						knowledgePointCodes.add(code / 100000);
					}
					d.setKnowledgePointCodes(knowledgePointCodes);
				}
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<KnowledgePoint> getValue(Long key) {
				if (key == null) {
					return null;
				}
				List<Long> codes = questionSimilarService.knowledgePointListByQuestion(key);
				if (CollectionUtils.isEmpty(codes)) {
					return Collections.EMPTY_LIST;
				}
				return knowledgePointService.mgetList(codes);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Map<Long, List<KnowledgePoint>> mgetValue(Collection<Long> keys) {
				Map<Long, List<KnowledgePoint>> retMap = new HashMap<Long, List<KnowledgePoint>>(keys.size());
				Map<Long, List<Long>> codeMap = questionSimilarService.knowledgePointMListByQuestions(keys);
				for (Map.Entry<Long, List<Long>> entry : codeMap.entrySet()) {
					if (CollectionUtils.isEmpty(entry.getValue())) {
						retMap.put(entry.getKey(), Collections.EMPTY_LIST);
					} else {
						retMap.put(entry.getKey(), knowledgePointService.mgetList(entry.getValue()));
					}
				}
				return retMap;
			}
		});
	}

}
