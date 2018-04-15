package com.lanking.uxb.rescon.question.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeReviewService;
import com.lanking.uxb.rescon.basedata.api.ResconQuestionKnowledgeSyncService;
import com.lanking.uxb.rescon.question.api.ResconAnswerManage;
import com.lanking.uxb.rescon.question.api.ResconQuestionRebuildService;
import com.lanking.uxb.service.index.value.QuestionIndexDoc;
import com.lanking.uxb.service.search.api.Page;

@Service
@Transactional(readOnly = true)
public class ResconQuestionRebuildImpl implements ResconQuestionRebuildService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;

	@Autowired
	@Qualifier("AnswerRepo")
	Repo<Answer, Long> answerRepo;

	@Autowired
	private ResconAnswerManage answerService;

	@Autowired
	private SearchService searchService;

	@Autowired
	private ResconQuestionKnowledgeSyncService questionKnowledgeSyncService;
	@Autowired
	private ResconQuestionKnowledgeReviewService questionKnowledgeReviewService;

	private String[] errTexts = { "，", "、", "＋", "﹣", "−", "×", "÷", "　", "≥", "≤", "^′", "π", "＝", "〈", "＜", "〉", "＞",
			"（", "）", "｛", "｝", "α", "β", "γ", "λ", "φ", "ω", "θ", "μ", "Δ", "∵", "∴", "≠", "∩", "∪", "∁", "∠", "⊥",
			"△", "∞", "∘C", "≤", "≥", "≈", "≠", "∽", "≌", "∈", "∉", "⊆", "⊇", "⊈", "⊉", "⊂", "⊃", "Ω", "∀", "∃", "∨",
			"∧", "^\\circ\\hspace{-0.09em}\\mathrm{C}", "…", "...", "′" };
	private String[] replaceTexts = { ",", ",", "+", "-", "-", "\\times", "\\div", " ", "\\ge ", "\\le ", "'", "\\pi ",
			"=", "<", "<", ">", ">", "(", ")", "{", "}", "\\alpha ", "\\beta ", "\\gamma ", "\\lambda ", "\\varphi ",
			"\\omega ", "\\theta ", "\\mu ", "\\Delta ", "\\because ", "\\therefore ", "\\neq ", "\\cap ", "\\cup ",
			"\\complement ", "\\angle ", "\\perp ", "\\triangle ", "\\infty ", "^\\circ\\mathrm{C} ", "\\le ", "\\ge ",
			"\\approx ", "\\neq ", "\\backsim ", "\\cong ", "\\in ", "\\notin ", "\\subseteq ", "\\supseteq ",
			"\nsubseteq ", "\\nsupseteq ", "\\subset ", "\\supset ", "\\Omega ", "\\forall ", "\\exists ", "\\vee ",
			"\\wedge ", "^\\circ\\mathrm{C} ", " \\cdots ", " \\cdots ", "'" };

	@Override
	@Transactional
	public void handleKatexInputQuestions(Collection<Long> questionIds) {
		Map<Long, Question> questionMap = questionRepo.mget(questionIds);
		Map<Long, List<Answer>> questionAnswerMap = answerService.getQuestionAnswers(questionIds);

		Pattern tagPattern = Pattern.compile("<ux-mth>([\\s\\S]+?)</ux-mth>");
		Set<Question> saveQuestions = new HashSet<Question>();
		Set<Answer> saveAnswers = new HashSet<Answer>();
		for (Long questionId : questionIds) {
			Question question = questionMap.get(questionId);
			if (question == null) {
				continue;
			}
			Map<String, Boolean> replaceFlag = new HashMap<String, Boolean>(1);
			replaceFlag.put("replace", false);
			List<Answer> answers = questionAnswerMap.get(questionId);
			question.setContent(this.katexReplace(question.getContent(), tagPattern, replaceFlag));
			if (question.getType() == Type.SINGLE_CHOICE || question.getType() == Type.MULTIPLE_CHOICE) {
				question.setChoiceA(this.katexReplace(question.getChoiceA(), tagPattern, replaceFlag));
				question.setChoiceB(this.katexReplace(question.getChoiceB(), tagPattern, replaceFlag));
				question.setChoiceC(this.katexReplace(question.getChoiceC(), tagPattern, replaceFlag));
				question.setChoiceD(this.katexReplace(question.getChoiceD(), tagPattern, replaceFlag));
				question.setChoiceE(this.katexReplace(question.getChoiceE(), tagPattern, replaceFlag));
				question.setChoiceF(this.katexReplace(question.getChoiceF(), tagPattern, replaceFlag));
			}
			question.setAnalysis(this.katexReplace(question.getAnalysis(), tagPattern, replaceFlag));
			question.setHint(this.katexReplace(question.getHint(), tagPattern, replaceFlag));
			if (replaceFlag.get("replace")) {
				saveQuestions.add(question);
			}
			replaceFlag.put("replace", false);
			if (question.getType() == Type.FILL_BLANK || question.getType() == Type.QUESTION_ANSWERING) {
				for (Answer answer : answers) {
					answer.setContent(this.katexReplace(answer.getContent(), tagPattern, replaceFlag));
					answer.setContentLatex(this.katexReplace(answer.getContentLatex(), tagPattern, replaceFlag));
					if (replaceFlag.get("replace")) {
						saveAnswers.add(answer);
					}
				}
			}
		}

		if (saveQuestions.size() > 0) {
			questionRepo.save(saveQuestions);
		}
		if (saveAnswers.size() > 0) {
			answerRepo.save(saveAnswers);
		}
	}

	/**
	 * 检查公式是否规范.
	 * 
	 * @param content
	 * @return
	 */
	public String katexReplace(String content, Pattern tagPattern, Map<String, Boolean> replaceFlag) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		Matcher matcher = tagPattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		int lastAppendPosition = 0; // 上次添加位置
		while (matcher.find()) {
			String temp = content.substring(lastAppendPosition, matcher.start());
			if (StringUtils.isNotBlank(temp)) {
				sb.append(temp);
			}
			String group0temp = matcher.group(0);
			for (int i = 0; i < this.errTexts.length; i++) {
				if (matcher.group(0).indexOf(this.errTexts[i]) != -1) {
					replaceFlag.put("replace", true);
					group0temp = group0temp.replace(this.errTexts[i], this.replaceTexts[i]);
				}
			}
			sb.append(group0temp);
			lastAppendPosition = matcher.end();
		}
		if (lastAppendPosition == 0) {
			sb.append(content);
		} else if (lastAppendPosition < content.length()) {
			sb.append(content.substring(lastAppendPosition, content.length()));
		}
		return sb.toString();
	}

	@Override
	@Transactional
	public List<Long> handleV3SameQuestions(Collection<Document> questionIndexDocs) {
		List<Long> qids = new ArrayList<Long>();
		for (Document doc : questionIndexDocs) {
			QuestionIndexDoc qdoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
			List<Long> knowledgeReviewCodes = qdoc.getKnowledgeReviewCodes();
			List<Long> knowledgeSyncCodes = qdoc.getKnowledgeSyncCodes();

			if (qdoc.getSameShow() != null && qdoc.getSameShow()) {
				// 展示题
				List<Long> sameQuestionIds = this.querySameQuestionIds(qdoc.getResourceId());
				this.saveV3Knowledges(sameQuestionIds, knowledgeReviewCodes, knowledgeSyncCodes);

				qids.addAll(sameQuestionIds);
			} else if (qdoc.getSameShowId() != null && qdoc.getSameShowId() > 0
					&& !hasV4Knowledge(qdoc.getSameShowId())) {
				// 重复题
				this.saveV3Knowledges(Lists.newArrayList(qdoc.getSameShowId()), knowledgeReviewCodes,
						knowledgeSyncCodes);
				qids.add(qdoc.getSameShowId());
			}
		}
		return qids;
	}

	/**
	 * 根据展示题获取重复题ID集合.
	 * 
	 * @param sameShowQuestionId
	 *            展示题ID
	 * @return
	 */
	private List<Long> querySameQuestionIds(long sameShowQuestionId) {
		List<Long> sameQuestionIds = new ArrayList<Long>();

		BoolQueryBuilder qb = null;
		try {
			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("sameShowId", sameShowQuestionId));

			// 不含v3知识点的
			BoolQueryBuilder qbin = QueryBuilders.boolQuery();
			qbin.should(QueryBuilders.existsQuery("knowledgeSyncCodes"));
			qbin.should(QueryBuilders.existsQuery("knowledgeReviewCodes"));
			qb.mustNot(qbin);

			Page docPage = searchService.search(IndexType.QUESTION, 0, 999, qb, null,
					new Order("resourceId", Direction.ASC));

			for (Document doc : docPage.getDocuments()) {
				long questionId = Long.parseLong(doc.getId());
				sameQuestionIds.add(questionId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return sameQuestionIds;
	}

	/**
	 * 是否包含V3知识点.
	 * 
	 * @param questionId
	 * @return
	 */
	private boolean hasV4Knowledge(long questionId) {
		BoolQueryBuilder qb = null;
		try {
			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("resourceId", questionId));
			Page docPage = searchService.search(IndexType.QUESTION, 0, 1, qb, null);
			for (Document doc : docPage.getDocuments()) {
				QuestionIndexDoc qdoc = JSON.parseObject(doc.getValue(), QuestionIndexDoc.class);
				if (CollectionUtils.isNotEmpty(qdoc.getKnowledgeReviewCodes())
						|| CollectionUtils.isNotEmpty(qdoc.getKnowledgeSyncCodes())) {
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * 存储V3知识点.
	 * 
	 * @param questionIds
	 * @param knowledgeReviewCodes
	 * @param knowledgeSyncCodes
	 */
	@Transactional
	private void saveV3Knowledges(Collection<Long> questionIds, List<Long> knowledgeReviewCodes,
			List<Long> knowledgeSyncCodes) {
		if (CollectionUtils.isNotEmpty(knowledgeReviewCodes)) {
			for (int i = knowledgeReviewCodes.size() - 1; i >= 0; i--) {
				if (knowledgeReviewCodes.get(i).toString().length() != 10) {
					knowledgeReviewCodes.remove(i);
				}
			}
		}
		if (CollectionUtils.isNotEmpty(knowledgeSyncCodes)) {
			for (int i = knowledgeSyncCodes.size() - 1; i >= 0; i--) {
				if (knowledgeSyncCodes.get(i).toString().length() != 9) {
					knowledgeSyncCodes.remove(i);
				}
			}
		}
		for (long questionId : questionIds) {
			if (CollectionUtils.isNotEmpty(knowledgeReviewCodes)) {
				questionKnowledgeReviewService.saveQuestionKnowledgeReview(questionId, knowledgeReviewCodes);
			}
			if (CollectionUtils.isNotEmpty(knowledgeSyncCodes)) {
				questionKnowledgeSyncService.saveQuestionKnowledgeSync(questionId, knowledgeSyncCodes);
			}
		}
	}

	public static void main(String args[]) {
	}
}
