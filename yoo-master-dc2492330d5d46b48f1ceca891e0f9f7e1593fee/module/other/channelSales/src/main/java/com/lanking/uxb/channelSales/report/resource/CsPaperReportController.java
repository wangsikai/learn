package com.lanking.uxb.channelSales.report.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.mq.common.constants.MqReportRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.channelSales.base.api.CsHomeworkClassService;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.report.api.CsStudentPaperReportRecordService;
import com.lanking.uxb.channelSales.report.api.CsStudentPaperReportService;
import com.lanking.uxb.channelSales.report.convert.CsStudentPaperReportRecordConvert;
import com.lanking.uxb.channelSales.report.form.StudentReportPaperForm;
import com.lanking.uxb.channelSales.report.value.VStudentPaperReportRecord;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 纸质报告相关.
 *
 * @author wanlong.che
 * @since 2017-09-29
 */
@RestController
@RequestMapping(value = "channelSales/pr")
public class CsPaperReportController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CsStudentPaperReportRecordService studentPaperReportRecordService;
	@Autowired
	private CsStudentPaperReportRecordConvert studentPaperReportRecordConvert;
	@Autowired
	private CsStudentPaperReportService studentPaperReportService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private CsHomeworkClassService homeworkClassService;
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 判断当前纸质报告状况.
	 * 
	 * @param classId
	 *            班级ID
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "check", method = { RequestMethod.GET, RequestMethod.POST })
	public Value check(Long classId, Long recordId) {
		if (classId == null && recordId == null) {
			return new Value(new MissingArgumentException());
		}
		// classId = 623068893010731008L;
		StudentPaperReportRecord studentPaperReportRecord = null;
		if (recordId != null && recordId > 0) {
			studentPaperReportRecord = studentPaperReportRecordService.get(recordId);
		} else {
			studentPaperReportRecord = studentPaperReportRecordService.getLatestRecord(classId);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		VStudentPaperReportRecord vo = studentPaperReportRecordConvert.to(studentPaperReportRecord);
		map.put("record", vo);

		if (vo != null) {
			// 判断是否有学生无数据
			List<StudentPaperReport> reports = studentPaperReportService.listByRecord(studentPaperReportRecord.getId());
			if (reports.size() == 0) {
				vo.setNoDatas(true);
			}
			List<Long> studentIds = new ArrayList<Long>(reports.size());
			for (StudentPaperReport report : reports) {
				studentIds.add(report.getStudentId());
			}
			String allStudentIdStr = studentPaperReportRecord.getStudentIdList();
			if (StringUtils.isNotBlank(allStudentIdStr)) {
				JSONArray array = JSONArray.parseArray(allStudentIdStr);
				Set<Long> allStudentIds = new HashSet<Long>(array.size());
				for (int i = 0; i < array.size(); i++) {
					allStudentIds.add(array.getLong(i));
				}

				// 没有数据学生
				allStudentIds.removeAll(studentIds);
				if (CollectionUtils.isNotEmpty(allStudentIds)) {
					List<VUser> users = userConvert.mgetList(allStudentIds);
					map.put("noDataUsers", users);
				}
				if (allStudentIds.size() == array.size()) {
					vo.setNoDatas(true);
				}
			}
		}

		return new Value(map);
	}

	/**
	 * 获取生成纸质报告所需数据.
	 * 
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "stuPaperReportDatas", method = { RequestMethod.GET, RequestMethod.POST })
	public Value stuPaperReportDatas(Long classId) {
		if (classId == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> map = new HashMap<String, Object>(2);

		// 获取班级所有学生
		List<Student> students = homeworkClassService.listAllStudents(classId);
		List<Map<String, Object>> studentDatas = new ArrayList<Map<String, Object>>(students.size());
		for (Student student : students) {
			Map<String, Object> data = new HashMap<String, Object>(2);
			data.put("id", student.getId().toString());
			data.put("name", student.getName());
			studentDatas.add(data);
		}
		map.put("students", studentDatas);
		return new Value(map);
	}

	/**
	 * 创建生成报告.
	 * 
	 * @param form
	 *            参数
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "createStudentReportPaper", method = { RequestMethod.GET, RequestMethod.POST })
	public Value createStudentReportPaper(StudentReportPaperForm form) {
		if (form == null) {
			return new Value(new MissingArgumentException());
		}

		if (form.isAllStudent()) {
			List<Student> students = homeworkClassService.listAllStudents(form.getClassId());
			List<Long> studentIds = new ArrayList<Long>(students.size());
			for (Student student : students) {
				studentIds.add(student.getId());
			}
			form.setStudentIds(studentIds);
		}

		try {
			studentPaperReportRecordService.createStudentReportPaper(form, Security.getUserId());

			// 通知数据生成
			JSONObject jo = new JSONObject();
			mqSender.send(MqReportRegistryConstants.EX_PAPERREPORT, MqReportRegistryConstants.RK_PAPERREPORT_DATA,
					MQ.builder().data(jo).build());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 下载报告.
	 * 
	 * @param recordId
	 *            报告ID
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "downloadReport", method = { RequestMethod.GET, RequestMethod.POST })
	public void downloadReport(long recordId, HttpServletRequest request, HttpServletResponse response) {
		StudentPaperReportRecord record = studentPaperReportRecordService.get(recordId);
		if (record != null) {
			UserChannel userChannel = userChannelService.getChannelByUser(record.getOperator());
			if (userChannel != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format2 = new SimpleDateFormat("yyyy年M月");
				String basePath = Env.getString("channelSales.stu.paperRport.path");
				String filePath = basePath + "/" + format.format(record.getCreateAt()) + "/" + recordId + ".zip";
				File file = new File(filePath);
				if (!file.exists()) {
					try {
						response.setHeader("Content-Type", "text/html; charset=UTF-8");
						response.getWriter().write("文件不存在！");
						return;
					} catch (IOException e) {
						logger.error("[stuPaperReport] 文件不存在！recordId=" + recordId);
					}
				}

				HomeworkClazz homeworkClazz = zyHomeworkClassService.get(record.getClassId());
				String fileName = homeworkClazz.getName() + " " + format2.format(record.getStartDate()) + "-"
						+ format2.format(record.getEndDate()) + "学情报告";

				OutputStream out = null;
				FileInputStream inputStream = null;
				FileChannel fileChannel = null;
				ByteBuffer bb = null;
				WritableByteChannel outChannel = null;
				try {
					String filename = new String(fileName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
					String agent = request.getHeader("User-Agent").toLowerCase();
					if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
						filename = URLEncoder.encode(fileName, "UTF-8").replace("+", " ");
					}

					response.setHeader("Content-Disposition", "inline; filename=\"" + filename + ".zip\"");
					response.setHeader("Content-Type", "application/x-zip-compressed; charset=UTF-8");

					out = response.getOutputStream();
					inputStream = new FileInputStream(file);
					fileChannel = inputStream.getChannel();

					outChannel = Channels.newChannel(out);
					fileChannel.transferTo(0, fileChannel.size(), outChannel);

					// 统计下载次数
					studentPaperReportRecordService.dowloadRecord(recordId);
				} catch (Exception e) {
					logger.error("export student paper report fail ", e);
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
		}
	}

	/**
	 * 获取未读消息数.
	 * 
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "countChannelNotRead", method = { RequestMethod.GET, RequestMethod.POST })
	public Value countChannelNotRead() {
		Map<String, Object> map = new HashMap<String, Object>(2);
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());
		if (userChannel != null) {
			long count = studentPaperReportRecordService.countChannelNotRead(userChannel.getCode());
			map.put("count", count);
			map.put("isChannel", true);
		} else {
			map.put("isChannel", false);
		}
		return new Value(map);
	}

	/**
	 * 查询报告.
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "queryRecords", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryRecords(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {

		// 当前用户渠道
		UserChannel userChannel = userChannelService.getChannelByUser(Security.getUserId());

		Page<StudentPaperReportRecord> pageDate = studentPaperReportRecordService.queryRecords(userChannel.getCode(),
				P.index(page, pageSize));

		VPage<VStudentPaperReportRecord> vp = new VPage<VStudentPaperReportRecord>();
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(pageDate.getPageCount());
		vp.setTotal(pageDate.getTotalCount());
		vp.setItems(studentPaperReportRecordConvert.to(pageDate.getItems()));

		return new Value(vp);
	}
}
