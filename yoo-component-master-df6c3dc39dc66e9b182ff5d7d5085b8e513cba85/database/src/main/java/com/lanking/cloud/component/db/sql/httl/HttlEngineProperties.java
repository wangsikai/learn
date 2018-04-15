package com.lanking.cloud.component.db.sql.httl;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "yoo-cloud.sql.httl")
public class HttlEngineProperties {

	private String tplPaths;

	private String templateDirectory;

	private String templateSuffix;

	private String compiler;

	private String loader;

	private String reloadable;

	private String preload;

	private boolean check;

	public String getTplPaths() {
		return tplPaths;
	}

	public void setTplPaths(String tplPaths) {
		this.tplPaths = tplPaths;
	}

	public String getTemplateDirectory() {
		return templateDirectory;
	}

	public void setTemplateDirectory(String templateDirectory) {
		this.templateDirectory = templateDirectory;
	}

	public String getTemplateSuffix() {
		return templateSuffix;
	}

	public void setTemplateSuffix(String templateSuffix) {
		this.templateSuffix = templateSuffix;
	}

	public String getCompiler() {
		return compiler;
	}

	public void setCompiler(String compiler) {
		this.compiler = compiler;
	}

	public String getLoader() {
		return loader;
	}

	public void setLoader(String loader) {
		this.loader = loader;
	}

	public String getReloadable() {
		return reloadable;
	}

	public void setReloadable(String reloadable) {
		this.reloadable = reloadable;
	}

	public String getPreload() {
		return preload;
	}

	public void setPreload(String preload) {
		this.preload = preload;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public Properties enginProperties() {
		Properties properties = new Properties();
		properties.setProperty("template.directory", this.templateDirectory);
		properties.setProperty("template.suffix", this.templateSuffix);
		properties.setProperty("compiler", this.compiler);
		properties.setProperty("loader", this.loader);
		properties.setProperty("reloadable", this.reloadable);
		properties.setProperty("preload", this.preload);
		properties.setProperty("check", String.valueOf(this.check));
		return properties;
	}

}
