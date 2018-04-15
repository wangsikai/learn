package com.lanking.uxb.service.file.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileStyle;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.domain.base.file.StyleMode;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 文件系统API
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月19日
 */
@RestController
@RequestMapping({ "f", "fs" })
public class FileSystemController extends FileBaseController {

	private Logger logger = LoggerFactory.getLogger(FileSystemController.class);

	private static final long EXPIRES = 2592000000L;
	private static long WAIT_MAX_SIZE = 20971520L;
	private static long WAIT_MAX_TIME = 10L;

	private boolean useNginx = false;

	@PostConstruct
	void init() throws Exception {
		useNginx = Env.getBoolean("file.image.usenginx");
		WAIT_MAX_SIZE = Env.getLong("file.convert.waitMaxSize");
		WAIT_MAX_TIME = Env.getLong("file.convert.waitMaxTime");
		// copy 404 and nopass
		String storePath = FileUtil.getPublicPath();
		java.io.File storePathDir = new java.io.File(storePath);
		if (!storePathDir.exists()) {
			storePathDir.mkdir();
		}
		java.io.File file404 = new java.io.File(storePath + java.io.File.separator + Env.getString("file.image.404"));
		if (!file404.exists()) {
			file404.createNewFile();
			InputStream fis = new BufferedInputStream(FileUtil.readBuiltInImage(Env.getString("file.image.404")));
			OutputStream fos = new BufferedOutputStream(new FileOutputStream(file404));
			byte[] buf = new byte[2048];
			int i = -1;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
			fis.close();
			fos.close();
		}
		java.io.File fileNopass = new java.io.File(
				storePath + java.io.File.separator + Env.getString("file.image.nopass"));
		if (!fileNopass.exists()) {
			fileNopass.createNewFile();
			InputStream fis = new BufferedInputStream(FileUtil.readBuiltInImage(Env.getString("file.image.nopass")));
			OutputStream fos = new BufferedOutputStream(new FileOutputStream(fileNopass));
			byte[] buf = new byte[2048];
			int i = -1;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
			fis.close();
			fos.close();
		}
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "{id}")
	public void file(@PathVariable("id") Long id, @RequestParam(required = false) String style,
			@RequestParam(value = "preview", defaultValue = "false") boolean preview,
			@RequestParam(value = "download", defaultValue = "false") boolean download, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			File file = fileService.getFile(id);
			logger.info("path:{}", FileUtil.getFilePath(file));
			if (download) {
				download(download, request, response, file);
			} else {
				if (file.isImage()) {
					if (StringUtils.isBlank(style)) {
						image(request, response, new java.io.File(FileUtil.getFilePath(file)), file);
					} else {
						image(id, style, request, response);
					}
				} else if (file.isAudio() || file.isVideo()) {
					media(id, request, response, preview);
				} else if (file.isBin()) {
					bin(request, response, new java.io.File(FileUtil.getFilePath(file)), file);
				}
			}

		} catch (FileException e) {
			logger.warn("file exception");
			logger.debug("file exception", e);
		} catch (Exception e) {
			logger.warn("file exception");
			logger.debug("file exception", e);
		}
	}

	/**
	 * 此接口只支持样式处理后的图片的输出(java)
	 * 
	 * @param id
	 *            图片ID
	 * @param name
	 *            style name
	 */

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "s/{name}/{id}")
	public void image(@PathVariable("id") Long id, @PathVariable("name") String name, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			File file = fileService.getFile(id);
			if (file.isImage()) {
				FileStyle fileStyle = fileStyleService.find(null, name);
				if (fileStyle != null && fileStyle.getMode() != StyleMode.QUALITY_COMPRESS
						&& fileStyle.getSpaceId() != file.getSpaceId()) {
					fileStyle = null;
				}
				String path = imageStyle(fileStyle, file);
				image(request, response, new java.io.File(path), file);
			}
		} catch (Exception e) {
			logger.warn("file io exception...");
			logger.debug("file io exception...", e);
		}
	}

	@SuppressWarnings("resource")
	private void download(boolean download, HttpServletRequest request, HttpServletResponse response, File file) {
		try {
			java.io.File nativeile = new java.io.File(FileUtil.getFilePath(file));
			String filename = new String(file.getName().getBytes(Charsets.UTF8), Charsets.ISO88591);
			String agent = request.getHeader("User-Agent").toLowerCase();
			if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0) {
				filename = URLEncoder.encode(file.getName(), "UTF-8");
			}
			response.setHeader("Content-disposition", "attachment; filename=\"" + filename + "\"");
			FileChannel fileChannel = new FileInputStream(nativeile).getChannel();
			OutputStream out = response.getOutputStream();
			WritableByteChannel outChannel = Channels.newChannel(out);
			fileChannel.transferTo(0, nativeile.length(), outChannel);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("download file error:", e);
		}

	}

	private void media(Long id, HttpServletRequest request, HttpServletResponse response, boolean preview)
			throws FileException {
		File file = fileService.getFile(id);
		if (file.getStatus() == Status.ENABLED) {
			fileAuthService.authorization(request, response);
			Space space = spaceService.getSpace(file.getSpaceId());
			if (space.isAuthorization() && !preview) {
				if (!Security.isLogin()) {
					throw new NoPermissionException();
				}
				fileAuthService.authorization(id, Security.getUserId());
			}
			if (FileUtil.getNativeFile(file).exists()) {
				String realFilePath = FileUtil.getMp4File(FileUtil.getNativeFile(file), preview).getAbsolutePath();
				if (file.getSize() <= WAIT_MAX_SIZE) {
					waitConvert(realFilePath);
				}
				response.setHeader("Content-Disposition", "inline; filename=\"" + file.getMp4Name() + "\"");
				if (file.getType() == FileType.AUDIO) {
					response.setHeader("Content-Type", "audio/mp4");
				} else {
					response.setHeader("Content-Type", "video/mp4");
				}
				response.setHeader("Content-Type", "video/mp4");
				response.setHeader("X-Accel-Redirect",
						realFilePath.replace(Env.getString("file.store.path"), "/media"));
			}
		}
	}

	private void waitConvert(String realFilePath) {
		java.io.File realFile = new java.io.File(realFilePath);
		if (!realFile.exists()) {
			// wait convert?
			int loop = 1;
			while (loop <= WAIT_MAX_TIME && !realFile.exists()) {
				realFile = null;
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					logger.warn("wait for convert exception...");
				}
				loop++;
				realFile = new java.io.File(realFilePath);
			}
		}
	}

	private String imageStyle(FileStyle fileStyle, File file) {
		String filePath = FileUtil.getFilePath(file);
		if (FileUtil.isGif(file)) {// GIF不做任何处理
			return filePath;
		}
		if (fileStyle == null) {
			return filePath;
		}
		String dealFilePath = FileUtil.getDealFilePath(file, fileStyle.getMode().getValue(), fileStyle.getWidth(),
				fileStyle.getHeight(), fileStyle.getQuality());
		styleHandle.imageStyleHandle(fileStyle, filePath, dealFilePath);
		return dealFilePath;
	}

	private void image(HttpServletRequest request, HttpServletResponse response, java.io.File f, File file)
			throws IOException {
		if (file.getStatus() != Status.ENABLED && Security.getSafeUserType().getValue() < 4) {
			f = FileUtil.getBuiltInImageFile(Env.getString("file.image.nopass"));
		} else {
			long imsTime = request.getDateHeader("If-Modified-Since");
			if (file.getUpdateAt() > imsTime) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
			if (imsTime + EXPIRES > System.currentTimeMillis()) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
			String previousToken = request.getHeader("If-None-Match");
			if (previousToken != null && previousToken.equals(Long.toString(file.getUpdateAt()))) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return;
			}
		}
		response.setHeader("ETag", Long.toString(f.lastModified()));
		response.addDateHeader("Last-Modified", System.currentTimeMillis());
		response.addDateHeader("Expires", System.currentTimeMillis() + EXPIRES);
		response.setStatus(HttpServletResponse.SC_OK);

		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		response.setHeader("Content-Type", "image/" + FileUtil.getSimpleExt(file.getName()));
		if (useNginx) {
			if (FileUtil.getNativeFile(file).exists()) {
				String realFilePath = f.getAbsolutePath();
				response.setHeader("X-Accel-Redirect", realFilePath.replace(Env.getString("file.store.path"), "/file"));
			}
		} else {
			OutputStream os = response.getOutputStream();
			FileInputStream fips = new FileInputStream(f);
			ByteArrayOutputStream bops = new ByteArrayOutputStream();
			int data = -1;
			try {
				while ((data = fips.read()) != -1) {
					bops.write(data);
				}
				os.write(bops.toByteArray());
				os.flush();
				os.close();
			} catch (Exception e) {
				logger.error("out put image stream error:", e);
			} finally {
				bops.close();
				fips.close();
				os.close();
			}
		}
	}

	@SuppressWarnings({ "resource" })
	private void bin(HttpServletRequest request, HttpServletResponse response, java.io.File f, File file)
			throws IOException {
		if (file.getStatus() == Status.ENABLED) {
			// TODO check auth
			java.io.File outFile = f;
			boolean isFlash = false;
			if (StringUtils.isNotBlank(request.getParameter("flash"))) {
				isFlash = true;
				response.setContentType("application/x-shockwave-flash");
				response.setHeader("Content-Disposition", "inline; filename=\"" + file.getSWFName() + "\"");
				outFile = FileUtil.getSWFFile(f);
			} else if (StringUtils.isNotBlank(request.getParameter("pdf"))) {
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline; filename=\"" + file.getPDFName() + "\"");
				outFile = FileUtil.getPDFFile(f);
			} else {
				response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
			}
			// 处理转换失败的情况
			if (isFlash && !outFile.exists()) {
				if (FileExt.PDF.equalsIgnoreCase(file.getExt())) {
					dhCacheService.pushPdf2Swf(file.getId());
				} else {
					dhCacheService.pushDoc2Pdf(file.getId());
				}
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					logger.warn("convert file wait fail...");
				}
			}
			if (!outFile.exists()) {
				return;
			}
			if (useNginx) {
				String realFilePath = outFile.getAbsolutePath();
				response.setHeader("X-Accel-Redirect", realFilePath.replace(Env.getString("file.store.path"), "/file"));
			} else {
				FileChannel fileChannel = new FileInputStream(outFile).getChannel();
				OutputStream out = response.getOutputStream();
				WritableByteChannel outChannel = Channels.newChannel(out);
				fileChannel.transferTo(0, f.length(), outChannel);
				out.flush();
				out.close();
				fileChannel.close();
			}
		}
	}
}
