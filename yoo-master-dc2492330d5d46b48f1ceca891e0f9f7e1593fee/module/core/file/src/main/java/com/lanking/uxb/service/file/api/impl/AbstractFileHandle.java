package com.lanking.uxb.service.file.api.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.catalina.core.ApplicationPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.domain.base.file.api.FileConstants;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.util.UUID;
import com.lanking.uxb.service.file.api.FileHandle;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.api.ImageTransform;
import com.lanking.uxb.service.file.api.SpaceService;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.session.api.impl.Security;

import sun.misc.BASE64Decoder;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
public abstract class AbstractFileHandle implements FileHandle {

	private Logger logger = LoggerFactory.getLogger(AbstractFileHandle.class);

	@Autowired
	private FileService fileService;
	@Autowired
	private SpaceService spaceService;

	@SuppressWarnings("deprecation")
	@Override
	public void preUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
		file.setId(SnowflakeUUID.next());
		Space space = file.getSpace();
		if (space == null) {
			throw new FileException(FileException.SPACE_NOT_EXIST);
		}
		file.setSpaceId(space.getId());
		if (space.getUsedSize() >= space.getSize()) {
			throw new FileException(FileException.SPACE_SIZE_LIMIT);
		}
		try {
			if (isNetUrl(file)) {// 网络图片
				file.setName(UUID.uuid() + FileExt.SEPARATOR + FileExt.JPG);
				file.setType(FileType.IMAGE);
			} else if (isBase64Data(file)) {
				if (file.getBase64Size() > space.getMaxFileSize()) {
					throw new FileException(FileException.FILE_SIZE_LIMIT);
				}
				file.setSize(file.getBase64Size());
				file.setName(UUID.uuid() + FileExt.SEPARATOR + file.getBase64Type());
				file.setType(FileType.IMAGE);
			} else if (isMultipartFormData(request)) {
				List<Part> parts = Lists.newArrayList(request.getParts());
				ApplicationPart part = (ApplicationPart) parts.get(0);
				for (Part p : parts) {
					if (FILE_NAME.equals(p.getName())) {
						part = (ApplicationPart) p;
						break;
					}
				}
				file.setPart(part);
				if (part.getSize() > space.getMaxFileSize()) {
					throw new FileException(FileException.FILE_SIZE_LIMIT);
				}
				file.setSize(part.getSize());
				String ext = FileUtil.getSimpleExt(part.getSubmittedFileName());
				if (!FileUtil.isValid(ext)) {
					throw new FileException(FileException.FILE_TYPE_NOT_VALID);
				}
				file.setName(part.getSubmittedFileName());
				FileType type = FileUtil.getFileType(ext);
				if (file.getType() == null) {
					file.setType(type);
				} else {
					if (type == FileType.BIN || type == FileType.IMAGE) {
						file.setType(type);
					}
				}
			} else if (isApplicationOctetStream(request)) {
				if (request.getContentLength() > space.getMaxFileSize()) {
					throw new FileException(FileException.FILE_SIZE_LIMIT);
				}
				file.setSize(request.getContentLength());
				file.setName(UUID.uuid() + FileExt.SEPARATOR + FileExt.JPG);
				file.setType(FileType.IMAGE);
			} else {
				if (request.getContentLength() > space.getMaxFileSize()) {
					throw new FileException(FileException.FILE_SIZE_LIMIT);
				}
				file.setSize(request.getContentLength());
				String fileName = URLDecoder.decode(request.getHeader("x-filename"));
				file.setName(fileName);
				String ext = FileUtil.getSimpleExt(fileName);
				if (!FileUtil.isValid(ext)) {
					throw new FileException(FileException.FILE_TYPE_NOT_VALID);
				}
				FileType type = FileUtil.getFileType(ext);
				if (file.getType() == null) {
					file.setType(type);
				} else {
					if (type == FileType.BIN || type == FileType.IMAGE) {
						file.setType(type);
					}
				}
			}
			file.setCreateAt(System.currentTimeMillis());
			file.setUpdateAt(file.getCreateAt());
			file.setOwnerId(Security.getUserId());
		} catch (IllegalStateException | IOException | ServletException e) {
			logger.error("pre upload fail:", e);
			throw new ServerException();
		}
	}

	@Override
	public void postUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
		try {
			java.io.File f = new java.io.File(FileUtil.getFilePath(file));
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			if (!f.exists()) {
				f.createNewFile();
			}
			if (isNetUrl(file)) {
				InputStream is = null;
				OutputStream os = null;
				try {
					URL url = new URL(file.getUrl());
					URLConnection con = url.openConnection();
					con.setConnectTimeout(10000);
					if (con.getContentLength() > file.getSpace().getMaxFileSize()) {
						throw new FileException(FileException.FILE_SIZE_LIMIT);
					}
					is = con.getInputStream();
					byte[] bs = new byte[8192];
					int len;
					os = new FileOutputStream(f.getPath());
					while ((len = is.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
				} catch (FileException e) {
					throw new FileException(FileException.FILE_SIZE_LIMIT);
				} catch (Exception e) {
					logger.error("save net image fail:", e);
					throw new ServerException();
				} finally {
					if (os != null) {
						os.close();
					}
					if (is != null) {
						is.close();
					}
				}
				file.setSize(f.length());
			} else if (isBase64Data(file)) {
				BASE64Decoder decoder = new BASE64Decoder();
				FileOutputStream write = new FileOutputStream(f);
				String base64Data = file.getBase64Data().replaceAll("\n", "").replaceAll("\r", "");
				if (base64Data.contains("base64,")) {
					base64Data = base64Data.split("base64,")[1];
				}
				byte[] decoderBytes = decoder.decodeBuffer(base64Data);
				write.write(decoderBytes);
				write.close();
			} else if (isMultipartFormData(request)) {
				file.getPart().write(f.getAbsolutePath());
			} else if (isApplicationOctetStream(request)) {
				InputStream inputStream = request.getInputStream();
				java.io.File diskFile = new java.io.File(FileUtil.getFilePath(file));
				f.createNewFile();
				OutputStream os = new FileOutputStream(diskFile);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				inputStream.close();
			} else {
				InputStream inputStream = request.getInputStream();
				java.io.File diskFile = new java.io.File(FileUtil.getFilePath(file));
				f.createNewFile();
				OutputStream os = new FileOutputStream(diskFile);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				inputStream.close();
			}
			file.setNativePath(f.getAbsolutePath());

			java.io.File nativeFile = new java.io.File(FileUtil.getFilePath(file));
			// md5
			file.setMd5(FileUtil.getFileMD5(nativeFile));
			// crc32
			file.setCrc32(FileUtil.getFileCrc32(FileUtil.getFilePath(file)));
			// exist
			File existFile = fileService.findByMd5(file.getSpaceId(), file.getMd5());
			file.setExist(existFile != null);
			// crop
			if (file.isCrop() && file.getCropModel().isLegal()) {
				String destPath = nativeFile.getAbsolutePath();
				java.io.File original = new java.io.File(
						nativeFile.getAbsolutePath() + FileConstants.PROCESS_SEPARATOR + "original");
				Files.copy(nativeFile, original);
				nativeFile.delete();
				ImageTransform itf = new AwtImageTransform();
				itf.load(original.getAbsolutePath());
				itf.crop(file.getCropModel().getLeft(), file.getCropModel().getTop(), file.getCropModel().getW(),
						file.getCropModel().getH());
				itf.save(destPath);
				itf.close();
				original.delete();
			}
		} catch (FileException e) {
			logger.error("post upload fail:", e);
			throw new FileException(e.getCode());
		} catch (Exception e) {
			logger.error("post upload fail:", e);
			throw new ServerException();
		}
	}

	@Override
	public void afterUpload(File file) throws FileException {
		fileService.saveFile(file);
		spaceService.updateSpace(file.getSpaceId(), 1, file.getSize());
	}

	private boolean isNetUrl(File file) {
		return StringUtils.isNotBlank(file.getUrl());
	}

	private boolean isBase64Data(File file) {
		if (StringUtils.isBlank(file.getBase64Data()) || StringUtils.isBlank(file.getBase64Type())
				|| file.getBase64Size() == null) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isMultipartFormData(HttpServletRequest request) {
		if (request.getContentType() == null) {
			return false;
		} else {
			return request.getContentType().toLowerCase().contains("multipart/form-data");
		}
	}

	private boolean isApplicationOctetStream(HttpServletRequest request) {
		if (request.getContentType() == null) {
			return false;
		} else {
			return request.getContentType().toLowerCase().contains("application/octet-stream");
		}
	}

}
