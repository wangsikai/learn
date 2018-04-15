package com.lanking.uxb.service.user.value;

import java.io.Serializable;
import java.util.List;

import com.lanking.uxb.service.code.value.VDuty;
import com.lanking.uxb.service.code.value.VTitle;

/**
 * 教师编辑 （职务职称列表VO）
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年6月17日 上午11:24:20
 */
public class VTEditInfo implements Serializable {

	private static final long serialVersionUID = 7464963502839106638L;
	private List<VTitle> titleList;
	private List<VDuty> dutyList;

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
