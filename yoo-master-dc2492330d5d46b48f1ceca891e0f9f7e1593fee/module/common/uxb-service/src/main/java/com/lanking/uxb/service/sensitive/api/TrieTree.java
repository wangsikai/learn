package com.lanking.uxb.service.sensitive.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 基于前缀树的高效字符串查找算法 ,支持干扰词
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年4月14日
 */
public class TrieTree {
	private static final char[] MOLEST_CHARS = { ' ', '　', '*', '-', '_', '|', '[', ':', '(', ')' };
	private TrieNode rootNode = new TrieNode('\0');
	private Set<Character> molestChars = new HashSet<Character>();

	public TrieTree(String... words) {
		this(MOLEST_CHARS, words);
	}

	public TrieTree(char[] molestChars, String... words) {
		for (char c : MOLEST_CHARS) {
			this.molestChars.add(c);
		}
		if (molestChars != null) {
			for (char c : molestChars) {
				this.molestChars.add(c);
			}
		}
		if (words != null) {
			for (String word : words) {
				addWord(word);
			}
		}
	}

	public void addWord(String word) {
		if (StringUtils.isBlank(word))
			return;
		TrieNode node = rootNode;
		TrieNode childNode;
		char[] words = word.trim().toCharArray();
		for (int i = 0; i < words.length; i++) {
			char c = words[i];
			childNode = findNode(node, c);
			if (childNode == null) {
				childNode = new TrieNode(c, i == word.length() - 1 ? 1 : 0);
				node.nodes.add(childNode);
			}
			node = childNode;
		}
	}

	public boolean contains(String content) {
		return !search(content, true).isEmpty();
	}

	public String replace(String content) {
		return replace(content, '*');
	}

	public String replace(String content, char mask) {
		if (content == null || "".equals(content)) {
			return content;
		}
		char[] chars = content.toCharArray();
		for (Result r : search(content, false)) {
			/** 处理英文start **/
			boolean isEnglishWord = false;
			if (!isEnglishWord && r.start > 0) {
				char startChar = chars[r.start - 1];
				isEnglishWord = (startChar >= 'a' && startChar <= 'z') || (startChar >= 'A' && startChar <= 'Z');
			}
			if (!isEnglishWord && r.end < chars.length - 1) {
				char endChar = chars[r.end + 1];
				isEnglishWord = (endChar >= 'a' && endChar <= 'z') || (endChar >= 'A' && endChar <= 'Z');
			}
			if (isEnglishWord) {
				continue;
			}
			/** 处理英文end **/
			for (int i = r.start; i <= r.end; i++) {
				chars[i] = mask;
			}
		}
		return new String(chars);
	}

	/**
	 * 标黄
	 * 
	 * @param content
	 *            要处理的内容
	 * @return 处理后的内容
	 */
	public String tagYellow(String content) {
		if (content == null || "".equals(content)) {
			return content;
		}
		List<Result> results = search(content, false);
		int delta = 0;
		for (Result r : results) {
			content = content.substring(0, r.start + delta) + "<span style=\"background-color:yellow\">"
					+ content.substring(r.start + delta, r.end + 1 + delta) + "</span>"
					+ content.substring(r.end + 1 + delta);
			delta += 45;
		}
		return content;
	}

	private List<Result> search(String content, boolean breakOnMatch) {
		List<Result> result = new ArrayList<Result>();
		char[] chars = content.toCharArray();
		TrieNode node = rootNode;
		TrieNode childNode;
		int start = -1, end;
		int subSpace = 0;
		for (int i = 0, len = chars.length; i < len; i++) {
			childNode = findNode(node, chars[i]);
			if (childNode != null) {
				if (start == -1)
					start = i;
				node = childNode;
				subSpace = 0;
			} else {
				if (molestChars.contains(chars[i])) {
					subSpace += 1;
					continue;
				}
				if (node.flag == 1 && start != -1) {
					end = i - 1 - subSpace;
					result.add(new Result(start, end));
					subSpace = 0;
					if (breakOnMatch)
						return result;
				}
				TrieNode tmpNode = findNode(rootNode, chars[i]);
				if (tmpNode != null) {
					start = i;
					node = tmpNode;
				} else {
					node = rootNode;
					start = -1;
				}
			}
		}
		if (node.flag == 1 && start != -1) {
			end = chars.length - 1;
			result.add(new Result(start, end));
		}
		return result;
	}

	private TrieNode findNode(TrieNode node, char c) {
		List<TrieNode> nodes = node.nodes;
		for (TrieNode n : nodes)
			if (Character.toLowerCase(n.c) == Character.toLowerCase(c))
				return n;
		return null;
	}
}
