package com.lanking.uxb.rescon.teach.value;

import java.util.List;

/**
 * @see com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementLesson
 * @author xinyu.zhou
 * @since 2.2.0
 */
public class VTeachAssistElementLesson extends VTeachAssistElement {
	private static final long serialVersionUID = 7007852714431321160L;

	private List<VTeachAssistElementLessonPoint> points;

	public List<VTeachAssistElementLessonPoint> getPoints() {
		return points;
	}

	public void setPoints(List<VTeachAssistElementLessonPoint> points) {
		this.points = points;
	}
}
