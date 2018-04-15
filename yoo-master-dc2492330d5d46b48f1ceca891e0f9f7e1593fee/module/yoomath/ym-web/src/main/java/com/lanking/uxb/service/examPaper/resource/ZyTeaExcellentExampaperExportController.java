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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.ExcellentExampaperExportService;
import com.lanking.uxb.service.examPaper.form.TeaExcellentExampaperExportForm;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsSnapshotService;

/**
 * 教师精品试卷下载服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月7日
 */
@RestController
@RequestMapping(value = "zy/mall/t/ep/exp")
@RolesAllowed(userTypes = { "TEACHER" })
public class ZyTeaExcellentExampaperExportController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ExcellentExampaperExportService excellentExampaperExportService;
	@Autowired
	private ResourcesGoodsSnapshotService resourcesGoodsSnapshotService;
	@Autowired
	private GoodsSnapshotService goodsSnapshotService;

	/**
	 * 创建精品试卷下载文档.
	 * 
	 * @param TeaCustomExampaperExportForm
	 *            参数
	 * @return
	 */
	@RequestMapping(value = "create")
	public Value createExportDoc(TeaExcellentExampaperExportForm form) {
		if (null == form || (form.getResourcesGoodsSnapshotID() == null && form.getExampaperID() == null)) {
			return new Value(new MissingArgumentException());
		}

		// 创建文档
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			int hash = excellentExampaperExportService.createTeaExcellentExampaperDoc(form);
			map.put("hash", hash);
			return new Value(map);
		} catch (Exception e) {
			logger.error("[教师精品试卷导出] 导出出错，" + e.getMessage(), e);
			return new Value(new ServerException(e));
		}
	}

	/**
	 * 下载.
	 * 
	 * @param hash
	 *            HASH值
	 * @param id
	 *            试卷的ID
	 * @param request
	 * @param response
	 */

	@RequestMapping(value = "download")
	public void download(Integer hash, Long id, Long resourcesGoodsSnapshotID, Long goodsSnapshotID,
			HttpServletRequest request, HttpServletResponse response) {
		String outName = "";
		ExamPaper exampaper = null;
		if (id != null) {
			exampaper = excellentExampaperExportService.getPaper(id);
			if (exampaper != null) {
				outName = exampaper.getName();
			}
		} else if (resourcesGoodsSnapshotID != null && goodsSnapshotID != null) {
			ResourcesGoodsSnapshot snapshot = resourcesGoodsSnapshotService.get(resourcesGoodsSnapshotID);
			GoodsSnapshot goodsSnapshot = goodsSnapshotService.get(goodsSnapshotID);
			if (snapshot != null) {
				exampaper = excellentExampaperExportService.getPaper(snapshot.getResourcesId());
				if (exampaper != null) {
					outName = goodsSnapshot.getName();
				}
			}
		}
		if (exampaper == null) {
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return;
		}
		try {
			String hashName = "h" + hash;

			// 获取文档存储路径
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String createAtStr = format.format(exampaper.getCreateAt());
			String destDir = new StringBuffer(Env.getString("word.file.store.path")).append("/tea-exampaper-ext/")
					.append(createAtStr).append("/").append(exampaper.getCreateId()).toString();

			// 判断压缩文档是否已存在
			String zipFilePath = destDir + "/" + hashName + ".zip";
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
				String filename = new String(outName.getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
				String agent = request.getHeader("User-Agent").toLowerCase();
				if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
					filename = URLEncoder.encode(outName, "UTF-8").replace("+", " ");
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
			logger.error("[教师试卷导出] 导出出错，" + e.getMessage(), e);
			try {
				response.setHeader("Content-Type", "text/html; charset=UTF-8");
				response.getWriter().write("文件不存在！");
			} catch (IOException e2) {
				logger.error(e2.getMessage(), e2);
			}
		}
	}
}
