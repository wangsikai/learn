package com.lanking.uxb.service.examPaper.resource;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.CustomExampaperExportService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperService;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvert;
import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm;

/**
 * 教师组卷导出功能.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月19日
 */
@RestController
@RequestMapping(value = "zy/t/ep/exp")
@RolesAllowed(userTypes = { "TEACHER" })
public class ZyTeaCustomExampaperExportController {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomExampaperService customExampaperService;
	@Autowired
	private CustomExampaperConvert customExampaperConvert;
	@Autowired
	private CustomExampaperExportService customExampaperExportService;

	/**
	 * 创建教师组卷
	 * 
	 * @param TeaCustomExampaperExportForm
	 *            参数
	 * @return
	 */
	@RequestMapping(value = "create")
	public Value createExportDoc(TeaCustomExampaperExportForm form) {
		if (null == form) {
			return new Value(new MissingArgumentException());
		}

		// 创建文档
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			int hash = customExampaperExportService.createTeaCustomExampaperDoc(form);
			map.put("hash", hash);
			return new Value(map);
		} catch (Exception e) {
			logger.error("[教师组卷导出] 导出出错，" + e.getMessage(), e);
			return new Value(new ServerException(e));
		}
	}

	/**
	 * 下载.
	 * 
	 * @param form
	 *            参数
	 */

	@RequestMapping(value = "download")
	public void download(Integer hash, Long id, HttpServletRequest request, HttpServletResponse response) {
		CustomExampaper customExampaper = customExampaperService.get(id);
		if (customExampaper == null) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}
		try {
			// 获取文档存储路径
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String createAtStr = format.format(customExampaper.getCreateAt());
			String destDir = new StringBuffer(Env.getString("word.file.store.path")).append("/tea-exampaper/")
					.append(createAtStr).append("/").append(customExampaper.getCreateId()).toString();

			// 判断压缩文档是否已存在
			String zipFilePath = destDir + "/" + hash + ".zip";
			File zipFile = new File(zipFilePath);
			if (!zipFile.exists()) {
				try {
					response.setHeader("Content-Type", "text/html; charset=UTF-8");
					response.getWriter().write("文件不存在！");
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				return;
			}

			OutputStream out = null;
			FileInputStream inputStream = null;
			FileChannel fileChannel = null;
			ByteBuffer bb = null;
			WritableByteChannel outChannel = null;
			try {
				String filename = new String(customExampaper.getName().getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
				String agent = request.getHeader("User-Agent").toLowerCase();
				if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
					filename = URLEncoder.encode(customExampaper.getName(), "UTF-8").replace("+", " ");
				}

				response.setHeader("Content-Disposition", "inline; filename=\"" + filename + ".zip\"");
				response.setHeader("Content-Type", "application/x-zip-compressed; charset=UTF-8");

				out = response.getOutputStream();
				inputStream = new FileInputStream(zipFile);
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
		} catch (Exception e) {
			logger.error("[教师组卷导出] 导出出错，" + e.getMessage(), e);
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e2) {
				logger.error(e2.getMessage(), e2);
			}
		}
	}
}
