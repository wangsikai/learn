package com.lanking.uxb.channelSales.openmember.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.member.MemberPackage;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrder;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSnapshot;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderSource;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderStatus;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderUser;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.channel.api.CsUserService;
import com.lanking.uxb.channelSales.common.ex.ChannelSalesConsoleException;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.channelSales.openmember.api.CsAccountService;
import com.lanking.uxb.channelSales.openmember.api.CsMemberPackageOrderService;
import com.lanking.uxb.channelSales.openmember.api.CsQuestionSchoolService;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;
import com.lanking.uxb.channelSales.openmember.form.OpenMemberPackageForm;
import com.lanking.uxb.channelSales.openmember.form.UserMemberCreateForm;
import com.lanking.uxb.service.mall.api.MemberPackageOrderSettlementService;
import com.taobao.api.internal.util.StringUtils;

/**
 * @see CsMemberPackageOrderService
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsMemberPackageOrderServiceImpl implements CsMemberPackageOrderService {
	@Autowired
	@Qualifier("MemberPackageOrderRepo")
	private Repo<MemberPackageOrder, Long> memberPackageOrderRepo;

	@Autowired
	@Qualifier("MemberPackageOrderSnapshotRepo")
	private Repo<MemberPackageOrderSnapshot, Long> memberPackageOrderSnapshotRepo;

	@Autowired
	@Qualifier("MemberPackageOrderUserRepo")
	private Repo<MemberPackageOrderUser, Long> memberPackageOrderUserRepo;
	@Autowired
	@Qualifier("UserChannelRepo")
	private Repo<UserChannel, Integer> userChannelRepo;

	@Autowired
	private CsMemberPackageService memberPackageService;
	@Autowired
	private CsUserMemberService userMemberService;
	@Autowired
	private CsUserService userService;
	@Autowired
	private CsQuestionSchoolService questionSchoolService;
	@Autowired
	private MemberPackageOrderSettlementService memberPackageOrderSettlementService;

	private static final Logger logger = LoggerFactory.getLogger(CsMemberPackageOrderService.class);
	/** 最大可通过Excel开通会员行数 */
	private static final int MAX_IMPORT_COUNT = 1000;

	@Autowired
	private CsAccountService accountService;

	@Override
	@Transactional
	public void create(OpenMemberPackageForm form, long userId) {
		if (form.getMemberPackageId() == null && form.getEndAt() == null) {
			throw new IllegalArgException();
		}
		if (CollectionUtils.isEmpty(form.getUserIds())) {
			throw new MissingArgumentException();
		}

		MemberPackage memberPackage = null;
		if (form.getMemberPackageId() != null) {
			memberPackage = memberPackageService.get(form.getMemberPackageId());
			if (memberPackage == null) {
				throw new IllegalArgException();
			}

			if (memberPackage.getStatus() != Status.ENABLED) {
				throw new IllegalArgException();
			}

			form.setMemberType(memberPackage.getMemberType());
			form.setTotalPrice(new BigDecimal(memberPackage.getPresentPrice().doubleValue() * form.getUserIds().size()));
		} else if (form.getMemberType() == null) {
			throw new IllegalArgException();
		} else if (form.getTotalPrice() == null) {
			throw new MissingArgumentException();
		}

		UserChannel userChannel = null;
		// 渠道商可以开通校级会员，校级会员跟额度无关
		if (form.getChannelCode() != null && form.getMemberType() != MemberType.SCHOOL_VIP) {
			userChannel = userChannelRepo.get(form.getChannelCode());
			double limit = userChannel.getOpenMemberLimit() == null ? 0d : userChannel.getOpenMemberLimit()
					.doubleValue();
			double opened = userChannel.getOpenedMember() == null ? 0d : userChannel.getOpenedMember().doubleValue();
			if (limit == 0 || limit < 0 || (limit - (opened + form.getTotalPrice().doubleValue())) < 0) {
				throw new ChannelSalesConsoleException(ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_LIMIT_ERROR);
			}
		}

		List<User> users = null;
		// 开通校极vip
		if (form.getMemberType() == MemberType.SCHOOL_VIP && form.getUserType() == UserType.TEACHER) {
			List<User> teachers = userService.findBySchool(form.getSchoolId(), UserType.TEACHER);
			Map<Long, User> teacherMap = new HashMap<Long, User>(teachers.size());
			for (User t : teachers) {
				teacherMap.put(t.getId(), t);
			}

			users = new ArrayList<User>(teachers.size());

			for (Long u : form.getUserIds()) {
				if (teacherMap.get(u) != null) {
					users.add(teacherMap.get(u));
				}
			}

			questionSchoolService.create(form.getSchoolId(), users.size());
		} else {
			users = userService.mgetList(form.getUserIds());
		}

		if (CollectionUtils.isEmpty(users)) {
			throw new IllegalArgException();
		}

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);

		UserMemberCreateForm memberCreateForm = new UserMemberCreateForm();
		memberCreateForm.setStartAt(cal.getTime());
		memberCreateForm.setMemberType(form.getMemberType());
		if (form.getEndAt() == null) {
			if (memberPackage != null) {
				memberCreateForm.setEndAt(DateUtils.addMonths(now,
						memberPackage.getMonth() + memberPackage.getExtraMonth()));
			}
		} else {
			try {
				memberCreateForm.setEndAt(new SimpleDateFormat("yyyy-MM-dd").parse(form.getEndAt()));
			} catch (ParseException e) {
				throw new IllegalArgException();
			}
		}

		// 保存订单信息
		MemberPackageOrder order = new MemberPackageOrder();
		Integer channelCode = users.get(0).getUserChannelCode();
		if (channelCode != UserChannel.YOOMATH) {
			order.setSource(MemberPackageOrderSource.CHANNEL);
			if (form.getUserType() != UserType.TEACHER && form.getChannelCode() != null && form.getChannelCode() > 0
					&& form.getChannelCode() != UserChannel.YOOMATH) {
				if (userChannel == null) {
					userChannel = userChannelRepo.get(channelCode);
				}
				userChannel.setOpenedMember(new BigDecimal(userChannel.getOpenedMember() == null ? 0 : userChannel
						.getOpenedMember().doubleValue() + form.getTotalPrice().doubleValue()));

				userChannelRepo.save(userChannel);
			}

		} else {
			order.setSource(MemberPackageOrderSource.USER);
		}

		// order.setUserId((long) users.get(0).getUserChannelCode());
		order.setUserChannelCode(users.get(0).getUserChannelCode());
		order.setMemberPackageGroupId(memberPackage == null || memberPackage.getMemberPackageGroupId() == null ? null
				: memberPackage.getMemberPackageGroupId());

		order.setAmount(form.getUserIds().size());
		order.setMemberPackageId(form.getMemberPackageId() == null ? 0 : form.getMemberPackageId());
		order.setMemberType(form.getMemberType());
		order.setOrderAt(now);
		order.setUpdateAt(now);
		order.setUpdateId(userId);
		order.setDelStatus(Status.ENABLED);
		if (form.getChannelCode() != null && form.getChannelCode() > 0) {
			order.setType(MemberPackageOrderType.CHANNEL_ADMIN);
		} else {
			order.setType(MemberPackageOrderType.ADMIN);
		}
		order.setStatus(MemberPackageOrderStatus.COMPLETE);
		order.setTotalPrice(form.getTotalPrice());

		memberPackageOrderRepo.save(order);
		order.setCode(order.getId().toString());

		// 保存订单快照信息
		MemberPackageOrderSnapshot snapshot = createSnapshot(order);
		order.setMemberPackageOrderSnapshotId(snapshot.getId());

		memberPackageOrderRepo.save(order);
		memberCreateForm.setOrderId(order.getId());

		for (User u : users) {
			if (!u.getUserChannelCode().equals(channelCode)) {
				throw new NoPermissionException();
			}
			// 订单与用户的对应关系
			MemberPackageOrderUser orderUser = new MemberPackageOrderUser();
			orderUser.setChannelCode(u.getUserChannelCode());
			orderUser.setMemberPackageOrderId(order.getId());
			orderUser.setMemberPackageOrderSnapshotId(snapshot.getId());
			orderUser.setMemberType(form.getMemberType());
			orderUser.setUserId(u.getId());
			if (form.getEndAt() != null) {
				try {
					orderUser.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(form.getEndAt()));
				} catch (ParseException e) {
					throw new IllegalArgException();
				}
			}

			memberPackageOrderUserRepo.save(orderUser);

			memberCreateForm.setUserId(u.getId());
			// 创建用户会员信息
			userMemberService.create(memberCreateForm);
		}

		// 创建会员订单结算数据
		memberPackageOrderSettlementService.createByOrder(order, users.size());
	}

	@Transactional
	private MemberPackageOrderSnapshot createSnapshot(MemberPackageOrder order) {
		MemberPackageOrderSnapshot snapshot = new MemberPackageOrderSnapshot();
		snapshot.setMemberPackageOrderId(order.getId());
		snapshot.setMemberPackageId(order.getMemberPackageId());
		snapshot.setOrderAt(order.getOrderAt());
		snapshot.setSource(order.getSource());
		snapshot.setUpdateId(order.getUpdateId());
		snapshot.setDelStatus(order.getDelStatus());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setStatus(order.getStatus());
		snapshot.setType(order.getType());
		snapshot.setUserId(order.getUserId());
		snapshot.setAmount(order.getAmount());
		snapshot.setUpdateAt(order.getUpdateAt());
		snapshot.setMemberType(order.getMemberType());
		snapshot.setAttachData(order.getAttachData());
		snapshot.setPaymentCode(order.getPaymentCode());
		snapshot.setBuyerNotes(order.getBuyerNotes());
		snapshot.setCode(order.getCode());
		snapshot.setPaymentPlatformCode(order.getPaymentPlatformCode());
		snapshot.setPayMod(order.getPayMod());
		snapshot.setVirtualCardCode(order.getVirtualCardCode());
		snapshot.setVirtualCardType(order.getVirtualCardType());
		snapshot.setPayTime(order.getPayTime());
		snapshot.setUserChannelCode(order.getUserChannelCode());
		snapshot.setMemberPackageGroupId(order.getMemberPackageGroupId());
		return memberPackageOrderSnapshotRepo.save(snapshot);
	}

	@Override
	@Transactional
	public MemberPackageOrder updatePaymentCallback(long memberPackageOrderID, String paymentPlatformOrderCode,
			String paymentCode, Date payTime) {
		MemberPackageOrder order = memberPackageOrderRepo.get(memberPackageOrderID);
		if (order.getPayTime() == null) {
			Date date = new Date();
			order.setPaymentPlatformOrderCode(paymentPlatformOrderCode);
			order.setPaymentCode(paymentCode);
			order.setPayTime(payTime);
			order.setStatus(MemberPackageOrderStatus.PAY);
			order.setUpdateAt(date);

			// 订单快照
			MemberPackageOrderSnapshot snapshot = this.createOrderSnapshot(order, date, null);
			order.setMemberPackageOrderSnapshotId(snapshot.getId());
			memberPackageOrderRepo.save(order);
		}
		return order;
	}

	/**
	 * 创建订单快照.
	 * 
	 * @param order
	 *            订单
	 * @return
	 */
	@Override
	@Transactional
	public MemberPackageOrderSnapshot createOrderSnapshot(MemberPackageOrder order, Date updateAt, Long updateID) {
		MemberPackageOrderSnapshot snapshot = new MemberPackageOrderSnapshot();
		snapshot.setAmount(order.getAmount());
		snapshot.setBuyerNotes(order.getBuyerNotes());
		snapshot.setCode(order.getCode());
		snapshot.setDelStatus(order.getDelStatus());
		snapshot.setMemberPackageId(order.getMemberPackageId());
		snapshot.setMemberPackageOrderId(order.getId());
		snapshot.setMemberType(order.getMemberType());
		snapshot.setOrderAt(order.getOrderAt());
		snapshot.setPayMod(order.getPayMod());
		snapshot.setPaymentPlatformCode(order.getPaymentPlatformCode());
		snapshot.setPaymentCode(order.getPaymentCode());
		snapshot.setPaymentPlatformOrderCode(order.getPaymentPlatformOrderCode());
		snapshot.setThirdPaymentMethod(order.getThirdPaymentMethod());
		snapshot.setPayTime(order.getPayTime());
		snapshot.setSellerNotes(order.getSellerNotes());
		snapshot.setSource(order.getSource());
		snapshot.setType(order.getType());
		snapshot.setTotalPrice(order.getTotalPrice());
		snapshot.setUpdateAt(updateAt);
		snapshot.setUpdateId(updateID);
		snapshot.setUserId(order.getUserId());
		snapshot.setVirtualCardType(order.getVirtualCardType());
		snapshot.setVirtualCardCode(order.getVirtualCardCode());
		snapshot.setStatus(order.getStatus());
		snapshot.setUserChannelCode(order.getUserChannelCode());
		snapshot.setMemberPackageGroupId(order.getMemberPackageGroupId());
		memberPackageOrderSnapshotRepo.save(snapshot);
		return snapshot;
	}

	@Override
	@Transactional
	public MemberPackageOrder updateOrderStatus(long memberPackageOrderID, Long updateID,
			MemberPackageOrderStatus status) {
		MemberPackageOrder order = memberPackageOrderRepo.get(memberPackageOrderID);
		if (order.getStatus() != status) {
			Date date = new Date();
			order.setStatus(status);
			order.setUpdateId(updateID);
			order.setUpdateAt(date);

			// 订单快照
			MemberPackageOrderSnapshot snapshot = this.createOrderSnapshot(order, date, updateID);
			order.setMemberPackageOrderSnapshotId(snapshot.getId());
			memberPackageOrderRepo.save(order);
		}
		return order;
	}

	@Override
	public List<Long> importMemberUser(HttpServletRequest request, Integer channelCode, UserType userType,
			MemberType memberType) {

		Workbook workbook = null;
		List<Long> users = Lists.newArrayList();
		int line = 0, index = 1;
		try {
			InputStream in = request.getInputStream();
			if (!in.markSupported()) {
				in = new PushbackInputStream(in, 1024);
			}

			if (POIFSFileSystem.hasPOIFSHeader(in)) {
				workbook = new HSSFWorkbook(in);
			} else if (POIXMLDocument.hasOOXMLHeader(in)) {
				workbook = new XSSFWorkbook(OPCPackage.open(in));
			}

			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();
			// 开通用户数量超过最大值
			if (rowCount >= (MAX_IMPORT_COUNT + 1)) {
				throw new ChannelSalesConsoleException(
						ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_IMPORT_MAX_COUNT);
			}

			List<String> datas = new ArrayList<String>(rowCount >= 40 ? 40 : rowCount);
			for (int r = 1; r < rowCount; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}

				String code = null, dataChannelCode = null, schoolCode = "";
				for (int c = 0; c <= 2; c++) {
					Cell cell = row.getCell(c);
					if (null == cell) {
						throw new ChannelSalesConsoleException(
								ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR, index);
					}
					switch (c) {
					case 0:
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							code = String.valueOf(cell.getNumericCellValue());
						} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							code = cell.getStringCellValue();
						} else {
							throw new ChannelSalesConsoleException(
									ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR, index);
						}
						break;
					case 1:
						// Excel会将纯数字数据转为number类型，或者纯数字还是String类型
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							dataChannelCode = cell.getStringCellValue();
						} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							dataChannelCode = String.valueOf(cell.getNumericCellValue());
						} else {
							throw new ChannelSalesConsoleException(
									ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR, index);
						}
						break;
					case 2:
						if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
							schoolCode = cell.getStringCellValue();
						} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
							schoolCode = String.valueOf(cell.getNumericCellValue());
						} else {
							throw new ChannelSalesConsoleException(
									ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR, index);
						}
						break;
					}
				}

				if (StringUtils.isEmpty(code) || StringUtils.isEmpty(dataChannelCode)
						|| StringUtils.isEmpty(schoolCode)) {
					continue;
				}

				if (datas.contains(code)) {
					continue;
				}

				try {
					if (channelCode != null && channelCode > 0
							&& Double.valueOf(dataChannelCode).longValue() != channelCode) {
						throw new ChannelSalesConsoleException(
								ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_NOT_CHANNELUSER, index);
					}
				} catch (NumberFormatException e) {
					throw new ChannelSalesConsoleException(
							ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR, index);
				}

				datas.add(code);

				if (datas.size() == 40) {
					users.addAll(openMemberByImport(datas, channelCode, memberType, line));
					datas = new ArrayList<String>(40);
					line += 40;
				}
				index++;
			}
			users.addAll(openMemberByImport(datas, channelCode, memberType, line));
		} catch (InvalidFormatException e) {
			logger.error("not valid excel file: {}", e);
			// Excel填写的数据包含非法格式
			throw new ChannelSalesConsoleException(
					ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_DATAFORMAT_ERROR, line);
		} catch (IOException e) {
			logger.error("when import the member user has error: {}", e);
		}

		return users;
	}

	private List<Long> openMemberByImport(List<String> accountNames, Integer channel, MemberType memberType, int line)
			throws ChannelSalesConsoleException {

		List<User> users = userService.findByAccountNames(accountNames);
		if (CollectionUtils.isEmpty(users)) {
			throw new ChannelSalesConsoleException(
					ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_ACCOUNT_NOT_EXISTS);
		}

		Map<Long, Account> accountMap = accountService.mgetByNames(accountNames);
		Map<String, Account> nameAccountName = new HashMap<String, Account>(accountNames.size());
		for (Map.Entry<Long, Account> a : accountMap.entrySet()) {
			nameAccountName.put(a.getValue().getName(), a.getValue());
		}
		// 表明根据账号去查询用户有问题
		int index = 1;
		if (accountMap.size() != accountNames.size()) {
			for (String accountName : accountNames) {
				if (!nameAccountName.containsKey(accountName)) {
					throw new ChannelSalesConsoleException(
							ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_ACCOUNT_NOT_EXISTS, line + index);
				}
				index++;
			}
		}

		List<Long> userIds = new ArrayList<Long>(users.size());
		for (User u : users) {
			userIds.add(u.getId());
		}

		Map<Long, UserMember> userMemberMap = userMemberService.findByUsers(userIds);

		for (User u : users) {
			Account account = accountMap.get(u.getAccountId());

			if (channel != null && channel > 0 && !u.getUserChannelCode().equals(channel)) {
				throw new ChannelSalesConsoleException(
						ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_NOT_CHANNELUSER,
						accountNames.indexOf(account.getName()) + line + 1);
			}

			if (memberType == MemberType.SCHOOL_VIP && u.getUserType() != UserType.TEACHER) {
				throw new ChannelSalesConsoleException(
						ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_NOT_CORRECT_USERTYPE,
						accountNames.indexOf(account.getName()) + line + 1);
			}

			UserMember userMember = userMemberMap.get(u.getId());
			// 用户以前并没有开过会员
			if (userMember != null && userMember.getMemberType() == MemberType.SCHOOL_VIP
					&& memberType == MemberType.VIP) {
				throw new ChannelSalesConsoleException(ChannelSalesConsoleException.CHANNELSALES_OPENMEMBER_ISSCHOOL,
						accountNames.indexOf(account.getName()) + line + 1);
			}
		}

		return userIds;
	}

}
