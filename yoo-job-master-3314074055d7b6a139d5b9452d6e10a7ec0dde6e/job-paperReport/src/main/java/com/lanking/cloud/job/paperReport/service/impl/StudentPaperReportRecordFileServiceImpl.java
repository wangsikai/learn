package com.lanking.cloud.job.paperReport.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.job.paperReport.service.ParameterService;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordFileService;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordService;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportService;
import com.lanking.cloud.sdk.util.ZipUtils;

import httl.util.StringUtils;
import sun.misc.BASE64Decoder;

@SuppressWarnings("restriction")
@Component
public class StudentPaperReportRecordFileServiceImpl implements StudentPaperReportRecordFileService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private StudentPaperReportService studentPaperReportService;
	@Autowired
	private StudentPaperReportRecordService studentPaperReportRecordService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private Environment env;

	/**
	 * 处理文件.
	 */
	@Override
	public void productFile() {
		List<StudentPaperReportRecord> list = studentPaperReportRecordService.findDataToFileList();
		String basePath = env.getProperty("channelSales.stu.paperRport.path");
		Parameter pageUrlParameter1 = parameterService.get(Product.YOOMATH, "stu.paper-report1.h5.url");
		Parameter pageUrlParameter2 = parameterService.get(Product.YOOMATH, "stu.paper-report2.h5.url");
		Parameter pageUrlParameter3 = parameterService.get(Product.YOOMATH, "stu.paper-report3.h5.url");
		Parameter pageUrlParameter4 = parameterService.get(Product.YOOMATH, "stu.paper-report4.h5.url");
		Parameter pageUrlParameter5 = parameterService.get(Product.YOOMATH, "stu.paper-report5.h5.url");
		if (pageUrlParameter1 == null || pageUrlParameter2 == null || pageUrlParameter3 == null
				|| pageUrlParameter4 == null) {
			return;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String baseUrl1 = pageUrlParameter1.getValue();
		String baseUrl2 = pageUrlParameter2.getValue();
		String baseUrl3 = pageUrlParameter3.getValue();
		String baseUrl4 = pageUrlParameter4.getValue();
		String baseUrl5 = pageUrlParameter5.getValue();
		// String baseUrl =
		// "http://m.zuoye.web.test.uxuebao.com/page/mobile/s/paper-report.html?reportId=828952531295043584";

		// 遍历记录
		List<Long> classIds = new ArrayList<Long>(list.size());
		for (StudentPaperReportRecord record : list) {
			classIds.add(record.getClassId());
		}
		Map<Long, HomeworkClazz> classMap = studentPaperReportRecordService.mgetHomeworkClazz(classIds); // 班级集合

		List<Long> zeroRecordIds = new ArrayList<Long>();
		for (StudentPaperReportRecord record : list) {
			List<StudentPaperReport> reports = studentPaperReportService.findByRecord(record.getId()); // 已生成的学生报告
			if (reports.size() == 0) {
				zeroRecordIds.add(record.getId());
				continue;
			}
			List<Long> studentIds = new ArrayList<Long>(list.size());
			for (StudentPaperReport report : reports) {
				studentIds.add(report.getStudentId());
			}
			Map<Long, Student> studentMap = studentPaperReportRecordService.mgetStudent(studentIds); // 学生集合

			// 过滤需要生成PDF的学生报告
			if (!record.isAllStudent()) {
				String needPDFStudentIds = record.getStudentIdList();
				for (int i = reports.size() - 1; i >= 0; i--) {
					if (needPDFStudentIds.indexOf(String.valueOf(reports.get(i).getStudentId())) == -1) {
						reports.remove(i);
					}
				}
			}
			if (reports.size() == 0) {
				zeroRecordIds.add(record.getId());
				continue;
			}

			List<String> pdfFilePaths = new ArrayList<String>(list.size());
			String recordPath = basePath + "/" + format.format(record.getCreateAt()) + "/" + record.getId();
			String pdfPath = recordPath + "/pdf";
			File file = new File(pdfPath);
			if (!file.exists()) {
				file.mkdirs();
			}
			String className = classMap.get(record.getClassId()) == null ? ""
					: classMap.get(record.getClassId()).getName();
			int num = 1;
			for (StudentPaperReport report : reports) {
				String studentName = studentMap.get(report.getStudentId()) == null ? ""
						: studentMap.get(report.getStudentId()).getName();
				String filePath1 = recordPath + "/" + report.getId() + "-1.jpg";
				String filePath2 = recordPath + "/" + report.getId() + "-2.jpg";
				String filePath3 = recordPath + "/" + report.getId() + "-3.jpg";
				String filePath4 = recordPath + "/" + report.getId() + "-4.jpg";
				String filePath5 = recordPath + "/" + report.getId() + "-5.jpg";
				String pdfFilePath = pdfPath + "/" + className + "--" + num + "、" + studentName + "学情报告.pdf";
				this.createImageFile(baseUrl1 + "?reportId=" + report.getId(), filePath1);
				this.createImageFile(baseUrl2 + "?reportId=" + report.getId(), filePath2);
				this.createImageFile(baseUrl3 + "?reportId=" + report.getId(), filePath3);
				this.createImageFile(baseUrl4 + "?reportId=" + report.getId(), filePath4);
				this.createImageFile(baseUrl5 + "?reportId=" + report.getId(), filePath5);
				this.buildPdfBy2Imgs(Lists.newArrayList(filePath1, filePath2, filePath3, filePath4, filePath5),
						pdfFilePath);
				pdfFilePaths.add(pdfFilePath);
				num++;
			}
			ZipUtils.zip(pdfPath, basePath + "/" + format.format(record.getCreateAt()) + "/" + record.getId() + ".zip");
			this.delFolder(recordPath); // 删除无用文件

			// 完成处理
			studentPaperReportRecordService.successFile(Lists.newArrayList(record.getId()));
		}

		if (zeroRecordIds.size() > 0) {
			// 完成处理
			studentPaperReportRecordService.successFile(zeroRecordIds);
		}
	}

	/**
	 * 调用js对网页截图，生成图片文件.
	 * 
	 * @param url
	 *            截图路径
	 * @param filePath
	 *            生成文件路径
	 * @return
	 */
	public void createImageFile(String url, String filePath) {
		String jsPath = env.getProperty("channelSales.stu.paperRport.path") + "/phantomjs/screenShot.js";
		StringBuilder builder = new StringBuilder();
		builder.append("phantomjs");
		builder.append(" ");
		builder.append(jsPath);
		builder.append(" ");
		builder.append(url);

		Runtime rt = Runtime.getRuntime();
		Process process = null;
		try {
			process = rt.exec(builder.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		BASE64Decoder decoder = new BASE64Decoder();
		InputStream is = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sbf = new StringBuffer();
		String tmp = "";
		OutputStream out = null;
		try {
			while ((tmp = br.readLine()) != null) {
				sbf.append(tmp);
			}
			byte[] b = decoder.decodeBuffer(sbf.toString());
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			out = new FileOutputStream(filePath);
			out.write(b);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("图片截图错误！ url = {}, filePath = {}", url, filePath, e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 在指定目录下,创建pdf文档
	 * 
	 * @param imgList
	 *            图片列表
	 * @param pdfPath
	 *            指定pdf 生成路径父目录
	 * @return 生成pdf文件名(全路径),或者创建失败返回null
	 * @throws Exception
	 *             输入图片文件名不存在或非法
	 */
	@SuppressWarnings("rawtypes")
	public void buildPdfBy2Imgs(List<String> imgList, String pdfPath) {
		if (imgList == null || imgList.size() < 1 || StringUtils.isBlank(pdfPath)) {
			throw new RuntimeException("build pdf,invalid input args");
		}
		Document document = null;
		PdfWriter pdfWriter = null;

		try {
			// 1 创建pdf document 对象
			document = new Document(PageSize.A4, 35, 30, 40, 40);

			// 2 创建pdfWriter对象，对document操作
			pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));

			// 3 打开pdf doc对象
			document.open();
			Iterator iter = imgList.iterator();
			Image img = Image.getInstance((String) iter.next());
			// 设置图片间距
			img.scaleAbsolute(530f, 745f);
			document.add(img);
			while (iter.hasNext()) {
				document.newPage();
				img = Image.getInstance((String) iter.next());
				// 设置图片间距
				img.scaleAbsolute(530f, 745f);
				document.add(img);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (document != null) {
					document.close();
				}
				if (pdfWriter != null) {
					pdfWriter.close();
				}
			} catch (Exception e) {
				logger.error("build pdf ocur exception: {}", e);
			}
		}
	}

	@PostConstruct
	void init() throws Exception {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath*:/phantomjs/*.*");
		String basePath = env.getProperty("channelSales.stu.paperRport.path");
		final File baseFileDir = new File(basePath + "/phantomjs");
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		if (resources != null) {
			for (Resource resource : resources) {
				String fileName = resource.getFilename();
				File ftlFile = new File(baseFileDir, fileName);
				if (ftlFile.exists()) {
					ftlFile.delete();
				}
				ftlFile.createNewFile();

				InputStream ins = resource.getInputStream();
				OutputStream os = new FileOutputStream(ftlFile);
				int bytesRead = 0;
				byte[] buffer = new byte[4096];
				while ((bytesRead = ins.read(buffer, 0, 4096)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
			}
		}
	}

	private boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	// 删除文件夹
	private void delFolder(String folderPath) {
		try {
			this.delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
