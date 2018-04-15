package com.lanking.uxb.service.search.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.search.UserKeyWord;
import com.lanking.uxb.service.search.api.KeyWordService;

@Service
@Transactional(readOnly = true)
public class KeyWordServiceImpl implements KeyWordService {

	@Autowired
	@Qualifier("UserKeyWordRepo")
	private Repo<UserKeyWord, Long> userKeyWordRepo;

	@Transactional
	@Override
	public UserKeyWord save(long userId, String type, int phaseCode, int subjectCode, String word) {
		UserKeyWord userKeyWord = new UserKeyWord();
		userKeyWord.setUserId(userId);
		userKeyWord.setPhaseCode(phaseCode);
		userKeyWord.setSubjectCode(subjectCode);
		userKeyWord.setType(type);
		userKeyWord.setWord(word);
		userKeyWord.setCreateAt(new Date());
		return userKeyWordRepo.save(userKeyWord);
	}

}
