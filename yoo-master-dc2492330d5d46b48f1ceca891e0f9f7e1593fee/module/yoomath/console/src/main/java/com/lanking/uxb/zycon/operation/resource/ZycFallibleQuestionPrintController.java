package com.lanking.uxb.zycon.operation.resource;

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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Express;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleExportRecordService;
import com.lanking.uxb.zycon.operation.api.ZycFallPrintQuery;
import com.lanking.uxb.zycon.operation.api.ZycFallibleQuestionPrintOrderService;
import com.lanking.uxb.zycon.operation.convert.ZycFallibleQuestionPrintOrderConvert;
import com.lanking.uxb.zycon.operation.form.ZycFallPrintForm;
import com.lanking.uxb.zycon.operation.value.VZycFallibleQuestionPrintOrder;

/**
 * 错题打印管理
 * 
 * @author wangsenhao
 * @since 2.5.0
 */
@RestController
@RequestMapping("zyc/print")
public class ZycFallibleQuestionPrintController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ZycFallibleQuestionPrintOrderService orderService;
	@Autowired
	private ZycFallibleQuestionPrintOrderConvert orderConvert;
	@Autowired
	private ZyStudentFallibleExportRecordService studentFallibleExportRecordService;
	@Autowired
	private UserService userService;

	/**
	 * 查询代打印列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "query")
	public Value query(ZycFallPrintQuery query) {
		int page = query.getPage();
		int pageSize = query.getPageSize();
		Page<FallibleQuestionPrintOrder> cp = orderService.queryPrintList(query, P.index(page, pageSize));
		VPage<VZycFallibleQuestionPrintOrder> vp = new VPage<VZycFallibleQuestionPrintOrder>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(orderConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 保存修改代打印信息(状态、快递、单号)
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "save")
	public Value save(ZycFallPrintForm form) {
		orderService.savePrint(form);
		return new Value();
	}

	/**
	 * 获取一些基础的信息，不会随条件变化而变化
	 * 
	 * @return
	 */
	@RequestMapping(value = "base")
	public Value base() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("todo", orderService.countPrintList(FallibleQuestionPrintOrderStatus.PAY));
		data.put("total", orderService.countPrintList(null));
		data.put("express", Express.values());
		return new Value(data);
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
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response, Long recordId) {
		if (recordId == null) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}

		StudentFallibleExportRecord record = studentFallibleExportRecordService.get(recordId);
		long studentId = record.getStudentId();

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
			OutputStream out = null;
			FileInputStream inputStream = null;
			FileChannel fileChannel = null;
			ByteBuffer bb = null;
			WritableByteChannel outChannel = null;
			try {
				UserInfo student = userService.getUser(studentId);
				String docName = student.getName() + "错题本";

				String filename = new String(docName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
				String agent = request.getHeader("User-Agent").toLowerCase();
				if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
					filename = URLEncoder.encode(docName, "UTF-8").replace("+", " ");
				}

				response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "."
						+ record.getExtend().toLowerCase() + "\"");
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
		} else {
			// 文件丢失！
			try {
				response.setHeader("Content-Type", "html/text; charset=UTF-8");
				response.getWriter().write("文档过期已被删除，请重新至错题本生成文档！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

}
