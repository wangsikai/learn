package com.lanking.uxb.service.question.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 提供跳转url的拼接
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月10日
 *
 */
public final class QuestionUtils {
	public static final Pattern PATTERN = Pattern.compile("<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)",
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

	public static final Pattern IMG_PATTERN = Pattern.compile("<\\s*img\\s+([^>]+)\\s*>");
	public static final String REGEX_HTML = "<[^>]+>"; // 定义HTML标签的正则表达式

	@SuppressWarnings("deprecation")
	public static String process(String s, Boolean rightAnswer, boolean renderByCoretext) {
		if (StringUtils.isNotBlank(s)) {
			// 处理填空空格
			s = s.replaceAll("<ux-blank sequence=(\\d)></ux-blank>", "_____");
			// 处理空格图片
			Matcher matcher = IMG_PATTERN.matcher(s);
			while (matcher.find()) {
				if (matcher.group(0).contains("/images/blank.png")) {
					s = s.replaceAll(matcher.group(0), "_____");
				}
			}
			// 处理图片
			List<String> imgs = parseImgSrc(s);
			for (String img : imgs) {
				if (img.startsWith("/fs")) {
					s = s.replace(img, Env.getString("fs.domain.m") + img);
				} else if (img.startsWith("/dd")) {
					s = s.replace(img, Env.getString("fs.domain.m") + "/dd" + img.replace("/dd", ""));
				}
			}
			// 处理公式
			String reg = "<ux-mth>(.+?)</ux-mth>";
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(s);
			List<String> maths = Lists.newArrayList();
			while (m.find()) {
				maths.add(m.group());
			}
			for (String math : maths) {
				String url = Env.getString("fs.domain.m") + "/f/latex/img?math=" + URLEncoder.encode(math)
						+ "&formula=1";
				if (rightAnswer != null) {
					if (rightAnswer) {
						url += "&color=00bb9d";
					} else {
						url += "&color=D90000";
					}
				}
				s = s.replace(math, "<img src=\"" + url + "\" />");
			}
		}
		if (renderByCoretext && Security.getDeviceType() == DeviceType.IOS) {
			s = processForCoreText(s);
		}
		// 去除空的公式标签
		if (StringUtils.isNotBlank(s)) {
			s = s.replaceAll("<ux-mth>[\\s]*?</ux-mth>", "");
		}
		return s;
	}

	private static String processForCoreText(String s) {
		if (StringUtils.isBlank(s)) {
			return StringUtils.EMPTY;
		}
		Matcher matcher = IMG_PATTERN.matcher(s);
		Map<String, String> map = new HashMap<String, String>();
		while (matcher.find()) {
			String value = matcher.group(0);
			String key = "#" + Codecs.md5Hex(value) + "#";
			map.put(key, value);
			s = s.replace(value, key);
		}
		// 处理&nbsp;
		s = s.replaceAll("<br>", "#uxb-br#");
		s = s.replaceAll("<br/>", "#uxb-br#");
		s = s.replaceAll("&nbsp;", " ");
		Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(s);
		s = m_html.replaceAll("");
		s = s.replaceAll("#uxb-br#", " ");
		for (String key : map.keySet()) {
			String img = map.get(key);
			List<String> srcs = parseImgSrc(img);
			if (CollectionUtils.isNotEmpty(srcs)) {
				// 临时处理iOS里面处理两个图片连在一起的bug
				img = "<img src=\"" + srcs.get(0) + "\" />  ";
			}
			s = s.replaceAll(key, img);
		}
		// 先处理html标签，再处理这种特殊情况
		s = s.replaceAll("&gt;", ">");
		s = s.replaceAll("&lt;", "<");
		return s;
	}

	private static List<String> parseImgSrc(String html) {
		Matcher matcher = PATTERN.matcher(html);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			String group = matcher.group(1);
			if (group == null) {
				continue;
			}
			if (group.startsWith("'")) {
				list.add(group.substring(1, group.indexOf("'", 1)));
			} else if (group.startsWith("\"")) {
				list.add(group.substring(1, group.indexOf("\"", 1)));
			} else {
				list.add(group.split("\\s")[0]);
			}
		}
		return list;
	}

}
