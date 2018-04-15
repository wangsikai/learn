package com.lanking.cloud.springboot.environment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lanking.cloud.springboot.listener.AbstractConfigListener;

/**
 * 获取配置
 * 
 * <pre>
 * 1.优先级 
 * 		application.properties
 * 		application-{spring.profiles.active}.properties
 *      {yoo-cloud.service.externalConfigFile}
 *      config server(ZK)
 *      java启动命令参数
 *   依次升高...
 * 2.getDynamicXXX 接口主要用于获取最新的配置(任何时候java启动命令参数优先级最高,不会被覆盖)
 * 3.node无关性(done)
 * 4.TODO micro-service相关性(doing)
 * </pre>
 * 
 * @since 3.9.7
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年5月8日
 */
public final class Env {

	public static short getShort(String key) {
		return Short.parseShort(getString(key));
	}

	public static short getDynamicShort(String key) {
		return Short.parseShort(getDynamicString(key));
	}

	public static int getInt(String key) {
		return Integer.parseInt(getString(key));
	}

	public static int getDynamicInt(String key) {
		return Integer.parseInt(getDynamicString(key));
	}

	public static long getLong(String key) {
		return Long.parseLong(getString(key));
	}

	public static long getDynamicLong(String key) {
		return Long.parseLong(getDynamicString(key));
	}

	public static boolean getBoolean(String key) {
		return Boolean.valueOf(getString(key));
	}

	public static boolean getDynamicBoolean(String key) {
		return Boolean.valueOf(getDynamicString(key));
	}

	public static String getString(String key) {
		return AbstractConfigListener.environment.getProperty(key);
	}

	public static String getDynamicString(String key) {
		if (AbstractConfigListener.dynamicConfigs.containsKey(key)) {
			return AbstractConfigListener.dynamicConfigs.get(key);
		}
		return AbstractConfigListener.environment.getProperty(key);
	}

	public static String getString(String key, Object[] args) {
		String regex = "\\[\\d+\\]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(getString(key));
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (matcher.find()) {
			matcher.appendReplacement(sb, args[i].toString());
			i++;
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static String getDynamicString(String key, Object[] args) {
		String regex = "\\[\\d+\\]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(getDynamicString(key));
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (matcher.find()) {
			matcher.appendReplacement(sb, args[i].toString());
			i++;
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
}
