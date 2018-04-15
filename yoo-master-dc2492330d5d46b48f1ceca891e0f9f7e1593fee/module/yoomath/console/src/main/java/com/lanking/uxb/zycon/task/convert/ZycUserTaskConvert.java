package com.lanking.uxb.zycon.task.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.task.value.VZycUserTask;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 *
 */
@Component
public class ZycUserTaskConvert extends Converter<VZycUserTask, UserTask, Integer> {

	@Override
	protected Integer getId(UserTask s) {
		return s.getCode();
	}

	@Override
	protected VZycUserTask convert(UserTask s) {
		VZycUserTask vo = new VZycUserTask();
		vo.setCode(s.getCode());
		vo.setIcon(s.getIcon());
		vo.setName(s.getName());
		vo.setNote(s.getNote());
		vo.setCoinsNote(s.getCoinsNote());
		vo.setGrowthNote(s.getGrowthNote());
		vo.setSequence(s.getSequence());
		vo.setStatus(s.getStatus());
		vo.setType(s.getType());
		vo.setUserTaskRuleCfg(s.getUserTaskRuleCfg());
		vo.setUserType(s.getUserType());
		vo.setUserScope(s.getUserScope());
		vo.setIconUrl(FileUtil.getUrl(s.getIcon()));
		return vo;
	}
}
