package com.lanking.uxb.service.sensitive.api;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年4月14日
 */
public class TrieNode {

	public char c;
	public int flag = 0;
	public List<TrieNode> nodes = new ArrayList<TrieNode>();

	public TrieNode(char c, int flag) {
		this.c = c;
		this.flag = flag;
	}

	public TrieNode(char c) {
		this(c, 0);
	}
}
