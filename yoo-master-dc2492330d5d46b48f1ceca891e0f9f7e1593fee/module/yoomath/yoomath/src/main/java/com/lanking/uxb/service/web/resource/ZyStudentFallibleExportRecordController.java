package com.lanking.uxb.service.web.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.web.api.ZyStudentFallibleExportService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleExportRecordService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleExportRecordConvert;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleExportRecord;

/**
 * 学生导出记录.
 * 
 * @author wlche
 * @since web 2.0.3
 */

@RestController
@RequestMapping(value = "zy/s/fallible/record")
public class ZyStudentFallibleExportRecordController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZyStudentFallibleExportRecordService studentFallibleExportRecordService;
	@Autowired
	private ZyStudentFallibleExportRecordConvert studentFallibleExportRecordConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private ZyStudentFallibleExportService studentFallibleExportService;

	/**
	 * 分页查询当前学生的错题下载记录.
	 * 
	 * @param pageNo
	 * @param page
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryRecord")
	@MemberAllowed
	public Value queryRecord(Integer pageNo, Integer size) {
		pageNo = pageNo == null ? 1 : pageNo;
		size = size == null ? 1 : size;
		Page<StudentFallibleExportRecord> page = studentFallibleExportRecordService.query(Security.getUserId(),
				P.offset((pageNo - 1) * size, size));

		List<Long> recordIDs = new ArrayList<Long>();
		for (StudentFallibleExportRecord record : page.getItems()) {
			if (record.getFallibleQuestionPrintOrderId() != null) {
				recordIDs.add(record.getFallibleQuestionPrintOrderId());
			}
		}

		VPage<VStudentFallibleExportRecord> vpage = new VPage<VStudentFallibleExportRecord>();
		vpage.setPageSize(size);
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(studentFallibleExportRecordConvert.to(page.getItems()));
		}

		if (recordIDs.size() > 0) {
			// 查询代打印订单
			Map<Long, FallibleQuestionPrintOrder> orderMap = stuFalliblePrintService.mget(recordIDs);

			// 拼装地区名称
			List<Long> codes = new ArrayList<Long>();
			for (FallibleQuestionPrintOrder order : orderMap.values()) {
				codes.add(order.getDistrictCode());
			}
			Map<Long, String> districtMap = districtService.mgetDistrictName(codes);

			for (VStudentFallibleExportRecord record : vpage.getItems()) {
				if (record.getFallibleQuestionPrintOrderId() != null) {
					FallibleQuestionPrintOrder order = orderMap.get(record.getFallibleQuestionPrintOrderId());
					record.setExpress(order.getExpress());
					record.setExpressCode(order.getExpressCode());
					if (order.getExpress() != null) {
						record.setExpressName(order.getExpress().getTitle());
					}
					record.setContactName(order.getContactName());
					record.setContactPhone(order.getContactPhone());
					record.setContactAddress(order.getContactAddress());
					record.setDistrictCode(order.getDistrictCode());
					record.setDistrictName(districtMap.get(order.getDistrictCode()));
					record.setOrderStatus(order.getStatus());
				}
				// 会员免费使用
				record.setBuy(SecurityContext.getMemberType() != MemberType.NONE ? true : record.isBuy());
			}
		}

		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(pageNo);
		return new Value(vpage);
	}

	/**
	 * 兑换.
	 * 
	 * @param id
	 *            记录ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "exchange")
	public Value exchange(Long recordId) {
		if (recordId == null) {
			return new Value(new MissingArgumentException());
		}
		long studentId = Security.getUserId();
		StudentFallibleExportRecord record = studentFallibleExportRecordService.get(recordId);
		if (record == null || record.getStudentId().longValue() != studentId) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> map = new HashMap<String, Object>(2);

		// 个人金币个数
		UserHonor honor = userHonorService.getUserHonor(studentId);
		map.put("coins", honor == null ? 0 : honor.getCoins());
		if (honor == null || honor.getCoins() < record.getCount()) {
			// 金币个数不足
			map.put("coinsEnough", 0);
		} else {
			map.put("coinsEnough", 1);

			// 更新记录状态
			studentFallibleExportRecordService.buy(recordId);

			// 金币兑换记录
			coinsService.earn(CoinsAction.BUY_COINS_FALLIBLE_DOC, studentId, -record.getSellPrice(), Biz.NULL, 0);
		}

		return new Value(map);
	}

	/**
	 * 下载文档.
	 * 
	 * @param request
	 * @param response
	 * @param recordId
	 *            兑换记录ID
	 * @param hash
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "download")
	@MemberAllowed
	public void download(HttpServletRequest request, HttpServletResponse response, Long recordId,
			String host) {
		if (recordId == null) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}

		long studentId = Security.getUserId();
		StudentFallibleExportRecord record = studentFallibleExportRecordService.get(recordId);

		// 会员免费使用
		record.setBuy(SecurityContext.getMemberType() != MemberType.NONE ? true : record.getBuy());

		if (record == null || record.getStudentId().longValue() != studentId || !record.getBuy()) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在或未兑换！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}

		// 获取文档存储路径
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = format.format(record.getCreatAt());
		String storePath = new StringBuffer(Env.getString("word.file.store.path")).append("/stuFallible2/")
				.append(studentId).append("/").append(today).append("/").append(record.getHash()).append(".")
				.append(record.getExtend().toLowerCase()).toString();

		File file = new File(storePath);
		if (file.exists()) {
			this.fileDownload(record, request, response, file);
		} else {
			// 根据记录重新生成
			List<Long> sectionCodes = record.getSectionCodes();
			String attachData = record.getAttachData();
			// 记录未记载attachData
			if (StringUtils.isBlank(attachData)) {
				try {
					response.setHeader("Content-Type", "html/text; charset=UTF-8");
					response.getWriter().write("文档过期已被删除，请重新至错题本生成文档！");
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				return;
			}

			JSONObject jo = JSONObject.parseObject(attachData);
			Integer errorTimes = (Integer) jo.get("errorTimes");
			List<Integer> questionTypes = (List<Integer>) jo.get("questionTypes");
			Integer timeScope = (Integer) jo.get("timeScope");

			// 错题集合
			List<Map> fquesions = sfqService.queryStudentExportQuestion(studentId, sectionCodes,
					getDateScope(timeScope, record.getCreatAt()), questionTypes, errorTimes);
			int hash = record.getHash();

			Object get = studentFallibleExportService.writeFileAndRecordTask(host, studentId, hash, fquesions,
					sectionCodes, null, true, attachData, null, true, record.getCreatAt());
			if (get == null) {
				this.fileDownload(record, request, response, file);
			}
		}
	}
	
	/**
	 * 文件下载
	 * */
	private void fileDownload(StudentFallibleExportRecord record, HttpServletRequest request,
			HttpServletResponse response, File file) {
		OutputStream out = null;
		FileInputStream inputStream = null;
		FileChannel fileChannel = null;
		ByteBuffer bb = null;
		WritableByteChannel outChannel = null;
		try {
			@SuppressWarnings("deprecation")
			String filename = new String(record.getName().getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
			String agent = request.getHeader("User-Agent").toLowerCase();
			if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
				filename = URLEncoder.encode(record.getName(), "UTF-8").replace("+", " ");
			}

			response.setHeader("Content-Disposition",
					"inline; filename=\"" + filename + "." + record.getExtend().toLowerCase() + "\"");
			if (record.getExtend().toLowerCase().equals("doc")) {
				response.setHeader("Content-Type", "application/msword; charset=UTF-8");
			} else if (record.getExtend().toLowerCase().equals("docx")) {
				response.setHeader("Content-Type",
						"application/vnd.openxmlformats-officedocument.wordprocessingml.document; charset=UTF-8");
			}

			out = response.getOutputStream();
			inputStream = new FileInputStream(file);
			fileChannel = inputStream.getChannel();

			outChannel = Channels.newChannel(out);
			fileChannel.transferTo(0, fileChannel.size(), outChannel); // NIO管道输出
		} catch (Exception e) {
			logger.error("export word fail ", e);
		} finally {
			if (null != bb) {
				bb.clear();
			}
			try {
				if (outChannel != null) {
					outChannel.close();
				}
				if (fileChannel != null) {
					fileChannel.close();
				}
				if (out != null) {
					out.flush();
					out.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 处理时间范围
	 * 
	 * @param timeScope
	 * @param createAt
	 * */
	private Date getDateScope(Integer timeScope, Date createAt) {
		Date dateScope = null;
		if (timeScope != null && timeScope != 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(format.parse(format.format(createAt)));
				if (timeScope == 1) {
					cal.add(Calendar.DAY_OF_YEAR, -30);
				} else if (timeScope == 3) {
					cal.add(Calendar.DAY_OF_YEAR, -90);
				} else if (timeScope == 6) {
					cal.add(Calendar.DAY_OF_YEAR, -180);
				}
				dateScope = cal.getTime();
			} catch (ParseException e) {
				logger.error("previewDownload format date error ", e);
			}
		}

		return dateScope;
	}
	
	/**
	 * 删除记录.
	 * 
	 * @param recordId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "delete")
	public Value delete(Long recordId) {
		if (null == recordId) {
			return new Value(new MissingArgumentException());
		}
		try {
			studentFallibleExportRecordService.updateStatus(recordId, Status.DELETED);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}
}