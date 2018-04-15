package com.lanking.cloud.component.db.sql.httl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import httl.Engine;

public class HttlEngineFactoryBean implements FactoryBean<Engine> {
	private Logger logger = LoggerFactory.getLogger(HttlEngineFactoryBean.class);
	private Engine engine;
	private HttlEngineProperties properties;

	public void setProperties(HttlEngineProperties properties) {
		this.properties = properties;
	}

	@Override
	public Engine getObject() throws Exception {
		if (engine == null) {
			engine = Engine.getEngine(properties.enginProperties());
		}
		return engine;
	}

	@Override
	public Class<?> getObjectType() {
		return Engine.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void mergeAndCheck() {
		// create directory
		File baseFileDir = new File(properties.getTemplateDirectory());
		baseFileDir.mkdirs();
		// delete old tpl
		for (File f : baseFileDir.listFiles()) {
			f.delete();
		}
		// load tpl resources
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		String tplClasspath = new StringBuilder("classpath*:").append(properties.getTplPaths()).append("/*")
				.append(properties.getTemplateSuffix()).toString();
		Resource[] resources = null;
		try {
			resources = resolver.getResources(tplClasspath);
		} catch (IOException e) {
			logger.error("load tpl resources error:", e);
		}

		Map<String, Set<String>> allMacros = null;
		if (properties.isCheck()) {
			allMacros = Maps.newHashMap();
		}

		if (resources != null) {
			final String commentLeft = "##";
			for (Resource resource : resources) {
				FileWriter fw = null;
				BufferedReader br = null;
				try {
					File ftlFile = new File(baseFileDir, resource.getFilename());
					boolean append = false;
					if (ftlFile.exists()) {
						append = true;
					} else {
						ftlFile.createNewFile();
					}
					fw = new FileWriter(ftlFile, append);
					if (append) {
						fw.write("\n");
					}
					br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
					String s = "";
					int line = 0;
					while ((s = br.readLine()) != null) {
						line++;
						if (!s.startsWith(commentLeft)) {
							fw.write(s + "\n");
						}
						if (properties.isCheck()) {
							String s1 = s.trim();
							// check annotate grammar
							Pattern p1 = Pattern.compile("^#(?!set|if|else|for|break|macro|end)+[^#]+");
							Matcher m1 = p1.matcher(s1);
							if (m1.find()) {
								logger.error("{}->line{}->SQL annotate error [{}]", resource.getURI(), line, s1);
								System.exit(0);
							}
							// check sql
							if (Pattern.matches(".*(now|NOW)\\(\\).*", s1)) {
								logger.error("{}->line{}->exist now() or NOW()", resource.getURI(), line, s1);
								System.exit(0);
							}
							// check macro repeat
							if (s.startsWith("#macro")) {
								Set<String> macros = allMacros.get(resource.getFilename());
								if (macros == null) {
									macros = Sets.newHashSet();
								}
								Pattern p = Pattern.compile("\\((.*?)\\(");
								Matcher m = p.matcher(s);
								if (m.find()) {
									String macro = m.group(1);
									if (macros.contains(macro)) {
										logger.error("{}->line{}->SQL macro {} is repeat", resource.getURI(), line, s1);
										System.exit(0);
									}
									macros.add(macro);
								}
								allMacros.put(resource.getFilename(), macros);
							}
						}
					}
					br.close();
					fw.close();
				} catch (IOException e) {
					logger.error("sql tpl copy to templateDirectory error:", e);
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							logger.error("sql tpl copy to templateDirectory close BufferedReader fail:", e);
						}
					}
					if (fw != null) {
						try {
							fw.close();
						} catch (IOException e) {
							logger.error("sql tpl copy to templateDirectory close FileWriter fail:", e);
						}
					}
				}
			}
		}
	}

}
