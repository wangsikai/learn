package com.lanking.uxb.service.examPaper.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 试卷下载文档配置.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月7日
 */
@Component
public class ExampaperExportConfigService implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FreeMarkerConfigurationFactory freeMarkerConfigurationFactory;

	public Map<Integer, Template> documentTemplateMap;
	public Template imageTemplate;
	public Template relsTemplate;
	public StreamSource streamSource;

	public boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list(); // 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public void copyFile(File file, String destPath) {
		if (file.exists()) {
			InputStream inputStream = null;
			FileOutputStream outputStream = null;
			FileChannel in = null;
			FileChannel out = null;
			try {
				inputStream = new FileInputStream(file);
				outputStream = new FileOutputStream(destPath);
				in = ((FileInputStream) inputStream).getChannel();
				out = outputStream.getChannel();
				in.transferTo(0, in.size(), out);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					if (out != null) {
						out.close();
					}
					if (in != null) {
						in.close();
					}
					if (outputStream != null) {
						outputStream.close();
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

	@Override
	public void afterPropertiesSet() throws Exception {
		Configuration configuration = freeMarkerConfigurationFactory.createConfiguration();

		final File baseFileDir = new File(Env.getString("word.template.destPath") + "/teacher-exampaper");
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		configuration.setTemplateLoader(new FileTemplateLoader(baseFileDir));

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver
				.getResources("classpath*:" + Env.getString("word.template.srcPath") + "/teacher-exampaper/*.*");
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

		documentTemplateMap = new HashMap<Integer, Template>(4);
		documentTemplateMap.put(TeaCustomExampaperExportForm.Sets.A3_2.getValue(),
				configuration.getTemplate("document-A3_2.ftl", Charsets.UTF8));
		documentTemplateMap.put(TeaCustomExampaperExportForm.Sets.A4.getValue(),
				configuration.getTemplate("document-A4.ftl", Charsets.UTF8));
		documentTemplateMap.put(TeaCustomExampaperExportForm.Sets.K16.getValue(),
				configuration.getTemplate("document-K16.ftl", Charsets.UTF8));
		documentTemplateMap.put(TeaCustomExampaperExportForm.Sets.K8_2.getValue(),
				configuration.getTemplate("document-K8_2.ftl", Charsets.UTF8));

		imageTemplate = configuration.getTemplate("image.ftl", Charsets.UTF8);
		relsTemplate = configuration.getTemplate("document.xml.rels.ftl", Charsets.UTF8);
		streamSource = new StreamSource(Env.getString("word.template.destPath") + "/MML2OMML.xsl");
	}
}
