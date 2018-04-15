package com.lanking.uxb.zycon.user.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.uxb.service.code.value.VDuty;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VTitle;

/**
 * 编辑增加所需的下拉信息
 * 
 * @author wangsenhao
 *
 */
public class VZycUserOther implements Serializable {

	private static final long serialVersionUID = 7728818319740116713L;

	private List<VTitle> titleList;
	private List<VDuty> dutyList;
	private List<VPhase> phaseList;

	public List<VPhase> getPhaseList() {
		return phaseList;
	}

	public void setPhaseList(List<VPhase> phaseList) {
		this.phaseList = phaseList;
	}

	public List<VTitle> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<VTitle> titleList) {
		this.titleList = titleList;
	}

	public List<VDuty> getDutyList() {
		return dutyList;
	}

	public void setDutyList(List<VDuty> dutyList) {
		this.dutyList = dutyList;
	}

}
