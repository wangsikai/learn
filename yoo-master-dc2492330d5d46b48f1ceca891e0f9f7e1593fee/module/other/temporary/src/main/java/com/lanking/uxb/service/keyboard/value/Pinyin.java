package com.lanking.uxb.service.keyboard.value;

import java.util.List;

public class Pinyin {
	private int version = 1;
	private List<String> usual;
	private List<Dictionary> dictionary;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<String> getUsual() {
		return usual;
	}

	public void setUsual(List<String> usual) {
		this.usual = usual;
	}

	public List<Dictionary> getDictionary() {
		return dictionary;
	}

	public void setDictionary(List<Dictionary> dictionary) {
		this.dictionary = dictionary;
	}

}
