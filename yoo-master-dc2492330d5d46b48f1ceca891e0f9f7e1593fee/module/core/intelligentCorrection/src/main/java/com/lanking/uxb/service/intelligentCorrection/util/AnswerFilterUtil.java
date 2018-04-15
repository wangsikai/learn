package com.lanking.uxb.service.intelligentCorrection.util;

public class AnswerFilterUtil {

	public static String tagFilter(String answer) {
		return answer.replaceAll("&nbsp;", "").replaceAll("<br>", "").replaceAll("<br/>", "").replaceAll("<p>", "")
				.replaceAll("\\\\text\\{ \\}", "").trim();
	}
}
