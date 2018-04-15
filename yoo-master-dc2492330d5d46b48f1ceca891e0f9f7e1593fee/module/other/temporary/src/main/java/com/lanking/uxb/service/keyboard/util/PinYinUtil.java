package com.lanking.uxb.service.keyboard.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.lanking.cloud.sdk.util.StringUtils;

public class PinYinUtil {

	public static void main(String[] args) throws Exception {
		Map<String, List<String>> pinyins = Maps.newHashMap();
		List<String> lines = Files.readLines(new File("d:\\汉字3500.txt"), Charset.forName("UTF-8"));
		for (String line : lines) {
			List<String> hanzis = process(line.trim());
			for (String hanzi : hanzis) {
				String pinyin = Pinyin4jUtil.getPinyinToLowerCase(hanzi);
				for (String py : pinyin.split(",")) {
					List<String> hzs = pinyins.get(py);
					if (hzs == null) {
						hzs = Lists.newArrayList();
					}
					hzs.add(hanzi);
					pinyins.put(py, hzs);
				}
			}
		}
		for (String key : pinyins.keySet()) {
			List<String> hzs = pinyins.get(key);
			System.err.print(key + ":");
			for (String hz : hzs) {
				System.err.print(hz);
			}
			System.err.println();
		}
	}

	public static List<String> process(String line) {
		List<String> hanzis = Lists.newArrayList();
		int length = line.length();
		for (int i = 2; i < length; i++) {
			String hanzi = line.substring(i, i + 1);
			if (StringUtils.isNotBlank(hanzi)) {
				hanzis.add(hanzi);
			}
		}
		return hanzis;
	}
}
