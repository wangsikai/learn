package com.lanking.uxb.service.web.util;

import java.util.Comparator;

import com.lanking.uxb.service.resources.value.VHomeworkQuestion;

public class VHomeworkQuestionComparator implements Comparator<VHomeworkQuestion> {

	@Override
	public int compare(VHomeworkQuestion o1, VHomeworkQuestion o2) {
		if (o1.getQuestion().getDifficulty() - o2.getQuestion().getDifficulty() > 0) {
			return -1;
		} else if (o1.getQuestion().getDifficulty() - o2.getQuestion().getDifficulty() < 0) {
			return 1;
		} else {
			return 0;
		}
	}

}
