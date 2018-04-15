package com.lanking.uxb.zycon.qs.api;

import java.util.Collection;
import java.util.List;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
public interface ZycQuestionSectionService {
	List<Integer> findByQuestionIds(Collection<Long> questionIds);
}
