package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyUserSchoolBookService;

@Transactional(readOnly = true)
@Service
public class ZyUserSchoolBookServiceImpl implements ZyUserSchoolBookService {

	@Autowired
	@Qualifier("UserSchoolBookRepo")
	private Repo<UserSchoolBook, Long> userSchoolBookRepo;

	@Override
	public Map<Long, Long> getUserSelectedBook(long userId) {
		Map<Long, Long> userbookMap = Maps.newHashMap();
		Params params = Params.param();
		params.put("userId", userId);
		List<Map> userMap = userSchoolBookRepo.find("$userSelectedBook", params).list(Map.class);
		for (Map map : userMap) {
			userbookMap.put(Long.valueOf(map.get("book_id").toString()), Long.valueOf(map.get("id").toString()));
		}
		return userbookMap;
	}

	@Override
	public List<UserSchoolBook> existBook(List<Long> bookIds, long userId) {
		return userSchoolBookRepo.find("$getUserBook", Params.param("bookIds", bookIds).put("userId", userId)).list();
	}

	@Transactional
	@Override
	public void save(Map<Long, Long> bookFromMap, long userId) {
		List<UserSchoolBook> usbList = Lists.newArrayList();
		Date date = new Date();
		for (Long key : bookFromMap.keySet()) {
			UserSchoolBook usb = new UserSchoolBook();
			usb.setBookId(key);
			usb.setCreateAt(date);
			if (bookFromMap.get(key) != null) {
				usb.setSchoolBookId(bookFromMap.get(key));
			}
			usb.setStatus(Status.ENABLED);
			usb.setUpdateAt(date);
			usb.setUserId(userId);
			usbList.add(usb);
		}
		userSchoolBookRepo.save(usbList);

	}

	@Transactional
	@Override
	public void changeBookStatus(List<Long> disableBookList, long userId, Status status) {
		userSchoolBookRepo.execute("$changeBookStatus", Params.param("ids", disableBookList).put("updateAt", new Date())
				.put("userId", userId).put("status", status.getValue()));
	}

	@Override
	public void updateTeacherChoosedBook(Long id, Status status) {
		Map<Long, Long> userBookSelecteds = this.getUserSelectedBook(id);
		if (!userBookSelecteds.isEmpty()) {
			this.changeBookStatus(Lists.newArrayList(userBookSelecteds.keySet()), id, status);
		}

	}

}
