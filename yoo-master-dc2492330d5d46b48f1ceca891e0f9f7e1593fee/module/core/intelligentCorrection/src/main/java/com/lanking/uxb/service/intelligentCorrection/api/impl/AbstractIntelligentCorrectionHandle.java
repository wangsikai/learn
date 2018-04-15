package com.lanking.uxb.service.intelligentCorrection.api.impl;

import com.lanking.uxb.service.intelligentCorrection.api.IntelligentCorrectionHandle;

/**
 * 批改handle抽象类
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月9日
 */
public abstract class AbstractIntelligentCorrectionHandle implements IntelligentCorrectionHandle {

	public boolean isDigital(String target) {
		return target == null ? false : target.replaceAll("<ux-mth>", "").replaceAll("</ux-mth>", "").matches("[0-9]+");
	}

	public boolean isAlphabet(String target) {
		return target == null ? false : target.matches("[a-zA-Z]+");
	}
}
