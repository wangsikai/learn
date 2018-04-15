package com.lanking.uxb.service.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.api.AbstractFileUtil;
import com.lanking.cloud.domain.base.file.api.FileConstants;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月29日
 *
 */
public class FileUtil extends AbstractFileUtil {

	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	private static final Set<String> EXTS = Sets.newHashSet();

	private static String FS_DOMAIN = null;
	private static String M_FS_DOMAIN = null;

	private static MessageDigest messagedigest = null;
	private static BaseEncoding base16 = null;

	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
			base16 = BaseEncoding.base16();
		} catch (NoSuchAlgorithmException e) {
			logger.error("file util init error:", e);
		}
	}

	private static void initFsDomain() {
		if (FS_DOMAIN == null) {
			FS_DOMAIN = Env.getString("fs.domain");
		}
		if (M_FS_DOMAIN == null) {
			M_FS_DOMAIN = Env.getString("fs.domain.m");
		}
	}

	public static Set<String> exts() {
		if (EXTS.size() == 0) {
			EXTS.addAll(FileExt.AUDIO);
			EXTS.addAll(FileExt.DOC);
			EXTS.addAll(FileExt.IMAGE);
			EXTS.addAll(FileExt.TEXT);
			EXTS.addAll(FileExt.VIDEO);
			EXTS.addAll(FileExt.ZIP);
		}
		return EXTS;
	}

	public static boolean isValid(String ext) {
		return exts().contains(ext.toLowerCase());
	}

	public static FileType getFileType(String ext) {
		if (FileExt.IMAGE.contains(ext)) {
			return FileType.IMAGE;
		} else if (FileExt.AUDIO.contains(ext)) {
			return FileType.AUDIO;
		} else if (FileExt.VIDEO.contains(ext)) {
			return FileType.VIDEO;
		} else {
			return FileType.BIN;
		}
	}

	public static String getRootPath() {
		return Env.getString("file.store.path");
	}

	public static String getFilePath(com.lanking.cloud.domain.base.file.File file) {
		String md5Id = Codecs.md5Hex(String.valueOf(file.getId()));
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.getCreateAt()));
		return getRootPath() + File.separator + (c.get(Calendar.YEAR)) + File.separator + (c.get(Calendar.MONTH) + 1)
				+ File.separator + file.getSpaceId() + File.separator + md5Id.substring(0, 2) + File.separator
				+ md5Id.substring(2, 4) + File.separator + md5Id;
	}

	public static File getNativeFile(com.lanking.cloud.domain.base.file.File file) {
		return new File(getFilePath(file));
	}

	public static String getFsDomain() {
		initFsDomain();
		return FS_DOMAIN;
	}

	public static String getMFsDomain() {
		initFsDomain();
		return M_FS_DOMAIN;
	}

	public static String getUrl(com.lanking.cloud.domain.base.file.File file) {
		if (Security.isClient()) {
			return getMFsDomain() + "/fs/" + file.getId() + "?fs=proxy";
		} else {
			if (StringUtils.isBlank(FS_DOMAIN)) {
				return getFsDomain() + "/fs/" + file.getId() + "?fs=proxy";
			}
			return getFsDomain() + "/fs/" + file.getId() + "?fs=proxy&"
					+ com.lanking.cloud.domain.base.session.api.Cookies.SECURITY_TOKEN + "=" + Security.getToken();
		}
	}

	public static String getUrl(Long id) {
		if (id == null || id == 0) {
			return StringUtils.EMPTY;
		}
		if (Security.isClient()) {
			return getMFsDomain() + "/fs/" + id + "?fs=proxy";
		} else {
			if (StringUtils.isBlank(FS_DOMAIN)) {
				return getFsDomain() + "/fs/" + id + "?fs=proxy";
			}
			return getFsDomain() + "/fs/" + id + "?fs=proxy&"
					+ com.lanking.cloud.domain.base.session.api.Cookies.SECURITY_TOKEN + "=" + Security.getToken();
		}
	}

	public static List<String> getUrl(List<Long> ids) {
		List<String> urls = new ArrayList<>(ids.size());
		for (Long id : ids) {
			urls.add(getUrl(id));
		}
		return urls;
	}

	public static String getPreviewUrl(Long id) {
		if (id == null || id == 0) {
			return StringUtils.EMPTY;
		}
		if (Security.isClient()) {
			return getMFsDomain() + "/fs/" + id + "?fs=proxy&preview=true";
		} else {
			if (StringUtils.isBlank(FS_DOMAIN)) {
				return getFsDomain() + "/fs/" + id + "?fs=proxy&preview=true";
			}
			return getFsDomain() + "/fs/" + id + "?fs=proxy&preview=true" + "&"
					+ com.lanking.cloud.domain.base.session.api.Cookies.SECURITY_TOKEN + "=" + Security.getToken();
		}
	}

	public static String getUrl(String styleName, com.lanking.cloud.domain.base.file.File file) {
		if (Security.isClient()) {
			StringBuffer sb = new StringBuffer(getMFsDomain() + "/fs");
			if (file.isImage()) {
				sb.append("/s/" + styleName);
			}
			sb.append("/" + file.getId());
			sb.append("?fs=proxy");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer(getFsDomain() + "/fs");
			if (file.isImage()) {
				sb.append("/s/" + styleName);
			}
			sb.append("/" + file.getId());
			sb.append("?fs=proxy");
			if (StringUtils.isNotBlank(FS_DOMAIN)) {
				sb.append("&" + com.lanking.cloud.domain.base.session.api.Cookies.SECURITY_TOKEN + "="
						+ Security.getToken());
			}
			return sb.toString();
		}
	}

	public static String getUrl(String styleName, Long id) {
		if (id == null || id == 0) {
			return StringUtils.EMPTY;
		}
		if (Security.isClient()) {
			StringBuffer sb = new StringBuffer(getMFsDomain() + "/fs" + "/s/" + styleName + "/" + id);
			sb.append("?fs=proxy");
			return sb.toString();
		} else {
			StringBuffer sb = new StringBuffer(getFsDomain() + "/fs" + "/s/" + styleName + "/" + id);
			sb.append("?fs=proxy");
			if (StringUtils.isNotBlank(FS_DOMAIN)) {
				sb.append("&" + com.lanking.cloud.domain.base.session.api.Cookies.SECURITY_TOKEN + "="
						+ Security.getToken());
			}
			return sb.toString();
		}
	}

	public static String getDealRootPath() {
		return getRootPath() + File.separator + "deal";
	}

	public static String getDealFilePath(com.lanking.cloud.domain.base.file.File file, int mode, int width, int height,
			int quality) {
		String md5Id = Codecs.md5Hex(String.valueOf(file.getId()));
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.getCreateAt()));
		return getDealRootPath() + File.separator + (c.get(Calendar.YEAR)) + File.separator
				+ (c.get(Calendar.MONTH) + 1) + File.separator + file.getSpaceId() + File.separator
				+ md5Id.substring(0, 2) + File.separator + md5Id.substring(2, 4) + File.separator + md5Id + "_" + mode
				+ "_" + width + "_" + height + "_" + quality;
	}

	public static String getPublicPath() {
		return FileUtil.getRootPath() + java.io.File.separator + "public";
	}

	/**
	 * 读取内置图片
	 * 
	 * @param fileName
	 *            文件名称
	 * @return
	 * @throws Exception
	 */
	public static InputStream readBuiltInImage(String fileName) throws Exception {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resource = resolver.getResources("classpath*:/" + fileName);
		InputStream fips = resource[0].getInputStream();
		return fips;
	}

	public static File getBuiltInImageFile(String fileName) {
		return new File(getPublicPath() + File.separator + fileName);
	}

	public static boolean isMp4(File file) {
		return FileExt.MP4.equalsIgnoreCase(getExt(file));
	}

	public static boolean isMp4(com.lanking.cloud.domain.base.file.File file) {
		return FileExt.MP4.equalsIgnoreCase(getSimpleExt(file.getName()));
	}

	public static boolean isPdf(File file) {
		return FileExt.PDF.equalsIgnoreCase(getExt(file));
	}

	public static boolean isPdf(com.lanking.cloud.domain.base.file.File file) {
		return FileExt.PDF.equalsIgnoreCase(getSimpleExt(file.getName()));
	}

	public static File getMp4File(File file, boolean preview) {
		if (preview) {
			return new File(file.getAbsolutePath() + FileConstants.PROCESS_SEPARATOR + FileExt.MP4
					+ FileConstants.PROCESS_SEPARATOR + FileConstants.PREVIEW_FILE_SUFFIX);
		} else {
			return new File(file.getAbsolutePath() + FileConstants.PROCESS_SEPARATOR + FileExt.MP4);
		}
	}

	public static File getSWFFile(File file) {
		return new File(file.getAbsolutePath() + "." + FileExt.SWF);
	}

	public static File getPDFFile(File file) {
		return new File(file.getAbsolutePath() + "." + FileExt.PDF);
	}

	public static String getFileMD5(File file) {
		InputStream fis = null;
		try {
			fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				messagedigest.update(buffer, 0, numRead);
			}
		} catch (FileNotFoundException e) {
			logger.error("get file md5 error:", e);
		} catch (IOException e) {
			logger.error("get file md5 error:", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				logger.error("get file md5 error:", e);
			}
		}
		byte[] digestBytes = messagedigest.digest();
		return base16.encode(digestBytes).toLowerCase();
	}

	public static long getFileCrc32(String filePath) {
		CheckedInputStream cis = null;
		long crc32 = -1;
		try {
			cis = new CheckedInputStream(new FileInputStream(filePath), new CRC32());
			byte[] buf = new byte[1024];
			while (cis.read(buf) >= 0) {
			}
			crc32 = cis.getChecksum().getValue();
		} catch (FileNotFoundException e) {
			logger.error("get file crc32 error:", e);
		} catch (IOException e) {
			logger.error("get file crc32 error:", e);
		} finally {
			try {
				if (cis != null) {
					cis.close();
				}
			} catch (IOException e) {
				logger.error("get file crc32 error:", e);
			}
		}
		return crc32;
	}

	public static boolean isGif(com.lanking.cloud.domain.base.file.File file) {
		return FileExt.GIF.equals(file.getExt());
	}
}