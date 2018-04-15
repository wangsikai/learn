package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFalliblePoint;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFreeEdit;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementKnowledgeSpec;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLearnGoal;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLesson;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPeriodChildTitle;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPeriodTitle;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointMap;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPointStructure;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPractice;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeComment;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareComment;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareGoal;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementProblemSolving;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementReview;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopic;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 教辅模块Convert
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementConvert extends Converter<VTeachAssistElement, AbstractTeachAssistElement, Long> {
	@Autowired
	private ResconTeachAssistElementPointMapConvert pointMapConvert;
	@Autowired
	private ResconTeachAssistElementPracticeCommentConvert practiceCommentConvert;
	@Autowired
	private ResconTeachAssistElementPracticeConvert practiceConvert;
	@Autowired
	private ResconTeachAssistElementPrepareCommentConvert prepareCommentConvert;
	@Autowired
	private ResconTeachAssistElementPrepareGoalConvert prepareGoalConvert;
	@Autowired
	private ResconTeachAssistElementProblemSolvingConvert problemSolvingConvert;
	@Autowired
	private ResconTeachAssistElementReviewConvert reviewConvert;
	@Autowired
	private ResconTeachAssistElementTopicConvert topicConvert;
	@Autowired
	private ResconTeachAssistElementFalliblePointConvert falliblePointConvert;
	@Autowired
	private ResconTeachAssistElementFreeEditConvert freeEditConvert;
	@Autowired
	private ResconTeachAssistElementLessonConvert lessonConvert;
	@Autowired
	private ResconTeachAssistElementPeriodChildTitleConvert periodChildTitleConvert;
	@Autowired
	private ResconTeachAssistElementPeriodTitleConvert periodTitleConvert;
	@Autowired
	private ResconTeachAssistElementKnowledgeSpecConvert knowledgeSpecConvert;
	@Autowired
	private ResconTeachAssistElementLearnGoalConvert learnGoalConvert;
	@Autowired
	private ResconTeachAssistElementPointStructureConvert pointStructureConvert;

	@Override
	protected Long getId(AbstractTeachAssistElement abstractTeachAssistElement) {
		return abstractTeachAssistElement.getId();
	}

	@Override
	protected VTeachAssistElement convert(AbstractTeachAssistElement abstractTeachAssistElement) {

		switch (abstractTeachAssistElement.getType()) {
		case POINT_MAP:
			return pointMapConvert.to((TeachAssistElementPointMap) abstractTeachAssistElement);
		case FALLIBLE_POINT:
			return falliblePointConvert.to((TeachAssistElementFalliblePoint) abstractTeachAssistElement);
		case FREE_EDIT:
			return freeEditConvert.to((TeachAssistElementFreeEdit) abstractTeachAssistElement);
		case KNOWLEDGE_SPEC:
			return knowledgeSpecConvert.to((TeachAssistElementKnowledgeSpec) abstractTeachAssistElement);
		case LEARN_GOAL:
			return learnGoalConvert.to((TeachAssistElementLearnGoal) abstractTeachAssistElement);
		case LESSON_TEACH:
			return lessonConvert.to((TeachAssistElementLesson) abstractTeachAssistElement);
		case PERIOD_CHILD_TITLE:
			return periodChildTitleConvert.to((TeachAssistElementPeriodChildTitle) abstractTeachAssistElement);
		case PERIOD_TITLE:
			return periodTitleConvert.to((TeachAssistElementPeriodTitle) abstractTeachAssistElement);
		case POINT_STRUCTURE:
			return pointStructureConvert.to((TeachAssistElementPointStructure) abstractTeachAssistElement);
		case PRACTICE:
			return practiceConvert.to((TeachAssistElementPractice) abstractTeachAssistElement);
		case PRACTICE_COMMENT:
			return practiceCommentConvert.to((TeachAssistElementPracticeComment) abstractTeachAssistElement);
		case PREPARE_COMMENT:
			return prepareCommentConvert.to((TeachAssistElementPrepareComment) abstractTeachAssistElement);
		case PREPARE_GOAL:
			return prepareGoalConvert.to((TeachAssistElementPrepareGoal) abstractTeachAssistElement);
		case PROBLEM_SOLVING:
			return problemSolvingConvert.to((TeachAssistElementProblemSolving) abstractTeachAssistElement);
		case REVIEW:
			return reviewConvert.to((TeachAssistElementReview) abstractTeachAssistElement);
		case TOPIC:
			return topicConvert.to((TeachAssistElementTopic) abstractTeachAssistElement);
		default:
			return null;
		}
	}
}
