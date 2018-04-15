package com.lanking.uxb.service.web.util;

import java.util.Comparator;

import com.lanking.uxb.service.resources.value.VQuestion;

public class VQuestionComparator implements Comparator<VQuestion> {

	@Override
	public int compare(VQuestion o1, VQuestion o2) {
		if (o1.getDifficulty() - o2.getDifficulty() > 0) {
			return -1;
		} else if (o1.getDifficulty() - o2.getDifficulty() < 0) {
			return 1;
		} else {
			return 0;
		}
	}

}
