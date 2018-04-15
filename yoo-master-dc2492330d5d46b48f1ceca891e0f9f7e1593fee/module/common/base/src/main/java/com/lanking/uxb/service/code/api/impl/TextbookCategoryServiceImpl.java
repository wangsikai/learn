package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.service.code.api.TextbookCategoryService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class TextbookCategoryServiceImpl implements TextbookCategoryService {
	@Autowired
	@Qualifier("TextbookCategoryRepo")
	Repo<TextbookCategory, Integer> tbCategoryRepo;

	@Override
	public TextbookCategory get(Integer code) {
		return code == null ? null : tbCategoryRepo.get(code);
	}

	@Override
	public List<TextbookCategory> getAll() {
		return tbCategoryRepo.find("$findAllTextbookCategory").list();
	}

	@Override
	public Map<Integer, TextbookCategory> mget(Collection<Integer> codes) {
		return tbCategoryRepo.mget(codes);
	}

	@Override
	public List<TextbookCategory> mgetList(Collection<Integer> codes) {
		return tbCategoryRepo.mgetList(codes);
	}

	@Override
	public List<TextbookCategory> find(Product product, Integer codePhase) {
		List<TextbookCategory> allList = getAll();
		List<TextbookCategory> list = new ArrayList<TextbookCategory>(allList.size());
		for (TextbookCategory tbc : allList) {
			if (product == Product.YOOMATH && tbc.getYoomathStatus() == Status.ENABLED && tbc.support(codePhase)) {
				list.add(tbc);
			}
		}
		return list;
	}

}
