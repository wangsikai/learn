package com.lanking.cloud.sdk.util;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

/**
 * zip工具类.
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年4月17日
 */
public class ZipUtils {

	/**
	 * 压缩文件/文件夹.
	 * 
	 * @param srcFileName
	 *            源文件/文件夹
	 * @param zipFileName
	 *            输出压缩文件
	 */
	public static void zip(String srcFileName, String zipFileName) {
		File srcFile = new File(srcFileName);
		if (srcFile.exists()) {
			Project prj = new Project();

			Zip zip = new Zip();
			zip.setProject(prj);
			zip.setDestFile(new File(zipFileName));

			FileSet fileSet = new FileSet();
			fileSet.setProject(prj);
			fileSet.setDir(srcFile);
			zip.addFileset(fileSet);
			zip.execute();
		}
	}

	/**
	 * 解压文件.
	 * 
	 * @param zipFileName
	 *            压缩文件
	 * @param unZipFileName
	 *            解压文件夹
	 */
	public static void unzip(String zipFileName, String unZipFileName) {
		File srcFile = new File(zipFileName);
		if (srcFile.exists()) {
			Project prj = new Project();
			Expand expand = new Expand();
			expand.setProject(prj);
			expand.setSrc(srcFile);
			expand.setDest(new File(unZipFileName));
			expand.execute();
		}
	}
}
