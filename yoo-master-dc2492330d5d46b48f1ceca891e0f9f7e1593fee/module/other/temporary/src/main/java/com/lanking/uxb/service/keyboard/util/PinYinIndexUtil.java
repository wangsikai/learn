package com.lanking.uxb.service.keyboard.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.keyboard.value.Dictionary;
import com.lanking.uxb.service.keyboard.value.Pinyin;

public class PinYinIndexUtil {

	public static void main(String[] args) throws Exception {
		Pinyin pinyin = new Pinyin();
		pinyin.setVersion(1);
		pinyin.setUsual(Lists.newArrayList("或", "和", "是", "不是", "等于", "直线", "平面", "平行"));
		List<Dictionary> dictionaries = new ArrayList<Dictionary>();
		List<String> lines = Files.readLines(new File("d:\\pinyin-index.txt"), Charset.forName("UTF-8"));
		for (String line : lines) {
			String py[] = line.split(":");
			List<String> hzs = process(py[1].trim());
			int index = 1;
			for (String hz : hzs) {
				Dictionary dictionary = new Dictionary();
				dictionary.setChinese(hz);
				dictionary.setPinyin(py[0]);
				dictionary.setIndex0(index);
				index++;
				dictionaries.add(dictionary);
			}
		}
		pinyin.setDictionary(dictionaries);
		System.err.println(JSONObject.toJSONString(pinyin));
	}

	public static List<String> process(String line) {
		List<String> hanzis = Lists.newArrayList();
		int length = line.length();
		for (int i = 0; i < length; i++) {
			String hanzi = line.substring(i, i + 1);
			if (StringUtils.isNotBlank(hanzi)) {
				hanzis.add(hanzi);
			}
		}
		return hanzis;
	}
}
