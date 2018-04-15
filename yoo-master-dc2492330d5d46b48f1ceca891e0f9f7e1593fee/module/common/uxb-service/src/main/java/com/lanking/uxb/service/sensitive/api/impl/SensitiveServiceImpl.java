package com.lanking.uxb.service.sensitive.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.SensitiveType;
import com.lanking.cloud.domain.common.baseData.SensitiveWord;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.sensitive.api.SensitiveService;
import com.lanking.uxb.service.sensitive.api.TrieTree;
import com.lanking.uxb.service.sensitive.ex.SensitiveException;

@Service
public class SensitiveServiceImpl implements SensitiveService {

	@Autowired
	@Qualifier("SensitiveWordRepo")
	Repo<SensitiveWord, Long> sensRepo;

	private Map<SensitiveType, TrieTree> trieTrees = Maps.newHashMap();

	public void containsForbidden(String text) throws SensitiveException {
		if (StringUtils.isNotBlank(text)) {
			TrieTree tree = trieTrees.get(SensitiveType.FORBIDDEN);
			if (tree.contains(text)) {
				throw new SensitiveException(SensitiveException.CONTAIN_FORBIDDEN_WORD);
			}
		}
	}

	public void containsForbidden(Collection<String> texts) throws SensitiveException {
		if (CollectionUtils.isNotEmpty(texts)) {
			for (String text : texts) {
				this.containsForbidden(text);
			}
		}
	}

	public boolean contains(SensitiveType type, String text) {
		if (StringUtils.isNotBlank(text)) {
			TrieTree tree = trieTrees.get(type);
			return tree.contains(text);
		} else {
			return false;
		}

	}

	@Override
	public boolean containsAudit(String text) {
		if (StringUtils.isNotBlank(text)) {
			TrieTree tree = trieTrees.get(SensitiveType.AUDIT);
			return tree.contains(text);
		} else {
			return false;
		}
	}

	@Override
	public String replace(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		TrieTree tree = trieTrees.get(SensitiveType.REPLACE);
		return tree.replace(text);
	}

	@Override
	public String tagYellow(String text) {
		if (StringUtils.isBlank(text)) {
			return text;
		}
		TrieTree tree = trieTrees.get(SensitiveType.AUDIT);
		return tree.tagYellow(text);
	}

	@Transactional(readOnly = true)
	@Override
	public void reload() {
		trieTrees = Maps.newHashMap();
		trieTrees.put(SensitiveType.FORBIDDEN, new TrieTree());
		trieTrees.put(SensitiveType.REPLACE, new TrieTree());
		trieTrees.put(SensitiveType.AUDIT, new TrieTree());
		List<SensitiveWord> sensitiveWords = sensRepo.find("$getAll").list();
		for (SensitiveWord sensitiveWord : sensitiveWords) {
			TrieTree tree = trieTrees.get(sensitiveWord.getType());
			tree.addWord(sensitiveWord.getWord());
			trieTrees.put(sensitiveWord.getType(), tree);
		}
	}

}
