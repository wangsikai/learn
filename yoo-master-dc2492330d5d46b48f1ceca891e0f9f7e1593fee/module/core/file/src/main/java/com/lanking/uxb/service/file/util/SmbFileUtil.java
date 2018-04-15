package com.lanking.uxb.service.file.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.springboot.environment.Env;

/**
 * Samba file 工具类
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月24日
 */
public class SmbFileUtil {
	private static Logger logger = LoggerFactory.getLogger(SmbFileUtil.class);

	public static void getSmbFile(String remoteUrl, String localDir, String ext) {
		InputStream in = null;
		OutputStream out = null;
		try {
			SmbFile smbFile = new SmbFile(remoteUrl);
			String fileName = smbFile.getName();
			File localFile = new File(localDir + File.separator + fileName + "." + ext);
			in = new BufferedInputStream(new SmbFileInputStream(smbFile));
			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[4096];
			int offset = 0;
			while ((offset = in.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, offset);
			}
		} catch (Exception e) {
			logger.error("get smb file fail:", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("output stream close fail:", e);
			}
			try {
				in.close();
			} catch (IOException e) {
				logger.error("input stream close fail:", e);
			}
		}
	}

	public static void pubSmbFile(String remoteUrl, String localFilePath) {
		InputStream in = null;
		OutputStream out = null;
		try {
			File localFile = new File(localFilePath);
			String fileName = localFile.getName();
			SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName);
			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			byte[] buffer = new byte[4096];
			while ((in.read(buffer)) != -1) {
				out.write(buffer);
			}
		} catch (Exception e) {
			logger.error("put smb file fail:", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("output stream close fail:", e);
			}
			try {
				in.close();
			} catch (IOException e) {
				logger.error("input stream close fail:", e);
			}
		}
	}

	public static String getFileSmbPath(com.lanking.cloud.domain.base.file.File file) {
		String md5Id = Codecs.md5Hex(String.valueOf(file.getId()));
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.getCreateAt()));
		return Env.getString("file.smb.path") + "/" + (c.get(Calendar.YEAR)) + "/" + (c.get(Calendar.MONTH) + 1) + "/"
				+ file.getSpaceId() + "/" + md5Id.substring(0, 2) + "/" + md5Id.substring(2, 4) + "/" + md5Id;
	}

	public static String getFileSmbDir(com.lanking.cloud.domain.base.file.File file) {
		String md5Id = Codecs.md5Hex(String.valueOf(file.getId()));
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(file.getCreateAt()));
		return Env.getString("file.smb.path") + "/" + (c.get(Calendar.YEAR)) + "/" + (c.get(Calendar.MONTH) + 1) + "/"
				+ file.getSpaceId() + "/" + md5Id.substring(0, 2) + "/" + md5Id.substring(2, 4);
	}
}