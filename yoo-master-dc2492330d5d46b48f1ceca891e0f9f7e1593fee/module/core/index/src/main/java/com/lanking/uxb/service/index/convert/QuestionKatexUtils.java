package com.lanking.uxb.service.index.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Katex 相关工具.
 * 
 * @author wlche
 *
 */
public class QuestionKatexUtils {
	private static final Pattern TAG_PATTERN = Pattern.compile("<ux-mth>([\\s\\S]+?)</ux-mth>");
	private static final String LATEX_REG = "[a-zA-Z0-9!@\\#$%^&*\\(\\)\\[\\]\\+\\-=\\{\\}:;\\<\\>\\?/\\\\\\|\"'`~,._/\\s]*";

	private static final String textReg1 = "\\\\text[\\s]*\\{([\u4e00-\u9fa5_a-zA-Z0-9!@\\#$%^&*\\(\\)\\[\\]\\+\\-=\\{\\}:;\\<\\>\\?/\\|\"'`~,./_\\s]*)}";
	private static final String textReg2 = "\\\\text[\\s]*[\u4e00-\u9fa5]";
	private static final String[] charRegs = { "#", "$", "%" };

	/**
	 * 检查公式是否规范.
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isLatexSpecs(String content) {
		if (StringUtils.isBlank(content)) {
			return true;
		}
		Matcher matcher = TAG_PATTERN.matcher(content);
		while (matcher.find()) {
			String groupTxt = matcher.group(1);
			groupTxt = groupTxt.replaceAll(textReg1, "").replaceAll(textReg2, "");
			if (groupTxt.indexOf("\\hspace") != -1) {
				return false;
			}
			for (int i = 0; i < charRegs.length; i++) {
				String temp = groupTxt.replace("\\" + charRegs[i], "");
				if (temp.indexOf(charRegs[i]) != -1) {
					return false;
				}
			}
			if (!groupTxt.matches(LATEX_REG)) {
				return false;
			}
		}
		return true;
	}

	public static void main(String args[]) {
		String content = "<ux-mth>\\$$</ux-mth>";
		System.out.println(QuestionKatexUtils.isLatexSpecs(content));
	}
}
