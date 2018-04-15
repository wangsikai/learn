package com.lanking.uxb.rescon.basedata.value;

/**
 * 考点转换配置.
 * 
 * @author wlche
 *
 */
public class ExaminationPointOption {
	private boolean hasPhase = false;
	private boolean hasSubject = false;

	public boolean isHasPhase() {
		return hasPhase;
	}

	public void setHasPhase(boolean hasPhase) {
		this.hasPhase = hasPhase;
	}

	public boolean isHasSubject() {
		return hasSubject;
	}

	public void setHasSubject(boolean hasSubject) {
		this.hasSubject = hasSubject;
	}
}
