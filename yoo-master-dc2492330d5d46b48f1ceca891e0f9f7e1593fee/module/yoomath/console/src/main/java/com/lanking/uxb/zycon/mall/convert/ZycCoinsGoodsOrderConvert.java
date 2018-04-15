package com.lanking.uxb.zycon.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.homework.api.ZycUserService;
import com.lanking.uxb.zycon.mall.api.ZycGoodsSnapshotService;
import com.lanking.uxb.zycon.mall.value.VZycCoinsGoodsOrder;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.qs.api.ZycSchoolService;
import com.lanking.uxb.zycon.qs.api.ZycTeacherService;

@Component
public class ZycCoinsGoodsOrderConvert extends Converter<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long> {

	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private ZycUserService userService;
	@Autowired
	private ZycTeacherService teacherService;
	@Autowired
	private ZycStudentService studentService;
	@Autowired
	private ZycSchoolService schoolService;
	@Autowired
	private ZycGoodsSnapshotService goodService;

	@Override
	protected Long getId(CoinsGoodsOrder s) {
		return s.getId();
	}

	@Override
	protected VZycCoinsGoodsOrder convert(CoinsGoodsOrder s) {
		VZycCoinsGoodsOrder v = new VZycCoinsGoodsOrder();
		v.setOrderAt(s.getOrderAt());
		v.setOrderId(s.getId());
		v.setCode(s.getCode());
		v.setStatus(s.getStatus());
		v.setNumber(s.getP0());
		v.setSellerNotes(s.getSellerNotes());
		v.setSource(s.getSource());
		v.setContactName(s.getContactName());
		v.setContactAddress(s.getContactAddress());
		v.setContactPhone(s.getContactPhone());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long, GoodsSnapshot>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VZycCoinsGoodsOrder d) {
				return s.getGoodsSnapshotId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VZycCoinsGoodsOrder d, GoodsSnapshot value) {
				d.setGoodName(value.getName());
			}

			@Override
			public GoodsSnapshot getValue(Long key) {
				return goodService.get(key);
			}

			@Override
			public Map<Long, GoodsSnapshot> mgetValue(Collection<Long> keys) {
				return goodService.mget(keys);
			}

		});
		assemblers.add(new ConverterAssembler<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long, User>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VZycCoinsGoodsOrder d) {
				return s.getUserId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VZycCoinsGoodsOrder d, User value) {
				d.setAccountId(value.getAccountId());
				s.setUserType(value.getUserType());
				if (value.getUserType() == UserType.PARENT) {
					d.setRoleName("家长");
				}
				if (value.getUserType() == UserType.STUDENT) {
					d.setRoleName("学生");
				}
				if (value.getUserType() == UserType.TEACHER) {
					d.setRoleName("老师");
				}
			}

			@Override
			public User getValue(Long key) {
				return userService.get(key);
			}

			@Override
			public Map<Long, User> mgetValue(Collection<Long> keys) {
				return userService.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long, Account>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VZycCoinsGoodsOrder d) {
				return d.getAccountId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VZycCoinsGoodsOrder d, Account value) {
				d.setAccountName(value.getName());
			}

			@Override
			public Account getValue(Long key) {
				return accountService.getAccount(key);
			}

			@Override
			public Map<Long, Account> mgetValue(Collection<Long> keys) {
				return accountService.mgeAccount(keys);
			}

		});
		assemblers.add(new ConverterAssembler<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long, Teacher>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return s.getUserType() == UserType.TEACHER;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VZycCoinsGoodsOrder d) {
				return s.getUserId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VZycCoinsGoodsOrder d, Teacher value) {
				d.setSchoolId(value.getSchoolId());
			}

			@Override
			public Teacher getValue(Long key) {
				return teacherService.get(key);
			}

			@Override
			public Map<Long, Teacher> mgetValue(Collection<Long> keys) {
				return teacherService.mget(keys);
			}

		});
		assemblers.add(new ConverterAssembler<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long, Student>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return s.getUserType() == UserType.STUDENT;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VZycCoinsGoodsOrder d) {
				return s.getUserId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VZycCoinsGoodsOrder d, Student value) {
				d.setSchoolId(value.getSchoolId());

			}

			@Override
			public Student getValue(Long key) {
				return studentService.get(key);
			}

			@Override
			public Map<Long, Student> mgetValue(Collection<Long> keys) {
				return studentService.mget(keys);
			}

		});
		assemblers.add(new ConverterAssembler<VZycCoinsGoodsOrder, CoinsGoodsOrder, Long, School>() {

			@Override
			public boolean accept(CoinsGoodsOrder s) {
				return s.getUserType() == UserType.STUDENT;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(CoinsGoodsOrder s, VZycCoinsGoodsOrder d) {
				return d.getSchoolId();
			}

			@Override
			public void setValue(CoinsGoodsOrder s, VZycCoinsGoodsOrder d, School value) {
				d.setSchoolName(value.getName());

			}

			@Override
			public School getValue(Long key) {
				return schoolService.get(key);
			}

			@Override
			public Map<Long, School> mgetValue(Collection<Long> keys) {
				return schoolService.mget(keys);
			}

		});
	}
}
