package com.lanking.uxb.zycon.base.api;

import java.util.List;

import com.lanking.uxb.zycon.base.value.CTeachVersion;

/**
 * 教材版本
 * 
 * @since 2.1
 * @author wangsenhao
 *
 */
public interface ZycTeachVersionService {
	/**
	 * 悠数学对应的版本教材
	 * 
	 * @param subjectCode
	 * @return
	 */
	List<CTeachVersion> getMathTextList(Integer subjectCode, String phaseName, String flag);

	/**
	 * 更新
	 * 
	 * @param jsonlist
	 */
	void save(List<CTeachVersion> jsonlist, String phaseName);

	/**
	 * 教材版本投入应用
	 */
	void syncData();

}
