package com.lanking.uxb.service.question.api.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.FileService;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * WORD导出相关公共资源初始化.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月4日
 */
@Component
public class WordResourceInitService implements InitializingBean {

	@Autowired
	private FreeMarkerConfigurationFactory freeMarkerConfigurationFactory;
	@Autowired
	private FileService fileService;

	private Configuration configuration;
	private Template imageTemplate;
	private Template relsTemplate;
	private StreamSource streamSource;

	@Override
	public void afterPropertiesSet() throws Exception {
		configuration = freeMarkerConfigurationFactory.createConfiguration();

		final File baseFileDir = new File(Env.getString("word.template.destPath") + "/common");
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		configuration.setTemplateLoader(new FileTemplateLoader(baseFileDir));

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources1 = resolver
				.getResources("classpath*:" + Env.getString("word.template.srcPath") + "/common/*.*");
		Resource[] resources2 = resolver
				.getResources("classpath*:" + Env.getString("word.template.srcPath") + "/MML2OMML.xsl");

		if (resources1 != null) {
			for (Resource resource : resources1) {
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
		if (resources2 != null) {
			for (Resource resource : resources2) {
				String fileName = resource.getFilename();
				File ftlFile = new File(new File(Env.getString("word.template.destPath")), fileName);
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

		imageTemplate = configuration.getTemplate("image.ftl", Charsets.UTF8);
		relsTemplate = configuration.getTemplate("document.xml.rels.ftl", Charsets.UTF8);
		streamSource = new StreamSource(Env.getString("word.template.destPath") + "/MML2OMML.xsl");
	}

	public Template getImageTemplate() {
		return imageTemplate;
	}

	public Template getRelsTemplate() {
		return relsTemplate;
	}

	public StreamSource getStreamSource() {
		return streamSource;
	}

	public FileService getFileService() {
		return fileService;
	}
}
